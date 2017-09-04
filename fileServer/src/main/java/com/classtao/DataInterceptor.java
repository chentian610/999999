package com.classtao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一请求拦截器，在这里进行一些基础框架数据的处理。。。。
 * @author Chenth
 */
public class DataInterceptor extends BaseController implements HandlerInterceptor{
	
	protected static Log logger = LogFactory.getLog(DataInterceptor.class);

	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler)  throws Exception {
		String requestUrl = request.getRequestURI().replaceFirst(request.getContextPath(), "");  
		logger.info("**********************RequestUrl:"+requestUrl+"***********************");
		logger.info("**********************IP-Adress:"+(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"))+",SessionID:"+request.getSession().getId());
		return true;
	}

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
