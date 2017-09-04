package com.ninesky.classtao.school.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.school.service.GradeService;
import com.ninesky.classtao.school.vo.GradeVO;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@RestController
@RequestMapping(value="gradeAction")
public class GradeController extends BaseController{

	@Autowired
	private GradeService gradeService;
	
	/**
	 * 获取学校年级列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getGradeList")
	public @ResponseBody Object getGradeList(HttpServletRequest request){
		GradeVO vo=BeanUtil.formatToBean(GradeVO.class);
		List<GradeVO> list=gradeService.getGradeList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 添加学校年级
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addGrade")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object addGrade(HttpServletRequest request){
		GradeVO vo=BeanUtil.formatToBean(GradeVO.class);
		vo=gradeService.addGrade(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 更新学校年级信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateGrade")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object updateGrade(HttpServletRequest request){
		GradeVO vo=BeanUtil.formatToBean(GradeVO.class);
		gradeService.updateGrade(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 归档学校年级信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteGrade")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object deleteGrade(HttpServletRequest request){
		GradeVO vo=BeanUtil.formatToBean(GradeVO.class);
		gradeService.deleteGrade(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取年级信息，并含相应年级下的班级
	 * @return
	 */
	@RequestMapping(value="/getGradeAndClass")
	@GetCache(name="getGradeAndClass",value="school_id")
	@ResultField(includes={"grade_id","grade_name","class_list"})
	public Object getGradeAndClass(){
		return gradeService.getGradeAndClass();
	}
}
