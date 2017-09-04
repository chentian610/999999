package com.ninesky.classtao.system.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * 数据字典
 * @author user
 *
 */
public class DictVO extends BaseVO{
	private Integer id;
	private Integer school_id;
	private String dict_code;
	private String dict_value;
	private String dict_group;
	private String other_field;
	private Integer sort;
	private String description;
	private Integer is_active;
	private String news_code_list;
	private Integer is_configure;//1:可配置，0：不可配置
	private String client_id;//客户端id

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public Integer getIs_configure() {
		return is_configure;
	}

	public void setIs_configure(Integer is_configure) {
		this.is_configure = is_configure;
	}

	public String getNews_code_list() {
		return news_code_list;
	}

	public void setNews_code_list(String news_code_list) {
		this.news_code_list = news_code_list;
	}

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public String getDict_code() {
		return dict_code;
	}

	public void setDict_code(String dict_code) {
		this.dict_code = dict_code;
	}

	public String getDict_value() {
		return dict_value;
	}

	public void setDict_value(String dict_value) {
		this.dict_value = dict_value;
	}

	public String getDict_group() {
		return dict_group;
	}

	public void setDict_group(String dict_group) {
		this.dict_group = dict_group;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOther_field() {
		return other_field;
	}

	public void setOther_field(String other_field) {
		this.other_field = other_field;
	}

	public DictVO(){}

	public DictVO(String dict_group,String dict_code){
		this.dict_group = dict_group;
		this.dict_code = dict_code;
	}
}
