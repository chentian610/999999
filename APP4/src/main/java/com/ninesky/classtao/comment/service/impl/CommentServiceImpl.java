package com.ninesky.classtao.comment.service.impl;

import com.ninesky.classtao.comment.service.CommentService;
import com.ninesky.classtao.comment.vo.CommentTemplateVO;
import com.ninesky.classtao.comment.vo.CommentVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService{

	@Autowired
	private GeneralDAO dao;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisService redisService;



	@Autowired
	private JedisDAO jedisDAO;

	/**
	 * 保存评语
	 * @param vo
	 * @return
	 */
	public void addComment(CommentVO vo) {
		if(IntegerUtil.isEmpty(vo.getComment_id())){
			StudentVO student = userService.getStudentById(vo.getStudent_id());
			if(student == null) throw new BusinessException(MsgService.getMsg("NO_STUDENT"));
			vo.setClass_id(student.getClass_id());
			vo.setTeacher_name(ActionUtil.getParameter("user_name"));
			vo.setComment_date(DateUtil.formatDateToString(ActionUtil.getSysTime(),"yyyy-MM-dd"));
			vo.setHead_url(redisService.getUserHeadUrl(student.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, student.getStudent_id()));
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			vo.setComment_id(dao.insertObjectReturnID("commentMap.insertComment", vo));
		} else {
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("commentMap.updateComment", vo);
		}
	}
	/**
	 * 获取学生列表(带最近评语信息的，公评语模块教师端调用)
	 * @param vo
	 * @return
	 */
	public List<CommentVO> getStudentList(CommentVO vo){
		return dao.queryForList("commentMap.getLatestComment",vo);
	}
	/**
	 * 获取评语列表
	 * @param vo
	 * @return
	 */
	public List<CommentVO> getCommentList(CommentVO vo) {
		List<CommentVO> returnList 
			= dao.queryForList("commentMap.getCommentList", vo);
		for (CommentVO commentVO : returnList) {
			commentVO.setTeacher_name(redisService.getUserName(commentVO.getSchool_id(), DictConstants.USERTYPE_TEACHER, commentVO.getCreate_by(), 0));
		}
		//只有孩子的家长看了才设置已读
		if(DictConstants.USERTYPE_PARENT.equals(vo.getUser_type()))
			dao.updateObject("commentMap.setCommentIsRead", vo);
		return returnList;
	}
	
	/**
	 * 删除评语
	 * @param vo
	 */
	public void deleteComment(CommentVO vo){
		dao.deleteObject("commentMap.deleteComment", vo);
	}
	/**
	 * 获取评语模板
	 * @param vo
	 * @return
	 */
	public List<CommentTemplateVO> getTemplate(CommentTemplateVO vo){
		return dao.queryForList("commentTemplateMap.getCommentTemplateList", vo);
	}
	
	/**
	 * 新增评语模板
	 * @param vo
	 * @return
	 */
	public void addTemplate(CommentTemplateVO vo){
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setTemplate_id(dao.insertObjectReturnID(
				"commentTemplateMap.insertCommentTemplate", vo));
	}
	
	/**
	 * 修改评语模板
	 * @param vo
	 * @return
	 */
	public void updateTemplate(CommentTemplateVO vo){
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("commentTemplateMap.updateCommentTemplate", vo);
	}
	
	/**
	 * 删除评语模板
	 * @param vo
	 * @return
	 */
	public void deleteTemplate(CommentTemplateVO vo){
		dao.deleteObject("commentTemplateMap.deleteCommentTemplate", vo);
	}
	
	/**
	 * 获取学生未读评语数量
	 * @return
	 */
	public Integer getUnreadCount(CommentVO vo) {
		if(IntegerUtil.isEmpty(vo.getStudent_id()))
			throw new BusinessException("学生id不能为空...");
		return dao.queryObject("commentMap.getUnReadCount", vo);
	}

	/**
	 * 个人评价添加动态
	 * 1、发送者教师添加动态
	 * 2、学生家长接受动态
	 */
	public void insertDynamic(HashMap<String, String> dataMap) {
		Integer student_id = Integer.parseInt(dataMap.get("student_id"));
		Integer pkID = Integer.parseInt(dataMap.get("module_pkid"));
		String teacherKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_COMMENT, pkID, "");
		String zsetKeyTeacher = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(),null);
		String student_name = dataMap.get("student_name");
		if (StringUtil.isEmpty(student_name)) student_name = userService.getStudentById(student_id).getStudent_name();
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_COMMENT_TEACHER",dataMap.get("student_name")));
		dataMap.put("info_content", dataMap.get("info_comment"));
		dataMap.put("user_type", DictConstants.USERTYPE_TEACHER);
		jedisDAO.hsetAll(teacherKey, dataMap);
		jedisDAO.zadd(zsetKeyTeacher, ActionUtil.getSysTime().getTime(),teacherKey);

		String studentKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_COMMENT, pkID, "STUDENT_ID:"+ student_id);
		//被评价的学生动态
		String zsetKeyStu = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,student_id);
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_COMMENT_STUDENT",ActionUtil.getParameter("user_name"),dataMap.get("student_name")));
		dataMap.put("info_content", dataMap.get("info_comment"));
		dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
		jedisDAO.hsetAll(studentKey, dataMap);
		jedisDAO.zadd(zsetKeyStu, ActionUtil.getSysTime().getTime(),studentKey);
		return;
	}
	
	/**
	 * 根据CommentID获取指定个人评价信息
	 */
	@Override
	public CommentVO getCommentByID(CommentVO vo) {
		// TODO Auto-generated method stub
		CommentVO comment = dao.queryObject("commentMap.getCommentListByCommentID",vo);
		comment.setTeacher_name(redisService.getUserName(comment.getSchool_id(), DictConstants.USERTYPE_TEACHER, comment.getCreate_by(), 0));
		return comment;
	}
}
