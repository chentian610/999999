package com.ninesky.classtao.info.vo;

import com.ninesky.common.vo.BaseVO;

public class InfoReceiveVO extends BaseVO {
	/**
	* 主键id
	*/
	private Integer id;
	
	/**
	* 消息ID，外键
	*/
	private Integer info_id;

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
	 * 学生id
	 */
	private Integer student_id;
	
	/**
	* 消息实际(最早)接收者
	*/
	private String module_code;
	
	/**
	 * 对应模块的主键ID
	 */
	private Integer module_pkid;
	
	/**
	* 用户类型：003
	*/
	private String user_type;

	/**
	* 接收人姓名
	*/
	private String receive_name;

	/**
	* 发送时间
	*/
	private String info_date;
	
	/**
	* 消息打开类型
	*/
	private String info_type;
	
	/**
	 * 消息打开类型为网页时对应的url
	 */
	private String info_url;
	
	/**
	* 发送人ID，用户ID
	*/
	private String info_title;

	/**
	* 发送人姓名
	*/
	private String info_content;
	
	/**
	 * 动态显示类型：023
	 */
	private String show_type;

	/**
	* 消息实际（最早）接受者时间
	*/
	private String init_data;
	
	/**
	 * 图片列表
	 */
	private String photo_list;
	
	/**
	 * 统计信息
	 */
	private String count_info;

	/**
	* 是否已读,0未读，1已读
	*/
	private Integer is_read;
	
	/**
	 * 用于通知模块更新动态使用
	 */
	private Integer send_id;
	
	private Integer sender_id;//动态的发送者
	
	public void setInfo_id(Integer info_id)  {
		this.info_id = info_id;
	}

	public Integer getInfo_id()  {
		return info_id;
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

	public void setInfo_title(String info_title)  {
		this.info_title = info_title;
	}

	public String getInfo_title()  {
		return info_title;
	}

	public void setInfo_content(String info_content)  {
		this.info_content = info_content;
	}

	public String getInfo_content()  {
		return info_content;
	}

	public void setModule_code(String module_code)  {
		this.module_code = module_code;
	}

	public String getModule_code()  {
		return module_code;
	}

	public void setInit_data(String init_data)  {
		this.init_data = init_data;
	}

	public String getInit_data()  {
		return init_data;
	}

	public void setIs_read(Integer is_read)  {
		this.is_read = is_read;
	}

	public Integer getIs_read()  {
		return is_read;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInfo_date() {
		return info_date;
	}

	public void setInfo_date(String infoDate) {
		info_date = infoDate;
	}

	public String getInfo_type() {
		return info_type;
	}

	public void setInfo_type(String infoType) {
		info_type = infoType;
	}

	public Integer getModule_pkid() {
		return module_pkid;
	}

	public void setModule_pkid(Integer modulePkid) {
		module_pkid = modulePkid;
	}

	public String getPhoto_list() {
		return photo_list;
	}

	public void setPhoto_list(String photoList) {
		photo_list = photoList;
	}

	public String getCount_info() {
		return count_info;
	}

	public void setCount_info(String countInfo) {
		count_info = countInfo;
	}

	public String getInfo_url() {
		return info_url;
	}

	public void setInfo_url(String infoUrl) {
		info_url = infoUrl;
	}

	public String getShow_type() {
		return show_type;
	}

	public void setShow_type(String showType) {
		show_type = showType;
	}

	public Integer getSend_id() {
		return send_id;
	}

	public void setSend_id(Integer send_id) {
		this.send_id = send_id;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}
}