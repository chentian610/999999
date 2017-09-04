package com.ninesky.classtao.homework.vo;

public class CountVO {
	/**
	 * 对应的数量
	 */
	private Integer count;
	/**
	 * 完成人数
	 */
	private Integer count_done;
	/**
	 * 统计总金额
	 */
	private double count_money;
	/**
	 * 统计当前已缴费金额
	 */
	private double count_current;

	public void setCount_current(double count_current) {
		this.count_current = count_current;
	}

	public double getCount_current() {
		return count_current;
	}

	public void setCount_money(double count_money) {
		this.count_money = count_money;
	}

	public double getCount_money() {
		return count_money;
	}

	public Integer getCount_done() {
		return count_done;
	}
	public void setCount_done(Integer count_done) {
		this.count_done = count_done;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
