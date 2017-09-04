package com.ninesky.classtao.school.vo;

import com.ninesky.common.vo.BaseVO;
/**
 * 寝室类
 * @author Administrator
 *
 */
public class BedroomVO extends BaseVO{
	/**
	 * 学校ID 
	 */
	private Integer school_id;
	/**
	 * 寝室ID
	 */
	private Integer bedroom_id;
	/**
	 * 寝室名称
	 */
	private String bedroom_name;

	private String bedroom_pre;
	
	private Integer start_num;
	
	private Integer end_num;
	/**
	 * 性别
	 */
	private Integer sex;
	
	/**
	 * 寝室所住人数
	 */
	private Integer count;

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
	public String getBedroom_name() {
		return bedroom_name;
	}
	public void setBedroom_name(String bedroomName) {
		bedroom_name = bedroomName;
	}
	public String getBedroom_pre() {
		return bedroom_pre;
	}
	public void setBedroom_pre(String bedroomPre) {
		bedroom_pre = bedroomPre;
	}
	public Integer getStart_num() {
		return start_num;
	}
	public void setStart_num(Integer startNum) {
		start_num = startNum;
	}
	public Integer getEnd_num() {
		return end_num;
	}
	public void setEnd_num(Integer endNum) {
		end_num = endNum;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
