package com.ninesky.classtao.picture.vo;


import com.ninesky.common.vo.BaseVO;

public class PictureVO extends BaseVO{


	/**
	* 照片ID
	*/
	private Integer picture_id;

	/**
	* 学校id
	*/
	private Integer school_id;

	/**
	* 级班ID
	*/
	private Integer class_id;

	/**
	* 相册类型：006
	*/
	private String picture_type;

	/**
	* 标题
	*/
	private String title;

	/**
	* 照片URL
	*/
	private String picture_url;

	/**
	* 照片缩略图
	*/
	private String picture_resize_url;

	/**
	* 添加日期
	*/
	private String add_date;
	
	/**
	 * 照片url数组
	 */
	private String file_list;
	
	public void setPicture_id(Integer picture_id)  {
		this.picture_id = picture_id;
	}

	public Integer getPicture_id()  {
		return picture_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setClass_id(Integer class_id)  {
		this.class_id = class_id;
	}

	public Integer getClass_id()  {
		return class_id;
	}

	public void setPicture_type(String picture_type)  {
		this.picture_type = picture_type;
	}

	public String getPicture_type()  {
		return picture_type;
	}

	public void setTitle(String title)  {
		this.title = title;
	}

	public String getTitle()  {
		return title;
	}

	public void setPicture_url(String picture_url)  {
		this.picture_url = picture_url;
	}

	public String getPicture_url()  {
		return picture_url;
	}

	public void setPicture_resize_url(String picture_resize_url)  {
		this.picture_resize_url = picture_resize_url;
	}

	public String getPicture_resize_url()  {
		return picture_resize_url;
	}

	public void setAdd_date(String add_date)  {
		this.add_date = add_date;
	}

	public String getAdd_date()  {
		return add_date;
	}

	public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String file_list) {
		this.file_list = file_list;
	}

}