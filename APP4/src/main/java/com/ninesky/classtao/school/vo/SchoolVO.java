package com.ninesky.classtao.school.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class SchoolVO extends BaseVO{
	/**
	* 学校ID，主键
	*/
	private Integer school_id;
	
	/**
	* 学校类型
	*/
	private String school_type;

	/**
	* 学校名称，唯一
	*/
	private String school_name;

	/**
	* 学校管理员
	*/
	private String school_admin;

	/**
	* 学校管理员电话
	*/
	private String school_admin_phone;
	
	/**
	* 学校英文名称
	*/
	private String english_name;
	
	/**
	* 校训
	*/
	private String school_motto;
	
	/**
	* 代理商手机号码
	*/
	private String agent_phone;


	/**
	* 学校组织机构图
	*/
	private String organize_pic_url;

	/**
	* 学校申请状态：007
	*/
	private String status;

	/**
	* 省份
	*/
	private String province;

	/**
	* 城市
	*/
	private String city;

	/**
	* 区县
	*/
	private String county;

	/**
	* 地址
	*/
	private String address;

	private String longitude;

	private String latitude;

	private Integer attend_range;

	/**
	* 学校电话
	*/
	private String phone;

	/**
	* 域名
	*/
	private String domain;

	/**
	* APP名
	*/
	private String app_name;

	/**
	* 学校APP图
	*/
	private String app_pic_url;
	
	/**
	* 名人墙图
	*/
	private String fistpage_url;
	
	/**
	* 名人墙首页类型
	*/
	private String fistpage_type;

	/**
	* App申请状态：007
	*/
	private String app_status;

	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新日期
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;
	/**
	 * 学校申请模块id串,逗号隔开
	 */
	private String module_ids;
	/**
	 * 学校申请模块code串,逗号隔开
	 */
	private String module_codes;
	/**
	 * 拒绝申请的原因
	 */
	private String content;
	
	/**
	 * APP安装地址
	 */
	private String install_url;

	private String app_list;

	private String school_server;

	private String valid_date;

	private String agent_name;

	private Integer unit_price;

	private Integer agent_id;

	private String client_id;

	private String main_url;

	private String user_role_list;

	private String record_no;
	private String host_url;
	private String main_domain;
	private String copyright;
	private String manager_url;
	private String app_dict;
	private String start_work_date;
	private String end_work_date;
	private String start_school_date;
	private String end_school_date;
	private String app_main_url;

	public String getApp_main_url() {
		return app_main_url;
	}

	public void setApp_main_url(String app_main_url) {
		this.app_main_url = app_main_url;
	}

	public String getEnd_school_date() {
		return end_school_date;
	}

	public void setEnd_school_date(String end_school_date) {
		this.end_school_date = end_school_date;
	}

	public String getStart_school_date() {
		return start_school_date;
	}

	public void setStart_school_date(String start_school_date) {
		this.start_school_date = start_school_date;
	}

	public String getEnd_work_date() {
		return end_work_date;
	}

	public void setEnd_work_date(String end_work_date) {
		this.end_work_date = end_work_date;
	}

	public String getStart_work_date() {
		return start_work_date;
	}

	public void setStart_work_date(String start_work_date) {
		this.start_work_date = start_work_date;
	}

	public String getApp_dict() {
		return app_dict;
	}

	public void setApp_dict(String app_dict) {
		this.app_dict = app_dict;
	}

	public String getManager_url() {
		return manager_url;
	}

	public void setManager_url(String manager_url) {
		this.manager_url = manager_url;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getMain_domain() {
		return main_domain;
	}

	public void setMain_domain(String main_domain) {
		this.main_domain = main_domain;
	}

	public String getHost_url() {
		return host_url;
	}

	public void setHost_url(String host_url) {
		this.host_url = host_url;
	}

	public String getRecord_no() {
		return record_no;
	}

	public void setRecord_no(String record_no) {
		this.record_no = record_no;
	}

	public String getUser_role_list() {
		return user_role_list;
	}

	public void setUser_role_list(String user_role_list) {
		this.user_role_list = user_role_list;
	}

	public String getMain_url() {
		return main_url;
	}

	public void setMain_url(String main_url) {
		this.main_url = main_url;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setAgent_id(Integer agent_id) {
		this.agent_id = agent_id;
	}

	public Integer getAgent_id() {
		return agent_id;
	}

	public void setUnit_price(Integer unit_price) {
		this.unit_price = unit_price;
	}

	public Integer getUnit_price() {
		return unit_price;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getAgent_name() { return agent_name; }

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public String getValid_date() {
		return valid_date;
	}

	public void setSchool_server(String school_server) {
		this.school_server = school_server;
	}

	public String getSchool_server() {
		return school_server;
	}

	public void setApp_list(String app_list) {
		this.app_list = app_list;
	}

	public String getApp_list() {
		return app_list;
	}

	public String getInstall_url() {
		return install_url;
	}

	public void setInstall_url(String install_url) {
		this.install_url = install_url;
	}

	public void setSchool_name(String school_name)  {
		this.school_name = school_name;
	}

	public String getSchool_name()  {
		return school_name;
	}

	public void setSchool_admin(String school_admin)  {
		this.school_admin = school_admin;
	}

	public String getSchool_admin()  {
		return school_admin;
	}

	public void setSchool_admin_phone(String school_admin_phone)  {
		this.school_admin_phone = school_admin_phone;
	}

	public String getSchool_admin_phone()  {
		return school_admin_phone;
	}

	public void setOrganize_pic_url(String organize_pic_url)  {
		this.organize_pic_url = organize_pic_url;
	}

	public String getOrganize_pic_url()  {
		return organize_pic_url;
	}

	public void setStatus(String status)  {
		this.status = status;
	}

	public String getStatus()  {
		return status;
	}

	public void setProvince(String province)  {
		this.province = province;
	}

	public String getProvince()  {
		return province;
	}

	public void setCity(String city)  {
		this.city = city;
	}

	public String getCity()  {
		return city;
	}

	public void setCounty(String county)  {
		this.county = county;
	}

	public String getCounty()  {
		return county;
	}

	public void setAddress(String address)  {
		this.address = address;
	}

	public String getAddress()  {
		return address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getAttend_range() {
		return attend_range;
	}

	public void setAttend_range(Integer attend_range) {
		this.attend_range = attend_range;
	}

	public void setPhone(String phone)  {
		this.phone = phone;
	}

	public String getPhone()  {
		return phone;
	}

	public void setDomain(String domain)  {
		this.domain = domain;
	}

	public String getDomain()  {
		return domain;
	}

	public void setApp_name(String app_name)  {
		this.app_name = app_name;
	}

	public String getApp_name()  {
		return app_name;
	}

	public void setApp_pic_url(String app_pic_url)  {
		this.app_pic_url = app_pic_url;
	}

	public String getApp_pic_url()  {
		return app_pic_url;
	}

	public void setApp_status(String app_status)  {
		this.app_status = app_status;
	}

	public String getApp_status()  {
		return app_status;
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

	public void setUpdate_by(Integer update_by)  {
		this.update_by = update_by;
	}

	public Integer getUpdate_by()  {
		return update_by;
	}

	public void setUpdate_date(Date update_date)  {
		this.update_date = update_date;
	}

	public Date getUpdate_date()  {
		return update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getEnglish_name() {
		return english_name;
	}

	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}

	public String getSchool_motto() {
		return school_motto;
	}

	public void setSchool_motto(String school_motto) {
		this.school_motto = school_motto;
	}

	public String getModule_ids() {
		return module_ids;
	}

	public void setModule_ids(String module_ids) {
		this.module_ids = module_ids;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSchool_type() {
		return school_type;
	}

	public void setSchool_type(String school_type) {
		this.school_type = school_type;
	}

	public String getModule_codes() {
		return module_codes;
	}

	public void setModule_codes(String module_codes) {
		this.module_codes = module_codes;
	}
	public String getFistpage_url() {
		return fistpage_url;
	}

	public void setFistpage_url(String fistpage_url) {
		this.fistpage_url = fistpage_url;
	}
	public String getFistpage_type() {
		return fistpage_type;
	}

	public void setFistpage_type(String fistpage_type) {
		this.fistpage_type = fistpage_type;
	}

	public String getAgent_phone() {
		return agent_phone;
	}

	public void setAgent_phone(String agent_phone) {
		this.agent_phone = agent_phone;
	}

}