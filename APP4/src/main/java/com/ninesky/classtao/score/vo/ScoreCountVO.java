package com.ninesky.classtao.score.vo;


import com.ninesky.common.vo.BaseVO;

public class ScoreCountVO extends BaseVO{
	private Integer school_id;
	private Integer group_id;
	private Integer team_id;
	private String score_type;
	private String team_type;
	private String start_date;
	private String end_date;
	private Integer sort;
	private String module_code;
	private String count_type;
	private String sum_type;
	private Integer student_id;

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
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

	public String getScore_type() {
		return score_type;
	}

	public void setScore_type(String score_type) {
		this.score_type = score_type;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getCount_type() {
		return count_type;
	}

	public void setCount_type(String count_type) {
		this.count_type = count_type;
	}

	public String getSum_type() {
		return sum_type;
	}

	public void setSum_type(String sum_type) {
		this.sum_type = sum_type;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
}
