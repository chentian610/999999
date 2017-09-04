package com.ninesky.classtao.photo.vo;


import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class PhotoVO extends BaseVO{


	/**
	* 照片ID
	*/
	private Integer photo_id;
	
	/**
	 * 学校id
	 */
	private Integer school_id;

	/**
	* 级班ID
	*/
	private Integer class_id;
	
	/**
	* 学生ID
	*/
	private Integer student_id;
	
	/**
	 * 用户id
	 */
	private Integer user_id;
	
	/**
	 * 用户类型
	 */
	private String user_type;
	
	/**
	* 照片类型：006
	*/
	private String photo_type;

	/**
	* 照片URL
	*/
	private String photo_url;
	
	/**
	* 照片缩略图URL
	*/
	private String photo_resize_url;

	/**
	* 建创日期("yyyy-M-d")
	*/
	private String add_date;
	
	/**
	 * 照片id串:逗号隔开
	 */
	private String photo_ids;
	
	private Integer sender_id;
	
	private Date send_time;

	private String team_type;

	private Date create_date;

	private Integer point_praise;//点赞

	private String comment_list;

	private String photo_list;

	private Integer point_praise_total;

	public Integer getPoint_praise_total() {
		return point_praise_total;
	}

	public void setPoint_praise_total(Integer point_praise_total) {
		this.point_praise_total = point_praise_total;
	}

	public String getPhoto_list() {
		return photo_list;
	}

	public void setPhoto_list(String photo_list) {
		this.photo_list = photo_list;
	}

	public String getComment_list() {
		return comment_list;
	}

	public void setComment_list(String comment_list) {
		this.comment_list = comment_list;
	}

	public Integer getPoint_praise() {
		return point_praise;
	}

	public void setPoint_praise(Integer point_praise) {
		this.point_praise = point_praise;
	}

	@Override
	public Date getCreate_date() {
		return create_date;
	}

	@Override
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}
	
	public void setPhoto_id(Integer photo_id)  {
		this.photo_id = photo_id;
	}

	public Integer getPhoto_id()  {
		return photo_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setClass_id(Integer class_id)  {
		this.class_id = class_id;
	}

	public Integer getClass_id()  {
		return class_id;
	}

	public void setStudent_id(Integer student_id)  {
		this.student_id = student_id;
	}

	public Integer getStudent_id()  {
		return student_id;
	}

	public void setPhoto_url(String photo_url)  {
		this.photo_url = photo_url;
	}

	public String getPhoto_url()  {
		return photo_url;
	}

	public String getPhoto_type() {
		return photo_type;
	}

	public void setPhoto_type(String photo_type) {
		this.photo_type = photo_type;
	}

	public String getAdd_date() {
		return add_date;
	}

	public void setAdd_date(String addDate) {
		add_date = addDate;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer userId) {
		user_id = userId;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String userType) {
		user_type = userType;
	}

	public String getPhoto_resize_url() {
		return photo_resize_url;
	}

	public void setPhoto_resize_url(String photo_resize_url) {
		this.photo_resize_url = photo_resize_url;
	}

	public String getPhoto_ids() {
		return photo_ids;
	}

	public void setPhoto_ids(String photo_ids) {
		this.photo_ids = photo_ids;
	}
	
}