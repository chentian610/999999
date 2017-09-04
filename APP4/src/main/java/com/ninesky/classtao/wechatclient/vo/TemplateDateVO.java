package com.ninesky.classtao.wechatclient.vo;

public class TemplateDateVO {
	
	private String value;
	
	private String color;
	
	public TemplateDateVO(){}
	
	public TemplateDateVO(String value, String color){
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
