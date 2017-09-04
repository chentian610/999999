package com.ninesky.classtao.wechat.vo;

import java.util.Date;

/**   
 * @Title: 图文模板
 * @Description: 微信图文模板实体，其下可以包含多个图文素材
 * @author zhusong
 * @date 2016-10-09 
 * @version V1.0   
 *
 */

public class NewsTemplateVO {
	
	private Integer template_id;
	/**
	 * 课道平台公众号ID
	 */
	private Integer account_id;
	/**
	 * 对应微信平台templateId
	 */
	private String platform_template_id;
	/**
	 * 对应微信平台accountId
	 */
	private String platform_account_id;
	/**
	 * 对应微信平台上传图文消息id
	 */
	private String platform_message_id;
	/**
	 * 上传标记
	 */
	private Integer upload_status;
	/**
	 * 模板名称
	 */
	private String template_name;
	/**
	 * 用于展示newitem里的图片
	 */
	private String showPic;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	public Integer getTemplate_id() {
		return template_id;
	}
	
	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}
	
	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}

	public String getPlatform_template_id() {
		return platform_template_id;
	}

	public void setPlatform_template_id(String platform_template_id) {
		this.platform_template_id = platform_template_id;
	}

	public String getPlatform_account_id() {
		return platform_account_id;
	}
	
	public String getPlatform_message_id() {
		return platform_message_id;
	}

	public void setPlatform_message_id(String platform_message_id) {
		this.platform_message_id = platform_message_id;
	}

	public Integer getUpload_status() {
		return upload_status;
	}

	public void setUpload_status(Integer upload_status) {
		this.upload_status = upload_status;
	}

	public void setPlatform_account_id(String platform_account_id) {
		this.platform_account_id = platform_account_id;
	}
	
	public String getTemplate_name() {
		return template_name;
	}
	
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public String getShowPic() {
		return showPic;
	}

	public void setShow_pic(String showPic) {
		this.showPic = showPic;
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
