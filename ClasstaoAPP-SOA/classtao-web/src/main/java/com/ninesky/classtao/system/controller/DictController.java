package com.ninesky.classtao.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.common.util.ActionUtil;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@Controller
@RequestMapping(value="dictAction")
public class DictController extends BaseController{

	@Autowired
	private DictService dictService;

	/**
	 * 获取数据字典
	 * @param request
	 * @return
	 */
	@GetCache(name="getDictionary",value="dict_group")
	@RequestMapping(value="getDictionary")
	public @ResponseBody Object getDictionary(HttpServletRequest request){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		List<DictVO> list=dictService.getDictList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 添加数据字典
	 * @param
	 * @return
	 */
	@PutCache(name="getDictionary",value="dict_group")
	@RequestMapping(value="addDictionary")
	public @ResponseBody Object addDictionary(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		dictService.addDictionary(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 添加数据字典
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateDictSort")
	public @ResponseBody Object updateDictSort(HttpServletRequest request){
		String jsonArray = request.getParameter("json_array");
		//dictService.updateDictSort(jsonArray);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 添加数据字典
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateDictSchoolSort")
	public @ResponseBody Object updateDictSchoolSort(HttpServletRequest request){
		String jsonArray=request.getParameter("json_array");//排序array
		String dict_group=request.getParameter("dict_group");
		dictService.updateDictSchoolSort(jsonArray,dict_group);
		return ResponseUtils.sendSuccess();
	}
	
	@RequestMapping(value="getDict")
	public @ResponseBody Object getDict(HttpServletRequest request){
		return ResponseUtils.sendSuccess(dictService.getDict(request.getParameter("dict_code")));
	}
	
	@RequestMapping(value="getNewsCssList")
	public @ResponseBody Object getNewsCssList(String dict_group){
		return ResponseUtils.sendSuccess(dictService.getNewsCssList(dict_group));
	}
	
	/**
	 * 获取学校配置（dict_school;兴趣班课程,请假类型）
	 * @return
	 */
	@RequestMapping(value="/getDictSchoolList")
	@ResultField(includes = {"dict_code","dict_value","dict_group","sort","is_active","other_field"})
	public @ResponseBody Object getDictSchoolList(){
		return ResponseUtils.sendSuccess(dictService.getDictSchoolList(BeanUtil.formatToBean(DictVO.class)));
	}
	
	/**
	 * 添加学校配置（dict_school;兴趣班课程,请假类型）
	 * @param
	 * @return
	 */
	@RequestMapping(value="/addDictSchool")
	public @ResponseBody Object addDictSchool(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		return ResponseUtils.sendSuccess(dictService.addDictSchool(vo));
	}
	
	/**
	 * 禁用兴趣班课程（dict_school；兴趣班课程）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/forbiddenDictSchool")
	public @ResponseBody Object forbiddenDictSchool(HttpServletRequest request){
		String dict_code=request.getParameter("dict_code");
		dictService.deleteDictSchool(dict_code);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 修改学校配置（dict_school;兴趣班课程，请假类型）
	 * @return
	 */
	@RequestMapping(value="/updateDictSchool")
	public @ResponseBody Object updateDictSchool(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		dictService.updateDictSchool(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 删除身份
	 * @return
	 */
	@RequestMapping(value="/removeSchoolRoleDict")
	public @ResponseBody Object removeSchoolRoleDict(String dict_code){
		dictService.removeSchoolRoleDict(dict_code);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 删除新闻栏目
	 * @return
	 */
	@RequestMapping(value="/deleteNewsDictSchool")
	public @ResponseBody Object deleteNewsDictSchool(DictVO vo){
		dictService.deleteNewsDictSchool(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 根据dict_code删除新闻栏目
	 * @return
	 */
	@RequestMapping(value="/deleteNewsDictSchoolByCode")
	public @ResponseBody Object deleteNewsDictSchoolByCode(DictVO vo){
		dictService.deleteNewsDictSchoolByCode(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 获取省份计划
	 * @return
	 */
	@RequestMapping(value="/getProvinceList")
	public @ResponseBody Object getProvinceList(){
		return dictService.getProvinceList();
	}

	/**
	 * 获取缴费类型集合
	 * @return
	 */
	@RequestMapping(value="/getPayTypeList")
	@ResultField(includes = {"dict_code","dict_value","dict_group","other_field","sort","description"})
	public @ResponseBody Object getPayTypeList(DictVO vo,HttpServletRequest request){
		vo.setClient_id(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		return dictService.getPayTypeList(vo);
	}

	/**
	 * 修改学校配置（dict_school;兴趣班课程，请假类型）
	 * @return
	 */
	@RequestMapping(value="/addNewsDictSchool")
	public @ResponseBody Object addNewsDictSchool(DictSchoolVO vo){
		return dictService.addNewsDictSchool(vo);
	}


	/**
	 * 获取新闻信息的news_code类型
	 * @param request
	 * @return
	 */
	@PutCache(name="getDictSchoolList",value="news_code,school_id")
	@RequestMapping(value="getNewsDictSchoolList")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version","id"})
	public @ResponseBody Object getDictSchoolList(DictSchoolVO vo,HttpServletRequest request){
		vo.setSchool_id(ActionUtil.getSchoolID());
		return dictService.getNewsDictSchoolList(vo);
	}

	/**
	 * 获取新闻信息的dict_code类型
	 * @param request
	 * @return
	 */
	@PutCache(name="getDictionary",value="news_code,school_id")
	@RequestMapping(value="getNewsDictionary")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version","id"})
	public @ResponseBody Object getNewsDictionary(HttpServletRequest request){
		DictSchoolVO vo=BeanUtil.formatToBean(DictSchoolVO.class);
		vo.setDict_group(ActionUtil.getParameter("dict_group"));
		vo.setSchool_id(ActionUtil.getSchoolID());
		return dictService.getNewsDictionary(vo);
	}
}
