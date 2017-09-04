package com.ninesky.classtao.homework.vo;


import java.util.Date;

import com.ninesky.common.vo.BaseVO;


public class HomeworkVO extends BaseVO{
	
	private Integer id;
	/**
	* 作业ID 主键
	*/
	private Integer homework_id;
	
	/**
	* 作业子项ID 
	*/
	private Integer item_id;
	
	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 发送者用户ID
	*/
	private Integer sender_id;

	/**
	* 发送者名字
	*/
	private String sender_name;
	
	/**
	* 课程类型
	*/
	private String course;

	/**
	* 作业标题
	*/
	private String title;

	/**
	* 作业内容
	*/
	private String content;
	
	/**
	* 作业规定完成日期
	*/
	private String end_date;
	
	/**
	 * 作业子条目内容（json数组格式）
	 */
	private String item_list;
	
	/**
	 * 作业提交统计：提交多少，未提交多少
	 */
	private String count_list;
	
	/**
	* 发送对象：班级id串 用逗号“,”隔开
	*/
	private String class_ids;

	private long send_time;
	
	private Integer is_submit;
	
	private Date create_date; 
	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(Integer is_submit) {
		this.is_submit = is_submit;
	}

	public long getSend_time() {
		return send_time;
	}

	public void setSend_time(long send_time) {
		this.send_time = send_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHomework_id() {
		return homework_id;
	}

	public void setHomework_id(Integer homeworkId) {
		homework_id = homeworkId;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer itemId) {
		item_id = itemId;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setSender_id(Integer sender_id)  {
		this.sender_id = sender_id;
	}

	public Integer getSender_id()  {
		return sender_id;
	}

	public void setSender_name(String sender_name)  {
		this.sender_name = sender_name;
	}

	public String getSender_name()  {
		return sender_name;
	}
	
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}
	
	public void setTitle(String title)  {
		this.title = title;
	}

	public String getTitle()  {
		return title;
	}

	public void setContent(String content)  {
		this.content = content;
	}

	public String getContent()  {
		return content;
	}
	
	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String itemList) {
		item_list = itemList;
	}
	
	public String getClass_ids() {
		return class_ids;
	}

	public void setClass_ids(String classIds) {
		class_ids = classIds;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String endDate) {
		end_date = endDate;
	}

	public String getCount_list() {
		return count_list;
	}

	public void setCount_list(String countList) {
		count_list = countList;
	}
}