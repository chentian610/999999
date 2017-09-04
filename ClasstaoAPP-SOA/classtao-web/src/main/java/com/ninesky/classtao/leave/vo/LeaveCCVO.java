package com.ninesky.classtao.leave.vo;

import com.ninesky.common.vo.BaseVO;

public class LeaveCCVO extends BaseVO{
	private Integer cc_id;//主键ID
	private Integer school_id;//学校ID
	private Integer leave_id;//请假申请的ID
	private Integer user_id;//用户ID
	private String phone;//电话号码
	private String head_url;//头像路径
	private String user_name;//替换名称

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_name() {

		return user_name;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public Integer getCc_id() {
		return cc_id;
	}

	public void setCc_id(Integer cc_id) {
		this.cc_id = cc_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
}
