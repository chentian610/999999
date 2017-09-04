package com.ninesky.classtao.school.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class LinkManVO extends BaseVO{

	/**
	* 学校名称，唯一
	*/
	private String school_name;

	/**
	* 学校管理员
	*/
	private String link_man;

	/**
	* 学校管理员电话
	*/
	private String link_style;

	/**
	 * 邮箱
	 */
	private String link_email;
	
	/**
	* 学校英文名称
	*/
	private String ip_address;
	
	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新日期
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;

	public String getLink_man() {
		return link_man;
	}

	public void setLink_man(String link_man) {
		this.link_man = link_man;
	}

	public String getLink_style() {
		return link_style;
	}

	public void setLink_style(String link_style) {
		this.link_style = link_style;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	@Override
	public Integer getCreate_by() {
		return create_by;
	}

	@Override
	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	@Override
	public Date getCreate_date() {
		return create_date;
	}

	@Override
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	@Override
	public Integer getUpdate_by() {
		return update_by;
	}

	@Override
	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}

	@Override
	public Date getUpdate_date() {
		return update_date;
	}

	@Override
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSchool_name() {

		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}


	public String getLink_email() {
		return link_email;
	}

	public void setLink_email(String link_email) {
		this.link_email = link_email;
	}
}