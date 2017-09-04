package com.ninesky.classtao.user.vo;

import java.util.Date;

public class ValidateCodeVO {


	/**
	* 未设置，请在数据库中设置
	*/
	private long id;

	/**
	* 未设置，请在数据库中设置
	*/
	private String phone;

	/**
	* 未设置，请在数据库中设置
	*/
	private String validate_code;
	
	/**
	* 是否被使用
	*/
	private Integer is_use;

	/**
	* 未设置，请在数据库中设置
	*/
	private String create_by;

	/**
	* 未设置，请在数据库中设置
	*/
	private Date create_date;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer update_by;

	/**
	* 未设置，请在数据库中设置
	*/
	private Date update_date;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer version;

	public void setId(long id)  {
		this.id = id;
	}

	public long getId()  {
		return id;
	}

	public void setPhone(String phone)  {
		this.phone = phone;
	}

	public String getPhone()  {
		return phone;
	}

	public void setValidate_code(String validate_code)  {
		this.validate_code = validate_code;
	}

	public String getValidate_code()  {
		return validate_code;
	}

	public void setCreate_by(String create_by)  {
		this.create_by = create_by;
	}

	public String getCreate_by()  {
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

	public Integer getIs_use() {
		return is_use;
	}

	public void setIs_use(Integer is_use) {
		this.is_use = is_use;
	}

}