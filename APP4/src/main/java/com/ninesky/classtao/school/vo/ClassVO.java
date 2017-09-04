package com.ninesky.classtao.school.vo;

import java.util.Date;
import com.ninesky.common.vo.BaseVO;

public class ClassVO extends BaseVO{
	private Integer enrollment_year;
	
	private Integer class_id;
	/**
	 * 班级名称
	 */
	private String class_name;
	
	private Integer grade_num;
	
	private Integer grade_id;
	
	/**
	 * 年级名称
	 */
	private String grade_name;
	
	private Integer class_num;
	
	private boolean checked;
	
	private Integer school_id;
	
	private String school_name;
	
	/**
	 * 班级代码
	 */
	private String class_code;
	
	/**
	 * 班级类型
	 */
	private String class_type;
	
	/**
	 * 班级类型
	 */
	private String img_url;
	
	/**
	 * 角色描述
	 */
	private String description;
	
	private Integer count;
	
	private String sort;
	
	/**
	 * 是/否已经毕业
	 */
	private Integer is_graduate;
	
	
	public Integer getIs_graduate() {
		return is_graduate;
	}

	public void setIs_graduate(Integer is_graduate) {
		this.is_graduate = is_graduate;
	}

	public Integer getEnrollment_year() {
		return enrollment_year;
	}

	public void setEnrollment_year(Integer enrollment_year) {
		this.enrollment_year = enrollment_year;
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

	private String status;
	
	private Integer create_by;
	
	private Date create_date;
	
	/**
	*  更新者
	*/
	private Integer update_by;

	/**
	*  更新日期
	*/
	private Date update_date;

	/**
	*  版本号
	*/
	private Integer version;
	
	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return this.class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getClass_code() {
		return class_code;
	}

	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getGrade_num() {
		return grade_num;
	}

	public void setGrade_num(Integer grade_num) {
		this.grade_num = grade_num;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}

	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}

	public Integer getClass_num() {
		return class_num;
	}

	public void setClass_num(Integer class_num) {
		this.class_num = class_num;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getClass_type() {
		return class_type;
	}

	public void setClass_type(String class_type) {
		this.class_type = class_type;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
}
