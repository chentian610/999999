package com.ninesky.classtao.studentLeave.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class StudentLeaveVO extends BaseVO {
    private Integer leave_id;

    /**
     * 学校ID
     */
    private Integer school_id;

    /**
     * 学生ID
     */
    private Integer student_id;

    /**
     * 学生学号
     */
    private String student_code;

    private String team_type;

    private Integer group_id;

    /**
     * 班级ID
     */
    private Integer team_id;

    /**
     * 请假类型
     */
    private String leave_type;

    /**
     * 症状类型
     */
    private String symptom_type;

    /**
     * 请假说明
     */
    private String leave_content;

    /**
     * 请假开始时间
     */
    private String start_date;

    /**
     * 请假结束时间
     */
    private String end_date;

    /**
     * 审批教师user_id
     */
    private Integer approver_id;

    /**审批教务处教师user_id
     *
     */
    private Integer master_id;

    /**
     * 请假状态
     */
    private String leave_status;

    /**
     * 拒绝理由
     */
    private String refuse_content;

    /**
     * 请假附件
     */
    private String file_list;

    /**
     * 学生头像
     */
    private String head_url;

    /**
     * 学生姓名
     */
    private String student_name;

    /**
     * 班级名称
     */
    private String team_name;

    private Date create_date;

    /**
     * 审核的教师名字
     */
    private String teacher_name;

    /**
     * 教师转交时间
     */
    private Date transfer_date;

    /**
     * 谁转交
     */
    private String transfer_teacher_name;

    /**
     * 请假天数
     */
    private Integer days;

    /**
     * 请假次数
     */
    private Integer count;

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getStudent_code() {
        return student_code;
    }

    public void setStudent_code(String student_code) {
        this.student_code = student_code;
    }

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

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getSymptom_type() {
        return symptom_type;
    }

    public void setSymptom_type(String symptom_type) {
        this.symptom_type = symptom_type;
    }

    public String getLeave_content() {
        return leave_content;
    }

    public void setLeave_content(String leave_content) {
        this.leave_content = leave_content;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(Integer approver_id) {
        this.approver_id = approver_id;
    }

    public Integer getMaster_id() {
        return master_id;
    }

    public void setMaster_id(Integer master_id) {
        this.master_id = master_id;
    }

    public String getLeave_status() {
        return leave_status;
    }

    public void setLeave_status(String leave_status) {
        this.leave_status = leave_status;
    }

    public String getRefuse_content() {
        return refuse_content;
    }

    public void setRefuse_content(String refuse_content) {
        this.refuse_content = refuse_content;
    }

    public Integer getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(Integer leave_id) {
        this.leave_id = leave_id;
    }

    public String getFile_list() {
        return file_list;
    }

    public void setFile_list(String file_list) {
        this.file_list = file_list;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    @Override
    public Date getCreate_date() {
        return create_date;
    }

    @Override
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public Date getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(Date transfer_date) {
        this.transfer_date = transfer_date;
    }

    public String getTransfer_teacher_name() {
        return transfer_teacher_name;
    }

    public void setTransfer_teacher_name(String transfer_teacher_name) {
        this.transfer_teacher_name = transfer_teacher_name;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}