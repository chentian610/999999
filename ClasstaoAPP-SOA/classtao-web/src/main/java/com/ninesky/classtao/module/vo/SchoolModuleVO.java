package com.ninesky.classtao.module.vo;

import com.ninesky.common.vo.BaseVO;

public class SchoolModuleVO extends BaseVO{


	/**
	* 未设置，请在数据库中设置
	*/
	private Integer id;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer school_id;

	/**
	* 户用类型
	*/
	private String user_type;

	/**
	* 未设置，请在数据库中设置
	*/
	private String module_code;

	/**
	* 父节点，表示根节点	*/
	private String parent_code;

	/**
	* 是否必选模块	*/
	private Integer is_must;

	/**
	* 是否被禁0?1禁用
	*/
	private Integer is_inactive;

	/**
	* 模块名称
	*/
	private String module_name;

	/**
	* 图标url
	*/
	private String icon_url;

	/**
	* css显示样式	*/
	private String icon_class;

	/**
	* 父亲节点ID
	*/
	private Integer parent_id;

	/**
	* 模块url，部分模块可能由web嵌入实现
	*/
	private String module_url;

	/**
	* 页面打开的初始化方法
	*/
	private String init_function;

	/**
	* 获取模块未读数量的请求action，提供给客户端进行获取该模块的为读数量使用，方便系统自由和第三方模块的统一管理
	*/
	private String count_action;

	/**
	* 是否大图标，提供教师使用
	*/
	private Integer is_bigicon;

	/**
	* 初始化数据，用于快速打开部分模块的某个子功能，由模块自己定义设计
	*/
	private String initdata;

	/**
	* 是否是新闻模块	*/
	private Integer is_news;

	private Integer module_price;

	private String introduce;

	private Integer module_id;

	private String school_type;

	private String partner_code;

	public SchoolModuleVO(){
	}

	public SchoolModuleVO(Integer school_id,Integer module_id){
		this.school_id = school_id;
		this.module_id = module_id;
	}

	public void setPartner_code(String partner_code) {
		this.partner_code = partner_code;
	}

	public String getPartner_code() {

		return partner_code;
	}

	public void setSchool_type(String school_type) {
		this.school_type = school_type;
	}

	public String getSchool_type() {

		return school_type;
	}

	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}

	public Integer getModule_id() {

		return module_id;
	}

	public void setModule_price(Integer module_price) {
		this.module_price = module_price;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIntroduce() {
		return introduce;
	}

	public Integer getModule_price() {
		return module_price;
	}

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public void setModule_code(String module_code)  {
		this.module_code = module_code;
	}

	public String getModule_code()  {
		return module_code;
	}

	public void setParent_code(String parent_code)  {
		this.parent_code = parent_code;
	}

	public String getParent_code()  {
		return parent_code;
	}

	public void setIs_must(Integer is_must)  {
		this.is_must = is_must;
	}

	public Integer getIs_must()  {
		return is_must;
	}

	public void setIs_inactive(Integer is_inactive)  {
		this.is_inactive = is_inactive;
	}

	public Integer getIs_inactive()  {
		return is_inactive;
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

	public void setIcon_class(String icon_class)  {
		this.icon_class = icon_class;
	}

	public String getIcon_class()  {
		return icon_class;
	}

	public void setParent_id(Integer parent_id)  {
		this.parent_id = parent_id;
	}

	public Integer getParent_id()  {
		return parent_id;
	}

	public void setModule_url(String module_url)  {
		this.module_url = module_url;
	}

	public String getModule_url()  {
		return module_url;
	}

	public void setInit_function(String init_function)  {
		this.init_function = init_function;
	}

	public String getInit_function()  {
		return init_function;
	}

	public void setCount_action(String count_action)  {
		this.count_action = count_action;
	}

	public String getCount_action()  {
		return count_action;
	}

	public void setIs_bigicon(Integer is_bigicon)  {
		this.is_bigicon = is_bigicon;
	}

	public Integer getIs_bigicon()  {
		return is_bigicon;
	}

	public void setInitdata(String initdata)  {
		this.initdata = initdata;
	}

	public String getInitdata()  {
		return initdata;
	}

	public void setIs_news(Integer is_news)  {
		this.is_news = is_news;
	}

	public Integer getIs_news()  {
		return is_news;
	}

}