package com.ninesky.classtao.login.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.common.util.ListUtil;
import com.ninesky.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ninesky.classtao.capital.vo.GetuiBindAlis;
import com.ninesky.classtao.capital.vo.GetuiUnBindAlis;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.agent.service.AgentService;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserRoleVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="loginAction")
public class LoginController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ClassService classService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private RedisService redisService;
	
	@RequestMapping(value="/login")
	public @ResponseBody Object login(HttpServletRequest request){
		int school_id=Integer.parseInt(request.getParameter("school_id"));
		//获取前端返回的数据
		 UserVO vo = BeanUtil.formatMapToBean(ActionUtil.getParameterMap(), UserVO.class);
		 UserVO uvo = new UserVO();
		 if (StringUtil.isNotEmpty(vo.getPass_word())) {//判断用户是否使用密码登录系统
			 uvo=userService.validateAdmin(vo);
			 if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())) {//如果是教师登录验证教师账号身份是否激活
				 //获取教师身份信息
				 List<TeacherVO> list = classService.getDutyNameList(vo.getPhone());
				 //判断教师身份已经激活账号
				 if (ListUtil.isNotEmpty(list) && uvo == null) throw new BusinessException(MsgService.getMsg("LOGIN_USER_INACTIVE_AND_USE_VCODE"));
			 }
			 //判断账号是否存在
			 if (uvo == null) throw new BusinessException(MsgService.getMsg("LOGIN_USER_NOT_EXISTS_TRY_AGAIN"));
			 //判断账号身份设置了登录密码
			 else if (StringUtil.isEmpty(uvo.getPass_word())) throw new BusinessException(MsgService.getMsg("LOGIN_USER_PASSWORD_EMPTY_AND_USE_VCODE"));
			 //判断登录密码输入身份正确
			 else if (!uvo.getPass_word().equals(vo.getPass_word())) throw new BusinessException(MsgService.getMsg("LOGIN_USER_PASSWORD_ERROR_TRY_AGAIN"));
		 } else {
			 if (!userService.checkValidateCode(ActionUtil.getParameterMap()))//新APP使用验证码登录
		        	throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
			 uvo=userService.getUserByPhone(vo.getPhone());//判断该用户是否已注册过
			 if (uvo != null) userService.validateUser(uvo);//验证该用户的状态是否正常
			 else uvo=userService.insertUser(vo,school_id);//若无该用户，就自动注册
		 }
		 //个人推送配置
		 GetuiBindAlis ptt1 = new GetuiBindAlis(ActionUtil.getSchoolID(),uvo.getUser_id(),request.getParameter("client_id"));
		 Thread thread1 = new Thread(ptt1);
		 thread1.start();
		 //判断用户是否登录成功
		 UserVO userVO=userService.getUserByID(uvo.getUser_id());
		 return doAppLogin(userVO,vo,request);
	}
	
	/**
	 * 管理员登录后台管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/admlogin")
	public @ResponseBody Object admlogin(HttpServletRequest request) {
		UserVO vo = BeanUtil.formatToBean(UserVO.class);
		UserVO returnUser = userService.validateAdmin(vo);
		return doAdminLogin(returnUser,request);
	}
	
	/**
	 * 管理员登录后台管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/LezhiWebLogin")
	public @ResponseBody Object LezhiWebLogin(UserVO vo, HttpServletRequest request) {
		UserVO returnUser = userService.validateLezhiAdmin(vo);
		setUserInfoToSession(returnUser,request);
		UserRoleVO roleVO = new UserRoleVO();                                                                                                                                                                                                                          
		roleVO.setPhone(returnUser.getPhone()); 
		roleVO.setSchool_id(returnUser.getSchool_id());
		roleVO.setUser_type(returnUser.getUser_type());
		if (DictConstants.USERTYPE_AGENT.equals(roleVO.getUser_type())) {
			AgentVO agent = agentService.getAgentByPhone(returnUser.getPhone());
			if (agent == null) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_AGENT"));
			else if (DictConstants.STATUS_STOP.equals(agent.getIs_enable())) throw new BusinessException(MsgService.getMsg("AGENT_USER_DISABLE"));
		} else if (DictConstants.USERTYPE_ADMIN.equals(roleVO.getUser_type())) {
			UserRoleVO uRoleVO = userService.getUserRoleByCondition(roleVO);
			if (uRoleVO==null) {
				List<TeacherVO> tlist = redisService.getSchoolTeacherRoleCodes(roleVO.getPhone());
				if (ListUtil.isEmpty(tlist)) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_ADMIN_TEACHER"));
				request.getSession().setAttribute("role_code", BeanUtil.ListTojson(tlist,false));
			} else request.getSession().setAttribute("role_code", DictConstants.USERTYPE_ADMIN);
		}else if (DictConstants.USERTYPE_SUPER.equals(roleVO.getUser_type())) {
			UserRoleVO uRoleVO = userService.getUserRoleByCondition(roleVO);
			if (uRoleVO == null) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_SUPER"));
		}
		HashMap<String , Object> payload=new HashMap<String, Object>();
		payload.put("uid", returnUser.getUser_id());//用户ID
		payload.put("iat", ActionUtil.getSysTime());//生成时间
		payload.put("ext",ActionUtil.getSysTime().getTime()+1000*60*60*24*7);//过期时间1小时
		returnUser.setToken(JWTUtil.createToken(payload));
		logger.info("returnUser.token="+returnUser.getToken());
		return ResponseUtils.sendSuccess(returnUser);
	}

	@RequestMapping(value="/Login")
	public @ResponseBody Object Login(HttpServletRequest request) throws ClassNotFoundException, IOException{
			ModelAndView model = new ModelAndView();
			String user_type = (String)request.getSession().getAttribute("user_type");
			String phone = (String)request.getSession().getAttribute("phone");
			String school_id = request.getSession().getAttribute("school_id")+"";
            String time = ActionUtil.getSysTime().getTime()+"";//增加time参数，防止缓存导致问题
			if (StringUtil.isEmpty(user_type) || StringUtil.isEmpty(phone)
					|| StringUtil.isEmpty(school_id) || DictConstants.DICT_NONE.equals(user_type)) {
				model.setViewName("../lezhi/skipHomePage.jsp?d="+time);
				return model;
			} else if (DictConstants.USERTYPE_AGENT.equals(user_type)
					|| DictConstants.USERTYPE_ADMIN.equals(user_type)
					|| DictConstants.USERTYPE_SUPER.equals(user_type)) {
				model.setViewName("../lezhi/default.jsp?d="+time);
				return model;
			} else {
				model.setViewName("../audit/home/login.jsp?d="+time);
				return model;
			}
	}

	/**
	 * 退出后除session中的电话号码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/emptySession")
	public @ResponseBody Object emptySession(HttpServletRequest request) {
		request.getSession().removeAttribute("phone");
		return ResponseUtils.sendSuccess();
	}

	/**
	 * APP端登录系统
	 * @param returnUser
	 * @param vo
	 * @param request
	 * @return
	 */
	private Object doAppLogin(UserVO returnUser,UserVO vo,HttpServletRequest request) {
//		if (returnUser==null)
//			throw new BusinessException("账户或者密码错误，请您重新登录！");
//		if (DictConstants.STATUS_INACTIVE.equals(returnUser.getStatus()))
//			throw new BusinessException("该账户还没有启用，请联系管理员开通......");
//		else if (DictConstants.STATUS_STOP.equals(returnUser.getStatus()))
//			throw new BusinessException("该账户已经被停用，请联系管理员......");
		//如果是老师，那么必须要有具体学校班级值才能登陆
//		returnUser.setApp_type(vo.getApp_type());
//		returnUser.setSchool_id(vo.getSchool_id());
		return ResponseUtils.sendSuccess(returnUser);
	}

	/**
	 * Web页面学校管理员登录系统
	 * @param returnUser
	 * @param returnUser
	 * @param request
	 * @return
	 */
	public Object doAdminLogin(UserVO returnUser,HttpServletRequest request){
		System.out.println(ActionUtil.getSchoolID());
		ModelAndView model = new ModelAndView();
		if (returnUser==null) {
			model.addObject("message","账户或者密码错误，请您重新登录！......");
			model.setViewName("../manager/login.jsp");
			return model;
		} else if (DictConstants.STATUS_STOP.equals(returnUser.getStatus()))
		{
			model.addObject("message","该账户已经被停用，请联系管理员......");
			model.setViewName("../manager/login.jsp");
			return model;
		}
		SchoolVO school = schoolService.getSchoolByAdminPhone(returnUser.getPhone());
		if (school == null) {
			model.addObject("message","该账户不是学校管理员，请核实！");
			model.setViewName("../manager/login.jsp");
			return model;
		} else {
			setAdminToSession(returnUser,school,request);
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("school_name", school.getSchool_name());
			map.put("school_id", school.getSchool_id());
			map.put("user_name", returnUser.getUser_name());
			map.put("app_pic_url", school.getApp_pic_url());
			map.put("head_url", returnUser.getHead_url());
			map.put("url", "manager/main_new.jsp");
			return ResponseUtils.sendSuccess(map);

//			model.setViewName("../manager/hplus.jsp");
//			return model;
		}
	}


	@RequestMapping(value="/logout")
	public Object loginout(HttpServletRequest request){
		if (DictConstants.APPTYPE_ANDROID.equals(ActionUtil.getAppType()) || DictConstants.APPTYPE_IOS.equals(ActionUtil.getAppType()))
		{
			GetuiUnBindAlis ptt1 = new GetuiUnBindAlis(ActionUtil.getSchoolID(),ActionUtil.getUserID(),request.getParameter("client_id"));
			Thread thread1 = new Thread(ptt1);
			thread1.start();
			return ResponseUtils.sendSuccess();
		} else {
			request.getSession().invalidate();
			ModelAndView model = new ModelAndView();
			String host = request.getRemoteHost();
			model.setViewName(host);
			return model;
		}
	}

	@RequestMapping(value="/sign")
	public @ResponseBody Object sign(HttpServletRequest request){
		UserVO vo = BeanUtil.formatToBean(UserVO.class);
		userService.signToXinge(vo);
//		AppVersionVO app = userService.getLastAppVersion(vo.getApp_type());
    	return ResponseUtils.sendSuccess();
	}

	@RequestMapping(value="/checkAppVerson")
	public @ResponseBody Object checkAppVerson(){
		UserVO vo = BeanUtil.formatMapToBean(ActionUtil.getParameterMap(), UserVO.class);
//		AppVersionVO app = userService.getLastAppVersion(vo.getApp_type());
//		if ((app==null) ||(app.getApp_version().equals(vo.getApp_version())))
//    		return ResponseUtils.sendSuccess(app);
		return ResponseUtils.sendFailure(MsgService.getMsg("NEW_VERSION"));
	}

	/**
	 * 将用户信息存入session中
	 * @param user
	 * @param request
	 */
	private void setAdminToSession(UserVO user,SchoolVO school, HttpServletRequest request) {
		request.getSession().setAttribute("time", ActionUtil.getSysTime().getTime()+"");
		request.getSession().setAttribute("user_name", user.getUser_name());
		request.getSession().setAttribute("user_id", user.getUser_id());
		request.getSession().setAttribute("phone", user.getPhone());
		request.getSession().setAttribute("head_url", StringUtil.isEmpty(user.getHead_url())?"images/img_user.jpg":user.getHead_url());
		request.getSession().setAttribute("school_id", school.getSchool_id());
		request.getSession().setAttribute("school_name", school.getSchool_name());
		request.getSession().setAttribute("app_pic_url", school.getOrganize_pic_url());
		request.getSession().setAttribute("app_type", ActionUtil.getAppType());
		request.getSession().setAttribute("user_type", ActionUtil.getUserType());
	}

	/**
	 * 将用户信息存入session中
	 * @param user
	 * @param request
	 */
	private void setSuperAdminToSession(UserVO user, HttpServletRequest request) {
		request.getSession().setAttribute("user_name", user.getUser_name());
		request.getSession().setAttribute("user_id", user.getUser_id());
		request.getSession().setAttribute("phone", user.getPhone());
		request.getSession().setAttribute("head_url", StringUtil.isEmpty(user.getHead_url())?"images/img_user.jpg":user.getHead_url());
//		request.getSession().setAttribute("last_login_ip", user.getLast_login_ip());
//		request.getSession().setAttribute("last_login_time", DateUtil.formatDateToString(user.getLast_login_time(),"yyyy-MM-dd HH:mm:ss"));
//		request.getSession().setAttribute("school_id", user.getSchool_id());
		request.getSession().setAttribute("school_name", "超级管理员管理界面");
		request.getSession().setAttribute("app_pic_url", "images/logo.png");
		request.getSession().setAttribute("app_type", ActionUtil.getAppType());
		request.getSession().setAttribute("user_type", ActionUtil.getUserType());
	}

	/**
	 * 将用户信息存入session中
	 * @param user
	 * @param request
	 */
	private void setUserInfoToSession(UserVO user, HttpServletRequest request) {
//		SchoolVO school = schoolService.getSchoolByID(user.getSchool_id());
		request.getSession().setAttribute("time", ActionUtil.getSysTime().getTime()+"");
		request.getSession().setAttribute("user_name", user.getUser_name());
		request.getSession().setAttribute("user_id", user.getUser_id());
		request.getSession().setAttribute("phone", user.getPhone());
		request.getSession().setAttribute("head_url", StringUtil.isEmpty(user.getHead_url())?"images/img_user.jpg":user.getHead_url());
//		request.getSession().setAttribute("last_login_ip", user.getLast_login_ip());
//		request.getSession().setAttribute("last_login_time", DateUtil.formatDateToString(user.getLast_login_time(),"yyyy-MM-dd HH:mm:ss"));
		request.getSession().setAttribute("school_id", ActionUtil.getSchoolID()+"");
//		request.getSession().setAttribute("school_name", school.getSchool_name());
		request.getSession().setAttribute("app_type", ActionUtil.getAppType());
		request.getSession().setAttribute("user_type", ActionUtil.getUserType());
	}
}
