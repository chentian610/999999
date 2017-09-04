package com.ninesky.classtao.school.service;

import java.util.List;

import com.ninesky.classtao.school.vo.GradeVO;

public interface GradeService {

	public List<GradeVO> getGradeList(GradeVO vo);//获取学校年级列表
	
	public GradeVO addGrade(GradeVO vo);//添加学校年级
	
	public void updateGrade(GradeVO vo);//更新学校年级信息
	
	public void deleteGrade(GradeVO vo);//归档学校年级信息
	
	public GradeVO getGradeByNum(GradeVO vo);//根据学校和年级num获取年级信息
	
	public GradeVO getGradeByID(Integer grade_id);//根据ID获取年级信息
	
	public List<GradeVO> getGradeAndClass();//获取年级信息，包括该年级的班级信息
	
	public void setGradeIsGraduateByGradeID(Integer grade_id);
}
