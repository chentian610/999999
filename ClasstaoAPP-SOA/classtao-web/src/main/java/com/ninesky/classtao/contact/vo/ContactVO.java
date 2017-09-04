package com.ninesky.classtao.contact.vo;


import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class ContactVO extends BaseVO{


	/**
	* 考勤ID，主键
	*/
	private Integer contact_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 用户id
	*/
	private Integer user_id;
	
	/**
	* 用户类型
	*/
	private String user_type;

	/**
	* 通讯录组名
	*/
	private String contact_name;
	
	/**
	 * 通讯录组内容
	 */
	private String item_list;
	
	/**
	 * 兴趣班人数
	 */
	private Integer count;
	
	/**
	 * 兴趣班课程
	 */
	private String course;
	
	/**
	 * 兴趣班课程名称
	 */
	private String course_name;

	/**
	 * 1:启用 0：禁用
	 */
	private Integer is_active;

	/**
	 * 年级ID，0为全校
	 */
	private Integer grade_id;

	/**
	 * 任课教师手机号
	 */
	private String phone;

	/**
	 * 开课时间
	 */
	private Date start_date;

	/**
	 * 课程结束时间
	 */
	private Date end_date;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 班级最大人数
	 */
	private Integer team_count;

	/**
	 * 可报名人数
	 */
	private Integer apply_count;

	/**
	 * 报名开始时间
	 */
	private Date apply_start_date;

	/**
	 * 报名截止时间
	 */
	private Date apply_end_date;

	/**
	 * 课程表图片url
	 */
	private String schedule_url;

	/**
	 * 1:抢报模式  0:非抢报模式
	 */
	private Integer is_grab;

	/**
	 *任课教师姓名
	 */
	private String teacher_name;

	/**
	 * 1:我创建的
	 */
	private Integer is_my;

	/**
	 * 自定义课程表的jsondata
	 */
	private String schedule;

	private Integer student_id;

	/**
	 * 是否成功报名
	 */
	private String is_success;

	/**
	 * 班级已有人数
	 */
	private Integer exist_count;

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public void setContact_id(Integer contact_id)  {
		this.contact_id = contact_id;
	}

	public Integer getContact_id()  {
		return contact_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public void setContact_name(String contact_name)  {
		this.contact_name = contact_name;
	}

	public String getContact_name()  {
		return contact_name;
	}

	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String item_list) {
		this.item_list = item_list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTeam_count() {
		return team_count;
	}

	public void setTeam_count(Integer team_count) {
		this.team_count = team_count;
	}

	public Integer getApply_count() {
		return apply_count;
	}

	public void setApply_count(Integer apply_count) {
		this.apply_count = apply_count;
	}

	public Date getApply_start_date() {
		return apply_start_date;
	}

	public void setApply_start_date(Date apply_start_date) {
		this.apply_start_date = apply_start_date;
	}

	public Date getApply_end_date() {
		return apply_end_date;
	}

	public void setApply_end_date(Date apply_end_date) {
		this.apply_end_date = apply_end_date;
	}

	public String getSchedule_url() {
		return schedule_url;
	}

	public void setSchedule_url(String schedule_url) {
		this.schedule_url = schedule_url;
	}

	public Integer getIs_grab() {
		return is_grab;
	}

	public void setIs_grab(Integer is_grab) {
		this.is_grab = is_grab;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public Integer getIs_my() {
		return is_my;
	}

	public void setIs_my(Integer is_my) {
		this.is_my = is_my;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public Integer getExist_count() {
		return exist_count;
	}

	public void setExist_count(Integer exist_count) {
		this.exist_count = exist_count;
	}
}