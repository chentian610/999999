package com.ninesky.classtao.wechatclient.message;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.classtao.wechatclient.util.WechatUtil;
import com.ninesky.classtao.wechatclient.vo.WxAccountTemplateVO;

public class WechatGetuiPush implements Runnable{
	
	private static Logger logger = LoggerFactory.getLogger(WechatGetuiPush.class);
	
	private WxAccountService wxAccountService;
	
	private WxApiService wxApiService;
	
	private WechatService wechatService;
	
	private WechatMessage message;

	private Integer schoolId;
	
	private List<String> openIds;
	
	private String url;
	
	private String accessToken;

	public WechatGetuiPush(){}
	
	public WechatGetuiPush(WxAccountService wxAccountService, WxApiService wxApiService, WechatService wechatService, Integer schoolId, String url, List<String> openIds, WechatMessage message, String accessToken){
		this.wxAccountService = wxAccountService;
		this.wxApiService = wxApiService;
		this.wechatService = wechatService;
		this.schoolId = schoolId;
		this.url = url;
		this.openIds = openIds;
		this.message = message;
		this.accessToken = accessToken;
	}

	@Override
	public void run() {
		WxAccountVO account = wxAccountService.getAccountBySchool(schoolId);
		WxAccountTemplateVO template = wechatService.getAccountTemplate(account.getAccount_id(), message.getTemplateNumber());
		if(template == null){
			logger.error("获取公众号【" + account.getAccount_name() + "】的" + message.getTemplateNumber() + "模板失败");
			return;
		}
		if(StringUtils.isEmpty(accessToken)){
			accessToken = wxApiService.loadAccesstoken(account);
		}
		WechatUtil.sendTemplateMessage(accessToken, openIds, template.getWechat_template_id(), url, message.convertTemplateDate());
	}
}
