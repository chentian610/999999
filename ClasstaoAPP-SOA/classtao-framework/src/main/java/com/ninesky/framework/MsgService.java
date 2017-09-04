package com.ninesky.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * 通用消息配置类，将中文提示配置到消息文件里面，用代码获取，防止乱码
 * @author Chenth
 */
public class MsgService {
	private static ResourceBundle rb = ResourceBundle.getBundle("messages");
	private static Logger logger = LoggerFactory.getLogger(MsgService.class);
	
	public static String getMsg(String key, Object...params){
		logger.info("MessageID:"+key);
		String msg = null;
		if(!rb.containsKey(key)) {
			msg = rb.getString("提示信息出错，消息代码无效");
			msg = MessageFormat.format(msg, key);
		}else{
			msg = rb.getString(key);
			msg = MessageFormat.format(msg, params);
		}
		return msg;
	}
	
	public static String getMsg(String key){
		logger.info("MessageID:"+key);
		if(!rb.containsKey(key)) return "";
		else return	rb.getString(key);
	}
	
}
