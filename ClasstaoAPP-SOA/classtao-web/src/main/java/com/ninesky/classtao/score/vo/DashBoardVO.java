package com.ninesky.classtao.score.vo;


public class DashBoardVO{
	/**
	 * 例：班级名称，学生姓名
	 */
	private String name;
	
	/**
	 * 例：扣分，考勤统计信息
	 */
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
