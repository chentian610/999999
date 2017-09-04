package com.ninesky.classtao.score.vo;

import java.util.Date;

public class AttendCodeVO {
	
	private Integer id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 考勤项目code
	*/
	private String attend_code;

	/**
	* 考勤项目名称
	*/
	private String attend_name;

	/**
	* 项目描述
	*/
	private String description;

	/**
	* 排序
	*/
	private Integer sort;
	
	/**
	 * 是否启用
	 */
	private Integer is_active;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建时间
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新时间
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

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

	public void setAttend_code(String attend_code)  {
		this.attend_code = attend_code;
	}

	public String getAttend_code()  {
		return attend_code;
	}

	public void setAttend_name(String attend_name)  {
		this.attend_name = attend_name;
	}

	public String getAttend_name()  {
		return attend_name;
	}

	public void setDescription(String description)  {
		this.description = description;
	}

	public String getDescription()  {
		return description;
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

}