package com.ninesky.classtao.score.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.score.vo.*;
import com.ninesky.classtao.user.vo.StudentVO;

public interface ScoreService {

	public List<ScoreReasonVO> getScoreReason(ScoreReasonVO vo);//获取预先设定的扣分原因
	
	public ScoreVO saveScore(ScoreVO vo);//新增扣分记录
	
	public ScoreVO updateScore(ScoreVO vo);//修改考勤
	
	public List<ScoreVO> getScoreList(ScoreVO vo);//获取扣分汇总列表
	
	public List<TaskVO> getScoreListTeacher(TaskVO vo);//获取有考勤的教师信息
	
	public List<TaskVO> getScoreListSchoolLeader(TaskVO vo);//获取学校管理层教师
	
	public List<ScoreListVO> getScoreListList(ScoreListVO vo);//获取扣分明细记录
	
	public Integer getUnreadCount(ScoreListVO vo);//获取个人未读数
	
	public List<ScoreListVO> getScoreListOfTeam(ScoreListVO vo);//获取班级汇总明细列表
	
	public List<ScoreListVO> getScoreByID(Integer score_id);//通过score_id获取扣分信息（左连接）
	
	public ScoreDetailVO getScoreListById(Integer score_id);//通过score_id获取扣分信息
	
	public Integer addScoreReason(ScoreReasonVO vo);//添加扣分项
	
	public void updateScoreReason(ScoreReasonVO vo);//修改扣分项
	
	public void deleteScoreReason(Integer id);//删除扣分项
	
	public void deleteByScoreID(Integer score_id);//删除主表一条记录
	
	public List<ScoreListVO> getStudentlist(ScoreListVO vo);//获取学生列表（带最近打分时间）

	/**
	 * 保存动态消息到Redis数据库
	 * @param vo
	 * @author chenth
	 */
	public void saveDynamic(ScoreVO vo);

	public void pushMessage(ScoreVO vo);

    public List<ScoreListVO> getScoreNoAttend();//获取学生扣分信息（除了考勤）

    public List<StudentVO> getStudentBySearch(StudentVO vo);//学生评分：搜索学生

	public Map<String,Object> getScoreCount(ScoreCountVO vo);//统计

	public Map<String,Object> getScoreDetail(ScoreCountVO vo);//班级评分统计详情
}
