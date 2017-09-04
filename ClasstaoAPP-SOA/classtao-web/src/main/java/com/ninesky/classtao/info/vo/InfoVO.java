package com.ninesky.classtao.info.vo;


import com.ninesky.common.vo.BaseVO;


public class InfoVO extends BaseVO{
	/**
	 * 主键id
	 */
	private Integer info_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 用户类型：003
	*/
	private String user_type;

	/**
	* 发送人员ID
	*/
	private Integer sender_id;

	/**
	* 模块编码
	*/
	private String module_code;
	
	/**
	* 模块的主键ID
	*/
	private Integer module_pkid;
	
	/**
	* 消息打开类型：数据字典：021
	*/
	private String info_type;
	
	/**
	 * 消息打开类型为网页时对应的url
	 */
	private String info_url;
	
	/**
	* 消息标题
	*/
	private String info_title;

	/**
	* 消息内容
	*/
	private String info_content;
	
	/**
	 * 动态显示类型：023
	 */
	private String show_type;
	
	/**
	* 模块给动态消息初始化Json数据，在打开动态的时候使用
	*/
	private String init_data;
	
	/**
	 * 消息时间
	 */
	private String info_date;
	
	/**
	 * 图片列表
	 */
	private String photo_list;
	
	/**
	 * 统计信息
	 */
	private String count_info;
	
	/**
	 * 接收人信息
	 */
	private String receive_list;
	/**
	* 年级id
	*/
	private Integer grade_id;
	/**
	* 班级id
	*/
	private Integer class_id;
	
	/**
	 * 透传内容
	 */
	private String transmissionContent;
	
	public Integer getInfo_id() {
		return info_id;
	}

	public void setInfo_id(Integer infoId) {
		info_id = infoId;
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

	public void setModule_code(String module_code)  {
		this.module_code = module_code;
	}

	public String getModule_code()  {
		return module_code;
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

	public void setInit_data(String init_data)  {
		this.init_data = init_data;
	}

	public String getInit_data()  {
		return init_data;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer gradeId) {
		grade_id = gradeId;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer classId) {
		class_id = classId;
	}

	public String getReceive_list() {
		return receive_list;
	}

	public void setReceive_list(String receiveList) {
		receive_list = receiveList;
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

	public void setModule_pkid(Integer module_pkid) {
		this.module_pkid = module_pkid;
	}

	public String getInfo_date() {
		return info_date;
	}

	public void setInfo_date(String infoDate) {
		info_date = infoDate;
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

	public String getTransmissionContent() {
		return transmissionContent;
	}

	public void setTransmissionContent(String transmissionContent) {
		this.transmissionContent = transmissionContent;
	}
}