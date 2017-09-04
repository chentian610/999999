package com.ninesky.classtao.template.vo;

import java.util.Date;
import java.util.List;

public class TemplateVO {


	/**
	* 主键ID
	*/
	private Integer template_id;
	
	/**
	 * 学校类型
	 */
	private String school_type;

	/**
	* 未知
	*/
	private String template_name;

	/**
	* 板模图片URL
	*/
	private String template_pic_url;
	
	/**
	* 模块列表
	*/
	private List<ModuleVO> module_list;

	/**
	* 未知
	*/
	private Integer is_active;

	/**
	* 未知
	*/
	private Integer create_by;

	/**
	* 未知
	*/
	private Date create_date;

	public void setTemplate_id(Integer template_id)  {
		this.template_id = template_id;
	}

	public Integer getTemplate_id()  {
		return template_id;
	}

	public String getSchool_type() {
		return school_type;
	}

	public void setSchool_type(String school_type) {
		this.school_type = school_type;
	}

	public void setTemplate_name(String template_name)  {
		this.template_name = template_name;
	}

	public String getTemplate_name()  {
		return template_name;
	}

	public void setTemplate_pic_url(String template_pic_url)  {
		this.template_pic_url = template_pic_url;
	}

	public String getTemplate_pic_url()  {
		return template_pic_url;
	}

	public void setIs_active(Integer is_active)  {
		this.is_active = is_active;
	}

	public Integer getIs_active()  {
		return is_active;
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

	public List<ModuleVO> getModule_list() {
		return module_list;
	}

	public void setModule_list(List<ModuleVO> module_list) {
		this.module_list = module_list;
	}

}