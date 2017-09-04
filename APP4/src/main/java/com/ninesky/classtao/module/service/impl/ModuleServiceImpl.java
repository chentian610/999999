package com.ninesky.classtao.module.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ninesky.common.util.ActionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.module.service.ModuleService;
import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.common.util.ListUtil;

@Service("ModuleServiceImpl")
public class ModuleServiceImpl implements ModuleService{
	@Autowired
	private GeneralDAO dao;
	
	/**
	 * 根据schoolId获取模块列表
	 * @param
	 * @return
	 */
	public List<SchoolModuleVO> getModuleList(Map<String, Object> paramMap) {
		return dao.queryForList("schoolModuleMap.getSchoolModuleList",paramMap);
	}
	
	/**
	 * 为学校添加模块列表
	 * @param
	 * @return
	 */
	public void addModuleList(List<SchoolModuleVO> list){
		if(ListUtil.isEmpty(list)) return;
		dao.insertObject("schoolModuleMap.insertSchoolModuleBatch", list);
	}

	@Override
	public List<SchoolModuleVO> getSchoolModuleCodeListBySchoolID(Integer school_id) {
		return dao.queryForList("schoolModuleMap.getSchoolModuleCodeListBySchoolID",school_id);
	}

	@Override
	public List<SchoolModuleVO> getSchoolModuleBasicsList(SchoolModuleVO vo){
		return dao.queryForList("schoolModuleMap.getSchoolModuleBasicsList",vo);
	}

	//扣分相关模块
	public List<SchoolModuleVO> getScoreModule(){
        String[] arr={"009023","009021","009022","009028","009025"};
        List<SchoolModuleVO> moduleList=new ArrayList<SchoolModuleVO>();
        for (String ss:arr){
            SchoolModuleVO vo=new SchoolModuleVO();
            vo.setSchool_id(ActionUtil.getSchoolID());
            vo.setModule_code(ss);
            List<SchoolModuleVO> list=dao.queryForList("schoolModuleMap.getModuleByCode",vo);//学校是否有在用
            if (ListUtil.isNotEmpty(list)) moduleList.add(list.get(0));
        }
		return moduleList;
	}

	//根据module_code查询模块
	public List<SchoolModuleVO> getSchoolModuleByCode(SchoolModuleVO vo){
		 return dao.queryForList("schoolModuleMap.getModuleByCode",vo);//学校是否有在用
	}
}
