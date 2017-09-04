package com.ninesky.classtao.leave.vo;

import com.ninesky.common.vo.BaseVO;

public class LeaveChangeVO extends BaseVO{
	private Integer change_id;//id
	private Integer leave_id;//请假申请的ID
	private Integer school_id;//学校的ID
	private Integer user_id;//调课老师的ID
	private String user_name;//调课老师的名称
	private Integer is_agree;//是否同意调课
	private String phone;//电话号码
	private String content;//调课老师的留言

	public Integer getChange_id() {
		return change_id;
	}

	public void setChange_id(Integer change_id) {
		this.change_id = change_id;
	}

	/**
	* 头像URL
	*/
	private String head_url;
	public String getHead_url() {
		return head_url;
	}
	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Integer getLeave_id() {
		return leave_id;
	}
	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getIs_agree() {
		return is_agree;
	}
	public void setIs_agree(Integer is_agree) {
		this.is_agree = is_agree;
	}
}
