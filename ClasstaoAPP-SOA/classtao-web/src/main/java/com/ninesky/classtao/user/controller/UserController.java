package com.ninesky.classtao.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.school.service.BedroomService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserRoleVO;
import com.ninesky.classtao.user.vo.UserSnsVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.classtao.user.vo.ValidateCodeVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.MD5Util;
import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="userAction")
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BedroomService bedroomService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value="/getValidateCode")
	public @ResponseBody Object getValidateCode(HttpServletRequest request) {
		String phone=request.getParameter("phone");
		int school_id;
		if (StringUtil.isEmpty(request.getParameter("school_id"))) school_id = ActionUtil.getSchoolID();
		else school_id=Integer.parseInt(request.getParameter("school_id"));
		SchoolVO schoolVO=schoolService.getSchoolById(school_id);
		String mobile="^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
		if(!phone.matches(mobile)){//对手机号的验证
			throw new BusinessException("请输入正确的手机号！");
		}		
		String vCode = StringUtil.randomCodeNumber(6);
		messageService.sendMessage(phone,MsgService.getMsg("MESSAGE_VCODE_LOGIN",vCode,schoolVO.getSchool_name()));
		ValidateCodeVO vo = new ValidateCodeVO();
		vo.setPhone(request.getParameter("phone"));
		vo.setValidate_code(vCode);
		vo.setIs_use(Constants.FALSE_FLAG);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		userService.updateValidateCode(vo);
		return ResponseUtils.sendSuccess("验证码已经发送，请查收！",vo);
	}	
	
	@RequestMapping(value="/getValidateCodePhone")
	public @ResponseBody Object getValidateCodePhone(HttpServletRequest request) {
		String phone=request.getParameter("phone");
		String mobile="^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
		if(!phone.matches(mobile)){//对手机号的验证
			throw new BusinessException(MsgService.getMsg("ERROR_PHONE"));
		}
		String vCode = StringUtil.randomCodeNumber(6);
		messageService.sendMessage(phone,MsgService.getMsg("MESSAGE_VCODE_DEFAULT",vCode));
		ValidateCodeVO vo = new ValidateCodeVO();
		vo.setPhone(request.getParameter("phone"));
		vo.setValidate_code(vCode);
		vo.setIs_use(Constants.FALSE_FLAG);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		userService.updateValidateCode(vo);
		return ResponseUtils.sendSuccess("验证码已经发送，请查收！",vo);
	}
	
	@RequestMapping(value="/checkValidateCode")
	public @ResponseBody Object checkValidateCode(HttpServletRequest request) {
		 if (!userService.checkValidateCode(ActionUtil.getParameterMap()))
	        	throw new BusinessException("验证码输入错误，请输入正确的验证码，或者重新获取验证码....");
		 return ResponseUtils.sendSuccess();
	}
	
	@RequestMapping(value="/getUserInfo")
	@ResultField
	@GetCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object getUserInfo() {
		 UserVO user = userService.getUserByID(ActionUtil.getUserID());
		 return ResponseUtils.sendSuccess(user);
	}	
	
	/**
	 * 忘记密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resetPassword")
	public @ResponseBody Object resetPassword(HttpServletRequest request) {
		  if (!userService.checkValidateCode(ActionUtil.getParameterMap()))
	        	throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
		UserVO vo = new UserVO();
		vo.setPhone(request.getParameter("phone"));
		vo.setPass_word(request.getParameter("pass_word"));
		userService.resetPassword(vo);
		return ResponseUtils.sendSuccess("密码重置成功");
	}
	
	/**
	 * 修改密码
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/updatePassword")
	public @ResponseBody Object updatePassword(UserVO vo) {
		userService.updatePassword(vo);
		return ResponseUtils.sendSuccess(MsgService.getMsg("UPDATE_PASSWORD"));
	}
	
	
	/**
	 * 后台忘记密码功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/forgetPassword")
	public @ResponseBody Object forgetPassword(HttpServletRequest request) {
		String phone=request.getParameter("phone");
		String mobile="^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
		if(!phone.matches(mobile)){//对手机号的验证
			throw new BusinessException(MsgService.getMsg("ERROR_PHONE"));
		}
		  if (!userService.checkValidateCode(ActionUtil.getParameterMap()))
	        	throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
		UserVO vo = new UserVO();
		vo.setPhone(request.getParameter("phone"));
		vo.setPass_word(MD5Util.toMd5(request.getParameter("pass_word")));
		userService.resetPassword(vo);
		return vo;
	}
	
	/**
	 * 更改用户手机号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resetPhone")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object modifyPhone(HttpServletRequest request,HttpServletResponse response){
		  if (!userService.checkValidateCode(ActionUtil.getParameterMap()))
	        	throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
		  UserVO uvo=userService.getUserByPhone(request.getParameter("phone"));
			if (uvo != null) 
				throw new BusinessException(MsgService.getMsg("USER_ONLY_PHONE"));
		UserVO vo = new UserVO();
		vo.setPhone(request.getParameter("phone"));
		vo.setUser_id(ActionUtil.getUserID());
		userService.modifyPhone(vo);
		return ResponseUtils.sendSuccess("更改手机号码成功");
	}
	
	/**
	 * 更改用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateUserInfo")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object completeUserInfo(HttpServletRequest request){
		UserVO vo=BeanUtil.formatToBean(UserVO.class);
	    vo.setUser_id(ActionUtil.getUserID());
    	userService.completeUserInfo(vo);
       vo= userService.getUserByID(ActionUtil.getUserID());
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 给教师添加职务
	 * @return
	 */
	@RequestMapping(value="/addTeacherDuty")
	@PutCache(name="getUserInfoByID",value="user_id")
	@ResultField
	public @ResponseBody Object insertTeacher(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		if (DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty())) {
			return userService.addTeacherInterestDuty(vo);
		} else 
			return userService.addTeacherDuty(vo);
	}
	
	/**
	 * 给家长添加孩子
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addParentChild")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object insertParent(HttpServletRequest request){
		ParentVO vo=BeanUtil.formatToBean(ParentVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		vo=userService.addParentChild(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 完善(更新)孩子信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateChildInfo")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object updateParentChild(HttpServletRequest request){
	        ParentVO vo=BeanUtil.formatToBean(ParentVO.class);
	    	vo=userService.updateChildByStudentCode(vo);
	    	return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 绑定社交帐号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/bindSnsAccount")
	public @ResponseBody Object bindSnsAccount(HttpServletRequest request){
		UserSnsVO vo=BeanUtil.formatToBean(UserSnsVO.class);
		vo=userService.addUserSns(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 获取社交帐号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSnsAccountList")
	public @ResponseBody Object getSnsAccountList(HttpServletRequest request){
		UserSnsVO vo=BeanUtil.formatToBean(UserSnsVO.class);
		List<UserSnsVO> list=userService.getUserSnsList(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 获取寝室学生列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStudentPosistionOfBed")
	@GetCache(name="getStudentPosistionOfBed",value="school_id,bedroom_id")
	@ResultField(includes={"id","school_id","bedroom_id","bedroom_name","bed_code","student_id","student_code","student_name","class_name","sex"})
	public Object getStudentPosistionOfBed(HttpServletRequest request){
		int bedroom_id=Integer.parseInt(request.getParameter("bedroom_id"));
		return bedroomService.getBedPositionList(bedroom_id);
	}
	
	/**
	 * 删除教师职务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteTeacherDuty")
	@PutCache(name="getUserInfoByID",value="user_id")
	@ResultField
	public @ResponseBody Object deleteTeacherDuty(HttpServletRequest request){
		int id=Integer.parseInt(request.getParameter("teacher_id"));
		return userService.deleteTeacherDuty(id);
	}

	/**
	 * 批量删除教师职务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteTeacherList")
	public @ResponseBody Object deleteTeacherList(HttpServletRequest request){
		String teacher_ids = request.getParameter("teacher_ids").toString();
		return userService.deleteTeacherList(teacher_ids);
	}


	/**
	 * 点击年级后显示相应班级
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/showClassOfGrade")
	public @ResponseBody Object showClassOfGrade(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		List<TeacherVO> list=userService.showClassOfGrade(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 添加教师
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addTeacher")
	public @ResponseBody Object addTeacher(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		if (DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty())) {
			TeacherVO teacherVO=userService.addInterestTeacher(vo);
			return ResponseUtils.sendSuccess(teacherVO);
		} else{
			TeacherVO teacherVO=userService.addTeacher(vo);
			return ResponseUtils.sendSuccess(teacherVO);
		}
	}
	
	/**
	 * 批量添加教师
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addTeacherList")
	public @ResponseBody Object addTeacherList(HttpServletRequest request){
		String item_list=request.getParameter("item_list");
		return userService.addTeacherList(item_list);
	}
	
	/**
	 * 删除学生
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteStudent")
	@PutCache(name="getStudentList",value="school_id,class_id")
	public @ResponseBody Object deleteStudent(HttpServletRequest request){
		StudentVO studentVO=BeanUtil.formatToBean(StudentVO.class);
		//设置class_id,提供缓存版本号更新使用
		ActionUtil.getParameterMap().put("class_id",userService.getStudentById(studentVO.getStudent_id()).getClass_id()+"");
		userService.deleteStudent(studentVO);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 确认教师身份
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateTeacherConfirmFlag")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object updateTeacherConfirmFlag(HttpServletRequest request){
		String teacher_id=request.getParameter("teacher_id");
		String[] list=teacher_id.split(",");//存放需确认的教师身份的teacher_id
		for(int i=0;i<list.length;i++){
		userService.confirmTeacher(Integer.parseInt(list[i].trim()));
		}
		return ResponseUtils.sendSuccess("已成功确认教师身份！");
	}

	/**
	 * 修改教师职务身份
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateTeacherDuty")
	@PutCache(name="getUserInfoByID",value="user_id")
	@ResultField
	public @ResponseBody Object updateTeacherDuty(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		return userService.updateTeacher(vo);
	}
	
	@RequestMapping(value="/updateTeacherDutyOfManager")
	@PutCache(name="getUserInfoByID",value="user_id")
	@ResultField
	public @ResponseBody Object updateTeacherDutyOfManager(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		return userService.updateTeacherOfManager(vo);
	}
	
	/**
	 * 删除孩子
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteChild")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object deleteChild(HttpServletRequest request){
		int parent_id=Integer.parseInt(request.getParameter("parent_id"));
		userService.deleteChild(parent_id);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 删除教师
	 * @return
	 */
	@RequestMapping(value="/deleteTeacherOfManager")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object deleteTeacherOfManager(HttpServletRequest request){
		String phone=request.getParameter("phone");
		UserVO vo=userService.getUserByPhone(phone);
		if (vo!=null)
			ActionUtil.getActionParam().setUser_id(vo.getUser_id());
		userService.deleteTeacherOfManager(phone);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 修改教师姓名或手机号,性别，后台管理
	 * @return
	 */
	@RequestMapping(value="/updateTeacherNamePhone")
	@PutCache(name="getUserInfoByID",value="user_id")
	public @ResponseBody Object updateTeacherNamePhone(){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		if (IntegerUtil.isNotEmpty(vo.getUser_id()))
			ActionUtil.getActionParam().setUser_id(vo.getUser_id());
		userService.updateTeacherNamePhone(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 修改学生信息
	 * @return
	 */
	@RequestMapping(value="/updateStudent")
	public @ResponseBody Object updateStudent(){
		StudentVO vo=BeanUtil.formatToBean(StudentVO.class);
		userService.updateStudent(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 家长花名册列表（新  全）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getParentOfMng")
	public @ResponseBody Object getParentOfMng(HttpServletRequest request){
		String search=request.getParameter("student_name");
		return ResponseUtils.sendSuccess(userService.getParentOfMng(search));
	}
	
	/**
	 * 后台添加家长
	 * @return
	 */
	@RequestMapping(value="/addParent")
	public @ResponseBody Object addParent(){
		ParentVO vo=BeanUtil.formatToBean(ParentVO.class);
		userService.addParent(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 删除某位学生的所有家长
	 * @return
	 */
	@RequestMapping(value="/deleteAllParent")
	public @ResponseBody Object deleteAllParent(HttpServletRequest request){
		userService.deleteAllChild(request.getParameter("student_code"));
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 后台修改家长
	 * @return
	 */
	@RequestMapping(value="/updateParent")
	public @ResponseBody Object updateParent(){
		ParentVO vo=BeanUtil.formatToBean(ParentVO.class);
		userService.updateParent(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 后台学生分组列表
	 * @return
	 */
	@RequestMapping(value="/getStudentListGroup")
	public @ResponseBody Object getStudentListGroup(){
		ContactListVO vo=BeanUtil.formatToBean(ContactListVO.class);
		return ResponseUtils.sendSuccess(userService.getStudentListOfGroup(vo));
	}
	
	/**
	 * 添加教师表和家长表的all_letter,first_letter
	 * @return
	 */
	@RequestMapping(value="/updateLetter")
	public @ResponseBody Object updateLetter(){
		userService.updateLetter();
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 重置代理商登录密码
	 * @return
	 */
	@RequestMapping(value="/ResetAgentPassword")
	public @ResponseBody Object ResetAgentPassword(HttpServletRequest request){
		UserVO vo = BeanUtil.formatToBean(UserVO.class);
		userService.resetPassword(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 重置代理商登录密码
	 * @return
	 */
	@RequestMapping(value="/addUserRole")
	public @ResponseBody Object addUserRole(HttpServletRequest request){
		UserRoleVO vo = BeanUtil.formatToBean(UserRoleVO.class);
		userService.addUserRole(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 校务通知获取全部教师
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAllTeacher")
	@ResultField(includes={"user_id","teacher_name","first_letter","all_letter","head_url","phone","duty_name"})
	public  Object getAllTeacher(HttpServletRequest request){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		return userService.getAllTeacher(vo);
	}

	/**
	 * 获取与教师相关的学生列表
	 * @return
	 */
	@RequestMapping(value="/getStudentList")
	@ResultField(includes = {"student_id","student_name","all_letter","first_letter","head_url","class_name"})
	public Object getStudent(HttpServletRequest request){
		return userService.getStudentList(request.getParameter("student_name"));
	}

	/**
	 * 获取学生基本信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStudentInformationByID")
	@ResultField(excludes = {"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date","version","sex_name","user_type","user_id"})
	public @ResponseBody Object getStudentInformationByID(Integer student_id,HttpServletRequest request){
		return ResponseUtils.sendSuccess(userService.getStudentInformationByID(student_id));
	}

    /**
     * 通过手机号获取教师身份
     * @param request
     * @return
     */
    @RequestMapping(value="/getTeacherListByPhone")
    public Object getTeacherListByPhone(HttpServletRequest request){
        TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
        vo.setSchool_id(ActionUtil.getSchoolID());
        return userService.getTeacherListByPhone(vo);
    }

    /**
     * 教师身份，合并身份 例：数学（2个班级）
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTeacherList")
    public Object getTeacherList(HttpServletRequest request){
        return userService.getDutyNameList(request.getParameter("phone"));
    }

	/**
	 * 教师身份，合并身份 例：数学（2个班级）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTeacherListByRole")
	@ResultField(includes = {"phone","school_id","user_id","is_charge","course","teacher_name","teacher_id","sex"})
	public Object getTeacherListByRole(HttpServletRequest request){
		TeacherVO vo = BeanUtil.formatToBean(TeacherVO.class);
		return userService.getTeacherListByRole(vo);
	}
}
