package com.ninesky.classtao.homework.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.homework.vo.HomeworkFileVO;
import com.ninesky.classtao.homework.vo.HomeworkReceiveVO;
import com.ninesky.classtao.homework.vo.HomeworkVO;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;

public interface HomeworkService {
	
	/**
	 * 添加作业
	 * @param vo
	 */
	public HomeworkVO addHomework(HomeworkVO vo);
//	/**
//	 * 获取作业列表
//	 * @param paramMap
//	 * @return
//	 */
//	public List<?> getHomeworkList(Map<String, String> paramMap);
	
	/**
	 * 根据作业id获取指定作业
	 * @param paramMap
	 * @return
	 */
	public List<?> getHomeworkById(Map<String, String> paramMap);
	
	/**
	 * 获取作业附件列表
	 * @param vo
	 * @return
	 */
	public List<HomeworkFileVO> getFileList(HomeworkFileVO vo);
	/**
	 * 设置子条目完成标记
	 * @param vo
	 */
	public void updateItemDone(HomeworkReceiveVO vo) throws ParseException;
	/**
	 * 提交作业
	 * @param vo
	 */
//	public void updateHomeworkDone(HomeworkReceiveVO vo);
	/**
	 * 获取作业完成情况
	 * @param vo : homeworkId
	 * @return
	 */
	public List<HomeworkReceiveVO> getHomeworkDoneList(Map<String, String> paramMap);
	
	/**
	 * 获取作业完成情况
	 * @param vo : homeworkId
	 * @return
	 */
	public HomeworkReceiveVO getHomeworkDoneListByID(Map<String, String> paramMap);

	/**
	 * 获取未读数量
	 * @param userId
	 * @return
	 */
	public Integer getUnreadCount(Map<String, String> paramMap);
	
	/**
	 * 作业未完成提醒
	 * @param vo
	 */
	public void addRemind(HomeworkReceiveVO vo);
	
	/**
	 * 获取
	 * @param vo
	 */
	public List<?> getHomeworkGroupList(Map<String, String> paramMap);
	
	public List<HashMap<String,Object>> getHomeworkCountFromRedis(TableVO vo);
	
	public List<TableHeadVO> getHomeworkTableHead(TableHeadVO vo);
	
	public List<?> getItemList(Map<String, String> paramMap);
}
