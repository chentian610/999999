package com.ninesky.classtao.info.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;

public interface InfoService {
	/**
	 * 添加消息
	 * @param vo
	 */
	public void addInfo(InfoVO vo);
	/**
	 * 添加消息集合
	 * @param vo
	 * @param list
	 * @return 
	 */
	public Integer addInfo(InfoVO vo, List<InfoReceiveVO> list);
	/**
	 * 获取接收的消息列表
	 * @param vo
	 * @return
	 */
	public List<InfoReceiveVO> getInfoReceiveList(InfoReceiveVO vo);
	/**
	 * 更新动态消息
	 * @param vo
	 */
	public void updateInformation(InfoReceiveVO vo);
	/**
	 * 获取用户未读消息数量
	 * @param paramMap
	 * @return
	 */
	public Integer getUnreadCount(Map<String, String> paramMap);
	/**
	 * 获取班级当天的相册动态信息
	 * @param vo 班级、模块code，模块pkid(当天)
	 * @return
	 */
	public InfoReceiveVO getReceiveInfoByModule(InfoReceiveVO vo);
	
	/**
	 * 通过腾讯信鸽，推送消息到APP
	 * @param vo 消息推送对象
	 */
	public void xgPushToApp(InfoReceiveVO vo);
	
	/**
	 * 通过腾讯信鸽，推送消息到APP
	 * @param vo 消息推送内容对象
	 * @param accountList 接收对象列表
	 */
	public void xgPushListToApp(InfoVO vo, List<String> accountList);
	
	/**
	 * 通过个推，推送消息到APP
	 * @param vo 消息推送内容对象
	 * @param accountList 接收对象列表
	 */
	public void gtPushListToApp(InfoVO vo, List<String> accountList);
	
}
