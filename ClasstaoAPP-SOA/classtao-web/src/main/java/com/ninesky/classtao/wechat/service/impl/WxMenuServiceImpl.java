package com.ninesky.classtao.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.wechat.service.WxMenuService;
import com.ninesky.classtao.wechat.vo.WxMenuVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;

@Service("wxMenuService")
public class WxMenuServiceImpl implements WxMenuService{

	@Autowired
	private GeneralDAO dao;
	
	
	@Override
	public void addWxMenu(WxMenuVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setMenu_id(dao.insertObjectReturnID("wxMenuMap.insertWxMenu", vo));
	}

	@Override
	public List<WxMenuVO> getWxTopMenuByAccount(Integer accountId) {
		return dao.queryForList("wxMenuMap.getWxTopMenuByAccount", accountId);
	}
	
	@Override
	public List<WxMenuVO> getWxMenuByAccount(Integer accountId) {
		return dao.queryForList("wxMenuMap.getWxMenuByAccount", accountId);
	}

	@Override
	public List<WxMenuVO> getWxMenuByParentId(Integer parentId) {
		return dao.queryForList("wxMenuMap.getWxMenuByParentId", parentId);
	}

	@Override
	public void updateWxMenu(WxMenuVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxMenuMap.updateWxMenuById", vo);
	}

	@Override
	public WxMenuVO getWxMenuById(Integer menuId) {
		return dao.queryObject("wxMenuMap.getWxMenuById", menuId);
	}

	@Override
	public void deleteById(Integer menuId) {
		dao.deleteObject("wxMenuMap.deleteById", menuId);
	}

	@Override
	public void deleteByParentId(Integer parentId) {
		dao.deleteObject("wxMenuMap.deleteByParentId", parentId);
	}
	
}
