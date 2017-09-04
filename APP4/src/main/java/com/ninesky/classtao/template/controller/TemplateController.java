package com.ninesky.classtao.template.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.SchoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ninesky.classtao.login.vo.AppVersionVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.template.service.TemplateService;
import com.ninesky.classtao.template.vo.ModuleVO;
import com.ninesky.classtao.template.vo.TemplateVO;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;

@Controller
@RequestMapping(value="templateAction")
public class TemplateController extends BaseController{
	@Autowired
	private TemplateService templateService;
	  
	
	@RequestMapping(value="/getTemplateList")
	public @ResponseBody Object getTemplateList(HttpServletRequest request){
		TemplateVO vo = BeanUtil.formatToBean(TemplateVO.class);
		List<TemplateVO> list = templateService.getTemplateList(vo);		
		return ResponseUtils.sendSuccess("成功！",list);
	}

	@RequestMapping(value="/getMandatoryTemplateList")
	public @ResponseBody Object getMandatoryTemplateList(HttpServletRequest request){
		TemplateVO vo = BeanUtil.formatToBean(TemplateVO.class);
		List<TemplateVO> list = templateService.getMandatoryTemplateList(vo);
		return ResponseUtils.sendSuccess("成功！",list);
	}
	
	@RequestMapping(value="/getModuleList")
	public @ResponseBody Object getModuleList(String school_name,String clientid){
		List<ModuleVO> list = templateService.getModuleList();
		return ResponseUtils.sendSuccess("成功！",list);
	}

	@RequestMapping(value="/getModuleBasicsList")
	public @ResponseBody Object getModuleBasicsList(String school_type,String parent_code){
		List<ModuleVO> list = templateService.getModuleBasicsList(school_type,parent_code);
		return ResponseUtils.sendSuccess("成功！",list);
	}
	
	/**
	 * 获取注册的学校列表
	 */
	@RequestMapping(value="/getSchoolApp")
	public @ResponseBody Object getSchoolApp(){
		SchoolVO vo = new SchoolVO();
		List<AppVersionVO> list = templateService.getSchoolApp(vo);
		return ResponseUtils.sendSuccess(list);
	}
}
