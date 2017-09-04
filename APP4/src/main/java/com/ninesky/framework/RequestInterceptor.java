package com.ninesky.framework;

import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 统一请求拦截器，在这里进行一些基础框架数据的处理。。。。
 * @author Chenth
 */
public class RequestInterceptor extends BaseController implements HandlerInterceptor{
	private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
	public static final String PAGINATION_BEAN_ATTRIBUTE = "pagination_bean";

	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler)  throws Exception {
		String app_type=request.getParameter("app_type");
		if (Constants.ON.equals(SystemConfig.getProperty("JWT_ON"))) checkToken(request);
		if (IntegerUtil.isEmpty(ActionUtil.getUserID())){
			if (StringUtil.isEmpty(app_type)) {//web端
				String header=request.getHeader("x-requested-with");
				if (StringUtil.isNotEmpty(header)) {//ajax请求
					response.setHeader("sessionstatus", "timeout");//在响应头设置session状态   
					response.setHeader("url", request.getServerName());//服务器域名
				} 
			}
			throw new BusinessException("您还未登陆，请先登录系统......");
			}
		else return true;
	}

	/**
	 * 将请求里面的所有参数设置到Param中
	 * @return
	 */
	private static void checkToken(HttpServletRequest request) {
		//从请求头中获取token
		String token=request.getHeader("token");
		logger.info("checkSchoolToken JWT="+token);
        if (StringUtil.isEmpty(token)) return;
		Map<String, Object> resultMap=JWTUtil.TokenToMap(token);
		TokenState state=(TokenState)resultMap.get("state");
		switch (state) {
			case VALID:
			    //dosomething
                break;
			case EXPIRED:
                throw new AuthException(1002,"身份已经过期，请重新登录......");
			case INVALID:
                throw new AuthException(1001,"身份校验失败，请重新登录......");
		}
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
