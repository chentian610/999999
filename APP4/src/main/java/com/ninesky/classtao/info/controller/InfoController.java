package com.ninesky.classtao.info.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.dynamic.vo.DynamicVO;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="infoAction")
public class InfoController extends BaseController{
	
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private DynamicService dynamicService;
	/**
	 * 获取消息，一般在界面打开的时候，可以获取单条或者多条消息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/getInformation")
	public Object getInformation(DynamicVO vo){
		//如果选择具体模块或者未读标记的，那么就不能按照排序获取了，只能先获取全部，然后遍历结果
//		if (vo.isUn_read() || StringUtil.isNotEmpty(vo.getModule_code())) vo.setLimit(-1);
		 if (IntegerUtil.isEmpty(vo.getLimit())) vo.setLimit(Constants.DEFAULT_LIMIT);
		List<Map<String, Object>> list = dynamicService.getDynamicList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	/**
	 * 添加动态消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addInformation")
	public Object addInformation(HttpServletRequest request){
		InfoVO infoVO = BeanUtil.formatToBean(InfoVO.class);
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoVO.setInfo_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		infoService.addInfo(infoVO);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 更新动态消息
	 */
	@RequestMapping(value="/updateInformation")
	public @ResponseBody Object updateInformation(HttpServletRequest request){
		InfoReceiveVO vo = BeanUtil.formatToBean(InfoReceiveVO.class);
		infoService.updateInformation(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取用户未读消息数量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUnreadCount")
	public @ResponseBody Object getUnreadCount(HttpServletRequest request){
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		Integer count = infoService.getUnreadCount(paramMap);
		return ResponseUtils.sendSuccess(count);
	}
}
