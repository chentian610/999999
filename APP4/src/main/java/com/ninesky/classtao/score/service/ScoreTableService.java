package com.ninesky.classtao.score.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.ninesky.classtao.score.vo.DashBoardVO;
import com.ninesky.classtao.score.vo.ScoreListVO;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.classtao.system.vo.DictVO;

public interface ScoreTableService {

	public List<TableHeadVO> getTableHead(TableHeadVO vo);//获取预先设定的扣分原因
	
	/**
	 * 添加记录到Redis数据库
	 * @param vo 
	 * @param
	 */
	public void addScoreSumToRedis(List<ScoreListVO> itemlist, ScoreVO vo);
	
	/**
	 * 添加记录到Redis数据库
	 * @param vo 
	 * @param
	 * @return 
	 */
	public List<HashMap<String, Object>> getScoreCountFromRedis(TableVO vo);

	public List<HashMap<String,Object>> getScoreCountOfCustom(TableVO vo);//自定义统计

	public List<HashMap<String,Object>> getStudentLeaveCount(TableVO vo);//学生请假统计

	public List<HashMap<String,Object>> getTeacherLeaveCount(TableVO vo,List<DictVO> list);//教师请假统计

	public List<HashMap<String,Object>> getPersonScoreCount(TableVO vo);//个人评分

	public List<HashMap<String,Object>> getClassScoreCount(TableVO vo);//班级评分
	
	public void deleteScoreToRedis(List<ScoreListVO> itemlist, ScoreVO vo);//删除主表记录时，修改redis中数据
	
	public void updateScoreToRedis(List<ScoreListVO> itemlist, ScoreVO vo);//修改扣分后，修改redis中数据
	
	public void addNewRecord(List<ScoreListVO> itemlist, ScoreVO vo);//保留最新3条记录
	
	public List<LinkedHashMap<String,Object>> getDashBoardFromRedis(TableVO vo);//从redis中获取仪表盘统计信息
	
	public List<ScoreVO> unAttendTeam(ScoreVO vo);//未考勤的班级
	
	public List<DashBoardVO> getDashBoardDetails(ScoreVO vo);//获取相应仪表盘的详细信息
	
	public void initTableHead(Integer school_id);//初始tableHead
}
