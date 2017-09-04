package com.ninesky.classtao.notice.service;

import java.util.HashMap;
import java.util.List;

import com.ninesky.classtao.notice.vo.NoticeCountVO;
import com.ninesky.classtao.notice.vo.NoticeFileVO;
import com.ninesky.classtao.notice.vo.NoticeReceiveVO;
import com.ninesky.classtao.notice.vo.NoticeReplyVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.common.vo.ReceiveVO;

public interface NoticeService {
	
	public List<NoticeVO> getNoticeList(NoticeVO vo);//获取通知列表

	public List<NoticeFileVO> getNoticeFileByID(NoticeFileVO vo);// 获取通知附件
	
	public NoticeReplyVO addReplyNotice(NoticeReplyVO vo);

	public NoticeVO addNotice(NoticeVO vo);// 新增通知
	
	public void addNoticeGroup(NoticeVO vo, String receive_list);//添加接收群体到notice_group表
	
	public NoticeVO addSchoolNoticeToReceive(String receive_list, NoticeVO vo,String teacher_duty);//校务通知，给一组人
	
	public NoticeVO addSchoolNoticeByDuty(String duty, NoticeVO vo);//校务通知，给某种身份的所有老师
	
	public NoticeVO addSchoolNoticeToAll(NoticeVO vo);//校务通知，全部教师
	
	public void dynamicAndPush(NoticeVO vo, String receive_list, String teacher_duty);//校务通知的动态和推送
	
	public List<NoticeFileVO> addFile(List<NoticeFileVO> list);// 添加附件

	public List<NoticeCountVO>  getNoticeReceiveByIDOld(Integer notice_id);// 获取通知接收情况
	
	public Integer setRead(NoticeReceiveVO vo);//将未读通知设为已读通知
	
	public void updateInfo(NoticeReceiveVO vo);//更新相应动态
	
	public void updateInfoByReply(NoticeReplyVO vo);//回复通知后，更新相应动态
	
	public List<NoticeReceiveVO> getUserListOfUnread(Integer notice_id);//未确认某条通知的用户列表
	
	public List<NoticeReplyVO> getReplyList(NoticeReplyVO vo);//获取回复记录列表（除了发送者的）
	
	public Integer addInfoOfReply(NoticeReplyVO vo);//回复通知后，添加动态
	
	public NoticeVO getNoticeByID(Integer notice_id);// 获取通知列表
	
	public NoticeVO getNoticeById(Integer notice_id);//获取通知列表

	public Integer addNoticeCount(NoticeVO vo, List<ReceiveVO> list);//添加通知人数

	public Integer addSchoolNoticeCount(NoticeVO vo,String receivelist,String teacherduty);//添加校务通知人数

	public void sendRemind(NoticeVO vo);//向未读用户发送提醒

	public List<NoticeReceiveVO> getUnreadUserList(Integer notice_id) ;//未读用户列表（100条内）

	public void confirmNotice(Integer notice_id);//确认通知

	public List<NoticeReplyVO> getReadList(Integer notice_id);//获取已读列表，含回复
}