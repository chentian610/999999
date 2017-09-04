package com.ninesky.classtao.info.service.impl;

import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.ListUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("InfoServiceImpl")
public class InfoServiceImpl implements InfoService {
	
	@Autowired
	private GeneralDAO dao;
	@Autowired
	private UserService userService;
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private GetuiService getuiService;

	/**
	 * 添加消息
	 * @param vo 消息内容
	 */
	public void addInfo(InfoVO vo) {
		//添加消息 
		vo.setInfo_id(dao.insertObjectReturnID("infoMap.insertInfo", vo));
		//添加接收
		List<InfoReceiveVO > receiveList = getInfoReceiveList(vo);
		if(ListUtil.isEmpty(receiveList)) throw new BusinessException("没有找到动态接收者...");
		dao.insertObject("infoReceiveMap.insertInfoReceiveBatch", receiveList);
		//推送消息
		pushMessage(vo, receiveList);
	}
	
	/**
	 * 添加消息(已有接收对象)
	 * @param vo 消息内容
	 * @param list 消息接收人
	 */
	public Integer addInfo(InfoVO vo, List<InfoReceiveVO> list){
		if(ListUtil.isEmpty(list)) 
			throw new BusinessException("动态接收对象不能为空.....");
		//添加消息
		Integer infoId = dao.insertObjectReturnID("infoMap.insertInfo", vo);
		//添加接收
		for(InfoReceiveVO recVO : list){
			recVO.setInfo_id(infoId);
		}
		dao.insertObject("infoReceiveMap.insertInfoReceiveBatch", list);
		//推送消息
		//if(StringUtil.isEmpty(vo.getTransmissionContent())) return infoId;
		pushMessage(vo, list);
		return infoId;
	}
	
	/**
	 * 获取接收的消息列表 
	 * @param vo
	 */
	public List<InfoReceiveVO> getInfoReceiveList(InfoReceiveVO vo) {
		if(!StringUtil.isEmpty(vo.getInfo_date())){
		Date infodate=DateUtil.formatStringToDate(vo.getInfo_date(), "yyyy-M-d");
		try {
			vo.setInfo_date(DateUtil.formatDate(infodate, "yyyy-MM-dd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		List<InfoReceiveVO> list = dao.queryForList("infoReceiveMap.getInfoReceiveList", vo);
		//设置已读
		if(!IntegerUtil.isEmpty(vo.getInfo_id()) || DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType()))
			dao.updateObject("infoReceiveMap.updateInfoReceiveIsRead",vo);//教师批量更新，家长某一条更新
		return list;
	}
	
	/**
	 * 根据模块信息更新动态消息
	 * @param vo
	 */
	public void updateInformation(InfoReceiveVO vo){
		dao.updateObject("infoMap.updateInfoByModule", vo);
		dao.updateObject("infoReceiveMap.updateInfoByModule", vo);
		pushPhotoUpdateMessage(vo);
	}
	
	/**
	 * 获取班级当天的相册动态信息
	 * @param vo 班级、模块code，模块pkid(当天)
	 * @return
	 */
	public InfoReceiveVO getReceiveInfoByModule(InfoReceiveVO vo){
		return dao.queryObject("infoReceiveMap.getReceiveInfoByModule", vo);
	}
	
	/**
	 * 获取未读消息数量
	 * @param paramMap 
	 */
	public Integer getUnreadCount(Map<String, String> paramMap) {
		return dao.queryObject("infoReceiveMap.getUnreadCount", paramMap);
	}
	
	
	//根据年级，班级数组获取全部接收对象
	private List<InfoReceiveVO> getInfoReceiveList(InfoVO vo){
		if(StringUtil.isEmpty(vo.getReceive_list()))
			throw new BusinessException("动态接收对象不能为空....");
		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
		List<InfoReceiveVO> teacherRecList = new ArrayList<InfoReceiveVO>();
		JSONArray array = JSONArray.fromObject(vo.getReceive_list()); 
		for (int i = 0; i < array.size(); i++) 
		{ 
		    JSONObject json = (JSONObject) array.get(i);  
			vo.setGrade_id(json.getInt("grade_id"));
			vo.setClass_id(json.getInt("class_id"));
			//只发给学生（家长根据学生获取动态）
			if(DictConstants.USERTYPE_STUDENT.equals(json.getString("user_type")) ||
					DictConstants.USERTYPE_PARENT.equals(json.getString("user_type"))){
				receiveList.addAll(getStuUserList(vo));
			//只发给老师
			}else if(DictConstants.USERTYPE_TEACHER.equals(json.getString("user_type"))){
				teacherRecList.addAll(getTeacherUserList(vo));
			}else{
				receiveList.addAll(getStuUserList(vo));
				teacherRecList.addAll(getTeacherUserList(vo));
			}
		}
		//过滤一个老师教多个班级或者一个老师教教多门课程
		filterRepeatReceive(teacherRecList);
		receiveList.addAll(teacherRecList);
		return receiveList;
	}
	//根据学校、年级、班级获取学生接收集合
	private List<InfoReceiveVO> getStuUserList(InfoVO infoVO){
			List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
			StudentVO stuVO = new StudentVO();
			stuVO.setSchool_id(infoVO.getSchool_id());
			stuVO.setGrade_id(infoVO.getGrade_id());
			stuVO.setClass_id(infoVO.getClass_id());
			List<StudentVO> stuList = userService.getStuUserList(stuVO);
			if(ListUtil.isEmpty(stuList)) return receiveList;
			for(StudentVO student : stuList)
			{
				InfoReceiveVO vo = new InfoReceiveVO();
				vo.setInfo_id(infoVO.getInfo_id());
				vo.setSchool_id(student.getSchool_id());
				vo.setGrade_id(student.getGrade_id());
				vo.setClass_id(student.getClass_id());
				vo.setUser_id(0);//学生设置为0
				vo.setStudent_id(student.getStudent_id());//家长查询动态的依据
				vo.setModule_code(infoVO.getModule_code());
				vo.setModule_pkid(infoVO.getModule_pkid());
				vo.setUser_type(DictConstants.USERTYPE_STUDENT);
				vo.setReceive_name(student.getStudent_name());
				vo.setInfo_date(infoVO.getInfo_date());
				vo.setInfo_type(infoVO.getInfo_type());
				vo.setInfo_url(infoVO.getInfo_url());
				vo.setInfo_title(infoVO.getInfo_title());
				vo.setInfo_content(infoVO.getInfo_content());
				vo.setShow_type(infoVO.getShow_type());
				vo.setInit_data(infoVO.getInit_data());
				vo.setPhoto_list(infoVO.getPhoto_list());
				vo.setCount_info(infoVO.getCount_info());
				vo.setCreate_date(infoVO.getCreate_date());
				vo.setCreate_by(infoVO.getCreate_by());
				receiveList.add(vo);
			}
			return receiveList;
	}
	
//	//根据学校、年级、班级获取家长接收集合
//	private List<InfoReceiveVO> getParentUserList(InfoVO infoVO){
//			List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
//			ParentVO parVO = new ParentVO();
//			parVO.setSchool_id(infoVO.getSchool_id());
//			parVO.setGrade_id(infoVO.getGrade_id());
//			parVO.setClass_id(infoVO.getClass_id());
//			List<ParentVO> parUserList = userService.getParUserList(parVO);
//			if(ListUtil.isEmpty(parUserList)) return receiveList;
//			for(ParentVO parent : parUserList)
//			{
//				if(parent.getUser_id()==null) continue;
//				InfoReceiveVO vo = new InfoReceiveVO();
//				vo.setInfo_id(infoVO.getInfo_id());
//				vo.setSchool_id(parent.getSchool_id());
//				vo.setGrade_id(parent.getGrade_id());
//				vo.setClass_id(parent.getClass_id());
//				vo.setUser_id(parent.getUser_id());
//				vo.setModule_code(infoVO.getModule_code());
//				vo.setModule_pkid(infoVO.getModule_pkid());
//				vo.setUser_type(DictConstants.USERTYPE_PARENT);
//				vo.setReceive_name(parent.getParent_name());
//				vo.setInfo_date(DateUtil.formatDateToString(
//						ActionUtil.getSysTime(), "yyyy-MM-dd"));
//				vo.setInfo_type(infoVO.getInfo_type());
//				vo.setInfo_url(infoVO.getInfo_url());
//				vo.setInfo_title(infoVO.getInfo_title());
//				vo.setInfo_content(infoVO.getInfo_content());
//				vo.setShow_type(infoVO.getShow_type());
//				vo.setInit_data(infoVO.getInit_data());
//				vo.setPhoto_list(infoVO.getPhoto_list());
//				vo.setCount_info(infoVO.getCount_info());
//				vo.setCreate_date(infoVO.getCreate_date());
//				vo.setCreate_by(infoVO.getCreate_by());
//				receiveList.add(vo);
//			}
//			return receiveList;
//	}
//	
	//根据学校、年级、班级获取教师接收集合
	private List<InfoReceiveVO> getTeacherUserList(InfoVO infoVO){
			List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
			TeacherVO teaVO = new TeacherVO();
			teaVO.setSchool_id(infoVO.getSchool_id());
			teaVO.setGrade_id(infoVO.getGrade_id());
			teaVO.setClass_id(infoVO.getClass_id());
			List<TeacherVO> teaUserList = userService.getTeaUserList(teaVO);
			if(ListUtil.isEmpty(teaUserList)) return receiveList;
			for(TeacherVO teacher : teaUserList)
			{	
				if(IntegerUtil.isEmpty(teacher.getUser_id())) continue;
				InfoReceiveVO vo = new InfoReceiveVO();
				vo.setInfo_id(infoVO.getInfo_id());
				vo.setSchool_id(teacher.getSchool_id());
				vo.setGrade_id(teacher.getGrade_id());
				vo.setClass_id(teacher.getClass_id());
				vo.setUser_id(teacher.getUser_id());
				vo.setStudent_id(0);
				vo.setModule_code(infoVO.getModule_code());
				vo.setModule_pkid(infoVO.getModule_pkid());
				vo.setUser_type(DictConstants.USERTYPE_TEACHER);
				vo.setReceive_name(teacher.getTeacher_name());
				vo.setInfo_date(infoVO.getInfo_date());
				vo.setInfo_type(infoVO.getInfo_type());
				vo.setInfo_url(infoVO.getInfo_url());
				vo.setInfo_title(infoVO.getInfo_title());
				vo.setInfo_content(infoVO.getInfo_content());
				vo.setShow_type(infoVO.getShow_type());
				vo.setInit_data(infoVO.getInit_data());
				vo.setPhoto_list(infoVO.getPhoto_list());
				vo.setCount_info(infoVO.getCount_info());
				vo.setCreate_date(infoVO.getCreate_date());
				vo.setCreate_by(infoVO.getCreate_by());
				receiveList.add(vo);
			}
			return receiveList;
	}

	//相册更新的提醒消息
	private void pushPhotoUpdateMessage(InfoReceiveVO vo){
		//消息
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(vo.getSchool_id());
		infoVO.setModule_code(vo.getModule_code());
		infoVO.setModule_pkid(vo.getModule_pkid());
		infoVO.setInfo_title(vo.getInfo_title());
		infoVO.setInfo_content(vo.getInfo_content());
		infoVO.setInfo_type(vo.getInfo_type());
		//提醒对象
		List<InfoReceiveVO> list 
			= dao.queryForList("infoReceiveMap.getReceiveInfoByInfoId", vo);
		if(ListUtil.isEmpty(list)) return;
		List<String> accountList = new ArrayList<String>();
		ParentVO parent = new ParentVO();
		for(InfoReceiveVO recVO : list){
			if(IntegerUtil.isEmpty(recVO.getUser_id()))
				getParentAccountList(recVO, accountList, parent);//根据学生id找家长用户id
			else
				accountList.add(recVO.getUser_id().toString());
		}
		//提醒
		xgPushListToApp(infoVO, accountList);
		gtPushListToApp(infoVO, accountList);
	}
	
	//推送消息
	private void pushMessage(InfoVO vo, List<InfoReceiveVO> list){
		if(ListUtil.isEmpty(list)) return;
		List<String> accountList = new ArrayList<String>();
		ParentVO parent = new ParentVO();
		if(DictConstants.MODULE_CODE_ATTEND.equals(vo.getModule_code()) || 
				DictConstants.MODULE_CODE_COMMENT.equals(vo.getModule_code()) || 
			DictConstants.MODULE_CODE_PERFORMANCE.equals(vo.getModule_code())){//考勤模块,个人评价模块，在校表现模块
			for(InfoReceiveVO recVO : list){
				if(IntegerUtil.isEmpty(recVO.getUser_id())){
					getParentAccountList(recVO, accountList, parent);//根据学生id找家长用户id
					if(ListUtil.isEmpty(accountList)) return;
					switch(vo.getModule_code()){
					case "009005":vo.setTransmissionContent(MsgService.getMsg("parentAttend", recVO.getReceive_name()));
						break;
					case "009017":vo.setTransmissionContent(MsgService.getMsg("studentPerformance", recVO.getReceive_name(),"评价",schoolService.getSchoolById(
							ActionUtil.getSchoolID()).getSchool_name()));
						break;
					case "009025":vo.setTransmissionContent(MsgService.getMsg("studentPerformance", recVO.getReceive_name(),"在校表现",schoolService.getSchoolById(
							ActionUtil.getSchoolID()).getSchool_name()));
						break;
					}
					gtPushListToApp(vo, accountList);
				}
			}
		}else{
		for(InfoReceiveVO recVO : list){
			if(IntegerUtil.isEmpty(recVO.getUser_id()))
				getParentAccountList(recVO, accountList, parent);//根据学生id找家长用户id
			else
				accountList.add(recVO.getUser_id().toString());
		}
		filterRepeatRemind(accountList);//过滤重复的提醒（多身份）
		xgPushListToApp(vo, accountList);
		gtPushListToApp(vo, accountList);
		}
	}
	
	//根据学生id 获取家长user_id 集合
	private void getParentAccountList(
			InfoReceiveVO recVO, List<String> accountList, ParentVO parent){
		if(IntegerUtil.isEmpty(recVO.getStudent_id())) return;
		parent.setSchool_id(recVO.getSchool_id());
		parent.setClass_id(recVO.getClass_id());
		parent.setGrade_id(recVO.getGrade_id());
		parent.setStudent_id(recVO.getStudent_id());
		List<ParentVO> parUserList = userService.getParUserList(parent);
		if(ListUtil.isEmpty(parUserList)) return;
		for(ParentVO parVO : parUserList)
		{
			if(IntegerUtil.isEmpty(parVO.getUser_id())) continue;
			accountList.add(parVO.getUser_id().toString());
		}
	}
	
	//过滤重复的动态接收者(用户id和用户类型都相同的过滤)
	private void filterRepeatReceive(List<InfoReceiveVO> list){
		 for (int i = 0; i < list.size() - 1; i++) {  
	           for (int j = list.size() - 1; j > i; j--) {  
	               if(list.get(j).getUser_id().equals(list.get(i).getUser_id()) &&
	            		 list.get(j).getUser_type().equals(list.get(i).getUser_type())){  
	                list.remove(j);  
	               }  
	           }  
	     }  
	}
	//过滤重复的动态提醒
	private void filterRepeatRemind(List<String> list){
		 for (int i = 0; i < list.size() - 1; i++) {  
	           for (int j = list.size() - 1; j > i; j--) {  
	               if(list.get(j).equals(list.get(i))){  
	                list.remove(j);  
	               }  
	           }  
	     }  
	}
	
	
	
	/**
	 * 通过腾讯信鸽，推送消息到APP
	 * @param vo 消息推送对象
	 */
	public void xgPushToApp(InfoReceiveVO vo) {
//		//推送给老师端IOS和Android
//		if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type()))
//		{
//			XingePushIOS ptt1 = new XingePushIOS(xgAppTeacherIOS,vo);
//			Thread thread1 = new Thread(ptt1);
//			thread1.start();
//			XingePush ptt2 = new XingePush(xgAppTeacher,vo);
//			Thread thread2 = new Thread(ptt2);
//			thread2.start();
//		} else if (DictConstants.USERTYPE_PARENT.equals(vo.getUser_type()))
//		{
//			//推送给家长端IOS和Android
//			XingePushIOS ptt1 = new XingePushIOS(xgAppParentIOS,vo);
//			Thread thread1 = new Thread(ptt1);
//			thread1.start();
//			XingePush ptt2 = new XingePush(xgAppParent,vo);
//			Thread thread2 = new Thread(ptt2);
//			thread2.start();
//		}
	}

	/**
	 * 通过腾讯信鸽，推送消息到APP
	 * @param vo 消息推送内容对象
	 * @param accountList 接收对象列表
	 */
	public void xgPushListToApp(InfoVO vo,List<String> accountList) {
//		if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type()))
//		{
//			XingePushIOS ptt1 = new XingePushIOS(xgAppTeacherIOS,vo,accountList);
//			Thread thread1 = new Thread(ptt1);
//			thread1.start();
//			XingePush ptt2 = new XingePush(xgAppTeacher,vo,accountList);
//			Thread thread2 = new Thread(ptt2);
//			thread2.start();
//		} else if (DictConstants.USERTYPE_PARENT.equals(vo.getUser_type()))
//		{
//			XingePushIOS ptt1 = new XingePushIOS(xgAppParentIOS,vo,accountList);
//			Thread thread1 = new Thread(ptt1);
//			thread1.start();
//			XingePush ptt2 = new XingePush(xgAppParent,vo,accountList);
//			Thread thread2 = new Thread(ptt2);
//			thread2.start();
//		}
	}

	/**
	 * 通过个推，推送消息到APP
	 * @param vo 消息推送内容对象
	 * @param accountList 接收对象列表
	 */
	public void gtPushListToApp(InfoVO vo, List<String> accountList) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("info_title",vo.getInfo_title());
		map.put("module_code",DictConstants.MODULE_CODE_NOTICE);
		map.put("module_pkid",vo.getInfo_id()+"");
		map.put("info_content",vo.getInfo_content());
		getuiService.pushMessageByList(map,accountList);
	}
}