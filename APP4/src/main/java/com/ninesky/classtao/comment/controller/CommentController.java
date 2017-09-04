package com.ninesky.classtao.comment.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.comment.service.CommentService;
import com.ninesky.classtao.comment.vo.CommentTemplateVO;
import com.ninesky.classtao.comment.vo.CommentVO;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="commentAction")
public class CommentController extends BaseController{
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private GetuiService getuiService;
	/**
	 * 保存评语
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/saveComment")
	public Object addComment(CommentVO vo){
		commentService.addComment(vo);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("module_code",DictConstants.MODULE_CODE_COMMENT);
		dataMap.put("module_pkid",vo.getComment_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("student_id", vo.getStudent_id().toString());
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("student_name", vo.getStudent_name());
		dataMap.put("info_comment", StringUtil.subString(vo.getComment(),30));
		commentService.insertDynamic(dataMap);
		//给家长发推送
		getuiService.pushMessageByStuID(dataMap,vo.getStudent_id());
		return vo;
	}
	/**
	 * 获取学生列表(带最近评语信息的，公评语模块教师端调用)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStudentList")
	@ResultField(includes={"comment_date","student_id","student_code","student_name","head_url","version"})
	public @ResponseBody Object getStudentList(HttpServletRequest request){
		CommentVO vo=BeanUtil.formatToBean(CommentVO.class);
		List<CommentVO> list = commentService.getStudentList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	/**
	 * 获取评语列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCommentList")
	public @ResponseBody Object getComment(HttpServletRequest request){
		CommentVO vo=BeanUtil.formatToBean(CommentVO.class);
		List<CommentVO> list = commentService.getCommentList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 删除评语
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteComment")
	public @ResponseBody Object deleteComment(HttpServletRequest request){
		CommentVO vo = BeanUtil.formatToBean(CommentVO.class);
		commentService.deleteComment(vo);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 获取评语模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTemplate")
	public @ResponseBody Object getTemplate(HttpServletRequest request){
		CommentTemplateVO vo = BeanUtil.formatToBean(CommentTemplateVO.class);
		List<CommentTemplateVO> list = commentService.getTemplate(vo);
		return ResponseUtils.sendSuccess(list);
	}
	/**
	 * 新增评语模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addTemplate")
	public @ResponseBody Object addTemplate(HttpServletRequest request){
		CommentTemplateVO vo = BeanUtil.formatToBean(CommentTemplateVO.class);
		commentService.addTemplate(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	/**
	 * 修改评语模板
	 * @param requset
	 * @return
	 */
	@RequestMapping(value="/updateTemplate")
	public @ResponseBody Object upateTemplate(HttpServletRequest requset){
		CommentTemplateVO vo = BeanUtil.formatToBean(CommentTemplateVO.class);
		commentService.updateTemplate(vo);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 删除评语模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteTemplate")
	public @ResponseBody Object deleteTemplate(HttpServletRequest request){
		CommentTemplateVO vo = BeanUtil.formatToBean(CommentTemplateVO.class);
		commentService.deleteTemplate(vo);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 获取个人未读数(学生未读的评语条数)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUnreadCount")
	public @ResponseBody Object getUnreadCount(HttpServletRequest request){
		CommentVO vo = BeanUtil.formatToBean(CommentVO.class);
		return ResponseUtils.sendSuccess(commentService.getUnreadCount(vo));
	}
	
	/**
	 * 获取个人未读数(学生未读的评语条数)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCommentByID")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","update_by","update_date"})
	public @ResponseBody Object getCommentByID(HttpServletRequest request){
		CommentVO vo = BeanUtil.formatToBean(CommentVO.class);
		return commentService.getCommentByID(vo);
	}
//	/**
//	 * 教师获取评语列表
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value="/getCommentOfClass")
//	public @ResponseBody Object getCommentOfClass(HttpServletRequest request){
//		CommentVO vo=BeanUtil.formatToBean(CommentVO.class);
//		List<CommentVO> list=commentService.getCommentOfClass(vo);
//		return ResponseUtils.sendSuccess(list);
//	}
}
