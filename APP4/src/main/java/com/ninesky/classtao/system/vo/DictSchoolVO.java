package com.ninesky.classtao.system.vo;

import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.BaseVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;

/**
 * 数据字典
 * @author user
 *
 */
public class DictSchoolVO extends BaseVO{
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
	private String news_list;
	private String child_node_list;

	public String getChild_node_list() {
		return child_node_list;
	}

	public void setChild_node_list(String child_node_list) {
		this.child_node_list = child_node_list;
	}

	/**
	 * 新闻显示条数
	 */
	private String css_list;

	private String css_value;

	private String css_code;

	public String getCss_code() {
		return css_code;
	}

	public void setCss_code(String css_code) {
		this.css_code = css_code;
	}

	public String getCss_value() {
		return css_value;
	}
	public void setCss_value(String css_value) {
		this.css_value = css_value;
	}

	public String getCss_list() {
		return css_list;
	}
	public void setCss_list(String css_list) {
		this.css_list = css_list;
	}
	public String getNews_list() {
		return news_list;
	}

	public void setNews_list(String news_list) {
		this.news_list = news_list;
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

	public DictSchoolVO () {}

	public DictSchoolVO (Integer school_id,String dict_group) {
		if (IntegerUtil.isEmpty(school_id) || StringUtil.isEmpty(dict_group))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.school_id = school_id;
		this.dict_group = dict_group;
	}
}
