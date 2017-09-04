package com.ninesky.classtao.studentLeave.service;

import com.ninesky.classtao.studentLeave.vo.StudentLeaveVO;

import java.util.List;

public interface StudentLeaveService {

    public void addStudentLeave(StudentLeaveVO vo);//学生请假

    public List<StudentLeaveVO> getStudentLeaveList(StudentLeaveVO vo);//学生请假记录

    public StudentLeaveVO getStudentLeave(Integer leave_id);//学生请假详情

    public void  cancelStudentLeave(StudentLeaveVO vo);//撤销学生请假

    public void deleteFileById(Integer id);//删除图片

    public List<StudentLeaveVO> getUnTreatedList(StudentLeaveVO vo);//教师待处理列表

    public List<StudentLeaveVO> getTreatedList(StudentLeaveVO vo);//教师已处理列表

    public void passStudentLeave(StudentLeaveVO vo);//同意请假

    public void transferStudentLeave(StudentLeaveVO vo);//转交请假申请

    public void refuseStudentLeave(StudentLeaveVO vo);//拒绝请假申请

    public List<StudentLeaveVO> getStudentLeaveCount(StudentLeaveVO vo);//学生请假统计

    public List<StudentLeaveVO> getStudentLeaveCountDetail(StudentLeaveVO vo);//学生请假统计详情

    public List<StudentLeaveVO> getPassLeaveByStuID (StudentLeaveVO vo);//判断学生是否请假

    public void cancelTransfer(StudentLeaveVO vo);//撤销转交申请
}
