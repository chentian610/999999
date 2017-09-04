package com.ninesky.classtao.login.vo;

import java.util.Date;

public class AppVersionVO {


	/**
	* 主键ID
	*/
	private Integer id;

	/**
	* APP编号
	*/
	private String app_code;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 用户类型:003
	*/
	private String user_type;

	/**
	* App类型：005
	*/
	private String app_type;

	/**
	* APP名称
	*/
	private String app_name;

	/**
	* App版本号
	*/
	private String app_version;

	/**
	* 更新地址
	*/
	private String update_url;

	/**
	* 更新说明
	*/
	private String update_msg;

	/**
	* 强制更新，1：强制更新
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

	/**
	* 未设置，请在数据库中设置
	*/
	private String kap_update_url;

	/**
	* 未设置，请在数据库中设置
	*/
	private Date ka_time;

	/**
	* 未设置，请在数据库中设置
	*/
	private Date kap_last_time;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer kap_state;

	/**
	* 未设置，请在数据库中设置
	*/
	private String kap_redactor;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer kap_type;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer kap_if_examine;

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setApp_code(String app_code)  {
		this.app_code = app_code;
	}

	public String getApp_code()  {
		return app_code;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public void setApp_type(String app_type)  {
		this.app_type = app_type;
	}

	public String getApp_type()  {
		return app_type;
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

	public void setKap_update_url(String kap_update_url)  {
		this.kap_update_url = kap_update_url;
	}

	public String getKap_update_url()  {
		return kap_update_url;
	}

	public void setKa_time(Date ka_time)  {
		this.ka_time = ka_time;
	}

	public Date getKa_time()  {
		return ka_time;
	}

	public void setKap_last_time(Date kap_last_time)  {
		this.kap_last_time = kap_last_time;
	}

	public Date getKap_last_time()  {
		return kap_last_time;
	}

	public void setKap_state(Integer kap_state)  {
		this.kap_state = kap_state;
	}

	public Integer getKap_state()  {
		return kap_state;
	}

	public void setKap_redactor(String kap_redactor)  {
		this.kap_redactor = kap_redactor;
	}

	public String getKap_redactor()  {
		return kap_redactor;
	}

	public void setKap_type(Integer kap_type)  {
		this.kap_type = kap_type;
	}

	public Integer getKap_type()  {
		return kap_type;
	}

	public void setKap_if_examine(Integer kap_if_examine)  {
		this.kap_if_examine = kap_if_examine;
	}

	public Integer getKap_if_examine()  {
		return kap_if_examine;
	}

}