package com.ninesky.classtao.app.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class SuggestVO extends BaseVO{
	/**
	* 主键
	*/
	private Integer id;
	/**
	* 学校ID
	*/
	private Integer user_id;

	/**
	* 提建议人名字
	*/
	private String user_name;

	/**
	* 题目内容
	*/
	private String content;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setUser_name(String user_name)  {
		this.user_name = user_name;
	}

	public String getUser_name()  {
		return user_name;
	}

	public void setContent(String content)  {
		this.content = content;
	}

	public String getContent()  {
		return content;
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

}