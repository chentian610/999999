package com.ninesky.classtao.user.vo;

import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.BaseVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;

public class TeacherVO extends BaseVO{

	private Integer teacher_id;
	
	/**
	* 用户ID
	*/
	private Integer user_id;

	/**
	* 学校ID
	*/
	private Integer school_id;
	
	/**
	 * 普通班级或兴趣班
	 */
	private String team_type;
	
	/**
	* 年级ID
	*/
	private Integer grade_id;

	/**
	 * 年级名称
	 */
	private String grade_name;
	
	/**
	* 班级ID
	*/
	private Integer class_id;

	/**
	* 班级名称，如果管理的是年级，则存放内容是年级名称
	*/
	private String class_name;
	
	/**
	 * 兴趣班ID
	 */
	private Integer contact_id;
	
	/**
	 * 教师性别
	 */
	private Integer sex;
	
	/**
	 * 老师姓名
	 */
	private String teacher_name;
	
	/**
	 * 教师手机号
	 */
	private String phone;
	
	/**
	 * 教师新号码，后台使用
	 */
	private String new_phone;
	
	/**
	 * 头像
	 */
	private String head_url;
	/**
	 * 教师身份是否已确认(0:未确认，1:已确认)
	 */
	private Integer is_confirm;
	/**
	 * 是/否已经毕业
	 */
	private Integer is_graduate;
	/**
	* 课程：015
	*/
	private String course;

	/**
	* 职务：016
	*/
	private String duty;
	
	/**
	 * 是否班主任，1为是班主任
	 */
	private Integer is_charge;
	
	/**
	 * 职务名称串，后台使用
	 */
	private String duty_name;

	/**
	 * 姓名首字母
	 */
	private String first_letter;
	
	/**
	 * 姓名拼音缩写
	 */
	private String all_letter;

	private Integer count;

	private String class_ids;

	private String teacher_ids;

	private Integer is_filtered;

	public String role_tags;

	public String getRole_tags() {
		return role_tags;
	}

	public void setRole_tags(String role_tags) {
		this.role_tags = role_tags;
	}

	public Integer getIs_filtered() {
		return is_filtered;
	}

	public void setIs_filtered(Integer is_filtered) {
		this.is_filtered = is_filtered;
	}

	public String getTeacher_ids() {
		return teacher_ids;
	}

	public void setTeacher_ids(String teacher_ids) {
		this.teacher_ids = teacher_ids;
	}

	public String getClass_ids() {
		return class_ids;
	}

	public void setClass_ids(String class_ids) {
		this.class_ids = class_ids;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getIs_graduate() {
		return is_graduate;
	}

	public void setIs_graduate(Integer is_graduate) {
		this.is_graduate = is_graduate;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public Integer getContact_id() {
		return contact_id;
	}

	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}

	public Integer getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(Integer teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setUser_id(Integer user_id)  {
		this.user_id = user_id;
	}

	public Integer getUser_id()  {
		return user_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setGrade_id(Integer grade_id)  {
		this.grade_id = grade_id;
	}

	public Integer getGrade_id()  {
		return grade_id;
	}

	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}

	public void setClass_id(Integer class_id)  {
		this.class_id = class_id;
	}

	public Integer getClass_id()  {
		return class_id;
	}

	public void setClass_name(String class_name)  {
		this.class_name = class_name;
	}

	public String getClass_name()  {
		return class_name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public Integer getIs_confirm() {
		return is_confirm;
	}

	public void setIs_confirm(Integer is_confirm) {
		this.is_confirm = is_confirm;
	}

	public void setCourse(String course)  {
		this.course = course;
	}

	public String getCourse()  {
		return course;
	}

	public void setDuty(String duty)  {
		this.duty = duty;
	}

	public String getDuty()  {
		return duty;
	}

	public Integer getIs_charge() {
		return is_charge;
	}

	public void setIs_charge(Integer is_charge) {
		this.is_charge = is_charge;
	}

	public String getDuty_name() {
		return duty_name;
	}

	public void setDuty_name(String duty_name) {
		this.duty_name = duty_name;
	}

	public String getNew_phone() {
		return new_phone;
	}

	public void setNew_phone(String new_phone) {
		this.new_phone = new_phone;
	}

	public String getFirst_letter() {
		return first_letter;
	}

	public void setFirst_letter(String first_letter) {
		this.first_letter = first_letter;
	}

	public String getAll_letter() {
		return all_letter;
	}

	public void setAll_letter(String all_letter) {
		this.all_letter = all_letter;
	}

	public TeacherVO(){}

	public TeacherVO(Integer school_id,String duty){
		if (IntegerUtil.isEmpty(school_id) || StringUtil.isEmpty(duty))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.school_id = school_id;
		this.duty = duty;
	}

	public TeacherVO(Integer school_id,String user_type,String phone){
		if (IntegerUtil.isEmpty(school_id) || StringUtil.isEmpty(phone))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.school_id = school_id;
		this.phone = phone;
	}
}