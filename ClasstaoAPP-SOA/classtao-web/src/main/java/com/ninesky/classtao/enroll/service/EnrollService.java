package com.ninesky.classtao.enroll.service;

import com.ninesky.classtao.enroll.vo.StudentEnrollVO;
import com.ninesky.classtao.enroll.vo.StudentRecruitVO;

import java.util.List;

public interface EnrollService {
    public List<StudentRecruitVO> getRecruitList(StudentRecruitVO vo);//获取学校招生简章

    public StudentRecruitVO getRecruitByID(Integer recruit_id);//获取学校招生简章详情

    public void enroll(StudentEnrollVO vo);//新生报名

    public StudentRecruitVO getEnrollStatus(StudentEnrollVO vo);//获取报名情况

    public List<StudentEnrollVO> getStudentList(StudentEnrollVO vo);//获取报名学生

    public List<StudentRecruitVO> getAllRecruitList(StudentRecruitVO vo);//获取学校招生简章

    public void addRecruit(StudentRecruitVO vo);//添加学校招生简章

    public void updateRecruit(StudentRecruitVO vo);//修改学校招生简章

    public void admission(StudentEnrollVO vo);//录取

    public StudentEnrollVO getStudentByID(StudentEnrollVO vo);//获取报名学生详情

    public void completeEnroll(StudentRecruitVO vo);//完成招生录取
}
