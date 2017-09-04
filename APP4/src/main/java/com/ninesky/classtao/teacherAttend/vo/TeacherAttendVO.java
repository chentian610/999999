package com.ninesky.classtao.teacherAttend.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class TeacherAttendVO extends BaseVO{

    private Integer attend_id;

    private Integer school_id;
    /**
     * 打卡人用户ID
     */
    private Integer user_id;

    private String teacher_name;

    /**
     * 打卡类型：上班，下班
     */
    private String attend_type;

    /**
     * 打开时间
     */
    private Date attend_time;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 地址
     */
    private String address;

    /**
     * 考勤状态
     */
    private String attend_status;

    /**
     * 查询时间：yyyy-MM
     */
    private String search_time;

    /**
     * 上班时间
     */
    private String work_time;

    /**
     * 下班时间
     */
    private String close_time;

    private Integer count;

    /**
     * 上班地址
     */
    private String work_address;

    /**
     * 上班地址经度
     */
    private String work_longitude;

    /**
     * 上班地址纬度
     */
    private String work_latitude;

    /**
     * 考勤范围
     */
    private Integer attend_range;

    public void setAttend_id(Integer attend_id)  {
        this.attend_id = attend_id;
    }

    public Integer getAttend_id()  {
        return attend_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public void setUser_id(Integer user_id)  {
        this.user_id = user_id;
    }

    public Integer getUser_id()  {
        return user_id;
    }

    public void setAttend_type(String attend_type)  {
        this.attend_type = attend_type;
    }

    public String getAttend_type()  {
        return attend_type;
    }

    public void setAttend_time(Date attend_time)  {
        this.attend_time = attend_time;
    }

    public Date getAttend_time()  {
        return attend_time;
    }

    public void setLongitude(String longitude)  {
        this.longitude = longitude;
    }

    public String getLongitude()  {
        return longitude;
    }

    public void setLatitude(String latitude)  {
        this.latitude = latitude;
    }

    public String getLatitude()  {
        return latitude;
    }

    public void setAddress(String address)  {
        this.address = address;
    }

    public String getAddress()  {
        return address;
    }

    public void setAttend_status(String attend_status)  {
        this.attend_status = attend_status;
    }

    public String getAttend_status()  {
        return attend_status;
    }

    public String getSearch_time() {
        return search_time;
    }

    public void setSearch_time(String search_time) {
        this.search_time = search_time;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getWork_address() {
        return work_address;
    }

    public void setWork_address(String work_address) {
        this.work_address = work_address;
    }

    public String getWork_longitude() {
        return work_longitude;
    }

    public void setWork_longitude(String work_longitude) {
        this.work_longitude = work_longitude;
    }

    public String getWork_latitude() {
        return work_latitude;
    }

    public void setWork_latitude(String work_latitude) {
        this.work_latitude = work_latitude;
    }

    public Integer getAttend_range() {
        return attend_range;
    }

    public void setAttend_range(Integer attend_range) {
        this.attend_range = attend_range;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}