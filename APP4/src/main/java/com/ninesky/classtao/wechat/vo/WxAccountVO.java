package com.ninesky.classtao.wechat.vo;

import java.util.Date;

/**   
 * @Title: 微信公众账号
 * @Description: 微信公众账号实体，对应微信管理平台
 * @author zhusong
 * @date 2016-09-25 
 * @version V1.0   
 *
 */

public class WxAccountVO {
	
	private Integer account_id;
	/**
	 * 学校ID
	 */
	private Integer school_id;
	/**
	 * 对应微信平台accountId
	 */
	private String platform_account_id;
	/**
	 * 公众号名称
	 */
	private String account_name;
	/**
	 * 公众号TOKEN
	 */
	private String account_token;
	/**
	 * 原始ID
	 */
	private String weixin_accountid;
	/**
	 * 公众号类型  1:服务号  2：订阅号
	 */
	private Integer account_type;
	/**
	 * 电子邮箱
	 */
	private String account_email;
	/**
	 * 公众号描述
	 */
	private String account_desc;
	/**
	 * 公众号APPID
	 */
	private String account_appid;
	/**
	 * 公众号APPSECRET
	 */
	private String account_appsecret;
	/**
	 * 有效状态  1：有效   0：无效
	 */
	private Integer is_effective;
	/**
	 * 是否认证  1：认证  0：未认证
	 */
	private Integer auth_status;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getPlatform_account_id() {
		return platform_account_id;
	}

	public void setPlatform_account_id(String platform_account_id) {
		this.platform_account_id = platform_account_id;
	}

	public String getAccount_token() {
		return account_token;
	}

	public void setAccount_token(String account_token) {
		this.account_token = account_token;
	}

	public String getWeixin_accountid() {
		return weixin_accountid;
	}

	public void setWeixin_accountid(String weixin_accountid) {
		this.weixin_accountid = weixin_accountid;
	}

	public Integer getAccount_type() {
		return account_type;
	}

	public void setAccount_type(Integer account_type) {
		this.account_type = account_type;
	}

	public String getAccount_email() {
		return account_email;
	}

	public void setAccount_email(String account_email) {
		this.account_email = account_email;
	}

	public String getAccount_desc() {
		return account_desc;
	}

	public void setAccount_desc(String account_desc) {
		this.account_desc = account_desc;
	}

	public String getAccount_appid() {
		return account_appid;
	}

	public void setAccount_appid(String account_appid) {
		this.account_appid = account_appid;
	}

	public String getAccount_appsecret() {
		return account_appsecret;
	}

	public void setAccount_appsecret(String account_appsecret) {
		this.account_appsecret = account_appsecret;
	}

	public Integer getIs_effective() {
		return is_effective;
	}

	public void setIs_effective(Integer is_effective) {
		this.is_effective = is_effective;
	}

	public Integer getAuth_status() {
		return auth_status;
	}

	public void setAuth_status(Integer auth_status) {
		this.auth_status = auth_status;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
}
