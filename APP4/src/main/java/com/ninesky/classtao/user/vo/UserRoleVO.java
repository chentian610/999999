package com.ninesky.classtao.user.vo;


import com.ninesky.common.vo.BaseVO;

public class UserRoleVO extends BaseVO{


	/**
	* 未设置，请在数据库中设置
	*/
	private Integer id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 手机号码
	*/
	private String phone;

	/**
	* 用户类型：003
	*/
	private String user_type;


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

	public void setPhone(String phone)  {
		this.phone = phone;
	}

	public String getPhone()  {
		return phone;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public UserRoleVO() {}

	public UserRoleVO(Integer school_id) {
		this.school_id = school_id;
	}
}