package com.ninesky.classtao.message.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.classtao.message.service.MessageService;

@RestController
@RequestMapping(value="messageAction")
public class MessageController extends BaseController{
	
	@Autowired
	private MessageService MessageService;
	/**
	 * 获取消息，一般在界面打开的时候，可以获取单条或者多条消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendMessage")
	public Object sendMessage(HttpServletRequest request){
		MessageService.sendMessage("18857169874", MsgService.getMsg("MESSAGE_VCODE_DEFAULT","458962"));
		return ResponseUtils.sendSuccess();
	}
}
