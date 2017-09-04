package com.ninesky.classtao.user.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class UserVO extends BaseVO{


	/**
	* 用户ID，主键
	*/
	private Integer user_id;

	/**
	* 手机
	*/
	private String phone;

	/**
	* 用户类型
	*/
	private String user_type;

	/**
	* 状态:001
	*/
	private String status;

	/**
	* 密码，加密后
	*/
	private String pass_word;

	/**
	 * 最近一次修改密码的时间
	 */
	private Date password_update_time;

	/**
	* 姓名
	*/
	private String user_name;

	/**
	* 头像URL
	*/
	private String head_url;

	/**
	* 性别：0男1女
	*/
	private Integer sex;

	/**
	 * 教师角色列表
	 */
	private String teacher_list;
	
	/**
	 * 家长角色列表
	 */
	private String parent_list;
	/**
	 * 原来的密码
	 */
	private String pass_word_old;
	
	private Integer school_id;

	/**
	 * 权限校验token
	 */
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getPass_word_old() {
		return pass_word_old;
	}

	public void setPass_word_old(String pass_word_old) {
		this.pass_word_old = pass_word_old;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setPhone(String phone)  {
		this.phone = phone;
	}

	public Date getPassword_update_time() {
		return password_update_time;
	}

	public void setPassword_update_time(Date password_update_time) {
		this.password_update_time = password_update_time;
	}

	public String getPhone()  {
		return phone;
	}

	public void setPass_word(String pass_word)  {
		this.pass_word = pass_word;
	}

	public String getPass_word()  {
		return pass_word;
	}

	public void setUser_name(String user_name)  {
		this.user_name = user_name;
	}

	public String getUser_name()  {
		return user_name;
	}

	public void setHead_url(String head_url)  {
		this.head_url = head_url;
	}

	public String getHead_url()  {
		return head_url;
	}

	public void setSex(Integer sex)  {
		this.sex = sex;
	}

	public Integer getSex()  {
		return sex;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeacher_list() {
		return teacher_list;
	}

	public void setTeacher_list(String teacher_list) {
		this.teacher_list = teacher_list;
	}

	public String getParent_list() {
		return parent_list;
	}

	public void setParent_list(String parent_list) {
		this.parent_list = parent_list;
	}

}