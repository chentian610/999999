package com.ninesky.classtao.contact.controller;

import com.ninesky.classtao.contact.service.ContactService;
import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/contactAction")
public class ContactController extends BaseController{
	
	@Resource
	private ContactService contactService;
	
	
	@Resource
	private ClassService classService;

	@Resource
	private DynamicService dynamicService;

	@Resource
	private GetuiService getuiService;
	@Autowired
	private RedisService redisService;

	/**
	 * 获取全部联系人
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getContactList")
	@ResultField(includes={"user_id","user_type","user_name","first_letter","all_letter","phone","head_url","duty","school_id","class_id"})
	public  Object getContactList(HttpServletRequest request){
		TeacherVO vo = BeanUtil.formatToBean(TeacherVO.class);
		List<TeacherVO> returnList = contactService.getTeacherContactList(vo);
		return returnList;
	}
	/**
	 * 新增自定义通讯录组及组成员
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addContactGroup")
	@ResultField(includes={"contact_id","contact_name"})
	public @ResponseBody Object addContactGroup(HttpServletRequest request){
		ContactVO vo = BeanUtil.formatToBean(ContactVO.class);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		contactService.addContactGroup(vo);
		return vo;
	}
	/**
	 * 删除自定义通讯录组及组成员(禁用兴趣班)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteContactGroup")
	public @ResponseBody Object deleteContactGroup(HttpServletRequest request){
		ContactVO vo = BeanUtil.formatToBean(ContactVO.class);
		contactService.deleteContactGroup(vo);
		return ResponseUtils.sendSuccess();
	}
	/**
	 * 为指定组添加成员(添加兴趣班成员)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addContactListByGroup")
	@ResultField(includes={"user_id","user_type","user_name","phone"})
	public @ResponseBody Object addContactListByGorup(HttpServletRequest request){
		ContactVO vo = BeanUtil.formatToBean(ContactVO.class);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		contactService.addContactListByGroup(vo);
		return vo;
	}
	/**
	 * 为指定组删除成员(删除兴趣班中的成员)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteContactByGorup")
	@ResultField(includes={"user_id","user_type","user_name","phone"})
	public @ResponseBody Object deleteContactByGorup(HttpServletRequest request){
		ContactListVO vo = BeanUtil.formatToBean(ContactListVO.class);
		contactService.deleteContactByGorup(vo);
		return vo;
	}
	/**
	 * 获取自定义通讯录组列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getContactGroupList")
	@ResultField(includes={"contact_id","contact_name","user_type"})
	public @ResponseBody Object getContactGroupList(HttpServletRequest request){
		ContactVO vo = BeanUtil.formatToBean(ContactVO.class);
		List<ContactVO> list = contactService.getContactGroupList(vo);
		return list;
	}
	/**
	 * 获取自定义通讯录组中的联系人列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getContactListByGroup")
	@ResultField(includes={"user_id","user_type","user_name","phone","first_letter","all_letter","head_url","contact_id","duty","role_tags"})
	public Object getContactListByGroup(HttpServletRequest request){
		TeacherVO vo = BeanUtil.formatToBean(TeacherVO.class);
		ContactListVO contact = new ContactListVO();
		List<ContactListVO> returnList = null;	
		if(vo.getDuty() != null){
			String duty = vo.getDuty();
			returnList = contactService.getContactList(duty);
		}else {
			contact.setContact_id(Integer.parseInt(ActionUtil.getParameter("contact_id")));
			contact.setSchool_id(ActionUtil.getSchoolID());
			returnList = contactService.getContactListByGroup(contact);
		}
		return returnList;
	}
	
	/**
	 * 后台花名册添加分组，只需分组名(添加兴趣班)
	 * @return
	 */
	@RequestMapping(value="/addContact")
	public @ResponseBody Object addContact(){
		ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
		ContactVO contactVO = contactService.addContact(vo);
		List<ReceiveVO> receiveList = new ArrayList<ReceiveVO>();
		ReceiveVO receive = new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),0);
		receiveList.add(receive);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("info_content","");
		dataMap.put("info_title", MsgService.getMsg("INTERST_CLASS_START_NOTICE",vo.getTeacher_name(),vo.getContact_name()));
		dataMap.put("module_code", DictConstants.MODULE_CODE_CURRICULA_VARIABLE);
		dataMap.put("module_pkid",vo.getContact_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
		dynamicService.insertDynamic(dataMap, receiveList);
		getuiService.pushMessage(dataMap, receiveList);
		return ResponseUtils.sendSuccess(contactVO);
	}
	
	/**
	 * 后台获取花名册分组（分教师，家长，学生）(获取兴趣班)
	 * @return
	 */
	@RequestMapping(value="/getContactGroupListOfManager")
	@ResultField
	public @ResponseBody Object getContactGroupListOfManager(){
		ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
		return ResponseUtils.sendSuccess(contactService.getContactGroupListOfManager(vo));
	}
	
	/**
	 * 获取相应班级的教师信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTeacherContactList")
	@ResultField(includes = {"teacher_id","user_id","teacher_name","phone","grade_id","class_name","class_id","role_tags","duty","is_charge"})
	public @ResponseBody Object getTeacherContactListByClassID(HttpServletRequest request){
		TeacherVO vo = BeanUtil.formatToBean(TeacherVO.class);
		return contactService.getTeacherContactList(vo);
	}
	
	/**
	 * 根据ID或者phone获取教师信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTeacherListByUserID")
	@ResultField(includes = {"teacher_id","user_id","team_type","grade_id","class_id","class_name","contact_id","sex","teacher_name","phone","head_url","is_confirm","is_graduate","course","duty","is_charge","duty_name","role_tags"})
	public @ResponseBody Object getTeacherListByPhone(HttpServletRequest request){
		return contactService.getTeacherListByPhone(request.getParameter("phone"));
	}
	
	
	/**
	 * 获取相应班级家长信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getParentContactList")
	@ResultField(excludes = {"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version"})
	public @ResponseBody Object getParentContactList(HttpServletRequest request){
		ParentVO vo = BeanUtil.formatToBean(ParentVO.class);
		List<ParentVO> returnList = contactService.getParentContactListByStudentID(vo);
		return returnList;
	}
	
	/**
	 * 获取相应班级家长信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStudentContactList")
	@ResultField(excludes = {"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version"})
	public @ResponseBody Object getStudentContactList(HttpServletRequest request){
		StudentVO vo = BeanUtil.formatToBean(StudentVO.class);
		List<StudentVO> returnList = contactService.getStudentContactListByStudentID(vo);
		return returnList;
	}

	/**
	 * 获取相应班级家长信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getContactByName")
	@ResultField(includes = {"all_letter","head_url","user_type","school_id","user_name","first_letter","user_id","phone","role_tags","class_name"})
	public @ResponseBody Object getContactByName(HttpServletRequest request){
		Map<String,String> map = ActionUtil.getParameterMap();
		return contactService.getContactBySearch(map);
	}

	/**
	 * 获取相应班级的师生信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTeaStudentContactList")
	@ResultField(includes = {"all_letter","head_url","user_type","school_id","user_name","first_letter","user_id","phone"})
	public @ResponseBody Object getTeaStudentContactList(HttpServletRequest request){
		Map<String,String> map = ActionUtil.getParameterMap();
		return contactService.getTeaStudentContactList(map);
	}

	/**
	 * 获取兴趣班列表
	 * @return
	 */
	@RequestMapping(value="/getInterestClassList")
	public Object getInterestApplyList(){
		ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
		return contactService.getInterestClassList(vo);
	}

	/**
	 * 已报名但未被教师选进兴趣班的学生列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUnContactStuList")
	@ResultField
	public Object getUnContactStuList(HttpServletRequest request){
		return contactService.getUnContactStuList(Integer.parseInt(request.getParameter("contact_id").trim()));
	}

	/**
	 * 报名
	 * @return
	 */
	@RequestMapping(value="/enrollClass")
	public Object enrollClass(){
		ContactListVO vo=BeanUtil.formatToBean(ContactListVO.class);
		contactService.enrollClass(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 获取兴趣班详情
	 */
	@RequestMapping(value = "/getInterestClassByID")
	@ResultField
	public Object getInterestClassByID(){
		ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
		return contactService.getContactByID(vo.getContact_id());
	}

	/**
	 * 修改兴趣班信息
	 */
	@RequestMapping(value="/updateInterestClass")
	public Object updateInterestClass(){
		ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
		contactService.updateInterestClass(vo);
		return ResponseUtils.sendSuccess();
	}

    /**
     * 通过兴趣班名称获取兴趣班
     * @return
     */
    @RequestMapping(value = "/getInterestByName")
    public Object getInterestByName(){
        ContactVO vo=BeanUtil.formatToBean(ContactVO.class);
        return contactService.getInterestByName(vo);
    }
}
