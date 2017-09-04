package com.ninesky.classtao.score.service.impl;

import java.util.*;

import com.ninesky.classtao.score.vo.*;
import com.ninesky.classtao.studentLeave.service.StudentLeaveService;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveVO;
import com.ninesky.framework.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.score.service.ScoreService;
import com.ninesky.classtao.score.service.ScoreTableService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.ListUtil;
import com.ninesky.common.util.RedisKeyUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;

@Service("scoreServiceImpl")
public  class ScoreServiceImpl implements ScoreService{

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private ScoreTableService tableService;
	
	@Autowired
	private GetuiService getuiService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	protected JedisDAO jedisDAO;

	@Autowired
	private UserService userService;
	
	@Autowired
	private WechatService wechatService;

	@Autowired
	private StudentLeaveService studentLeaveService;
	
	//获取预先设定的扣分原因
		public List<ScoreReasonVO> getScoreReason(ScoreReasonVO vo) {
			if (IntegerUtil.isEmpty(vo.getSchool_id()))
				vo.setSchool_id(ActionUtil.getSchoolID());
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) //考勤的话，寝室教室一样
				vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type()))//兴趣班应该是和教室一样
				vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			vo.setIs_active(1);
			return dao.queryForList("scoreReasonMap.getScoreReasonList",vo);
		}
		
	//保存考勤
	public ScoreVO saveScore(ScoreVO vo) {
		if(StringUtil.isEmpty(vo.getUser_name())) throw new BusinessException("请填写user_name!");
		if(IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())) throw new BusinessException("group_id,team_id不能为空！");
		if(vo.getScore_id()==0){
			return addScore(vo);//新增
		}else{
			return updateScore(vo);//修改扣分信息
		}
	}

	//新增扣分
	public ScoreVO addScore(ScoreVO vo) {
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);//被考核的学生集合
		if (((DictConstants.MODULE_CODE_ATTEND.equals(vo.getModule_code()))  ||
				(DictConstants.MODULE_CODE_BEDROOM.equals(vo.getModule_code()) &&
						DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type()))) && (checkHaveAttend(vo)))
			throw new BusinessException(MsgService.getMsg("un_team_attend"));
		//设置年级ID，否则Redis会出问题
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && vo.getGroup_id() == null)
			vo.setGroup_id(classService.getClassByID(vo.getTeam_id()).getGrade_id());
		vo.setCount(itemlist.size());
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setScore_date(DateUtil.getNow("yyyy-MM-dd"));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		int scoreID=dao.insertObjectReturnID("scoreMap.insertScore", vo);//新增考核到主表
		if(itemlist.size()==0) {
			tableService.addScoreSumToRedis(itemlist, vo);
			//tableService.addNewRecord(itemlist,vo);
			vo.setScore_id(scoreID);
			return vo;
		}
		for(ScoreListVO avo:itemlist){
			avo.setScore_id(scoreID);
			avo.setSchool_id(ActionUtil.getSchoolID());
			avo.setTeam_type(vo.getTeam_type());
			avo.setTeam_id(vo.getTeam_id());
			avo.setScore_date(vo.getScore_date());
			avo.setCreate_by(ActionUtil.getUserID());
			avo.setCreate_date(ActionUtil.getSysTime());
			avo.setTeam_code(vo.getTeam_code());
			avo.setTeam_name(vo.getTeam_name());
			avo.setScore_type(vo.getScore_type());
			avo.setAttend_item(vo.getAttend_item());
			avo.setModule_code(vo.getModule_code());
            avo.setGroup_id(vo.getGroup_id());
            if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一次
		}
		dao.insertObject("scoreListMap.insertScoreList", itemlist);//新增考核到子表
		//考勤家长推送消息
//暂时屏蔽给家长发短信功能
//		for (ScoreListVO sav:itemlist){
//			 ParentVO parent = new ParentVO();
//			 parent.setSchool_id(sav.getSchool_id());
//			 parent.setStudent_id(sav.getStudent_id());
//			 List<ParentVO> list=userService.getParentListBy(parent);
//			 if(DictConstants.SCORE_TYPE_ATTEND.equals(sav.getScore_type())){//判断类型是否为考勤
//		        for(ParentVO pvo:list){//遍历考勤不合格的学生
//					messageService.sendMessage(pvo.getPhone(), MsgService.getMsg("MESSAGE_PARENT_ATTEND",pvo.getStudent_name()));
//					}
//			   }
//		}	
		updateCountInfoOfAdd(vo, scoreID);//新增后，修改countinfo
		tableService.addScoreSumToRedis(itemlist, vo);
		//tableService.addNewRecord(itemlist,vo);
		return vo;
	}
	
	private boolean checkHaveAttend(ScoreVO vo) {
		return dao.queryObject("scoreMap.getAttendScore", vo) !=null;
	}

	//新增后，修改countinfo
	public void updateCountInfoOfAdd(ScoreVO vo, int scoreID) {
		List<CountInfoVO> clist=dao.queryForList("scoreListMap.getCountInfo", scoreID);
		scoreSum(clist, vo);//纪律，卫生的json中的count显示的是分数
		String count_info=BeanUtil.ListTojson(clist).toString();//统计信息
		vo.setScore_id(scoreID);
		vo.setCount_info(count_info);
		dao.updateObject("scoreMap.updateCountInfo", vo);//更新主表中的统计信息
	}
	
	//修改考勤
	public ScoreVO updateScore(ScoreVO vo) {
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);//被考勤的学生集合
		if (ListUtil.isEmpty(itemlist)) return vo;
		for(ScoreListVO slvo:itemlist){
			if(IntegerUtil.isEmpty(slvo.getStudent_id()) || StringUtil.isEmpty(slvo.getStudent_code()) || 
					StringUtil.isEmpty(slvo.getStudent_name())){
				throw new BusinessException("请填写student_id.student_code,student_name!");
			}
		}
		List<ScoreListVO> list=new ArrayList<ScoreListVO>();//存放需添加的记录
		String modulcode=vo.getModule_code();
		vo.setScore_date(DateUtil.getNow("yyyy-MM-dd"));
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && vo.getGroup_id() == null)
			vo.setGroup_id(classService.getClassByID(vo.getTeam_id()).getGrade_id());
		for (ScoreListVO avo:itemlist) {
			if (avo.getList_id()==0) {//添加
				addList(vo,list, avo);
			} else if (StringUtil.isNotEmpty(avo.getScore_code())) {//修改
				updateList(vo, avo);
			} else {
				deleteList(avo);//删除
			}
		}
		if(ListUtil.isNotEmpty(list))
		dao.insertObject("scoreListMap.insertScoreList", list);
		vo.setModule_code(modulcode);
		vo = updateCountInfo(vo);//修改主表统计信息
		tableService.updateScoreToRedis(itemlist, vo);//修改扣分后，修改redis中数据
		return vo;
	}

	//修改扣分（删除某条扣分）
	private void deleteList(ScoreListVO avo) {
		ScoreListVO old = dao.queryObject("scoreListMap.getScoreItemByListID", avo.getList_id());
        avo.setScore_old(old.getScore()*old.getCount());
		avo.setScore_code_old(old.getScore_code());
		dao.deleteObject("scoreListMap.deleteScoreList", avo.getList_id());
	}

	//修改扣分（修改某条扣分）
	private void updateList(ScoreVO vo, ScoreListVO avo) {
		avo.setAttend_item(vo.getAttend_item());
		avo.setUpdate_by(ActionUtil.getUserID());
		avo.setUpdate_date(ActionUtil.getSysTime());
		ScoreListVO old = dao.queryObject("scoreListMap.getScoreItemByListID", avo.getList_id());
        avo.setScore_old(old.getScore()*old.getCount());
		avo.setScore_code_old(old.getScore_code());
        if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
		dao.updateObject("scoreListMap.updateScoreList", avo);
	}

	//修改扣分（添加某条扣分记录）
	private void addList(ScoreVO vo,List<ScoreListVO> list, ScoreListVO avo) {
		avo.setScore_id(vo.getScore_id());
		avo.setSchool_id(ActionUtil.getSchoolID());
		avo.setTeam_type(vo.getTeam_type());
		avo.setTeam_id(vo.getTeam_id());
		avo.setScore_date(DateUtil.getNow("yyyy-MM-dd"));
		avo.setCreate_by(ActionUtil.getUserID());
		avo.setCreate_date(ActionUtil.getSysTime());
		avo.setTeam_code(vo.getTeam_code());
		avo.setTeam_name(vo.getTeam_name());
		avo.setScore_type(vo.getScore_type());
		avo.setAttend_item(vo.getAttend_item());
		avo.setModule_code(vo.getModule_code());
        avo.setGroup_id(vo.getGroup_id());
        if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
		list.add(avo);
	}

	//修改主表统计信息
	public ScoreVO updateCountInfo(ScoreVO vo) {
		ScoreVO scorevo;
		List<CountInfoVO> clist=dao.queryForList("scoreListMap.getCountInfo", vo.getScore_id());
		scoreSum(clist, vo);//纪律，卫生的json中的count显示的是分数
		String count_info=BeanUtil.ListTojson(clist).toString();//统计信息
		vo.setCount_info(count_info);
		int count=dao.queryObject("scoreListMap.getCount", vo.getScore_id());//异常情况人数
		vo.setCount(count);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("scoreMap.updateScore", vo);
		scorevo=dao.queryObject("scoreMap.getScoreList", vo);
		if(IntegerUtil.isNotEmpty(scorevo.getTeam_id()) && DictConstants.TEAM_TYPE_CLASS.equals(scorevo.getTeam_type()))
		scorevo.setTeam_name(classService.getClassByID(scorevo.getTeam_id()).getClass_name());
		return scorevo;
	}

	//纪律，卫生的json中的count显示的是分数
	public void scoreSum(List<CountInfoVO> list, ScoreVO scoreVO) {
		if (!DictConstants.SCORE_TYPE_ATTEND.equals(scoreVO.getScore_type())) {
			for (CountInfoVO cvo:list){//纪律和卫生的count显示的是分数,在校表现
				ScoreReasonVO srvo=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(), cvo.getScore_code(),scoreVO.getScore_type(),scoreVO.getTeam_type());
				cvo.setCount(cvo.getCount()*srvo.getScore());
			}
		}
	}

	//获取考勤
	public List<ScoreVO> getScoreList(ScoreVO vo) {
		if (StringUtil.isEmpty(vo.getTeam_type())) throw new BusinessException("请填写team_type");
		vo.setSchool_id(ActionUtil.getSchoolID());
		if ("0".equals(vo.getScore_date())) vo.setScore_date("");//应前端需求
		List<ScoreVO> list=new ArrayList<ScoreVO>();
		if (StringUtil.isEmpty(vo.getModule_code()) && DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && StringUtil.isEmpty(vo.getScore_type())) {
			list = dao.queryForList("scoreMap.getScoreNoAttend", vo);//教室管理只显示纪律和卫生，排除考勤和在校表现
		} else
			list = dao.queryForList("scoreMap.getScore", vo);
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {//班级名称才转换，寝室名称不转换
		for (ScoreVO svo:list) {
			svo.setTeam_name(classService.getClassByID(svo.getTeam_id()).getClass_name());
			svo.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, svo.getCreate_by(), null));
		}
		} else {//寝室管理
			for (ScoreVO so:list) {
				so.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, so.getCreate_by(), null));
			}
		}
		if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(vo.getModule_code())) {//个人评分
			for (ScoreVO svo:list){
				svo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
				svo.setStudent_code(userService.getStudentById(svo.getStudent_id()).getStudent_code());
			}
		}
		return list;
	}

	//获取扣分明细记录
	public List<ScoreListVO> getScoreListList(ScoreListVO vo) {
//		Date start_time=DateUtil.formatStringToDate(vo.getStart_time(), "yyyy-M-d");
//		Date end_time=DateUtil.formatStringToDate(vo.getEnd_time(),"yyyy-M-d");
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ScoreListVO> list=dao.queryForList("scoreListMap.getScoreListList", vo);
		for (ScoreListVO slvo:list) {
			ScoreVO scorevo=new ScoreVO();
			scorevo.setScore_id(slvo.getScore_id());
			ScoreVO svo=dao.queryObject("scoreMap.getScoreList", scorevo);
			slvo.setUser_name(redisService.getUserName(svo.getSchool_id(), DictConstants.USERTYPE_TEACHER, svo.getCreate_by(), null));
            //slvo.setScore(slvo.getScore()*slvo.getCount());//会有多个扣分
		}
		if (DictConstants.USERTYPE_PARENT.equals(ActionUtil.getUserType())) {
		for (ScoreListVO scoreListVO:list) {
			if (scoreListVO.getIs_read()==0)//获取后设成已读
				dao.updateObject("scoreListMap.setRead", scoreListVO.getList_id());
		}
	}
		return list;
	}

	//获取个人未读数
	public Integer getUnreadCount(ScoreListVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		int count=dao.queryObject("scoreListMap.getUnreadCount", vo);
		return count;
	}

	//获取班级汇总明细列表
	public List<ScoreListVO> getScoreListOfTeam(ScoreListVO vo) {
		List<ScoreListVO> slist=new ArrayList<ScoreListVO>();
        vo.setSchool_id(ActionUtil.getSchoolID());
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {//教室
			slist=dao.queryForList("scoreListMap.getStudentScoreList", vo);
			for (ScoreListVO slvo:slist) {
				if (IntegerUtil.isEmpty(slvo.getTeam_id())) continue;
				slvo.setTeam_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,null , vo.getTeam_id()));
			}
		} else if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())) {//寝室
			slist = dao.queryForList("scoreListMap.getStudentScoreListOfBed", vo);
		} else if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type())) {//兴趣班
			slist = dao.queryForList("scoreListMap.getStudentScoreListOfInterest", vo);
			for (ScoreListVO slvo:slist) {
				slvo.setTeam_name(redisService.getTeamName(DictConstants.TEAM_TYPE_INTEREST, 0, vo.getTeam_id()));
				slvo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,
						0, slvo.getStudent_id()));
				StudentVO svo=userService.getStudentById( slvo.getStudent_id());
				slvo.setStudent_code(svo.getStudent_code());
			}
		}
		if (!DictConstants.SCORE_TYPE_PERFORMANCE.equals(vo.getScore_type())) {//如果打分类型是在校表现，则不判断该班级是否已经扣过分
			ScoreVO score=dao.queryObject("scoreMap.getScoreInfo", vo);
			if (score!=null) {//该班级已扣过分
				String user_name=redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
					score.getCreate_by(), 0);
				for (ScoreListVO svo:slist) {
					svo.setUser_name(user_name);//扣分老师姓名
					svo.setScore_date(score.getScore_date());
					svo.setSender_id(score.getCreate_by());
					svo.setScore_id(score.getScore_id());
				}
			}
		}
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){
			for (ScoreListVO slvo:slist){//与请假进行关联
				StudentLeaveVO leave=new StudentLeaveVO();
				leave.setStudent_id(slvo.getStudent_id());
				if (StringUtil.isEmpty(slvo.getScore_date())) leave.setStart_date(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));
				else leave.setStart_date(slvo.getScore_date());
				List<StudentLeaveVO> leaveList=studentLeaveService.getPassLeaveByStuID(leave);
				if (ListUtil.isNotEmpty(leaveList))
					slvo.setIs_leave(true);//请假状态
			}
		}
		return slist;
	}

	
	/**
	 * 保存动态消息到Redis数据库
	 * @param vo
	 * @author chenth
	 */
	public void saveDynamic(ScoreVO vo) {
		//存放一组接收者的信息
		ReceiveVO receive = new ReceiveVO(ActionUtil.getSchoolID(),vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id());
		switch (vo.getModule_code()) {
			case DictConstants.MODULE_CODE_ATTEND:
				saveDynamicOfAttend(vo,receive);
				break;
			case DictConstants.MODULE_CODE_BEDROOM:
				saveDynamicOfBedroom(vo,receive);
				break;
			case DictConstants.MODULE_CODE_CLASSROOM:
				saveDynamicOfClassroom(vo,receive);
				break;
			case DictConstants.MODULE_CODE_PERFORMANCE:
				saveDynamicOfPerformance(vo,receive);
				break;
			case DictConstants.MODULE_CODE_CLASS_SCORE:
				saveDynamicOfClassScore(vo,receive);
				break;
			case DictConstants.MODULE_CODE_PERSON_SCORE:
				saveDynamicOfPersonScore(vo,receive);
				break;
			default: break;
		}
	}
	
	@Override
	public void pushMessage(ScoreVO vo) {
		//存放一组接收者的信息
		ReceiveVO receive = new ReceiveVO(ActionUtil.getSchoolID(),vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id());
		switch (vo.getModule_code()) {
			case DictConstants.MODULE_CODE_ATTEND:
				pushMessageOfAttend(vo,receive);
				break;
			case DictConstants.MODULE_CODE_BEDROOM:
				pushMessageOfBedroom(vo,receive);
				break;
			case DictConstants.MODULE_CODE_CLASSROOM:
				pushMessageOfClassroom(vo,receive);
				break;
			case DictConstants.MODULE_CODE_PERFORMANCE:
				pushMessageOfPerformance(vo,receive);
				break;
			case DictConstants.MODULE_CODE_PERSON_SCORE:
				pushMessageOfPersonScore(vo,receive);
		default: break;
		}
	}

	private void saveDynamicOfPersonScore(final ScoreVO vo,final ReceiveVO receive) {
		//得到纪律等
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		//班級名稱
		String class_name = vo.getTeam_name();
		if (StringUtil.isEmpty(class_name))
			class_name = redisService.getTeamName(vo.getTeam_type(), vo.getGroup_id(), vo.getTeam_id());
		//被考核的学生集合
		List<ScoreListVO> Scorelist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		//打分时，去除重复的学生ID情况
		HashMap<Integer ,String> map = new HashMap<Integer,String>();
		for (ScoreListVO sco : Scorelist) {
			map.put(sco.getStudent_id(), vo.getItem_list());
		}
		String title = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_TITLE",vo.getUser_name(),class_name);
		String content_discipline = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_CONTENT_DISCIPLINE",dictVO.getDict_value());
		String module_code = DictConstants.MODULE_CODE_PERSON_SCORE;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		jedisDAO.hset(key, "info_title", title);
		jedisDAO.hset(key, "info_content", content_discipline);
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key, "score_type",vo.getScore_type());
		//增加动态记录到教师端
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, vo.getGroup_id(), vo.getTeam_id()), ActionUtil.getSysTime().getTime(),key);
		//添加动态到个人
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),ActionUtil.getSysTime().getTime(),key);
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		for (ScoreListVO svo:itemlist) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, null, svo.getStudent_id());
			String stuKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "STUDENT_ID:" + svo.getStudent_id());
			jedisDAO.hset(stuKey, "module_code", module_code);
			jedisDAO.hset(stuKey, "module_pkid", module_pkid.toString());
			jedisDAO.hset(stuKey, "suffix", "");
			String stuContent = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_CONTENT", vo.getUser_name());
			String stuTitle = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_TITLE", svo.getStudent_name(), dictVO.getDict_value());
			jedisDAO.hset(stuKey, "info_title", stuTitle);
			jedisDAO.hset(stuKey, "info_content", stuContent);
			jedisDAO.hset(stuKey, "student_id", svo.getStudent_id().toString());
			jedisDAO.hset(stuKey, "info_date", ActionUtil.getSysTime().getTime() + "");
			jedisDAO.hset(stuKey, "link_type", DictConstants.LINK_TYPE_DETAIL);
			jedisDAO.hset(stuKey, "info_url", "detail.html");
			jedisDAO.hset(stuKey, "user_type", DictConstants.USERTYPE_STUDENT);
			jedisDAO.hset(stuKey, "score_type",vo.getScore_type());
			jedisDAO.zadd(zsetKey, ActionUtil.getSysTime().getTime(), stuKey);
		}
	}

	private void saveDynamicOfClassScore(final ScoreVO vo,final ReceiveVO receive) {
		//得到纪律等
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		//班級名稱
		String class_name = vo.getTeam_name();
		if (StringUtil.isEmpty(class_name))
			class_name = redisService.getTeamName(vo.getTeam_type(), vo.getGroup_id(), vo.getTeam_id());
		String title = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_TITLE",vo.getUser_name(),class_name);
		String content_discipline = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_CONTENT_DISCIPLINE",dictVO.getDict_value());
		String module_code = DictConstants.MODULE_CODE_CLASS_SCORE;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		jedisDAO.hset(key, "info_title", title);
		jedisDAO.hset(key, "info_content", content_discipline);
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key, "score_type",vo.getScore_type());
		//增加动态记录到教师端
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, vo.getGroup_id(), vo.getTeam_id()), ActionUtil.getSysTime().getTime(),key);
		//添加动态到个人
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),ActionUtil.getSysTime().getTime(),key);
	}

	private void saveDynamicOfPerformance(final ScoreVO vo,final ReceiveVO receive) {
		//班級名称
		String class_name = vo.getTeam_name();
		if (StringUtil.isEmpty(class_name)) 
			class_name = redisService.getTeamName(vo.getTeam_type(), vo.getGroup_id(), vo.getTeam_id());
		//被考核的学生集合
		List<ScoreListVO> Scorelist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		//打分时，去除重复的学生ID情况
		HashMap<Integer ,String> map = new HashMap<Integer,String>();
		for (ScoreListVO sco : Scorelist) {
			map.put(sco.getStudent_id(), vo.getItem_list());
		}
		String title = MsgService.getMsg("DYNAMIC_PERFORMANCE_TEACHER",vo.getUser_name(),class_name);
		String content_discipline = MsgService.getMsg("DYNAMIC_PERFORMANCE_TEACHER_CONTENT_DISCIPLINE");
		String module_code = DictConstants.MODULE_CODE_PERFORMANCE;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		jedisDAO.hset(key, "info_title", title);
		jedisDAO.hset(key, "info_content", content_discipline);
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key,"score_type",vo.getScore_type());
		//增加动态记录到教师端
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, vo.getGroup_id(), vo.getTeam_id()), ActionUtil.getSysTime().getTime(),key);
		//添加动态到个人
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),ActionUtil.getSysTime().getTime(),key);
		//被考核的学生集合
		List<ScoreListVO> itemList=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		for (ScoreListVO svo:itemList) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,svo.getStudent_id());
			String stuKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "STUDENT_ID:"+svo.getStudent_id());
			if (StringUtil.isEmpty(svo.getScore_code())) {//编辑中的取消某一个学生考勤
				jedisDAO.del(stuKey);
				jedisDAO.zrem(zsetKey, stuKey);
			} else {
				jedisDAO.hset(stuKey, "module_code", module_code);
				jedisDAO.hset(stuKey, "module_pkid", module_pkid.toString());
				jedisDAO.hset(stuKey, "suffix", "");
				String stuContent = MsgService.getMsg("DYNAMIC_PARENT_STUDENT_CONTENT", vo.getUser_name());
				String stuTitle = MsgService.getMsg("DYNAMIC_PARENT_STUDENT_TITLE", svo.getStudent_name());
				jedisDAO.hset(stuKey, "info_title", stuTitle);
				jedisDAO.hset(stuKey, "info_content", stuContent);
				jedisDAO.hset(stuKey, "student_id", svo.getStudent_id().toString());
				jedisDAO.hset(stuKey, "info_date", ActionUtil.getSysTime().getTime() + "");
				jedisDAO.hset(stuKey, "link_type", DictConstants.LINK_TYPE_DETAIL);
				jedisDAO.hset(stuKey, "info_url", "detail.html");
				jedisDAO.hset(stuKey, "user_type", DictConstants.USERTYPE_STUDENT);
				jedisDAO.hset(stuKey,"score_type",vo.getScore_type());
				jedisDAO.zadd(zsetKey, ActionUtil.getSysTime().getTime(), stuKey);
			}
		}
	}
	
	private void saveDynamicOfClassroom(final ScoreVO vo,final ReceiveVO receive) {
		//得到纪律等
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		//班級名稱
		String class_name = vo.getTeam_name();
		if (StringUtil.isEmpty(class_name)) 
			class_name = redisService.getTeamName(vo.getTeam_type(), vo.getGroup_id(), vo.getTeam_id());
		//被考核的学生集合
		List<ScoreListVO> Scorelist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		//打分时，去除重复的学生ID情况
		HashMap<Integer ,String> map = new HashMap<Integer,String>();
		for (ScoreListVO sco : Scorelist) {
			map.put(sco.getStudent_id(), vo.getItem_list());
		}
		String title = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_TITLE",vo.getUser_name(),class_name);
		String content_discipline = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_CONTENT_DISCIPLINE",dictVO.getDict_value());
		String content_hygiene = MsgService.getMsg("DYNAMIC_CLASSROOM_TEACHER_CONTENT_HYGIENE");
		String dict_code=dictVO.getDict_code();
		String module_code = DictConstants.MODULE_CODE_CLASSROOM;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		//对教室管理中 纪律、卫生进行判断
		if (DictConstants.SCORE_TYPE_DISCIPLINE.equals(dict_code)) {
			jedisDAO.hset(key, "info_title", title);
			jedisDAO.hset(key, "info_content", content_discipline);
		} else if (DictConstants.SCORE_TYPE_HYGIENE.equals(dict_code)) {
			jedisDAO.hset(key, "info_title", title);
			jedisDAO.hset(key, "info_content", content_hygiene);
		}
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key, "score_type",vo.getScore_type());
		//增加动态记录到教师端
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, vo.getGroup_id(), vo.getTeam_id()), ActionUtil.getSysTime().getTime(),key);
		//添加动态到个人
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),ActionUtil.getSysTime().getTime(),key);
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		for (ScoreListVO svo:itemlist) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, null, svo.getStudent_id());
			String stuKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "STUDENT_ID:" + svo.getStudent_id());
			if (StringUtil.isEmpty(svo.getScore_code())) {//编辑中的取消某一个学生考勤
				jedisDAO.del(stuKey);
				jedisDAO.zrem(zsetKey, stuKey);
			} else {
				jedisDAO.hset(stuKey, "module_code", module_code);
				jedisDAO.hset(stuKey, "module_pkid", module_pkid.toString());
				jedisDAO.hset(stuKey, "suffix", "");
				String stuContent = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_CONTENT", vo.getUser_name());
				String stuTitle = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_TITLE", svo.getStudent_name(), dictVO.getDict_value());
				jedisDAO.hset(stuKey, "info_title", stuTitle);
				jedisDAO.hset(stuKey, "info_content", stuContent);
				jedisDAO.hset(stuKey, "student_id", svo.getStudent_id().toString());
				jedisDAO.hset(stuKey, "info_date", ActionUtil.getSysTime().getTime() + "");
				jedisDAO.hset(stuKey, "link_type", DictConstants.LINK_TYPE_DETAIL);
				jedisDAO.hset(stuKey, "info_url", "detail.html");
				jedisDAO.hset(stuKey, "user_type", DictConstants.USERTYPE_STUDENT);
				jedisDAO.hset(stuKey, "score_type",vo.getScore_type());
				jedisDAO.zadd(zsetKey, ActionUtil.getSysTime().getTime(), stuKey);
			}
		}
	}

	private void saveDynamicOfBedroom(final ScoreVO vo,final ReceiveVO receive) {
		//得到晨检等
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		String bedroom_name = vo.getTeam_name();
		//被考核的学生集合
		String title = "";
		String module_code = DictConstants.MODULE_CODE_BEDROOM;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");
		//被考核的学生集合
		String content = "";
		Integer Score = 0;
		List<ScoreListVO> Scorelist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		//打分时，去除学生ID重复的情况
		HashMap<Integer ,String> map = new HashMap<Integer,String>();
		for (ScoreListVO sco : Scorelist) {
			map.put(sco.getStudent_id(), vo.getItem_list());
		}

		for(ScoreListVO svolist:Scorelist){
			Score += svolist.getScore();
		}
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())&&map.size()>0) {
			title=MsgService.getMsg("MESSAGE_TEACHER_ATTEND_TITLE",vo.getUser_name(),bedroom_name,"");
			content = getCountInfo(vo);
		} else if (map.size()==0){
			title=MsgService.getMsg("MESSAGE_TEACHER_ATTEND_TITLE",vo.getUser_name(),bedroom_name,"");
			content = MsgService.getMsg("DYNAMIC_TEACHER_CONTENT_KQ_ALL");
//		} else if (Score == 0){
//			content = MsgService.getMsg("DYNAMIC_TEACHER_CONTENT_NORMAL");
		} else {
			title=MsgService.getMsg("DYNAMIC_TEACHER_TITLE",vo.getUser_name(),bedroom_name);
			content = MsgService.getMsg("DYNAMIC_TEACHER_CONTENT_ATTENDANCE_SITUATION",dictVO.getDict_value());
		}
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		jedisDAO.hset(key, "info_title", title);
		jedisDAO.hset(key, "info_content", content);
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key,"score_type",vo.getScore_type());
		//增加动态记录到教师端个人分组中
		String personKey = RedisKeyUtil.getUnionDynamicSetKey();
		jedisDAO.zadd(personKey, ActionUtil.getSysTime().getTime(),key);
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		for (ScoreListVO svo:itemlist) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,svo.getStudent_id());
			String stuKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "STUDENT_ID:"+svo.getStudent_id());
			if (StringUtil.isEmpty(svo.getScore_code())) {//编辑中的取消某一个学生考勤
				jedisDAO.del(stuKey);
				jedisDAO.zrem(zsetKey, stuKey);
			} else {
				jedisDAO.hset(stuKey, "module_code", module_code);
				jedisDAO.hset(stuKey, "module_pkid", module_pkid.toString());
				jedisDAO.hset(stuKey, "suffix", "");
				String stuContent = "";
				String stuTitle = "";
				if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
					ScoreReasonVO reson=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),svo.getScore_code(),vo.getScore_type(),vo.getTeam_type());
					stuTitle = MsgService.getMsg("MESSAGE_PARENT_ATTEND_TITLE",svo.getStudent_name(),dictVO.getDict_value(),reson.getScore_reason());
					stuContent = MsgService.getMsg("DYNAMIC_ATTEND_STUDENT",vo.getUser_name());
				} else {
					stuTitle = MsgService.getMsg("DYNAMIC_PARENT_TITLE",svo.getStudent_name(),dictVO.getDict_value());
					stuContent = MsgService.getMsg("DYNAMIC_PARENT_CONTENT",vo.getUser_name());
				}
				jedisDAO.hset(stuKey, "info_title", stuTitle);
				jedisDAO.hset(stuKey, "info_content", stuContent);
				jedisDAO.hset(stuKey, "student_id", svo.getStudent_id().toString());
				jedisDAO.hset(stuKey, "info_date", ActionUtil.getSysTime().getTime()+"");
				jedisDAO.hset(stuKey, "link_type",DictConstants.LINK_TYPE_DETAIL);
				jedisDAO.hset(stuKey, "info_url","detail.html");
				jedisDAO.hset(stuKey, "user_type",DictConstants.USERTYPE_STUDENT);
				jedisDAO.hset(stuKey,"score_type",vo.getScore_type());
				jedisDAO.zadd(zsetKey, ActionUtil.getSysTime().getTime(),stuKey);
			}
		}
	}

	/**
	 * 添加考勤模块的动态
	 * @author 陈天辉
	 */
	private void saveDynamicOfAttend(final ScoreVO vo,final ReceiveVO receive) {
		//得到晨检等
		String attend_name=redisService.getDictValue(vo.getAttend_item());
		String class_name = redisService.getTeamName(vo.getTeam_type(), vo.getGroup_id(), vo.getTeam_id());
		String teacher_name = redisService.getUserName(vo.getSchool_id(), DictConstants.USERTYPE_TEACHER, vo.getCreate_by(), null);//当前操作用户
		//String attend = vo.getAttend_item();//考勤项目
		
		//被考核学生集合
		List<ScoreListVO> Scorelist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		//打分时，去除考勤学生ID重复的情况
		HashMap<Integer ,String> map = new HashMap<Integer,String>();
		for (ScoreListVO sco : Scorelist) {
			map.put(sco.getStudent_id(), vo.getItem_list());
		}
		final String time = DateUtil.formatDate(ActionUtil.getSysTime(), DateUtil.Y_M_D_HMS);
		//final String content = MsgService.getMsg("DYNAMIC_ATTEND", class_name,attendVO.getAttend_name(),map.size());
		final String content_all = MsgService.getMsg("DYNAMIC_ATTEND_ALL");
		final String title = MsgService.getMsg("MESSAGE_TEACHER_ATTEND_TITLE", vo.getUser_name(),class_name,attend_name);
		final int size = map.size();
		String module_code = DictConstants.MODULE_CODE_ATTEND;
		Integer module_pkid = vo.getScore_id();
		String key = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "");//dynamic Key
		//将业务Key放入到分组集合中
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(receive);
		jedisDAO.zadd(groupKey, ActionUtil.getSysTime().getTime(),key);//放入DATA中
		//放入dynamic中
		jedisDAO.hset(key, "module_code", module_code);
		jedisDAO.hset(key, "module_pkid", module_pkid.toString());
		jedisDAO.hset(key, "suffix", "");
		if (size==0) {
			jedisDAO.hset(key, "info_title", title);
			jedisDAO.hset(key, "info_content", content_all);
		} else if (size>0){
			jedisDAO.hset(key, "info_title", title);
			String content = getCountInfo(vo);//例：已到：1人 请假：1人 迟到：1人 未到：1人
			jedisDAO.hset(key, "info_content", content);
		}
		jedisDAO.hset(key, "info_date",ActionUtil.getSysTime().getTime()+"");
		jedisDAO.hset(key, "link_type",DictConstants.LINK_TYPE_DETAIL);
		jedisDAO.hset(key, "info_url","detail.html");
		jedisDAO.hset(key, "user_type",DictConstants.USERTYPE_TEACHER);
		jedisDAO.hset(key,"score_type",vo.getScore_type());
		//增加动态记录到教师端
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, vo.getGroup_id(), vo.getTeam_id()), ActionUtil.getSysTime().getTime(),key);
		//添加动态到个人
		jedisDAO.zadd(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),ActionUtil.getSysTime().getTime(),key);
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		for (ScoreListVO svo:itemlist) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,svo.getStudent_id());//DATA(student_id)
			String stuKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, "STUDENT_ID:"+svo.getStudent_id());//dynamic(student_id)
			if (StringUtil.isEmpty(svo.getScore_code())) {//编辑中的取消某一个学生考勤
				jedisDAO.del(stuKey);
				jedisDAO.zrem(zsetKey, stuKey);
			} else {
				jedisDAO.hset(stuKey, "module_code", module_code);
				jedisDAO.hset(stuKey, "module_pkid", module_pkid.toString());
				jedisDAO.hset(stuKey, "suffix", "");
				ScoreReasonVO reson=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),svo.getScore_code(),vo.getScore_type(),vo.getTeam_type());
				String stuTitle = MsgService.getMsg("MESSAGE_PARENT_ATTEND_TITLE",svo.getStudent_name(),attend_name,reson.getScore_reason());
				jedisDAO.hset(stuKey, "info_title", stuTitle);
				jedisDAO.hset(stuKey, "info_content", MsgService.getMsg("DYNAMIC_ATTEND_STUDENT",vo.getUser_name()));
				jedisDAO.hset(stuKey, "student_id", svo.getStudent_id().toString());
				jedisDAO.hset(stuKey, "info_date", ActionUtil.getSysTime().getTime()+"");
				jedisDAO.hset(stuKey, "link_type",DictConstants.LINK_TYPE_DETAIL);
				jedisDAO.hset(stuKey, "info_url","detail.html");
				jedisDAO.hset(stuKey, "user_type",DictConstants.USERTYPE_STUDENT);
				jedisDAO.hset(stuKey,"score_type",vo.getScore_type());
				jedisDAO.zadd(zsetKey, ActionUtil.getSysTime().getTime(),stuKey);
			}
		}
	}

	private String getCountInfo(ScoreVO vo) {
		List<CountInfoVO> countInfoVOList= BeanUtil.jsonToList(vo.getCount_info(),CountInfoVO.class);
		String content="";
		int team_count=vo.getTeam_count();
		for (CountInfoVO countInfoVO:countInfoVOList) {
			if (DictConstants.ATTEND_STATUS_ARRIVE.equals(countInfoVO.getScore_code())) continue;
            String score_reason=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),countInfoVO.getScore_code(),vo.getScore_type(),vo.getTeam_type()).getScore_reason();
            team_count=team_count-countInfoVO.getCount();
			if (StringUtil.isEmpty(content))
                content=score_reason+"："+countInfoVO.getCount()+"人";
            else content=content+" "+score_reason+"："+countInfoVO.getCount()+"人";
        }
		return "已到："+team_count+"人"+content;
	}

	private void pushMessageOfPersonScore(ScoreVO vo,ReceiveVO receive) {
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		for (ScoreListVO svo:itemlist) {
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			HashMap<String,String> dataMap = new HashMap<String,String>();
			String stuContent=MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_CONTENT", vo.getUser_name());
			String stuTitle = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_TITLE", svo.getStudent_name(),dictVO.getDict_value());
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content",stuContent);
			dataMap.put("module_code",DictConstants.MODULE_CODE_PERSON_SCORE);
			dataMap.put("module_pkid",vo.getScore_id().toString());
			dataMap.put("student_id", svo.getStudent_id().toString());
			dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url","detail.html");
			dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
			dataMap.put("score_type",vo.getScore_type());
			getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
		}
	}

	private void pushMessageOfPerformance(ScoreVO vo,ReceiveVO receive) {
		List<ScoreListVO> slist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		if (ListUtil.isEmpty(slist)) return;
		for (ScoreListVO svo:slist) {
			String stuTitle = MsgService.getMsg("DYNAMIC_PARENT_STUDENT_TITLE",svo.getStudent_name());
			String stuContent = MsgService.getMsg("DYNAMIC_PARENT_STUDENT_CONTENT",vo.getUser_name());
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content",stuContent);
			dataMap.put("module_code",DictConstants.MODULE_CODE_PERFORMANCE);
			dataMap.put("module_pkid",vo.getScore_id().toString());
			dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url", "detail.html");
			dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
			dataMap.put("user_id", "0");
			dataMap.put("student_id",svo.getStudent_id().toString());
			dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
			dataMap.put("score_type",vo.getScore_type());
			getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
		}
	}
	
	private void pushMessageOfClassroom(ScoreVO vo,ReceiveVO receive) {
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		for (ScoreListVO svo:itemlist) {
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			HashMap<String,String> dataMap = new HashMap<String,String>();
			String stuContent=MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_CONTENT", vo.getUser_name());
			String stuTitle = MsgService.getMsg("DYNAMIC_CLASSROOM_PARENT_TITLE", svo.getStudent_name(),dictVO.getDict_value());
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content",stuContent);
			dataMap.put("module_code",DictConstants.MODULE_CODE_CLASSROOM);
			dataMap.put("module_pkid",vo.getScore_id().toString());
			dataMap.put("student_id", svo.getStudent_id().toString());
			dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url","detail.html");
			dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
			dataMap.put("score_type",vo.getScore_type());
			getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
		}
	}

	private void pushMessageOfBedroom(final ScoreVO vo,final ReceiveVO receive) {
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(vo.getScore_type());
		dict.setOther_field(vo.getModule_code());
		DictVO dictVO=new DictVO();
		dictVO=dao.queryObject("dictMap.getDictByOtherField",dict);
		if (dictVO==null)
			dictVO=dao.queryObject("dictMap.getDictInfo", vo.getScore_type());
		for (ScoreListVO svo:itemlist) {
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			HashMap<String,String> dataMap = new HashMap<String,String>();
			String stuContent = "";
			String stuTitle = "";
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
				ScoreReasonVO reson=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),svo.getScore_code(),vo.getScore_type(),vo.getTeam_type());
				stuTitle = MsgService.getMsg("MESSAGE_PARENT_ATTEND_TITLE",svo.getStudent_name(),dictVO.getDict_value(),reson.getScore_reason());
				stuContent = MsgService.getMsg("DYNAMIC_ATTEND_STUDENT",vo.getUser_name());
			} else {
				stuTitle = MsgService.getMsg("DYNAMIC_PARENT_TITLE",svo.getStudent_name(),dictVO.getDict_value());
				stuContent = MsgService.getMsg("DYNAMIC_PARENT_CONTENT",vo.getUser_name());
			}
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content",stuContent);
			dataMap.put("module_code",DictConstants.MODULE_CODE_BEDROOM);
			dataMap.put("module_pkid",vo.getScore_id().toString());
			dataMap.put("student_id", svo.getStudent_id().toString());
			dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url","detail.html");
			dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
			dataMap.put("score_type",vo.getScore_type());
			getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
		} 
	}

	/**
	 * 添加考勤模块的动态
	 * @author 陈天辉
	 */
	private void pushMessageOfAttend(final ScoreVO vo,final ReceiveVO receive) {
		//被考核的学生集合
		List<ScoreListVO> itemlist=BeanUtil.jsonToList(vo.getItem_list(), ScoreListVO.class);
		String time = DateUtil.formatDate(ActionUtil.getSysTime(), DateUtil.Y_M_D_HMS);
		for (ScoreListVO svo:itemlist){
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			ScoreReasonVO reson=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),svo.getScore_code(),vo.getScore_type(),vo.getTeam_type());
			String attend_name=redisService.getDictValue(vo.getAttend_item());
			String stuContent = MsgService.getMsg("DYNAMIC_ATTEND_STUDENT",vo.getUser_name());
			String stuTitle = MsgService.getMsg("MESSAGE_PARENT_ATTEND_TITLE", svo.getStudent_name(),attend_name,reson.getScore_reason());
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content",stuContent);
			dataMap.put("module_code",DictConstants.MODULE_CODE_ATTEND);
			dataMap.put("module_pkid",vo.getScore_id().toString());
			dataMap.put("student_id", svo.getStudent_id().toString());
			dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url","detail.html");
			dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
			dataMap.put("score_type",vo.getScore_type());
			getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
			
			//以下是微信家长端推送
			dataMap.put("attend_date",time);
			dataMap.put("class_name",svo.getTeam_name());
			dataMap.put("student_name", svo.getStudent_name());
			dataMap.put("client_url", "pdetail.html");
			wechatService.pushAttendanceMessage(dataMap, svo.getStudent_id(), ActionUtil.getSchoolID());
		} 
	}

	//通过score_id获取扣分信息（左连接）
	public List<ScoreListVO> getScoreByID(Integer score_id) {
		if (IntegerUtil.isEmpty(score_id)) throw new BusinessException("score_id不能为空或0哦！");
		ScoreVO vo=new ScoreVO();
		vo.setScore_id(score_id);
		ScoreVO svo=dao.queryObject("scoreMap.getScoreList", vo);
		if (svo==null) throw new BusinessException(MsgService.getMsg("NO_RECORD"));
		ScoreListVO slvo=new ScoreListVO();
		slvo.setTeam_type(svo.getTeam_type());
		slvo.setTeam_id(svo.getTeam_id());
		slvo.setScore_id(score_id);
		slvo.setScore_type(svo.getScore_type());
		slvo.setScore_date(svo.getScore_date());
		if (StringUtil.isNotEmpty(svo.getAttend_item())) slvo.setAttend_item(svo.getAttend_item());
		List<ScoreListVO> list=getScoreListOfTeam(slvo);
		if (StringUtil.isEmpty(svo.getAttend_item())){
			for (ScoreListVO scorevo:list){
				scorevo.setTeam_name(svo.getTeam_name());
				scorevo.setUser_name(redisService.getUserName(svo.getSchool_id(), DictConstants.USERTYPE_TEACHER, svo.getCreate_by(), null));
				scorevo.setScore_date(svo.getScore_date());
				scorevo.setSender_id(svo.getCreate_by());
                scorevo.setCreate_date(svo.getCreate_date());
				scorevo.setTeam_type(svo.getTeam_type());
				scorevo.setGroup_id(svo.getGroup_id());
			}
		} else {
			for (ScoreListVO scorevo:list) {
				scorevo.setTeam_name(svo.getTeam_name());
				scorevo.setAttend_item(svo.getAttend_item());
				scorevo.setUser_name(redisService.getUserName(svo.getSchool_id(), DictConstants.USERTYPE_TEACHER, svo.getCreate_by(), null));
				scorevo.setScore_date(svo.getScore_date());
				scorevo.setSender_id(svo.getCreate_by());
                scorevo.setCreate_date(svo.getCreate_date());
				scorevo.setTeam_type(svo.getTeam_type());
				scorevo.setGroup_id(svo.getGroup_id());
			}
		}
		return list;
	}

	//通过score_id获取扣分信息
    public ScoreDetailVO getScoreListById(Integer score_id) {
        List<ScoreListVO> list=dao.queryForList("scoreListMap.getScoreList", score_id);
        ScoreVO vo=new ScoreVO();
        vo.setScore_id(score_id);
        ScoreVO scorevo=dao.queryObject("scoreMap.getScoreList", vo);
        for (ScoreListVO slvo:list) {
            if (IntegerUtil.isEmpty(slvo.getStudent_id())) {//动态详情：用于教室卫生，学生ID为空
                slvo.setTeam_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS, scorevo.getGroup_id(), scorevo.getTeam_id()));
            } else if (IntegerUtil.isNotEmpty(slvo.getStudent_id())) {
                slvo.setStudent_name(redisService.getUserName(slvo.getSchool_id(), DictConstants.USERTYPE_STUDENT, null, slvo.getStudent_id()));
            }
            slvo.setUser_name(redisService.getUserName(scorevo.getSchool_id(), DictConstants.USERTYPE_TEACHER, scorevo.getCreate_by(), null));
            //slvo.setScore(slvo.getScore()*slvo.getCount());
        }
        ScoreDetailVO sdvo=new ScoreDetailVO();
        sdvo.setScore_date(scorevo.getScore_date());
        sdvo.setUser_name(redisService.getUserName(scorevo.getSchool_id(), DictConstants.USERTYPE_TEACHER, scorevo.getCreate_by(), null));
		sdvo.setTeam_type(scorevo.getTeam_type());
		if (IntegerUtil.isNotEmpty(scorevo.getGroup_id()))
			sdvo.setGroup_id(scorevo.getGroup_id());
		sdvo.setTeam_id(scorevo.getTeam_id());
		sdvo.setTeam_name(redisService.getTeamName(scorevo.getTeam_type(), scorevo.getGroup_id(), scorevo.getTeam_id()));
		if (StringUtil.isNotEmpty(scorevo.getAttend_item()))
			sdvo.setAttend_item(scorevo.getAttend_item());
		sdvo.setScore_type(scorevo.getScore_type());
		sdvo.setCreate_by(scorevo.getCreate_by());
		sdvo.setCreate_date(scorevo.getCreate_date());
		if (ListUtil.isNotEmpty(list))
            sdvo.setScore_detail(BeanUtil.ListTojson(list));
        return sdvo;
    }

	//添加扣分项
	public Integer addScoreReason(ScoreReasonVO vo) {//页面要传入module_code
		vo.setSchool_id(ActionUtil.getSchoolID());
		String score_code=dao.queryObject("scoreReasonMap.getScorecodemax", ActionUtil.getSchoolID());
		String newScorecode=Integer.parseInt(score_code)+1+"";
		vo.setDescription(vo.getScore_reason());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setScore_code("0"+newScorecode);
		int id= dao.insertObjectReturnID("scoreReasonMap.insertScoreReason", vo);
		//添加统计表头项
		addTableHead(vo,DictConstants.COUNT_TYPE_STUDENT);
		addTableHead(vo,DictConstants.COUNT_TYPE_TEAM);
		//team_type:teamtype,score_type:scoretype,score_reason:nameVal,score:selectVal
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			vo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
			addTableHead(vo,DictConstants.COUNT_TYPE_STUDENT);//添加统计表头项
			addTableHead(vo,DictConstants.COUNT_TYPE_TEAM);
			//出勤率
			updateRate();
		} else {
			//总扣分
			updateTotal(vo);
		}
		//redis
		redisService.saveScoreReasonToRedis(ActionUtil.getSchoolID(), vo.getScore_code(), vo.getScore_reason(),vo.getScore());
		return id;
	}

	//总扣分
	private void updateTotal(ScoreReasonVO vo) {
		ScoreReasonVO srvo=new ScoreReasonVO();
		srvo.setSchool_id(ActionUtil.getSchoolID());
		srvo.setTeam_type(vo.getTeam_type());
		srvo.setScore_type(vo.getScore_type());
		srvo.setIs_active(1);
		//srvo.setModule_code(vo.getModule_code());
		List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", srvo);
		String total="";
		for (ScoreReasonVO svo:list) {//{013051}%2B{013052}%2B{013053}
			if (StringUtil.isEmpty(total)) {
				total+="{"+svo.getScore_code()+"}";
			} else {
				total+="%2B{"+svo.getScore_code()+"}";
			}
		}
		TableHeadVO hvo=new TableHeadVO();
		hvo.setSchool_id(ActionUtil.getSchoolID());
		hvo.setTeam_type(srvo.getTeam_type());
		hvo.setScore_type(srvo.getScore_type());
		hvo.setField("total");
		hvo.setField_func(total);
		hvo.setUpdate_by(ActionUtil.getUserID());
		hvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("tableMap.updateFieldFunc", hvo);//更新总扣分函数
	}

	//出勤率
	private void updateRate() {
		ScoreReasonVO srvo5=new ScoreReasonVO();
		srvo5.setSchool_id(ActionUtil.getSchoolID());
		srvo5.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		srvo5.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		srvo5.setIs_active(1);
		List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", srvo5);
		String total="";
		String sum="";//field_func_sum
		for (ScoreReasonVO svo:list) {//({total_day}-{013041}-{013042}-{013043})*100/{total_day}%
			if (StringUtil.isEmpty(total)) {
				total+="({total_day}-{"+svo.getScore_code()+"}";
				sum+="([{total_day}*{team_count})]-[{"+svo.getScore_code()+"}";
			} else {
				total+="-{"+svo.getScore_code()+"}";
				sum+="+{"+svo.getScore_code()+"}";
			}
		}
		total+=")*100/{total_day}%";
		sum+="])*100/([{total_day}*{team_count}])%";
		TableHeadVO hvo=new TableHeadVO();
		hvo.setSchool_id(ActionUtil.getSchoolID());
		hvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		hvo.setScore_type(srvo5.getScore_type());
		hvo.setField("rate");
		hvo.setField_func(total);
		hvo.setField_func_sum(sum);
		hvo.setUpdate_by(ActionUtil.getUserID());
		hvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("tableMap.updateFieldFunc", hvo);//更新个人出勤率函数
		String total1="";
		for (ScoreReasonVO svo:list) {//({total_day}*{team_count}-{013041}-{013042}-{013043})*100/({total_day}*{team_count})%
			if (StringUtil.isEmpty(total1)) {
				total1+="({total_day}*{team_count}-{"+svo.getScore_code()+"}";
			} else {
				total1+="-{"+svo.getScore_code()+"}";
			}
		}
		total1+=")*100/({total_day}*{team_count})%";
		hvo.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		hvo.setField_func(total1);
		dao.updateObject("tableMap.updateFieldFunc", hvo);//更新班级出勤率函数
	}

	//添加统计表头项
	private void addTableHead(ScoreReasonVO vo,String count_type) {
		TableHeadVO hh=new TableHeadVO();
		hh.setSchool_id(ActionUtil.getSchoolID());
		hh.setTeam_type(vo.getTeam_type());
		hh.setScore_type(vo.getScore_type());
		hh.setCount_type(count_type);
		hh.setField(vo.getScore_code());
		hh.setField_name(vo.getScore_reason());
		hh.setSort(5);
		hh.setFunc_type(DictConstants.TABLE_SUM);//汇总
		hh.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", hh);
	}

	//修改扣分项
	public void updateScoreReason(ScoreReasonVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("scoreReasonMap.updateScoreReason", vo);
		ScoreReasonVO srvo=dao.queryObject("scoreReasonMap.getScoreReasonByID", vo.getId());
		TableHeadVO tvo=new TableHeadVO();
		tvo.setSchool_id(ActionUtil.getSchoolID());
		tvo.setField(srvo.getScore_code());
		tvo.setField_name(vo.getScore_reason());
		tvo.setUpdate_by(ActionUtil.getUserID());
		tvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("tableMap.updateTable", tvo);//修改table表，用于统计
		//redis
		redisService.saveScoreReasonToRedis(ActionUtil.getSchoolID(), srvo.getScore_code(), vo.getScore_reason(),vo.getScore());
	}

	//删除扣分项
	public void deleteScoreReason(Integer id) {
		ScoreReasonVO vo=dao.queryObject("scoreReasonMap.getScoreReasonByID", id);
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("scoreReasonMap.updateReasonStatus", vo);//不删除，修改状态为禁用
		//删除统计扣分项
		TableHeadVO thvo=new TableHeadVO();
		thvo.setSchool_id(ActionUtil.getSchoolID());
		thvo.setField(vo.getScore_code());
		dao.deleteObject("tableMap.deleteTableHead", thvo);
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) 
			updateRate();//出勤率
		 else 
			updateTotal(vo);//总扣分
	}

	//删除主表一条记录
	public void deleteByScoreID(Integer score_id) {
		ScoreVO vo=new ScoreVO();
		vo.setScore_id(score_id);
		ScoreVO svo=dao.queryObject("scoreMap.getScoreList", vo);
		List<ScoreListVO> list=dao.queryForList("scoreListMap.getScoreList", score_id);//被扣分的学生
		dao.deleteObject("scoreMap.deleteScore", score_id);
		dao.deleteObject("scoreListMap.deleteByScoreID", score_id);
		//redis动态
		String dynamicKey = RedisKeyUtil.getDynamicKey(svo.getModule_code(), score_id, "");//dynamic Key
		jedisDAO.del(dynamicKey);
		String groupKey = RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, svo.getGroup_id(), svo.getTeam_id());
		jedisDAO.zrem(groupKey, dynamicKey);
		if (DictConstants.MODULE_CODE_BEDROOM.equals(svo.getModule_code())) {//寝室管理还要删除个人分组里的动态
			String personKey = RedisKeyUtil.getUnionDynamicSetKey();
			jedisDAO.zrem(personKey, dynamicKey);
		}
		for (ScoreListVO slvo:list) {
			String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,slvo.getStudent_id());//DATA(student_id)
			String stuKey = RedisKeyUtil.getDynamicKey(slvo.getModule_code(), score_id, "STUDENT_ID:"+slvo.getStudent_id());//dynamic(student_id)
			jedisDAO.zrem(zsetKey, stuKey);
			jedisDAO.del(stuKey);
		} 
		//redis统计
		tableService.deleteScoreToRedis(list,svo);
	}

	//获取学生列表（带最近打分时间）
	public List<ScoreListVO> getStudentlist(ScoreListVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {//教室
			List<ScoreListVO> slist=dao.queryForList("scoreListMap.getStudentList", vo);
			for (ScoreListVO slvo:slist) {
				if (IntegerUtil.isEmpty(slvo.getTeam_id())) continue;
				slvo.setTeam_name(classService.getClassByID(slvo.getTeam_id()).getClass_name());
			}
			return slist;
		} else if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type())) {//兴趣班
			List<ScoreListVO> slist=dao.queryForList("scoreListMap.getInterestStudentList", vo);
			if (IntegerUtil.isEmpty(vo.getTeam_id())) throw new BusinessException("team_id不能为空！");
			String team_name=redisService.getTeamName(vo.getTeam_type(), 0, vo.getTeam_id());
			for (ScoreListVO slco:slist) {
				StudentVO svo=userService.getStudentById( slco.getStudent_id());
				slco.setStudent_code(svo.getStudent_code());
				slco.setStudent_name(svo.getStudent_name());
				slco.setTeam_name(team_name);
			}
			return slist;
		}
		return null;
	}

	//获取考勤班级的教师
	public List<TaskVO> getScoreListTeacher(TaskVO vo) {
		vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);//团队类型：教室
		vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);//打分类型：考勤
		String time=DateUtil.getNow("yyyy-MM-dd");//获取系统时间
		vo.setScore_date(time);
		return dao.queryForList("scoreListMap.getScoreAttendTeacherList", vo);
	}
	
	//获取学校管理层教师
	public List<TaskVO> getScoreListSchoolLeader(TaskVO vo){
		vo.setDuty(DictConstants.DICT_TEACHER_CLASS);
		String time=DateUtil.getNow("yyyy-MM-dd");//获取系统时间
		vo.setScore_date(time);
		return dao.queryForList("scoreMap.getSchoolLeaderList",vo);
	}

	//新--------------------------------------------------------
    //获取学生扣分信息（除了考勤）
    public List<ScoreListVO> getScoreNoAttend(){
        ScoreListVO listVO=new ScoreListVO();
        listVO.setStudent_id(ActionUtil.getStudentID());
        listVO.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
        List<ScoreListVO> list= dao.queryForList("scoreListMap.getScoreNoAttend",listVO);
        for (ScoreListVO vo:list)
            vo.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getCreate_by(),0));
        return list;
    }

    //学生评分：搜索学生
    public List<StudentVO> getStudentBySearch(StudentVO vo){
        vo.setSchool_id(ActionUtil.getSchoolID());
        List<StudentVO> list=dao.queryForList("studentMap.getStudentBySearch",vo);
        for (StudentVO stuID:list) {
			stuID.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,stuID.getGrade_id(),stuID.getClass_id()));
        }
        return list;
    }

    //统计
	public Map<String,Object> getScoreCount(ScoreCountVO vo){
		Map<String,Object> hashMap=new HashMap<String,Object>();
        vo.setSchool_id(ActionUtil.getSchoolID());
        if (DictConstants.COUNT_TYPE_TEAM.equals(vo.getCount_type())) {
			List<ScoreVO> list=dao.queryForList("scoreListMap.getScoreCount",vo);
            for (ScoreVO svo:list){
                svo.setTeam_name(redisService.getTeamName(svo.getTeam_type(),svo.getGroup_id(),svo.getTeam_id()));
            }
            hashMap.put("ascDetail",BeanUtil.ListTojson(list));
        } else if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {
			vo.setSort(1);//升序
			List<ScoreVO> sList=dao.queryForList("scoreListMap.getScoreCountOfStudent",vo);
			vo.setSort(0);//降序
			List<ScoreVO> scList=dao.queryForList("scoreListMap.getScoreCountOfStudent",vo);
			for (ScoreVO svo:sList)
				svo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
			for (ScoreVO svo:scList)
				svo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
			hashMap.put("ascDetail",BeanUtil.ListTojson(sList));
			hashMap.put("desDetail",BeanUtil.ListTojson(scList));
        }
        return hashMap;
	}

	//班级评分
	public Map<String,Object> getScoreDetail(ScoreCountVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
//		List<DashBoardVO> dataList=new ArrayList<DashBoardVO>();
		Map<String,Object> hashMap=new HashMap<String,Object>();
		List<DashBoardVO> dlist=new ArrayList<DashBoardVO>();
		List<DashBoardVO> daList=new ArrayList<DashBoardVO>();
//		if (DictConstants.SUM_TYPE_DAY.equals(vo.getSum_type())) {//统计今天
//			String day = DateUtil.getNow("yyyy-MM-dd");
//			vo.setStart_date(day);
//			vo.setEnd_date(day);
//			DashBoardVO dvo=dao.queryObject("scoreListMap.getScoreDetail",vo);//当天折线数据
//			dataList.add(dvo);
			dlist=dao.queryForList("scoreListMap.getScoreSum",vo);
			daList=dao.queryForList("scoreListMap.getScoreCodeDetail",vo);
//			for (int i=0;i<6;i++) {//后6天的折线数据
//				day=DateUtil.formatDate(DateUtil.addDay(DateUtil.formatStringToDate(day,"yyyy-MM-dd"),-1),"yyyy-MM-dd");
//				vo.setStart_date(day);
//				vo.setEnd_date(day);
//				DashBoardVO dvo1=dao.queryObject("scoreListMap.getScoreDetail",vo);
//				dataList.add(dvo1);
//			}
//		} else if (DictConstants.SUM_TYPE_WEEK.equals(vo.getSum_type())) {//统计本周
//			String week = DateUtil.getFirstDayOfWeek(ActionUtil.getSysTime(),"yyyy-MM-dd");
//			String lastDay=DateUtil.formatDate(DateUtil.addDay(DateUtil.formatStringToDate(week,"yyyy-MM-dd"),6),"yyyy-MM-dd");
//			vo.setStart_date(week);
//			vo.setEnd_date(lastDay);
//			DashBoardVO dvo=dao.queryObject("scoreListMap.getScoreDetail",vo);//本周折线数据
//			dataList.add(dvo);
//			dlist=dao.queryForList("scoreListMap.getScoreSum",vo);
//			daList=dao.queryForList("scoreListMap.getScoreCodeDetail",vo);
//			for (int i=0;i<3;i++) {//后3周的折线数据
//				week=DateUtil.formatDate(DateUtil.addDay(DateUtil.formatStringToDate(week,"yyyy-MM-dd"),-6),"yyyy-MM-dd");
//				lastDay=DateUtil.formatDate(DateUtil.addDay(DateUtil.formatStringToDate(week,"yyyy-MM-dd"),6),"yyyy-MM-dd");
//				vo.setStart_date(week);
//				vo.setEnd_date(lastDay);
//				DashBoardVO dvo1=dao.queryObject("scoreListMap.getScoreDetail",vo);
//				dataList.add(dvo1);
//			}
//		} else if (DictConstants.SUM_TYPE_MONTH.equals(vo.getSum_type())) {//统计本月
//			String month = DateUtil.getFirstDayOfMonth(ActionUtil.getSysTime(),"yyyy-MM-dd");
//			String lastDay=DateUtil.getLastDayOfMonth(DateUtil.formatStringToDate(month,"yyyy-MM-dd"),"yyyy-MM-dd");
//			vo.setStart_date(month);
//			vo.setEnd_date(lastDay);
//			DashBoardVO dvo=dao.queryObject("scoreListMap.getScoreDetail",vo);//本月折线数据
//			dataList.add(dvo);
//			dlist=dao.queryForList("scoreListMap.getScoreSum",vo);
//			daList=dao.queryForList("scoreListMap.getScoreCodeDetail",vo);
//			for (int i=0;i<5;i++) {//前5月折线数据
//				lastDay=DateUtil.formatDate(DateUtil.addDay(DateUtil.formatStringToDate(month,"yyyy-MM-dd"),-1),"yyyy-MM-dd");
//				month = DateUtil.getFirstDayOfMonth(DateUtil.addDay(DateUtil.formatStringToDate(month,"yyyy-MM-dd"),-1),"yyyy-MM-dd");
//				vo.setStart_date(month);
//				vo.setEnd_date(lastDay);
//				DashBoardVO dvo1=dao.queryObject("scoreListMap.getScoreDetail",vo);
//				dataList.add(dvo1);
//			}
//		}
		//hashMap.put("detail",BeanUtil.ListTojson(dataList));//折线数据
		hashMap.put("sum",BeanUtil.ListTojson(dlist));//总分
		hashMap.put("codeDetail",BeanUtil.ListTojson(daList));//扣分项详情
		return hashMap;
	}
}