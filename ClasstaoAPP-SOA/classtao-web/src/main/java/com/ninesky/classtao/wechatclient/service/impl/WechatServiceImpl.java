package com.ninesky.classtao.wechatclient.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserSnsVO;
import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechatclient.message.WechatAttendanceMessage;
import com.ninesky.classtao.wechatclient.message.WechatGetuiPush;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.classtao.wechatclient.vo.WechatUserVO;
import com.ninesky.classtao.wechatclient.vo.WxAccountTemplateVO;
import com.ninesky.common.Constants;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.SystemConfig;

@Service("wechatService")
public class WechatServiceImpl implements WechatService{
	
	@Autowired
	private GeneralDAO dao;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private WxApiService wxApiService;
	@Autowired
	private UserService userService;
	
	@Override
	public WechatUserVO getWechatUser(String appId, String secret, String code){
		//step1. 通过code获取access_token
		String turl = String.format(Constants.WECHAT_ACCESS_TOKEN_URL, appId, secret, code);
		Document doc = null;
		try {
			doc = Jsoup.connect(turl).ignoreContentType(true).get();
		} catch (IOException e) {
			throw new BusinessException("通过code获取access_token失败");
		}
		JSONObject tjson = JSON.parseObject(doc.body().html());
		
		String accessToken = null;
		if(tjson.get("errcode") != null){
			throw new BusinessException("微信账号登录暂时不可用，或用户取消了授权：" + tjson.get("errmsg"));
		}else{
			accessToken = String.valueOf(tjson.get("access_token"));
		}
		String openid = String.valueOf(tjson.get("openid"));
		
		//step2. 根据openid获取用户信息
		String uurl = String.format(Constants.WECHAT_USERINFO_URL, accessToken, openid);
		
		try {
			doc = Jsoup.connect(uurl).ignoreContentType(true).get();
		} catch (IOException e) {
			throw new BusinessException("根据openid获取用户信息失败");
		}
		JSONObject ijson = JSON.parseObject(doc.body().html());
		
		if(ijson.get("errcode") != null){
			throw new BusinessException("根据openid获取用户信息失败:" + ijson.get("errmsg"));
		}
		
		WechatUserVO user = new WechatUserVO();
		user.setHead_img_url(ijson.getString("headimgurl"));
		user.setNick_name(ijson.getString("nickname"));
		user.setOpen_id(ijson.getString("openid"));
		user.setSex(ijson.getInteger("sex"));
		
		return user;
	}

	@Override
	public void pushAttendanceMessage(Map<String,String> dataMap,Integer studentId, Integer schoolId) {
		
		//发送给绑定了微信家长端的学生家长
		List<UserSnsVO> snses = userService.getParentSnsAccount(Constants.SNS_TYPE_WECHAT, studentId);
		List<String> openIds = new ArrayList<String>();
		for(UserSnsVO sns : snses){
			openIds.add(sns.getAccount());
		}
		String url = SystemConfig.getProperty("API_PLATFORM_DOMAIN") 
				+ "/wechatclient/parent/" + dataMap.get("module_code") 
				+ "/" + dataMap.get("client_url")
				+ "?id=" + dataMap.get("module_pkid")
				+ "&student_id=" + dataMap.get("student_id");
		
		WechatAttendanceMessage message = new WechatAttendanceMessage();
		message.setTitle("家长，您好！");
		message.setDate(dataMap.get("attend_date"));
		message.setClassName(dataMap.get("class_name"));
		message.setName(dataMap.get("student_name"));
		message.setInfo(dataMap.get("info_content"));
		message.setRemark("点击查看详情");
		
		new Thread(new WechatGetuiPush(wxAccountService, wxApiService, this, schoolId, url, openIds, message, null)).start();
	}

	@Override
	public void addAccountTemplate(WxAccountTemplateVO vo) {
		vo.setId(dao.insertObjectReturnID("wxAccountTemplateMap.insertWxAccountTemplate", vo));
	}

	@Override
	public WxAccountTemplateVO getAccountTemplate(Integer accountId,
			String templateNumber) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("accountId", accountId);
		param.put("templateNumber", templateNumber);
		return dao.queryObject("wxAccountTemplateMap.getAccountTemplate", param);
	}
}
