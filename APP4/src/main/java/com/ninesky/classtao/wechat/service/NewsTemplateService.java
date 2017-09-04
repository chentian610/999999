package com.ninesky.classtao.wechat.service;

import java.util.List;

import com.ninesky.classtao.wechat.vo.NewsTemplateVO;

public interface NewsTemplateService {
	
	public void addNewsTemplate(NewsTemplateVO vo);
	
	public void updateNewsTemplate(NewsTemplateVO vo);
	
	public void updateUploadStatus(Integer templateId, int status);
	
	public void updateUploadStatus(Integer templateId, int status, String messageId);
	
	public void deleteById(Integer templateId);
	
	public List<NewsTemplateVO> getNewsTemplateByAccount(Integer accountId);
	
	public List<NewsTemplateVO> getAllUploadTemplate(Integer accountId);
	
	public NewsTemplateVO getNewsTemplateById(Integer templateId);

}
