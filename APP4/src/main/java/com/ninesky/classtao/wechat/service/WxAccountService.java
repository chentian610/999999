package com.ninesky.classtao.wechat.service;

import java.util.List;

import com.ninesky.classtao.wechat.vo.WxAccountVO;

public interface WxAccountService {
	
	public void addWxAccount(WxAccountVO vo);
	
	public WxAccountVO getAccountBySchool();
	
	public WxAccountVO getAccountBySchool(int school_id);
	
	public WxAccountVO getAccountById(Integer accoountId);
	
	public void updateWxAccount(WxAccountVO vo);
	
	public List<WxAccountVO> getAllSchoolAccount();

}
