package com.ninesky.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by jeff on 15/10/23.
 */
@Order(1)
@ControllerAdvice(basePackages = "com.ninesky.classtao")
public class MyRequestBodyAdvice implements RequestBodyAdvice {
	private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

	@Override
	public Object afterBodyRead(Object arg0, HttpInputMessage arg1,
			MethodParameter arg2, Type arg3,
			Class<? extends HttpMessageConverter<?>> arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage arg0,
			MethodParameter arg1, Type arg2,
			Class<? extends HttpMessageConverter<?>> arg3) throws IOException {
		logger.info("请不要离开我beforeBodyRead"); 
		return null;
	}

	@Override
	public Object handleEmptyBody(Object arg0, HttpInputMessage arg1,
			MethodParameter arg2, Type arg3,
			Class<? extends HttpMessageConverter<?>> arg4) {
		logger.info("请不要离开我handleEmptyBody");
		return null;
	}

	@Override
	public boolean supports(MethodParameter arg0, Type arg1,
			Class<? extends HttpMessageConverter<?>> arg2) {
		logger.info("请不要离开我supports");
		return false;
	}
}