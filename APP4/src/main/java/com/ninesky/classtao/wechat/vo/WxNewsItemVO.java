package com.ninesky.classtao.wechat.vo;

import java.util.Date;

/**   
 * @Title: 图文素材
 * @Description: 微信图文素材实体
 * @author zhusong
 * @date 2016-10-09 
 * @version V1.0   
 *
 */

public class WxNewsItemVO {
	
	private Integer item_id;
	/**
	 * 对应微信平台itemId
	 */
	private String platform_item_id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 图片链接
	 */
	private String image_path;
	/**
	 * 正文
	 */
	private String content;
	/**
	 * 摘要
	 */
	private String description;
	/**
	 * 序号
	 */
	private String orders;
	/**
	 * 所属模板
	 */
	private Integer news_template_id;
	/**
	 * 对应与微信平台的templateid
	 */
	private String platform_news_template_id;
	/**
	 * 外部链接
	 */
	private String url;
	/**
	 * 原文链接
	 */
	private String original_link;
	/**
	 * 是否封面显示  1：显示  0：不显示
	 */
	private String display_cover_flag;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	public Integer getItem_id() {
		return item_id;
	}
	
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	
	public String getPlatform_item_id() {
		return platform_item_id;
	}

	public void setPlatform_item_id(String platform_item_id) {
		this.platform_item_id = platform_item_id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getOrders() {
		return orders;
	}
	
	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	public Integer getNews_template_id() {
		return news_template_id;
	}
	
	public void setNews_template_id(Integer news_template_id) {
		this.news_template_id = news_template_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPlatform_news_template_id() {
		return platform_news_template_id;
	}

	public void setPlatform_news_template_id(String platform_news_template_id) {
		this.platform_news_template_id = platform_news_template_id;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getOriginal_link() {
		return original_link;
	}

	public void setOriginal_link(String original_link) {
		this.original_link = original_link;
	}

	public String getDisplay_cover_flag() {
		return display_cover_flag;
	}

	public void setDisplay_cover_flag(String display_cover_flag) {
		this.display_cover_flag = display_cover_flag;
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
