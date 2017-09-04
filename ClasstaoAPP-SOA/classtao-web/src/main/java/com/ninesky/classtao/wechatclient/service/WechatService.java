package com.ninesky.classtao.wechatclient.service;

import java.util.Map;

import com.ninesky.classtao.wechatclient.vo.WechatUserVO;
import com.ninesky.classtao.wechatclient.vo.WxAccountTemplateVO;

public interface WechatService {
	
	/**
	 * 获取授权的微信用户信息
	 */
	public WechatUserVO getWechatUser(String appId, String secret, String code);
	
	/**
	 * 发送考勤异常提醒消息
	 */
	public void pushAttendanceMessage(Map<String, String> dataMap, Integer studentId, Integer schoolId);
	/**
	 * 添加微信消息模板
	 */
	public void addAccountTemplate(WxAccountTemplateVO vo);
	/**
	 * 根据课道平台公众号id和微信平台消息模板编号查询微信消息模板
	 */
	public WxAccountTemplateVO getAccountTemplate(Integer accountId, String templateNumber);

}
