package com.ninesky.classtao.score.service;

import java.util.List;

import com.ninesky.classtao.score.vo.AttendCodeVO;

public interface AttendService {
	
	//获取学校考勤项目列表
	public List<AttendCodeVO> getAttendCodeList(String school_id);
	
	//删除学校考勤项目
	public void deleteAttendCode(String attend_code);
}
