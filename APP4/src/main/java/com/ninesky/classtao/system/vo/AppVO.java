package com.ninesky.classtao.system.vo;

import java.util.Date;

public class AppVO {


	/**
	* 未设置，请在数据库中设置
	*/
	private Integer id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* App类型：005
	*/
	private String app_type;

	/**
	* APP编号
	*/
	private String app_code;

	/**
	* APP名称
	*/
	private String app_name;

	/**
	* App版本号
	*/
	private String app_version;


	/**
	 * 是否整体更新：1是，0，局部
	 */
	private Integer is_all;
	
	/**
	* 更新地址
	*/
	private String update_url;

	/**
	* 更新说明
	*/
	private String update_msg;

	/**
	* 强制更新，1未强制更新
	*/
	private Integer must_update;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;

	/**
	*  更新者
	*/
	private Integer update_by;

	/**
	*  更新日期
	*/
	private Date update_date;

	/**
	*  版本号
	*/
	private Integer version;

	private Integer is_disable;

	private String update_phone;

	public void setUpdate_phone(String update_phone) {
		this.update_phone = update_phone;
	}

	public String getUpdate_phone() {
		return update_phone;
	}

	public void setIs_disable(Integer is_disable) {
		this.is_disable = is_disable;
	}

	public Integer getIs_disable() {
		return is_disable;
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

	public void setApp_type(String app_type)  {
		this.app_type = app_type;
	}

	public String getApp_type()  {
		return app_type;
	}

	public void setApp_code(String app_code)  {
		this.app_code = app_code;
	}

	public String getApp_code()  {
		return app_code;
	}

	public void setApp_name(String app_name)  {
		this.app_name = app_name;
	}

	public String getApp_name()  {
		return app_name;
	}

	public void setApp_version(String app_version)  {
		this.app_version = app_version;
	}

	public String getApp_version()  {
		return app_version;
	}

	public void setUpdate_url(String update_url)  {
		this.update_url = update_url;
	}

	public String getUpdate_url()  {
		return update_url;
	}

	public void setUpdate_msg(String update_msg)  {
		this.update_msg = update_msg;
	}

	public String getUpdate_msg()  {
		return update_msg;
	}

	public void setMust_update(Integer must_update)  {
		this.must_update = must_update;
	}

	public Integer getMust_update()  {
		return must_update;
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

	public Integer getIs_all() {
		return is_all;
	}

	public void setIs_all(Integer is_all) {
		this.is_all = is_all;
	}

}