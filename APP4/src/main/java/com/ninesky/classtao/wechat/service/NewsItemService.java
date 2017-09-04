package com.ninesky.classtao.wechat.service;

import java.util.List;

import com.ninesky.classtao.wechat.vo.WxNewsItemVO;

public interface NewsItemService {
	
	public void addNewsItem(WxNewsItemVO vo);
	
	public void updateNewsItem(WxNewsItemVO vo);
	
	public void deleteById(Integer itemId);
	
	public void deleteByTemplate(Integer templateId);
	
	public List<WxNewsItemVO> getNewsItemByTemplate(Integer templateId);
	
	public WxNewsItemVO getNewsItemById(Integer itemId);
	
	public String getTemplateShowPic(Integer templateId);
	
}
