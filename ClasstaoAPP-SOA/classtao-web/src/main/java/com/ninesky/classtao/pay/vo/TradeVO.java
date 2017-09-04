package com.ninesky.classtao.pay.vo;

public class TradeVO {
    /**
     * 学校ID
     */
    private Integer school_id;

    /**
     * 用户类型
     */
    private String user_type;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 学生ID
     */
    private Integer student_id;

    /**
     * 交易标题
     */
    private String trade_title;

    /**
     * 交易内容
     */
    private String trade_content;

    /**
     * 交易金额
     */
    private double pay_money;

    /**
     * 是否设置订单过期时间
     */
    private boolean is_timeout;

    /**
     * 支付宝回调接口
     */
    private String notify_url;

    /**
     * 支付宝网页回调接口
     */
    private String return_url;

    /**
     * 模块编码
     */
    private String module_code;

    /**
     * 主键ID
     */
    private String pk_id;


    /**
     * 业务数据
     */
    private String bus_data;

    public boolean isIs_timeout() {
        return is_timeout;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getTrade_title() {
        return trade_title;
    }

    public void setTrade_title(String trade_title) {
        this.trade_title = trade_title;
    }

    public String getTrade_content() {
        return trade_content;
    }

    public void setTrade_content(String trade_content) {
        this.trade_content = trade_content;
    }

    public double getPay_money() {
        return pay_money;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBus_data() {
        return bus_data;
    }

    public void setBus_data(String bus_data) {
        this.bus_data = bus_data;
    }

    public boolean is_timeout() {
        return is_timeout;
    }

    public void setIs_timeout(boolean is_timeout) {
        this.is_timeout = is_timeout;
    }

    public String getModule_code() {
        return module_code;
    }

    public void setModule_code(String module_code) {
        this.module_code = module_code;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }
}