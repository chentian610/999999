package com.ninesky.classtao.wechat.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ninesky.classtao.wechat.service.NewsItemService;
import com.ninesky.classtao.wechat.service.NewsTemplateService;
import com.ninesky.classtao.wechat.service.VoteService;
import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.NewsTemplateVO;
import com.ninesky.classtao.wechat.vo.VoteOptionVO;
import com.ninesky.classtao.wechat.vo.VoteQuestionVO;
import com.ninesky.classtao.wechat.vo.VoteVO;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechat.vo.WxNewsItemVO;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.classtao.wechatclient.util.WechatUtil;
import com.ninesky.classtao.wechatclient.vo.WxAccountTemplateVO;
import com.ninesky.common.Constants;
import com.ninesky.framework.SystemConfig;

@Service("wxApiService")
public class WxApiServiceImpl implements WxApiService{
	
	private static Logger logger = LoggerFactory.getLogger(WxApiServiceImpl.class);
	
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private NewsItemService newsItemService;
	@Autowired
	private NewsTemplateService newsTemplateService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private WechatService wechatService;

	@Override
	public void syncAccount(Integer accountId) {
		WxAccountVO account = wxAccountService.getAccountById(accountId);
		
		if(account == null){
			logger.error("同步微信平台公众号信息失败，未找到同步对象");
			return;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("account_name", account.getAccount_name());
		params.put("account_token", account.getAccount_token());
		params.put("weixin_accountid", account.getWeixin_accountid());
		params.put("account_type", String.valueOf(account.getAccount_type()));
		params.put("account_desc", account.getAccount_desc());
		params.put("account_email", account.getAccount_email());
		params.put("account_appid", account.getAccount_appid());
		params.put("account_appsecret", account.getAccount_appsecret());
		params.put("is_effective", String.valueOf(account.getIs_effective()));
		params.put("auth_status", String.valueOf(account.getAuth_status()));
		if(!StringUtils.isEmpty(account.getPlatform_account_id())){
			params.put("platform_account_id", account.getPlatform_account_id());
		}
		
		try {
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNCACCOUNT_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("同步微信平台公众号信息失败，返回内容为null");
				return;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("同步微信平台公众号信息失败，返回错误信息：" + errorMsg);
				return;
			}
			
			String platformAccountId = jsonResult.getString("obj");
			logger.info("同步微信平台公众号成功，返回微信平台id：" + platformAccountId);
			account.setPlatform_account_id(platformAccountId);
			wxAccountService.updateWxAccount(account);
			
			//为新加的公众号绑定消息模板
			String accessToken = loadAccesstoken(account);
			String sx_templateId = WechatUtil.getTemplateId(accessToken, Constants.WECHAT_MESSAGE_TEMPLATE_SX);
			String kq_templateId = WechatUtil.getTemplateId(accessToken, Constants.WECHAT_MESSAGE_TEMPLATE_KQ);
			
			WxAccountTemplateVO sx_template = new WxAccountTemplateVO();
			sx_template.setAccount_id(account.getAccount_id());
			sx_template.setWechat_template_number(Constants.WECHAT_MESSAGE_TEMPLATE_SX);
			sx_template.setWechat_template_id(sx_templateId);
			sx_template.setCreate_time(new Date());
			wechatService.addAccountTemplate(sx_template);
			
			WxAccountTemplateVO kq_template = new WxAccountTemplateVO();
			kq_template.setAccount_id(account.getAccount_id());
			kq_template.setWechat_template_number(Constants.WECHAT_MESSAGE_TEMPLATE_KQ);
			kq_template.setWechat_template_id(kq_templateId);
			kq_template.setCreate_time(new Date());
			wechatService.addAccountTemplate(kq_template);
		} catch (Exception e) {
			logger.error("同步微信平台公众号发生错误", e);
		}
	}

	@Override
	public void syncDeleteNewsItem(String platformItemId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("item_id", platformItemId);
		try {
			logger.info("---------开始执行图文消息删除操作--------");
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNC_DELETE_NEWSITEM_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("执行图文消息删除操作，返回内容为null");
				return;
			}
			
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("执行图文消息删除操作失败，返回错误信息：" + errorMsg);
			}else{
				logger.error("执行图文消息删除操作成功");
			}
		} catch (Exception e) {
			logger.error("执行图文消息删除操作发生错误", e);
		}
	}

	@Override
	public void syncDeleteNewsTemplate(String platformTemplateId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("template_id", platformTemplateId);
		try {
			logger.info("---------开始执行图文消息模板删除操作--------");
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNC_DELETE_NEWSTEMPLATE_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("执行图文消息模板删除操作，返回内容为null");
				return;
			}
			
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("执行图文消息模板删除操作失败，返回错误信息：" + errorMsg);
			}else{
				logger.error("执行图文消息模板删除操作成功");
			}
		} catch (Exception e) {
			logger.error("执行图文消息模板删除操作发生错误", e);
		}
	}

	@Override
	public void syncNewsTemplate(Integer templateId) {
		NewsTemplateVO vo = newsTemplateService.getNewsTemplateById(templateId);
		
		if(vo == null){
			logger.error("同步微信图文素材模板信息失败，未找到同步对象");
			return;
		}
		
		JSONObject o = JSON.parseObject(JSON.toJSONString(vo));
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsTemplate", o.toString());
		try {
			
			logger.info("---------开始执行微信图文素材模板同步操作--------");
			logger.info("请求参数--------" + o.toString());
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNC_NEWSTEMPLATE_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("同步微信图文素材模板失败，返回内容为null");
				return;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("同步微信图文素材模板失败，返回错误信息：" + errorMsg);
				return;
			}
			
			String platform_template_id = jsonResult.getString("obj");
			vo.setPlatform_template_id(platform_template_id);
			newsTemplateService.updateNewsTemplate(vo);
			
			logger.info("---------执行微信图文素材模板同步操作成功--------");
			
		} catch (Exception e) {
			logger.error("同步微信图文素材发生错误", e);
		}
	}

	@Override
	public void syncNewsItem(Integer itemId) {
		
		WxNewsItemVO vo = newsItemService.getNewsItemById(itemId);
		if(vo == null){
			logger.error("同步微信图文素材信息失败，未找到同步对象");
			return;
		}
		
		JSONObject o = JSON.parseObject(JSON.toJSONString(vo));
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsItem", o.toString());
		try {
			
			logger.info("---------开始执行微信图文素材同步操作--------");
			logger.info("请求参数--------" + o.toString());
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNC_NEWSITEM_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("同步微信图文素材失败，返回内容为null");
				return;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("同步微信图文素材失败，返回错误信息：" + errorMsg);
				return;
			}
			
			String platform_item_id = jsonResult.getString("obj");
			vo.setPlatform_item_id(platform_item_id);
			newsItemService.updateNewsItem(vo);
			
			logger.info("--------执行微信图文素材同步操作成功--------");
			
		} catch (Exception e) {
			logger.error("同步微信图文素材发生错误", e);
		}
		
	}

	@Override
	public void uploadNewsTemplate(List<NewsTemplateVO> templates,
			String platform_account_id) {
		List<String> platformTemplateIds = new ArrayList<String>();
		Map<String, NewsTemplateVO> templateMap = new HashMap<String, NewsTemplateVO>();
		for(NewsTemplateVO template : templates){
			platformTemplateIds.add(template.getPlatform_template_id());
			templateMap.put(template.getPlatform_template_id(), template);
		}
		
		JSONArray o = JSON.parseArray(JSON.toJSONString(platformTemplateIds));
		Map<String, String> params = new HashMap<String, String>();
		params.put("templateIds", o.toString());
		params.put("accountId", platform_account_id);
		try {
			
			logger.info("---------开始执行上传微信图文素材同步操作--------");
			logger.info("请求参数--------" + o.toString());
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_UPLOAD_NEWStEMPLATE_URL).data(params).ignoreContentType(true).timeout(100000).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("上传微信图文素材失败，返回内容为null");
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("上传微信图文素材失败，返回错误信息：" + errorMsg);
			}
			
			String result = jsonResult.getString("obj");
			JSONObject templateJson = JSON.parseObject(result);
			for(String id : platformTemplateIds){
				NewsTemplateVO template = templateMap.get(id);
				String messageId = templateJson.getString(id);
				try{
					if(StringUtils.isEmpty(messageId)){
						//FIXME 未能上传成功，暂时把状态改回“未上传”
						template.setUpload_status(Constants.WECHAT_NEWSTEMPLATE_UPLOADSTATUS_NONE);
						template.setPlatform_message_id(null);
					}else{
						template.setUpload_status(Constants.WECHAT_NEWSTEMPLATE_UPLOADSTATUS_SUCCESS);
						template.setPlatform_message_id(messageId);
					}
				}catch(Exception e){
					logger.error("上传微信图文素材发生错误", e);
					//FIXME 未能上传成功，暂时把状态改回“未上传”
					template.setUpload_status(Constants.WECHAT_NEWSTEMPLATE_UPLOADSTATUS_NONE);
					template.setPlatform_message_id(null);
				}
				newsTemplateService.updateUploadStatus(template.getTemplate_id(), template.getUpload_status(), template.getPlatform_message_id());
			}
			logger.info("--------上传微信图文素材同步操作成功--------");
			
		} catch (Exception e) {
			for(String id : platformTemplateIds){
				//FIXME 未能上传成功，暂时把状态改回“未上传”
				NewsTemplateVO template = templateMap.get(id);
				template.setUpload_status(Constants.WECHAT_NEWSTEMPLATE_UPLOADSTATUS_NONE);
				template.setPlatform_message_id(null);
				newsTemplateService.updateUploadStatus(template.getTemplate_id(), template.getUpload_status(), template.getPlatform_message_id());
			}
			logger.error("上传微信图文素材发生错误", e);
		}
	}

	@Override
	public void syncVote(Integer voteId) {
		
		VoteVO vote = voteService.getVoteById(voteId);
		
		if(vote == null){
			logger.error("发布投票活动失败，未找到发布对象");
			return;
		}
		
		WxAccountVO account = wxAccountService.getAccountById(vote.getAccount_id());
		Map<String, String> params = new HashMap<String, String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jo = JSON.parseObject(JSON.toJSONString(vote));
		params.put("vote", jo.toString());
		params.put("accountid", account.getPlatform_account_id());
		params.put("begin_date", df.format(vote.getBegin_date()));
		params.put("valid_date", df.format(vote.getValid_date()));
		try {
			
			logger.info("---------开始执行投票【" + vote.getTitle() + "】发布操作--------");
			
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_PUBLISH_VOTE_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("发布投票失败，返回内容为null");
				vote.setStatement(Constants.WECHAT_VOTE_STATEMENT_UNPUBLISH);
				voteService.updateVote(vote);
				return;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("发布投票失败，返回错误信息：" + errorMsg);
				vote.setStatement(Constants.WECHAT_VOTE_STATEMENT_UNPUBLISH);
				voteService.updateVote(vote);
				return;
			}
			String voteResult = jsonResult.getString("obj");
			JSONObject voteJson = JSON.parseObject(voteResult);
			//对应微信平台调研主题id
			String platform_main_id = voteJson.getString("platform_main_id");
			vote.setPlatform_main_id(platform_main_id);
			//对应微信平台调研问题的id
			JSONObject qusResult = voteJson.getJSONObject("qusResult");
			//对应微信平台调研问题选项的id
			JSONObject optResult = voteJson.getJSONObject("optResult");

			List<VoteQuestionVO> questions = vote.getQuestions();
			if(questions != null && questions.size() > 0){
				for(VoteQuestionVO q : questions){
					String platform_survey_id = qusResult.getString(String.valueOf(q.getQuestion_id()));
					q.setPlatform_main_id(platform_main_id);
					q.setPlatform_survey_id(platform_survey_id);
					voteService.updateQuestion(q);
					List<VoteOptionVO> options = q.getOptions();
					if(options != null && options.size() > 0){
						for(VoteOptionVO opt : options){
							String platform_option_id = optResult.getString(String.valueOf(opt.getOption_id()));
							opt.setPlatform_option_id(platform_option_id);
							opt.setPlatform_survey_id(platform_survey_id);
							voteService.updateOption(opt);
						}
					}
				}
			}
			
			vote.setStatement(Constants.WECHAT_VOTE_STATEMENT_PUBLISHED);
			voteService.updateVote(vote);
			
			logger.info("发布投票【" + vote.getTitle() + "】成功");
		} catch (Exception e) {
			logger.error("发布投票【" + vote.getTitle() + "】发生错误", e);
			vote.setStatement(Constants.WECHAT_VOTE_STATEMENT_UNPUBLISH);
			voteService.updateVote(vote);
		}
	}

	@Override
	public void sendGroupMessage(String msgtype, String textcontent,
			String newsTemplateId, String platformAccountId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("msgtype", msgtype);
		params.put("accountId", platformAccountId);
		if("text".equals(msgtype)){
			params.put("param", textcontent);
		}else if("mpnews".equals(msgtype)){
			params.put("templateId", newsTemplateId);
		}
		
		try {
			logger.info("---------开始执行群发消息操作--------");
			logger.info("请求参数------【msgtype:" + msgtype + "】,【accountId:" + platformAccountId + "】,【param:" + params.get("param") + "】,【templateId:" + params.get("templateId") + "】");
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_GROUP_MESSAGE_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("群发消息失败，返回内容为null");
				return;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("群发消息失败，返回错误信息：" + errorMsg);
			}else{
				logger.info("--------群发消息操作成功--------");
			}
		} catch (Exception e) {
			logger.error("群发消息发生错误", e);
		}
	}

	@Override
	public Map<String, String> loadJsapiTicket(Integer accountId, String requestUrl) {
		
		WxAccountVO account = wxAccountService.getAccountById(accountId);
		if(account == null){
			logger.warn("指定的公众号未找到");
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountid", account.getPlatform_account_id());
		params.put("url", requestUrl);
		try {
			
			logger.info("---------开始调取公众号的Jsapi凭证--------");
			
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_GET_JSAPI_TICKET).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("调取公众号的Jsapi凭证失败，返回内容为null");
				return null;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("调取公众号的Jsapi凭证失败，返回错误信息：" + errorMsg);
				return null;
			}
			String ticketResult = jsonResult.getString("obj");
			JSONObject ticketJson = JSON.parseObject(ticketResult);
			Map<String, String> jsapiTicket = new HashMap<String, String>();
			jsapiTicket.put("appId", ticketJson.getString("appId"));
			jsapiTicket.put("timestamp", ticketJson.getString("timestamp"));
			jsapiTicket.put("nonceStr", ticketJson.getString("nonceStr"));
			jsapiTicket.put("signature", ticketJson.getString("signature"));
			
			logger.info("---------调取公众号的Jsapi凭证成功-------【" + jsapiTicket + "】");
			
			return jsapiTicket;
		} catch (Exception e) {
			logger.error("调取公众号的Jsapi凭证失败", e);
		}
		return null;
	}

	@Override
	public String loadAccesstoken(Integer accountId) {
		WxAccountVO account = wxAccountService.getAccountById(accountId);
		return loadAccesstoken(account);
	}

	@Override
	public String loadAccesstoken(WxAccountVO account) {
		if(account == null){
			logger.warn("指定的公众号未找到");
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountid", account.getPlatform_account_id());
		try {
			
			logger.info("---------开始调取公众号的accesstoken--------");
			
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_GET_ACCESSTOKEN).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("调取公众号的accesstoken失败，返回内容为null");
				return null;
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("调取公众号的accesstoken失败，返回错误信息：" + errorMsg);
				return null;
			}
			String accessToken = jsonResult.getString("obj");
			
			logger.info("---------调取公众号的accesstoken成功-------【" + accessToken + "】");
			
			return accessToken;
		} catch (Exception e) {
			logger.error("调取公众号的accesstoken失败", e);
		}
		return null;
	}
}
