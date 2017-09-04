package com.ninesky.classtao.pay.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

/**
 * Created by TOOTU on 2017/5/9.
 */
public class PayVO extends BaseVO{
    /**
     * 缴费ID
     */
    private Integer pay_id;
    /**
     * 缴费标题
     */
    private String pay_title;
    /**
     * 缴费内容描述
     */
    private String pay_content;
    /**
     * 学校ID
     */
    private Integer school_id;
    /**
     * 过期日期
     */
    private Date end_date;
    /**
     * 缴费类型
     */
    private String pay_type;
    /**
     * 客户端ID
     */
    private String client_id;
    /**
     * 缴费金额
     */
    private double pay_money;
    /**
     * 缴费类别
     */
    private String pay_category;

    private String pay_group_list;

    private String pay_count;

    /**
     * 教师ID
     */
    private Integer user_id;
    /**
     * 学生ID
     */
    private Integer student_id;

    private Integer team_id;

    private String team_type;

    private Integer pay_status;

    private Integer is_expired;

    private String sender_name;

    private String pay_type_name;

    private String pay_category_name;

    private String user_type;

    private String trade_status;

    private Integer is_graduate;

    private Integer group_id;

    private String pay_team_names;

    private Date create_date;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getPay_team_names() {
        return pay_team_names;
    }

    public void setPay_team_names(String pay_team_names) {
        this.pay_team_names = pay_team_names;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getIs_graduate() {
        return is_graduate;
    }

    public void setIs_graduate(Integer is_graduate) {
        this.is_graduate = is_graduate;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPay_title() {
        return pay_title;
    }

    public void setPay_title(String pay_title) {
        this.pay_title = pay_title;
    }

    public void setPay_category_name(String pay_category_name) {
        this.pay_category_name = pay_category_name;
    }

    public String getPay_category_name() {
        return pay_category_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setIs_expired(Integer is_expired) {
        this.is_expired = is_expired;
    }

    public Integer getIs_expired() {
        return is_expired;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public void setTeam_type(String team_type) {
        this.team_type = team_type;
    }

    public String getTeam_type() {
        return team_type;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Integer getTeam_id() {
        return team_id;
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

    public void setPay_count(String pay_count) {
        this.pay_count = pay_count;
    }

    public String getPay_count() {
        return pay_count;
    }

    public void setPay_category(String pay_category) {
        this.pay_category = pay_category;
    }

    public String getPay_category() {
        return pay_category;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public double getPay_money() {
        return pay_money;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_group_list(String pay_group_list) {
        this.pay_group_list = pay_group_list;
    }

    public String getPay_group_list() {
        return pay_group_list;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setPay_content(String pay_content) {
        this.pay_content = pay_content;
    }

    public String getPay_content() {
        return pay_content;
    }

    public void setPay_id(Integer pay_id) {
        this.pay_id = pay_id;
    }

    public Integer getPay_id() {
        return pay_id;
    }
}
