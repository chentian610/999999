package com.ninesky.classtao.score.vo;

import java.util.Date;

public class TableHeadVO {


	/**
	* 主键，自增长
	*/
	private Integer id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 团队类型，011
	*/
	private String team_type;

	/**
	* 打分类型：012
	*/
	private String score_type;
	
	/**
	* 统计对象类型：028
	*/
	private String count_type;

	/**
	* 考情项目：014，非考情为空
	*/
	private String attend_item;

	/**
	* 列字段
	*/
	private String field;

	/**
	* 列名称
	*/
	private String field_name;

	/**
	* 列计算函数
	*/
	private String field_func;
	
	/**
	* 列计算函数(合计)
	*/
	private String field_func_sum;
	
	/**
	* 列底部函数
	*/
	private String func_type;

	/**
	* 排序
	*/
	private Integer sort;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新日期
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
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

	public void setAttend_item(String attend_item)  {
		this.attend_item = attend_item;
	}

	public String getAttend_item()  {
		return attend_item;
	}

	public void setField(String field)  {
		this.field = field;
	}

	public String getField()  {
		return field;
	}

	public void setField_name(String field_name)  {
		this.field_name = field_name;
	}

	public String getField_name()  {
		return field_name;
	}

	public void setField_func(String field_func)  {
		this.field_func = field_func;
	}

	public String getField_func()  {
		return field_func;
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

	public void setUpdate_by(Integer update_by)  {
		this.update_by = update_by;
	}

	public Integer getUpdate_by()  {
		return update_by;
	}

	public void setUpdate_date(Date update_date)  {
		this.update_date = update_date;
	}

	public Date getUpdate_date()  {
		return update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public String getCount_type() {
		return count_type;
	}

	public void setCount_type(String count_type) {
		this.count_type = count_type;
	}

	public String getFunc_type() {
		return func_type;
	}

	public void setFunc_type(String func_type) {
		this.func_type = func_type;
	}

	public String getField_func_sum() {
		return field_func_sum;
	}

	public void setField_func_sum(String field_func_sum) {
		this.field_func_sum = field_func_sum;
	}

}