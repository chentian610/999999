package com.ninesky.classtao.notice.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class NoticeFileVO extends BaseVO{

	private Integer id;
	/**
	* 消息ID，外键
	*/
	private Integer notice_id;

	/**
	* 文件URL,绝对路径
	*/
	private String file_url;
	
	/**
	* 文件缩略图URL,绝对路径
	*/
	private String file_resize_url;
	
	/**
	 * 音频播放时间长度
	 */
	private Integer play_time;
	
	/**
	 * 文件名称
	 */
	private String file_name;
	
	/**
	 * 文件类型
	 */
	private String file_type;
	
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

	public Integer getPlay_time() {
		return play_time;
	}

	public void setPlay_time(Integer play_time) {
		this.play_time = play_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNotice_id(Integer notice_id)  {
		this.notice_id = notice_id;
	}

	public Integer getNotice_id()  {
		return notice_id;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
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

	public String getFile_resize_url() {
		return file_resize_url;
	}

	public void setFile_resize_url(String file_resize_url) {
		this.file_resize_url = file_resize_url;
	}

}