package com.ninesky.classtao.balance.vo;

import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class BalanceVO extends BaseVO{


	/**
	* 主键，自增长
	*/
	private Integer balance_id;

	/**
	* 代理商手机号码
	*/
	private String phone;

	/**
	 * 余额
	 */
	private double balance;

	private double	consumption_money;

	private Integer user_id;

	private Integer agent_id;

	private Integer consumption_type;

	private String ip_address;

	private String recharge_type;

	private String user_type;
	private String trade_no;
	private String out_trade_no;
	private String trade_status;

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public void setRecharge_type(String recharge_type) {
		this.recharge_type = recharge_type;
	}

	public String getRecharge_type() {
		return recharge_type;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setConsumption_type(Integer consumption_type) {
		this.consumption_type = consumption_type;
	}

	public Integer getConsumption_type() {
		return consumption_type;
	}

	public void setAgent_id(Integer agent_id) {
		this.agent_id = agent_id;
	}

	public Integer getAgent_id() {
		return agent_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setConsumption_money(double consumption_money) {
		this.consumption_money = consumption_money;
	}

	public double getConsumption_money() {
		return consumption_money;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance_id(Integer balance_id) {
		this.balance_id = balance_id;
	}

	public Integer getBalance_id() {
		return balance_id;
	}
}