package com.ninesky.classtao.studentRegister.service;

import com.ninesky.classtao.studentRegister.vo.StudentRegisterVO;

import java.util.List;

public interface StudentRegisterService {

    public StudentRegisterVO addRegister(StudentRegisterVO vo);//新生报到

    public List<StudentRegisterVO> getRegisterList(StudentRegisterVO vo);//新生报到人员列表

    public List<StudentRegisterVO> getRegisterDetail();//获取新生报到详情
}
