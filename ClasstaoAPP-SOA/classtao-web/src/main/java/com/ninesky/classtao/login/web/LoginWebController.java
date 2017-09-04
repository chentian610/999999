package com.ninesky.classtao.login.web;

import com.ninesky.classtao.agent.service.AgentService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.shiro.MyShiroToken;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
class LoginWebController {
    private static final Logger logger = LoggerFactory.getLogger(LoginWebController.class);

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

    /**
     * 管理员登录后台管理
     * @param request
     * @return
     */
    @PostMapping(value="loginWeb/login")
    public String login(UserVO vo, HttpServletRequest request) {
        HashMap<String , Object> payload=new HashMap<String, Object>();
//        payload.put("uid", loginUser.getUser_id());//用户ID
//        payload.put("phone", vo.getPhone());
//        payload.put("pass_word",loginUser.getPass_word());
//        payload.put("iat", ActionUtil.getSysTime());//生成时间
//        payload.put("ext",ActionUtil.getSysTime().getTime()+1000*60*60*24*7);//过期时间1小时
//        String jwt = JWTUtil.createToken(payload);
//        loginUser.setToken(jwt);
        MyShiroToken myShiroToken = new MyShiroToken(vo.getPhone(), vo.getPass_word(),null);
        SecurityUtils.getSubject().login(myShiroToken);
        request.setAttribute("user",vo);
        return "default";
    }

    /**
     * 管理员登录后台管理
     * @param request
     * @return
     */
    @GetMapping(value="loginWeb/{module_name}/{page_name}")
    public ModelAndView openUrlFromMenu(@PathVariable String module_name, @PathVariable String page_name, HttpServletRequest request) {
        System.out.println(request.getParameter("d"));
        Map<String,String> paramMap = getParamFromRequest(request);
        return new ModelAndView(module_name+"/"+page_name,paramMap);
    }


    /**
     * 将请求里面的所有参数设置到Param中
     * @return
     */
    private HashMap<String, String> getParamFromRequest(HttpServletRequest request) {
        HashMap<String, String> param = new HashMap<String, String>();
        Map<?, ?> map=request.getParameterMap();
        for (Object key : map.keySet()) {
            param.put(key.toString(), request.getParameter(key.toString()));
        }
        return param;
    }
}
