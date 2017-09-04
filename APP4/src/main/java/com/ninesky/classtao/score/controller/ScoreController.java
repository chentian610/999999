package com.ninesky.classtao.score.controller;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.classtao.score.vo.ScoreCountVO;
import com.ninesky.classtao.user.vo.StudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ninesky.classtao.score.service.ScoreService;
import com.ninesky.classtao.score.vo.ScoreListVO;
import com.ninesky.classtao.score.vo.ScoreReasonVO;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@RestController
@RequestMapping("scoreAction")
public class ScoreController extends BaseController{

	@Autowired
	private ScoreService scoreService;
	
	/**
	 * 获取预先设定的扣分原因
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getScoreReason")
	@ResultField(includes={"id","score_code","team_type","score_type","score_reason","description","score"})
	public @ResponseBody Object getScoreReason(HttpServletRequest request){
		ScoreReasonVO vo=BeanUtil.formatToBean(ScoreReasonVO.class);
		List<ScoreReasonVO> list=scoreService.getScoreReason(vo);
		return list;
	}
	/**
	 * 新增扣分记录
	 * @return
	 */
	@RequestMapping("/saveScore")
	@ResultField(includes={"score_id","team_type","score_type","attend_item","score_date","team_id","team_name","team_count","count","count_info"})
	public @ResponseBody Object addAttend(ScoreVO vo){
		scoreService.saveScore(vo);
		scoreService.saveDynamic(vo);
		scoreService.pushMessage(vo);
		return vo;
	}
	
	/**
	 * 删除主表一条扣分记录
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteByScoreID")
	public @ResponseBody Object deleteByScoreID(HttpServletRequest request){
		int score_id=Integer.parseInt(request.getParameter("score_id"));
		scoreService.deleteByScoreID(score_id);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取扣分汇总列表  首页列表
	 * 获取考勤项目列表，获取的是主表数据，提供教室管理、寝室管理使用
	 *  @return
	 */
	@RequestMapping("/getTeamScoreSum")
	@ResultField
	public @ResponseBody Object getAttendList(ScoreVO vo){
		vo.setApp_sql(ActionUtil.getParameter("app_sql"));
		vo.setOrder_sql(ActionUtil.getParameter("order_sql"));
		Date start_time = DateUtil.smartFormat(vo.getScore_date());
		vo.setStart_time(start_time);
		switch (vo.getSum_type()+"") {
			case DictConstants.SUM_TYPE_DAY:
				vo.setEnd_time(DateUtil.smartFormat(vo.getScore_date()+DateUtil.DATE_END));
				break;
			case DictConstants.SUM_TYPE_MONTH:
				vo.setEnd_time(DateUtil.formatDateEnd(DateUtil.getMonthLastDay(start_time)));
				break;
			case DictConstants.SUM_TYPE_YEAR:
				vo.setEnd_time(DateUtil.formatDateEnd(DateUtil.getYearLastDay(start_time)));
				break;
			default: 
				vo.setEnd_time(DateUtil.smartFormat(DateUtil.formatDate(new Date(), DateUtil.Y_M_D)+DateUtil.DATE_END));
		}
		return scoreService.getScoreList(vo);
	}
	
	/**
	 * 获取扣分明细记录  获取学生扣分明细
	 * @param
	 * @return
	 */
	@RequestMapping("/getScoreList")  
	@ResultField(includes={"list_id","user_name","score_id","student_id","student_code","student_name","score_date","score_code","score","content","count"})
	public  Object getAttendListByID(ScoreListVO vo) {
		vo.setApp_sql(ActionUtil.getParameter("app_sql"));
		vo.setOrder_sql(ActionUtil.getParameter("order_sql"));
		return scoreService.getScoreListList(vo);
	}
	
	/**
	 * 获取个人未读数
	 * @return
	 */
	@RequestMapping(value="/getUnreadCount")
	@ResultField(includes={"count"})
	public @ResponseBody Object getUnreadCount(){
		ScoreListVO vo=BeanUtil.formatToBean(ScoreListVO.class);
		return scoreService.getUnreadCount(vo);
	}
	
	/**
	 * 获取班级汇总明细列表 考勤扣分页面
	 * @return
	 */
	@RequestMapping(value="/getScoreListOfTeam")
	@ResultField(includes={"list_id","score_id","student_id","student_code","student_name","score_code","score","content","sender_id","user_name","score_date","all_letter","first_letter","team_name","attend_item","bed_code","is_leave"})
	public @ResponseBody Object getScoreListOfClass(){
		ScoreListVO vo=BeanUtil.formatToBean(ScoreListVO.class);
		return scoreService.getScoreListOfTeam(vo);
	}
	
	/**
	 * 通过score_id获取扣分信息（左连接）  考勤详细信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getScoreByID")
    @ResultField
	public @ResponseBody Object getScoreByID(HttpServletRequest request){
		return scoreService.getScoreByID(Integer.parseInt(request.getParameter("score_id")));
	}
	
	/**
	 * 通过score_id获取扣分信息   扣分详情页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getScoreListByID")
	public @ResponseBody Object getScoreListByID(HttpServletRequest request){
		return scoreService.getScoreListById(Integer.parseInt(request.getParameter("score_id")));
	}
	
	/**
	 * 增加评分项
	 * @return
	 */
	@RequestMapping(value="/addScoreReason")
	public @ResponseBody Object addScoreReason(){
		ScoreReasonVO vo=BeanUtil.formatToBean(ScoreReasonVO.class);
		return ResponseUtils.sendSuccess(scoreService.addScoreReason(vo));
		
	}
	
	/**
	 * 修改评分项
	 * @return
	 */
	@RequestMapping(value="/updateScoreReason")
	public @ResponseBody Object updateScoreReason(){
		ScoreReasonVO vo=BeanUtil.formatToBean(ScoreReasonVO.class);
		scoreService.updateScoreReason(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 删除评分项
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteScoreReason")
	public @ResponseBody Object deleteScoreReason(HttpServletRequest request){
		scoreService.deleteScoreReason(Integer.parseInt(request.getParameter("id")));
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取学生列表（带最近扣分时间）扣分扣分页面
	 * @return
	 */
	@RequestMapping(value="/getStudentList")
	@ResultField
	public Object getStudentList(){
		ScoreListVO vo=BeanUtil.formatToBean(ScoreListVO.class);
		return scoreService.getStudentlist(vo);
	}

    /**-------------------------------------------------------------------
     * 家长端：在校评分
     * @return
     */
    @RequestMapping(value = "/getScoreNoAttendByStuID")
    @ResultField
    //score_date,user_name,score,score_id
    public Object getScoreNoAttend(){
        return scoreService.getScoreNoAttend();
    }

    /**
     * 学生评分：搜索学生
     * @return
     */
    @RequestMapping(value = "/getStudentBySearch")
    @ResultField(includes = {"grade_id","class_id","student_id","student_code","student_name","class_name"})
    public Object getStudentBySearch(){
        StudentVO vo=BeanUtil.formatToBean(StudentVO.class);
        return scoreService.getStudentBySearch(vo);
    }

	/**
	 * 统计
	 */
	@RequestMapping(value="/getScoreCount")
	public Object getScoreCount(){
		ScoreCountVO vo=BeanUtil.formatToBean(ScoreCountVO.class);
		return scoreService.getScoreCount(vo);
	}

	/**
	 * 班级评分统计详情
	 */
	@RequestMapping(value = "/getScoreDetail")
	public Object getScoreDetail(){
		ScoreCountVO vo=BeanUtil.formatToBean(ScoreCountVO.class);
		return scoreService.getScoreDetail(vo);
	}
}
