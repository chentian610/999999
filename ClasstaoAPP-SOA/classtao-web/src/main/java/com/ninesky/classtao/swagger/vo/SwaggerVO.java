package com.ninesky.classtao.swagger.vo;

import java.util.HashMap;
import java.util.List;

public class SwaggerVO {

	private String swagger;
	
	private String host;
	
	private String basePath;
	
	private List<TagVO> tags;
	
	private HashMap<String,Object> paths;
	
	private SwaggerInfoVO info;
	
	public String getSwagger() {
		return swagger;
	}

	public void setSwagger(String swagger) {
		this.swagger = swagger;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public List<TagVO> getTags() {
		return tags;
	}

	public void setTags(List<TagVO> tags) {
		this.tags = tags;
	}

	public HashMap<String,Object> getPaths() {
		return paths;
	}

	public void setPaths(HashMap<String,Object> paths) {
		this.paths = paths;
	}

	public SwaggerInfoVO getInfo() {
		return info;
	}

	public void setInfo(SwaggerInfoVO info) {
		this.info = info;
	}

}
