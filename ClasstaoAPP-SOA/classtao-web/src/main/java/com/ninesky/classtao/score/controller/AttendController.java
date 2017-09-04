package com.ninesky.classtao.score.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.score.service.AttendService;
import com.ninesky.classtao.score.vo.AttendCodeVO;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@RestController
@RequestMapping("attendAction")
public class AttendController extends BaseController{

	@Autowired
	private AttendService attendService;
	
	/**
	 * 获取学校考勤项目
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAttendCodeList")
	@ResultField
	public Object getAttendCodeList(HttpServletRequest request) {
		return attendService.getAttendCodeList(request.getParameter("school_id"));
	}
	
	/**
	 * 删除学校考勤项目
	 * @param request
	 * @return
	 */
	@RequestMapping(value="deleteAttendCode")
	public Object deleteAttendCode(HttpServletRequest request){
		attendService.deleteAttendCode(request.getParameter("attend_code"));
		return ResponseUtils.sendSuccess();
	}
}
