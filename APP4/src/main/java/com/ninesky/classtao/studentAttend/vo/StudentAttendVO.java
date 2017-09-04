package com.ninesky.classtao.studentAttend.vo;

import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class StudentAttendVO extends BaseVO {

	private Integer attend_id;

	private Integer school_id;
	/**
	* 年级ID
	*/
	private Integer group_id;

	/**
	* 班级ID
	*/
	private Integer team_id;

	/**
	* 学生ID
	*/
	private Integer student_id;

	private String student_name;

	/**
	* 打卡时间
	*/
	private Date attend_time;

	/**
	* 文件URL
	*/
	private String file_url;

	/**
	* 缩略图URL
	*/
	private String file_resize_url;

	private String head_url;

	/**
	 * 查询时间
	 */
	private String search_time;

	/**
	 * 上学时间
	 */
	private Date work_time;

	/**
	 * 放学时间
	 */
	private Date close_time;

	/**
	 * 正常次数
	 */
	private Integer normal_count;

	/**
	 * 异常次数
	 */
	private Integer abnormal_count;

	/**
	 *异常记录
	 */
	private String abnormal_list;

	/**
	 * 通勤卡号
	 */
	private String card_number;

	public void setAttend_id(Integer attend_id)  {
		this.attend_id = attend_id;
	}

	public Integer getAttend_id()  {
		return attend_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setGroup_id(Integer group_id)  {
		this.group_id = group_id;
	}

	public Integer getGroup_id()  {
		return group_id;
	}

	public void setTeam_id(Integer team_id)  {
		this.team_id = team_id;
	}

	public Integer getTeam_id()  {
		return team_id;
	}

	public void setStudent_id(Integer student_id)  {
		this.student_id = student_id;
	}

	public Integer getStudent_id()  {
		return student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public void setAttend_time(Date attend_time)  {
		this.attend_time = attend_time;
	}

	public Date getAttend_time()  {
		return attend_time;
	}

	public void setFile_url(String file_url)  {
		this.file_url = file_url;
	}

	public String getFile_url()  {
		return file_url;
	}

	public void setFile_resize_url(String file_resize_url)  {
		this.file_resize_url = file_resize_url;
	}

	public String getFile_resize_url()  {
		return file_resize_url;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getSearch_time() {
		return search_time;
	}

	public void setSearch_time(String search_time) {
		this.search_time = search_time;
	}

	public Date getWork_time() {
		return work_time;
	}

	public void setWork_time(Date work_time) {
		this.work_time = work_time;
	}

	public Date getClose_time() {
		return close_time;
	}

	public void setClose_time(Date close_time) {
		this.close_time = close_time;
	}

	public Integer getNormal_count() {
		return normal_count;
	}

	public void setNormal_count(Integer normal_count) {
		this.normal_count = normal_count;
	}

	public Integer getAbnormal_count() {
		return abnormal_count;
	}

	public void setAbnormal_count(Integer abnormal_count) {
		this.abnormal_count = abnormal_count;
	}

	public String getAbnormal_list() {
		return abnormal_list;
	}

	public void setAbnormal_list(String abnormal_list) {
		this.abnormal_list = abnormal_list;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
}