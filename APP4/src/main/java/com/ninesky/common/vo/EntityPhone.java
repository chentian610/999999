package com.ninesky.common.vo;

public class EntityPhone {

	private String phoneNo; //电话号码
	
	private String spType;  //sp类型
	
	private String areaCode; //地区代码
	
	private String content; //内容
	
	private String codesrt;
	
	private String isNation;
	
	private Integer type;//信息类型 1：通知 2：验证码

	public EntityPhone() {}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSpType() {
		return spType;
	}

	public void setSpType(String spType) {
		this.spType = spType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCodesrt() {
		return codesrt;
	}

	public void setCodesrt(String codesrt) {
		this.codesrt = codesrt;
	}

	public String getIsNation() {
		return isNation;
	}

	public void setIsNation(String isNation) {
		this.isNation = isNation;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
