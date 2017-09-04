package com.ninesky.classtao.comment.vo;


import com.ninesky.common.vo.BaseVO;

public class CommentVO extends BaseVO{
	
	/**
	 * 评价id
	 */
	private Integer comment_id;
	
	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 班级ID
	*/
	private Integer class_id;
	
	/**
	 * 用户id
	 */
	private Integer user_id;
	/**
	 * 用户类型
	 */
	private String user_type;
	/**
	 * 评价人
	 */
	private String teacher_name;
	
	/**
	* 评语内容
	*/
	private String comment;
	
	/**
	* 扣分日期(YYYY-MM-DD)
	*/
	private String comment_date;
	
	/**
	* 学生ID
	*/
	private Integer student_id;
	
	/**
	* 学号
	*/
	private String student_code;

	/**
	* 学生姓名
	*/
	private String student_name;

	/**
	 * 头像
	 */
	private String head_url;
	
	/**
	 * 是否已读,0未读，1已读
	 * @return
	 */
	private Integer is_read;
	
	
	public Integer getComment_id() {
		return comment_id;
	}

	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getStudent_code() {
		return student_code;
	}

	public void setStudent_code(String student_code) {
		this.student_code = student_code;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public Integer getIs_read() {
		return is_read;
	}

	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
}