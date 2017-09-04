package com.ninesky.classtao.school.vo;

import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.BaseVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;

/**
 * Created by TOOTU on 2017/7/3.
 */
public class SchoolMainVO extends BaseVO{
    private Integer school_id;
    private String school_name;
    private String urban_district;
    private String zip_code;
    private String record_no;
    private String host_url;
    private String path;
    private String manager_url;
    private String copyright;
    private String province;
    private String install_url;
    private String content;
    private String domain;
    private String main_domain;
    private String phone;
    private String address;
    private String organize_pic_url;
    private String main_url;

    public String getMain_url() {
        return main_url;
    }

    public void setMain_url(String main_url) {
        this.main_url = main_url;
    }

    public String getOrganize_pic_url() {
        return organize_pic_url;
    }

    public void setOrganize_pic_url(String organize_pic_url) {
        this.organize_pic_url = organize_pic_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMain_domain() {
        return main_domain;
    }

    public void setMain_domain(String main_domain) {
        this.main_domain = main_domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getManager_url() {
        return manager_url;
    }

    public void setManager_url(String manager_url) {
        this.manager_url = manager_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost_url() {
        return host_url;
    }

    public void setHost_url(String host_url) {
        this.host_url = host_url;
    }

    public String getRecord_no() {
        return record_no;
    }

    public void setRecord_no(String record_no) {
        this.record_no = record_no;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getUrban_district() {
        return urban_district;
    }

    public void setUrban_district(String urban_district) {
        this.urban_district = urban_district;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public SchoolMainVO(){}

    public SchoolMainVO(Integer school_id,String main_url){
        this.school_id = school_id;
        this.main_url = main_url;
    }
}
