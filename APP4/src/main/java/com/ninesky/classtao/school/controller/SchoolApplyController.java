package com.ninesky.classtao.school.controller;


import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.LinkManVO;
import com.ninesky.classtao.school.vo.SchoolMainVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 给客户的请求
 * @return
 */
@RestController
@RequestMapping(value="schoolApplyAction")
public class SchoolApplyController extends BaseController{

	@Autowired
	private SchoolService schoolService;
	
	
	/**
	 * 添加学校申请
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/addSchool")
	public Object addSchool(HttpServletRequest request,SchoolVO vo){
		vo.setClient_id(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		return ResponseUtils.sendSuccess(schoolService.addSchool(vo));
	}

	/**
	 * 添加百度过来的申请人
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/addLinkManFromBaidu")
	public Object addLinkManFromBaidu(HttpServletRequest request,LinkManVO vo){
        vo.setIp_address(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		vo.setCreate_by(0);
		vo.setCreate_date(ActionUtil.getSysTime());
		schoolService.addLinkManFromBaidu(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 检查域名是否已经存在
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/examineDomainName")
	public Object examineDomainName(SchoolMainVO vo){
		schoolService.examineDomainName(vo);
		return ResponseUtils.sendSuccess();
	}
}
