package com.ninesky.classtao.wechat.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.framework.ResponseUtils;

@RestController
@RequestMapping(value="wxGroupMessageAction")
public class GroupMessageController extends WxBaseController{

	@Autowired
	private WxApiService wxApiService;
	/**
	 *群发消息
	 * @return
	 */
	@RequestMapping(value="/sendMessage")
	public @ResponseBody Object sendMessage(HttpServletRequest request){
		
		final String msgtype = request.getParameter("msgtype");
		final String textcontent = request.getParameter("textcontent");
		final String newsTemplateId = request.getParameter("newsTemplateId");
		if(StringUtils.isEmpty(msgtype)){
			return ResponseUtils.sendFailure("缺少消息类型参数");
		}
		WxAccountVO account = getSessionWxAccount();
		if(account == null){
			return ResponseUtils.sendFailure("请在创建微信公众账号后，再进行群发消息操作");
		}
		
		final String platformAccountId = account.getPlatform_account_id();
		if(StringUtils.isEmpty(platformAccountId)){
			return ResponseUtils.sendFailure("微信公众账号未能创建成功，请联系管理员进行核实");
		}
		new Thread(){
			public void run() {
				wxApiService.sendGroupMessage(msgtype, textcontent, newsTemplateId, platformAccountId);
			};
		}.start();
		return ResponseUtils.sendSuccess();
	}
}
