package com.ninesky.classtao.homework.vo;

import com.ninesky.common.vo.BaseVO;


public class HomeworkFileVO extends BaseVO{

	/**
	 * id
	 */
	private Integer id;
	/**
	* 消息ID，外键
	*/
	private Integer homework_id;
	
	/**
	 * 作业子项id
	 */
	private Integer item_id;
	/**
	* 文件类型
	*/
	private String file_type;

	/**
	* 文件名
	*/
	private String file_name;

	/**
	* 文件URL,绝对路径
	*/
	private String file_url;

	/**
	* 图片缩略图URL,绝对路径
	*/
	private String file_resize_url;
	
	/**
	 * 播放时间长度  单位：秒
	 */
	private Integer play_time;
	
	public Integer  getPlay_time() {
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

	public void setHomework_id(Integer homework_id)  {
		this.homework_id = homework_id;
	}

	public Integer getHomework_id()  {
		return homework_id;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public void setFile_name(String file_name)  {
		this.file_name = file_name;
	}

	public String getFile_name()  {
		return file_name;
	}

	public void setFile_url(String file_url)  {
		this.file_url = file_url;
	}

	public String getFile_url()  {
		return file_url;
	}
	
	public String getFile_resize_url() {
		return file_resize_url;
	}

	public void setFile_resize_url(String fileResizeUrl) {
		file_resize_url = fileResizeUrl;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String fileType) {
		file_type = fileType;
	}
}