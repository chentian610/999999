package com.ninesky.classtao.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.wechat.service.NewsItemService;
import com.ninesky.classtao.wechat.vo.WxNewsItemVO;
import com.ninesky.framework.GeneralDAO;

@Service("newsItemService")
public class NewsItemServiceImpl implements NewsItemService{

	@Autowired
	private GeneralDAO dao;
	
	@Override
	public void addNewsItem(WxNewsItemVO vo) {
		vo.setItem_id(dao.insertObjectReturnID("wxNewsItemMap.insertNewsItem", vo));
	}

	@Override
	public void updateNewsItem(WxNewsItemVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxNewsItemMap.updateNewsItemById", vo);
	}

	@Override
	public void deleteById(Integer itemId) {
		dao.deleteObject("wxNewsItemMap.deleteById", itemId);
		
	}

	@Override
	public void deleteByTemplate(Integer templateId) {
		dao.deleteObject("wxNewsItemMap.deleteByTemplate", templateId);
		
	}

	@Override
	public List<WxNewsItemVO> getNewsItemByTemplate(Integer templateId) {
		return dao.queryForList("wxNewsItemMap.getNewsItemByTemplate", templateId);
	}

	@Override
	public WxNewsItemVO getNewsItemById(Integer itemId) {
		return dao.queryObject("wxNewsItemMap.getNewsItemById", itemId);
	}

	@Override
	public String getTemplateShowPic(Integer templateId) {
		return dao.queryObject("wxNewsItemMap.getTemplateShowPic", templateId);
	}

}
