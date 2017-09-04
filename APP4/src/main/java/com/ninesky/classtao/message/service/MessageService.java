package com.ninesky.classtao.message.service;

public interface MessageService {
	
	/**
	 * 发送手机短信
	 * @param phone 手机号码
	 * @param message 短信内容
	 * @return
	 */
	public void sendMessage(String phone, String message);
}
