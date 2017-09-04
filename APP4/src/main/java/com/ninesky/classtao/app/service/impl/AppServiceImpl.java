package com.ninesky.classtao.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.app.service.AppService;
import com.ninesky.classtao.app.vo.SettingVO;
import com.ninesky.classtao.app.vo.SuggestVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;
@Service("AppServiceImpl")
public class AppServiceImpl implements AppService {
	@Autowired
	private GeneralDAO dao;
	
	/**
	 * 系统设置
	 * @param vo
	 */
	public void saveSetting(SettingVO vo){
		if(dao.updateObject("settingMap.updateSetting", vo)==0){
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("settingMap.insertSetting", vo);
		}
	}
	
	/**
	 * 获取系统设置列表
	 * @param vo
	 * @return
	 */
	public List<SettingVO> getUserSetting(SettingVO vo) {
		return dao.queryForList("settingMap.getSettingList", vo);
	}
	
	/**
	 * 添加意见
	 * @param vo
	 */
	public void addSuggest(SuggestVO vo) {
		dao.insertObject("suggestMap.insertSuggest", vo);
	}
	
	/**
	 * 获取意见列表
	 * @param vo
	 * @return
	 */
	public List<SuggestVO> getSuggestList(SuggestVO vo) {
		return dao.queryForList("suggestMap.getSuggestList", vo);
	}

}
