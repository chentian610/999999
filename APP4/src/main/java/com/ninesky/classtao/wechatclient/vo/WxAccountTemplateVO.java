package com.ninesky.classtao.wechatclient.vo;

import java.util.Date;

/**   
 * @Title: 微信模板消息
 * @Description: 微信模板消息实体
 * @author zhusong
 * @date 2016-11-30 
 * @version V1.0   
 *
 */
public class WxAccountTemplateVO {
	
	private Integer id;
	/**
	 * 课道平台公众号ID
	 */
	private Integer account_id;
	/**
	 * 微信平台模板编号
	 */
	private String wechat_template_number;
	/**
	 * 微信平台模板ID
	 */
	private String wechat_template_id;
	/**
	 * 创建时间
	 */
	private Date create_time;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getAccount_id() {
		return account_id;
	}
	
	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	
	public String getWechat_template_number() {
		return wechat_template_number;
	}
	
	public void setWechat_template_number(String wechat_template_number) {
		this.wechat_template_number = wechat_template_number;
	}
	
	public String getWechat_template_id() {
		return wechat_template_id;
	}
	
	public void setWechat_template_id(String wechat_template_id) {
		this.wechat_template_id = wechat_template_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
