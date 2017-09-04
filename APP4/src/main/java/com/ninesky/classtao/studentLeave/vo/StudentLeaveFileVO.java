package com.ninesky.classtao.studentLeave.vo;

import java.util.Date;

public class StudentLeaveFileVO {
	private Integer id;//主键ID
	private Integer leave_id;//请假申请的ID
	private String file_type;//文件类型
	private String file_name;//文件名
	private String file_url;//文件URL,绝对路径
	private String file_resize_url;//图片缩略图URL,绝对路径
	private Integer create_by;
	private Date create_date;
	private Integer version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public String getFile_type() {
		return file_type;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getFile_url() {
		return file_url;
	}

	public String getFile_resize_url() {
		return file_resize_url;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public void setFile_resize_url(String file_resize_url) {
		this.file_resize_url = file_resize_url;
	}

	public Integer getCreate_by() {
		return create_by;
	}

	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
