package com.ninesky.classtao.login.controller;

import com.ninesky.classtao.agent.service.AgentService;
import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.classtao.login.service.QRcodeContext;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserRoleVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.ListUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value="scanAction")
public class ScanController extends BaseController{
	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private AgentService agentService;

	private static Logger logger = LoggerFactory.getLogger(ScanController.class);
	

	@RequestMapping(value="/createCode")
	public Object createCode(HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		String school_id = request.getParameter("school_id");
		//注册订阅
		QRcodeContext.registerSub(uuid);
		try {
			//1分钟重连一次
			logger.error(uuid+"：开始等待用户APP扫描");
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			logger.error(uuid+"：用户已经使用APP扫描！");
			//去数据库查询
			UserVO user = QRcodeContext.getUserInfo(uuid);
			if (!(user.getSchool_id()+"").equals(school_id)) throw new BusinessException("学校不匹配，扫码登录失败！");
			user.setUser_name(redisService.getUserName(user.getSchool_id(), DictConstants.USERTYPE_TEACHER, user.getUser_id(), null));
			user.setHead_url(redisService.getUserHeadUrl(user.getSchool_id(), DictConstants.USERTYPE_TEACHER, user.getUser_id(), null));
			return user;
		} finally {
			logger.error(uuid+"：等待结束，用户没有进行扫描操作！");
			//释放订阅
			QRcodeContext.removeSub(uuid);
		}
		throw new BusinessException("二维码已经过期，请重新扫描！");
	}
	
	@RequestMapping(value="/secondConfirm")
	public Object secondConfirm(HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		//注册订阅
		QRcodeContext.registerSub(uuid);
		UserVO user = null;
		try {
			//1分钟重连一次
			logger.error(uuid+"：开始等待用户二次确认");
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			logger.error(uuid+"：用户已经完成二次确认！");
			//去数据库查询
			UserVO userParam = QRcodeContext.getUserInfo(uuid);
			if (userParam==null || userParam.getUser_id() ==null) 
				throw new BusinessException("二次确认的用户信息不正确，登录失败！");
			user = userService.getUserByID(userParam.getUser_id());
			request.getSession().setAttribute("phone", user.getPhone());
			request.getSession().setAttribute("school_id", ActionUtil.getSchoolID());
			request.getSession().setAttribute("user_id", user.getUser_id());
			UserRoleVO roleVO = new UserRoleVO();
			roleVO.setPhone(user.getPhone());
			roleVO.setSchool_id(ActionUtil.getSchoolID());
			roleVO.setUser_type( request.getParameter("user_type"));
			if (DictConstants.USERTYPE_AGENT.equals(roleVO.getUser_type())) {
				AgentVO agent = agentService.getAgentByPhone(user.getPhone());
				if (agent == null) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_AGENT"));
				else if (DictConstants.STATUS_STOP.equals(agent.getIs_enable())) throw new BusinessException(MsgService.getMsg("AGENT_USER_DISABLE"));
			} else {
				UserRoleVO uRoleVO = userService.getUserRoleByCondition(roleVO);
				if (DictConstants.USERTYPE_ADMIN.equals(roleVO.getUser_type())) {
					if (uRoleVO == null) {
						List<TeacherVO> tlist = redisService.getSchoolTeacherRoleCodes(roleVO.getPhone());
						if (ListUtil.isEmpty(tlist)) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_ADMIN_TEACHER"));
						request.getSession().setAttribute("role_code", BeanUtil.ListTojson(tlist,false));
					} else request.getSession().setAttribute("role_code", DictConstants.USERTYPE_ADMIN);
				} else if (DictConstants.USERTYPE_SUPER.equals(roleVO.getUser_type())) {
					if (uRoleVO == null) throw new BusinessException(MsgService.getMsg("USER_NO_AUTH_SUPER"));
				}
			}
		} finally {
			logger.error(uuid+"：等待结束，用户没有进行二次确认！");
			//释放订阅
			QRcodeContext.removeSub(uuid);
		}
		if (user==null)	throw new BusinessException("没有在规定时间内完成登录操作，请重新扫描登录！");
		return user;
	}
	
	
	@RequestMapping(value="/scan")
	public Object scan(HttpServletRequest request,UserVO user){
		String uuid = request.getParameter("uuid");
		logger.error(uuid+"：已经扫描！");
		QRcodeContext.pubToClient(uuid,user);
		return user;
	}
	
	/**
	 * 管理员登录后台管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login")
	public Object login(HttpServletRequest request,UserVO user) {
		String uuid = request.getParameter("uuid");
		logger.error(uuid+"：已经登录！");
		QRcodeContext.pubToClient(uuid,user);
		return user;
	}
}
