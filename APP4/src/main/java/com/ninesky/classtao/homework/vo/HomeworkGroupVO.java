package com.ninesky.classtao.homework.vo;


import com.ninesky.common.vo.BaseVO;

public class HomeworkGroupVO extends BaseVO{


	/**
	* 主键ID
	*/
	private Integer id;

	/**
	* 通知ID,外键
	*/
	private Integer homework_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 年级ID
	*/
	private Integer group_id;

	/**
	* 团队类型：011
	*/
	private String team_type;

	/**
	* 班级ID,或寝室ID
	*/
	private Integer team_id;

	/**
	* 接收者user_id(校务通知发给特定教师)
	*/
	private Integer user_id;

	/**
	* 用户类型：003
	*/
	private String user_type;
	
	/**
	 * 子项目的内容
	 */
	private String item_list;
	
	private String end_date;
	/**
	 * 科目code
	 */
	private String course;
		
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String item_list) {
		this.item_list = item_list;
	}

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setHomework_id(Integer homework_id)  {
		this.homework_id = homework_id;
	}

	public Integer getHomework_id()  {
		return homework_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setGroup_id(Integer group_id)  {
		this.group_id = group_id;
	}

	public Integer getGroup_id()  {
		return group_id;
	}

	public void setTeam_type(String team_type)  {
		this.team_type = team_type;
	}

	public String getTeam_type()  {
		return team_type;
	}

	public void setTeam_id(Integer team_id)  {
		this.team_id = team_id;
	}

	public Integer getTeam_id()  {
		return team_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}
}