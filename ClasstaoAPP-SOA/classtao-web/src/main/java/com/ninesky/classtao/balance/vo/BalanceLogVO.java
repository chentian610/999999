package com.ninesky.classtao.balance.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/4/1.
 */
public class BalanceLogVO extends BaseVO {
    private Integer log_id;//日志ID
    private String phone;//电话号码
    private double pre_balance;//之前余额
    private double current_balance;//当前余额
    private double money;//消费金额
    private String content;//操作描述
    private Integer agent_id;//用户ID
    private String ip_address;
    private String recharge_type;
    private String trade_no;
    private String out_trade_no;
    private String trade_status;

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

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_no() {
        return trade_no;
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

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    public void setCurrent_balance(double current_balance) {

        this.current_balance = current_balance;
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    public void setPre_balance(double pre_balance) {
        this.pre_balance = pre_balance;
    }

    public double getPre_balance() {
        return pre_balance;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public String getPhone() {

        return phone;
    }

    public void setLog_id(Integer log_id) {

        this.log_id = log_id;
    }

    public Integer getLog_id() {

        return log_id;
    }
}
