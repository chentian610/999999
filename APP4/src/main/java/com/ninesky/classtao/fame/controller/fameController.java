package com.ninesky.classtao.fame.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.fame.service.fameService;
import com.ninesky.classtao.fame.vo.FameVO;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.framework.ResponseUtils;

@RestController
@RequestMapping(value = "fameAction")
public class fameController extends BaseController {

	@Autowired
	private fameService fameService;

	/**
	 * 新增名人信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addFame")
	@PutCache(name="FameList",value="school_id")
	public Object addFame(HttpServletRequest request) {
		FameVO vo = BeanUtil.formatToBean(FameVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		//System.out.println("添加接口通过......");	
		return fameService.addFame(vo);
	}


	/**
	 * 获取指定名人信息
	 * @param request ：fame_id
	 * @return
	 */
	@RequestMapping(value="/getFameList")
	@GetCache(name="FameList",value="school_id")
	public @ResponseBody Object getFameList(HttpServletRequest request){
		Integer fame_id = Integer.parseInt(request.getParameter("fame_id"));
		FameVO fame = fameService.getFameList(fame_id);
		return ResponseUtils.sendSuccess(fame);
	}
	
	/**
	 * 获取名人墙消息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getFameListForWeb")
	@GetCache(name="FameList",value="school_id")
	public @ResponseBody Object getFameListForWeb(HttpServletRequest request){
		FameVO vo = BeanUtil.formatToBean(FameVO.class);
		if(IntegerUtil.isEmpty(vo.getSchool_id()))
			vo.setSchool_id(ActionUtil.getSchoolID());
		List<FameVO> list = fameService.getFameListForWeb(vo);
		return ResponseUtils.sendSuccess(list);
	}
	

	

	/**
	 * 删除名人信息
	 */
	@RequestMapping(value = "/deleteFame")
	@PutCache(name="FameList",value="school_id")
	public Object deleteFame(HttpServletRequest request) {
		FameVO vo = BeanUtil.formatToBean(FameVO.class);
		// System.out.println("删除接口已经通过......");
		fameService.deleteFame(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 更新名人信息
	 * @param request
	 * @return
	 */	
	@PutCache(name="fameID",value="fame_id")
	@RequestMapping(value="/updateFame")
	public Object updateFame(HttpServletRequest request){
		FameVO vo = BeanUtil.formatToBean(FameVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		fameService.updateFame(vo);
		return ResponseUtils.sendSuccess(vo);
	}
}
