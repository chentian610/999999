package com.ninesky.classtao.notice.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class NoticeGroupVO extends BaseVO{


	/**
	* 主键ID
	*/
	private Integer id;

	/**
	* 通知ID,外键
	*/
	private Integer notice_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	private String team_type;

	/**
	* 年级ID
	*/
	private Integer group_id;

	/**
	* 班级ID,或寝室ID
	*/
	private Integer team_id;

	/**
	* 接收者user_id(校务通知发给特定教师)
	*/
	private Integer user_id;

	/**
	* 用户类型：003
	*/
	private String user_type;
	
	/**
	 * 模块：家校通知，校务通知
	 */
	private String module_code;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建时间
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新时间
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setNotice_id(Integer notice_id)  {
		this.notice_id = notice_id;
	}

	public Integer getNotice_id()  {
		return notice_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public void setGroup_id(Integer group_id)  {
		this.group_id = group_id;
	}

	public Integer getGroup_id()  {
		return group_id;
	}

	public void setTeam_id(Integer team_id)  {
		this.team_id = team_id;
	}

	public Integer getTeam_id()  {
		return team_id;
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

	public void setCreate_by(Integer create_by)  {
		this.create_by = create_by;
	}

	public Integer getCreate_by()  {
		return create_by;
	}

	public void setCreate_date(Date create_date)  {
		this.create_date = create_date;
	}

	public Date getCreate_date()  {
		return create_date;
	}

	public void setUpdate_by(Integer update_by)  {
		this.update_by = update_by;
	}

	public Integer getUpdate_by()  {
		return update_by;
	}

	public void setUpdate_date(Date update_date)  {
		this.update_date = update_date;
	}

	public Date getUpdate_date()  {
		return update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

}