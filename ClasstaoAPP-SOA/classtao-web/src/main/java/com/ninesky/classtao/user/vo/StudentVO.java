package com.ninesky.classtao.user.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

/**
 * 学生实体类
 * @author user
 *
 */
public class StudentVO extends BaseVO{
	
	private Integer student_id;
	private Integer user_id;
	private Integer school_id;
	private String student_code;//学号
	private Integer enrollment_year;
	private Integer grade_id;
	private Integer class_id;
	private Integer class_num;
	private String class_name;//班级名称
	
	private String student_name;//学生姓名
	
	private Integer sex;//性别（0，1）
	
	private String sex_name;//性别（男，女）
	
	private String head_url;//头像路径
	
	private String first_letter;//姓名首字母H
	
	private String all_letter;//姓名首字母全拼HMM
	
	private String parent_list;//该学生的家长列表

	private String team_list;//该学生的班级列表

	private String user_type;
	/**
	 * 更新者
	 */
	private Integer update_by;
	
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	private Integer version;

	public Integer getClass_num() {
		return class_num;
	}

	public void setClass_num(Integer class_num) {
		this.class_num = class_num;
	}

	public Integer getEnrollment_year() {
		return enrollment_year;
	}

	public void setEnrollment_year(Integer enrollment_year) {
		this.enrollment_year = enrollment_year;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public String getStudent_code() {
		return student_code;
	}
	public void setStudent_code(String student_code) {
		this.student_code = student_code;
	}
	
	public Integer getGrade_id() {
		return grade_id;
	}
	public void setGrade_id(Integer gradeId) {
		grade_id = gradeId;
	}
	public Integer getClass_id() {
		return class_id;
	}
	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	public String getSex_name() {
		return sex_name;
	}
	public void setSex_name(String sex_name) {
		this.sex_name = sex_name;
	}
	public String getHead_url() {
		return head_url;
	}
	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	public String getFirst_letter() {
		return first_letter;
	}
	public void setFirst_letter(String first_letter) {
		this.first_letter = first_letter;
	}
	public String getAll_letter() {
		return all_letter;
	}
	public void setAll_letter(String all_letter) {
		this.all_letter = all_letter;
	}
	public String getParent_list() {
		return parent_list;
	}
	public void setParent_list(String parent_list) {
		this.parent_list = parent_list;
	}

	public String getTeam_list() {
		return team_list;
	}

	public void setTeam_list(String team_list) {
		this.team_list = team_list;
	}

	public Integer getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}