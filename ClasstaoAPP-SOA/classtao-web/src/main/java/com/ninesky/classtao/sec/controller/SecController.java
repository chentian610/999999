// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecController.java

package com.ninesky.classtao.sec.controller;

import com.ninesky.classtao.sec.service.SecService;
import com.ninesky.classtao.sec.vo.SecVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("secAction")
public class SecController extends BaseController
{
	@Autowired
	private SecService secService;

	@RequestMapping("/addSec")
	public Object addSec(HttpServletRequest request, SecVO vo)
	{
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setIp_address(request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr());
		secService.addSec(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	@RequestMapping("/getSecList")
	public Object getSec(HttpServletRequest request, SecVO vo)
	{
		java.util.List list = secService.getSec(vo);
		return ResponseUtils.sendSuccess(list);
	}
}
