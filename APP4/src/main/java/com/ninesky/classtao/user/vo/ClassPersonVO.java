package com.ninesky.classtao.user.vo;

import java.util.Date;


/**
 * 班级用户信息实体类，包含了班级对应的教师、学生、家长
 */
public class ClassPersonVO {

	/**
	 * 未知
	 */
	private Integer classperson_id;
	
	/**
	 * 用户ID
	 */
	private Integer user_id;

	/**
	 * 学校ID
	 */
	private Integer school_id;

	/**
	 * 班级ID
	 */
	private Integer class_id;
	
	/**
	 * 班级代码
	 */
	private String class_code;
	
	/**
	 * 班级名称
	 */
	private String class_name;
	
	/**
	 * 姓名
	 */
	private String user_name;

	/**
	 * 别名，比如XX爸，XX老师
	 */
	private String aliase_name;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 用户类型：老师，家长，学生
	 */
	private String user_type;

	/**
	 * 和班级关系：家长，还是老师，还是学生；已经停用
	 */
	private String relation;

	/**
	 * 学生ID
	 */
	private Integer student_id;

	/**
	 * 学生姓名
	 */
	private String student_name;

	/**
	 * 学生学号
	 */
	private String student_code;
	
	/**
	 * 学生性别
	 */
	private String sex;
	
	public String getStudent_code() {
		return student_code;
	}

	public void setStudent_code(String student_code) {
		this.student_code = student_code;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 头像URL
	 */
	private String head_url;
	

	/**
	 * 人员状态
	 */
	private String status;
	
	/**
	* 信鸽信息推送Token，Ios就是其token值，安卓系统是其clientId值
	*/
	private String app_token;
	

	/**
	 * 融云token，聊天时用到
	 */
	private String im_token;

	/**
	 * 建创者
	 */
	private Integer create_by;

	/**
	 * 创建日期
	 */
	private Date create_date;

	/**
	 * 最后更新人
	 */
	private Integer update_by;

	/**
	 * 最后更新时间
	 */
	private Date update_date;

	/**
	 * 版本号
	 */
	private Integer version;

	/**
	 * 未知
	 */
	private String kst_school_name;

	/**
	 * 未知
	 */
	private String kst_class_name;

	/**
	 * 未知
	 */
	private Integer kst_state;

	/**
	 * 未知
	 */
	private Integer kst_grade_id;

	/**
	 * 未知
	 */
	private Date kst_time;

	/**
	 * 未知
	 */
	private Integer kst_relation_type;

	public void setClassperson_id(Integer classperson_id) {
		this.classperson_id = classperson_id;
	}

	public Integer getClassperson_id() {
		return classperson_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setAliase_name(String aliase_name) {
		this.aliase_name = aliase_name;
	}

	public String getAliase_name() {
		return aliase_name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRelation() {
		return relation;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	public Integer getCreate_by() {
		return create_by;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}

	public void setKst_school_name(String kst_school_name) {
		this.kst_school_name = kst_school_name;
	}

	public String getKst_school_name() {
		return kst_school_name;
	}

	public void setKst_class_name(String kst_class_name) {
		this.kst_class_name = kst_class_name;
	}

	public String getKst_class_name() {
		return kst_class_name;
	}

	public void setKst_state(Integer kst_state) {
		this.kst_state = kst_state;
	}

	public Integer getKst_state() {
		return kst_state;
	}

	public void setKst_grade_id(Integer kst_grade_id) {
		this.kst_grade_id = kst_grade_id;
	}

	public Integer getKst_grade_id() {
		return kst_grade_id;
	}

	public void setKst_time(Date kst_time) {
		this.kst_time = kst_time;
	}

	public Date getKst_time() {
		return kst_time;
	}

	public void setKst_relation_type(Integer kst_relation_type) {
		this.kst_relation_type = kst_relation_type;
	}

	public Integer getKst_relation_type() {
		return kst_relation_type;
	}

	public void setIm_token(String im_token) {
		this.im_token = im_token;
	}

	public String getIm_token() {
		return im_token;
	}

	public String getClass_code() {
		return class_code;
	}

	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApp_token() {
		return app_token;
	}

	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}

}
