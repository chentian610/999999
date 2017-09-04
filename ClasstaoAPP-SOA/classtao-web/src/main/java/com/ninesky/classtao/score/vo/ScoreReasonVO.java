package com.ninesky.classtao.score.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class ScoreReasonVO extends BaseVO{
	
	private Integer id;

	private String score_code;
	
	/**
	* 团队类型（寝室还是班级）011
	*/
	private String team_type;

	/**
	* 扣分类型（卫生还是纪律）012
	*/
	private String score_type;

	/**
	* 扣分原因
	*/
	private String score_reason;

	/**
	* 扣分描述
	*/
	private String description;

	/**
	* 分值
	*/
	private Integer score;
	
	/**
	 * 学校ID
	 */
	private Integer school_id;

	/**
	* 排序
	*/
	private Integer sort;
	
	/**
	 * 是否启用
	 */
	private Integer is_active;

	/**
	 * 模块code
	 */
	private String module_code;
	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建时间
	*/
	private Date create_date;

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScore_code() {
		return score_code;
	}

	public void setScore_code(String score_code) {
		this.score_code = score_code;
	}

	public void setTeam_type(String team_type)  {
		this.team_type = team_type;
	}

	public String getTeam_type()  {
		return team_type;
	}

	public void setScore_type(String score_type)  {
		this.score_type = score_type;
	}

	public String getScore_type()  {
		return score_type;
	}

	public void setScore_reason(String score_reason)  {
		this.score_reason = score_reason;
	}

	public String getScore_reason()  {
		return score_reason;
	}

	public void setDescription(String description)  {
		this.description = description;
	}

	public String getDescription()  {
		return description;
	}

	public void setScore(Integer score)  {
		this.score = score;
	}

	public Integer getScore()  {
		return score;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setSort(Integer sort)  {
		this.sort = sort;
	}

	public Integer getSort()  {
		return sort;
	}

	public void setCreate_by(Integer create_by)  {
		this.create_by = create_by;
	}

	public Integer getCreate_by()  {
		return create_by;
	}

	public void setCreate_date(Date create_date)  {
		this.create_date = create_date;
	}

	public Date getCreate_date()  {
		return create_date;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}
}