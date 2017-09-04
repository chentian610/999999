package com.ninesky.classtao.homework.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class HomeworkItemDoneVO extends BaseVO{
	/**
	 * id
	 */
	private Integer id;
	/**
	* 消息ID，外键
	*/
	private Integer homework_id;
	
	/**
	* 学生ID，外键
	*/
	private Integer student_id;
	
	/**
	 * 作业子项id
	 */
	private Integer item_id;
	
	/**
	 * 子项目内容
	 */
	private String content;
	
	/**
	 * 是否：提交完成
	 */
	private Integer is_done;
	
	/**
	 * 提交完成时间
	 */
	private Date done_date;
	
	/**
	 * 作业附件列表
	 */
	private String file_list;
	
	public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String file_list) {
		this.file_list = file_list;
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

	public void setHomework_id(Integer homework_id) {
		this.homework_id = homework_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIs_done() {
		return is_done;
	}

	public void setIs_done(Integer is_done) {
		this.is_done = is_done;
	}

	public Date getDone_date() {
		return done_date;
	}

	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}
	
}
