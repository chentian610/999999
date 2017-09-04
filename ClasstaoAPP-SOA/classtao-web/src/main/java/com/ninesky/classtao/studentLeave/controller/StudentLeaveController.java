package com.ninesky.classtao.studentLeave.controller;

import com.ninesky.classtao.studentLeave.service.StudentLeaveService;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveFileVO;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("studentLeaveAction")
public class StudentLeaveController extends BaseController {

    @Autowired
    private StudentLeaveService studentLeaveService;

    /**
     * 学生请假
     * @return
     */
    @RequestMapping("/addStudentLeave")
    public Object addStudentLeave(){
        StudentLeaveVO vo= BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.addStudentLeave(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 获取学生请假记录
     * @return
     */
    @RequestMapping("/getStudentLeaveList")
    @ResultField
    public Object getStudentLeaveList(){
        StudentLeaveVO vo= BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getStudentLeaveList(vo);
    }

    /**
     * 获取请假详情
     * @return
     */
    @RequestMapping("/getStudentLeaveByID")
    @ResultField
    public Object getStudentLeave(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getStudentLeave(vo.getLeave_id());
    }

    /**
     * 撤销请假申请
     * @return
     */
    @RequestMapping("/cancelStudentLeave")
    public Object cancelStudentLeave(){
        StudentLeaveVO vo =BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.cancelStudentLeave(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 删除图片
     * @return
     */
    @RequestMapping("/deleteFileByID")
    public Object deleteFileByID(){
        StudentLeaveFileVO vo = BeanUtil.formatToBean(StudentLeaveFileVO.class);
        studentLeaveService.deleteFileById(vo.getId());
        return ResponseUtils.sendSuccess();
    }

    /**
     * 教师未处理请假列表
     * @return
     */
    @RequestMapping("/getUnTreatedList")
    @ResultField
    public Object getUnTreatedList(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getUnTreatedList(vo);
    }

    /**
     * 教师已处理情况列表
     * @return
     */
    @RequestMapping("/getTreatedList")
    @ResultField
    public Object getTreatedList(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getTreatedList(vo);
    }

    /**
     * 同意请假申请
     * @return
     */
    @RequestMapping("/passStudentLeave")
    public Object passStudentLeave(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.passStudentLeave(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 转交请假申请
     * @return
     */
    @RequestMapping("/transferStudentLeave")
    public Object transferStudentLeave(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.transferStudentLeave(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 拒绝请假申请
     * @return
     */
    @RequestMapping("/refuseStudentLeave")
    public Object refuseStudentLeave(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.refuseStudentLeave(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 学生请假统计
     * @return
     */
    @RequestMapping("/getStudentLeaveCount")
    @ResultField
    public Object getStudentLeaveCount(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getStudentLeaveCount(vo);
    }

    /**
     * 学生请假统计详情
     * @return
     */
    @RequestMapping("/getStudentLeaveCountDetail")
    @ResultField
    public Object getStudentLeaveCountDetail(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        return studentLeaveService.getStudentLeaveCountDetail(vo);
    }

    /**
     * 教师取消转交申请
     * @return
     */
    @RequestMapping("/cancelTransfer")
    public Object cancelTransfer(){
        StudentLeaveVO vo=BeanUtil.formatToBean(StudentLeaveVO.class);
        studentLeaveService.cancelTransfer(vo);
        return ResponseUtils.sendSuccess();
    }
}
