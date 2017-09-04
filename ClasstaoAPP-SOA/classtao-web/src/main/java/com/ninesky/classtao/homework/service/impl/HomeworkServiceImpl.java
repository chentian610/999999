package com.ninesky.classtao.homework.service.impl;

import com.ninesky.classtao.homework.service.HomeworkService;
import com.ninesky.classtao.homework.vo.*;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
@Service("HomeworkServiceImpl")
public class HomeworkServiceImpl implements HomeworkService {
	private static Logger logger = LoggerFactory.getLogger(HomeworkServiceImpl.class);
	@Autowired
	private GeneralDAO dao;
	@Autowired
	private UserService userService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private JedisDAO jedisDao;
	
	/**
	 * 添加作业
	 * @param vo
	 */
	public HomeworkVO addHomework(HomeworkVO vo){
		//if(StringUtil.isEmpty(vo.getTitle()))
		//	throw new BusinessException(MsgService.getMsg("ENTER_HOMEWORK_TITLE_CONTENT"));
		if (StringUtil.isEmpty(vo.getItem_list()))
			throw new BusinessException(MsgService.getMsg("ENTER_HOMEWORK_ITEM_LIST_NOT_NULL"));
		//添加作业
		vo.setHomework_id(dao.insertObjectReturnID("homeworkMap.insertHomework", vo));
		addHomeworkGroup(vo.getHomework_id());
		//批量添加作业子项、子项接收者、附件
		addHomeworkItem(vo);
		List<CountVO> countList = getCountList(vo);
		vo.setCount_list(BeanUtil.ListTojson(countList)+"");
		vo.setSend_time(ActionUtil.getSysTime().getTime());
		vo.setTitle(redisService.getDictValue(vo.getCourse())+" ："+StringUtil.subString(vo.getContent(),12));
		return vo;
	}
	
	private List<CountVO> getCountList(HomeworkVO vo){
		List<CountVO> countList = new ArrayList<CountVO>();
		CountVO count = dao.queryObject("homeworkGroupMap.getCountList",vo.getHomework_id());
		String homeworkKey = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), vo.getHomework_id(), 0);
		jedisDao.hset(homeworkKey, "count", count.getCount()+"");
		jedisDao.hset(homeworkKey, "count_done", count.getCount_done()+"");
		countList.add(count);
		return countList;
	}
	
	/**
	 * 根据作业id获取指定作业
	 * @param paramMap
	 * @return
	 */
	public List<?> getHomeworkById(Map<String, String> paramMap){
		//老师获取指定作业
		if(DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())){
			List<HomeworkVO> homeworkList = dao.queryForList("homeworkMap.getHomeworkById", paramMap);
			setItemList(homeworkList);//子项列表
			setItemDoneCount(homeworkList);//统计作业完成人数
			for(HomeworkVO vo:homeworkList){//补充content.(新原型，content在子项中)
				vo.setContent(redisService.getHomeworkContent(vo.getHomework_id()));
			}
			return homeworkList;
		}else{
			//家长获取指定作业
			List<HomeworkVO> homeworkList = dao.queryForList("homeworkMap.getHomeworkById", paramMap);
			setRecItemList(homeworkList);//子项列表
			for(HomeworkVO vo:homeworkList){//补充content.(新原型，content在子项中)
				vo.setContent(redisService.getHomeworkContent(vo.getHomework_id()));
			}
			return homeworkList;
		}
	}	
	
	/**
	 * 获取作业附件列表
	 * @param vo
	 * @return
	 */
	public List<HomeworkFileVO> getFileList(HomeworkFileVO vo) {
		return dao.queryForList("homeworkFileMap.getHomeworkFileList", vo);
	}
	
	/**
	 * 设置子条目完成标记
	 * @param vo
	 * @throws ParseException 
	 */
	public void updateItemDone(HomeworkReceiveVO vo) throws ParseException {
		HomeworkItemDoneVO itemDone = new HomeworkItemDoneVO();
		itemDone.setItem_id(vo.getItem_id());
		itemDone.setHomework_id(vo.getHomework_id());
		itemDone.setContent(vo.getContent());
		itemDone.setStudent_id(vo.getStudent_id());
		itemDone.setIs_done(vo.getIs_done());
		itemDone.setDone_date(ActionUtil.getSysTime());
		itemDone.setCreate_by(ActionUtil.getUserID());
		itemDone.setCreate_date(ActionUtil.getSysTime());
		//判断学生是否重复提交
		if (dao.queryObject("homeworkItemDoneMap.getHomeworkItemDone", itemDone)!=null) {
			dao.updateObject("homeworkItemDoneMap.updateHomeworkItemDone", itemDone);
			dao.deleteObject("homeworkFileDoneMap.deleteHomeworkFileDoneList", itemDone);
			if (StringUtil.isEmpty(vo.getFile_list())) return;
			List<HomeworkFileDoneVO> fileList = BeanUtil.jsonToList(vo.getFile_list(), HomeworkFileDoneVO.class);
			for (HomeworkFileDoneVO homeworkFileDoneVO : fileList) {
				homeworkFileDoneVO.setItem_id(vo.getItem_id());
				homeworkFileDoneVO.setHomework_id(vo.getHomework_id());
				homeworkFileDoneVO.setStudent_id(vo.getStudent_id());
				dao.insertObjectReturnID("homeworkFileDoneMap.insertHomeworkFileDone",homeworkFileDoneVO);
			}
			return;
		}
		itemDone.setId(dao.insertObjectReturnID("homeworkItemDoneMap.insertHomeworkItemDone", itemDone));
		if (StringUtil.isNotEmpty(vo.getFile_list())){
			HomeworkFileDoneVO fileDone = new HomeworkFileDoneVO();
			fileDone.setHomework_id(vo.getHomework_id());
			fileDone.setItem_id(vo.getItem_id());
			fileDone.setStudent_id(vo.getStudent_id());
			fileDone.setCreate_by(ActionUtil.getUserID());
			fileDone.setCreate_date(ActionUtil.getSysTime());
			List<HomeworkFileDoneVO> fileList = BeanUtil.jsonToList(vo.getFile_list(), HomeworkFileDoneVO.class);
			for (HomeworkFileDoneVO homeworkFileDoneVO : fileList) {
				fileDone.setFile_name(homeworkFileDoneVO.getFile_name());
				fileDone.setFile_type(homeworkFileDoneVO.getFile_type());
				fileDone.setFile_url(homeworkFileDoneVO.getFile_url());
				fileDone.setFile_resize_url(homeworkFileDoneVO.getFile_resize_url());
				fileDone.setPlay_time(homeworkFileDoneVO.getPlay_time());
				fileDone.setId(dao.insertObjectReturnID("homeworkFileDoneMap.insertHomeworkFileDone", fileDone));
			}
		}
		updateHomeworkDone(vo);
	}
	//完成所以子项作业时添加完成作业记录
	private void updateHomeworkDone(HomeworkReceiveVO vo) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("homework_id", vo.getHomework_id());
		map.put("student_id", vo.getStudent_id());
		map.put("school_id", vo.getSchool_id());
		if (dao.queryObject("homeworkReceiveMap.getHomeworkByMap", map)!=null) return;
		CountVO countVO = dao.queryObject("homeworkItemMap.getCountByHomeworkID", vo); 
		if (countVO.getCount()!=countVO.getCount_done()) return;
		vo.setIs_submit(vo.getIs_done());
		vo.setSubmit_time(ActionUtil.getSysTime());
		vo.setReceive_id(dao.insertObjectReturnID("homeworkReceiveMap.insertHomeworkReceiveBatch", vo));
		addHomeworkStuToRedis(vo);
		
	}
	/**
	 * 获取作业完成情况
	 * @param paramMap
	 * @return
	 */
	public List<HomeworkReceiveVO> getHomeworkDoneList(Map<String, String> paramMap) {
		List<HomeworkReceiveVO> homeworkReceive = new ArrayList<HomeworkReceiveVO>();
		List<StudentVO> studentList = dao.queryForList("homeworkGroupMap.getStudentListByMap", paramMap);
		for (StudentVO student : studentList) {
			HomeworkReceiveVO homeworkReceiveVO = new HomeworkReceiveVO();
			homeworkReceiveVO.setStudent_name(redisService.getUserName(Integer.parseInt(paramMap.get("school_id")), DictConstants.USERTYPE_STUDENT, 0, student.getStudent_id()));
			homeworkReceiveVO.setHead_url(redisService.getUserHeadUrl(Integer.parseInt(paramMap.get("school_id")), DictConstants.USERTYPE_STUDENT, 0, student.getStudent_id()));
			homeworkReceiveVO.setStudent_id(student.getStudent_id());
			homeworkReceiveVO.setSchool_id(Integer.parseInt(paramMap.get("school_id")));
			paramMap.put("student_id", student.getStudent_id()+"");
			homeworkReceiveVO.setItem_list(getHomeworkItemDoneList(paramMap));
			homeworkReceive.add(homeworkReceiveVO);
		}
		return homeworkReceive;
	}
		
	/**
	 * 获取每一个学生的作业子项的完成情况和完成内容、图片等
	 * @param paramMap
	 * @return
	 */
	private String getHomeworkItemDoneList(Map<String, String> paramMap){
		List<HomeworkItemDoneVO> itemDone = dao.queryForList("homeworkItemDoneMap.getHomeworkItemDonelist", paramMap);
		if (ListUtil.isEmpty(itemDone)) return "";
		for (HomeworkItemDoneVO homeworkItemDoneVO : itemDone) {
			List<HomeworkFileDoneVO> fileDone = dao.queryForList("homeworkFileDoneMap.getHomeworkFileDoneList", paramMap);
			homeworkItemDoneVO.setFile_list(BeanUtil.ListTojson(fileDone)+"");
		}
		return BeanUtil.ListTojson(itemDone)+"";
	}
	/**
	 * 获取未读数量
	 * @param paramMap
	 * @return
	 */
	public Integer getUnreadCount(Map<String, String> paramMap) {
		return dao.queryObject("homeworkReceiveMap.getUnreadCount", paramMap);
	}
	
	/**
	 * 作业未完成提醒
	 * @param vo
	 */
	public void addRemind(HomeworkReceiveVO vo){
		//提醒内容
		InfoVO infoVO = setRemindInfo(vo);
		//发送提醒
		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
		receiveList.add(setStuRemindReceive(vo, infoVO));
		infoVO.setTransmissionContent(MsgService.getMsg("remindHomework", redisService.getUserName(vo.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, vo.getStudent_id()),
				schoolService.getSchoolById(ActionUtil.getSchoolID()).getSchool_name()));
		infoService.addInfo(infoVO, receiveList);
	}

	//批量添加作业子项、子项接收者、附件
	private void addHomeworkItem(HomeworkVO vo){
		if(StringUtil.isNotEmpty(vo.getItem_list()))
		{
			HomeworkItemVO itemVO = new HomeworkItemVO();
			itemVO.setHomework_id(vo.getHomework_id());
			itemVO.setEnd_date(vo.getEnd_date());
			itemVO.setTitle(vo.getTitle());
			itemVO.setCreate_by(vo.getCreate_by());
			itemVO.setCreate_date(vo.getCreate_date());
			List<HomeworkItemVO> list = BeanUtil.jsonToList(vo.getItem_list(),HomeworkItemVO.class);
			for (HomeworkItemVO homeworkItemVO:list) {
				//添加子项
				itemVO.setContent(homeworkItemVO.getContent());
				if (StringUtil.isNotEmpty(homeworkItemVO.getFile_list()))
					itemVO.setFile_list(homeworkItemVO.getFile_list());
				itemVO.setItem_id(dao.insertObjectReturnID(	"homeworkItemMap.insertHomeworkItem", itemVO));
				//添加附件
				addHomeworkFileBatch(itemVO);
			}
			vo.setContent(list.get(0).getContent());
		}
	}
	
	//批量添加作业附件
	private void addHomeworkFileBatch(HomeworkItemVO vo){		
		if(StringUtil.isEmpty(vo.getFile_list())) return;
		List<HomeworkFileVO> fileVOList 
			= BeanUtil.jsonToList(vo.getFile_list(), HomeworkFileVO.class);
		for (HomeworkFileVO fileVO : fileVOList) {
			fileVO.setHomework_id(vo.getHomework_id());
			fileVO.setItem_id(vo.getItem_id());
			fileVO.setCreate_by(vo.getCreate_by());
			fileVO.setCreate_date(vo.getCreate_date());
			dao.insertObjectReturnID("homeworkFileMap.insertHomeworkFlie", fileVO);
		}
	}
	
	//封装动态消息
	private InfoVO setInfo(HomeworkVO vo){
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoVO.setInfo_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		infoVO.setModule_code(DictConstants.MODULE_CODE_HOMEWORK);
		infoVO.setModule_pkid(vo.getHomework_id());
		infoVO.setInfo_title(vo.getTitle());
		infoVO.setInfo_content(vo.getContent());
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		return infoVO;
	}
	
	//统计作业提交和未提交数量
	private String setHomeworkCount(Integer homework_id){
		String homeworkKey = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), homework_id,0);
		String count = jedisDao.hget(homeworkKey,"count");
		if (StringUtil.isNotEmpty(count)) {
			CountVO countVO = new CountVO();
			countVO.setCount(Integer.parseInt(count));
			countVO.setCount_done(Integer.parseInt(jedisDao.hget(homeworkKey, "count_done")));
			return "["+BeanUtil.beanToJson(countVO)+"]";
		}
		CountVO countVO = dao.queryObject("homeworkGroupMap.getCountList", homework_id);
		jedisDao.hset(homeworkKey, "count", countVO.getCount()+"");
		jedisDao.hset(homeworkKey, "count_done", countVO.getCount_done()+"");
		return "["+BeanUtil.beanToJson(countVO)+"]";
	}
	//统计作业子项目提交和未提交数量
	private void setItemDoneCount(List<HomeworkVO> homeworkList){
		if(ListUtil.isEmpty(homeworkList)) return;
		for(HomeworkVO item : homeworkList){
			List<CountVO> countList = dao.queryForList("homeworkGroupMap.getCountList", item.getHomework_id());
			List<HomeworkItemVO> ItemList = dao.queryForList("homeworkItemMap.getItemListByHomeworkId", item.getHomework_id());
			for (HomeworkItemVO homeworkItemVO : ItemList) {
                Integer count_done = dao.queryObject("homeworkItemDoneMap.getCountList", homeworkItemVO.getItem_id());
				//获取该作业子项的完成人数
				CountVO countVO = new CountVO();
                countVO.setCount_done(IntegerUtil.isEmpty(count_done)? 0 : count_done);
                countVO.setCount(countList.get(0).getCount());
				countList.add(countVO);
			}
			item.setCount_list(BeanUtil.ListTojson(countList)+"");
		}
	}	
	
	//老师端返回作业的子项列表
	private void setItemList(List<HomeworkVO> homeworkList){
		if(ListUtil.isEmpty(homeworkList)) return;
		List<HomeworkItemVO> itemList = null;
		for(HomeworkVO homework : homeworkList){
			itemList = dao.queryForList("homeworkItemMap.getItemDoneByID", homework);
			if(ListUtil.isEmpty(itemList)) continue;
			setFileList(itemList);//设置三条附件
			homework.setItem_list(BeanUtil.ListTojson(itemList)+"");
		}
	}
	//学生端返回作业的子项列表
	private void setRecItemList(List<HomeworkVO> homeworkList){
		if(ListUtil.isEmpty(homeworkList)) return;
		List<HomeworkItemVO> itemList = null;
		for(HomeworkVO homework : homeworkList){
			itemList = dao.queryForList("homeworkItemMap.getItemDoneByID", homework);
			for (HomeworkItemVO homeworkItemVO : itemList) {
				homeworkItemVO.setStudent_id(ActionUtil.getStudentID());
				HomeworkItemDoneVO itemDone = dao.queryObject("homeworkItemDoneMap.getHomeworkItemDone", homeworkItemVO);
				if (itemDone == null) continue;
				homeworkItemVO.setIs_done(itemDone.getIs_done());
				homeworkItemVO.setDone_time(DateUtil.formatDateToString(itemDone.getDone_date(),"yyyy-MM-dd"));
			}
			if(ListUtil.isEmpty(itemList)) continue;
			setFileList(itemList);//设置三条附件
			homework.setItem_list(BeanUtil.ListTojson(itemList)+"");
		}
	}
	
	//为子项列表设置附件
	private void setFileList(List<HomeworkItemVO> itemList){
		if(ListUtil.isEmpty(itemList)) return;
		//ActionUtil.getActionParam().setLimit(Constants.DEFAULT_FILE_LIMIT);
		List<HomeworkFileVO> fileList = null;
		HomeworkFileVO file = new HomeworkFileVO();
		for(HomeworkItemVO itemVO : itemList)
		{	
			file.setHomework_id(itemVO.getHomework_id());
			file.setItem_id(itemVO.getItem_id());
			fileList = dao.queryForList("homeworkFileMap.getHomeworkFileList", file);
			if(ListUtil.isEmpty(fileList)) continue;
			itemVO.setFile_list(BeanUtil.ListTojson(fileList)+"");
		}
	}
	
	//设置提醒消息
	private InfoVO setRemindInfo(HomeworkReceiveVO vo){
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoVO.setInfo_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		infoVO.setModule_code(DictConstants.MODULE_CODE_HOMEWORK);
		infoVO.setModule_pkid(vo.getHomework_id());
		infoVO.setInfo_title("您有一条未完成的作业："+vo.getTitle()+",请在"+vo.getEnd_date()+"前完成！");
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		return infoVO;
	}
	//学生接收提醒
	private InfoReceiveVO setStuRemindReceive(HomeworkReceiveVO vo, InfoVO infoVO){
		StudentVO student = userService.getStudentById(vo.getStudent_id());
		if(student==null)
			throw new BusinessException("没有找到该学生....");
		InfoReceiveVO recVO = new InfoReceiveVO();
		recVO.setSchool_id(student.getSchool_id());
		recVO.setGrade_id(student.getGrade_id());
		recVO.setClass_id(student.getClass_id());
		recVO.setUser_id(0);
		recVO.setStudent_id(vo.getStudent_id());
		recVO.setModule_code(infoVO.getModule_code());
		recVO.setModule_pkid(infoVO.getModule_pkid());
		recVO.setUser_type(DictConstants.USERTYPE_STUDENT);
		recVO.setReceive_name(redisService.getUserName(student.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, student.getStudent_id()));
		recVO.setInfo_date(infoVO.getInfo_date());
		recVO.setInfo_type(infoVO.getInfo_type());
		recVO.setInfo_title(infoVO.getInfo_title());
		recVO.setInfo_content(infoVO.getInfo_content());
		recVO.setShow_type(infoVO.getShow_type());
		recVO.setInit_data(infoVO.getInit_data());
		recVO.setCreate_date(infoVO.getCreate_date());
		recVO.setCreate_by(infoVO.getCreate_by());
		return recVO;
	}
//	//家长接收作业未完成提醒
//	private List<InfoReceiveVO> setParRemindReceive(HomeworkReceiveVO vo, InfoVO infoVO){
//		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
//		ParentVO parent = new ParentVO();
//		parent.setStudent_id(vo.getStudent_id());
//		List<ParentVO> parents = userService.getParUserList(parent);
//		if(!ListUtil.isEmpty(parents)) return receiveList;
//		for(ParentVO parVO : parents)
//		{	
//			if(parVO.getUser_id()==null) continue;
//			InfoReceiveVO recVO = new InfoReceiveVO();
//			recVO.setSchool_id(parVO.getSchool_id());
//			recVO.setGrade_id(parVO.getGrade_id());
//			recVO.setClass_id(parVO.getClass_id());
//			recVO.setUser_id(parVO.getUser_id());
//			recVO.setModule_code(infoVO.getModule_code());
//			recVO.setModule_pkid(infoVO.getModule_pkid());
//			recVO.setUser_type(DictConstants.USERTYPE_PARENT);
//			recVO.setReceive_name(parVO.getParent_name());
//			recVO.setInfo_date(DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd"));
//			recVO.setInfo_type(infoVO.getInfo_type());
//			recVO.setInfo_title(infoVO.getInfo_title());
//			recVO.setInfo_content(infoVO.getInfo_content());
//			recVO.setShow_type(infoVO.getShow_type());
//			recVO.setInit_data(infoVO.getInit_data());
//			recVO.setCreate_date(ActionUtil.getSysTime());
//			recVO.setCreate_by(ActionUtil.getUserID());
//			receiveList.add(recVO);
//		}
//		return receiveList;
//	}
//	//家长接收动态
//	private List<InfoReceiveVO> setInfoReceivePar(InfoVO infoVO, String classId){
//		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
//		ParentVO parVO = new ParentVO();
//		parVO.setSchool_id(infoVO.getSchool_id());
//		parVO.setClass_id(Integer.parseInt(classId));
//		List<ParentVO> parList = userService.getParUserList(parVO);
//		if(ListUtil.isEmpty(parList)) return receiveList;
//		for(ParentVO parent : parList)
//		{	
//			//用户没注册就跳过不发送
//			if(parent.getUser_id()==null) continue;
//			InfoReceiveVO recVO = new InfoReceiveVO();
//			recVO.setSchool_id(parent.getSchool_id());
//			recVO.setGrade_id(parent.getGrade_id());
//			recVO.setClass_id(parent.getClass_id());
//			recVO.setUser_id(parent.getUser_id());
//			recVO.setModule_code(infoVO.getModule_code());
//			recVO.setModule_pkid(infoVO.getModule_pkid());
//			recVO.setUser_type(DictConstants.USERTYPE_PARENT);
//			recVO.setReceive_name(parent.getParent_name());
//			recVO.setInfo_date(infoVO.getInfo_date());
//			recVO.setInfo_type(infoVO.getInfo_type());
//			recVO.setInfo_title(infoVO.getInfo_title());
//			recVO.setInfo_content(infoVO.getInfo_content());
//			recVO.setShow_type(infoVO.getShow_type());
//			recVO.setInit_data(infoVO.getInit_data());
//			recVO.setPhoto_list(infoVO.getPhoto_list());
//			recVO.setCreate_date(infoVO.getCreate_date());
//			recVO.setCreate_by(infoVO.getCreate_by());
//			receiveList.add(recVO);
//		}
//		return receiveList;
//	}

	public void addHomeworkGroup(Integer homework_id) {
		List<HomeworkGroupVO> homeworklist = BeanUtil.jsonToList(ActionUtil.getParameter("receive_list"), HomeworkGroupVO.class);
		for (HomeworkGroupVO homeworkGroupVO : homeworklist) {
			HomeworkGroupVO homework = new HomeworkGroupVO();
			homework.setHomework_id(homework_id);
			homework.setSchool_id(ActionUtil.getSchoolID());
			homework.setUser_id(ActionUtil.getUserID());
			homework.setTeam_id(homeworkGroupVO.getTeam_id());
			homework.setGroup_id(homeworkGroupVO.getGroup_id());
			homework.setUser_type(homeworkGroupVO.getUser_type());
			homework.setTeam_type(homeworkGroupVO.getTeam_type());
			homework.setCreate_by(ActionUtil.getUserID());
			homework.setCreate_date(ActionUtil.getSysTime());
			homework.setId(dao.insertObjectReturnID("homeworkGroupMap.insertHomeworkGroup", homework));
			addHomeworkSumToRedis(homeworkGroupVO.getGroup_id(),homeworkGroupVO.getTeam_id(),homeworkGroupVO.getTeam_type());
		}
	}
	
	@Override
	public List<?> getHomeworkGroupList(Map<String, String> paramMap) {
		List<HomeworkVO> homeworkList = null;
		if (StringUtil.isEmpty(paramMap.get("student_id"))) {
			homeworkList = dao.queryForList("homeworkGroupMap.getHomeworkGroupList", paramMap);
			if (ListUtil.isEmpty(homeworkList)) return homeworkList;
			for(HomeworkVO vo:homeworkList){//补充content.(新原型，content在子项中)
				vo.setContent(redisService.getHomeworkContent(vo.getHomework_id()));
				vo.setCount_list(setHomeworkCount(vo.getHomework_id()));//统计作业完成人数
			}	
			return homeworkList;
		} else {
			paramMap.remove("user_id");//因为user_id是教师端获取作业列表的条件,而家长端不需要这个条件,会影响家长端获取作业列表,所以删掉.
			homeworkList = dao.queryForList("homeworkGroupMap.getHomeworkGroupList", paramMap);
			if (ListUtil.isEmpty(homeworkList)) return homeworkList;
			for(HomeworkVO vo:homeworkList){//补充content.(新原型，content在子项中)
				paramMap.put("homework_id", vo.getHomework_id()+"");
				vo.setContent(redisService.getHomeworkContent(vo.getHomework_id()));
				vo.setSender_name(redisService.getUserName(vo.getSchool_id(), DictConstants.USERTYPE_TEACHER, vo.getSender_id(), 0));
				vo.setIs_submit(getHomeworkIsSubmit(paramMap));
			}
			return homeworkList;
		}		
	}
	//获取每条作业的完成情况
	private Integer getHomeworkIsSubmit(Map<String, String> paramMap){
		String key = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), Integer.parseInt(paramMap.get("homework_id")),ActionUtil.getStudentID());
		String is_submit = jedisDao.hget(key, "is_submit");
		if (StringUtil.isNotEmpty(is_submit)) return Integer.parseInt(is_submit);
		Integer isSubmit = dao.queryObject("homeworkReceiveMap.getHomeworkByMap", paramMap)==null ?0:1;
		jedisDao.hset(key, "is_submit", isSubmit+"");
		return isSubmit;
	}

	@Override
	public HomeworkReceiveVO getHomeworkDoneListByID(Map<String, String> paramMap) {
		HomeworkReceiveVO homeworkReceive = new HomeworkReceiveVO();
		homeworkReceive.setStudent_name(redisService.getUserName(Integer.parseInt(paramMap.get("school_id")), DictConstants.USERTYPE_STUDENT, 0, Integer.parseInt(paramMap.get("student_id"))));
		homeworkReceive.setHead_url(redisService.getUserHeadUrl(Integer.parseInt(paramMap.get("school_id")), DictConstants.USERTYPE_STUDENT, 0, Integer.parseInt(paramMap.get("student_id"))));
		List<HomeworkItemDoneVO> itemDone = dao.queryForList("homeworkItemDoneMap.getHomeworkItemDonelist", paramMap);
		if (ListUtil.isEmpty(itemDone)) return homeworkReceive;
		for (HomeworkItemDoneVO homeworkItemDoneVO : itemDone) {
			List<HomeworkFileDoneVO> fileDone = dao.queryForList("homeworkFileDoneMap.getHomeworkFileDoneList", homeworkItemDoneVO);
			homeworkItemDoneVO.setFile_list(BeanUtil.ListTojson(fileDone)+"");
		}
		homeworkReceive.setItem_list(BeanUtil.ListTojson(itemDone)+"");
		return homeworkReceive;
	}

	@Override
	public List<HashMap<String, Object>> getHomeworkCountFromRedis(TableVO vo) {
		List<HashMap<String,Object>> list = getListFromClassroom(vo);
		TableHeadVO tableHead = new TableHeadVO();
		tableHead.setCount_type(vo.getCount_type());
		tableHead.setSchool_id(ActionUtil.getSchoolID());
		tableHead.setTeam_type(vo.getTeam_type());
		tableHead.setScore_type(vo.getScore_type());
		List<TableHeadVO> field_list = dao.queryForList("homeworkTableMap.getHomeworkTableList", vo);
		String key = getHomeworkDateKey(vo);
		for (HashMap<String,Object> stuMap: list) {
			if (StringUtil.isNotEmpty(ActionUtil.getParameter("course"))) {//查询每个班级科目的作业数量
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {//查询班级中每个人每个科目作业的数量
					String teamKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(ActionUtil.getParameter("group_id")),Integer.parseInt(ActionUtil.getParameter("team_id")),vo.getTeam_type(),ActionUtil.getParameter("course"));
					stuMap.put("submit_count", jedisDao.hget(teamKey+key, "submit_count"));
				} else {//查询每个班级每个科目作业的数量
					String teamKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(stuMap.get("group_id")+""),Integer.parseInt(stuMap.get("team_id")+""),vo.getTeam_type(),ActionUtil.getParameter("course"));
					stuMap.put("submit_count", jedisDao.hget(teamKey+key, "submit_count"));
				}	
			} else {//查询每个班级的作业数量
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {//查询班级中每个人作业的数量
					String teamKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(ActionUtil.getParameter("group_id")),Integer.parseInt(ActionUtil.getParameter("team_id")),vo.getTeam_type(),"");
					stuMap.put("submit_count", jedisDao.hget(teamKey+key, "submit_count"));
				} else {//查询每个班级作业的数量
					String teamKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(stuMap.get("group_id")+""),Integer.parseInt(stuMap.get("team_id")+""),vo.getTeam_type(),"");
					stuMap.put("submit_count", jedisDao.hget(teamKey+key, "submit_count"));
				}	
			}	
			String queryKey = "";
			//判断是否查询每个班级作业的完成情况
			if (IntegerUtil.isNotEmpty(vo.getTeam_id())){
				//判断是否查询每个班级的科目作业完成情况
				if (StringUtil.isNotEmpty(ActionUtil.getParameter("course"))) queryKey += RedisKeyUtil.getHomeworkStudentCountKey(ActionUtil.getSchoolID(),vo.getGroup_id(),vo.getTeam_id(), (Integer) stuMap.get("student_id"),vo.getTeam_type(),ActionUtil.getParameter("course"));
				else queryKey += RedisKeyUtil.getHomeworkStudentCountKey(ActionUtil.getSchoolID(),vo.getGroup_id(),vo.getTeam_id(), (Integer) stuMap.get("student_id"),vo.getTeam_type(),"");
			} else {
				if (StringUtil.isNotEmpty(ActionUtil.getParameter("course"))) queryKey += RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(stuMap.get("group_id")+""),Integer.parseInt(stuMap.get("team_id")+""),vo.getTeam_type(),ActionUtil.getParameter("course"));
				else queryKey += RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), Integer.parseInt(stuMap.get("group_id")+""),Integer.parseInt(stuMap.get("team_id")+""),vo.getTeam_type(),"");
			}
			queryKey += key;
			Map<String,String> dataMap = jedisDao.hgetAll(queryKey);
			stuMap.putAll(dataMap);
			for (TableHeadVO field:field_list) 
			{
				if (StringUtil.isEmpty(field.getField_func())) continue;
				stuMap.put(field.getField(), getFunctionValue(field.getField_func(),stuMap));
			}
		}
		return list;
	}
	
	private List<HashMap<String,Object>> getListFromClassroom(TableVO vo) {
		if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type()))
			if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type())) return dao.queryForList("tableMap.getStudentListByContactID",vo.getTeam_id());
			else return dao.queryForList("tableMap.getStudentListByClassID",vo.getTeam_id());
		List<HashMap<String,Object>> list =	dao.queryForList("tableMap.getClassListBySchoolID",vo);
		for (HashMap<String,Object> map:list) {
			if (IntegerUtil.isEmpty(Integer.parseInt(map.get("team_count")+""))) continue;
			map.put("team_name", redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,(Integer)map.get("group_id"),(Integer) map.get("team_id")));
		}
		return list;
	}
	
	/**
	 * 生成作业的Key
	 * @param vo
	 * @return
	 */
	private String getHomeworkDateKey(TableVO vo) {
		String key;
		if (StringUtil.isEmpty(vo.getScore_date())) 
			vo.setScore_date(DateUtil.getNow(DateUtil.YMD));
		switch (vo.getSum_type()) {
			case DictConstants.SUM_TYPE_DAY:
				key = ":DAY"+ vo.getScore_date().replaceAll("-", "");	
				break;
			case DictConstants.SUM_TYPE_WEEK:
				key = ":WEEK"+ DateUtil.getFirstDayOfWeek(vo.getScore_date());	
				break;
			case DictConstants.SUM_TYPE_MONTH:
				key = ":MONTH"+ DateUtil.getFirstDayOfMonth(DateUtil.smartFormat(vo.getScore_date()));	
				break;
			case DictConstants.SUM_TYPE_TERM:
				key = ":TERM"+ DateUtil.getYearAndTerm(DateUtil.smartFormat(vo.getScore_date()));
				break;
			case DictConstants.SUM_TYPE_YEAR:
				key = ":YEAR"+ DateUtil.getFirstDayOfYear(DateUtil.smartFormat(vo.getScore_date()));
				break;
			default: key = "";
		}
		return key;
	}
	
	/**
	 * 函数：当前只支持简单的函数，加减乘除
	 * @param field_func
	 * @param dataMap
	 * @return
	 */
	private Object getFunctionValue(String field_func,HashMap<String, Object> dataMap) {
		String mathString = new String(field_func);
		int preIndex=0;
		for (int i=0;i<field_func.length();i++) {
			String a = field_func.substring(i, i+1);
			if ("{".equals(a)) preIndex = i;
			if ("}".equals(a)) {
				String field = field_func.substring(preIndex+1,i);
				mathString = mathString.replaceAll(field, (dataMap.containsKey(field)&&dataMap.get(field)!=null)?dataMap.get(field).toString():"0");
			}
		}
		mathString = mathString.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("%2B", "+");
		return StringUtil.mathByString(mathString);
	}

	@Override
	public List<TableHeadVO> getHomeworkTableHead(TableHeadVO vo) {	
		return dao.queryForList("homeworkTableMap.getHomeworkTableList", vo);
	}
	
	private void addHomeworkSumToRedis(Integer group_id,Integer team_id,String team_type){
		Date now = new Date();
		String day = DateUtil.getNow("yyyyMMdd");//天
		String week = DateUtil.getFirstDayOfWeek(now);//星期
		String month = DateUtil.getFirstDayOfMonth(now);//月
		String term = DateUtil.getYearAndTerm(now);//学期
		String year = DateUtil.getFirstDayOfYear(day);//年
		//每个班级的统计
		String homrworkKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), group_id, team_id,team_type,"");
		jedisDao.hincrBy(homrworkKey+":DAY"+day, "submit_count", 1);
		jedisDao.hincrBy(homrworkKey+":WEEK"+week, "submit_count", 1);
		jedisDao.hincrBy(homrworkKey+":MONTH"+month, "submit_count", 1);
		jedisDao.hincrBy(homrworkKey+":TERM"+term, "submit_count", 1);
		jedisDao.hincrBy(homrworkKey+":YEAR"+year, "submit_count", 1);
		//每个科目的统计
		String homrworkCourseKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), group_id, team_id,team_type,ActionUtil.getParameter("course"));
		jedisDao.hincrBy(homrworkCourseKey+":DAY"+day, "submit_count", 1);
		jedisDao.hincrBy(homrworkCourseKey+":WEEK"+week, "submit_count", 1);
		jedisDao.hincrBy(homrworkCourseKey+":MONTH"+month, "submit_count", 1);
		jedisDao.hincrBy(homrworkCourseKey+":TERM"+term, "submit_count", 1);
		jedisDao.hincrBy(homrworkCourseKey+":YEAR"+year, "submit_count", 1);
	}
	
	private void addHomeworkStuToRedis(HomeworkReceiveVO vo) throws ParseException{
		String homeworkKey = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), vo.getHomework_id(),0);
		jedisDao.hincrBy(homeworkKey, "count_done", 1);
		String homeworkStuKey = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), vo.getHomework_id(),vo.getStudent_id());
		jedisDao.hincrBy(homeworkStuKey, "is_submit", 1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("homework_id", vo.getHomework_id());
		map.put("school_id", vo.getSchool_id());
		map.put("student_id", vo.getStudent_id());
		List<HomeworkGroupVO> homework = dao.queryForList("homeworkGroupMap.getHomeworkList", map);
		for (HomeworkGroupVO homeworkGroupVO : homework) {
			Date groupTime = homeworkGroupVO.getCreate_date();
			String homeworkDay = DateUtil.getNow("yyyyMMdd",groupTime);//天
			String homeworkWeek = DateUtil.getFirstDayOfWeek(groupTime);//星期
			String homeworkMonth = DateUtil.getFirstDayOfMonth(groupTime);//月
			String homeworkTerm = DateUtil.getYearAndTerm(groupTime);//学期
			String homeworkYear = DateUtil.getFirstDayOfYear(homeworkDay);//年
			//班级统计的键
			String homrworkKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), homeworkGroupVO.getGroup_id(), homeworkGroupVO.getTeam_id(),homeworkGroupVO.getTeam_type(),"");
			String homeworkStudentKey = RedisKeyUtil.getHomeworkStudentCountKey(vo.getSchool_id(),homeworkGroupVO.getGroup_id(),homeworkGroupVO.getTeam_id(),vo.getStudent_id(),homeworkGroupVO.getTeam_type(),"");
			//班级每个科目的 键
			String homrworkCourseKey = RedisKeyUtil.getHomeworkCountKey(ActionUtil.getSchoolID(), homeworkGroupVO.getGroup_id(), homeworkGroupVO.getTeam_id(),homeworkGroupVO.getTeam_type(),homeworkGroupVO.getCourse());
			String homeworkStudentCourseKey = RedisKeyUtil.getHomeworkStudentCountKey(vo.getSchool_id(),homeworkGroupVO.getGroup_id(),homeworkGroupVO.getTeam_id(),vo.getStudent_id(),homeworkGroupVO.getTeam_type(),homeworkGroupVO.getCourse());
			Date homeworkTime = DateUtil.dateTimeFormat(homeworkGroupVO.getEnd_date()+":00");
			System.out.println(ActionUtil.getSysTime());
			//准时提交的情况
			if (DateUtil.compareDate(homeworkTime,ActionUtil.getSysTime())<1){
				logger.error("homework_id:"+vo.getHomework_id()+"-school_id:"+vo.getSchool_id()+"-student_id:"+vo.getStudent_id()
						+"StudentCount:(-DAY:"+homeworkStudentKey+":DAY"+homeworkDay+"-people_submit:1"
						+"-WEEK:"+homeworkStudentKey+":WEEK"+homeworkDay+"-people_submit:1"
						+"-MONTH:"+homeworkStudentKey+":MONTH"+homeworkDay+"-people_submit:1"
						+"-TERM:"+homeworkStudentKey+":TERM"+homeworkDay+"-people_submit:1"
						+"-YEAR:"+homeworkStudentKey+":YEAR"+homeworkDay+"-people_submit:1)"
						+"ClassCount:(-DAY:"+homrworkKey+":DAY"+homeworkDay+"-people_submit:1"
						+"-WEEK:"+homrworkKey+":WEEK"+homeworkDay+"-people_submit:1"
						+"-MONTH:"+homrworkKey+":MONTH"+homeworkDay+"-people_submit:1"
						+"-TERM:"+homrworkKey+":TERM"+homeworkDay+"-people_submit:1"
						+"-YEAR:"+homrworkKey+":YEAR"+homeworkDay+"-people_submit:1)"
						+"StudentCourseCount:(-DAY:"+homeworkStudentCourseKey+":DAY"+homeworkDay+"-people_submit:1"
						+"-WEEK:"+homeworkStudentCourseKey+":WEEK"+homeworkDay+"-people_submit:1"
						+"-MONTH:"+homeworkStudentCourseKey+":MONTH"+homeworkDay+"-people_submit:1"
						+"-TERM:"+homeworkStudentCourseKey+":TERM"+homeworkDay+"-people_submit:1"
						+"-YEAR:"+homeworkStudentCourseKey+":YEAR"+homeworkDay+"-people_submit:1)"
						+"ClassCourseCount:(-DAY:"+homrworkCourseKey+":DAY"+homeworkDay+"-people_submit:1"
						+"-WEEK:"+homrworkCourseKey+":WEEK"+homeworkDay+"-people_submit:1"
						+"-MONTH:"+homrworkCourseKey+":MONTH"+homeworkDay+"-people_submit:1"
						+"-TERM:"+homrworkCourseKey+":TERM"+homeworkDay+"-people_submit:1"
						+"-YEAR:"+homrworkCourseKey+":YEAR"+homeworkDay+"-people_submit:1)");
				//班级统计准时提交数
				jedisDao.hincrBy(homeworkStudentKey+":DAY"+homeworkDay, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":WEEK"+homeworkWeek, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":MONTH"+homeworkMonth, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":TERM"+homeworkTerm, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":YEAR"+homeworkYear, "people_submit", 1);
				jedisDao.hincrBy(homrworkKey+":DAY"+homeworkDay, "people_count", 1);
				jedisDao.hincrBy(homrworkKey+":WEEK"+homeworkWeek, "people_count", 1);
				jedisDao.hincrBy(homrworkKey+":MONTH"+homeworkMonth, "people_count", 1);
				jedisDao.hincrBy(homrworkKey+":TERM"+homeworkTerm, "people_count", 1);
				jedisDao.hincrBy(homrworkKey+":YEAR"+homeworkYear, "people_count", 1);
				//每个科目统计准时提交数
				jedisDao.hincrBy(homeworkStudentCourseKey+":DAY"+homeworkDay, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":WEEK"+homeworkWeek, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":MONTH"+homeworkMonth, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":TERM"+homeworkTerm, "people_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":YEAR"+homeworkYear, "people_submit", 1);
				jedisDao.hincrBy(homrworkCourseKey+":DAY"+homeworkDay, "people_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":WEEK"+homeworkWeek, "people_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":MONTH"+homeworkMonth, "people_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":TERM"+homeworkTerm, "people_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":YEAR"+homeworkYear, "people_count", 1);
			} else {
				logger.error("homework_id:"+vo.getHomework_id()+"-school_id:"+vo.getSchool_id()+"-student_id:"+vo.getStudent_id()
						+"StudentCount:(-DAY:"+homeworkStudentKey+":DAY"+homeworkDay+"-late_submit:1"
						+"-WEEK:"+homeworkStudentKey+":WEEK"+homeworkDay+"-late_submit:1"
						+"-MONTH:"+homeworkStudentKey+":MONTH"+homeworkDay+"-late_submit:1"
						+"-TERM:"+homeworkStudentKey+":TERM"+homeworkDay+"-late_submit:1"
						+"-YEAR:"+homeworkStudentKey+":YEAR"+homeworkDay+"-late_submit:1)"
						+"ClassCount:(-DAY:"+homrworkKey+":DAY"+homeworkDay+"-late_submit:1"
						+"-WEEK:"+homrworkKey+":WEEK"+homeworkDay+"-late_submit:1"
						+"-MONTH:"+homrworkKey+":MONTH"+homeworkDay+"-late_submit:1"
						+"-TERM:"+homrworkKey+":TERM"+homeworkDay+"-late_submit:1"
						+"-YEAR:"+homrworkKey+":YEAR"+homeworkDay+"-late_submit:1)"
						+"StudentCourseCount:(-DAY:"+homeworkStudentCourseKey+":DAY"+homeworkDay+"-late_submit:1"
						+"-WEEK:"+homeworkStudentCourseKey+":WEEK"+homeworkDay+"-late_submit:1"
						+"-MONTH:"+homeworkStudentCourseKey+":MONTH"+homeworkDay+"-late_submit:1"
						+"-TERM:"+homeworkStudentCourseKey+":TERM"+homeworkDay+"-late_submit:1"
						+"-YEAR:"+homeworkStudentCourseKey+":YEAR"+homeworkDay+"-late_submit:1)"
						+"ClassCourseCount:(-DAY:"+homrworkCourseKey+":DAY"+homeworkDay+"-late_submit:1"
						+"-WEEK:"+homrworkCourseKey+":WEEK"+homeworkDay+"-late_submit:1"
						+"-MONTH:"+homrworkCourseKey+":MONTH"+homeworkDay+"-late_submit:1"
						+"-TERM:"+homrworkCourseKey+":TERM"+homeworkDay+"-late_submit:1"
						+"-YEAR:"+homrworkCourseKey+":YEAR"+homeworkDay+"-late_submit:1)");
				//班级统计迟交数
				jedisDao.hincrBy(homeworkStudentKey+":DAY"+homeworkDay, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":WEEK"+homeworkWeek, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":MONTH"+homeworkMonth, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":TERM"+homeworkTerm, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentKey+":YEAR"+homeworkYear, "late_submit", 1);
				jedisDao.hincrBy(homrworkKey+":DAY"+homeworkDay, "late_count", 1);
				jedisDao.hincrBy(homrworkKey+":WEEK"+homeworkWeek, "late_count", 1);
				jedisDao.hincrBy(homrworkKey+":MONTH"+homeworkMonth, "late_count", 1);
				jedisDao.hincrBy(homrworkKey+":TERM"+homeworkTerm, "late_count", 1);
				jedisDao.hincrBy(homrworkKey+":YEAR"+homeworkYear, "late_count", 1);
				//每个科目统计迟交数
				jedisDao.hincrBy(homeworkStudentCourseKey+":DAY"+homeworkDay, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":WEEK"+homeworkWeek, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":MONTH"+homeworkMonth, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":TERM"+homeworkTerm, "late_submit", 1);
				jedisDao.hincrBy(homeworkStudentCourseKey+":YEAR"+homeworkYear, "late_submit", 1);
				jedisDao.hincrBy(homrworkCourseKey+":DAY"+homeworkDay, "late_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":WEEK"+homeworkWeek, "late_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":MONTH"+homeworkMonth, "late_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":TERM"+homeworkTerm, "late_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":YEAR"+homeworkYear, "late_count", 1);
			}
		}
	}
	/**
	 * 获取作业子项列表
	 * @param paramMap
	 * @return
	 */
	public List<?> getItemList(Map<String, String> paramMap) {
		//教师端
		if(DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())){
			List<HomeworkItemVO> list = dao.queryForList(
					"homeworkItemMap.getHomeworkItemList", paramMap);
			setFileList(list);//设置附件
			for(HomeworkItemVO hivo:list){
				HomeworkVO hvo=dao.queryObject("homeworkMap.getHomeworkByhId", hivo.getHomework_id());
				hivo.setTitle(hvo.getTitle());
				hivo.setCourse(hvo.getCourse());
				hivo.setSender_name(redisService.getUserName(hvo.getSchool_id(), DictConstants.USERTYPE_TEACHER, hvo.getSender_id(), 0));
				hivo.setEnd_date(hvo.getEnd_date());
				hivo.setSend_time(hvo.getCreate_date().getTime());
			}
			return list;
		}else{
			List<HomeworkReceiveVO> receiveList = dao.queryForList(
					"homeworkItemMap.getHomeworkItemListByMap", paramMap);
			//setRecFileList(receiveList);//设置附件
			for(HomeworkReceiveVO vo:receiveList){
				HomeworkVO hvo=dao.queryObject("homeworkMap.getHomeworkByhId", vo.getHomework_id());
				if (hvo==null) continue;
				vo.setTitle(hvo.getTitle());
				vo.setCourse(hvo.getCourse());
				vo.setSender_name(redisService.getUserName(hvo.getSchool_id(), DictConstants.USERTYPE_TEACHER, hvo.getSender_id(), 0));
				vo.setEnd_date(hvo.getEnd_date());
				HomeworkReceiveVO hrvo=dao.queryObject("homeworkReceiveMap.getHomeworkReceiveListById", paramMap);
				vo.setStudent_name(redisService.getUserName(vo.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, ActionUtil.getStudentID()));
				vo.setHead_url(redisService.getUserHeadUrl(vo.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, ActionUtil.getStudentID()));
				vo.setSend_time(vo.getCreate_date().getTime());
				vo.setIs_submit((hrvo == null)?0:getHomeworkIsSubmit(paramMap));
				vo.setReceive_id((hrvo == null)?0:hrvo.getReceive_id());
			}
			return receiveList;
		}
	}
}
