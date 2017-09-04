package com.ninesky.classtao.notice.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by user on 2017/1/4.
 */
public class NoticeReadVO extends BaseVO {
    private Integer id;
    private Integer school_id;
    private Integer notice_id;
    private String user_type;
    private Integer user_id;
    private Integer student_id;
    private Integer is_confirm;//是否已确认通知

    public Integer getId() {
        return id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(Integer notice_id) {
        this.notice_id = notice_id;
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

    public Integer getIs_confirm() {
        return is_confirm;
    }

    public void setIs_confirm(Integer is_confirm) {
        this.is_confirm = is_confirm;
    }
}
