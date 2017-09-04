package com.ninesky.classtao.enroll.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class StudentRecruitVO extends BaseVO{
    private Integer recruit_id;

    /**
     * 学校ID
     */
    private Integer school_id;

    /**
     * 招生简章标题
     */
    private String title;

    /**
     * 招生简章内容
     */
    private String content;

    /**
     * 报名开始时间
     */
    private String apply_start_date;

    /**
     * 报名结束时间
     */
    private String apply_end_date;

    /**
     * 招生简章发布时间
     */
    private Date create_date;

    /**
     * 报名人数
     */
    private Integer count;
    private String table_rows;

    /**
     *录取率
     */
    private String acceptance_rate;

    /**
     * 录取人数
     */
    private Integer enroll_count;

    /**
     * 男女比例
     */
    private String ratio;

    /**
     * 招生状态，是否已完成录取
     */
    private String status;

    /**
     * 录取完成时间
     */
    private String completion_date;

    public Integer getRecruit_id() {
        return recruit_id;
    }

    public void setRecruit_id(Integer recruit_id) {
        this.recruit_id = recruit_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApply_start_date() {
        return apply_start_date;
    }

    public void setApply_start_date(String apply_start_date) {
        this.apply_start_date = apply_start_date;
    }

    public String getApply_end_date() {
        return apply_end_date;
    }

    public void setApply_end_date(String apply_end_date) {
        this.apply_end_date = apply_end_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTable_rows() {
        return table_rows;
    }

    public void setTable_rows(String table_rows) {
        this.table_rows = table_rows;
    }

    public String getAcceptance_rate() {
        return acceptance_rate;
    }

    public void setAcceptance_rate(String acceptance_rate) {
        this.acceptance_rate = acceptance_rate;
    }

    public Integer getEnroll_count() {
        return enroll_count;
    }

    public void setEnroll_count(Integer enroll_count) {
        this.enroll_count = enroll_count;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }
}
