package com.ninesky.classtao.news.service;

import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.system.vo.DictVO;

import java.util.List;
import java.util.Map;

public interface NewsService {
	/**
	 * 添加校园风采
	 * @param vo
	 */
	public void addNews(NewsVO vo);
	
	/**
	 * 获取校园风采列表
	 * @param paramMap
	 * @return
	 */
	public List<NewsVO> getNewsList(Map<String, String> paramMap);
	
	/**
	 * 获取指定校园风采
	 * @param paramMap
	 * @return
	 */
	public NewsVO getNewsByID(Integer news_id);
	
	/**
	 * 删除指定校园风采
	 * @param news_id
	 */
	public void deleteNews(Integer news_id);
	
	/**
	 * 更新校园风采
	 * @param vo
	 */
	public void updateNews(NewsVO vo);
	
	/**
	 * 添加动态
	 * @param vo
	 */
	public void addInformation(NewsVO vo);
	/**
	 * 获取app登录首页新闻列表(全部新闻模块)
	 * @param vo
	 */
	public List<DictSchoolVO> getNewsListOfLogin(DictSchoolVO vo);
	
	/**
	 * 获取app登录后首页新闻列表(全部新闻模块)
	 * @param vo
	 */
	public List<DictSchoolVO> getNewsListForAPP(DictSchoolVO vo);

	/**
	 *添加学校新闻父栏目列表
	 */
	public void addSchoolDictGroupList(Integer school_id);
}
