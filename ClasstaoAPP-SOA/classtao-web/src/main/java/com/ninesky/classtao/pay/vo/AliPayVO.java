package com.ninesky.classtao.pay.vo;

import java.util.Date;

public class AliPayVO {


	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 支付宝APP_ID
	*/
	private String alipay_app_id;

	private String alipay_gateway_url;

	/**
	 * 支付宝应用公钥
	 */
	private String alipay_app_public_key;

	/**
	* 支付宝应用私钥
	*/
	private String alipay_app_private_key;

	/**
	* 支付宝应用公钥
	*/
	private String alipay_public_key;

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setAlipay_app_id(String alipay_app_id)  {
		this.alipay_app_id = alipay_app_id;
	}

	public String getAlipay_app_id()  {
		return alipay_app_id;
	}

	public void setAlipay_app_private_key(String alipay_app_private_key)  {
		this.alipay_app_private_key = alipay_app_private_key;
	}

	public String getAlipay_app_private_key()  {
		return alipay_app_private_key;
	}

	public void setAlipay_public_key(String alipay_public_key)  {
		this.alipay_public_key = alipay_public_key;
	}

	public String getAlipay_public_key()  {
		return alipay_public_key;
	}

	public String getAlipay_gateway_url() {
		return alipay_gateway_url;
	}

	public void setAlipay_gateway_url(String alipay_gateway_url) {
		this.alipay_gateway_url = alipay_gateway_url;
	}

	public String getAlipay_app_public_key() {
		return alipay_app_public_key;
	}

	public void setAlipay_app_public_key(String alipay_app_public_key) {
		this.alipay_app_public_key = alipay_app_public_key;
	}

}