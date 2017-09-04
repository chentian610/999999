package com.ninesky.classtao.notice.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class NoticeVO extends BaseVO{

	private Integer notice_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 用户类型：003
	*/
	private String user_type;

	/**
	* 发送者用户ID
	*/
	private Integer sender_id;

	/**
	* 发送者名字
	*/
	private String sender_name;

	/**
	 * 模块，区分校务通知和普通通知
	 */
	private String module_code;
	
	/**
	* 消息标题
	*/
	private String notice_title;

	/**
	* 消息内容
	*/
	private String notice_content;
	
	/**
	 * 阅读情况，发出的通知需要该统计
	 */
	private String count_list;
	
	/**
	 * 通知附件
	 */
	private String file_list;
	
	/**
	 * 发送时间
	 */
	private Date send_time;

	/**
	 * 发送班级
	 */
	private String team_name;

	/**
	 * 是否含有附件（1：是；2：否）
	 */
	private Integer have_file;

    /**
     * 未读人数
     */
	private Integer unreadCount;

	/**
	 * 已读人数
	 */
	private Integer readCount;

	/**
	 * 回复人数
	 */
	private Integer replyCount;

	/**
	 * 通知总人数
	 */
	private Integer total_count;

	/**
	 * 是否可提醒未读用户
	 */
	private boolean can_remind;

	/**
	 * 是否已确认
	 */
	private boolean have_confirm;

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public Integer getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Integer unreadCount) {
		this.unreadCount = unreadCount;
	}

	public Integer getHave_file() {
		return have_file;
	}

	public void setHave_file(Integer have_file) {
		this.have_file = have_file;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String file_list) {
		this.file_list = file_list;
	}

	public String getCount_list() {
		return count_list;
	}

	public void setCount_list(String count_list) {
		this.count_list = count_list;
	}

	public Integer getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(Integer notice_id) {
		this.notice_id = notice_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public void setSender_id(Integer sender_id)  {
		this.sender_id = sender_id;
	}

	public Integer getSender_id()  {
		return sender_id;
	}

	public void setSender_name(String sender_name)  {
		this.sender_name = sender_name;
	}

	public String getSender_name()  {
		return sender_name;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public void setNotice_title(String notice_title)  {
		this.notice_title = notice_title;
	}

	public String getNotice_title()  {
		return notice_title;
	}

	public void setNotice_content(String notice_content)  {
		this.notice_content = notice_content;
	}

	public String getNotice_content()  {
		return notice_content;
	}

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}

	public boolean isCan_remind() {
		return can_remind;
	}

	public void setCan_remind(boolean can_remind) {
		this.can_remind = can_remind;
	}

	public boolean isHave_confirm() {
		return have_confirm;
	}

	public void setHave_confirm(boolean have_confirm) {
		this.have_confirm = have_confirm;
	}
}