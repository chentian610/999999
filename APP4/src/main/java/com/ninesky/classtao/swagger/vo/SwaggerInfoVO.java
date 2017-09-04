package com.ninesky.classtao.swagger.vo;

import java.util.HashMap;

public class SwaggerInfoVO {
	private String name;
	
	private String title = "课道平台API管理";
	
	private String version = "V3.8.0";
	
	private String description = "API接口文档管理";
	
	private String termsOfService ="www.classtao.com";
	
	private ConcatVO contact;
	
	private HashMap<String,Object> license;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(String termsOfService) {
		this.termsOfService = termsOfService;
	}

	public ConcatVO getContact() {
		return contact;
	}

	public void setContact(ConcatVO contact) {
		this.contact = contact;
	}

	public HashMap<String, Object> getLicense() {
		return license;
	}

	public void setLicense(HashMap<String, Object> license) {
		this.license = license;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
