package com.ninesky.classtao.swagger.vo;

import java.util.HashMap;

public class ResponseVO {
	private HashMap<String,Object> schema;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<String,Object> getSchema() {
		return schema;
	}

	public void setSchema(HashMap<String,Object> schema) {
		this.schema = schema;
	}

}
