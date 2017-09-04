package com.ninesky.classtao.balance.controller;


import com.ninesky.classtao.balance.service.BalanceService;
import com.ninesky.classtao.balance.vo.BalanceVO;
import com.ninesky.classtao.pay.service.AlipayService;
import com.ninesky.classtao.pay.vo.PayNotifyVO;
import com.ninesky.classtao.pay.vo.TradeVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping(value = "balanceAction")
public class BalanceController extends BaseController{
	
	@Autowired
	private BalanceService balanceService;


	@Autowired
	private AlipayService alipayService;

	@Autowired
	private RedisService redisService;

	/**
	 * 查询代理商余额
	 * @param request
	 */
	@RequestMapping(value="/getBalanceByID")
	public @ResponseBody Object getBalanceByID(HttpServletRequest request){
		BalanceVO vo = BeanUtil.formatToBean(BalanceVO.class);
		return ResponseUtils.sendSuccess(balanceService.getBalanceByID(vo));
	}

	/**
	 * 查询代理商余额
	 * @param request
	 */
	@RequestMapping(value="/getBalanceByPhone")
	public @ResponseBody Object getBalanceByPhone(HttpServletRequest request){
		BalanceVO vo = BeanUtil.formatToBean(BalanceVO.class);
		return ResponseUtils.sendSuccess(balanceService.getBalanceByPhone(vo));
	}

	/**
	 * 代理商充值
	 * @param request
	 */
	@RequestMapping(value="/notify")
	@ResultField(format = false)
	public @ResponseBody Object rechargeBalance(PayNotifyVO vo,HttpServletRequest request){
		alipayService.insertPayNotify(vo);
		if (DictConstants.TRADE_STATUS.equals(vo.getTrade_status())) {
			BalanceVO balanceVO = BeanUtil.formatJsonToBean(vo.getPassback_params(),BalanceVO.class);
			balanceVO.setTrade_no(vo.getTrade_no());
			balanceVO.setOut_trade_no(vo.getOut_trade_no());
			balanceVO.setTrade_status(vo.getTrade_status());
			balanceService.rechargeBalance(balanceVO);
		}
		return "success";
	}

	/**
	 * 代理商消费
	 * @param request
	 */
	@RequestMapping(value="/consumptionBalance")
	public @ResponseBody Object consumptionBalance(HttpServletRequest request){
		BalanceVO vo = BeanUtil.formatToBean(BalanceVO.class);
		vo.setIp_address(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		return ResponseUtils.sendSuccess(balanceService.consumptionBalance(vo));
	}

	/**
	 * 代理商消费
	 * @param request
	 */
	@RequestMapping(value="/rechargeBalance")
	public @ResponseBody Object rechargeBalance(HttpServletRequest request){
		BalanceVO vo = BeanUtil.formatToBean(BalanceVO.class);
		vo.setIp_address(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		return ResponseUtils.sendSuccess(balanceService.rechargeBalance(vo));
	}

	/**
	 * 测试发送推送
	 * 支付宝开发文档:https://doc.open.alipay.com/doc2/apiList?docType=4
	 * @return
	 */
	@RequestMapping(value="/webPayment")
	@ResultField(format = false)
	public void webPayment(HttpServletRequest request, HttpServletResponse httpResponse) throws ServletException, IOException {
		BalanceVO vo = BeanUtil.formatToBean(BalanceVO.class);
		vo.setIp_address(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		vo.setUser_type(ActionUtil.getUserType());
		String content = MsgService.getMsg("BALANCE_AGENT_RECHARGE_CONTENT",balanceService.getAgentByAgentID(vo.getAgent_id()).getAgent_name(),vo.getConsumption_money());
		TradeVO tradeVO = new TradeVO();
		tradeVO.setSchool_id(0);
		tradeVO.setPay_money(vo.getConsumption_money());
		tradeVO.setTrade_title(content);
		tradeVO.setTrade_content(content);
		tradeVO.setModule_code(ActionUtil.getUserType());
		tradeVO.setPk_id(vo.getAgent_id()+"");
		tradeVO.setBus_data(BeanUtil.beanToJson(vo,false));
		tradeVO.setIs_timeout(true);
		tradeVO.setReturn_url(SystemConfig.getProperty("ALIPAY_RETURN_URL_AGENT"));
		tradeVO.setNotify_url(SystemConfig.getProperty("ALIPAY_NOTTIFY_URL_AGENT"));
		String form = alipayService.webPayment(tradeVO);
		httpResponse.setContentType("text/html;charset=UTF-8");
		httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}
}
