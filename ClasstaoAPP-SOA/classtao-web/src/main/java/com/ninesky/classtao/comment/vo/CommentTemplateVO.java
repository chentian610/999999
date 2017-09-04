package com.ninesky.classtao.comment.vo;


import com.ninesky.common.vo.BaseVO;

public class CommentTemplateVO extends BaseVO{


	/**
	* 评价模板id
	*/
	private Integer template_id;

	/**
	* 学校id
	*/
	private Integer school_id;

	/**
	* 用户id
	*/
	private Integer user_id;

	/**
	* 评语内容
	*/
	private String comment;

	/**
	* 是否已读,0未读，1已读
	*/
	private Integer is_read;


	public void setTemplate_id(Integer template_id)  {
		this.template_id = template_id;
	}

	public Integer getTemplate_id()  {
		return template_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setComment(String comment)  {
		this.comment = comment;
	}

	public String getComment()  {
		return comment;
	}

	public void setIs_read(Integer is_read)  {
		this.is_read = is_read;
	}

	public Integer getIs_read()  {
		return is_read;
	}

}