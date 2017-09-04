package com.ninesky.classtao.teacherAttend.controller;

import com.ninesky.classtao.teacherAttend.service.TeacherAttendService;
import com.ninesky.classtao.teacherAttend.vo.TeacherAttendVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "teacherAttendAction")
public class TeacherAttendController extends BaseController{

    @Autowired
    private TeacherAttendService teacherAttendService;

    /**
     * 教师打卡
     * @return
     */
    @RequestMapping(value = "/addTeacherAttend")
    @ResultField
    public Object addTeacherAttend(){
        TeacherAttendVO vo= BeanUtil.formatToBean(TeacherAttendVO.class);
        return teacherAttendService.addTeacherAttend(vo);
    }

    /**
     * 教师打卡更新
     * @return
     */
    @RequestMapping(value = "/updateTeacherAttend")
    @ResultField
    public Object updateTeacherAttend(){
        TeacherAttendVO vo=BeanUtil.formatToBean(TeacherAttendVO.class);
        return teacherAttendService.updateTeacherAttend(vo);
    }

    /**
     * 获取教师打卡记录列表
     * @param search_time
     * @return
     */
    @RequestMapping(value = "/getAttendListByUserID")
    @ResultField
    public Object getAttendListByUserID(String search_time){
        return teacherAttendService.getAttendListByUserID(search_time);
    }

    /**
     * 教师通勤统计
     * @return
     */
    @RequestMapping(value = "/getTeacherAttendCount")
    @ResultField
    public Object getTeacherAttendCount(){
        return teacherAttendService.getTeacherAttendCount();
    }

    /**
     * 教师通勤当天记录
     * @return
     */
    @RequestMapping(value = "/getAttendByUserID")
    @ResultField
    public Object getAttendByUserID(){
        return teacherAttendService.getAttendByUserID();
    }

    /**
     * 教师通勤统计详情
     * @param attend_status
     * @return
     */
    @RequestMapping(value = "/getTeacherAttendCountDetail")
    @ResultField
    public Object getTeacherAttendCountDetail(String attend_status){
        return teacherAttendService.getTeacherAttendCountDetail(attend_status);
    }
}
