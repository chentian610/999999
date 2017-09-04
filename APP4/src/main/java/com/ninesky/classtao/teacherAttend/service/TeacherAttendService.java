package com.ninesky.classtao.teacherAttend.service;

import com.ninesky.classtao.teacherAttend.vo.TeacherAttendVO;

import java.util.List;

public interface TeacherAttendService {

	public TeacherAttendVO addTeacherAttend(TeacherAttendVO vo);//教师考勤打卡

	public List<TeacherAttendVO> getAttendByUserID();//获取教师当天打卡记录

	public List<TeacherAttendVO> getAttendListByUserID(String search_time);//获取教师考勤记录列表（月）

	public List<TeacherAttendVO> getTeacherAttendCount();//统计

	public List<TeacherAttendVO> getTeacherAttendCountDetail(String attend_status);//统计详情

	public TeacherAttendVO updateTeacherAttend(TeacherAttendVO vo);//教师考勤打卡更新
}
