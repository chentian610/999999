package com.ninesky.classtao.wechatclient.message;

import java.util.Map;

import com.ninesky.classtao.wechatclient.vo.TemplateDateVO;

/**   
 * @Title: 微信模板消息接口
 * @Description: 微信模板消息接口类，用于推送微信模板消息
 * @author zhusong
 * @date 2016-11-28
 * @version V1.0   
 *
 */

public interface WechatMessage {
	public String getTemplateNumber();
	public Map<String, TemplateDateVO> convertTemplateDate();
}
