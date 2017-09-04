package com.ninesky.classtao.school.vo;

/**
 * 团队实体(班级,寝室,兴趣班,自定义等)
 * @author TOOTU
 *
 */
public class TeamVO{
	
	/**
	 * 学校ID
	 */
	private Integer school_id;
	
	/**
	 * 团队所属年级
	 */
	private Integer grade_id;
	
	/**
	 * 团队ID(班级,寝室,兴趣班,自定义等)
	 */
	private Integer class_id;
	
	/**
	 * 团队名称
	 */
	private String class_name;
	
	/**
	 * 团队类型(班级,寝室,兴趣班,自定义等)
	 */
	private String team_type;
	
	/**
	 * 是/否已经毕业
	 */
	private Integer is_graduate;

	private String user_type;

	private Integer team_id;

	private Integer group_id;

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_type() {
		return user_type;
	}

	/**
	 * 班级人数
	 */
	private Integer count;
	
	public Integer getIs_graduate() {
		return is_graduate;
	}

	public void setIs_graduate(Integer is_graduate) {
		this.is_graduate = is_graduate;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
