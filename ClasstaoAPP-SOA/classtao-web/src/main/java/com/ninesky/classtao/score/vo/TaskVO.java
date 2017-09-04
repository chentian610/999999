package com.ninesky.classtao.score.vo;

public class TaskVO {

	/**
	* 职务：016
	*/
	private String duty;
	
	/**
	 * 教师手机号
	 */
	private String phone;
	
	/**
	 * 老师姓名
	 */
	private String teacher_name;
	
	/**
	* 学校ID
	*/
	private Integer school_id;
	
	/**
	* 团队类型，012
	*/
	private String team_type;
	
	/**
	* 团队ID（对应教室class_id或者寝室bedroom_id）
	*/
	private Integer team_id;
	

	/**
	* 团队名
	*/
	private String team_name;
	
	/**
	* 打分类型
	*/
	private String score_type;

	/**
	* 扣分日期(YYYY-MM-DD)
	*/
	private String score_date;

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public String getScore_type() {
		return score_type;
	}

	public void setScore_type(String score_type) {
		this.score_type = score_type;
	}

	public String getScore_date() {
		return score_date;
	}

	public void setScore_date(String score_date) {
		this.score_date = score_date;
	}
	

	
}
