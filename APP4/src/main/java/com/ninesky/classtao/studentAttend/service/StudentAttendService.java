package com.ninesky.classtao.studentAttend.service;

import com.ninesky.classtao.studentAttend.vo.StudentAttendVO;

import java.util.List;

public interface StudentAttendService {

    public StudentAttendVO addStudentAttend(StudentAttendVO vo);//学生打卡

    public List<StudentAttendVO> getAttendListByStuID(StudentAttendVO vo);//获取学生打卡记录

    public StudentAttendVO getAttendByID(Integer attend_id);//获取学生打卡记录详情

    public StudentAttendVO getAttendList(StudentAttendVO vo);//统计
}
