package com.ninesky.classtao.dynamic.vo;

import com.ninesky.common.vo.BaseVO;

public class DynamicVO extends BaseVO{
	/**
	* 模块编码
	*/
	private String module_code;
	
	/**
	* 模块的主键ID
	*/
	private Integer module_pkid;
	/**
	* 是否未读
	*/
	private boolean un_read;
	
	/**
	* 分页开始KEY
	*/
	private String start_key;
	
	/**
	* 动态Redis服务KEY
	*/
	private String key;

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public Integer getModule_pkid() {
		return module_pkid;
	}

	public void setModule_pkid(Integer module_pkid) {
		this.module_pkid = module_pkid;
	}

	public boolean isUn_read() {
		return un_read;
	}

	public void setUn_read(boolean un_read) {
		this.un_read = un_read;
	}

	public String getStart_key() {
		return start_key;
	}

	public void setStart_key(String start_key) {
		this.start_key = start_key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	

}
