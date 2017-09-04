package com.ninesky.classtao.studentCard.controller;

import com.ninesky.classtao.schoolMenu.service.SchoolMenuService;
import com.ninesky.classtao.schoolMenu.vo.SchoolMenuVO;
import com.ninesky.classtao.studentAttend.service.StudentAttendService;
import com.ninesky.classtao.studentAttend.vo.StudentAttendVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "studentCardAction")
public class StudentCardController extends BaseController{

    @Autowired
    private StudentAttendService studentAttendService;

    @Autowired
    private SchoolMenuService schoolMenuService;

    /**
     * 学生通勤打卡
     * @return
     */
    @RequestMapping(value = "/addStudentAttend")
    @ResultField(includes = {"attend_id","attend_time","card_number","file_url","file_resize_url","student_name"})
    public Object addStudentAttend(HttpServletRequest request){
        Boolean flag=JWTUtil.checkSchoolToken(request);
        if (!flag) throw new AuthException("身份校验失败");
        StudentAttendVO vo=BeanUtil.formatToBean(StudentAttendVO.class);
        return studentAttendService.addStudentAttend(vo);
    }

    /**
     * 获取当天菜谱
     * @return
     */
    @RequestMapping(value = "/getSchoolMenuList")
    @ResultField(includes = {"school_menu_id","school_id","menu_date","menu_name","file_list","monday_data"})
    public Object getSchoolMenuList(HttpServletRequest request){
        Boolean flag=JWTUtil.checkSchoolToken(request);
        if (!flag) throw new AuthException("身份校验失败");
        SchoolMenuVO vo = BeanUtil.formatToBean(SchoolMenuVO.class);
        return schoolMenuService.getSchoolMenuList(vo);
    }
}
