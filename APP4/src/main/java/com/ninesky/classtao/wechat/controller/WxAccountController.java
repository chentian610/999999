package com.ninesky.classtao.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.framework.ResponseUtils;

@RestController
@RequestMapping(value="wxAccountAction")
public class WxAccountController extends WxBaseController{

	/**
	 * 获得学校关联的公众号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAccount")
	public @ResponseBody Object getAccount(){
		return ResponseUtils.sendSuccess(getSessionWxAccount());
	}
	
	/**
	 * 保存公众号信息
	 * @param WxAccountVO
	 * @return
	 */
	@RequestMapping(value="/saveAccount")
	public @ResponseBody Object saveAccount(WxAccountVO vo){
		if(vo.getAccount_id() != null && vo.getAccount_id() > 0){
			wxAccountService.updateWxAccount(vo);
		}else{
			wxAccountService.addWxAccount(vo);
		}
		updateSessionWxAccount(vo);
		//同步公众号信息到微信管理平台
		final Integer accountId = vo.getAccount_id();
		new Thread(){
			public void run() {
				wxApiService.syncAccount(accountId);
			};
		}.start();
		return ResponseUtils.sendSuccess(vo); 
	}
}
