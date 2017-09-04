package com.ninesky.classtao.capital.controller;

//import com.alibaba.dubbo.demo.bid.BidService;
import com.ninesky.classtao.capital.GetuiConfig;
import com.ninesky.classtao.capital.service.GetuiService;
//import com.ninesky.classtao.capital.service.GetuiService2;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.SensitiveConfig;
import com.ninesky.framework.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping(value="getuiAction")
public class GetuiController extends BaseController{

	@Autowired
	private GetuiService getuiService;


//    @Autowired
//    private GetuiService2 getuiService2;


//	@Autowired
//	private BidService bidService;
	/**
	 * 测试发送推送
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gtPushToApp")
	public Object gtPushToApp(HttpServletRequest request) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("info_title","测试标题"+DateUtil.formatDate(new Date(), DateUtil.Y_M_D_HMS));
		String string = "共产党太多第一代领导第一代领导第一代领导第一代领导的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
				+ "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
				+ "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
		map.put("info_content","测试内容"+DateUtil.formatDate(new Date(), DateUtil.Y_M_D_HMS));
		map.put("module_code",DictConstants.MODULE_CODE_NOTICE);
		map.put("module_pkid","9999");
		map.put("info_content",SensitiveConfig.replaceSensitiveWord(string, 2,"*河蟹*"));
		getuiService.pushMessage(map,ActionUtil.getUserID());
		return SystemConfig.getProperty("WEB_DOMAIN_RECORD");
	}


    /**
     * 测试发送推送
     * @param request
     * @return
     */
    @RequestMapping(value="/gtPushToApp2")
    public Object gtPushToApp2(HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("info_title","测试标题"+DateUtil.formatDate(new Date(), DateUtil.Y_M_D_HMS));
        String string = "共产党太多第一代领导第一代领导第一代领导第一代领导的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        map.put("info_content","测试内容"+DateUtil.formatDate(new Date(), DateUtil.Y_M_D_HMS));
        map.put("module_code",DictConstants.MODULE_CODE_NOTICE);
        map.put("module_pkid","9999");
        map.put("info_content",SensitiveConfig.replaceSensitiveWord(string, 2,"*河蟹*"));
//        getuiService2.pushMessage(map,ActionUtil.getUserID());
        return map;
    }


	/**
	 * 测试发送推送
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="/gtPushToApp3")
//	public Object gtPushToApp3(HttpServletRequest request) {
//		return bidService.bid(null);
//	}

	/**
	 * 获取学校个推配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getConfig")
	public Object getConfig(HttpServletRequest request) {
		return GetuiConfig.getProperty(ActionUtil.getSchoolID());
	}
}
