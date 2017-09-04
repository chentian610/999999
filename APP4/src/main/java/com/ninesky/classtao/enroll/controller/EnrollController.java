package com.ninesky.classtao.enroll.controller;

import com.ninesky.classtao.enroll.service.EnrollService;
import com.ninesky.classtao.enroll.vo.StudentEnrollVO;
import com.ninesky.classtao.enroll.vo.StudentRecruitVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "enrollAction")
public class EnrollController extends BaseController{

    @Autowired
    private EnrollService enrollService;

    /**
     * 获取学校招生简章
     * @return
     */
    @RequestMapping(value = "/getRecruitList")
    @ResultField(excludes = {"content"})
    public Object getRecruitList(){
        StudentRecruitVO vo= BeanUtil.formatToBean(StudentRecruitVO.class);
        return enrollService.getRecruitList(vo);
    }

    /**
     * 获取招生简章详情
     * @return
     */
    @RequestMapping(value = "/getRecruitByID")
    @ResultField
    public Object getRecruitByID(){
        StudentRecruitVO vo=BeanUtil.formatToBean(StudentRecruitVO.class);
        return enrollService.getRecruitByID(vo.getRecruit_id());
    }

    /**
     * 新生报名
     * @return
     */
    @RequestMapping(value = "/enroll")
    public Object enroll(){
        StudentEnrollVO vo=BeanUtil.formatToBean(StudentEnrollVO.class);
        enrollService.enroll(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 获取招生报名情况
     * @return
     */
    @RequestMapping(value = "/getEnrollStatus")
    public Object getEnrollStatus(){
        StudentEnrollVO vo=BeanUtil.formatToBean(StudentEnrollVO.class);
        return enrollService.getEnrollStatus(vo);
    }

    /**
     * 获取报名学生列表
     * @return
     */
    @RequestMapping(value = "/getStudentList")
    public Object getStudentList(){
        StudentEnrollVO vo=BeanUtil.formatToBean(StudentEnrollVO.class);
        return enrollService.getStudentList(vo);
    }

    /**
     * 获取学校招生简章
     * @return
     */
    @RequestMapping(value = "/getAllRecruitList")
    @ResultField(excludes = {"content"})
    public Object getAllRecruitList(){
        StudentRecruitVO vo= BeanUtil.formatToBean(StudentRecruitVO.class);
        return enrollService.getAllRecruitList(vo);
    }

    /**
     * 添加学校招生简章
     * @return
     */
    @RequestMapping(value = "/addRecruit")
    public Object addRecruit(){
        StudentRecruitVO vo=BeanUtil.formatToBean(StudentRecruitVO.class);
        enrollService.addRecruit(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 更新学校招生简章
     * @return
     */
    @RequestMapping(value = "/updateRecruit")
    public Object updateRecruit(){
        StudentRecruitVO vo=BeanUtil.formatToBean(StudentRecruitVO.class);
        enrollService.updateRecruit(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 录取
     * @return
     */
    @RequestMapping(value = "/admission")
    public Object admission(){
        StudentEnrollVO vo=BeanUtil.formatToBean(StudentEnrollVO.class);
        enrollService.admission(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 获取报名学生详情
     * @return
     */
    @RequestMapping(value = "/getStudentByID")
    public Object getStudentByID(){
        StudentEnrollVO vo=BeanUtil.formatToBean(StudentEnrollVO.class);
        return enrollService.getStudentByID(vo);
    }

    /**
     * 完成招生录取
     * @return
     */
    @RequestMapping(value = "/completeEnroll")
    public Object completeEnroll(){
        StudentRecruitVO vo=BeanUtil.formatToBean(StudentRecruitVO.class);
        enrollService.completeEnroll(vo);
        return ResponseUtils.sendSuccess();
    }
}
