package com.ninesky.framework;

import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用来统一封装异常信息，可以在产生异常的时候同时将错误信息进行封装成需要的格式：比如JSON、XML等
 * @author Chenth
 */
public class BaseController {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);
	/**
	* 异常控制，这便是异常细节可控，将来可用于支持国际化（异常信息国际化）
	**/
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value= HttpStatus.OK)
	public @ResponseBody
    Object handleException(Exception ex, HttpServletRequest request) {
		//写入错误日志
		String requestUrl = request.getRequestURI().replaceFirst(request.getContextPath(), "");
		String ip_address = request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP");
		logger.error("**********************REQUEST-URL:"+requestUrl+"***********************");
		logger.error("**********************IP-ADRESS:"+ip_address+",SessionID:"+request.getSession().getId());
		logger.error("**********************ERROR HAPPEND PARAM:"+ ActionUtil.getActionParam());
		logger.error(ex.getMessage(), ex);

		if (ex instanceof CacheException) return ResponseUtils.sendFailure(ex.getMessage(),100);
		insert2DB(ex, requestUrl,ip_address);
		if (ex instanceof AuthException) return ResponseUtils.sendFailure(ex.getMessage(),((AuthException) ex).getError_code());
		if (ex instanceof BusinessException) return ResponseUtils.sendFailure(ex.getMessage(),((BusinessException) ex).getError_code());
		else return ResponseUtils.sendFailure(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
	}

	/**
	 * 将异常信息存入到数据库
	 * @param ex
	 * @param requestUrl
	 */
	private void insert2DB(Exception ex, String requestUrl, String ip_address) {
		//将错误日志存入数据库
		GeneralDAO dao= SpringBeanUtil.getBean(GeneralDAO.class);
		LogVO log=new LogVO();
		log.setMethod(requestUrl);
		log.setParameter(ActionUtil.getActionParam().toString());
		log.setIp_address(ip_address);
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw, true));
		log.setMsg(sw.toString());
		log.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("logMap.insertErrorLog",log);
	}
}
