package com.ninesky.classtao.pay.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/5/9.
 */
public class PayGroupVO extends BaseVO {
    /**
     * 缴费团体或者个人ID
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
     * 团体类型
     */
    private String team_type;
    /**
     * 年级ID
     */
    private Integer group_id;
    /**
     * 团队ID
     */
    private Integer team_id;
    /**
     * 教师ID
     */
    private Integer user_id;
    /**
     * 学生ID
     */
    private Integer student_id;

    private String pay_group_count;

    private String team_name;

    private String trade_status;

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setPay_group_count(String pay_group_count) {
        this.pay_group_count = pay_group_count;
    }

    public String getPay_group_count() {
        return pay_group_count;
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

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setTeam_type(String team_type) {
        this.team_type = team_type;
    }

    public String getTeam_type() {
        return team_type;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getSchool_id() {
        return school_id;
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
}
