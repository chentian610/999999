package com.ninesky.classtao.system.service;

import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.system.vo.DictVO;

import java.util.List;
import java.util.Map;

public interface DictService {
	
	public List<DictVO> getDictList(DictVO vo);//获取数据字典

	public void updateDictSort(String jsonArray, String dict_group);//更新数据字典（排序）

	public void updateDictSchoolSort(String jsonArray, String dict_group);//更新数据字典（排序）

	public DictVO addDictionary(DictVO vo);//增加数据字典
	
	public void deleteDictionary(DictVO vo);//删除数据字典

	public void updateDictName(DictVO vo);//字典重命名
	
	public DictVO getDict(String dict_code);
	
	public List<DictVO> getNewsCssList(String dict_group);//获取新闻样式
	
	public List<DictVO> getDictSchoolList(DictVO vo);//获取学校配置（dict_school;兴趣班课程，请假类型）
	
	public DictVO addDictSchool(DictVO vo);//添加学校配置（dict_school;兴趣班课程,请假类型）
	
	public void deleteDictSchool(String dict_code);//删除学校配置（dict_school;兴趣班课程，请假类型）
	
	public void updateDictSchool(DictVO vo);//修改学校配置（dict_school;兴趣班课程，请假类型）

	public  void deleteNewsDictSchool(DictVO vo);//删除学校配置（dict_school;新闻模块）

	public  void deleteNewsDictSchoolByCode(DictVO vo);//删除学校配置（dict_school;新闻模块）

	public void initSchoolDict(Integer school_id);//生成学校时初始化新闻栏目

	public DictSchoolVO addNewsDictSchool(DictSchoolVO vo);//添加新闻栏目(新闻栏目的dict_code不能重复)

	public List<DictSchoolVO> getNewsDictSchoolList(DictSchoolVO vo);//获取新闻栏目列表

	public List<DictSchoolVO> getNewsDictionary(DictSchoolVO vo);	// 获取新闻栏目模块列表

	public List<Map<String,Object>> getProvinceList(); //获取省级列表

	public List<DictVO> getPayTypeList(DictVO vo); //获取缴费类型列表

	public void removeSchoolRoleDict(String dict_code); //删除学校角色配置信息

	public void insertSchoolAPPDict(SchoolVO vo);//插入学校APP配置信息

}
