package com.ninesky.classtao.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ninesky.classtao.pay.PayConfig;
import com.ninesky.classtao.pay.service.AlipayService;
import com.ninesky.classtao.pay.service.PayService;
import com.ninesky.classtao.pay.vo.*;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping(value="payAction")
public class PayController extends BaseController{
    private static Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private PayService payService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private JedisDAO jedisDAO;

	/**
	 * 测试发送推送
     * 支付宝开发文档:https://doc.open.alipay.com/doc2/apiList?docType=4
	 * @return
	 */
	@RequestMapping(value="/payment")
    @ResultField(format = false)
	public Object payment(PayDetailVO vo) {
        PayVO PayVO = payService.getPayByID(vo.getPay_id());
        vo.setPay_content(PayVO.getPay_content());
        vo.setPay_title(PayVO.getPay_title());
        vo.setPay_money(PayVO.getPay_money());
        TradeVO tradeVO = new TradeVO();
        tradeVO.setSchool_id(ActionUtil.getSchoolID());
        tradeVO.setPay_money(PayVO.getPay_money());
        tradeVO.setTrade_title(vo.getPay_title());
        tradeVO.setTrade_content(PayVO.getPay_content());
        tradeVO.setModule_code(DictConstants.MODULE_CODE_SCHOOL_PAY);
        tradeVO.setPk_id(vo.getPay_id()+"");
        tradeVO.setBus_data(BeanUtil.beanToJson(vo,false));
        tradeVO.setIs_timeout(true);
        tradeVO.setNotify_url(SystemConfig.getProperty("ALIPAY_NOTIFY_URL_DEFAULT"));
        return alipayService.payment(tradeVO);
    }

    /**
     * 测试发送推送
     * 支付宝开发文档:https://doc.open.alipay.com/doc2/apiList?docType=4
     * @return
     */
    @RequestMapping(value="/webPayment")
    @ResultField(format = false)
    public void webPayment(PayDetailVO vo,HttpServletResponse httpResponse) throws IOException {
        PayVO PayVO = payService.getPayByID(vo.getPay_id());
        vo.setPay_content(PayVO.getPay_content());
        vo.setPay_title(PayVO.getPay_title());
        vo.setPay_money(PayVO.getPay_money());
        TradeVO tradeVO = new TradeVO();
        tradeVO.setSchool_id(ActionUtil.getSchoolID());
        tradeVO.setPay_money(PayVO.getPay_money());
        tradeVO.setTrade_title(vo.getPay_title());
        tradeVO.setTrade_content(PayVO.getPay_content());
        tradeVO.setModule_code(DictConstants.MODULE_CODE_SCHOOL_PAY);
        tradeVO.setPk_id(vo.getPay_id()+"");
        tradeVO.setBus_data(BeanUtil.beanToJson(vo,false));
        tradeVO.setIs_timeout(true);
        tradeVO.setNotify_url(SystemConfig.getProperty("ALIPAY_NOTIFY_URL_DEFAULT"));
        tradeVO.setReturn_url(SystemConfig.getProperty("ALIPAY_RETURN_URL_DEFAULT"));
        String form = alipayService.webPayment(tradeVO);
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 测试发送推送
     * 支付宝开发文档:https://doc.open.alipay.com/doc2/apiList?docType=4
     * @return
     */
    @RequestMapping(value="/refund")
    public Object refund(String payid,Integer school_id,Double total) {
        AliPayVO pay = PayConfig.getProperty(school_id);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(pay.getAlipay_gateway_url(), pay.getAlipay_app_id(), pay.getAlipay_app_private_key(), "json", "UTF-8", pay.getAlipay_public_key(), "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\"2217051217233073476133644\"," +
                "    \"trade_no\":\"2017051221001004430293808586\"," +
                "    \"refund_amount\":0.01," +
                "    \"refund_reason\":\"FEI正常退款\""+
                "  }");
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return response.isSuccess();
    }

    /**
     * 测试发送推送
     * @return
     */
    @RequestMapping(value="/notify")
    @ResultField(format = false)
    public Object notify(PayNotifyVO vo) {
        alipayService.insertPayNotify(vo);
        PayDetailVO payDetailVO = BeanUtil.formatJsonToBean(vo.getPassback_params(),PayDetailVO.class);
        payDetailVO.setTrade_no(vo.getTrade_no());
        payDetailVO.setOut_trade_no(vo.getOut_trade_no());
        payDetailVO.setPay_date(DateUtil.formatStringToDate(vo.getGmt_payment(),DateUtil.Y_M_D_HMS));
        payDetailVO.setTrade_status(vo.getTrade_status());
        if (DictConstants.TRADE_STATUS.equals(vo.getTrade_status()))
            payService.addPayDetail(payDetailVO);
        return "success";
    }

    /**
     * 测试发送推送
     * @return
     */
    @RequestMapping(value="/alipayReturn")
    @ResultField(format = false)
    public Object alipayReturn(PayNotifyVO vo) {
//        alipayService.insertPayNotify(vo);
//        PayDetailVO payDetailVO = BeanUtil.formatJsonToBean(vo.getPassback_params(),PayDetailVO.class);
//        payDetailVO.setTrade_no(vo.getTrade_no());
//        payDetailVO.setOut_trade_no(vo.getOut_trade_no());
//        payDetailVO.setPay_date(DateUtil.formatStringToDate(vo.getGmt_payment(),DateUtil.Y_M_D_HMS));
//        if (DictConstants.TRADE_STATUS.equals(vo.getTrade_status()))
//            payService.addPayDetail(payDetailVO);
        return "success";
    }

    /**
     * 测试发送推送
     * @return
     */
    @RequestMapping(value="/addPay")
    public @ResponseBody Object addPay(HttpServletRequest request){
        PayVO vo =BeanUtil.formatToBean(PayVO.class);
        return ResponseUtils.sendSuccess("发布收费成功", payService.addPay(vo));
    }

    /**
     * 查询缴费情况
     * @param request
     */
    @RequestMapping(value="/getPayList")
    @ResultField(includes = {"create_date","pay_id","pay_title","pay_content","school_id","end_date","pay_type","pay_money","pay_category","pay_status","is_expired","sender_name","pay_type_name","pay_category_name","user_type","pay_team_names"})
    public @ResponseBody Object getPayList(HttpServletRequest request){
        PayVO vo =BeanUtil.formatToBean(PayVO.class);
        return ResponseUtils.sendSuccess(payService.getPayList(vo));
    }

    /**
     * 获取学生/教师缴费情况
     * @param request
     */
    @RequestMapping(value="/getUserPayRecordList")
    public @ResponseBody Object getUserPayRecordList(HttpServletRequest request) {
        PayVO vo = BeanUtil.formatToBean(PayVO.class);
        return ResponseUtils.sendSuccess(payService.getUserPayRecordList(vo));
    }

    /**
     * 获取缴费团队
     * @param request
     */
    @RequestMapping(value="/getPayGroupList")
    public @ResponseBody Object getPayGroupList(HttpServletRequest request){
        PayVO vo = BeanUtil.formatToBean(PayVO.class);
        return ResponseUtils.sendSuccess(payService.getPayGroupList(vo));
    }
}
