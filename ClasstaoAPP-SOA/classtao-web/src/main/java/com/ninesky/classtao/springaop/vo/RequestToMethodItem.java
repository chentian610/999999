package com.ninesky.classtao.springaop.vo;

public class RequestToMethodItem
    {
        public String controllerName;
        public String methodName;
        public String requestType;
        public String requestUrl;
        public Class<?>[] methodParmaTypes;

        public RequestToMethodItem(String requestUrl, String requestType, String controllerName, String requestMethodName,
Class<?>[] methodParmaTypes)
        {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.controllerName = controllerName;
            this.methodName = requestMethodName;
            this.methodParmaTypes = methodParmaTypes;

        }
    }