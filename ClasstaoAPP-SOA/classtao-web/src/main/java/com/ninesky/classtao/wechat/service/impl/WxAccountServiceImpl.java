package com.ninesky.classtao.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;

@Service("wxAccountService")
public class WxAccountServiceImpl implements WxAccountService{

	@Autowired
	private GeneralDAO dao;
	
	@Override
	public void addWxAccount(WxAccountVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setAccount_id(dao.insertObjectReturnID("wxAccountMap.insertWxAccount", vo));
	}

	@Override
	public WxAccountVO getAccountBySchool() {
		Integer school_id = ActionUtil.getSchoolID();
		return dao.queryObject("wxAccountMap.getWxAccountBySchool", school_id);
	}
	
	@Override
	public WxAccountVO getAccountBySchool(int school_id) {
		return dao.queryObject("wxAccountMap.getWxAccountBySchool", school_id);
	}

	@Override
	public void updateWxAccount(WxAccountVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxAccountMap.updateWxAccountById", vo);
	}

	@Override
	public WxAccountVO getAccountById(Integer accoountId) {
		return dao.queryObject("wxAccountMap.getWxAccountById", accoountId);
	}

	@Override
	public List<WxAccountVO> getAllSchoolAccount() {
		return dao.queryForList("wxAccountMap.getAllSchoolAccount");
	}
}
