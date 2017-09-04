package com.ninesky.classtao.shiro;

import com.ninesky.common.Constants;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.ErrorCode;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.JWTUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.TokenState;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 自定义FormAuthenticationFilter
 * 1、身份校验，提供token校验功能
 * 2、扩展异步请求(ajax)认证提示功能,防止被返回页面
 * @author chenth
 *
 */
public class MyAuthorizationFilter extends FormAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(MyAuthorizationFilter.class);
    private static String XMLHttpRequest = "XMLHttpRequest";
    private static String X_Requested_With = "X-Requested-With";
    private static String UTF_8 = "utf-8";
    private static String JSON_TYPE = "application/json";

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     * onAccessDenied
     */
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        logger.info("**********************ACCESS RESULT:DENIED***********************");
        Map<String,Object> tokenMap = JWTUtil.TokenToMapFromRequest(request);
        //如果校验通过
        if (TokenState.VALID.equals(tokenMap.get(JWTUtil.STATE))){
            MyShiroToken myShiroToken = new MyShiroToken(tokenMap.get("phone")+"", tokenMap.get("pass_word")+"",tokenMap.get("jwt")+"");
            SecurityUtils.getSubject().login(myShiroToken);
            return true;
        }
        String requestedWith = request.getHeader(X_Requested_With);
        Subject subject = getSubject(servletRequest, servletResponse);
        //如果是ajax的未登录请求返回指定格式数据
        if ((subject.getPrincipal() == null && StringUtil.isNotEmpty(requestedWith) && XMLHttpRequest.equals(requestedWith)) || (this.pathsMatch("/*Action/**", servletRequest))){
            response.setCharacterEncoding(Constants.UTF_8);
            response.setContentType(Constants.JSON_TYPE);
            TokenState state = (TokenState) tokenMap.get(JWTUtil.STATE);
            switch (state) {
                case INVALID:
                    response.getWriter().write(BeanUtil.beanToJson(ResponseUtils.sendFailure(ErrorCode.TOKEN_INVALID)));
                    break;
                case EXPIRED:
                    response.getWriter().write(BeanUtil.beanToJson(ResponseUtils.sendFailure(ErrorCode.TOKEN_EXPIRED)));
                    break;
                default:
                    response.getWriter().write(BeanUtil.beanToJson(ResponseUtils.sendFailure(ErrorCode.NO_LOGIN)));
                    break;
            }
            return false;
        } else return super.onAccessDenied(servletRequest, servletResponse);
    }

    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUrl = httpServletRequest.getRequestURI().replaceFirst(httpServletRequest.getContextPath(), "");
        logger.info("**********************REQUEST-URL:"+requestUrl+"***********************");
        logger.info("**********************IP-ADRESS:"+(httpServletRequest.getHeader("X-Real-IP")==null?httpServletRequest.getRemoteAddr():httpServletRequest.getHeader("X-Real-IP"))+",SessionID:"+httpServletRequest.getSession().getId());

        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }
}