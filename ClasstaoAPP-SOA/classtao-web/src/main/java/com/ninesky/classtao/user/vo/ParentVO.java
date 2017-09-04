package com.ninesky.classtao.user.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class ParentVO extends BaseVO{

	private Integer parent_id;
	/**
	* 用户ID
	*/
	private Integer user_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

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
	 * 家长手机
	 */
	private String phone;

	/**
	 * 家长姓名
	 */
	private String parent_name;
	/**
	* 课程：015
	*/
	private Integer student_id;

	/**
	* 职务：016
	*/
	private String student_code;

	/**
	* 未设置，请在数据库中设置
	*/
	private String student_name;

	/**
	 * 学生性别
	 */
	private Integer student_sex;
	
	/**
	* 关系：003015
	*/
	private String relation;

	/**
	 * 头像路径
	 */
	private String head_url;
	
	/**
	 * 姓名首字母
	 */
	private String first_letter;
	
	/**
	 * 姓名首字母缩写
	 */
	private String all_letter;
	
	/**
	 * 创建者
	 */
	private Integer create_by;
	
	/**
	 * 创建时间
	 */
	private Date create_date;
	
	/**
	 * 更新者
	 */
	private Integer update_by;
	
	/**
	 * 更新时间
	 */
	private Date update_date;
	
	/**
	* 版本号
	*/
	private Integer version;
	
	/**
	 * 孩子所属的班级和兴趣班
	 */
	private String team_list;

	

	public String getTeam_list() {
		return team_list;
	}

	public void setTeam_list(String team_list) {
		this.team_list = team_list;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
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

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public void setStudent_code(String student_code)  {
		this.student_code = student_code;
	}

	public String getStudent_code()  {
		return student_code;
	}

	public void setStudent_name(String student_name)  {
		this.student_name = student_name;
	}

	public String getStudent_name()  {
		return student_name;
	}

	

	public Integer getStudent_sex() {
		return student_sex;
	}

	public void setStudent_sex(Integer student_sex) {
		this.student_sex = student_sex;
	}

	public void setRelation(String relation)  {
		this.relation = relation;
	}

	public String getRelation()  {
		return relation;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
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

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}