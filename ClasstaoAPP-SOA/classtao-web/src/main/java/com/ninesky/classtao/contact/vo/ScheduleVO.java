package com.ninesky.classtao.contact.vo;

import com.ninesky.common.vo.BaseVO;

public class ScheduleVO extends BaseVO {

    private Integer schedule_id;

    /**
     * 兴趣班ID
     */
    private Integer contact_id;

    /**
     * 上课时间
     */
    private String class_date;

    /**
     * 上课地点
     */
    private String place;

    public Integer getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(Integer schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Integer getContact_id() {
        return contact_id;
    }

    public void setContact_id(Integer contact_id) {
        this.contact_id = contact_id;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
