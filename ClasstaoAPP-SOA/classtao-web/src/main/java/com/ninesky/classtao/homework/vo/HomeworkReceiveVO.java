package com.ninesky.classtao.homework.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class HomeworkReceiveVO extends BaseVO{

	/**
	 * 子项表主键
	 */
	private Integer id;
	
	/**
	 * 接收表主键
	 */
	private Integer receive_id;
	
	/**
	* 作业ID，外键
	*/
	private Integer homework_id;
	
	/**
	 * 作业课程类型
	 */
	private String course;
	
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
	* 接收者学号
	*/
	private Integer student_id;

	/**
	* 接收者姓名
	*/
	private String student_name;
	
	/**
	 * 学生头像Url
	 */
	private String head_url;

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
	 * 是否已读
	 */
	private Integer is_read;
	
	/**
	* 是否已提交，0未提交，1已提交
	*/
	private Integer is_submit;

	/**
	* 提交时间
	*/
	private Date submit_time;
	
	/**
	 * 提交作业的照片url串，逗号隔开
	 */
	private String photo_list;
	
	/**
	* 是否已完成，0未完成，1已完成
	*/
	private Integer is_done;

	/**
	* 完成时间
	*/
	private Date done_time;
	
	/**
	 * 作业附件列表
	 */
	private String file_list;
	/**
	 * 作业子项json列表
	 */
	private String item_list;
	
	private String count_list;

	public String getCount_list() {
		return count_list;
	}

	public void setCount_list(String count_list) {
		this.count_list = count_list;
	}

	private long send_time;

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
	
	public Integer getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(Integer receiveId) {
		receive_id = receiveId;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer itemId) {
		item_id = itemId;
	}

	public Integer getIs_done() {
		return is_done;
	}

	public void setIs_done(Integer isDone) {
		is_done = isDone;
	}

	public Date getDone_time() {
		return done_time;
	}

	public void setDone_time(Date doneTime) {
		done_time = doneTime;
	}

	public void setHomework_id(Integer homework_id)  {
		this.homework_id = homework_id;
	}

	public Integer getHomework_id()  {
		return homework_id;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
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

	public void setStudent_id(Integer student_id)  {
		this.student_id = student_id;
	}

	public Integer getStudent_id()  {
		return student_id;
	}

	public void setStudent_name(String student_name)  {
		this.student_name = student_name;
	}

	public String getStudent_name()  {
		return student_name;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
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

	public void setIs_submit(Integer is_submit)  {
		this.is_submit = is_submit;
	}

	public Integer getIs_submit()  {
		return is_submit;
	}

	public void setSubmit_time(Date submit_time)  {
		this.submit_time = submit_time;
	}

	public Date getSubmit_time()  {
		return submit_time;
	}

	public String getPhoto_list() {
		return photo_list;
	}

	public void setPhoto_list(String photo_list) {
		this.photo_list = photo_list;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String endDate) {
		end_date = endDate;
	}

	public Integer getIs_read() {
		return is_read;
	}

	public void setIs_read(Integer isRead) {
		is_read = isRead;
	}

	public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String fileList) {
		file_list = fileList;
	}

	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String itemList) {
		item_list = itemList;
	}
	
}