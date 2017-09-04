package com.ninesky.classtao.wechat.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.framework.BaseController;

public class WxBaseController extends BaseController{
	
	private static final String SESSION_WX_ACCOUNT = "session-weixin-account";
	
	@Autowired
	protected WxAccountService wxAccountService;
	@Autowired
	protected WxApiService wxApiService;
	
	protected WxAccountVO getSessionWxAccount(){
		HttpServletRequest request = getRequest();
		WxAccountVO account = (WxAccountVO) request.getSession().getAttribute(SESSION_WX_ACCOUNT);
		if(account == null || StringUtils.isEmpty(account.getPlatform_account_id())){
			//通过学校获取微信公众号
			account = wxAccountService.getAccountBySchool();
			if(account == null){
				return null;
			}
			request.getSession().setAttribute(SESSION_WX_ACCOUNT, account);
		}
		return account;
	}
	
	protected void updateSessionWxAccount(WxAccountVO account){
		getRequest().getSession().setAttribute(SESSION_WX_ACCOUNT, account);
	}
	
	protected HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
	}
	
}
