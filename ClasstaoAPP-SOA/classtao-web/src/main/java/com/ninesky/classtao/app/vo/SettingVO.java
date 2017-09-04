package com.ninesky.classtao.app.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class SettingVO extends BaseVO{

	/**
	* 主键
	*/
	private Integer id;
	/**
	* 用户ID，主键
	*/
	private Integer user_id;

	/**
	* 系统设置类型：018
	*/
	private String set_type;

	/**
	* 是否开启，0关闭，1开启
	*/
	private Integer is_open;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;
	
	/**
	* 更新
	*/
	private Integer update_by;
	
	/**
	* 更新日期
	*/
	private Date update_date;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setSet_type(String set_type)  {
		this.set_type = set_type;
	}

	public String getSet_type()  {
		return set_type;
	}

	public void setIs_open(Integer is_open)  {
		this.is_open = is_open;
	}

	public Integer getIs_open()  {
		return is_open;
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

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(Integer updateBy) {
		update_by = updateBy;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date updateDate) {
		update_date = updateDate;
	}
}