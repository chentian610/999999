package com.ninesky.classtao.leave.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.leave.vo.LeaveChangeVO;
import com.ninesky.classtao.leave.vo.LeaveCountVO;
import com.ninesky.classtao.leave.vo.LeaveLogVO;
import com.ninesky.classtao.leave.vo.LeaveVO;
import com.ninesky.common.vo.ReceiveVO;

public interface LeaveService {
	
	public void addLeave(LeaveVO vo);//请假
	
	public List<LeaveVO> getLeaveList(Map<String, String> paramMap);//查询学生请假记录
	
	public List<LeaveLogVO> getLeaveFlow(Integer leave_id);
	
	public LeaveVO getLeaveByID(Map<String, String> paramMap);
	
	public void doChangeCourse(LeaveChangeVO vo);//处理调课申请
	
	public void sendLeave(Map<String, String> paramMap);//发送请假申请
	
	public void changeApprover(Map<String, String> paramMap);//修改审批人员
	
	public void authLeave(Map<String, String> paramMap);//驳回请假申请
	
	public void passLeave(Map<String, String> paramMap);//通过请假申请
	
	public void deleteLeave(Integer leave_id);//删除请假申请
	
	public void cancelLeave(Map<String, String> paramMap);//撤销请假申请
	
	public List<LeaveVO> getLeaveApplyList(Map<String, String> paramMap);//获取接收到的请假申请
	
	public void updateLeave(Map<String, String> paramMap);//修改请假申请
	
	public Map<String, String> getUntreatedCount(Map<String, String> paramMap);//获取未处理的请假申请条数

	public List<LeaveVO> getLeaveListCC2me(Map<String, String> paramMap);//获取抄送列表

	public LeaveCountVO getLeaveSumAndTotal(Map<String, String> paramMap);//获取请假教师占比

	public List<LeaveCountVO> getLeaveListOfSomeDay(LeaveVO vo);//获取某一时间段,教师请假汇总列表

	public List<LeaveVO> getSomeTeaLeaveListOfSomeDay(LeaveVO vo);//获取某一时间段，某个教师请假列表
}
