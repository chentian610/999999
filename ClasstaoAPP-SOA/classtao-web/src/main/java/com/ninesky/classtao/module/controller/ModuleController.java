package com.ninesky.classtao.module.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.SchoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.module.service.ModuleService;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResultField;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="moduleAction")
public class ModuleController extends BaseController{
	
	@Autowired
	private ModuleService moduleService;
	/**
	 * 获取模块列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getModuleList")
	@GetCache(name="ModuleList",value="school_id,user_type")
	@ResultField(includes={"user_type","module_code","module_name","icon_url","parent_code","module_url","initdata","is_bigicon","is_news"})
	public Object getModuleList(){
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("user_type", ActionUtil.getUserType());
		paramMap.put("school_id", ActionUtil.getSchoolID());
		paramMap.put("partner_code", SchoolConfig.getProperty(Constants.PARTNER_CODE));
		return moduleService.getModuleList(paramMap);
	}

	@RequestMapping(value="/getSchoolModuleCodeListBySchoolID")
	@ResultField(includes={"user_type","module_code"})
	public @ResponseBody Object getSchoolModuleCodeListBySchoolID(){
		Integer school_id = ActionUtil.getSchoolID();
		return moduleService.getSchoolModuleCodeListBySchoolID(school_id);
	}

	@RequestMapping(value="/getSchoolModuleBasicsList")
	public @ResponseBody Object getSchoolModuleBasicsList(HttpServletRequest request){
		SchoolModuleVO vo = BeanUtil.formatToBean(SchoolModuleVO.class);
		vo.setParent_code(SchoolConfig.getProperty(Constants.PARTNER_CODE));
		List<SchoolModuleVO> list = moduleService.getSchoolModuleBasicsList(vo);
		return ResponseUtils.sendSuccess("成功！",list);
	}

	/**
	 * 扣分相关模块
	 * @return
	 */
	@RequestMapping(value = "/getScoreModule")
    @ResultField(includes = {"module_code","module_name"})
	public Object  getScoreModule(){
		return moduleService.getScoreModule();
	}

	/**
	 * 根据module_code查询模块
	 * @return
	 */
	@RequestMapping(value = "/getSchoolModuleByCode")
	@ResultField
	public @ResponseBody Object getSchoolModuleByCode(){
		SchoolModuleVO vo=BeanUtil.formatToBean(SchoolModuleVO.class);
		 return moduleService.getSchoolModuleByCode(vo);
	}
}
