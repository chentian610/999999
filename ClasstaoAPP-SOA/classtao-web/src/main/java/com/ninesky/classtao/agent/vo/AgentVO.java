package com.ninesky.classtao.agent.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class AgentVO extends BaseVO{


	/**
	* 代理商ID，主键，自增长
	*/
	private Integer agent_id;

	/**
	* 代理商手机号码
	*/
	private String phone;

	/**
	* 代理商名称
	*/
	private String agent_name;

	/**
	* 注册时间
	*/
	private String regist_date;

	/**
	* 截止有效时间
	*/
	private String valid_date;

	private String search;

	/**
	 * 是否启用
	 */
	private String is_enable;
	/**
	 * 学校单价
	 */
	private String unit_price;

	/**
	 * 区域
	 */
	private String region;

	private String pass_word;

	private double balance;

	private Integer user_id;

	private String region_code;

	private String ip_address;

	private String recharge_type;

	private String serial_number;

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getSerial_number() {
		return serial_number;
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

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public String getRegion_code() {
		return region_code;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public double getBalance() {
		return balance;
	}

	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}

	public String getPass_word() {
		return pass_word;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setIs_enable(String is_enable) {
		this.is_enable = is_enable;
	}

	public String getIs_enable() {
		return is_enable;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setAgent_id(Integer agent_id)  {
		this.agent_id = agent_id;
	}

	public Integer getAgent_id()  {
		return agent_id;
	}

	public void setPhone(String phone)  {
		this.phone = phone;
	}

	public String getPhone()  {
		return phone;
	}

	public void setAgent_name(String agent_name)  {
		this.agent_name = agent_name;
	}

	public String getAgent_name()  {
		return agent_name;
	}

	public void setRegist_date(String regist_date)  {
		this.regist_date = regist_date;
	}

	public String getRegist_date()  {
		return regist_date;
	}

	public void setValid_date(String valid_date)  {
		this.valid_date = valid_date;
	}

	public String getValid_date()  {
		return valid_date;
	}

}