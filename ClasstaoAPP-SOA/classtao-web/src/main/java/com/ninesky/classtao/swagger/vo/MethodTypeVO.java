package com.ninesky.classtao.swagger.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MethodTypeVO {
	
	private MethodVO post;
	
	private MethodVO get;
	
	private MethodVO delete;
	
	private MethodVO put;

	public MethodVO getPost() {
		return post;
	}

	public void setPost(MethodVO post) {
		this.post = post;
	}

	public MethodVO getGet() {
		return get;
	}

	public void setGet(MethodVO get) {
		this.get = get;
	}

	public MethodVO getDelete() {
		return delete;
	}

	public void setDelete(MethodVO delete) {
		this.delete = delete;
	}

	public MethodVO getPut() {
		return put;
	}

	public void setPut(MethodVO put) {
		this.put = put;
	}
}
