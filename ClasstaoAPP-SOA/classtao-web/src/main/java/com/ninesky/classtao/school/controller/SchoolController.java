
package com.ninesky.classtao.school.controller;


import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value="schoolAction")
public class SchoolController extends BaseController{

	@Autowired
	private SchoolService schoolService;

	
	/**
	 * 获取学校管理员联系方式
	 * @return
	 */
	@RequestMapping(value="/getAdminPhone")
	public @ResponseBody Object getAdminPhone(){
		String phone=schoolService.getAdminPhone(ActionUtil.getSchoolID());
		return ResponseUtils.sendSuccess(phone);
	}
	/**
	 * 根据学校id获取学校信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolById")
	@ResultField(excludes={"app_sql","order_sql","start_time","direction","create_by","agent_phone","fistpage_url","fistpage_type","app_status","module_ids","module_codes","content",""})
	public @ResponseBody Object getSchoolById(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		return ResponseUtils.sendSuccess(schoolService.getSchoolById(vo.getSchool_id()));
	}
	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolApplyList")
	public @ResponseBody Object getSchoolApplyList(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		return ResponseUtils.sendSuccess(schoolService.getSchoolApplyList(vo));
	}
	/**
	 * 通过学校申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateSchool")
	public @ResponseBody Object updateSchool(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		schoolService.updateSchool(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 通过学校申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateSchoolApply")
	public @ResponseBody Object passSchoolApply(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		schoolService.passSchoolApply(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolList")
	@ResultField(includes={"school_id","school_name"})
	public Object getSchoolList(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		return schoolService.getSchoolList(vo);
	}

	/**
	 * 获取代理商申请学校列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAgentApplySchoolList")
	public Object getAgentApplySchoolList(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		return schoolService.getAgentApplySchoolList(vo);
	}

	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeSchool")
	public Object removeSchool(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		schoolService.removeSchool(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolServerConfig")
	public Object getSchoolServerConfig(Integer school_id,HttpServletRequest request){
		return ResponseUtils.sendSuccess(schoolService.getSchoolServerConfig(school_id));
	}

	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolAPPUpdateList")
	public Object getSchoolAPPUpdateList(HttpServletRequest request){
		Map<String,String> paramMap = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(schoolService.getSchoolAPPUpdateList(paramMap));
	}

	/**
	 * 获取学校申请列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSchoolAPPUpdateListByID")
	public Object getSchoolAPPUpdateListByID(HttpServletRequest request){
		SchoolVO vo = BeanUtil.formatToBean(SchoolVO.class);
		if (IntegerUtil.isEmpty(vo.getSchool_id())) return ResponseUtils.sendSuccess();
		return ResponseUtils.sendSuccess(schoolService.getSchoolAPPUpdateListByID(vo));
	}
	
	/**
	 * 获取学校申请列表
	 * @param request
	 * @author chenth123
	 * @return
	 * @throws NoSuchRequestHandlingMethodException 
	 */
	@RequestMapping(value = "redirect/{domain}", method = RequestMethod.GET)
	public @ResponseBody Object redirectSchoolByDomain(@PathVariable String domain,HttpServletRequest request) throws NoSuchRequestHandlingMethodException{
		ModelAndView model = new ModelAndView();
		model.setViewName("../../audit/home/login.html");
		if ("agent".equals(domain)) {
			request.getSession().setAttribute("logo", "agent/images/img_glod.pn");
			request.getSession().setAttribute("school_name", MsgService.getMsg("HELLO_AGENT"));
			request.getSession().setAttribute("school_id", 0);
			request.getSession().setAttribute("user_type", DictConstants.USERTYPE_AGENT);
			model.addObject("logo","../../agent/images/img_glod.png");
			model.addObject("school_name",MsgService.getMsg("HELLO_AGENT"));
			model.addObject("school_id",0);
			model.addObject("user_type",DictConstants.USERTYPE_AGENT);
		} else if ("manager".equals(domain)) {
			request.getSession().setAttribute("logo", "audit/home/images/img_glod.png");
			request.getSession().setAttribute("school_name", MsgService.getMsg("HELLO_MANAGER"));
			request.getSession().setAttribute("school_id", 0);
			request.getSession().setAttribute("user_type", DictConstants.USERTYPE_SUPER);
			model.addObject("logo","audit/home/images/img_glod.png");
			model.addObject("school_name",MsgService.getMsg("HELLO_MANAGER"));
			model.addObject("school_id",0);
			model.addObject("user_type",DictConstants.USERTYPE_SUPER);
		} else {
			SchoolVO vo = schoolService.getSchoolByDomain(domain);
			if (vo == null) throw new NoSuchRequestHandlingMethodException(request);
			request.getSession().setAttribute("logo", vo.getOrganize_pic_url());
			request.getSession().setAttribute("school_name", vo.getSchool_name());
			request.getSession().setAttribute("school_id", vo.getSchool_id());
			request.getSession().setAttribute("user_type", DictConstants.USERTYPE_ADMIN);
			request.getSession().setAttribute("install_url",vo.getInstall_url());
			model.addObject("school_name",vo.getSchool_name());
			model.addObject("logo",vo.getOrganize_pic_url());
			model.addObject("school_id",vo.getSchool_id());
			model.addObject("user_type",DictConstants.USERTYPE_ADMIN);
			model.addObject("install_url",vo.getInstall_url());
		}
		request.getSession().setAttribute("web_domain_record",SystemConfig.getProperty("WEB_DOMAIN_RECORD"));
		model.addObject("web_domain_record",SystemConfig.getProperty("WEB_DOMAIN_RECORD"));
		return model;
	}
}
