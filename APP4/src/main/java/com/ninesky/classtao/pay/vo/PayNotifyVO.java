package com.ninesky.classtao.pay.vo;

import java.util.Date;

public class PayNotifyVO {


	/**
	* 未设置，请在数据库中设置
	*/
	private Integer id;

	/**
	* 支付宝通知ID
	*/
	private String notify_id;

	/**
	* 支付宝分配给开发者的应用ID
	*/
	private String app_id;

	/**
	* 请求使用的编码格式，如utf-8,gbk,gb2312等
	*/
	private String charset;

	/**
	* 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	*/
	private String sign_type;

	/**
	* 商户请求参数的签名串，详见签名
	*/
	private String sign;

	/**
	* 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
	*/
	private String out_trade_no;

	/**
	* 支付宝交易号
	*/
	private String trade_no;

	/**
	* 订单标题
	*/
	private String subject;

	/**
	* 交易金额
	*/
	private Double total_amount;

	/**
	* 实收金额
	*/
	private Double receipt_amount;

	/**
	* 买家付款的金额
	*/
	private Double buyer_pay_amount;

	/**
	* 买家的支付宝用户id，如果为空，会从传入了码值信息中获取买家ID
	*/
	private String buyer_id;

	/**
	* 买家支付宝账号
	*/
	private String buyer_logon_id;

	/**
	* 未设置，请在数据库中设置
	*/
	private String body;

	/**
	* 未设置，请在数据库中设置
	*/
	private String auth_app_id;

	/**
	* 未设置，请在数据库中设置
	*/
	private String notify_type;

	/**
	* 未设置，请在数据库中设置
	*/
	private String invoice_amount;

	/**
	 * 未设置，请在数据库中设置
	 */
	private String passback_params;

	/**
	* 未设置，请在数据库中设置
	*/
	private String trade_status;

	/**
	* 交易支付时间
	*/
	private String gmt_payment;

	/**
	* 未设置，请在数据库中设置
	*/
	private String point_amount;

	/**
	* 未设置，请在数据库中设置
	*/
	private String gmt_create;

	/**
	* 交易支付使用的资金渠道
	*/
	private String fund_bill_list;

	/**
	* 卖家支付宝ID，默认为商户签约账号对应的支付宝用户ID
	*/
	private String seller_id;

	/**
	* 卖家支付宝邮箱，默认为商户签约账号对应的支付宝邮箱
	*/
	private String seller_email;

	/**
	* 调用的接口版本，固定为：1.0
	*/
	private String version;

	/**
	* 未设置，请在数据库中设置
	*/
	private String notify_time;

	public void setId(Integer id)  {
		this.id = id;
	}

	public Integer getId()  {
		return id;
	}

	public void setNotify_id(String notify_id)  {
		this.notify_id = notify_id;
	}

	public String getNotify_id()  {
		return notify_id;
	}

	public void setApp_id(String app_id)  {
		this.app_id = app_id;
	}

	public String getApp_id()  {
		return app_id;
	}

	public void setCharset(String charset)  {
		this.charset = charset;
	}

	public String getCharset()  {
		return charset;
	}

	public void setSign_type(String sign_type)  {
		this.sign_type = sign_type;
	}

	public String getSign_type()  {
		return sign_type;
	}

	public void setSign(String sign)  {
		this.sign = sign;
	}

	public String getSign()  {
		return sign;
	}

	public void setOut_trade_no(String out_trade_no)  {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_trade_no()  {
		return out_trade_no;
	}

	public void setTrade_no(String trade_no)  {
		this.trade_no = trade_no;
	}

	public String getTrade_no()  {
		return trade_no;
	}

	public void setSubject(String subject)  {
		this.subject = subject;
	}

	public String getSubject()  {
		return subject;
	}

	public void setTotal_amount(Double total_amount)  {
		this.total_amount = total_amount;
	}

	public Double getTotal_amount()  {
		return total_amount;
	}

	public void setReceipt_amount(Double receipt_amount)  {
		this.receipt_amount = receipt_amount;
	}

	public Double getReceipt_amount()  {
		return receipt_amount;
	}

	public void setBuyer_pay_amount(Double buyer_pay_amount)  {
		this.buyer_pay_amount = buyer_pay_amount;
	}

	public Double getBuyer_pay_amount()  {
		return buyer_pay_amount;
	}

	public void setBuyer_id(String buyer_id)  {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_id()  {
		return buyer_id;
	}

	public void setBuyer_logon_id(String buyer_logon_id)  {
		this.buyer_logon_id = buyer_logon_id;
	}

	public String getBuyer_logon_id()  {
		return buyer_logon_id;
	}

	public void setBody(String body)  {
		this.body = body;
	}

	public String getBody()  {
		return body;
	}

	public void setAuth_app_id(String auth_app_id)  {
		this.auth_app_id = auth_app_id;
	}

	public String getAuth_app_id()  {
		return auth_app_id;
	}

	public void setNotify_type(String notify_type)  {
		this.notify_type = notify_type;
	}

	public String getNotify_type()  {
		return notify_type;
	}

	public void setInvoice_amount(String invoice_amount)  {
		this.invoice_amount = invoice_amount;
	}

	public String getInvoice_amount()  {
		return invoice_amount;
	}

	public void setTrade_status(String trade_status)  {
		this.trade_status = trade_status;
	}

	public String getTrade_status()  {
		return trade_status;
	}

	public void setGmt_payment(String gmt_payment)  {
		this.gmt_payment = gmt_payment;
	}

	public String getGmt_payment()  {
		return gmt_payment;
	}

	public void setPoint_amount(String point_amount)  {
		this.point_amount = point_amount;
	}

	public String getPoint_amount()  {
		return point_amount;
	}

	public void setGmt_create(String gmt_create)  {
		this.gmt_create = gmt_create;
	}

	public String getGmt_create()  {
		return gmt_create;
	}

	public void setFund_bill_list(String fund_bill_list)  {
		this.fund_bill_list = fund_bill_list;
	}

	public String getFund_bill_list()  {
		return fund_bill_list;
	}

	public void setSeller_id(String seller_id)  {
		this.seller_id = seller_id;
	}

	public String getSeller_id()  {
		return seller_id;
	}

	public void setSeller_email(String seller_email)  {
		this.seller_email = seller_email;
	}

	public String getSeller_email()  {
		return seller_email;
	}

	public void setVersion(String version)  {
		this.version = version;
	}

	public String getVersion()  {
		return version;
	}


	public String getPassback_params() {
		return passback_params;
	}

	public void setPassback_params(String passback_params) {
		this.passback_params = passback_params;
	}

	public void setNotify_time(String notify_time)  {
		this.notify_time = notify_time;
	}

	public String getNotify_time()  {
		return notify_time;
	}

}