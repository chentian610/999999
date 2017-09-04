package com.ninesky.classtao.comment.service;

import java.util.HashMap;
import java.util.List;

import com.ninesky.classtao.comment.vo.CommentTemplateVO;
import com.ninesky.classtao.comment.vo.CommentVO;

public interface CommentService {
	/**
	 * 保存评语
	 * @param vo
	 * @return
	 */
	public void addComment(CommentVO vo);
	/**
	 * 获取学生列表(带最近评语信息的，公评语模块教师端调用)
	 * @param request
	 * @return
	 */
	public List<CommentVO> getStudentList(CommentVO vo);
	/**
	 * 获取评语列表
	 * @param vo
	 * @return
	 */
	public List<CommentVO> getCommentList(CommentVO vo);
	/**
	 * 删除评语
	 * @param vo
	 */
	public void deleteComment(CommentVO vo);
	/**
	 * 获取评语模板
	 * @param vo
	 * @return
	 */
	public List<CommentTemplateVO> getTemplate(CommentTemplateVO vo);
	/**
	 * 新增评语模板
	 * @param vo
	 * @return
	 */
	public void addTemplate(CommentTemplateVO vo);
	/**
	 * 修改评语模板
	 * @param vo
	 * @return
	 */
	public void updateTemplate(CommentTemplateVO vo);
	/**
	 * 删除评语模板
	 * @param vo
	 * @return
	 */
	public void deleteTemplate(CommentTemplateVO vo);
	/**
	 * 获取学生未读评语数量
	 * @return
	 */
	public Integer getUnreadCount(CommentVO vo);//获取个人未读数
	
	public void insertDynamic(HashMap<String, String> dataMap);
	
	public CommentVO getCommentByID(CommentVO vo);
}
