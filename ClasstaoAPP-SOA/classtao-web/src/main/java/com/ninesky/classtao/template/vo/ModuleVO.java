package com.ninesky.classtao.template.vo;

import java.util.Date;


public class ModuleVO {


	/**
	* 主键，模块ID
	*/
	private Integer module_id;
	
	/**
	* 模块code
	*/
	private String module_code;
	
	/**
	* 父亲code
	*/
	private String parent_code;
	
	/**
	 * 用户类型
	 */
	private String user_type;
	
	/**
	* 模板ID
	*/
	private Integer template_id;

	/**
	* 模块名称
	*/
	private String module_name;
	
	/**
	* 模块url，部分模块可能由web嵌入实现
	*/
	private String module_url;

	/**
	* 模块显示图片URL
	*/
	private String icon_url;
	
	/**
	* 是否必选模块
	*/
	private Integer is_must;
	
	/**
	 * module是否必选模块
	 */
	private Integer module_is_must;
	
	/**
	* 是否启用，1是启用，0是未启用
	*/
	private Integer is_inactive;

	/**
	* 排序
	*/
	private Integer sort;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建时间
	*/
	private Date create_date;
	
	private Integer is_news;

	private  String partner_code;

	private String school_type;

	private Integer module_price;

	private String introduce;

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIntroduce() {

		return introduce;
	}

	public void setModule_price(Integer module_price) {
		this.module_price = module_price;
	}

	public Integer getModule_price() {

		return module_price;
	}

	public void setSchool_type(String school_type) {

		this.school_type = school_type;
	}

	public String getSchool_type() {
		return school_type;
	}

	public String getPartner_code() {
		return partner_code;
	}

	public void setPartner_code(String partner_code) {
		this.partner_code = partner_code;
	}

	public Integer getIs_news() {
		return is_news;
	}

	public void setIs_news(Integer is_news) {
		this.is_news = is_news;
	}

	public void setModule_id(Integer module_id)  {
		this.module_id = module_id;
	}

	public Integer getModule_id()  {
		return module_id;
	}

	public void setModule_name(String module_name)  {
		this.module_name = module_name;
	}

	public String getModule_name()  {
		return module_name;
	}

	public void setIcon_url(String icon_url)  {
		this.icon_url = icon_url;
	}

	public String getIcon_url()  {
		return icon_url;
	}

	public void setIs_inactive(Integer is_inactive)  {
		this.is_inactive = is_inactive;
	}

	public Integer getIs_inactive()  {
		return is_inactive;
	}

	public void setSort(Integer sort)  {
		this.sort = sort;
	}

	public Integer getSort()  {
		return sort;
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

	public Integer getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getModule_url() {
		return module_url;
	}

	public void setModule_url(String module_url) {
		this.module_url = module_url;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Integer getIs_must() {
		return is_must;
	}

	public void setIs_must(Integer is_must) {
		this.is_must = is_must;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public Integer getModule_is_must() {
		return module_is_must;
	}

	public void setModule_is_must(Integer module_is_must) {
		this.module_is_must = module_is_must;
	}

	

}