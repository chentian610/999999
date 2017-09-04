package com.ninesky.classtao.shiro;

import com.ninesky.common.Constants;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.ErrorCode;
import com.ninesky.framework.JWTUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.TokenState;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 1.提供token校验功能，只校验token，给第三方系统接口使用
 * 目前使用到的系统有万家星城幼儿园门禁系统
 * @author chenth
 *
 */
public class MyAccessControlFilter extends AccessControlFilter {
    private static Logger logger = LoggerFactory.getLogger(MyAccessControlFilter.class);

    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String jwt = request.getHeader(JWTUtil.ACCESS_TOKEN);
        logger.info("**********************ACCESS TOKEN:"+ jwt +"***********************");
        Map<String, Object> tokenMap = JWTUtil.TokenToMap(jwt);
        TokenState state = (TokenState) tokenMap.get(JWTUtil.STATE);
        response.setCharacterEncoding(Constants.UTF_8);
        response.setContentType(Constants.JSON_TYPE);
        switch (state) {
            case VALID:
                //校验成功
                return true;
            case EXPIRED:
                response.getWriter().write(BeanUtil.beanToJson(ResponseUtils.sendFailure(ErrorCode.TOKEN_EXPIRED)));
                return false;
            default:
                response.getWriter().write(BeanUtil.beanToJson(ResponseUtils.sendFailure(ErrorCode.TOKEN_INVALID)));
                return false;
        }
    }

    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUrl = httpServletRequest.getRequestURI().replaceFirst(httpServletRequest.getContextPath(), "");
        logger.info("**********************REQUEST-URL:"+requestUrl+"***********************");
        logger.info("**********************IP-ADRESS:"+(httpServletRequest.getHeader("X-Real-IP")==null?httpServletRequest.getRemoteAddr():httpServletRequest.getHeader("X-Real-IP"))+",SessionID:"+httpServletRequest.getSession().getId());

        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }
}