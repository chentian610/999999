package com.ninesky.classtao.wechat.service;

import java.util.List;

import com.ninesky.classtao.wechat.vo.WxMenuVO;

public interface WxMenuService {
	
	public void addWxMenu(WxMenuVO vo);
	
	public List<WxMenuVO> getWxTopMenuByAccount(Integer accountId);
	
	public List<WxMenuVO> getWxMenuByAccount(Integer accountId);
	
	public List<WxMenuVO> getWxMenuByParentId(Integer parentId);
	
	public WxMenuVO getWxMenuById(Integer menuId);
	
	public void updateWxMenu(WxMenuVO vo);
	
	public void deleteById(Integer menuId);
	
	public void deleteByParentId(Integer parentId);
}
