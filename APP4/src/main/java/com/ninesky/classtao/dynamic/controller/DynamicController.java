package com.ninesky.classtao.dynamic.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.dynamic.vo.DynamicVO;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="dynamicAction")
public class DynamicController extends BaseController{
	
	@Autowired
	private DynamicService dynamicService;
	/**
	 * 获取消息，一般在界面打开的时候，可以获取单条或者多条消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getDynamicList")
	public Object getDynamicrmation(HttpServletRequest request){
		DynamicVO vo = BeanUtil.formatToBean(DynamicVO.class);
		List<Map<String,Object>> list = dynamicService.getDynamicList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	
	/**
	 * 获取消息，一般在界面打开的时候，可以获取单条或者多条消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/insertDynamic")
	public Object insertDynamic(HttpServletRequest request){
		DynamicVO vo = BeanUtil.formatToBean(DynamicVO.class);
//		dynamicService.insertDynamic(null);
		return ResponseUtils.sendSuccess(null);
	}
	
	
	/**
	 * 更新动态的已读状态
	 * @param
	 * @return
	 */
	@RequestMapping(value="/updateReadFlag")
	public Object updateReadFlag(DynamicVO vo){
		if (StringUtil.isEmpty(vo.getStart_key())) 
			return false;
		dynamicService.updateReadFlag(vo.getStart_key());
		return true;
	}

	/**
	 * 判断是否有新的动态(小红点)
	 * @return
	 */
	@RequestMapping(value="/haveNewInfo")
	public Object haveNewInfo(){
		return dynamicService.haveNewInfo();
	}
}
