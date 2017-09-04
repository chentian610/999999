package com.ninesky.classtao.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.pay.PayConfig;
import com.ninesky.classtao.pay.service.AlipayService;
import com.ninesky.classtao.pay.vo.AliPayVO;
import com.ninesky.classtao.pay.vo.PayNotifyVO;
import com.ninesky.classtao.pay.vo.TradeVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AlipayServiceImpl")
public class AlipayServiceImpl implements AlipayService {
	private static Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

	@Autowired
	private GeneralDAO dao;

	@Autowired
	private RedisService redisService;

	@Autowired
	private DynamicService dynamicService;

	@Autowired
	private GetuiService getuiService;

	@Autowired
	private JedisDAO jedisDAO;

	@Override
	public String payment(TradeVO vo) {
		if (StringUtil.isEmpty(vo.getTrade_title()) || StringUtil.isEmpty(vo.getModule_code()) ||StringUtil.isEmpty(vo.getPk_id()) ) throw new RuntimeException(MsgService.getMsg("ALIPAY_TRADE_INFO_ISNOT_FULL"));
		if (vo.getPay_money()<=0) throw new RuntimeException(MsgService.getMsg("ALIPAY_TRADE_AMOUNT_ISEMPTY"));
		AliPayVO pay = PayConfig.getProperty(vo.getSchool_id());
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(pay.getAlipay_gateway_url(), pay.getAlipay_app_id(), pay.getAlipay_app_private_key(), "json", "UTF-8", pay.getAlipay_public_key(), "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(vo.getTrade_content());
		model.setSubject(vo.getTrade_title());
		model.setOutTradeNo(getTradeNo(vo));
		model.setTotalAmount(vo.getPay_money()+"");
		request.setNotifyUrl(vo.getNotify_url());
		//业务数据
		model.setPassbackParams(vo.getBus_data());
//		if (vo.is_timeout()) model.setTimeExpire(SystemConfig.getProperty("ALIPAY_TIMEOUT_EXPRESS"));
		request.setBizModel(model);
		model.setProductCode("QUICK_MSECURITY_PAY");
		try {
			//这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			String returnMsg = response.getBody();
			logger.info(returnMsg);
			return response.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public String webPayment(TradeVO vo) {
		if (StringUtil.isEmpty(vo.getTrade_title()) || StringUtil.isEmpty(vo.getModule_code()) ||StringUtil.isEmpty(vo.getPk_id()) ) throw new RuntimeException(MsgService.getMsg("ALIPAY_TRADE_INFO_ISNOT_FULL"));
		if (vo.getPay_money()<=0) throw new RuntimeException(MsgService.getMsg("ALIPAY_TRADE_AMOUNT_ISEMPTY"));
		AliPayVO pay = PayConfig.getProperty(vo.getSchool_id());
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(pay.getAlipay_gateway_url(), pay.getAlipay_app_id(), pay.getAlipay_app_private_key(), "json", "UTF-8", pay.getAlipay_public_key(), "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradePagePayModel model = new AlipayTradePagePayModel();
		model.setBody(vo.getTrade_content());
		model.setSubject(vo.getTrade_title());
		model.setOutTradeNo(getTradeNo(vo,true));
		model.setTotalAmount(vo.getPay_money()+"");
		request.setReturnUrl(vo.getReturn_url());
		request.setNotifyUrl(vo.getNotify_url());
		//业务数据
		model.setPassbackParams(vo.getBus_data());
		model.setProductCode("FAST_INSTANT_TRADE_PAY");
		request.setBizModel(model);
		try {
			//这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
			String returnMsg = response.getBody();
			logger.info(returnMsg);
			return response.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void insertPayNotify(PayNotifyVO vo) {
		dao.insertObject("payNotifyMap.insertPayNotify",vo);
	}


	/**
	 * 获取订单号，[模块代号]+S+[学校ID]+U+[用户类型]+[用户ID或学生ID]+B+[主键ID]
	 * @return
	 * @throws Exception
	 */
	public String getTradeNo(TradeVO vo) {
		return getTradeNo(vo,false);
	}

	/**
	 * 获取订单号，[模块代号]+S+[学校ID]+U+[用户类型]+[用户ID或学生ID]+B+[主键ID]
	 * @param vo 订单业务信息
	 * @param flag 是否允许重复提交，如果允许重复提交，则订单增加流水号信息
	 * @return
	 * @throws Exception
	 */
	public String getTradeNo(TradeVO vo,boolean flag) {
		String trade_no = "";
		String test_trade = SystemConfig.getProperty("TEST_TRADE");
		if (StringUtil.isNotEmpty(test_trade)) trade_no = test_trade;
		switch (vo.getModule_code()) {
			case DictConstants.MODULE_CODE_SCHOOL_PAY:
				trade_no+="P";
				break;
			/**
			 * 这里继续添加其他支付类型
			 */
			default:trade_no+="D";
		}
		trade_no+="S"+ActionUtil.getSchoolID();
		if (IntegerUtil.isNotEmpty(ActionUtil.getStudentID()))
			trade_no+="U"+DictConstants.USERTYPE_STUDENT+ActionUtil.getStudentID();
		else trade_no+="U"+DictConstants.USERTYPE_TEACHER+ActionUtil.getUserID();
		trade_no+="B"+vo.getPk_id();//业务+主键ID
		if (flag) trade_no+="N"+jedisDAO.incr("TRADE_INFO_NO");//添加流水号
		logger.info("业务订单号为："+trade_no);
		return trade_no;
	}
}
