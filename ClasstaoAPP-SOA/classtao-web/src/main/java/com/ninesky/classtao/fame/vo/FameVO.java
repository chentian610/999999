package com.ninesky.classtao.fame.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class FameVO extends BaseVO{


	/**
	* 主键ID，自增长
	*/
	private Integer fame_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 姓名
	*/
	private String name;

	/**
	* 描述
	*/
	private String description;

	/**
	* 头像URL
	*/
	private String head_url;

	/**
	* 生日
	*/
	private String birthday;

	/**
	* 毕业时间
	*/
	private String graduation_date;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新时间
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;
	
	/**
	 * 信息搜索条件
	 */
	private String search;
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setFame_id(Integer fame_id)  {
		this.fame_id = fame_id;
	}

	public Integer getFame_id()  {
		return fame_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setName(String name)  {
		this.name = name;
	}

	public String getName()  {
		return name;
	}

	public void setDescription(String description)  {
		this.description = description;
	}

	public String getDescription()  {
		return description;
	}

	public void setHead_url(String head_url)  {
		this.head_url = head_url;
	}

	public String getHead_url()  {
		return head_url;
	}

	public void setBirthday(String birthday)  {
		this.birthday = birthday;
	}

	public String getBirthday()  {
		return birthday;
	}

	public void setGraduation_date(String graduation_date)  {
		this.graduation_date = graduation_date;
	}

	public String getGraduation_date()  {
		return graduation_date;
	}
	
	public void setUpdate_by(Integer update_by)  {
		this.update_by = update_by;
	}

	public Integer getUpdate_by()  {
		return update_by;
	}

	public void setUpdate_date(Date update_date)  {
		this.update_date = update_date;
	}

	public Date getUpdate_date()  {
		return update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public Integer getCreate_by() {
		return create_by;
	}

	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}
	
	
}