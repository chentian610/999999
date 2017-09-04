package com.ninesky.classtao.app.service;

import java.util.List;

import com.ninesky.classtao.app.vo.SettingVO;
import com.ninesky.classtao.app.vo.SuggestVO;

public interface AppService {
	/**
	 * 系统设置
	 * @param vo
	 */
	public void saveSetting(SettingVO vo);
	/**
	 * 获取系统设置列表
	 * @param vo
	 * @return
	 */
	public List<SettingVO> getUserSetting(SettingVO vo);
	/**
	 * 添加意见
	 * @param vo
	 */
	public void addSuggest(SuggestVO vo);
	/**
	 * 获取意见列表
	 * @param vo
	 * @return
	 */
	public List<SuggestVO> getSuggestList(SuggestVO vo);
}
