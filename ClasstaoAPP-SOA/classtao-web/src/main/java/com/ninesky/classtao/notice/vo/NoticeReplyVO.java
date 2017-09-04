package com.ninesky.classtao.notice.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class NoticeReplyVO extends BaseVO{

	private Integer reply_id;
	
	/**
	* 通知接收者ID，user_id或student_id
	*/
	private Integer receive_id;
	
	/**
	 * 通知的接收者（学生或教师）
	 */
	private String receive_type;

	/**
	* 消息ID，外键
	*/
	private Integer notice_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	 * 学生ID，0代表不是学生
	 */
	private Integer student_id;
	
	/**
	* 回复人ID
	*/
	private Integer user_id;

	/**
	 * 接收人姓名
	 */
	private String receive_name;
	/**
	* 用户类型：003
	*/
	private String user_type;

	/**
	* 回复内容
	*/
	private String reply_content;

	/**
	* 回复时间
	*/
	private Date reply_time;

	/**
	 * 回复人姓名
	 */
	private String reply_name;
	
	/**
	 * 回复人头像url
	 */
	private String head_url;
	
	/**
	 * 未读数
	 */
	private Integer count;
	
	/**
	 * 发送该条通知的user_id
	 */
	private Integer sender_id;

	public String getReceive_type() {
		return receive_type;
	}

	public void setReceive_type(String receive_type) {
		this.receive_type = receive_type;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}

	public Integer getReply_id() {
		return reply_id;
	}

	public void setReply_id(Integer reply_id) {
		this.reply_id = reply_id;
	}

	public void setReceive_id(Integer receive_id)  {
		this.receive_id = receive_id;
	}

	public Integer getReceive_id()  {
		return receive_id;
	}

	public void setNotice_id(Integer notice_id)  {
		this.notice_id = notice_id;
	}

	public Integer getNotice_id()  {
		return notice_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getReceive_name() {
		return receive_name;
	}

	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public void setReply_content(String reply_content)  {
		this.reply_content = reply_content;
	}

	public String getReply_content()  {
		return reply_content;
	}

	public void setReply_time(Date reply_time)  {
		this.reply_time = reply_time;
	}

	public Date getReply_time()  {
		return reply_time;
	}

	public String getReply_name() {
		return reply_name;
	}

	public void setReply_name(String reply_name) {
		this.reply_name = reply_name;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}