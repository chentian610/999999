package com.ninesky.classtao.teacherDisk.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class DiskReceiveVO extends BaseVO {
	
	/**
	 * 用户ID
	 */
	private Integer user_id;

	/**
	 * 用户类型
	 */
	private String user_type;
	/**
	 * 文件ID
	 */
	private Integer file_id;
	
	/**
	 * 文件名称
	 */
	private String file_name;
	
	/**
	 * 文件类型
	 */
	private String file_type;
	
	/**
	 * 学校ID
	 */
	private Integer school_id;
	
	/**
	 * 团队类型(区分普通班级和兴趣班)
	 */
	private String team_type;
	
	/**
	 * 年级ID
	 */
	private Integer group_id;
	
	/**
	 * 班级ID
	 */
	private Integer team_id;
	
	/**
	 * 发送者
	 */
	private Integer create_by;
	
	/**
	 * 发送日期
	 */
	private Date create_date;

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

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
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
