package com.ninesky.classtao.template.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.SchoolConfig;
import org.omg.CORBA.portable.ValueOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.classtao.login.vo.AppVersionVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.template.service.TemplateService;
import com.ninesky.classtao.template.vo.ModuleVO;
import com.ninesky.classtao.template.vo.TemplateVO;
import com.ninesky.common.DictConstants;

@Service("templateServiceImpl")
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private GeneralDAO dao;
	
	
	public List<TemplateVO> getTemplateList(TemplateVO vo){
		List<TemplateVO> list =  dao.queryForList("templateMap.getTemplateList", vo);
		for (TemplateVO item : list) {
			List<ModuleVO> module_list = dao.queryForList("templateModuleMap.getTemplateModuleList",item.getTemplate_id());
			List<ModuleVO> allModule=new ArrayList<ModuleVO>();
			for(ModuleVO tvo: module_list){
				if(tvo.getIs_must()==1||tvo.getModule_is_must()==1){
					tvo.setIs_must(1);
					allModule.add(tvo);
				}
			}
			item.setModule_list(allModule);
		}
		return list;
    }

	public List<TemplateVO> getMandatoryTemplateList(TemplateVO vo){
		List<TemplateVO> list =  dao.queryForList("templateMap.getTemplateList", vo);
		for (TemplateVO item : list) {
			List<ModuleVO> module_list = dao.queryForList("templateModuleMap.getTemplateList",item.getTemplate_id());
			item.setModule_list(module_list);
		}
		return list;
	}

	public List<ModuleVO> getModuleList(){
		ModuleVO vo = new ModuleVO();
		vo.setIs_inactive(0);
		vo.setPartner_code(SchoolConfig.getProperty(Constants.PARTNER_CODE));
		if (StringUtil.isNotEmpty(ActionUtil.getParameter("parent_code"))) vo.setParent_code(ActionUtil.getParameter("parent_code"));
		List<ModuleVO> list =  dao.queryForList("moduleMap.getModuleList",vo);
		return list;
	}

	public List<AppVersionVO> getSchoolApp(SchoolVO vo) { 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("school_id", ActionUtil.getSchoolID());
		map.put("app_type", DictConstants.APPTYPE_ANDROID);
		List<AppVersionVO> list =  dao.queryForList("appVersionMap.getAppVersionList",map);
		return list;
	}
	
	/**
	 * 根据id获取指定模块信息
	 * @param
	 * @return
	 */
	public ModuleVO getModuleById(Integer module_id){
		return dao.queryObject("moduleMap.getModuleById", module_id);
	}
	
	/**
	 * 根据模块code获取指定模块信息
	 * @param
	 * @return
	 */
	public List<ModuleVO> getModuleByCode(String module_code){
		return dao.queryForList("moduleMap.getModuleByCode", module_code);
	}

	public List<ModuleVO> getModuleBasicsList(String school_type,String parent_code){
		ModuleVO vo = new ModuleVO();
		vo.setIs_inactive(0);
		vo.setPartner_code(SchoolConfig.getProperty(Constants.PARTNER_CODE));
		if (StringUtil.isNotEmpty(school_type)) vo.setSchool_type(school_type);
		if (StringUtil.isNotEmpty(parent_code)) vo.setParent_code(parent_code);
		return dao.queryForList("moduleMap.getModuleBasicsList",vo);
	}
}
