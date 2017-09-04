package com.ninesky.classtao.wechat.vo;

import java.util.Date;

public class VoteOptionVO {
	
	private Integer option_id;
	/**
	 * 对应微信平台调研问题选项id
	 */
	private String platform_option_id;
	/**
	 * 所属投票问题
	 */
	private Integer question_id;
	/**
	 * 对应微信平台调研问题id
	 */
	private String platform_survey_id;
	/**
	 * 选项标题
	 */
	private String title;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	public Integer getOption_id() {
		return option_id;
	}
	
	public void setOption_id(Integer option_id) {
		this.option_id = option_id;
	}
	
	public String getPlatform_option_id() {
		return platform_option_id;
	}
	
	public void setPlatform_option_id(String platform_option_id) {
		this.platform_option_id = platform_option_id;
	}
	
	public Integer getQuestion_id() {
		return question_id;
	}
	
	public void setQuestion_id(Integer question_id) {
		this.question_id = question_id;
	}
	
	public String getPlatform_survey_id() {
		return platform_survey_id;
	}
	
	public void setPlatform_survey_id(String platform_survey_id) {
		this.platform_survey_id = platform_survey_id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getCreate_date() {
		return create_date;
	}
	
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	public Date getUpdate_date() {
		return update_date;
	}
	
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}
