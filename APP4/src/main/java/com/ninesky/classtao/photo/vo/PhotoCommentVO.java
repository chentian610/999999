package com.ninesky.classtao.photo.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/8/16.
 */
public class PhotoCommentVO extends BaseVO{
    private Integer id;//主键ID
    private Integer school_id;//学校ID
    private Integer user_id;//用户ID
    private String user_type;//用户类型
    private Integer point_praise;//点赞
    private String comment;//评论
    private Integer student_id;//学生ID
    private String relation;//家长类型
    private String reviewer_name;//评论人名称
    private String add_date;//起源时间

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public void setReviewer_name(String reviewer_name) {
        this.reviewer_name = reviewer_name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPoint_praise() {
        return point_praise;
    }

    public void setPoint_praise(Integer point_praise) {
        this.point_praise = point_praise;
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

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

