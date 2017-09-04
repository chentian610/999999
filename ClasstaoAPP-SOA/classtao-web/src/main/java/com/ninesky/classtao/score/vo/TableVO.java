package com.ninesky.classtao.score.vo;

public class TableVO {
	/**
	 * 学校ID
	 */
	private Integer school_id;
	/**
	* 团队类型，011
	*/
	private String team_type;

	/**
	* 打分类型
	*/
	private String score_type;

	/**
	* 考情项目：014，非考勤为空
	*/
	private String attend_item;
	
	/**
	* 统计类型：按天、按周、按月、按学期：027
	*/
	private String sum_type;
	
	
	/**
	* 统计对象：统计学生、统计班级
	*/
	private String count_type;
	
	/**
	* 年级或者楼层ID
	*/
	private Integer group_id;

	/**
	* 班级或者寝室ID
	*/
	private Integer team_id;

	/**
	* 列计算函数
	*/
	private Integer student_id;
	

	/**
	* 日期，查询日期条件所在的第一天。
	* 按周统计：当前周的第一天
	* 按月统计：当前月的1日
	* 按年统计：当前年的1月1日
	*/
	private String score_date;
	
	/**
	* 字段函数
	*/
	private String field_list;
	
	/**
	 * 用于仪表盘，首页或详情页
	 */
	private boolean is_detail;

	private String module_code;

	private String start_date;

	private String end_date;

	public boolean is_detail() {
		return is_detail;
	}

	public void setIs_detail(boolean is_detail) {
		this.is_detail = is_detail;
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

	public String getScore_type() {
		return score_type;
	}

	public void setScore_type(String score_type) {
		this.score_type = score_type;
	}

	public String getAttend_item() {
		return attend_item;
	}

	public void setAttend_item(String attend_item) {
		this.attend_item = attend_item;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getCount_type() {
		return count_type;
	}

	public void setCount_type(String count_type) {
		this.count_type = count_type;
	}

	public String getScore_date() {
		return score_date;
	}

	public void setScore_date(String score_date) {
		this.score_date = score_date;
	}

	public String getField_list() {
		return field_list;
	}

	public void setField_list(String field_list) {
		this.field_list = field_list;
	}

	public String getSum_type() {
		return sum_type;
	}

	public void setSum_type(String sum_type) {
		this.sum_type = sum_type;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
}