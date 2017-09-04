package com.ninesky.classtao.wechat.vo;

import java.util.Date;
import java.util.List;

/**   
 * @Title: 微信公众号菜单
 * @Description: 微信公众号菜单实体，对应微信管理平台
 * @author zhusong
 * @date 2016-09-25 
 * @version V1.0   
 *
 */

public class WxMenuVO {
	
	private Integer menu_id;
	/**
	 * 父菜单ID
	 */
	private Integer parent_id;
	/**
	 * 课道平台公众号ID
	 */
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
	 * 对应微信平台menuId
	 */
	private String platform_menu_id;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 动作类型  消息触发类：click 网页链接类：view
	 */
	private String type;
	/**
	 * 网页链接类：view  网页地址
	 */
	private String url;
	/**
	 * 消息触发类：click  消息类型（news:图文消息， vote:投票活动， info:资讯）  
	 */
	private String msg_type;
	/**
	 * 消息触发类：click  消息模板（图文消息平台id，投票活动平台id，资讯栏目编号）
	 */
	private String template_id;
	/**
	 * 排序
	 */
	private String orders;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	private List<WxMenuVO> childMenus;
	
	public Integer getMenu_id() {
		return menu_id;
	}
	
	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
	
	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getAccount_id() {
		return account_id;
	}
	
	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	
	public Integer getSchool_id() {
		return school_id;
	}
	
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	
	public String getPlatform_account_id() {
		return platform_account_id;
	}
	
	public void setPlatform_account_id(String platform_account_id) {
		this.platform_account_id = platform_account_id;
	}
	
	public String getPlatform_menu_id() {
		return platform_menu_id;
	}
	
	public void setPlatform_menu_id(String platform_menu_id) {
		this.platform_menu_id = platform_menu_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getOrders() {
		return orders;
	}
	
	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public List<WxMenuVO> getChildMenus() {
		return childMenus;
	}
	
	public void setChildMenus(List<WxMenuVO> childMenus) {
		this.childMenus = childMenus;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}
