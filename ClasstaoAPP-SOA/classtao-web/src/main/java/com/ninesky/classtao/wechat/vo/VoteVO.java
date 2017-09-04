package com.ninesky.classtao.wechat.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**   
 * @Title: 投票主题
 * @author zhusong
 * @date 2016-10-19 
 * @version V1.0   
 *
 */

public class VoteVO {
	
	private Integer vote_id;
	/**
	 * 课道平台公众号ID
	 */
	private Integer account_id;
	/**
	 * 对应微信平台调研主题id
	 */
	private String platform_main_id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 当前状态 0：未发布；1：发布中；2：已发布
	 */
	private String statement;
	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date begin_date;
	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date valid_date;
	/**
	 * 创建时间
	 */
	private Date create_date;
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	private List<VoteQuestionVO> questions;
	
	public Integer getVote_id() {
		return vote_id;
	}
	
	public void setVote_id(Integer vote_id) {
		this.vote_id = vote_id;
	}
	
	public Integer getAccount_id() {
		return account_id;
	}
	
	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	
	public String getPlatform_main_id() {
		return platform_main_id;
	}

	public void setPlatform_main_id(String platform_main_id) {
		this.platform_main_id = platform_main_id;
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

	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Date getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}

	public Date getValid_date() {
		return valid_date;
	}

	public void setValid_date(Date valid_date) {
		this.valid_date = valid_date;
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

	public List<VoteQuestionVO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VoteQuestionVO> questions) {
		this.questions = questions;
	}
}
