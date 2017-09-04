package com.ninesky.classtao.score.vo;


import com.ninesky.common.vo.BaseVO;

import java.util.Date;

public class ScoreListVO extends BaseVO{


	/**
	* 未设置，请在数据库中设置
	*/
	private Integer list_id;

	/**
	* 外键
	*/
	private Integer score_id;

	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 团队类型，012
	*/
	private String team_type;

	/**
	* 团队ID（对应教室class_id或者寝室bedroom_id）
	*/
	private Integer team_id;

	/**
	* 团队编码
	*/
	private String team_code;

	/**
	* 团队名
	*/
	private String team_name;

	/**
	* 打分类型
	*/
	private String score_type;

	/**
	* 扣分类型013
	*/
	private String score_code;

	/**
	* 考勤项目:014
	*/
	private String attend_item;
	
	/**
	* 考勤项目:014，修改前的项目
	*/
	private String score_code_old;
	
	/**
	* 扣分日期(YYYY-MM-DD)
	*/
	private String score_date;
	
	private String user_name;

	/**
	* 学生ID，0指集体
	*/
	private Integer student_id;

	/**
	* 学号，0指集体
	*/
	private String student_code;

	/**
	* 未设置，请在数据库中设置
	*/
	private String student_name;
	
	/**
	 * 学生头像
	 */
	private String head_url;

	/**
	* 题目内容
	*/
	private String content;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer score;
	
	/**
	* 修改的旧值
	*/
	private Integer score_old;
	
	/**
	 * 模块code，生成动态使用
	 */
	private String module_code;
	
	private String bed_code;

	/**
	* 是否已读,0未读，1已读
	*/
	private Integer is_read;
	
	/**
	 * 扣分这user_id
	 */
	private Integer sender_id;
	
	/**
	 * 统计出的个数
	 */
	private Integer count;

	/**
	 * 姓名首字母全拼
	 */
	private String first_letter;

	/**
	 * 姓名全拼
	 */
	private String all_letter;

	private Date create_date;
	private Integer group_id;
	private String start_date;
	private String end_date;
	/**
	 * 是否请假,true为请假状态
	 */
	private boolean is_leave;

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	@Override
	public Date getCreate_date() {
		return create_date;
	}

	@Override
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getAll_letter() {
		return all_letter;
	}

	public void setAll_letter(String all_letter) {
		this.all_letter = all_letter;
	}

	public String getFirst_letter() {
		return first_letter;
	}

	public void setFirst_letter(String first_letter) {
		this.first_letter = first_letter;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSender_id() {
		return sender_id;
	}

	public void setSender_id(Integer sender_id) {
		this.sender_id = sender_id;
	}

	public void setList_id(Integer list_id)  {
		this.list_id = list_id;
	}

	public Integer getList_id()  {
		return list_id;
	}

	public void setScore_id(Integer score_id)  {
		this.score_id = score_id;
	}

	public Integer getScore_id()  {
		return score_id;
	}

	public void setSchool_id(Integer school_id)  {
		this.school_id = school_id;
	}

	public Integer getSchool_id()  {
		return school_id;
	}

	public void setTeam_type(String team_type)  {
		this.team_type = team_type;
	}

	public String getTeam_type()  {
		return team_type;
	}

	public void setTeam_id(Integer team_id)  {
		this.team_id = team_id;
	}

	public Integer getTeam_id()  {
		return team_id;
	}

	public void setTeam_code(String team_code)  {
		this.team_code = team_code;
	}

	public String getTeam_code()  {
		return team_code;
	}

	public void setTeam_name(String team_name)  {
		this.team_name = team_name;
	}

	public String getTeam_name()  {
		return team_name;
	}

	public void setScore_type(String score_type)  {
		this.score_type = score_type;
	}

	public String getScore_type()  {
		return score_type;
	}

	public void setScore_code(String score_code)  {
		this.score_code = score_code;
	}

	public String getScore_code()  {
		return score_code;
	}

	public String getAttend_item() {
		return attend_item;
	}

	public void setAttend_item(String attend_item) {
		this.attend_item = attend_item;
	}

	public void setScore_date(String score_date)  {
		this.score_date = score_date;
	}

	public String getScore_date()  {
		return score_date;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setStudent_id(Integer student_id)  {
		this.student_id = student_id;
	}

	public Integer getStudent_id()  {
		return student_id;
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

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public void setContent(String content)  {
		this.content = content;
	}

	public String getContent()  {
		return content;
	}

	public void setScore(Integer score)  {
		this.score = score;
	}

	public Integer getScore()  {
		return score;
	}

	public void setIs_read(Integer is_read)  {
		this.is_read = is_read;
	}

	public Integer getIs_read()  {
		return is_read;
	}

	public String getBed_code() {
		return bed_code;
	}

	public void setBed_code(String bed_code) {
		this.bed_code = bed_code;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getScore_code_old() {
		return score_code_old;
	}

	public void setScore_code_old(String score_code_old) {
		this.score_code_old = score_code_old;
	}

	public Integer getScore_old() {
		return score_old;
	}

	public void setScore_old(Integer score_old) {
		this.score_old = score_old;
	}

	public boolean is_leave() {
		return is_leave;
	}

	public void setIs_leave(boolean is_leave) {
		this.is_leave = is_leave;
	}
}