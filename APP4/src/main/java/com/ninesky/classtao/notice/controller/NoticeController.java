package com.ninesky.classtao.notice.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.notice.service.NoticeService;
import com.ninesky.classtao.notice.vo.NoticeFileVO;
import com.ninesky.classtao.notice.vo.NoticeReplyVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@RestController
@RequestMapping(value="noticeAction")
public class NoticeController extends BaseController{
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private DynamicService dynamicService;
	
	@Autowired
	private GetuiService getuiService;
	
	@Autowired
	private RedisService redisService;
	
	/**
	 * 获取通知列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getNoticeList")
	@ResultField
	public Object getNoticeList(HttpServletRequest request){
		NoticeVO vo=BeanUtil.formatToBean(NoticeVO.class);
		return noticeService.getNoticeList(vo);
	}
	
	/**
	 * 获取通知附件
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getNoticeFileByID")
	public @ResponseBody Object getNoticeFileByID(HttpServletRequest request){
		NoticeFileVO vo=BeanUtil.formatToBean(NoticeFileVO.class);
		List<NoticeFileVO> list=noticeService.getNoticeFileByID(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	@RequestMapping(value="/replyNotice")
	@ResultField
	public @ResponseBody Object replyNotice(){
		NoticeReplyVO vo=BeanUtil.formatToBean(NoticeReplyVO.class);
		vo=noticeService.addReplyNotice(vo);
		//更新动态
//		noticeService.updateInfoByReply(vo);//更新通知的统计信息(----------)
//		noticeService.addInfoOfReply(vo);//添加回复通知动态
		NoticeVO notice = noticeService.getNoticeByID(vo.getNotice_id());
		dynamicService.updateDynamicReply(notice,vo.getReply_content());
		return vo;
	}
	
	@RequestMapping(value="/addNotice")
	@ResultField
	public Object addNotice(HttpServletRequest request,NoticeVO vo){
		noticeService.addNotice(vo);//添加一条发送的通知到notice表
		String receive_list=request.getParameter("receive_list");//存放一组接收者的信息
		noticeService.addNoticeGroup(vo, receive_list);//添加接收群体到notice_group表
		String file_list=request.getParameter("file_list");//存放一组附件的信息
		if(StringUtil.isNotEmpty(file_list)) {
		List<NoticeFileVO> filelist=BeanUtil.jsonToList(file_list, NoticeFileVO.class);
		for(NoticeFileVO fvo:filelist){//遍历集合，添加数据
			fvo.setNotice_id(vo.getNotice_id());
			fvo.setCreate_by(ActionUtil.getUserID());
			fvo.setCreate_date(ActionUtil.getSysTime());
		}
		noticeService.addFile(filelist);//新增附件
		}
		//存放一组接收者的信息
		List<ReceiveVO> receivelist=BeanUtil.removeDuplicate(BeanUtil.jsonToList(receive_list, ReceiveVO.class));
		noticeService.addNoticeCount(vo,receivelist);//添加通知总人数
		//加到redis中,用于未读数，已读数，回复数的获取
		redisService.addNoticeToRedis(vo, receivelist);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("info_title",StringUtil.subString(vo.getNotice_title(),15));
		dataMap.put("info_content",StringUtil.subString(vo.getNotice_content(),20));
		dataMap.put("module_code",vo.getModule_code());
		dataMap.put("module_pkid",vo.getNotice_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", DictConstants.MODULE_CODE_NOTICE.equals(vo.getModule_code())?DictConstants.USERTYPE_ALL:DictConstants.USERTYPE_TEACHER);
		dynamicService.insertDynamic(dataMap,receivelist);
		getuiService.pushMessage(dataMap,receivelist);
		return vo;
	}
	
	@RequestMapping(value="addSchoolNotice")
	@ResultField
	public Object addSchoolNotice(HttpServletRequest request,NoticeVO vo){
		noticeService.addNotice(vo);//添加一条发送的通知到notice表
		String receive_list=request.getParameter("receive_list");//存放一组接收者的信息,多个user_id
		String teacher_duty=request.getParameter("teacher_duty");//发给某种身份的所有老师
        if (StringUtil.isNotEmpty(teacher_duty)) {//选择职务发送
            noticeService.addSchoolNoticeByDuty(teacher_duty, vo);
        }
        if (StringUtil.isNotEmpty(receive_list)){//user_id.逗号隔开
            noticeService.addSchoolNoticeToReceive(receive_list,vo,teacher_duty);//添加多条接收的信息到notice_receive表
            //加到redis中,用于未读数，已读数，回复数的获取
            redisService.addSchoolNoticeToRedis(vo, receive_list, null);
        }
        noticeService.addSchoolNoticeCount(vo,receive_list,teacher_duty);//添加通知人数
        String file_list=request.getParameter("file_list");//存放一组附件的信息
		if(StringUtil.isNotEmpty(file_list)) {
			List<NoticeFileVO> filelist=BeanUtil.jsonToList(file_list, NoticeFileVO.class);
			for(NoticeFileVO fvo:filelist){//遍历集合，添加数据
				fvo.setNotice_id(vo.getNotice_id());
				fvo.setCreate_by(ActionUtil.getUserID());
				fvo.setCreate_date(ActionUtil.getSysTime());
			}
			noticeService.addFile(filelist);//新增附件
			}
		noticeService.dynamicAndPush(vo, receive_list, teacher_duty);//动态和消息推送
		return vo;
	}
	
	/**
	 * 获取通知接收情况
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getNoticeReceiveByID")
	public  Object getNoticeReceiveByID(){
		NoticeVO vo=BeanUtil.formatToBean(NoticeVO.class);
		return redisService.getNoticeReceiveFromRedis(vo);
	}
	
	/**
	 * 获取个人未读数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUnreadCount")
	public  Object getUnreadCount(HttpServletRequest request){
		return redisService.getUnReadCountFromRedis(request.getParameter("module_code"));
	}
	
	/**
	 * 获取回复记录列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getNoticeReplyList")
	@ResultField
	public Object getNoticeReplyList(HttpServletRequest request){
		NoticeReplyVO replyVO=BeanUtil.formatToBean(NoticeReplyVO.class);
		List<NoticeReplyVO> list=noticeService.getReplyList(replyVO);
		return list;
	}
	
	/**
	 * 某条通知的未读用户列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUnreadUserList")
	@ResultField(includes={"user_id","student_id","receive_name","head_url"})
	public Object getUnreadUserList(HttpServletRequest request){
		int notice_id=Integer.parseInt(request.getParameter("notice_id"));
		return noticeService.getUnreadUserList(notice_id);
	}
	
	/**
	 * 获取某条通知
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getNoticeById")
	@ResultField
	public @ResponseBody Object getNoticeById(HttpServletRequest request){
		return noticeService.getNoticeById(Integer.parseInt(request.getParameter("notice_id")));
	}

	/**
	 * 给未读用户发送提醒
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sendRemind")
	public Object sendRemind(HttpServletRequest request) {
		NoticeVO vo=BeanUtil.formatToBean(NoticeVO.class);
		noticeService.sendRemind(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 确认通知
	 * @param notice_id
	 * @return
	 */
	@RequestMapping(value = "/confirmNotice")
	public Object confirmNotice(Integer notice_id) {
		noticeService.confirmNotice(notice_id);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 获取已读列表
	 * @param notice_id
	 * @return
	 */
	@RequestMapping(value = "/getReadList")
	@ResultField
	public Object getReadList(Integer notice_id){
		return noticeService.getReadList(notice_id);
	}
}
