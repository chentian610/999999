package com.ninesky.classtao.score.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.score.service.ScoreTableService;
import com.ninesky.classtao.score.vo.DashBoardVO;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResultField;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("scoreTableAction")
public class ScoreTableController extends BaseController{

	@Autowired
	private ScoreTableService scoreTableService;
	
	/**
	 * 获取预先设定的扣分原因
	 * @param
	 * @return
	 */
	@RequestMapping("getTableHead")
	@ResultField(includes={"field","field_name","field_func","field_func_sum","func_type"})
	public List<TableHeadVO> getTableHead(TableHeadVO vo){
		List<TableHeadVO> list=scoreTableService.getTableHead(vo);
		return list;
	}
	
	@RequestMapping("getScoreCount")
	public List<HashMap<String,Object>> getScoreCount(TableVO vo){
		return scoreTableService.getScoreCountFromRedis(vo);
	}
	
	/**
	 * 获取仪表盘统计信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("getDashBoardInfo")
	public List<LinkedHashMap<String,Object>> getDashBoardInfo(TableVO vo){
		return scoreTableService.getDashBoardFromRedis(vo);
	}
	
	/**
	 * 未考勤的班级
	 * @param vo
	 * @return
	 */
	@RequestMapping("getUnAttendTeam")
	@ResultField(includes={"team_name"})
	public List<ScoreVO> getUnCompleteClass(ScoreVO vo){
		return scoreTableService.unAttendTeam(vo);
	}
	
	/**
	 * 获取仪表盘详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("getDashBoardDetails")
	@ResultField(includes={"name","info"})
	public List<DashBoardVO> getDashBoardDetails(ScoreVO vo){
		return scoreTableService.getDashBoardDetails(vo);
	}
	
	/**
	 * 初始化统计表头
	 */
	@RequestMapping("initTableHead")
	public void initTableHead(HttpServletRequest request){
		scoreTableService.initTableHead(Integer.parseInt(request.getParameter("school_id").trim()));
	}
}
