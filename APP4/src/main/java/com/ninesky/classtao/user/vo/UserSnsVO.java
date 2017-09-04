package com.ninesky.classtao.user.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class UserSnsVO extends BaseVO{

	private Integer sns_id;
	/**
	* 未设置，请在数据库中设置
	*/
	private Integer user_id;

	/**
	* 未设置，请在数据库中设置
	*/
	private String sns_type;

	/**
	* 未设置，请在数据库中设置
	*/
	private String account;

	/**
	* 未设置，请在数据库中设置
	*/
	private String is_inactive;

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
	
	public Integer getSns_id() {
		return sns_id;
	}

	public void setSns_id(Integer sns_id) {
		this.sns_id = sns_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setSns_type(String sns_type)  {
		this.sns_type = sns_type;
	}

	public String getSns_type()  {
		return sns_type;
	}

	public void setAccount(String account)  {
		this.account = account;
	}

	public String getAccount()  {
		return account;
	}

	public void setIs_inactive(String is_inactive)  {
		this.is_inactive = is_inactive;
	}

	public String getIs_inactive()  {
		return is_inactive;
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

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}