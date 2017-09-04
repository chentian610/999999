package com.ninesky.classtao.school.vo;

public class GroupVO {

	/**
	 * 用户类型
	 */
	private String user_type;
	
	/**
	 * 分组类型
	 */
	private String group_type;
	
	/**
	 * 自定义分组ID
	 */
	private Integer group_id;
	
	/**
	 * 分组名称
	 */
	private String group_name;
	
	/**
	 * 年级ID
	 */
	private Integer grade_id;
	
	/**
	 * 班级ID
	 */
	private Integer class_id;
	
	/**
	 * 教师职务分组
	 */
	private String duty;

	/**
	 * 该身份教师人数
	 */
	private Integer count;

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getGroup_type() {
		return group_type;
	}

	public void setGroup_type(String group_type) {
		this.group_type = group_type;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
