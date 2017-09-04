package com.ninesky.classtao.leave.service.impl;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.leave.service.LeaveService;
import com.ninesky.classtao.leave.vo.*;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("LeaveServiceImpl")
public class LeaveServiceImpl implements LeaveService{

	@Autowired
	private GeneralDAO dao;

	@Autowired
	private RedisService redisService;

	@Autowired
	private DynamicService dynamicService;

	@Autowired
	private GetuiService getuiService;
	/**
	 * 添加请假申请
	 * @param vo 添加信息
	 */
	public void addLeave(LeaveVO vo) {
		if (IntegerUtil.isEmpty(vo.getLeave_hours())&&IntegerUtil.isEmpty(vo.getLeave_days()))
			throw new BusinessException(MsgService.getMsg("DYNAMIC_LEAVE_DATE_NULL"));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setLeave_status(DictConstants.LEAVE_STATUS_NEW);
		vo.setLeave_id(dao.insertObjectReturnID("leaveMap.insertLeave", vo));
		//添加请假申请附件
		addLeaveFileList(vo);
        //添加抄送列表
        addLeaveCC(vo);
		//添加调课老师记录
		if (DictConstants.TRUE ==vo.getIs_change_course()) addLeaveChange(vo.getChange_teacher_list(),vo.getLeave_id());
		//添加日志
		addLeaveLog(vo.getLeave_id(),DictConstants.LEAVE_STATUS_NEW,DictConstants.LEAVE_STATUS_NEW,"新建请假申请");
	}

	/**
	 * 添加日志
	 * @param leave_id 请假申请ID
	 * @param pre_status 请假申请之前的状态
	 * @param current_status 请假申请现在的状态
	 * @param content 留言
	 */
	private void addLeaveLog(Integer leave_id,String pre_status,String current_status,String content){
		LeaveLogVO log = new LeaveLogVO();
		log.setLeave_id(leave_id);
		log.setPre_status(pre_status);
		log.setCurrent_status(current_status);
		log.setContent(content);
		log.setCreate_by(ActionUtil.getUserID());
		log.setCreate_date(ActionUtil.getSysTime());
		log.setId(dao.insertObjectReturnID("leaveLogMap.insertLeaveLog", log));
	}
	//获取请假申请
	@Override
	public List<LeaveVO> getLeaveList(Map<String, String> paramMap) {
		if (StringUtil.isNotEmpty(paramMap.get("leave_id")) && paramMap.get("leave_id").indexOf("0340") > 0) paramMap.put("leave_id",paramMap.get("leave_id").substring(0,paramMap.get("leave_id").indexOf("0340")));
		List<LeaveVO> leaveList = dao.queryForList("leaveMap.getLeaveList", paramMap);
		for (LeaveVO leaveVO : leaveList) {
			setLeaveVO(leaveVO);
		}
		return leaveList;
	}

	@Override
	public List<LeaveVO> getLeaveApplyList(Map<String, String> paramMap) {
        if (StringUtil.isNotEmpty(paramMap.get("leave_id")) && paramMap.get("leave_id").indexOf("0340") > 0) paramMap.put("leave_id",paramMap.get("leave_id").substring(0,paramMap.get("leave_id").indexOf("0340")));
		List<LeaveVO> leaveList = dao.queryForList("leaveMap.getLeaveApplyList", paramMap);
		for (LeaveVO leaveVO : leaveList) {
				setLeaveVO(leaveVO);
		}
		return leaveList;
	}
	//获取请假申请
	@Override
	public LeaveVO getLeaveByID(Map<String, String> paramMap) {
		if (StringUtil.isNotEmpty(paramMap.get("leave_id")) && paramMap.get("leave_id").indexOf("0340") > 0) paramMap.put("leave_id",paramMap.get("leave_id").substring(0,paramMap.get("leave_id").indexOf("0340")));
		LeaveVO leaveVO = dao.queryObject("leaveMap.getLeaveByID", paramMap);
		if (leaveVO == null) throw new BusinessException(MsgService.getMsg("DYNAMIC_LEAVE_ERROR"));
		setLeaveVO(leaveVO);
		return leaveVO;
	}
	//处理调课申请
	@Override
	public void doChangeCourse(LeaveChangeVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		if (IntegerUtil.getValue(dao.queryObject("leaveChangeMap.getLeaveChange",vo)) == 0) {
			dao.updateObject("leaveChangeMap.updateLeaveChange", vo);
			addLeaveLog(vo.getLeave_id(), DictConstants.LEAVE_STATUS_NEW, DictConstants.LEAVE_STATUS_NEW, vo.getIs_agree() == 1 ? "同意调课" : "不同意调课");
		} else throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
	}
	// 发送请假申请
	@Override
	public void sendLeave(Map<String, String> paramMap) {
		setMapByLeaveStatus(paramMap);
		if (dao.updateObject("leaveMap.updateLeaveByID",paramMap)==0) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		addLeaveLog(IntegerUtil.getValue(paramMap.get("leave_id")),DictConstants.LEAVE_STATUS_NEW,paramMap.get("leave_status"),"发送请假申请");
	}
	//更换审批人
	@Override
	public void changeApprover(Map<String, String> paramMap) {
		if (dao.updateObject("leaveMap.updateApproverByID", paramMap)==0) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		addLeaveLog(IntegerUtil.getValue(paramMap.get("leave_id")),DictConstants.LEAVE_STATUS_NEW,paramMap.get("leave_status"),"更改审批人");
	}
	//审核请假申请
	@Override
	public void authLeave(Map<String, String> paramMap) {
		setMapByLeaveStatus(paramMap);
		if (dao.updateObject("leaveMap.updateLeaveByID", paramMap)==0) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		String pre_status = DictConstants.LEAVE_STATUS_MASTER.equals(paramMap.get("leave_status"))?DictConstants.LEAVE_STATUS_APPEOVER:DictConstants.LEAVE_STATUS_NEW;
		addLeaveLog(IntegerUtil.getValue(paramMap.get("leave_id")),pre_status,paramMap.get("leave_status"),"审核请假申请:不通过");
	}
	//通过请假申请
	@Override
	public void passLeave(Map<String, String> paramMap) {
		setMapByLeaveStatus(paramMap);
		if (dao.updateObject("leaveMap.updateLeaveByID", paramMap)==0) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		if (DictConstants.LEAVE_STATUS_PASS.equals(paramMap.get("leave_status"))) sendOutDynamicAndGetui(paramMap);
		String pre_status = DictConstants.LEAVE_STATUS_MASTER.equals(paramMap.get("leave_status"))?DictConstants.LEAVE_STATUS_APPEOVER:DictConstants.LEAVE_STATUS_NEW;
		addLeaveLog(IntegerUtil.getValue(paramMap.get("leave_id")),pre_status,paramMap.get("leave_status"),"审核请假申请:"+(DictConstants.SCH_STATUS_PASS.equals(paramMap.get("leave_status"))?"同意":"驳回")+"");
	}

	//获取请假申请
	@Override
	public List<LeaveLogVO> getLeaveFlow(Integer leave_id) {
		List<LeaveLogVO> leaveLogList = dao.queryForList("leaveLogMap.getLeaveLogList", leave_id);
		for (LeaveLogVO leaveLogVO : leaveLogList) {
			leaveLogVO.setOper_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveLogVO.getOper_id(), 0));
		}
		return leaveLogList;
	}
	//删除请假申请
	@Override
	public void deleteLeave(Integer leave_id) {
        LeaveVO vo = new LeaveVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setLeave_id(leave_id);
        removeLeaveRelevantDynamic(vo);
		dao.deleteObject("leaveMap.removeLeave",vo);
		dao.deleteObject("leaveChangeMap.removeLeaveChange",vo);
		dao.deleteObject("leaveLogMap.deleteLeaveLog",leave_id);
		dao.deleteObject("leaveFileMap.removeLeaveFileListByID",vo);
        dao.deleteObject("leaveCCMap.deleteLeaveCCByID",vo);//删除之前的抄送教师列表
	}

	//撤销请假申请
	@Override
	public void cancelLeave(Map<String, String> paramMap) {
		if (dao.updateObject("leaveMap.updateLeaveStatusByID",paramMap)==0) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		addLeaveLog(IntegerUtil.getValue(paramMap.get("leave_id")),DictConstants.LEAVE_STATUS_NEW,DictConstants.LEAVE_STATUS_CANCEL,"撤回请假申请");
	}
	//修改请假申请
	@Override
	public void updateLeave(Map<String, String> paramMap) {
		dao.updateObject("leaveMap.updateLeave",paramMap);
		//添加请假申请附件
		if (StringUtil.isNotEmpty(paramMap.get("file_list"))) {//判断是否有附件
			LeaveVO vo = new LeaveVO();
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setLeave_id(IntegerUtil.getValue(paramMap.get("leave_id")));
			vo.setFile_list(paramMap.get("file_list"));
			addLeaveFileList(vo);
		}
		//根据is_change_course判断是否校园调课
		if (Constants.SORT_UP.equals(paramMap.get("is_change_course"))) addLeaveChange(paramMap.get("change_teacher_list"),IntegerUtil.getValue(ActionUtil.getParameter("leave_id")));
		else removeLeaveChangeDynamic(IntegerUtil.getValue(ActionUtil.getParameter("leave_id")));
	}

    //获取未处理数
	public Map<String, String> getUntreatedCount(Map<String,String> paramMap){
		List<LeaveVO> leaveList = dao.queryForList("leaveMap.getLeaveApplyList", paramMap);
		paramMap.put("count",leaveList.size()+"");
		return paramMap;
	}

	//获取抄送信息
	public List<LeaveVO> getLeaveListCC2me(Map<String,String> paramMap){
		if (StringUtil.isNotEmpty(paramMap.get("leave_id")) && paramMap.get("leave_id").indexOf("0340") > 0) paramMap.put("leave_id",paramMap.get("leave_id").substring(0,paramMap.get("leave_id").indexOf("0340")));
		paramMap.put("leave_status",DictConstants.LEAVE_STATUS_PASS);
		List<LeaveVO> leaveList = dao.queryForList("leaveMap.getLeaveCCList", paramMap);
		for (LeaveVO leaveVO : leaveList) {
			setLeaveVO(leaveVO);
		}
		return leaveList;
	}

	//添加学校请假情况
	public LeaveCountVO getLeaveSumAndTotal(Map<String,String> paramMap) {
		if (StringUtil.isEmpty(paramMap.get("leave_date")))
			throw new BusinessException(MsgService.getMsg("LEAVE_DATE_NULL"));
		LeaveCountVO vo = new LeaveCountVO();
		paramMap.put("leave_status",DictConstants.LEAVE_STATUS_PASS);
		vo.setTotal_count(IntegerUtil.getValue(dao.queryObject("teacherMap.getTeacherTotal",ActionUtil.getSchoolID())));
		vo.setLeave_count(IntegerUtil.getValue(dao.queryObject("leaveMap.getLeaveCountByDate",paramMap)));
		return vo;
	}

	//获取某个教师请假申请统计总数列表
	public List<LeaveCountVO> getLeaveListOfSomeDay(LeaveVO vo){
		List<LeaveCountVO> leaveCountList = dao.queryForList("leaveMap.getLeaveListOfSomeDay",vo);
		for (LeaveCountVO leaveCountVO:leaveCountList) {
			leaveCountVO.setLeave_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveCountVO.getUser_id(),0));
			leaveCountVO.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveCountVO.getUser_id()));
			leaveCountVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveCountVO.getUser_id(),0));
		}
		return leaveCountList;
	}

	//获取某个教师每天请假申请的详细信息
	public List<LeaveVO> getSomeTeaLeaveListOfSomeDay(LeaveVO vo){
		List<LeaveVO> leaveList = dao.queryForList("leaveMap.getSomeTeaLeaveListOfSomeDay",vo);
		for (LeaveVO leaveVO:leaveList) {
			leaveVO.setLeave_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveVO.getUser_id(),0));
			leaveVO.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveVO.getUser_id()));
			leaveVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveVO.getUser_id(),0));
			leaveVO.setDict_value(redisService.getDictValue(leaveVO.getLeave_type()));
			leaveVO.setStart_date(leaveVO.getStart_date().substring(0,leaveVO.getStart_date().indexOf(".0")));
			leaveVO.setEnd_date(leaveVO.getEnd_date().substring(0,leaveVO.getEnd_date().indexOf(".0")));
		}
		return leaveList;
	}

	private String getCCTeacherList(LeaveVO vo){
		List<LeaveCCVO> list = dao.queryForList("leaveCCMap.getLeaveCC",vo);
		if (ListUtil.isEmpty(list)) return "";
		for (LeaveCCVO ccVO:list) {
			ccVO.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ccVO.getUser_id(),0));
			ccVO.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ccVO.getUser_id()));
			ccVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ccVO.getUser_id(),0));
		}
		return BeanUtil.ListTojson(list)+"";
	}

	//获取附件
	private String getLeaveFileList(LeaveVO vo){
		List<LeaveFileVO> fileList = dao.queryForList("leaveFileMap.getLeaveFileListByID",vo);
		if (ListUtil.isEmpty(fileList)) return "";
		return BeanUtil.ListTojson(fileList)+"";
	}

	//获取调课老师
	private String getChangeTeacher(LeaveVO leaveVO){
		List<LeaveChangeVO> leaveChange = dao.queryForList("leaveChangeMap.getLeaveChangeList", leaveVO);
		if (ListUtil.isEmpty(leaveChange)) return "";
		for (LeaveChangeVO leaveChangeVO : leaveChange) {
			leaveChangeVO.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id()));
			leaveChangeVO.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id(), 0));
			leaveChangeVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id(), 0));
		}
		return BeanUtil.ListTojson(leaveChange)+"";
	}

	//添加抄送教师列表
	private void addLeaveCC(LeaveVO vo) {
		if (StringUtil.isEmpty(vo.getCc_teacher_list())) return;
		dao.deleteObject("leaveCCMap.deleteLeaveCCByID",vo);//删除之前的抄送教师列表
		List<LeaveCCVO> ccList = BeanUtil.jsonToList(vo.getCc_teacher_list(),LeaveCCVO.class);
		for (LeaveCCVO ccVO: ccList) {
			ccVO.setSchool_id(ActionUtil.getSchoolID());
			ccVO.setLeave_id(vo.getLeave_id());
			ccVO.setCreate_by(ActionUtil.getUserID());
			ccVO.setCreate_date(ActionUtil.getSysTime());
		}
		dao.insertObject("leaveCCMap.insertLeaveCCBatch",ccList);
	}

	//发送提示动态给抄送教师
	private void sendOutDynamicAndGetui(Map<String, String> paramMap){
		HashMap<String,String> dataMap = new HashMap<String,String>();
		LeaveVO vo = getLeaveByID(paramMap);
		List<ReceiveVO> receiveList = dao.queryForList("leaveCCMap.getReceive",vo);
		dataMap.put("module_code",DictConstants.MODULE_CODE_LEAVE);
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_CC);
		dataMap.put("leave_id",vo.getLeave_id()+"");
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_CC_LEAVE",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getUser_id(),0)));
		dataMap.put("create_id", vo.getUser_id()+"");//创建人ID
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", ActionUtil.getUserType());
		dataMap.put("info_content",StringUtil.subString(vo.getContent(),15));
		dataMap.put("info_leave_status", DictConstants.LEAVE_STATUS_NEW);
		//start 创建请假人的动态
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);//拉起详情
		dataMap.put("info_url", "detail.html");
		dynamicService.insertDynamic(dataMap,receiveList);//
		getuiService.pushMessage(dataMap,receiveList);//向抄送老师发推送
	}

	//根据change_id,查询对应的用户,删除相应的用户动态
	private void removeSingleLeaveDynamic(HashMap<String,String> dataMap,Integer change_id) {
		LeaveChangeVO vo = new LeaveChangeVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setChange_id(change_id);
		ReceiveVO receiveVO = dao.queryObject("leaveChangeMap.getReceive",vo);
		dynamicService.removeSingleDynamic(dataMap,receiveVO);
	}

	//删除调课老师集合的动态和列表
	private List<ReceiveVO> removeLeaveDynamic(HashMap<String,String> dataMap,List<ReceiveVO> list) {
		if (ListUtil.isEmpty(list)) return null;
		dao.deleteObject("leaveChangeMap.deleteLeaveChange", IntegerUtil.getValue(dataMap.get("leave_id")));
		dynamicService.removeDynamicList(dataMap, list);
		return null;
	}

	//设置基础键值的Map
	private HashMap<String,String> setHashMap(Integer leave_id){
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("module_code",DictConstants.MODULE_CODE_LEAVE);
		//生成module_pkid
		dataMap.put("module_pkid",leave_id+DictConstants.LEAVE_STATUS_CLASS);
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);//拉起详情
		dataMap.put("info_leave_status", DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("leave_id",leave_id+"");
		return dataMap;
	}

	//设置调课老师列表
	private List<ReceiveVO> setReceiveList(Integer leave_id){
		LeaveChangeVO vo = new LeaveChangeVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setLeave_id(leave_id);
		return dao.queryForList("leaveChangeMap.getReceive",vo);
	}

	//添加附件
	private void addLeaveFileList(LeaveVO vo){
		if (StringUtil.isEmpty(vo.getFile_list())) return;
		dao.deleteObject("leaveFileMap.deleteLeaveFileListByID",vo);
		List<LeaveFileVO> fileList = BeanUtil.jsonToList(vo.getFile_list(),LeaveFileVO.class);
		for (LeaveFileVO fileVO: fileList) {
			fileVO.setSchool_id(ActionUtil.getSchoolID());
			fileVO.setLeave_id(vo.getLeave_id());
			fileVO.setCreate_by(ActionUtil.getUserID());
			fileVO.setCreate_date(ActionUtil.getSysTime());
		}
		dao.insertObject("leaveFileMap.insertLeaveFileBatch",fileList);
	}

	//删除请假申请相关人员的动态
	private void removeLeaveRelevantDynamic(LeaveVO vo){
		removeLeavePeopleDynamic(vo);//删除行政教师和请假人的动态
		List<ReceiveVO> receiveList = setReceiveList(vo.getLeave_id());//获取修改前的调课老师列
		HashMap<String,String> dataMap = setHashMap(vo.getLeave_id());//设置基础的键值参数
		dynamicService.removeDynamicList(dataMap, receiveList);
	}

	//删除之前的抄送教师动态
	private void removeLeavePeopleDynamic(LeaveVO vo){
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("module_code",DictConstants.MODULE_CODE_LEAVE);
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);//拉起模块
		dataMap.put("school_id",ActionUtil.getSchoolID()+"");
		dataMap.put("leave_id", vo.getLeave_id()+"");
		LeaveVO leaveVO = dao.queryObject("leaveMap.getLeaveByID", dataMap);
		dynamicService.removeSingleDynamic(dataMap,new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveVO.getUser_id()));//删除相应用户动态
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_APPEOVER);
		dynamicService.removeSingleDynamic(dataMap,new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,leaveVO.getApprover_id()));//删除相应用户动态
	}

	//添加调课老师记录
	private void addLeaveChange(String change_teacher_list,Integer leave_id){
		if (StringUtil.isEmpty(change_teacher_list)) throw new BusinessException(MsgService.getMsg("LEAVE_CHANGE_LIST_NULL"));
		List<ReceiveVO> receiveList = setReceiveList(leave_id);//获取修改前的调课老师列表
		HashMap<String,String> dataMap = setHashMap(leave_id);//设置基础的键值参数
		List<LeaveChangeVO> leaveChange = BeanUtil.jsonToList(change_teacher_list, LeaveChangeVO.class);
		for (LeaveChangeVO leaveChangeVO : leaveChange) {
			leaveChangeVO.setIs_agree(IntegerUtil.isEmpty(leaveChangeVO.getIs_agree())?Constants.FALSE_FLAG:leaveChangeVO.getIs_agree());
			if (IntegerUtil.isNotEmpty(leaveChangeVO.getChange_id())) {
				removeSingleLeaveDynamic(dataMap,leaveChangeVO.getChange_id());//根据change_id对应的用户删除相应的用户动态
				leaveChangeVO.setUpdate_by(ActionUtil.getUserID());
				leaveChangeVO.setUpdate_date(ActionUtil.getSysTime());
				dao.updateObject("leaveChangeMap.updateLeaveChangeByID", leaveChangeVO);
			} else {
				receiveList = removeLeaveDynamic(dataMap,receiveList);//根据集合中的用户删除相应的用户动态和列表
				leaveChangeVO.setSchool_id(ActionUtil.getSchoolID());
				leaveChangeVO.setLeave_id(leave_id);
				leaveChangeVO.setCreate_by(ActionUtil.getUserID());
				leaveChangeVO.setCreate_date(ActionUtil.getSysTime());
				leaveChangeVO.setChange_id(dao.insertObjectReturnID("leaveChangeMap.insertLeaveChange", leaveChangeVO));
			}
		}
	}

	//设置请假申请实体参数
	private void setLeaveVO(LeaveVO leaveVO){
		if (IntegerUtil.isNotEmpty(leaveVO.getApprover_id()))//获取审核人的姓名
			leaveVO.setApprover_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveVO.getApprover_id(), 0));
		if (IntegerUtil.isNotEmpty(leaveVO.getMaster_id()))//获取校长的姓名
			leaveVO.setMaster_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveVO.getMaster_id(), 0));
		leaveVO.setStart_date(leaveVO.getStart_date().substring(0, leaveVO.getStart_date().indexOf(":")+3));
		leaveVO.setEnd_date(leaveVO.getEnd_date().substring(0, leaveVO.getStart_date().indexOf(":")+3));
		leaveVO.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveVO.getUser_id(), 0));
		leaveVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveVO.getUser_id(), 0));
		leaveVO.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveVO.getUser_id()));
		leaveVO.setChange_teacher_list(getChangeTeacher(leaveVO));//获取调课老师的列表
		leaveVO.setFile_list(getLeaveFileList(leaveVO));//获取请假申请附件列表
		leaveVO.setCc_teacher_list(getCCTeacherList(leaveVO));//获取抄送教师列表
		leaveVO.setApprover_date(StringUtil.isNotEmpty(leaveVO.getApprover_date())?leaveVO.getApprover_date().substring(0,leaveVO.getApprover_date().indexOf(".0")):"");
		leaveVO.setMaster_date(StringUtil.isNotEmpty(leaveVO.getMaster_date())?leaveVO.getMaster_date().substring(0,leaveVO.getMaster_date().indexOf(".0")):"");
	}

	//根据leave_status设置Map参数
	private void setMapByLeaveStatus(Map<String, String> paramMap){
		switch (paramMap.get("leave_status")) {
			case DictConstants.LEAVE_STATUS_APPEOVER:
				paramMap.put("approver_id", paramMap.get("receive_id"));
				break;
			case DictConstants.LEAVE_STATUS_MASTER:
				paramMap.put("master_id", paramMap.get("receive_id"));
				paramMap.put("approver_content", paramMap.get("content"));
				paramMap.put("approver_date", DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd HH:mm:ss")+"");
				break;
			default:
				LeaveVO vo = dao.queryObject("leaveMap.getLeaveByID", paramMap);
				if (DictConstants.LEAVE_STATUS_MASTER.equals(vo.getLeave_status())) {
					paramMap.put("master_content", paramMap.get("content"));
					paramMap.put("master_date",DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd HH:mm:ss")+"");
				} else {
					paramMap.put("approver_content", paramMap.get("content"));
					paramMap.put("approver_date", DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd HH:mm:ss")+"");
				}
				break;
		}
	}

	private void removeLeaveChangeDynamic(Integer leave_id){
		List<ReceiveVO> receiveList = setReceiveList(leave_id);//获取修改前的调课老师列表
		if (ListUtil.isEmpty(receiveList)) return;
		HashMap<String,String> dataMap = setHashMap(leave_id);//设置基础的键值参数
		removeLeaveDynamic(dataMap,receiveList);//根据集合中的用户删除相应的用户动态和列表
	}
}