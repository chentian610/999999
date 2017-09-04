package com.ninesky.classtao.studentAttend.controller;

import com.ninesky.classtao.studentAttend.service.StudentAttendService;
import com.ninesky.classtao.studentAttend.vo.StudentAttendVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "studentAttendAction")
public class StudentAttendController extends BaseController{

    @Autowired
    private StudentAttendService studentAttendService;

    /**
     * 获取学生打卡记录
     * @return
     */
    @RequestMapping(value = "/getAttendListByStuID")
    @ResultField
    public Object getAttendListByStuID(){
        StudentAttendVO vo=BeanUtil.formatToBean(StudentAttendVO.class);
        return studentAttendService.getAttendListByStuID(vo);
    }

    /**
     * 获取学生打卡记录详情
     * @param attend_id
     * @return
     */
    @RequestMapping(value = "/getAttendById")
    @ResultField
    public Object getAttendById(Integer attend_id){
        return studentAttendService.getAttendByID(attend_id);
    }

    /**
     * 学生通勤统计
     * @return
     */
    @RequestMapping(value = "/getStudentAttendCount")
    @ResultField
    public Object getStudentAttendCount(){
        StudentAttendVO vo= BeanUtil.formatToBean(StudentAttendVO.class);
        return studentAttendService.getAttendList(vo);
    }

    /**
     * 学生通勤打卡
     * @return
     */
    @RequestMapping(value = "/addStudentAttend")
    @ResultField
    public Object addStudentAttend(){
        StudentAttendVO vo=BeanUtil.formatToBean(StudentAttendVO.class);
        return studentAttendService.addStudentAttend(vo);
    }
}
