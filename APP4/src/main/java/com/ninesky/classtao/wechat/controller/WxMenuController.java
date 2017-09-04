package com.ninesky.classtao.wechat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ninesky.classtao.wechat.service.NewsTemplateService;
import com.ninesky.classtao.wechat.service.VoteService;
import com.ninesky.classtao.wechat.service.WxMenuService;
import com.ninesky.classtao.wechat.vo.NewsTemplateVO;
import com.ninesky.classtao.wechat.vo.VoteVO;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechat.vo.WxMenuVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.SystemConfig;

@RestController
@RequestMapping(value="wxMenuAction")
public class WxMenuController extends WxBaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(WxMenuController.class);
	
	@Autowired
	private WxMenuService wxMenuService;
	@Autowired
	private NewsTemplateService newsTemplateService;
	@Autowired
	private VoteService voteService;
	
	/**
	 * 获得一级公众号菜单
	 * @return
	 */
	@RequestMapping(value="/getTopMenus")
	public @ResponseBody Object getTopMenus(){
		WxAccountVO account = getSessionWxAccount();
		if(account == null){
			return ResponseUtils.sendFailure("请在创建微信公众账号后，再管理菜单");
		}
		return ResponseUtils.sendSuccess(wxMenuService.getWxTopMenuByAccount(account.getAccount_id()));
	}

	/**
	 * 获得一级公众号菜单下的子菜单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getChildMenus")
	public @ResponseBody Object getChildMenus(HttpServletRequest request){
		Integer parentId = Integer.parseInt(request.getParameter("parentId"));
		return ResponseUtils.sendSuccess(wxMenuService.getWxMenuByParentId(parentId));
	}
	
	/**
	 * 根据menuId 查询菜单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getMenuById")
	public @ResponseBody Object getMenuById(HttpServletRequest request){
		Integer menuId = Integer.parseInt(request.getParameter("menuId"));
		return ResponseUtils.sendSuccess(wxMenuService.getWxMenuById(menuId));
	}
	
	/**
	 * 保存微信菜单信息
	 * @param WxMenuVO
	 * @return
	 */
	@RequestMapping(value="/saveMenu")
	public @ResponseBody Object saveMenu(WxMenuVO vo){
		if(vo.getMenu_id() != null && vo.getMenu_id() > 0){
			wxMenuService.updateWxMenu(vo);
		}else{
			WxAccountVO account = getSessionWxAccount();
			if(account == null){
				return ResponseUtils.sendFailure("请在创建微信公众账号后，再添加菜单");
			}
			if(StringUtils.isEmpty(account.getPlatform_account_id())){
				return ResponseUtils.sendFailure("微信公众账号未能创建成功，请联系管理员进行核实");
			}
			vo.setAccount_id(account.getAccount_id());
			vo.setPlatform_account_id(account.getPlatform_account_id());
			wxMenuService.addWxMenu(vo);
		}
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 删除微信菜单
	 * @param WxMenuVO
	 * @return
	 */
	@RequestMapping(value="/deleteMenu")
	public @ResponseBody Object deleteMenu(HttpServletRequest request){
		Integer menuId = Integer.parseInt(request.getParameter("menu_id"));
		WxMenuVO menu = wxMenuService.getWxMenuById(menuId);
		if(menu == null){
			return ResponseUtils.sendFailure("不存在这个菜单");
		}else{
			wxMenuService.deleteById(menuId);
			if(menu.getParent_id() == 0){
				wxMenuService.deleteByParentId(menuId);
			}
			return ResponseUtils.sendSuccess("删除成功");
		}
		
	}
	
	/**
	 * 同步菜单到公众号
	 * @param WxMenuVO
	 * @return
	 */
	@RequestMapping(value="/doSyncMenu")
	public @ResponseBody Object doSyncMenu(HttpServletRequest request){
		List<WxMenuVO> menus = wxMenuService.getWxMenuByAccount(getSessionWxAccount().getAccount_id());
		List<WxMenuVO> paramMenus = new ArrayList<WxMenuVO>();
		for(WxMenuVO menu : menus){
			try{
				WxMenuVO m = new WxMenuVO();
				BeanUtils.copyProperties(menu, m);
				//将投票类型的菜单转换成网页链接类型
				if("click".equals(m.getType()) && "vote".equals(m.getMsg_type())){
					VoteVO vote = voteService.getVoteById(Integer.parseInt(m.getTemplate_id()));
					m.setType("view");
					m.setUrl(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_VOTE_ACTIVITY_URL.replace("ACCOUNTID", m.getPlatform_account_id()).replace("VOTEID", vote.getPlatform_main_id()));
				}
				//将资讯类型的菜单转换成网页链接类型
				if("click".equals(m.getType()) && "info".equals(m.getMsg_type())){
					m.setType("view");
					m.setUrl(SystemConfig.getProperty(Constants.APP4_PLATFORM_CONFIG_KEY) + Constants.WECHAT_NEWS_CHANNEL_URL.replace("DICTID", m.getTemplate_id()).replace("SCHOOLID", String.valueOf(ActionUtil.getSchoolID())));
				}
				//将家长端类型的菜单转换成网页链接类型
				if("parent_client".equals(m.getMsg_type())){
					m.setType("view");
					m.setUrl(SystemConfig.getProperty(Constants.APP4_PLATFORM_CONFIG_KEY) + Constants.WECHAT_PARENT_CLIENT_URL.replace("ACCOUNTID", String.valueOf(m.getAccount_id())));
				}
				//更换图文消息模板id
				if("click".equals(m.getType()) && "news".equals(m.getMsg_type())){
					NewsTemplateVO template = newsTemplateService.getNewsTemplateById(Integer.parseInt(m.getTemplate_id()));
					m.setTemplate_id(template.getPlatform_template_id());
				}
				paramMenus.add(m);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		JSONArray arr = JSON.parseArray(JSON.toJSONString(paramMenus));
		Map<String, String> params = new HashMap<String, String>();
		params.put("menus", arr.toString());
		try {
			
			logger.info("---------" + getSessionWxAccount().getAccount_name() + "开始执行微信菜单同步操作--------");
			
			Document doc = Jsoup.connect(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_API_SYNCMENU_URL).data(params).ignoreContentType(true).post();
			String content = StringEscapeUtils.unescapeHtml(doc.body().html());
			JSONObject jsonResult = JSON.parseObject(content);
			if(jsonResult == null){
				logger.error("同步微信平自定义菜单失败，返回内容为null");
				return ResponseUtils.sendFailure("同步微信平自定义菜单失败");
			}
			String success = jsonResult.getString("success");
			if("false".equals(success)){
				String errorMsg = jsonResult.getString("msg");
				logger.error("同步微信平自定义菜单失败，返回错误信息：" + errorMsg);
				return ResponseUtils.sendFailure("同步微信平自定义菜单失败");
			}
			
			String menusJson = jsonResult.getString("obj");
			JSONObject o = JSON.parseObject(menusJson);
			for(WxMenuVO menu : menus){
				String platform_menu_id = o.getString(String.valueOf(menu.getMenu_id()));
				if(!platform_menu_id.equals(menu.getPlatform_menu_id())){
					menu.setPlatform_menu_id(platform_menu_id);
					wxMenuService.updateWxMenu(menu);
				}
			}
			
			logger.info("---------" + getSessionWxAccount().getAccount_name() + "执行微信菜单同步操作成功--------");
			
			return ResponseUtils.sendSuccess("同步微信平自定义菜单成功");
		} catch (Exception e) {
			logger.error("同步微信平自定义菜单发生错误", e);
			return ResponseUtils.sendFailure("同步微信平自定义菜单失败");
		}
	}
	
	 /** 获取指定类型下的模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gettemplate")
	public @ResponseBody Object gettemplate(HttpServletRequest request){
		String msgType = request.getParameter("msgType");
		switch (msgType) {
		case "news":
			return ResponseUtils.
					sendSuccess(newsTemplateService.getNewsTemplateByAccount(getSessionWxAccount().getAccount_id()));
		case "vote":
			return ResponseUtils.
					sendSuccess(voteService.getPublishVoteByAccount(getSessionWxAccount().getAccount_id()));
		default:
			break;
		}
		return ResponseUtils.sendSuccess(null);
	}
}
