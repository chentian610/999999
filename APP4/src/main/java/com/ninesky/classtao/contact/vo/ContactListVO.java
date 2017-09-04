package com.ninesky.classtao.contact.vo;


import com.ninesky.common.vo.BaseVO;
public class ContactListVO extends BaseVO implements Comparable<ContactListVO>{


	/**
	* 考勤ID，主键
	*/
	private Integer list_id;

	/**
	* 外键
	*/
	private Integer contact_id;

	/**
	* 学校ID
	*/
	private Integer school_id;
	
	/**
	 * 用户ID
	 */
	private Integer user_id;

	/**
	* 用户类型：003
	*/
	private String user_type;
	
	/**
	 * 学生ID
	 */
	private Integer student_id;

	/**
	* 用户姓名
	*/
	private String user_name;
	
	/**
	* 用户 姓氏 拼音首字母
	*/
	private String first_letter;
	
	/**
	* 用户 姓名 拼音首字母
	*/
	private String all_letter;
	
	/**
	 * 头像
	 */
	private String head_url;

	/**
	* 电话
	*/
	private String phone;
	/**
	 * 班级id
	 */
	private Integer class_id;
	/**
	 * 联系人类型（老师，家长）
	 */
	private String contact_type;
	/**
	 * 联系人姓名（模糊查询条件）
	 */
	private String seek_name;
	
	/**
	 * 多个班级时以‘，’号隔开
	 */
	private String class_ids;

	public String class_name;

	public Integer grade_id;

	public String role_tags;

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getRole_tags() {
		return role_tags;
	}

	public void setRole_tags(String role_tags) {
		this.role_tags = role_tags;
	}

	public String getClass_ids() {
		return class_ids;
	}

	public void setClass_ids(String class_ids) {
		this.class_ids = class_ids;
	}

	public Integer getList_id() {
		return list_id;
	}

	public void setList_id(Integer list_id) {
		this.list_id = list_id;
	}

	public Integer getContact_id() {
		return contact_id;
	}

	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
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

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public String getContact_type() {
		return contact_type;
	}

	public void setContact_type(String contact_type) {
		this.contact_type = contact_type;
	}

	public String getSeek_name() {
		return seek_name;
	}

	public void setSeek_name(String seek_name) {
		this.seek_name = seek_name;
	}

//	//按姓名拼音排序
//	public int compareTo(ContactListVO o) {
//		return Collator.getInstance(java.util.Locale.CHINESE).compare(
//				this.getUser_name(), o.getUser_name());
//	}
	
	//按姓名拼音首字母排序
	public int compareTo(ContactListVO o) {
		return this.all_letter.compareTo(o.all_letter);
	}
}