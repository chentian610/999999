package com.ninesky.classtao.score.vo;


import java.util.Date;

public class ScoreDetailVO {

    private String user_name;

    private String score_date;

    private String score_detail;
    private String team_type;
    private Integer group_id;
    private Integer team_id;
    private String team_name;
    private String attend_item;
    private String score_type;
    private Integer create_by;
    private Date create_date;

    public String getTeam_type() {
        return team_type;
    }

    public void setTeam_type(String team_type) {
        this.team_type = team_type;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getAttend_item() {
        return attend_item;
    }

    public void setAttend_item(String attend_item) {
        this.attend_item = attend_item;
    }

    public String getScore_type() {
        return score_type;
    }

    public void setScore_type(String score_type) {
        this.score_type = score_type;
    }

    public Integer getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getScore_date() {
        return score_date;
    }

    public void setScore_date(String score_date) {
        this.score_date = score_date;
    }

    public String getScore_detail() {
        return score_detail;
    }

    public void setScore_detail(String score_detail) {
        this.score_detail = score_detail;
    }
}
