package com.ninesky.classtao.message.service.impl;

import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.common.util.MD5Util;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.PostUtil;
import com.ninesky.framework.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
	private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	
	/**
	 * 发送手机短信
	 * @phone 手机号码
	 * @message 短信内容
	 * @return
	 */
	@Override
	public void sendMessage(String phone,String message) {
		if ("TEST".equals(SystemConfig.getProperty("MESSAGE_ENVIRONMENT")) && (!checkIsTestPhone(phone)))
		{
			logger.error("该号码:"+phone+"没有接收短信的权限！");
			return;
		} else sendmsg(phone,message);
	}
	
	private static void sendmsg(String mobiles,String msgs) {	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", SystemConfig.getProperty("MESSAGE_SID"));
		map.put("cpid", SystemConfig.getProperty("MESSAGE_SID"));
		map.put("sign", MD5Util.toMd5(SystemConfig.getProperty("MESSAGE_CPID")+SystemConfig.getProperty("MESSAGE_KEY")));
		map.put("mobi", mobiles);
		try {
			byte[] b = Base64.encodeBase64(msgs.getBytes("UTF-8"));
			String Bmsgs =  URLEncoder.encode(new String(b),"UTF-8");
			map.put("msg", Bmsgs);
			String value=PostUtil.doPost(SystemConfig.getProperty("MESSAGE_BASEURL"), map);
			if (value.indexOf("1000")==-1)
				throw new BusinessException("发送短信 失败，详细信息："+value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 在这里写入能够接收短信的手机号码，防止批量发送的时候非开发者接收到短信
	 * @param phoneNo
	 * @return
	 */
	private static boolean checkIsTestPhone(String phoneNo) {
		return phoneNo.equals("18857169874") 
			|| phoneNo.equals("18368085187")
			|| phoneNo.equals("18317893352")
			|| phoneNo.equals("13296541882")
			|| phoneNo.equals("18257166300")
		    || phoneNo.equals("15120079336")
		    || phoneNo.equals("18758063413"); 
	}
}
