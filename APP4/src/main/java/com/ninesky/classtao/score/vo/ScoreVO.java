package com.ninesky.classtao.score.vo;

import java.util.Date;

import com.ninesky.common.vo.BaseVO;

public class ScoreVO extends BaseVO{

	private Integer score_id;
	
	private Integer list_id;//老版本使用，新版本需删除*******************
	/**
	* 学校ID
	*/
	private Integer school_id;

	/**
	* 年级ID或者楼层ID
	*/
	private Integer group_id;
	/**
	* 团队类型，012
	*/
	private String team_type;

	/**
	* 团队ID，对应教室或者寝室ID
	*/
	private Integer team_id;

	/**
	 * 团队编码，对应教室或者寝室code
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
	* 考勤项目014
	*/
	private String attend_item;

	/**
	 * 扣分原因
	 */
	private String score_reason;
	
	/**
	* 统计时间段类型：027
	*/
	private String sum_type;
	
	/**
	* 扣分日期(YYYY-MM-DD)
	*/
	private String score_date;

	/**
	 * 打分人
	 */
	private String user_name;
	
	/**
	* 统计信息（json格式）
	*/
	private String count_info;
	
	/**
	* 团队人员总数
	*/
	private Integer team_count;
	
	/**
	 * 异常人数
	 */
	private Integer count;
	/**
	 * 学生ID，0指集体
	 */
	private Integer student_id;
	/**
	* 学号，0指集体
	*/
	private String student_code;

	/**
	* 题目内容
	*/
	private String content;

	/**
	* 未设置，请在数据库中设置
	*/
	private Integer score;

	/**
	 * 模块code，生成动态使用
	 */
	private String module_code;
	
	/**
	* 创建者
	*/
	private Integer create_by;

	/**
	* 创建日期
	*/
	private Date create_date;

	/**
	* 更新者
	*/
	private Integer update_by;

	/**
	* 更新日期
	*/
	private Date update_date;

	/**
	* 版本号
	*/
	private Integer version;

	/**
	* 未设置，请在数据库中设置
	*/
	private String student_name;
	
	/**
	 * 是否已读,0未读，1已读
	 */
	private Integer is_read;
	
	/**
	 * 床号
	 */
	private String bed_code;

	/**
	 * 被扣分的学生列表
	 */
	private String item_list;
	
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

	public String getTeam_code() {
		return team_code;
	}

	public void setTeam_code(String team_code) {
		this.team_code = team_code;
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

	public String getAttend_item() {
		return attend_item;
	}

	public void setAttend_item(String attend_item) {
		this.attend_item = attend_item;
	}

	public String getScore_reason() {
		return score_reason;
	}

	public void setScore_reason(String score_reason) {
		this.score_reason = score_reason;
	}

	public void setScore_date(String score_date)  {
		this.score_date = score_date;
	}

	public String getScore_date()  {
		return score_date;
	}

	public void setStudent_code(String student_code)  {
		this.student_code = student_code;
	}

	public String getStudent_code()  {
		return student_code;
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

	public void setCreate_by(Integer create_by)  {
		this.create_by = create_by;
	}

	public Integer getCreate_by()  {
		return create_by;
	}

	public void setCreate_date(Date create_date)  {
		this.create_date = create_date;
	}

	public Date getCreate_date()  {
		return create_date;
	}

	public void setUpdate_by(Integer update_by)  {
		this.update_by = update_by;
	}

	public Integer getUpdate_by()  {
		return update_by;
	}

	public void setUpdate_date(Date update_date)  {
		this.update_date = update_date;
	}

	public Date getUpdate_date()  {
		return update_date;
	}

	public void setVersion(Integer version)  {
		this.version = version;
	}

	public Integer getVersion()  {
		return version;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public void setStudent_name(String student_name)  {
		this.student_name = student_name;
	}

	public String getStudent_name()  {
		return student_name;
	}

	public Integer getIs_read() {
		return is_read;
	}

	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getBed_code() {
		return bed_code;
	}

	public void setBed_code(String bed_code) {
		this.bed_code = bed_code;
	}

	public Integer getScore_id() {
		return score_id;
	}

	public void setScore_id(Integer score_id) {
		this.score_id = score_id;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCount_info() {
		return count_info;
	}

	public void setCount_info(String count_info) {
		this.count_info = count_info;
	}

	public Integer getTeam_count() {
		return team_count;
	}

	public void setTeam_count(Integer team_count) {
		this.team_count = team_count;
	}

	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String item_list) {
		this.item_list = item_list;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public Integer getList_id() {
		return list_id;
	}

	public void setList_id(Integer list_id) {
		this.list_id = list_id;
	}

	public String getSum_type() {
		return sum_type;
	}

	public void setSum_type(String sum_type) {
		this.sum_type = sum_type;
	}

}