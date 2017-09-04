package com.ninesky.classtao.teacherDisk.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class DiskVO extends BaseVO {

	/**
	 * 学校ID
	 */
	private Integer school_id;
	
	/**
	 * 教师user_id
	 */
	private Integer user_id;
	
	/**
	 * 根目录ID
	 */
	private Integer parent_id;
	
	/**
	 * 主键
	 */
	private Integer file_id;
	
	/**
	 * 文件名称
	 */
	private String file_name;
	
	/**
	 * 文件路径地址
	 */
	private String file_url;
	
	/**
	 * 文件大小
	 */
	private String file_size;
	
	/**
	 * 是否公开（0代表公开，1代表私有）
	 */
	private Integer is_private;
	
	/**
	 * 修改日期
	 */
	private Date update_date;

	/**
	 * 文件类型
	 * @return
	 */
	private String file_type;
	
	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getFile_id() {
		return file_id;
	}

	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public Integer getIs_private() {
		return is_private;
	}

	public void setIs_private(Integer is_private) {
		this.is_private = is_private;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	
	
}
