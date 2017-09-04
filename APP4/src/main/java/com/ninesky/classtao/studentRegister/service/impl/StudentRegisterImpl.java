package com.ninesky.classtao.studentRegister.service.impl;

import com.ninesky.classtao.studentRegister.service.StudentRegisterService;
import com.ninesky.classtao.studentRegister.vo.StudentRegisterVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("studentRegisterImpl")
public class StudentRegisterImpl implements StudentRegisterService{

    @Autowired
    private GeneralDAO dao;

    //新生报到
    public StudentRegisterVO addRegister(StudentRegisterVO vo){
        if (StringUtil.isEmpty(vo.getStudent_name()) || IntegerUtil.isNull(vo.getSex()) ||
                StringUtil.isEmpty(vo.getId_number()) || StringUtil.isEmpty(vo.getMiddle_school()) ||
                IntegerUtil.isNull(vo.getIs_accommodate()))
            throw new BusinessException(MsgService.getMsg("PLEASE_WRITE_INFORMATION_FULL"));
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//当前报到年份
        vo.setEnrollment_year(year);
        StudentRegisterVO registerVO=dao.queryObject("studentRegisterMap.getStudentRegister",vo);
        if (registerVO==null) {
            vo.setCreate_by(ActionUtil.getUserID());
            vo.setCreate_date(ActionUtil.getSysTime());
            dao.insertObject("studentRegisterMap.insertStudentRegister",vo);
        } else {//已报到过则覆盖之前一条
            vo.setUpdate_by(ActionUtil.getUserID());
            vo.setUpdate_date(ActionUtil.getSysTime());
            dao.updateObject("studentRegisterMap.updateStudentRegister",vo);
        }
        return vo;
    }

    //新生报到人员列表
    public List<StudentRegisterVO> getRegisterList(StudentRegisterVO vo){
        vo.setSchool_id(ActionUtil.getSchoolID());
        return dao.queryForList("studentRegisterMap.getStudentRegisterList",vo);
    }

    //新生报到详情
    public List<StudentRegisterVO> getRegisterDetail(){
        List<Integer> list= dao.queryForList("studentRegisterMap.getRegisterYear",ActionUtil.getSchoolID());
        List<StudentRegisterVO> registerVOList=new ArrayList<StudentRegisterVO>();
        for (int year :list){
            StudentRegisterVO vo=new StudentRegisterVO();
            vo.setSchool_id(ActionUtil.getSchoolID());
            vo.setEnrollment_year(year);
            int count=dao.queryObject("studentRegisterMap.getRegisterCount",vo);//报到人数
            vo.setCount(count);
            registerVOList.add(vo);
        }
        return registerVOList;
    }
}
