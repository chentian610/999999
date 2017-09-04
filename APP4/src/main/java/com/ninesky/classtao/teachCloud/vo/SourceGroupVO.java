package com.ninesky.classtao.teachCloud.vo;


import java.util.Date;
import com.ninesky.common.vo.BaseVO;

public class SourceGroupVO extends BaseVO {

	/**
	 * 接收表主键
	 */
	private Integer id;
	
	/**
	 * 资源ID
	 */
	private String source_id;
	
	/**
	 * 通知ID
	 */
	private Integer teach_source_id;
	
	/**
	* 学校ID
	*/
	private Integer school_id;
	
	/**
	* 发送者用户ID
	*/
	private Integer sender_id;

	/**
	* 发送者名字
	*/
	private String sender_name;

	/**
	* 接收者学号
	*/
	private Integer student_id;

	/**
	 * 接收者ID
	 */
	private Integer user_id;
	
	/**
	* 接收者姓名
	*/
	private String student_name;
	
	/**
	* 资源名称
	*/
	private String source_name;
	
	/**
	 * 教学云提供的json数据
	 */
	private String source_data;
	
	/**
	 * 资源类型
	 */
	private String source_type;
	
	/**
	 * 留言内容
	 */
	private String remark;
	
	/**
	 * 年级ID
	 */
	private Integer group_id;
	
	/**
	 * team_id
	 * @return
	 */
	private Integer team_id;
	
	/**
	 * 用户类型：003
	 * @return
	 */
	private String user_type;
	
	/**
	 * 发送时间
	 * @return
	 */
	private Date send_time;
	
	/**
	 * 团队类型：普通班
	 * @return
	 */
	private String team_type;
	
	/**
	 * 团队类型：兴趣班
	 */
	private String team_type_interest;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTeach_source_id() {
		return teach_source_id;
	}

	public void setTeach_source_id(Integer teach_source_id) {
		this.teach_source_id = teach_source_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getSource_data() {
		return source_data;
	}

	public void setSource_data(String source_data) {
		this.source_data = source_data;
	}

	public String getTeam_type() {
		return team_type;
	}

	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}

	public String getTeam_type_interest() {
		return team_type_interest;
	}

	public void setTeam_type_interest(String team_type_interest) {
		this.team_type_interest = team_type_interest;
	}


}
