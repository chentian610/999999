package com.ninesky.classtao.pay.service;

import com.ninesky.classtao.pay.vo.PayVO;
import com.ninesky.classtao.pay.vo.PayDetailVO;

import java.util.List;
import java.util.Map;

public interface PayService {

	/**
	 * 添加缴费
	 * @param vo
	 */
	public PayVO addPay(PayVO vo);

	/**
	 * 获取发布缴费的记录
	 * @param vo
	 * @return
	 */
	public List<PayVO> getPayList(PayVO vo);

	/**
	 * 获取学生/教师缴费情况
	 * @param vo
	 * @return
	 */
	public List<Map<String,Object>> getUserPayRecordList(PayVO vo);

	/**
	 * 添加缴费明细
	 * @return
	 */
	public void addPayDetail(PayDetailVO vo);

	public PayVO getPayByID(Integer pay_id);

	public String getPayGroupList(PayVO vo);
}
