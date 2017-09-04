package com.ninesky.classtao.leave.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class LeaveVO extends BaseVO{
	private Integer leave_id;//请假申请的ID
	private Integer school_id;//学校ID
	private Integer user_id;//申请人的ID
	private String user_name;//申请人名称
	private String start_date;//开始时间
	private String end_date;//结束时间
	private String leave_type;//请假类型
	private Integer is_change_course;//是否调课
	private String leave_status;//申请状态
	private Integer approver_id;//审批人ID
	private String approver_name;//审批人姓名
	private String approver_content;//审批人的留言
	private Integer master_id;//校长ID
	private String master_name;//校长姓名
	private String master_content;//校长留言
	private String content;//请假人留言
	private String remarks;//调课备注	
	private String approver_date;//审批人操作时间
	private String master_date;//校长操作时间
	/**
	* 头像URL
	*/
	private String head_url;
	/**
	* 电话号码
	*/
	private String phone;
	/**
	* 电话号码
	*/
	private String master_phone;
	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;
	
	/**
	* 版本号
	*/
	private Integer version;

	private String file_list;

    private Integer change_id;

	private Integer leave_hours;//请假小时

	private Integer leave_days;//请假天数

	private String cc_teacher_list;//抄送列表

	private String leave_name;

	private String dict_value;

	private Integer cc_id;

	public void setCc_id(Integer cc_id) {
		this.cc_id = cc_id;
	}

	public Integer getCc_id() {
		return cc_id;
	}

	public void setDict_value(String dict_value) {
		this.dict_value = dict_value;
	}

	public String getDict_value() {
		return dict_value;
	}

	public void setLeave_name(String leave_name) {
		this.leave_name = leave_name;
	}

	public String getLeave_name() {
		return leave_name;
	}

	public void setCc_teacher_list(String cc_teacher_list) {
		this.cc_teacher_list = cc_teacher_list;
	}

	public String getCc_teacher_list() {
		return cc_teacher_list;
	}

	public void setLeave_days(Integer leave_days) {
		this.leave_days = leave_days;
	}

	public Integer getLeave_days() {

		return leave_days;
	}

	public void setLeave_hours(Integer leave_hours) {

		this.leave_hours = leave_hours;
	}

	public Integer getLeave_hours() {

		return leave_hours;
	}

	public Integer getChange_id() {
        return change_id;
    }

    public void setChange_id(Integer change_id) {
        this.change_id = change_id;
    }

    public String getFile_list() {
		return file_list;
	}

	public void setFile_list(String file_list) {
		this.file_list = file_list;
	}

	public String getMaster_phone() {
		return master_phone;
	}
	public void setMaster_phone(String master_phone) {
		this.master_phone = master_phone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getApprover_date() {
		return approver_date;
	}
	public void setApprover_date(String approver_date) {
		this.approver_date = approver_date;
	}
	public String getMaster_date() {
		return master_date;
	}
	public void setMaster_date(String master_date) {
		this.master_date = master_date;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getHead_url() {
		return head_url;
	}
	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	public String getApprover_name() {
		return approver_name;
	}
	public void setApprover_name(String approver_name) {
		this.approver_name = approver_name;
	}
	public String getMaster_name() {
		return master_name;
	}
	public void setMaster_name(String master_name) {
		this.master_name = master_name;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	private String change_teacher_list;
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getChange_teacher_list() {
		return change_teacher_list;
	}
	public void setChange_teacher_list(String change_teacher_list) {
		this.change_teacher_list = change_teacher_list;
	}
	public Integer getLeave_id() {
		return leave_id;
	}
	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
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
	public String getLeave_type() {
		return leave_type;
	}
	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}
	public Integer getIs_change_course() {
		return is_change_course;
	}
	public void setIs_change_course(Integer is_change_course) {
		this.is_change_course = is_change_course;
	}
	public String getLeave_status() {
		return leave_status;
	}
	public void setLeave_status(String leave_status) {
		this.leave_status = leave_status;
	}
	public Integer getApprover_id() {
		return approver_id;
	}
	public void setApprover_id(Integer approver_id) {
		this.approver_id = approver_id;
	}
	public String getApprover_content() {
		return approver_content;
	}
	public void setApprover_content(String approver_content) {
		this.approver_content = approver_content;
	}
	public Integer getMaster_id() {
		return master_id;
	}
	public void setMaster_id(Integer master_id) {
		this.master_id = master_id;
	}
	public String getMaster_content() {
		return master_content;
	}
	public void setMaster_content(String master_content) {
		this.master_content = master_content;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
