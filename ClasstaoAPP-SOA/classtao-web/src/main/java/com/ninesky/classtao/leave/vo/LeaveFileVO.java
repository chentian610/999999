package com.ninesky.classtao.leave.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class LeaveFileVO extends BaseVO{
	private Integer file_id;//主键ID
	private Integer school_id;//学校ID
	private Integer leave_id;//请假申请的ID
	private String file_type;//文件类型
	private String file_name;//文件名
	private String file_url;//文件URL,绝对路径
	private String file_resize_url;//图片缩略图URL,绝对路径
	private Integer play_time;//播放时间长度  单位：秒

	public Integer getFile_id() {
		return file_id;
	}

	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
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

	public Integer getPlay_time() {
		return play_time;
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

	public void setPlay_time(Integer play_time) {
		this.play_time = play_time;
	}
}
