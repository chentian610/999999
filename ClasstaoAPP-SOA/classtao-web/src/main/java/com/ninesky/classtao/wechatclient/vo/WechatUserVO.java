package com.ninesky.classtao.wechatclient.vo;

/**   
 * @Title: 微信用户
 * @Description: 微信用户实体
 * @author zhusong
 * @date 2016-11-10
 * @version V1.0   
 *
 */

public class WechatUserVO {
	
	/**
	 * 用户的标识，对当前公众号唯一
	 */
	private String open_id;
	/**
	 * 用户的昵称
	 */
	private String nick_name;
	/**
	 * 用户头像
	 */
	private String head_img_url;
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	private int sex;

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getHead_img_url() {
		return head_img_url;
	}

	public void setHead_img_url(String head_img_url) {
		this.head_img_url = head_img_url;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "WechatUserVO [open_id=" + open_id + ", nick_name=" + nick_name
				+ ", head_img_url=" + head_img_url + ", sex=" + sex + "]";
	}
}
