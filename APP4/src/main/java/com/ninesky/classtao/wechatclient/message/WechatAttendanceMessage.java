package com.ninesky.classtao.wechatclient.message;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ninesky.classtao.wechatclient.vo.TemplateDateVO;
import com.ninesky.common.Constants;


/**   
 * @Title: 考勤异常提醒-微信模板消息
 * @Description: 考勤异常提醒微信模板消息实体
 * @author zhusong
 * @date 2016-11-28
 * @version V1.0   
 *
 * 模板格式：
 * {{first.DATA}}
 * 考勤日期：{{date.DATA}}
 * 班级名称：{{class.DATA}}
 * 学生姓名：{{name.DATA}}
 * 异常情况：{{info.DATA}}
 * {{remark.DATA}}
 */

public class WechatAttendanceMessage implements WechatMessage{
	
	public WechatAttendanceMessage(){}

	public WechatAttendanceMessage(String title, String date,
			String className, String name, String info, String remark) {
		this.title = title;
		this.date = date;
		this.className = className;
		this.name = name;
		this.info = info;
		this.remark = remark;
	}

	private String title;
	
	private String date;
	
	private String className;
	
	private String name;
	
	private String info;
	
	private String remark;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
		map.put("date", new TemplateDateVO(this.date, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("Class", new TemplateDateVO(this.className, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("name", new TemplateDateVO(this.name, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("info", new TemplateDateVO(this.info, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		map.put("remark", new TemplateDateVO(this.remark, Constants.WECHAT_MESSAGE_TEMPLATE_COLOR));
		return map;
	}

	@Override
	public String getTemplateNumber() {
		return Constants.WECHAT_MESSAGE_TEMPLATE_KQ;
	}
}
