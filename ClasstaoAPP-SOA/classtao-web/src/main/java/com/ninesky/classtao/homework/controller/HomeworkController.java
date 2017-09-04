package com.ninesky.classtao.homework.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.homework.service.HomeworkService;
import com.ninesky.classtao.homework.vo.HomeworkFileVO;
import com.ninesky.classtao.homework.vo.HomeworkReceiveVO;
import com.ninesky.classtao.homework.vo.HomeworkVO;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import com.ninesky.common.util.StringUtil;
@RestController
@RequestMapping(value="homeworkAction")
public class HomeworkController extends BaseController{
	
	@Autowired
	private HomeworkService homeworkService;
	
	
	@Autowired
	private DynamicService dynamicService;
	
	
	@Autowired
	private GetuiService getuiService;
	
	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 教师布置新增作业
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addHomework")
	@ResultField(includes={"homework_id","course","title","content","end_date","send_time","count_list"})
	public @ResponseBody Object addHomework(HttpServletRequest request){
		HomeworkVO vo =  BeanUtil.formatToBean(HomeworkVO.class);
		vo.setSender_id(ActionUtil.getUserID());
		vo.setSender_name(ActionUtil.getParameter("user_name"));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		homeworkService.addHomework(vo);
		//存放一组接收者的信息
		List<ReceiveVO> receivelist=BeanUtil.jsonToList(request.getParameter("receive_list"), ReceiveVO.class);
		List<ReceiveVO> receive = BeanUtil.removeDuplicate(receivelist);
		for (ReceiveVO Rvo: receive) {
			Rvo.setSchool_id(ActionUtil.getSchoolID());
			Rvo.setStudent_id(0);
		}
		//添加一条发给老师自己的动态
		receive.add(new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID()));
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("info_title",vo.getTitle());
		dataMap.put("info_content",StringUtil.subString(vo.getContent(),20));
		dataMap.put("module_code",DictConstants.MODULE_CODE_HOMEWORK);
		dataMap.put("module_pkid",vo.getHomework_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("user_type", DictConstants.USERTYPE_ALL);
		dataMap.put("user_id", ActionUtil.getUserID().toString());
		dataMap.put("student_id","0");
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dynamicService.insertDynamic(dataMap,receive);
		getuiService.pushMessage(dataMap,receive);
		return vo;
	}
	
	/**
	 * 获取作业列表
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/getHomeworkList")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","update_by","update_date","version"})
	public Object getHomeworkList(HttpServletRequest request) throws ParseException {
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		if (StringUtil.isEmpty(paramMap.get("homework_id"))) {
			List<?> list = homeworkService.getHomeworkGroupList(paramMap);
			return ResponseUtils.sendSuccess(list);
		} else {
			List<?> list = homeworkService.getHomeworkById(paramMap);//获取指定作业
			return list;
		}
	}	
	/**
	 * 获取作业附件列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getFileList")
	@ResultField(includes={"file_name","file_url","file_resize_url","play_time"})
	public @ResponseBody Object getFileList(HttpServletRequest request){
		HomeworkFileVO vo = BeanUtil.formatToBean(HomeworkFileVO.class);
		List<HomeworkFileVO> list = homeworkService.getFileList(vo);
		return list;
	}
	/**
	 * 获取作业子项列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getItemList")
	@ResultField(includes={"item_id","homework_id","title","content","file_list","is_submit","is_done","sender_name","course","send_time","end_date","create_date"})
	public @ResponseBody Object getItemList(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return homeworkService.getItemList(paramMap);
	}
	/**
	 * 获取作业完成情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getDoneList")
	@ResultField(includes= {"head_url","item_list","student_id","school_id", "student_name"})
	public @ResponseBody Object getDoneList(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		List<HomeworkReceiveVO> list = new ArrayList<HomeworkReceiveVO>();
		if (StringUtil.isEmpty(paramMap.get("student_id"))) list.addAll(homeworkService.getHomeworkDoneList(paramMap));
		else list.add(homeworkService.getHomeworkDoneListByID(paramMap));
		return ResponseUtils.sendSuccess(list);	
	}
	/**
	 * 设置子条目完成标记
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/updateItemDone")
	public @ResponseBody Object updateItemDone(HttpServletRequest request) throws ParseException{
		HomeworkReceiveVO vo = BeanUtil.formatToBean(HomeworkReceiveVO.class);
		homeworkService.updateItemDone(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	/**
	 * 获取个人未读数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUnreadCount")
	@ResultField(includes={"count"})
	public @ResponseBody Object getUnreadCount(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		Integer count = homeworkService.getUnreadCount(paramMap);
		return count;
	}
	/**
	 * 作业未完成提醒
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/addRemind")
	public @ResponseBody Object addRemind(HomeworkReceiveVO vo){
//		HomeworkReceiveVO vo = BeanUtil.formatToBean(HomeworkReceiveVO.class);
//		homeworkService.addRemind(vo);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("info_title",MsgService.getMsg("remindHomework", vo.getStudent_name(),schoolService.getSchoolById(ActionUtil.getSchoolID()).getSchool_name()));
		dataMap.put("info_content","");
		dataMap.put("module_code",DictConstants.MODULE_CODE_HOMEWORK);
		dataMap.put("module_pkid",vo.getHomework_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_OTHER);
		dataMap.put("other_flag", "Remind");
		dataMap.put("info_url", "detail.html");
		dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
		dynamicService.insertDynamicByStuID(dataMap,vo.getStudent_id());
		getuiService.pushMessageByStuID(dataMap,vo.getStudent_id());
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 获取统计数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getHomeworkCountFromRedis")
	public @ResponseBody Object getHomeworkCountFromRedis(HttpServletRequest request){
		TableVO vo = BeanUtil.formatToBean(TableVO.class);
		return ResponseUtils.sendSuccess(homeworkService.getHomeworkCountFromRedis(vo));
	}
	
	/**
	 * 获取表头
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getHomeworkTableHead")
	public @ResponseBody Object getTableHead(HttpServletRequest request){
		TableHeadVO vo = BeanUtil.formatToBean(TableHeadVO.class);
		return ResponseUtils.sendSuccess(homeworkService.getHomeworkTableHead(vo));
	}
}
