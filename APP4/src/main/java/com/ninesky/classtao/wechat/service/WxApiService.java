package com.ninesky.classtao.wechat.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.wechat.vo.NewsTemplateVO;
import com.ninesky.classtao.wechat.vo.WxAccountVO;

public interface WxApiService {
	
	/**
	 * 同步微信公众号到微信平台
	 */
	public void syncAccount(Integer accountId);
	/**
	 * 同步删除图文素材
	 */
	public void syncDeleteNewsItem(String platformItemId);
	/**
	 * 同步删除图文素材模板
	 */
	public void syncDeleteNewsTemplate(String platformTemplateId);
	/**
	 * 同步图文素材模板信息到微信平台
	 */
	public void syncNewsTemplate(Integer templateId);
	/**
	 * 同步图文素材到微信平台
	 */
	public void syncNewsItem(Integer itemId);
	/**
	 * 上传图文素材模板到微信平台（上传后的模板可用于群发消息）
	 */
	public void uploadNewsTemplate(List<NewsTemplateVO> templates, String platform_account_id);
	/**
	 * 发布校园投票活动到微信平台
	 */
	public void syncVote(Integer voteId);
	/**
	 *群发消息
	 */
	public void sendGroupMessage(String msgtype, String textcontent, String newsTemplateId, String platformAccountId);
	/**
	 * 调取公众号jsapi凭证
	 */
	public Map<String, String> loadJsapiTicket(Integer accountId, String requestUrl);
	/**
	 * 调取公众号access token
	 */
	public String loadAccesstoken(Integer accountId);
	/**
	 * 调取公众号access token
	 */
	public String loadAccesstoken(WxAccountVO account);
}
