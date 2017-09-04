package com.ninesky.classtao.notice.vo;

public class NoticeCountVO {

	/**
	 * 主键
	 */
	private Integer notice_id;
	/**
	 * 通知状态
	 */
	private String notice_status;
	
	/**
	 * 各种通知状态数目
	 */
	private Integer count;

	public Integer getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(Integer notice_id) {
		this.notice_id = notice_id;
	}

	public String getNotice_status() {
		return notice_status;
	}

	public void setNotice_status(String notice_status) {
		this.notice_status = notice_status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
