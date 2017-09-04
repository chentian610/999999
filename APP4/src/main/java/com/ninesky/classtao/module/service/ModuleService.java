package com.ninesky.classtao.module.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.classtao.school.vo.SchoolVO;

public interface ModuleService {
	/**
	 * 根据schoolId获取模块列表
	 * @param schoolId
	 * @return
	 */
	public List<SchoolModuleVO> getModuleList(Map<String, Object> paramMap);
	
	/**
	 * 为学校添加模块列表
	 * @param schoolId
	 * @return
	 */
	public void addModuleList(List<SchoolModuleVO> list);
	
	/**
	 * 根据schoolId获取module_code集合
	 * @param school_id
	 * @return
	 */
	public List<SchoolModuleVO> getSchoolModuleCodeListBySchoolID(Integer school_id);

	public List<SchoolModuleVO> getSchoolModuleBasicsList(SchoolModuleVO vo);

	public List<SchoolModuleVO> getScoreModule();//扣分相关模块

	public List<SchoolModuleVO> getSchoolModuleByCode(SchoolModuleVO vo);//根据module_code查询模块
}
