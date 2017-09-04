package com.ninesky.classtao.leave.vo;

import com.ninesky.common.vo.BaseVO;

public class LeaveLogVO extends BaseVO{
	private Integer id;//id
	private Integer leave_id;//请假申请的ID
	private String pre_status;//请假申请之前的状态
	private String current_status;//请假申请当前的状态
	private String content;//申请描述(通过/同意/驳回/新建)
	private Integer oper_id;//审批人ID
	private String oper_name;//审批人的名称
	
	public Integer getOper_id() {
		return oper_id;
	}
	public void setOper_id(Integer oper_id) {
		this.oper_id = oper_id;
	}
	public String getOper_name() {
		return oper_name;
	}
	public void setOper_name(String oper_name) {
		this.oper_name = oper_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLeave_id() {
		return leave_id;
	}
	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}
	public String getPre_status() {
		return pre_status;
	}
	public void setPre_status(String pre_status) {
		this.pre_status = pre_status;
	}
	public String getCurrent_status() {
		return current_status;
	}
	public void setCurrent_status(String current_status) {
		this.current_status = current_status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
