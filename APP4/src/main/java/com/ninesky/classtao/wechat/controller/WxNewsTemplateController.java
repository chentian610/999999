package com.ninesky.classtao.wechat.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.wechat.service.NewsItemService;
import com.ninesky.classtao.wechat.service.NewsTemplateService;
import com.ninesky.classtao.wechat.vo.NewsTemplateVO;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechat.vo.WxNewsItemVO;
import com.ninesky.common.Constants;
import com.ninesky.framework.ResponseUtils;

@RestController
@RequestMapping(value="wxNewsTemplateAction")
public class WxNewsTemplateController extends WxBaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(WxNewsTemplateController.class);

	@Autowired
	private NewsTemplateService newsTemplateService;
	@Autowired
	private NewsItemService newsItemService;
	/**
	 * 获得公众号下的图文模板
	 * @return
	 */
	@RequestMapping(value="/getTemplates")
	public @ResponseBody Object getTemplates(){
		WxAccountVO account = getSessionWxAccount();
		if(account == null){
			return ResponseUtils.sendFailure("请在创建微信公众账号后，再管理图文素材");
		}
		return ResponseUtils.sendSuccess(newsTemplateService.getNewsTemplateByAccount(account.getAccount_id()));
	}
	
	/**
	 * 根据id获得某个的图文模板
	 * @return
	 */
	@RequestMapping(value="/getTemplate")
	public @ResponseBody Object getTemplate(HttpServletRequest request){
		Integer templateId = Integer.parseInt(request.getParameter("templateId"));
		return ResponseUtils.sendSuccess(newsTemplateService.getNewsTemplateById(templateId));
	}
	
	/**
	 * 删除图文模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteTemplate")
	public @ResponseBody Object deleteTemplate(HttpServletRequest request){
		Integer templateId = Integer.parseInt(request.getParameter("templateId"));
		NewsTemplateVO template = newsTemplateService.getNewsTemplateById(templateId);
		if(template == null){
			return ResponseUtils.sendFailure("不存在这个图文素材模板");
		}else{
			newsTemplateService.deleteById(templateId);
			newsItemService.deleteByTemplate(templateId);
			final String platformTemplateId = template.getPlatform_template_id();
			new Thread(){
				public void run() {
					wxApiService.syncDeleteNewsTemplate(platformTemplateId);
				};
			}.start();
			return ResponseUtils.sendSuccess("删除成功");
		}
	}
	
	/**
	 * 保存图文模板
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/saveTemplate")
	public @ResponseBody Object saveTemplate(NewsTemplateVO vo){
		if(vo.getTemplate_id() != null && vo.getTemplate_id() > 0){
			newsTemplateService.updateNewsTemplate(vo);
		}else{
			WxAccountVO account = getSessionWxAccount();
			if(account == null){
				return ResponseUtils.sendFailure("请在创建微信公众账号后，再添加图文模板");
			}
			if(StringUtils.isEmpty(account.getPlatform_account_id())){
				return ResponseUtils.sendFailure("微信公众账号未能创建成功，请联系管理员进行核实");
			}
			vo.setCreate_date(new Date());
			vo.setAccount_id(account.getAccount_id());
			vo.setPlatform_account_id(account.getPlatform_account_id());
			newsTemplateService.addNewsTemplate(vo);
		}
		
		final Integer templateId = vo.getTemplate_id();
		new Thread(){
			public void run() {
				wxApiService.syncNewsTemplate(templateId);
			};
		}.start();
		
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 上传图文模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/uploadTemplates")
	public @ResponseBody Object uploadTemplates(HttpServletRequest request){
		String templateids = request.getParameter("templateids");
		if(StringUtils.isEmpty(templateids)){
			return ResponseUtils.sendFailure("缺少需要上传的图文id");
		}
		
		logger.info("----开始执行微信图文素材上传操作---执行的模板id=" + templateids);
		String ids[] = templateids.split(",");
		final List<NewsTemplateVO> templates = new ArrayList<NewsTemplateVO>();
		for(String id : ids){
			NewsTemplateVO template = newsTemplateService.getNewsTemplateById(Integer.parseInt(id));
			newsTemplateService.updateUploadStatus(Integer.parseInt(id), Constants.WECHAT_NEWSTEMPLATE_UPLOADSTATUS_ING);
			templates.add(template);
		}
		
		NewsTemplateVO template = newsTemplateService.getNewsTemplateById(Integer.parseInt(ids[0]));
		WxAccountVO account = wxAccountService.getAccountById(template.getAccount_id());
		final String platform_account_id = account.getPlatform_account_id();
		
		new Thread(){
			public void run() {
				wxApiService.uploadNewsTemplate(templates, platform_account_id);
			};
		}.start();
		
		return ResponseUtils.sendSuccess("上传请求成功发出");
	}
	
	/**
	 * 保存图文素材
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/saveItem")
	public @ResponseBody Object saveItem(WxNewsItemVO vo){
		if(vo.getItem_id() != null && vo.getItem_id() > 0){
			newsItemService.updateNewsItem(vo);
		}else{
			vo.setCreate_date(new Date());
			NewsTemplateVO template = newsTemplateService.getNewsTemplateById(vo.getNews_template_id());
			vo.setPlatform_news_template_id(template.getPlatform_template_id());
			newsItemService.addNewsItem(vo);
		}
		
		final Integer itemId = vo.getItem_id();
		new Thread(){
			public void run() {
				wxApiService.syncNewsItem(itemId);
			};
		}.start();
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 获取某个图文模板的图文素材
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getItems")
	public @ResponseBody Object getItems(HttpServletRequest request){
		Integer templateId = Integer.parseInt(request.getParameter("templateId"));
		return ResponseUtils.sendSuccess(newsItemService.getNewsItemByTemplate(templateId));
	}
	
	/**
	 * 获取某个图文素材
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getItem")
	public @ResponseBody Object getItem(HttpServletRequest request){
		Integer itemId = Integer.parseInt(request.getParameter("itemId"));
		return ResponseUtils.sendSuccess(newsItemService.getNewsItemById(itemId));
	}
	
	/**
	 * 删除图文素材
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteItem")
	public @ResponseBody Object deleteItem(HttpServletRequest request){
		Integer itemId = Integer.parseInt(request.getParameter("itemId"));
		WxNewsItemVO item = newsItemService.getNewsItemById(itemId);
		if(item == null){
			return ResponseUtils.sendFailure("不存在这个图文素材");
		}else{
			final String platformItemId = item.getPlatform_item_id();
			newsItemService.deleteById(itemId);
			new Thread(){
				public void run() {
					wxApiService.syncDeleteNewsItem(platformItemId);
				};
			}.start();
			return ResponseUtils.sendSuccess("删除成功");
		}
	}
	
	/**
	 * 获取所有上传的图文模板
	 * @return
	 */
	@RequestMapping(value="/loadAllUploadNewsTemplate")
	public @ResponseBody Object loadAllUploadNewsTemplate(){
		WxAccountVO account = getSessionWxAccount();
		if(account == null){
			return ResponseUtils.sendFailure("未知的微信公众号");
		}
		
		List<NewsTemplateVO> templates = newsTemplateService.getAllUploadTemplate(account.getAccount_id());
		for(NewsTemplateVO vo : templates){
			vo.setShow_pic(newsItemService.getTemplateShowPic(vo.getTemplate_id()));
		}
		return ResponseUtils.sendSuccess(templates);
	}
}
