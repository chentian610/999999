package com.ninesky.classtao.pay.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

/**
 * Created by TOOTU on 2017/5/9.
 */
public class PayDetailVO extends BaseVO {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 缴费ID
     */
    private Integer pay_id;
    /**
     * 学校ID
     */
    private Integer school_id;
    /**
     * 教师ID
     */
    private Integer user_id;
    /**
     * 学生ID
     */
    private Integer student_id;
    /**
     * 客户端ID
     */
    private String client_id;
    /**
     * 流水号
     */
    private String out_trade_no;

    private String trade_no;
    /**
     * 缴费日期
     */
    private Date pay_date;
    /**
     * 用户类型
     */
    private String group_type;
    /**
     * 转账类型
     */
    private String pay_type;
    /**
     * 支付状态
     */
    private String trade_status;

    private String pay_title;

    private String pay_content;

    private double pay_money;

    public double getPay_money() {
        return pay_money;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public String getPay_content() {
        return pay_content;
    }

    public void setPay_content(String pay_content) {
        this.pay_content = pay_content;
    }

    public String getPay_title() {
        return pay_title;
    }

    public void setPay_title(String pay_title) {
        this.pay_title = pay_title;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date;
    }

    public Date getPay_date() {
        return pay_date;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setPay_id(Integer pay_id) {
        this.pay_id = pay_id;
    }

    public Integer getPay_id() {
        return pay_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
