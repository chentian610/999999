package com.ninesky.classtao.microPortal.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/7/27.
 */
public class MicroPortalInfoVO extends BaseVO {
    private Integer info_id;//主键ID
    private Integer school_id;//学校ID
    private String campus_name;//校区名称
    private String address;//校区地址
    private String phone;//校区联系电话
    private String email;//校区邮箱地址

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCampus_name() {
        return campus_name;
    }

    public void setCampus_name(String campus_name) {
        this.campus_name = campus_name;
    }

    public Integer getInfo_id() {
        return info_id;
    }

    public void setInfo_id(Integer info_id) {
        this.info_id = info_id;
    }
}
