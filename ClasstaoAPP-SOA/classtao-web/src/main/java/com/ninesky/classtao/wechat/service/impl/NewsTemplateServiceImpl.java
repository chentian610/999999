package com.ninesky.classtao.wechat.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.wechat.service.NewsTemplateService;
import com.ninesky.classtao.wechat.vo.NewsTemplateVO;
import com.ninesky.framework.GeneralDAO;

@Service("newsTemplateService")
public class NewsTemplateServiceImpl implements NewsTemplateService {

	@Autowired
	private GeneralDAO dao;
	
	@Override
	public void addNewsTemplate(NewsTemplateVO vo) {
		vo.setTemplate_id(dao.insertObjectReturnID("wxNewsTemplateMap.insertNewsTemplate", vo));
	}

	@Override
	public void updateNewsTemplate(NewsTemplateVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxNewsTemplateMap.updateNewsTemplateById", vo);
	}

	@Override
	public void deleteById(Integer templateId) {
		dao.deleteObject("wxNewsTemplateMap.deleteById", templateId);
	}

	@Override
	public List<NewsTemplateVO> getNewsTemplateByAccount(Integer accountId) {
		return dao.queryForList("wxNewsTemplateMap.getNewsTemplateByAccount", accountId);
	}

	@Override
	public NewsTemplateVO getNewsTemplateById(Integer templateId) {
		return dao.queryObject("wxNewsTemplateMap.getNewsTemplateById", templateId);
	}

	@Override
	public List<NewsTemplateVO> getAllUploadTemplate(Integer accountId) {
		return dao.queryForList("wxNewsTemplateMap.getAllUploadTemplate", accountId);
	}

	@Override
	public void updateUploadStatus(Integer templateId, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		params.put("status", status);
		params.put("updateDate", new Date());
		dao.updateObject("wxNewsTemplateMap.updateUploadStatus", params);
	}

	@Override
	public void updateUploadStatus(Integer templateId, int status,
			String messageId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		params.put("status", status);
		params.put("messageId", messageId);
		params.put("updateDate", new Date());
		dao.updateObject("wxNewsTemplateMap.updateUploadStatus2", params);
		
	}

}
