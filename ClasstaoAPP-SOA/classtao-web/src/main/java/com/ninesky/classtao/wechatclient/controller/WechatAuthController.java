package com.ninesky.classtao.wechatclient.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserSnsVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.classtao.wechatclient.vo.WechatUserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.SystemConfig;

@RestController
@RequestMapping(value="wechatAuthAction")
public class WechatAuthController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(WechatAuthController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private WxApiService wxApiService;
	
/*	@RequestMapping(value="/test")
	public @ResponseBody Object test(){
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("module_code", "009005");
		dataMap.put("module_pkid", "4396");
		dataMap.put("student_id", "543");
		dataMap.put("attend_date", "2016年12月2日 8点20分");
		dataMap.put("class_name", "三（4）班");
		dataMap.put("student_name", "王雯琪");
		dataMap.put("info_content", "早自习考勤中迟到");
		dataMap.put("client_url", "pdetail.html");
		wechatService.pushAttendanceMessage(dataMap, 543, 1030);
		
		return ResponseUtils.sendSuccess(dataMap); 
	}*/
	
	@RequestMapping(value="/login")
	public @ResponseBody Object login(HttpServletRequest request){
		
		String accountId = request.getParameter("accountId");
		String studentId = request.getParameter("studentId");
		WxAccountVO account = wxAccountService.getAccountById(Integer.parseInt(accountId));
		if(account == null){
			throw new BusinessException("未找到授权公众账号");
		}
		String callbackUrl = SystemConfig.getProperty("WECHAT_PLATFORM_DOMAIN") + "/wechatAuthAction/callback?accountId=" + accountId;
		if(!StringUtils.isEmpty(studentId)){
			callbackUrl += "&studentId=" + studentId;
		}		
		String oauthUrl = "";
		try {
			oauthUrl = Constants.WECHAT_OAUTH2_URL.replaceAll("APPID", account.getAccount_appid())
											   .replaceAll("REDIRECT_URI", URLEncoder.encode(callbackUrl, "utf-8"))
											   .replaceAll("SCOPE", "snsapi_userinfo")
											   .replaceAll("STATE", "wechat_parent_oauth_login");
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(e.getMessage());
		}
		
		return new ModelAndView("redirect:" + oauthUrl);
	}
	
	//oauth授权回调的地址
	@RequestMapping(value="/callback")
	public @ResponseBody Object callback(HttpServletRequest request){
		
		String code = request.getParameter("code");
		String accountId = request.getParameter("accountId");
		String studentId = request.getParameter("studentId");
		logger.info("-----【callback】-----得到的oauth2.0  code值=" + code);
		
		WxAccountVO account = wxAccountService.getAccountById(Integer.parseInt(accountId));
		if(account == null){
			throw new BusinessException("未找到授权公众账号");
		}
		String appId = account.getAccount_appid();
		String secret = account.getAccount_appsecret();
		if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(secret)){
			throw new BusinessException("公众账号参数配置错误");
		}
		
		//获取微信用户
		WechatUserVO wxUser = wechatService.getWechatUser(appId, secret, code);
		logger.info("返回微信用户：" + wxUser.toString());
		ModelAndView model = new ModelAndView();
		
		String oauthLoginUrl = SystemConfig.getProperty(Constants.APP4_PLATFORM_CONFIG_KEY) + "/wechatAuthAction/doOauthLogin?accountId="+accountId
		+ "&school_id=" + account.getSchool_id() 
		+ "&openId=" + wxUser.getOpen_id()
		+ "&nickName=" + wxUser.getNick_name()
		+ "&headImgUrl=" + wxUser.getHead_img_url()
		+ "&sex=" + wxUser.getSex();
		if(!StringUtils.isEmpty(studentId)){
			oauthLoginUrl += "&studentId=" + studentId;
		}
		
		request.setAttribute("oauthLoginUrl", oauthLoginUrl);
		model.setViewName("../wechatclient/parent/main/callback.jsp");
		return model;
	}
	
	
	@RequestMapping(value="/doOauthLogin")
	public @ResponseBody Object doOauthLogin(HttpServletRequest request){
		
		String accountId = request.getParameter("accountId");
		String studentId = request.getParameter("studentId");
		WxAccountVO account = wxAccountService.getAccountById(Integer.parseInt(accountId));
		if(account == null){
			throw new BusinessException("未找到授权公众账号");
		}
		WechatUserVO wxUser = new WechatUserVO();
		wxUser.setOpen_id(request.getParameter("openId"));
		wxUser.setNick_name(request.getParameter("nickName"));
		wxUser.setHead_img_url(request.getParameter("headImgUrl"));
		wxUser.setSex(Integer.parseInt(request.getParameter("sex")));
		
		ModelAndView model = new ModelAndView();
		UserSnsVO uSns = new UserSnsVO();
		uSns.setSns_type(Constants.SNS_TYPE_WECHAT);
		uSns.setAccount(wxUser.getOpen_id());
		List<UserSnsVO> snsList = userService.getUserSnsList(uSns);
		if(snsList != null && snsList.size() > 0){
			//已经绑定：将绑定的用户做登录操作
			UserSnsVO fUserSns = snsList.get(0);
			UserVO user = userService.getUserByID(fUserSns.getUser_id());
			//登录用户类型为家长
			user.setUser_type(DictConstants.USERTYPE_PARENT);
			//登录学校为公众号对应的学校
			user.setSchool_id(account.getSchool_id());
			setUserInfoToSession(user, request);
			String redirectUrl = "redirect:/wechatclient/parent/main/load.html?schoolId=" + account.getSchool_id();
			if(!StringUtils.isEmpty(studentId)){
				redirectUrl += "&studentId=" + studentId;
			}
			model.setViewName(redirectUrl);
		}else{
			//尚未绑定进入绑定页
			request.getSession().setAttribute("session_wechat_user", wxUser);
			model.setViewName("redirect:/wechatclient/parent/main/bind.html?schoolId=" + account.getSchool_id());
		}
		return model;
	}
	
	@RequestMapping(value="/getLoginUser")
	public @ResponseBody Object getLoginUser(HttpServletRequest request){
		Object userId = request.getSession().getAttribute("user_id");
		if(userId == null){
			return ResponseUtils.sendFailure("用户尚未登录，请先进行登录操作");
		}
		UserVO userVO = userService.getUserByID((Integer)userId);
		return ResponseUtils.sendSuccess(userVO);
	} 
	
	@RequestMapping(value="/bind")
	public @ResponseBody Object bind(HttpServletRequest request){
		int school_id = Integer.parseInt(request.getParameter("school_id"));
		WechatUserVO wxUser = (WechatUserVO) request.getSession().getAttribute("session_wechat_user");
		//获取前端返回的数据
		UserVO vo = BeanUtil.formatMapToBean(ActionUtil.getParameterMap(), UserVO.class);
		UserVO uvo = new  UserVO();

		if (!userService.checkValidateCode(ActionUtil.getParameterMap())){
			throw new BusinessException("验证码已经失效，请重新获取验证码....");
		}
		 
		uvo = userService.getUserByPhone(vo.getPhone());//判断该用户是否已注册过
		if (uvo != null){
			userService.validateUser(uvo);//验证该用户的状态是否正常
		}else{
			vo.setHead_url(wxUser.getHead_img_url());//设置用户的默认头像
			if(wxUser.getSex() == 1){//男
				vo.setSex(0);
			}else if(wxUser.getSex() == 2){//女
				vo.setSex(1);
			}
			vo.setUser_name(filterEmoji(wxUser.getNick_name()));
			uvo = userService.insertUser(vo,school_id);//若无该用户，就自动注册
		}
		 
		//用户信息与微信绑定
		UserSnsVO fUserSns = new UserSnsVO();
		fUserSns.setUser_id(uvo.getUser_id());
		fUserSns.setSns_type(Constants.SNS_TYPE_WECHAT);
		fUserSns.setAccount(wxUser.getOpen_id());
		userService.addUserSns(fUserSns);
 
		//登录用户类型为家长
		uvo.setUser_type(DictConstants.USERTYPE_PARENT);
		//登录学校为公众号对应的学校
		uvo.setSchool_id(school_id);
		setUserInfoToSession(uvo, request);
		return ResponseUtils.sendSuccess();
	}
	
	
	@RequestMapping(value="/jsapiTicket")
	public @ResponseBody Object jsapiTicket(HttpServletRequest request){
		String schoolId = request.getParameter("schoolId");
		String url = request.getParameter("url");
		WxAccountVO account = wxAccountService.getAccountBySchool(Integer.parseInt(schoolId));
		Map<String, String> ticket = wxApiService.loadJsapiTicket(account.getAccount_id(), url);
		return ResponseUtils.sendSuccess(ticket);
	}
	
	@RequestMapping(value="/accessToken")
	public @ResponseBody Object accessToken(HttpServletRequest request){
		String schoolId = request.getParameter("schoolId");
		WxAccountVO account = wxAccountService.getAccountBySchool(Integer.parseInt(schoolId));
		String accessToken = wxApiService.loadAccesstoken(account.getAccount_id());
		return ResponseUtils.sendSuccess(accessToken);
	}
	
	/**
	 * 将用户信息存入session中
	 * @param user
	 * @param request
	 */
	private void setUserInfoToSession(UserVO user, HttpServletRequest request) {
		request.getSession().setAttribute("user_name", user.getUser_name());
		request.getSession().setAttribute("user_id", user.getUser_id());
		request.getSession().setAttribute("phone", user.getPhone());
		request.getSession().setAttribute("head_url", StringUtil.isEmpty(user.getHead_url())?"images/img_user.jpg":user.getHead_url());
		request.getSession().setAttribute("school_id", user.getSchool_id());
		request.getSession().setAttribute("user_type", user.getUser_type());
	}
	
	/**
	 * 将微信昵称中Emoji表情过滤
	 * @param source
	 */
	private static String filterEmoji(String source) { 
       if(source != null){
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()){
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
       }
       return source; 
   }
}
