package com.ninesky.classtao.pay.service;

import com.ninesky.classtao.pay.vo.PayNotifyVO;
import com.ninesky.classtao.pay.vo.TradeVO;

public interface AlipayService {

		String payment(TradeVO vo);

		void insertPayNotify(PayNotifyVO vo);

		String webPayment(TradeVO tradeVO);
}
