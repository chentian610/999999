package com.ninesky.classtao.swagger.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MethodVO {
    public String controllerName;
    public String methodName;
    public String requestType;
    public String requestUrl;
    public Class<?>[] methodParmaTypes;
    public List<String> tags;
    public String summary;
    public String operationId;
    public List<String> consumes;
    public List<String> produces;
    public List<ParamVO> parameters;
    
    public HashMap<String,Object> responses;
    
	
    public String getControllerName() {
		return controllerName;
	}


	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}


	public String getRequestType() {
		return requestType;
	}


	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	public String getRequestUrl() {
		return requestUrl;
	}


	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}


	public Class<?>[] getMethodParmaTypes() {
		return methodParmaTypes;
	}


	public void setMethodParmaTypes(Class<?>[] methodParmaTypes) {
		this.methodParmaTypes = methodParmaTypes;
	}


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getOperationId() {
		return operationId;
	}


	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}


	public List<String> getConsumes() {
		return consumes;
	}


	public void setConsumes(List<String> consumes) {
		this.consumes = consumes;
	}


	public List<String> getProduces() {
		return produces;
	}


	public void setProduces(List<String> produces) {
		this.produces = produces;
	}


	public List<ParamVO> getParameters() {
		return parameters;
	}


	public void setParameters(List<ParamVO> parameters) {
		this.parameters = parameters;
	}


	public HashMap<String, Object> getResponses() {
		return responses;
	}


	public void setResponses(HashMap<String, Object> responses) {
		this.responses = responses;
	}


	public MethodVO(String requestUrl, String requestType, String controllerName, String requestMethodName,Class<?>[] methodParmaTypes)
    {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.controllerName = controllerName;
        this.methodName = requestMethodName;
        this.methodParmaTypes = methodParmaTypes;
        this.tags = new ArrayList<String>();
        this.consumes= new ArrayList<String>();
        consumes.add("application/json");
        this.produces= new ArrayList<String>();
        produces.add("application/json");
        this.parameters= new ArrayList<ParamVO>();
        
		this.responses = new HashMap<String,Object>();
		HashMap<String,Object> response200 = new HashMap<String,Object>();
		response200.put("description", "OK");
		HashMap<String,Object> schema = new HashMap<String,Object>();
		schema.put("type", "string");
		response200.put("description", "username字段描述");
		response200.put("schema", schema);
		
		HashMap<String,Object> response400 = new HashMap<String,Object>();
		response400.put("description", "Invalid username/password supplied");
		
		HashMap<String,Object> response401 = new HashMap<String,Object>();
		response401.put("description", "Forbidden");
		
		HashMap<String,Object> response403 = new HashMap<String,Object>();
		response403.put("description", "Not Found");

		responses.put("200", response200);
		responses.put("400", response400);
		responses.put("401", response401);
		responses.put("403", response403);
    }
}
