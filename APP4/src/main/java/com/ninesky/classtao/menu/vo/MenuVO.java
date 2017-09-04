package com.ninesky.classtao.menu.vo;


import com.ninesky.common.vo.BaseVO;

public class MenuVO extends BaseVO{


	/**
	* 主键
	*/
	private Integer menu_id;

	/**
	* 功能标题
	*/
	private String menu_name;

	/**
	* 功能类型
	*/
	private Integer parent_id;

	/**
	* 功能路径
	*/
	private String title_url;

	/**
	* 功能列表的顺序
	*/
	private Integer sort;

	/**
	* 功能的css样式
	*/
	private String css_name;

	/**
	* 所属用户类型
	*/
	private String user_type;

	/**
	 * 合作伙伴的菜单类型
	 */
	private String partner_code;

	private Integer is_active;

	private  String children_list;

	private String role_code;

	private Integer school_id;

	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public MenuVO() {}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

	public String getChildren_list() {
		return children_list;
	}

	public void setChildren_list(String children_list) {
		this.children_list = children_list;
	}

	public String getPartner_code() {
		return partner_code;
	}

	public void setPartner_code(String partner_code) {
		this.partner_code = partner_code;
	}

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public void setMenu_id(Integer menu_id)  {
		this.menu_id = menu_id;
	}

	public Integer getMenu_id()  {
		return menu_id;
	}

	public void setMenu_name(String menu_name)  {
		this.menu_name = menu_name;
	}

	public String getMenu_name()  {
		return menu_name;
	}

	public void setParent_id(Integer parent_id)  {
		this.parent_id = parent_id;
	}

	public Integer getParent_id()  {
		return parent_id;
	}

	public void setTitle_url(String title_url)  {
		this.title_url = title_url;
	}

	public String getTitle_url()  {
		return title_url;
	}

	public void setSort(Integer sort)  {
		this.sort = sort;
	}

	public Integer getSort()  {
		return sort;
	}

	public void setCss_name(String css_name)  {
		this.css_name = css_name;
	}

	public String getCss_name()  {
		return css_name;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}
}