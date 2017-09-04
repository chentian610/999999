package com.ninesky.classtao.leave.controller;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.leave.service.LeaveService;
import com.ninesky.classtao.leave.vo.LeaveChangeVO;
import com.ninesky.classtao.leave.vo.LeaveVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("leaveAction")
public class LeaveController extends BaseController{

	@Autowired
	private LeaveService leaveService;
	@Autowired
	private DynamicService dynamicService;
	@Autowired
	private GetuiService getuiService;
	@Autowired
	private RedisService redisService;

	/**
	 * 请假
	 * @return
	 */
	@RequestMapping("/addLeave")
	@ResultField(excludes={"app_sql","order_sql","start","start_time","end_time","limit","direction","id"})
	public Object addLeave(LeaveVO vo){
		leaveService.addLeave(vo);
		HashMap<String,String> dataMap = setDynamicBasicParameter(vo);
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("info_title","你添加了一条请假申请！");
		dataMap.put("info_content",StringUtil.subString(vo.getContent(),15));
		//start 创建请假人的动态
		List<ReceiveVO> receive = new ArrayList<ReceiveVO>();
		receive.add(new ReceiveVO(ActionUtil.getSchoolID (), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
		dynamicService.insertDynamic(dataMap,receive);
		//end
		//start 创建调课老师的动态
		if (StringUtil.isEmpty(vo.getChange_teacher_list())) return ResponseUtils.sendSuccess(vo);//判断是否需要调课
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_LEAVE_ADD", redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0)));
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_CLASS);
		//创建调课老师的key
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		List<LeaveChangeVO> leaveChangeList = BeanUtil.jsonToList(vo.getChange_teacher_list(), LeaveChangeVO.class);
		for (LeaveChangeVO leaveChangeVO : leaveChangeList) {
			if (IntegerUtil.isEmpty(leaveChangeVO.getUser_id())) continue;
			dynamicService.insertSingleDynamic(dataMap,new ReceiveVO(ActionUtil.getSchoolID (), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id()));
			receiveList.add(new ReceiveVO(ActionUtil.getSchoolID (), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id()));
		}
		//end
		//向调课人员发送推送
		getuiService.pushMessage(dataMap,receiveList);
		return ResponseUtils.sendSuccess(vo);
	}

	/**
	 * 查询请假记录
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveListOfMine")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
	public Object getLeaveListOfMine(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(leaveService.getLeaveList(paramMap));
	}

    /**
     * 查询请假记录
     * @param request
     * @return
     */
    @RequestMapping("/getLeaveList")
    @ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
    public Object getLeaveList(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        return ResponseUtils.sendSuccess(leaveService.getLeaveList(paramMap));
    }

    /**
     * 查询未审批到的请假申请
     * @param request
     * @return
     */
    @RequestMapping("/getLeaveApplyList")
    @ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
    public Object getLeaveApplyList(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        return leaveService.getLeaveApplyList(paramMap);
    }


    /**
	 * 查询未审批到的请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveListOfApprove")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
	public Object getLeaveListOfApprove(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return leaveService.getLeaveApplyList(paramMap);
	}

	/**
	 * 查询已审批到的请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveListHaveApproved")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
	public Object getLeaveListHaveApproved(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return leaveService.getLeaveApplyList(paramMap);
	}


    /**
     * 查询已审批到的请假申请
     * @param request
     * @return
     */
    @RequestMapping("/getLeaveByID")
    @ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
    public Object getLeaveByID(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        return leaveService.getLeaveByID(paramMap);
    }


    /**
	 * 查看请假审批流程
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveFlow")
	public Object getLeaveFlow(HttpServletRequest request){
		Integer leave_id = IntegerUtil.getValue(ActionUtil.getParameter("leave_id"));
		return leaveService.getLeaveFlow(leave_id);
	}

	/**
	 * 处理调课申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/doChangeCourse")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","id"})
	public Object doChangeCourse(LeaveChangeVO vo,HttpServletRequest request){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("school_id", ActionUtil.getSchoolID()+"");
        paramMap.put("leave_id", vo.getLeave_id()+"");
        LeaveVO leave = leaveService.getLeaveByID(paramMap);
		leaveService.doChangeCourse(vo);
		HashMap<String,String> dataMap = setDynamicBasicParameter(leave);
		String content = DictConstants.TRUE == vo.getIs_agree()?leave.getContent():vo.getContent();
		String status = DictConstants.TRUE == vo.getIs_agree()?"同意":"驳回";
		dataMap.put("info_content",StringUtil.subString(content,15));
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+ DictConstants.LEAVE_STATUS_CLASS);
		dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_DO_CHANGE","你",status,redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leave.getUser_id(), 0)));
		//start 更新调课老师
		//当前调课老师的动态key
		String DynamicKey = RedisKeyUtil.getSingleDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_CLASS),ActionUtil.getUserID(), DictConstants.LINK_TYPE_DETAIL);//拉起模块的键
		//更新调课老师的动态
		dynamicService.updateDynamic(DynamicKey,dataMap);
		//end
		//start 更新请假人的动态
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_DO_CHANGE",(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0)+"老师"),status,"你"));
		//当前请假人的动态key
		String CreateKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);//拉起详情页
		//更新请假人的动态
		dynamicService.updateDynamic(CreateKey,dataMap);
		//end
		//向请假人发送推送
		getuiService.pushMessage(dataMap,leave.getUser_id());
		return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
	}

	/**
	 * 发送请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/sendLeave")
	public Object sendLeave(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		leaveService.sendLeave(paramMap);
		LeaveVO vo = leaveService.getLeaveByID(paramMap);
		HashMap<String,String> dataMap = setDynamicBasicParameter(vo);
		dataMap.put("info_content",StringUtil.subString(vo.getContent(),15));
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_LEAVE_SEND","你",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getApprover_id(),0)));
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		//当前申请人的key
		String ReceiveKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);//拉起详情页的键
		//更新申请人的动态
		dynamicService.updateDynamic(ReceiveKey,dataMap);
		//end
		//start 添加审核人动态
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_APPEOVER);
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_LEAVE_SEND",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),"你"));
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		receiveList.add(new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getApprover_id()));
		dynamicService.insertDynamic(dataMap,receiveList);
		//end
		//向接收人发推送
		getuiService.pushMessage(dataMap,IntegerUtil.getValue(paramMap.get("receive_id")));
		return ResponseUtils.sendSuccess(vo);
	}

	/**
	 * 修改审批人员
	 * @param request
	 * @return
	 */
	@RequestMapping("/changeApprover")
	@ResultField(excludes={"app_sql","order_sql","start","start_time","end_time","limit","direction","id"})
	public  Object changeAuther(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
        LeaveVO vo = leaveService.getLeaveByID(paramMap);
		leaveService.changeApprover(paramMap);
		HashMap<String,String> dataMap = setDynamicBasicParameter(vo);
		dataMap.put("info_content", StringUtil.subString(StringUtil.isNotEmpty(paramMap.get("content"))?paramMap.get("content"):vo.getContent(),15));
		dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_AUDIT",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0),"提交",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getUser_id(),0)));
		//生成module_pkid
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_MASTER);
        //删除1对1用户动态
        dynamicService.removeSingleDynamic(dataMap,new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getMaster_id()));
		// start 创建新的审批人动态
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		receiveList.add(new ReceiveVO(ActionUtil.getSchoolID (), DictConstants.USERTYPE_TEACHER, IntegerUtil.getValue(paramMap.get("master_id"))));
		dynamicService.insertDynamic(dataMap,receiveList);
        getuiService.pushMessage(dataMap,IntegerUtil.getValue(paramMap.get("master_id")));
		//当前申请人的key
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_SUBMIT_SCHOOL_LEADER",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0)));
        String ReceiveKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_MASTER), DictConstants.LINK_TYPE_DETAIL);//拉起详情页的键
        dynamicService.updateDynamic(ReceiveKey,dataMap);
		// end
		return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
	}

	/**
	 * 审核请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/authLeave")
	@ResultField(excludes={"app_sql","order_sql","start","start_time","end_time","limit","direction","id"})
	public  Object authLeave(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		LeaveVO vo = leaveService.getLeaveByID(paramMap);
		leaveService.authLeave(paramMap);
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		receiveList.add(new ReceiveVO(ActionUtil.getSchoolID (), DictConstants.USERTYPE_TEACHER, vo.getUser_id()));
		HashMap<String,String> dataMap =  setDynamicBasicParameter(vo);
		dataMap.put("info_content",StringUtil.subString(StringUtil.isNotEmpty(paramMap.get("content"))?paramMap.get("content"):vo.getContent(),15));
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_LEAVE_AUDIT","你","驳回",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getUser_id(),0)));
		//start 更新教务处或者校领导的动态
		String DynamicKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);
		if (DictConstants.LEAVE_STATUS_MASTER.equals(vo.getLeave_status())) {
			dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_MASTER);
			String schoolKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_MASTER), DictConstants.LINK_TYPE_DETAIL);
			dynamicService.updateDynamic(schoolKey, dataMap);
		} else {
			dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_APPEOVER);
			String applicantKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id()+DictConstants.LEAVE_STATUS_APPEOVER), DictConstants.LINK_TYPE_DETAIL);
			dynamicService.updateDynamic(applicantKey, dataMap);
		}
		//end
		// start 更新创建人的动态
		dataMap.put("module_pkid",vo.getLeave_id()+DictConstants.LEAVE_STATUS_NEW);
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_LEAVE_AUDIT",(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0)+"老师"),"驳回","你"));
		dynamicService.updateDynamic(DynamicKey, dataMap);
		//向创建人发送动态
		getuiService.pushMessage(dataMap,receiveList);
		return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
	}

	/**
	 * 通过请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/passLeave")
	@ResultField(excludes={"app_sql","order_sql","start","start_time","end_time","limit","direction","id"})
	public  Object passLeave(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		LeaveVO vo = leaveService.getLeaveByID(paramMap);
		if (vo.getLeave_status().equals(DictConstants.LEAVE_STATUS_NEW) || vo == null) throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
		else {
			leaveService.passLeave(paramMap);
			HashMap<String, String> dataMap = setDynamicBasicParameter(vo);
			dataMap.put("info_content", StringUtil.subString(DictConstants.LEAVE_STATUS_RECALL.equals(paramMap.get("leave_status")) ? paramMap.get("content") : vo.getContent(), 15));
			List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
			receiveList.add(new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, IntegerUtil.getValue(paramMap.get("receive_id"))));
			if (DictConstants.LEAVE_STATUS_MASTER.equals(paramMap.get("leave_status"))) {
				dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_NEW);
				dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_SUBMIT_SCHOOL_LEADER", redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0)));
				getuiService.pushMessage(dataMap, receiveList);
				String teacherKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);
				dynamicService.updateDynamic(teacherKey, dataMap);
				dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER);
				dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_AUDIT", "你", "同意", redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(), 0)));
				String applicantKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER), DictConstants.LINK_TYPE_DETAIL);
				dynamicService.updateDynamic(applicantKey, dataMap);
				dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_MASTER);
				dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_SUBMIT", redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0), redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(), 0)));
				dynamicService.insertDynamic(dataMap, receiveList);
			} else {
				String ExamineStatus = DictConstants.LEAVE_STATUS_RECALL.equals(paramMap.get("leave_status")) ? "驳回" : "同意";
				String duty_name = DictConstants.LEAVE_STATUS_APPEOVER.equals(vo.getLeave_status()) ? "老师" : "校领导";
				dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_NEW);
				dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_AUDIT", (redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0) + duty_name), ExamineStatus, "你"));
				getuiService.pushMessage(dataMap, receiveList);
				String teacherKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);
				dynamicService.updateDynamic(teacherKey, dataMap);
				if (DictConstants.LEAVE_STATUS_APPEOVER.equals(vo.getLeave_status())) {
					dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER);
					dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_AUDIT", "你", ExamineStatus, redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(), 0)));
					String applicantKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER), DictConstants.LINK_TYPE_DETAIL);
					dynamicService.updateDynamic(applicantKey, dataMap);
				} else if (DictConstants.LEAVE_STATUS_MASTER.equals(vo.getLeave_status())) {
					dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_MASTER);
					dataMap.put("info_title", MsgService.getMsg("DYNAMIC_LEAVE_AUDIT", "你", ExamineStatus, redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(), 0)));
					String masterKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_MASTER), DictConstants.LINK_TYPE_DETAIL);
					dynamicService.updateDynamic(masterKey, dataMap);
				}
			}
			return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
		}
	}

	/**
	 * 删除请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteLeave")
	public  Object deleteLeave(HttpServletRequest request){
		Integer leave_id = IntegerUtil.getValue(ActionUtil.getParameter("leave_id"));
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		LeaveVO vo = leaveService.getLeaveByID(paramMap);
		if (DictConstants.LEAVE_STATUS_NEW.equals(vo.getLeave_status()) || DictConstants.LEAVE_STATUS_APPEOVER.equals(vo.getLeave_status())) {
			leaveService.deleteLeave(leave_id);
			return ResponseUtils.sendSuccess("删除成功！");
		} else throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
	}

	/**
	 * 修改请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateLeave")
	public  Object updateLeave(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
        LeaveVO vo = leaveService.getLeaveByID(paramMap);
        if (vo.getLeave_status().equals(DictConstants.LEAVE_STATUS_NEW) || vo.getLeave_status().equals(DictConstants.LEAVE_STATUS_APPEOVER)) {
			leaveService.updateLeave(paramMap);
			vo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0));
			if (StringUtil.isEmpty(vo.getChange_teacher_list())) return ResponseUtils.sendSuccess(vo);
			HashMap<String, String> dataMap = setDynamicBasicParameter(vo);
			//生成module_pkid
			dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_CLASS);
			dataMap.put("info_title", redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0) + "老师向你发起了调课申请");
			dataMap.put("info_content", StringUtil.subString(paramMap.get("content"), 15));
			//更新动态是否已经存在
			List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
			List<LeaveChangeVO> leaveChangeList = BeanUtil.jsonToList(paramMap.get("change_teacher_list"), LeaveChangeVO.class);
			for (LeaveChangeVO leaveChangeVO : leaveChangeList) {
				if (IntegerUtil.isEmpty(leaveChangeVO.getUser_id())) continue;
				dynamicService.insertSingleDynamic(dataMap, new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id()));
				receiveList.add(new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, leaveChangeVO.getUser_id()));
			}
			getuiService.pushMessage(dataMap, receiveList);
			return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
		} else throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
	}

	/**
	 * 撤回请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/cancelLeave")
	public  Object cancelLeave(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		LeaveVO vo = leaveService.getLeaveByID(paramMap);
		if (DictConstants.LEAVE_STATUS_APPEOVER.equals(vo.getLeave_status()) || DictConstants.LEAVE_STATUS_NEW.equals(vo.getLeave_status())) {
			leaveService.cancelLeave(paramMap);
			HashMap<String, String> dataMap = setDynamicBasicParameter(vo);
			//生成module_pkid
			dataMap.put("module_pkid", vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER);
			dataMap.put("info_title", "该请假申请不存在或者已撤销！");
			dataMap.put("info_content", StringUtil.subString(vo.getContent(), 15));
			//更新动态是否已经存在
			List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
			receiveList.add(new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id()));
			String applicantKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_APPEOVER), DictConstants.LINK_TYPE_DETAIL);
			dynamicService.updateDynamic(applicantKey, dataMap);
			dataMap.put("info_title", "你已经撤销了该条请假申请！");
			String teacherKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_LEAVE, IntegerUtil.getValue(vo.getLeave_id() + DictConstants.LEAVE_STATUS_NEW), DictConstants.LINK_TYPE_DETAIL);
			dynamicService.updateDynamic(teacherKey, dataMap);
			getuiService.pushMessage(dataMap, receiveList);
			return ResponseUtils.sendSuccess(leaveService.getLeaveByID(paramMap));
		} else throw new BusinessException(MsgService.getMsg("dataHaveBeenModifyByOthers"));
	}

	/**
	 * 获取未处理的请假申请数量
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUntreatedCount")
	public  Object getUntreatedCount(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return leaveService.getUntreatedCount(paramMap);
	}

	/**
	 * 查询抄送的请假申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveListCC2me")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction"})
	public Object getLeaveListCC2me(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return leaveService.getLeaveListCC2me(paramMap);
	}

	/**
	 * 获取请假申请统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveSumAndTotal")
	@ResultField(includes={"total_count","leave_count"})
	public Object getLeaveSumAndTotal(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return leaveService.getLeaveSumAndTotal(paramMap);
	}

	/**
	 * 获取某一时间段,教师请假汇总列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLeaveListOfSomeDay")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","total_count","leave_count"})
	public Object getLeaveListOfSomeDay(HttpServletRequest request){
		LeaveVO vo = BeanUtil.formatToBean(LeaveVO.class);
		setLeaveStartOfEnd(vo);
		return leaveService.getLeaveListOfSomeDay(vo);
	}

	/**
	 * 获取某一时间段，某个教师请假列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getSomeTeaLeaveListOfSomeDay")
	@ResultField(includes={"leave_days","phone","leave_id","school_id", "end_date","leave_type","leave_hours","user_id","head_url","leave_name","start_date","dict_value","create_date"})
	public Object getSomeTeaLeaveListOfSomeDay(HttpServletRequest request){
		LeaveVO vo = BeanUtil.formatToBean(LeaveVO.class);
		setLeaveStartOfEnd(vo);
		return leaveService.getSomeTeaLeaveListOfSomeDay(vo);
	}

	private HashMap<String,String> setDynamicBasicParameter(LeaveVO vo){
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("leave_id",vo.getLeave_id()+"");
		dataMap.put("module_code",DictConstants.MODULE_CODE_LEAVE);
		//生成module_pkid
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);//拉起模块
		dataMap.put("info_url", "detail.html");
		dataMap.put("create_id", vo.getUser_id()+"");//创建人ID
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", ActionUtil.getUserType());
		dataMap.put("info_leave_status", StringUtil.isNotEmpty(ActionUtil.getParameter("leave_status"))?ActionUtil.getParameter("leave_status"):DictConstants.LEAVE_STATUS_NEW);
		return dataMap;
	}

	private static void setLeaveStartOfEnd(LeaveVO vo){
		Date end = DateUtil.formatDateEnd(DateUtil.formatStringToDate(vo.getEnd_date(),"yyyy-MM-dd"));
		Date start = DateUtil.formatDateStart(DateUtil.formatStringToDate(vo.getStart_date(),"yyyy-MM-dd"));
		vo.setEnd_date(DateUtil.formatDateToString(end,"yyyy-MM-dd HH:mm:ss"));
		vo.setStart_date(DateUtil.formatDateToString(start,"yyyy-MM-dd HH:mm:ss"));
		vo.setLeave_status(DictConstants.LEAVE_STATUS_PASS);
	}
}
