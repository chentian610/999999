package com.ninesky.classtao.school.vo;


import com.ninesky.common.vo.BaseVO;
/**
 * 床位人员关系类
 * @author Administrator
 *
 */
public class BedVO extends BaseVO{
	/**
	 * kt_bas_position_bed表主键id
	 */
	private Integer id;
	/**
	 * 学校id
	 */
	private Integer school_id;
	/**
	 * 寝室id
	 */
	private Integer bedroom_id;
	/**
	 * 寝室名称
	 */
	private String bedroom_name;
	/**
	 * 寝室床位号
	 */
	private String bed_code;
	/**
	 * 学号
	 */
	private Integer student_id;
	/**
	 * 学生编码
	 */
	private String student_code;
	/**
	 * 学生姓名
	 */
	private String student_name;
	
	/**
	 * 所属班级名称
	 */
	private String class_name;
	
	/**
	 * 学生性别
	 */
	private Integer sex;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer schoolId) {
		school_id = schoolId;
	}
	public Integer getBedroom_id() {
		return bedroom_id;
	}
	public void setBedroom_id(Integer bedroomId) {
		bedroom_id = bedroomId;
	}
	public String getBed_code() {
		return bed_code;
	}
	public void setBed_code(String bedCode) {
		bed_code = bedCode;
	}
	public String getStudent_code() {
		return student_code;
	}
	public void setStudent_code(String studentCode) {
		student_code = studentCode;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String studentName) {
		student_name = studentName;
	}
	public String getBedroom_name() {
		return bedroom_name;
	}
	public void setBedroom_name(String bedroomName) {
		bedroom_name = bedroomName;
	}
	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer studentId) {
		student_id = studentId;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
}
