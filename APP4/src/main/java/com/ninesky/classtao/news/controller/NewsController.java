package com.ninesky.classtao.news.controller;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.news.service.NewsService;
import com.ninesky.classtao.news.vo.NewsCodeVO;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.FileOperateUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="newsAction")
public class NewsController extends BaseController{
	@Autowired
	private NewsService newsService;
	@Autowired
	private DynamicService dynamicService;
	@Autowired
	private GetuiService getuiService;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 添加新闻信息并发送动态信息和推送
	 * @param vo
	 */
	@PutCache(name="newsList",value="school_id,news_code222")
	@RequestMapping(value="/addNews")
	@ResultField(includes={"news_id","title","content","main_pic_url","dept_name","deploy_date","module_code","dict_group","template_type"})
	public @ResponseBody Object addNews(NewsVO vo){
		vo.setDict_group(ActionUtil.getParameter("dict_group"));
		newsService.addNews(vo);
//		newsService.addInformation(vo);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("module_code",DictConstants.MODULE_CODE_SCHOOLSTYLE);
		//生成module_pkid
		dataMap.put("module_pkid",vo.getNews_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		String user_type = DictConstants.NEWS_DYJS_DICT_GROUP.equals(vo.getDict_group())?DictConstants.USERTYPE_TEACHER:DictConstants.USERTYPE_ALL;
		dataMap.put("user_type", user_type);
		dataMap.put("info_title",StringUtil.subString(vo.getTitle(),60));
		dataMap.put("info_content",StringUtil.subString(vo.getContent_text(),60));
		dataMap.put("news_code",vo.getNews_code());
		dataMap.put("dict_group", vo.getDict_group());
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		ReceiveVO receive = new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.TEAM_TYPE_CLASS,0,0);
		receiveList.add(receive);
		dynamicService.insertDynamic(dataMap,receiveList);
		//对相应用户分组进行推送
		getuiService.pushMessage(dataMap,user_type);
		return vo;
	}
	
	/**
	 * 根据查询条件获取新闻信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getNewsList")
	@ResultField(includes={"news_id","code_name","title","content","content_text","main_pic_url","dept_name","deploy_date","news_code","dict_group","template_type","item_list","file_list"})
	public @ResponseBody Object getNewsList(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
        return newsService.getNewsList(paramMap);
	}
	
	/**
	 * 获取app登录后首页新闻列表
	 * @param request
	 * @return
	 */
	@GetCache(name="newsList",value="school_id,news_code")
	@RequestMapping(value="/getNewsListForAPP")
	@ResultField(excludes = {"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version","description","id","news_code_list","is_active","search","school_id"})
	public @ResponseBody Object getNewsListForAPP(DictSchoolVO vo,HttpServletRequest request){
        vo.setDict_group(StringUtil.isNotEmpty(vo.getDict_group())?vo.getDict_group():StringUtil.isNotEmpty(ActionUtil.getParameter("news_group"))?ActionUtil.getParameter("news_group"):DictConstants.DCIT_GROUP_NEWS);
		List<DictSchoolVO>  dictSchoolList = null;
        dictSchoolList = newsService.getNewsListForAPP(vo);
		// start 如果news_group不等于空则将数据dictSchoolList转换成newsCodeList返回
		if (StringUtil.isNotEmpty(ActionUtil.getParameter("news_group"))) {
			List<NewsCodeVO> newsCodeList = new ArrayList<NewsCodeVO>();
			for (DictSchoolVO dictSchool : dictSchoolList) {
				NewsCodeVO newsCode = new NewsCodeVO();
				newsCode.setNews_group(dictSchool.getDict_group());
				newsCode.setNews_code(dictSchool.getDict_code());
				newsCode.setCode_name(dictSchool.getDict_value());
                Map<String,Object> map = BeanUtil.jsonToMap(dictSchool.getCss_list());
				newsCode.setCss_main_count(map.get("limit")+"");
				newsCode.setCss_value(map.get("css_value")+"");
				newsCode.setCss_code(dictSchool.getCss_code());
				newsCode.setNews_list(dictSchool.getNews_list());
				newsCodeList.add(newsCode);
			}
			return newsCodeList;
		}
		// end
		return dictSchoolList;
	}
	
	/**
	 * 根据查询条件获取新闻信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getMainPageNewsList")
	public @ResponseBody Object getMainPageNewsList(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		paramMap.put("is_main", "1");
        return newsService.getNewsList(paramMap);
	}
	
	/**
	 * 根据id获取新闻信息
	 * @param request news_id
	 * @return
	 */
	@GetCache(name="newsID",value="news_id")
	@RequestMapping(value="/getNews")
	public @ResponseBody Object getNews(HttpServletRequest request){
		Integer news_id = Integer.parseInt(request.getParameter("news_id"));
		NewsVO news = newsService.getNewsByID(news_id);
		return ResponseUtils.sendSuccess(news);
	}
	
	/**
	 * 根据查询条件获取新闻信息列表
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/getNews1",method = RequestMethod.POST)
	public @ResponseBody Object getNews1(@RequestBody NewsVO vo){
		Integer news_id = vo.getNews_id();
		NewsVO News = newsService.getNewsByID(news_id);
		return ResponseUtils.sendSuccess(News);
	}
	
	/**
	 * 根据条件删除新闻信息
	 * @param news_id
	 * @return
	 */
	@PutCache(name="newsID",value="news_id")
	@RequestMapping(value="/deleteNews")
	public @ResponseBody Object deleteNews(Integer news_id){
        newsService.deleteNews(news_id);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 根据条件修改新闻信息
	 * @param request
	 * @return
	 */
	@PutCache(name="newsID",value="news_id")
	@RequestMapping(value="/updateNews")
	public @ResponseBody Object updateNews(HttpServletRequest request){
		NewsVO vo = BeanUtil.formatToBean(NewsVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
        newsService.updateNews(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	 /**
     * app端口获取新闻信息
     * @param school_id
     * @return
     */
	@RequestMapping(value="getAPPNewsList")
	@ResultField(includes={"news_id","dict_group","title","content_text","main_pic_url","deploy_date","news_code","template_type"})
	public @ResponseBody Object getAPPNewsList(Integer school_id) {
		return ResponseUtils.sendSuccess(redisService.getNewsList(school_id));
	}
	
	/**
	 * 获取APP登录首页新闻列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getNewsListOfLogin")
	@ResultField(excludes = {"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version","description","id","news_code_list","is_active","search","school_id"})
	public @ResponseBody Object getNewsListOfLogin(DictSchoolVO vo,HttpServletRequest request){
        vo.setDict_group(StringUtil.isNotEmpty(vo.getDict_group())?vo.getDict_group():StringUtil.isNotEmpty(ActionUtil.getParameter("news_group"))?ActionUtil.getParameter("news_group"):DictConstants.DCIT_GROUP_NEWS);
		List<DictSchoolVO>  dictSchoolList = null;
		dictSchoolList = newsService.getNewsListOfLogin(vo);
		// start 如果news_group不等于空则将数据dictSchoolList转换成newsCodeList返回
		if (StringUtil.isNotEmpty(ActionUtil.getParameter("news_group"))) {
			List<NewsCodeVO> newsCodeList = new ArrayList<NewsCodeVO>();
			for (DictSchoolVO dictSchool : dictSchoolList) {
				NewsCodeVO newsCode = new NewsCodeVO();
				newsCode.setNews_group(dictSchool.getDict_group());
				newsCode.setNews_code(dictSchool.getDict_code());
				newsCode.setCode_name(dictSchool.getDict_value());
                Map<String,Object> map = BeanUtil.jsonToMap(dictSchool.getCss_list());
                newsCode.setCss_main_count(map.get("limit")+"");
				newsCode.setCss_value(map.get("css_value")+"");
				newsCode.setCss_code(dictSchool.getCss_code());
				newsCode.setNews_list(dictSchool.getNews_list());
				newsCodeList.add(newsCode);
			}
			return newsCodeList;
		}
		// end
		return dictSchoolList;
	}
}
