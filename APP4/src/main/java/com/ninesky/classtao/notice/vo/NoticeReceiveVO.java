package com.ninesky.classtao.notice.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class NoticeReceiveVO extends BaseVO{

	private Integer receive_id;
	
	/**
	* 消息ID，外键
	*/
	private Integer notice_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 年级ID
	*/
	private Integer grade_id;

	/**
	* 班级ID
	*/
	private Integer class_id;

	/**
	* 接收人UerID
	*/
	private Integer user_id;

	/**
	 * 接收人student_id
	 */
	private Integer student_id;
	
	/**
	* 用户类型：003
	*/
	private String user_type;

	/**
	* 接收人姓名
	*/
	private String receive_name;
	
	/**
	 * 接收人头像url
	 */
	private String head_url;

	/**
	* 发送者用户ID
	*/
	private Integer sender_id;

	/**
	* 发送者名字
	*/
	private String sender_name;

	/**
	* 发送时间
	*/
	private Date send_time;

	/**
	* 发送人ID，用户ID
	*/
	private String notice_title;

	/**
	* 发送人姓名
	*/
	private String notice_content;

	/**
	 * 创建者
	 */
	private Integer create_by;
	
	/**
	 * 创建时间
	 */
	private Date create_date;
	
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
	* 消息发送类型：010
	*/
	private String send_type;
	
	/**
	 * 消息状态
	 */
	private String notice_status;
	
	/**
	 * 条数
	 * @return
	 */
	private Integer count;
	
	/**
	 * 是否已读,0未读，1已读
	 * @return
	 */
	private Integer is_read;
	
	/**
	 * 阅读情况，发出的通知需要该统计
	 */
	private String count_list;
	
	/**
	 * 模块，区分校务通知和普通通知
	 */
	private String module_code;
	
	/**
	 * 附件列表，显示前三个文件
	 */
	private String file_list;

	private String duty_list;
	
	public Integer getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(Integer receive_id) {
		this.receive_id = receive_id;
	}

	public void setNotice_id(Integer notice_id)  {
		this.notice_id = notice_id;
	}

	public Integer getNotice_id()  {
		return notice_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setGrade_id(Integer grade_id)  {
		this.grade_id = grade_id;
	}

	public Integer getGrade_id()  {
		return grade_id;
	}

	public void setClass_id(Integer class_id)  {
		this.class_id = class_id;
	}

	public Integer getClass_id()  {
		return class_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public void setUser_type(String user_type)  {
		this.user_type = user_type;
	}

	public String getUser_type()  {
		return user_type;
	}

	public void setReceive_name(String receive_name)  {
		this.receive_name = receive_name;
	}

	public String getReceive_name()  {
		return receive_name;
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


	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public void setNotice_content(String notice_content)  {
		this.notice_content = notice_content;
	}

	public String getNotice_content()  {
		return notice_content;
	}

	public Integer getCreate_by() {
		return create_by;
	}

	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public void setSend_type(String send_type)  {
		this.send_type = send_type;
	}

	public String getSend_type()  {
		return send_type;
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

	

	public Integer getIs_read() {
		return is_read;
	}

	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}

	public String getCount_list() {
		return count_list;
	}

	public void setCount_list(String count_list) {
		this.count_list = count_list;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String file_list) {
		this.file_list = file_list;
	}

	public String getDuty_list() {
		return duty_list;
	}

	public void setDuty_list(String duty_list) {
		this.duty_list = duty_list;
	}
}