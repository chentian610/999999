package com.ninesky.classtao.teachCloud.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class SourceVO extends BaseVO {

	
	
	/**
	 * 消息id，资源主键
	 */
	private Integer id;
	
	/**
	 * 接收者（json数组格式）
	 */
	private String source_list;
	
	/**
	 * 教学云资源唯一ID
	 */
	private String source_id;
	
	/**
	 * 发送者ID（教师ID）
	 */
	private Integer sender_id;
	
	/**
	 * 学校ID
	 */
	private Integer school_id;
	
	/**
	* 发送对象：班级id串 用逗号“,”隔开
	*/
	private String class_ids;
	
	/**
	 * 发送者姓名（教师姓名）
	 */
	private String sender_name;
	
	/**
	 * 资源名称
	 */
	private String source_name;
	
	/**
	 * 资源类型（word、excel、PPT、视频）
	 */
	private String source_type;
	
	/**
	 * 用户类型
	 */
	private String user_type;
	
	/**
	 * 教师推荐留言备注
	 */
	private String remark;
	
	/**
	 * 发送时间
	 */
	private Date send_time;
	
	/**
	 * 教学云提供的json数据
	 */
	private String source_data;

	/**
	 * 模块code，生成动态使用
	 */
	private String module_code;
	
	/*--------------WEB端-------------*/
	
	/**
	 * 上传时间（WEB端）
	 * @return
	 */
	private String updateTime;
	
	/**
	 * 资源文件大小（WEB端）
	 * @return
	 */
	private String resourceSize;
	
	/**
	 * 阅读次数
	 * @return
	 */
	private Integer browse_count;
	
	/**
	 * 点赞次数
	 * @return
	 */
	private Integer praise_count;
	
	/**
	 * 收藏次數
	 */
	private Integer fav_count;
	
	/**
	 * 下载次数
	 */
	private Integer download_count;
	
	/**
	 * 资源格式
	 * @return
	 */
	private String extensionName;
	
	/**
	 * 资源地址
	 * @return
	 */
	private String resourceUrl;
	
    
	
	public String getSource_list() {
		return source_list;
	}

	public void setSource_list(String source_list) {
		this.source_list = source_list;
	}
	
	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClass_ids() {
		return class_ids;
	}

	public void setClass_ids(String class_ids) {
		this.class_ids = class_ids;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public String getSource_data() {
		return source_data;
	}

	public void setSource_data(String source_data) {
		this.source_data = source_data;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getResourceSize() {
		return resourceSize;
	}

	public void setResourceSize(String resourceSize) {
		this.resourceSize = resourceSize;
	}

	public Integer getPraise_count() {
		return praise_count;
	}

	public void setPraise_count(Integer praise_count) {
		this.praise_count = praise_count;
	}

	public Integer getBrowse_count() {
		return browse_count;
	}

	public void setBrowse_count(Integer browse_count) {
		this.browse_count = browse_count;
	}

	public Integer getFav_count() {
		return fav_count;
	}

	public void setFav_count(Integer fav_count) {
		this.fav_count = fav_count;
	}

	public Integer getDownload_count() {
		return download_count;
	}

	public void setDownload_count(Integer download_count) {
		this.download_count = download_count;
	}

	public String getExtensionName() {
		return extensionName;
	}

	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
}
