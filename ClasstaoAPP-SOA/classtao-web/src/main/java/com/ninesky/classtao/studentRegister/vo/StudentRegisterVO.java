package com.ninesky.classtao.studentRegister.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class StudentRegisterVO extends BaseVO{


	/**
	* 主键
	*/
	private Integer register_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	 * 报到年份
	 */
	private Integer enrollment_year;

	/**
	* 报到学生姓名
	*/
	private String student_name;

	/**
	* 报到学生性别
	*/
	private Integer sex;

	/**
	* 身份证号码
	*/
	private String id_number;

	/**
	* 就读初中学校
	*/
	private String middle_school;

	/**
	* 是否住宿
	*/
	private Integer is_accommodate;

	/**
	 * 报到人数
	 */
	private Integer count;

	public void setRegister_id(Integer register_id)  {
		this.register_id = register_id;
	}

	public Integer getRegister_id()  {
		return register_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public Integer getEnrollment_year() {
		return enrollment_year;
	}

	public void setEnrollment_year(Integer enrollment_year) {
		this.enrollment_year = enrollment_year;
	}

	public void setStudent_name(String student_name)  {
		this.student_name = student_name;
	}

	public String getStudent_name()  {
		return student_name;
	}

	public void setSex(Integer sex)  {
		this.sex = sex;
	}

	public Integer getSex()  {
		return sex;
	}

	public void setId_number(String id_number)  {
		this.id_number = id_number;
	}

	public String getId_number()  {
		return id_number;
	}

	public void setMiddle_school(String middle_school)  {
		this.middle_school = middle_school;
	}

	public String getMiddle_school()  {
		return middle_school;
	}

	public void setIs_accommodate(Integer is_accommodate)  {
		this.is_accommodate = is_accommodate;
	}

	public Integer getIs_accommodate()  {
		return is_accommodate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}