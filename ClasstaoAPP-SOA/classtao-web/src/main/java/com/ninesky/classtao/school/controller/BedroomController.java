package com.ninesky.classtao.school.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ninesky.classtao.school.service.BedroomService;
import com.ninesky.classtao.school.vo.BedVO;
import com.ninesky.classtao.school.vo.BedroomVO;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;

@RestController
@RequestMapping(value="bedroomAction")
public class BedroomController extends BaseController
{	
	@Autowired
	private BedroomService bedroomService;
	
	/**
	 * 添加寝室
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="addBedroom")
	@PutCache(name="getBedroomList",value="school_id")
	public @ResponseBody Object addBedroom(HttpServletRequest request)
	{	
		BedroomVO vo = BeanUtil.formatToBean(BedroomVO.class);
		List<BedroomVO> list = new ArrayList<BedroomVO>();
		list = bedroomService.insertBedroom(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 根据寝室前缀和楼层 获得寝室信息(前缀：bedroom_pre, 楼层：bedroom_floor)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getBedroom")
	@PutCache(name="getBedroomList",value="school_id")
	public @ResponseBody Object getBedroom(HttpServletRequest request)
	{
		String bedroom_pre = request.getParameter("bedroom_pre");
		String bedroom_floor = request.getParameter("bedroom_floor");
		//模糊查询 
		//如果不输入bedroom_pre就只按楼层查询
		String bedroom = (!StringUtil.isEmpty(bedroom_pre))?(bedroom_pre + bedroom_floor + "%"):("_" + bedroom_floor + "%");
		List<BedroomVO> list = bedroomService.getBedroomInfoByFloor(bedroom);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 点击楼号后，显示寝室
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getBedroomList")
	@GetCache(name="getBedroomList",value="school_id")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date"})
	public Object getBedroomList(HttpServletRequest request){
		BedroomVO vo=BeanUtil.formatToBean(BedroomVO.class);
		return bedroomService.getBedroomList(vo);
	}
	
	/**
	 * 设置寝室床位人员
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="bindBedOfStudent")
	@GetCache(name="getStudentPosistionOfBed",value="school_id,bedroom_id")
	public @ResponseBody Object bindBedOfStudent(HttpServletRequest request)
	{	
		BedVO vo = BeanUtil.formatToBean(BedVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		bedroomService.bindBedForStudent(vo);
		return ResponseUtils.sendSuccess("绑定学生和床位成功");
	}
	
	/**
	 * 添加寝室人员
	 * @return
	 */
	@RequestMapping(value="addStudentForBed")
	@GetCache(name="getStudentPosistionOfBed",value="school_id,bedroom_id")
	public @ResponseBody Object addStudentForBed(){
		BedVO vo = BeanUtil.formatToBean(BedVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		bedroomService.addStudentForBed(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 批量添加寝室人员
	 * @return
	 */
	@RequestMapping(value="addStudentListForBed")
	public @ResponseBody Object addStudentListForBed(HttpServletRequest request){
		return ResponseUtils.sendSuccess(bedroomService.addStudentListForBed(request.getParameter("item_list")));
	}
	
	/**
	 * 获取指定寝室人员列表
	 * @param bedroom_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getBedroomStuList")
	public @ResponseBody Object getStudentPosition(@RequestParam(value="bedroom_id") Integer bedroom_id)
	{	
		List<BedVO> list = bedroomService.getBedListByBedroomId(bedroom_id);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 根据寝室id、床位号 删除寝室床位人员信息（床位号为0删除全寝室人员）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="deleteStudentPosition")
	@PutCache(name="getStudentPosistionOfBed",value="school_id,bedroom_id")
	public @ResponseBody Object deleteBedOfStudent(HttpServletRequest request)
	{	
		BedVO vo = BeanUtil.formatToBean(BedVO.class);
		bedroomService.deleteBedOfStudent(vo);
		return ResponseUtils.sendSuccess(MsgService.getMsg("DELETE_SUCCESS"));
	}

	/**
	 * 后台寝室设置页面
	 * @return
	 */
	@RequestMapping(value="showBedroom")
	public @ResponseBody Object showBedroom(){
		return ResponseUtils.sendSuccess(bedroomService.getBedroomListOfManager());
	}
	
	/**
	 * 后台寝室设置页面（添加寝室）
	 * @return
	 */
	@RequestMapping(value="insertBedroom")
	@PutCache(name="getBedroomList",value="school_id")
	public @ResponseBody Object insertBedroom(){
		return ResponseUtils.sendSuccess(bedroomService.addBedroom(BeanUtil.formatToBean(BedroomVO.class)));
	}
	
	/**
	 * 后台寝室设置页面（删除寝室）
	 * @return
	 */
	@RequestMapping(value="deleteBedroom")
	@PutCache(name="getBedroomList",value="school_id")
	public @ResponseBody Object deleteBedroom(HttpServletRequest request){
		int bedroom_id=Integer.parseInt(request.getParameter("bedroom_id"));
		bedroomService.deleteBedroom(bedroom_id);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 寝室打分
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="roomjilv")
	public @ResponseBody ModelAndView roomjilv()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("../zju/bedroomDiscipline.jsp");
		return model;
	}
	
	/**
	 * 寝室打分
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/roomjiancha")
	public @ResponseBody ModelAndView roomjiancha(HttpServletRequest request)
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("../score/roomjiancha.jsp");
		return model;
	}
	
	/**
	 * 寝室考勤
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/roomkq")
	public @ResponseBody ModelAndView roomkq(HttpServletRequest request)
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("../score/roomkq.jsp");
		return model;
	}
}
