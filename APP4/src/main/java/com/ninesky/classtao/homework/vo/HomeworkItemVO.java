package com.ninesky.classtao.homework.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * 作业子项实体类
 *
 */
public class HomeworkItemVO extends BaseVO{
	
	/**
	 * 子项id
	 */
	private Integer item_id;
	/**
	 * 作业id
	 */
	private Integer homework_id;
	/**
	 *	子项标题
	 */
	private String title;
	/**
	 *	子项内容
	 */
	private String content;
	
	/**
	 * 附件列表
	 */
	private String file_list;
	/**
	 * 作业截止日期
	 */
	private String end_date;
	
	private String course;
	
	private String sender_name;
	
	private long send_time;
	
	private Integer is_done;
	
	private String done_time;
	
	private Integer student_id;
	
	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getDone_time() {
		return done_time;
	}

	public void setDone_time(String done_time) {
		this.done_time = done_time;
	}

	public Integer getIs_done() {
		return is_done;
	}

	public void setIs_done(Integer is_done) {
		this.is_done = is_done;
	}

	public long getSend_time() {
		return send_time;
	}

	public void setSend_time(long send_time) {
		this.send_time = send_time;
	}
	
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFile_list() {
		return file_list;
	}
	public void setFile_list(String file_list) {
		this.file_list = file_list;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
}
