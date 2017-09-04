package com.ninesky.classtao.school.vo;


import com.ninesky.common.vo.BaseVO;

public class GradeVO extends BaseVO{
	private Integer grade_id;
	
	/**
	 * 学校ID
	 */
	private Integer school_id;
	
	private Integer enrollment_year;
	/**
	 * 年级名称
	 */
	private String grade_name;
	
	/**
	 * 年级号
	 */
	private Integer grade_num;
	
	private Integer sort;

	private String class_list;
	/**
	 * 该年级是否已经毕业
	 */
	private Integer is_graduate;
	
	public Integer getIs_graduate() {
		return is_graduate;
	}

	public void setIs_graduate(Integer is_graduate) {
		this.is_graduate = is_graduate;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getEnrollment_year() {
		return enrollment_year;
	}

	public void setEnrollment_year(Integer enrollment_year) {
		this.enrollment_year = enrollment_year;
	}

	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}

	public Integer getGrade_num() {
		return grade_num;
	}

	public void setGrade_num(Integer grade_num) {
		this.grade_num = grade_num;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getClass_list() {
		return class_list;
	}

	public void setClass_list(String class_list) {
		this.class_list = class_list;
	}
}
