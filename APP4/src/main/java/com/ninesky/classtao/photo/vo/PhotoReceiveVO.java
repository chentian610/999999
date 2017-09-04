package com.ninesky.classtao.photo.vo;

import com.ninesky.common.vo.BaseVO;


public class PhotoReceiveVO extends BaseVO{

	/**
	 * 主键
	 */
	private Integer id;
	/**
	* 照片ID
	*/
	private Integer photo_id;

	/**
	* 接收人UerID
	*/
	private Integer user_id;

	/**
	* 用户类型：003
	*/
	private String user_type;
	
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
	* 相册类型：006
	*/
	private String photo_type;

	/**
	* 照片URL
	*/
	private String photo_url;
	
	/**
	 * 照片缩略图url
	 */
	private String photo_resize_url;
	
	/**
	* 添加日期
	*/
	private String add_date;

	/**
	* 是否已读,0未读，1已读
	*/
	private Integer is_read;

	private String team_type;

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPhoto_id(Integer photo_id)  {
		this.photo_id = photo_id;
	}

	public Integer getPhoto_id()  {
		return photo_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
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

	public void setPhoto_type(String photo_type)  {
		this.photo_type = photo_type;
	}

	public String getPhoto_type()  {
		return photo_type;
	}

	public void setPhoto_url(String photo_url)  {
		this.photo_url = photo_url;
	}

	public String getPhoto_url()  {
		return photo_url;
	}
	
	public String getPhoto_resize_url() {
		return photo_resize_url;
	}

	public void setPhoto_resize_url(String photoResizeUrl) {
		photo_resize_url = photoResizeUrl;
	}

	public void setAdd_date(String add_date)  {
		this.add_date = add_date;
	}

	public String getAdd_date()  {
		return add_date;
	}

	public void setIs_read(Integer is_read)  {
		this.is_read = is_read;
	}

	public Integer getIs_read()  {
		return is_read;
	}
}