package com.ninesky.classtao.dynamic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.dynamic.vo.DynamicVO;
import com.ninesky.classtao.dynamic.vo.NewInfoVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.common.vo.ReceiveVO;

public interface DynamicService {
	/**
	 * 更新动态信息
	 * @param redisKey 动态的key
	 * @param map 需要替换的参数
	 * @return
	 */
	public void updateDynamic(String redisKey, HashMap<String, String> map);

	
	/**
	 * 获取接收的消息列表
	 * @param vo
	 * @return
	 */
	public List<Map<String,Object>> getDynamicList(final DynamicVO vo);
	
	/**
	 * 插入动态信息
	 * @param dataMap 存放的动态数据
	 * @param receivelist 动态接收对象组
	 * @return
	 */
	public void insertDynamic(HashMap<String, String> dataMap, List<ReceiveVO> receivelist);

	/**
	 * 更新动态的未读标记
	 * @param key
	 */
	public void updateReadFlag(final String key);
	
	public void insertDynamicByStuID(final HashMap<String, String> dataMap, final Integer student_id);
	
	/**
	 * 更新Redis回复汇总数，更新动态的回复条数，更新通知的回复人数
	 * 业务模块将需要更新的字段、操作类型等封装到Map中
	 * @param notice 通知对象，有标题、学校ID、发送人用户类型、发送人ID等关键信息
	 * @param reply_content：回复内容，生成动态的时候使用
	 * @return 返回更新掉的通知回复人员数（每个人员只能算一次）
	 */
	public void updateDynamicReply(NoticeVO notice, String reply_content);

	public NewInfoVO haveNewInfo();//true表示有新数据，false表示没有新数据

	/**
	 * 插入1对1单条动态
	 * @param dataMap
	 * @param vo
	 */
	public void insertSingleDynamic(HashMap<String, String> dataMap, ReceiveVO vo);

    /**
     * 删除单人动态
     * @param dataMap
     * @param vo
     */
    public void removeSingleDynamic(HashMap<String, String> dataMap, ReceiveVO vo);

    /**
     * 删除多人动态
     * @param dataMap
     */
    public void removeDynamicList(HashMap<String, String> dataMap, List<ReceiveVO> receivelist);
}
