package com.ninesky.classtao.template.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.login.vo.AppVersionVO;
import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.template.vo.ModuleVO;
import com.ninesky.classtao.template.vo.TemplateVO;

public interface TemplateService {
	public List<TemplateVO> getTemplateList(TemplateVO vo);
	public List<ModuleVO> getModuleList();
	public List<AppVersionVO> getSchoolApp(SchoolVO vo);
	/**
	 * 根据id获取指定模块信息
	 * @param moduleId
	 * @return
	 */
	public ModuleVO getModuleById(Integer moduleId);
	/**
	 * 根据模块code获取指定模块信息
	 * @param
	 * @return
	 */
	public List<ModuleVO> getModuleByCode(String moduleCode);

	/**
	 * 根据学校类型获取必选模板列表
	 * @param vo
	 * @return
	 */
	public List<TemplateVO> getMandatoryTemplateList(TemplateVO vo);

	public List<ModuleVO> getModuleBasicsList(String school_type, String parent_code);


}
