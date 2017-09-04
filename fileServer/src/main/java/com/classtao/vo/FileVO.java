package com.classtao.vo;


public class FileVO {


	/**
	* 原始文件名
	*/
	private String file_name;

	/**
	* 存储的文件名
	*/
	private String store_name;
	
	/**
	* 存储的url
	*/
	private String file_url;
	
	/**
	* 存储的缩略url
	*/
	private String file_resize_url;
	
	/**
	* 文件类型
	*/
	private String file_type;
	
	/**
	 * 文件真实名称，不是生成的数字串
	 */
	private String file_real_name;
	
	/**
	 * 文件大小
	 */
	private String file_size;
	
	
	/**
	 * 文件服务器存放绝对路径
	 */
	private String file_path;
	
	/**
	 * 多文件的索引
	 */
	private String file_index;


	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getFile_resize_url() {
		return file_resize_url;
	}

	public void setFile_resize_url(String file_resize_url) {
		this.file_resize_url = file_resize_url;
	}

	public String getFile_real_name() {
		return file_real_name;
	}

	public void setFile_real_name(String file_real_name) {
		this.file_real_name = file_real_name;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getFile_index() {
		return file_index;
	}

	public void setFile_index(String file_index) {
		this.file_index = file_index;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}


}