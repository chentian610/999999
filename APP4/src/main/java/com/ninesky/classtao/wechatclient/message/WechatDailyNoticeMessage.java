package com.ninesky.classtao.wechatclient.message;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ninesky.classtao.wechatclient.vo.TemplateDateVO;
import com.ninesky.common.Constants;

/**   
 * @Title: 每日汇总消息提醒-微信模板消息
 * @Description: 每日汇总消息提醒微信模板消息实体
 * @author zhusong
 * @date 2016-11-28
 * @version V1.0   
 *
 * 模板格式
 * {{first.DATA}}
 * 老师：{{keyword1.DATA}}
 * 时间：{{keyword2.DATA}}
 * 消息内容：{{keyword3.DATA}}
 * {{remark.DATA}}
 */

public class WechatDailyNoticeMessage implements WechatMessage{
	
	public WechatDailyNoticeMessage(){}
	
	public WechatDailyNoticeMessage(String title, String teacher,
			String date, String message, String remark) {
		this.title = title;
		this.teacher = teacher;
		this.date = date;
		this.message = message;
		this.remark = remark;
	}

	private String title;
	
	private String teacher;
	
	private String date; 
	
	private String message;
	
	private String remark;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public Map<String, TemplateDateVO> convertTemplateDate() {
		Map<String, TemplateDateVO> map = new LinkedHashMap<String, TemplateDateVO>();
		map.put("first", new TemplateDateVO(this.title, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("keyword1", new TemplateDateVO(this.teacher, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("keyword2", new TemplateDateVO(this.date, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("keyword3", new TemplateDateVO(this.message, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("remark", new TemplateDateVO(this.remark, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		return map;
	}
	
	@Override
	public String getTemplateNumber() {
		return Constants.WECHAT_MESSAGE_TEMPLATE_SX;
	}
}
