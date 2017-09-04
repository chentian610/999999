package com.ninesky.classtao.picture.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninesky.classtao.picture.service.PictureService;
import com.ninesky.classtao.picture.vo.PictureVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.ResponseUtils;

@Controller
@RequestMapping(value="pictureAction")
public class PictureController {
	
	@Resource
	private PictureService pictureService;
	/**
	 * 新增条目
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addPicture")
	public @ResponseBody Object addPicture(HttpServletRequest request){
		PictureVO vo = BeanUtil.formatToBean(PictureVO.class);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setAdd_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		return ResponseUtils.sendSuccess(pictureService.addPicture(vo));
	}
	/**
	 * 获取条目列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPictureList")
	public @ResponseBody Object getPictureList(HttpServletRequest request){
		PictureVO vo = BeanUtil.formatToBean(PictureVO.class);
		return ResponseUtils.sendSuccess(pictureService.getPictureList(vo));
	}
	/**
	 * 删除条目
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deletePicture")
	public @ResponseBody Object deletePicture(HttpServletRequest request){
		PictureVO vo = BeanUtil.formatToBean(PictureVO.class);
		pictureService.deletePicture(vo);
		return ResponseUtils.sendSuccess();
	}
}
