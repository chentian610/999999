package com.ninesky.classtao.wechat.vo;

import java.util.Date;
import java.util.List;

/**   
 * @Title: 投票问题
 * @author zhusong
 * @date 2016-10-19 
 * @version V1.0   
 *
 */

public class VoteQuestionVO {
	
	private Integer question_id;
	/**
	 * 对应微信平台问题id
	 */
	private String platform_survey_id;
	/**
	 * 所属投票主题
	 */
	private Integer vote_id;
	/**
	 * 对应微信平台调研主题id
	 */
	private String platform_main_id;
	/**
	 * 问题标题
	 */
	private String title;
	/**
	 * 问题描述
	 */
	private String description;
	/**
	 * 调研题目类型： 1.单选  2:多选 3：填空
	 */
	private String type;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	private List<VoteOptionVO> options;
	
	public Integer getQuestion_id() {
		return question_id;
	}
	
	public void setQuestion_id(Integer question_id) {
		this.question_id = question_id;
	}
	
	public Integer getVote_id() {
		return vote_id;
	}
	
	public void setVote_id(Integer vote_id) {
		this.vote_id = vote_id;
	}
	
	public String getPlatform_main_id() {
		return platform_main_id;
	}
	
	public void setPlatform_main_id(String platform_main_id) {
		this.platform_main_id = platform_main_id;
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getSeq() {
		return seq;
	}
	
	public void setSeq(Integer seq) {
		this.seq = seq;
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

	public List<VoteOptionVO> getOptions() {
		return options;
	}

	public void setOptions(List<VoteOptionVO> options) {
		this.options = options;
	}
}
