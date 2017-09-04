package com.ninesky.classtao.microPortal.controller;

import com.ninesky.classtao.microPortal.service.MicroPortalService;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by TOOTU on 2017/7/27.
 */
@RestController
@RequestMapping(value = "microPortalAction")
public class MicroPortalController {
    @Autowired
    private MicroPortalService  microPortalService;

    /**
     * 获取学校微门户
     * @param
     * @return
     */
    @RequestMapping(value="/getMicroPortal")
    public Object getMicroPortal(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        return microPortalService.getMicroPortal(paramMap);
    }

    /**
     * 添加学校微门户
     * @param
     * @return
     */
    @RequestMapping(value="/addMicroPortal")
    public Object addMicroPortal(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        microPortalService.addMicroPortal(paramMap);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 添加学校微门户
     * @param
     * @return
     */
    @RequestMapping(value="/deleteMicroPortal")
    public Object deleteMicroPortal(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        microPortalService.deleteMicroPortal(paramMap);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 添加学校微门户
     * @param
     * @return
     */
    @RequestMapping(value="/updateMicroPortal")
    public Object updateMicroPortal(HttpServletRequest request){
        Map<String, String> paramMap = ActionUtil.getParameterMap();
        microPortalService.addMicroPortal(paramMap);
        return ResponseUtils.sendSuccess();
    }
}
