package com.ninesky.classtao.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninesky.classtao.app.service.AppService;
import com.ninesky.classtao.app.vo.SettingVO;
import com.ninesky.classtao.app.vo.SuggestVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
@Controller
@RequestMapping(value="appAction")
public class AppController extends BaseController{
	
	@Autowired
	private AppService appService;
	
	/**
	 * 系统设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveSetting")
	public @ResponseBody Object saveSetting(HttpServletRequest request) {
		SettingVO vo = BeanUtil.formatToBean(SettingVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		appService.saveSetting(vo);
		return ResponseUtils.sendSuccess();
	}
	@RequestMapping(value="/getUserSetting")
	public @ResponseBody Object getUserSetting(HttpServletRequest request){
		SettingVO vo = BeanUtil.formatToBean(SettingVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		List<SettingVO> list = appService.getUserSetting(vo);
		return ResponseUtils.sendSuccess(list);
	}
	/**
	 * 给APP提建议
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addSuggest")
	public @ResponseBody Object addSuggest(HttpServletRequest request){
		SuggestVO vo = BeanUtil.formatToBean(SuggestVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		vo.setUser_name(ActionUtil.getParameter("user_name"));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		appService.addSuggest(vo);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 获取建议列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSuggestList")
	@ResultField(includes={"id","user_id","user_name","create_date","content"})
	public @ResponseBody Object getSuggestList(HttpServletRequest request){
		SuggestVO vo = BeanUtil.formatToBean(SuggestVO.class);
		List<SuggestVO> list = appService.getSuggestList(vo);
		return list;
	}
}
