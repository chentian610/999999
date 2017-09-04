package com.ninesky.classtao.leave.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/3/21.
 */
public class LeaveCountVO extends BaseVO{
    private Integer leave_count;//请假总人数
    private Integer total_count;//学校教师总人数
    private Integer user_id;
    private Integer leave_days_sum;
    private Integer leave_hours_sum;
    private Integer leave_counts;
    private String leave_name;
    private String phone;
    private String head_url;

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getHead_url() {

        return head_url;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_counts(Integer leave_counts) {
        this.leave_counts = leave_counts;
    }

    public Integer getLeave_counts() {
        return leave_counts;
    }

    public void setLeave_hours_sum(Integer leave_hours_sum) {
        this.leave_hours_sum = leave_hours_sum;
    }

    public Integer getLeave_hours_sum() {
        return leave_hours_sum;
    }

    public void setLeave_days_sum(Integer leave_days_sum) {
        this.leave_days_sum = leave_days_sum;
    }

    public Integer getLeave_days_sum() {
        return leave_days_sum;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setLeave_count(Integer leave_count) {
        this.leave_count = leave_count;
    }

    public Integer getLeave_count() {
        return leave_count;
    }
}
