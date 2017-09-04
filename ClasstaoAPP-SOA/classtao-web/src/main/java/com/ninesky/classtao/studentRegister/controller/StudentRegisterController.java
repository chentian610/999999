package com.ninesky.classtao.studentRegister.controller;

import com.ninesky.classtao.studentRegister.service.StudentRegisterService;
import com.ninesky.classtao.studentRegister.vo.StudentRegisterVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("studentRegisterAction")
public class StudentRegisterController extends BaseController{

    @Autowired
    private StudentRegisterService studentRegisterService;

    /**
     * 新生报到
     * @return
     */
    @RequestMapping("/addRegister")
    @ResultField
    public Object addRegister(){
        StudentRegisterVO vo= BeanUtil.formatToBean(StudentRegisterVO.class);
        return studentRegisterService.addRegister(vo);
    }

    /**
     * 新生报到人员列表
     * @return
     */
    @RequestMapping("/getRegisterList")
    public Object getRegisterList(){
        StudentRegisterVO vo=BeanUtil.formatToBean(StudentRegisterVO.class);
        return studentRegisterService.getRegisterList(vo);
    }

    /**
     * 新生报到详情
     * @return
     */
    @RequestMapping("/getRegisterDetail")
    public Object getRegisterYear(){
        return studentRegisterService.getRegisterDetail();
    }
}
