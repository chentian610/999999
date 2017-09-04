package com.ninesky.classtao.shiro;

import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yangqj on 2017/4/21.
 */
public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    private String ss;

    @Resource
    private UserService userService;

//    @Resource
//    private ResourcesService1 resourcesService;

    /**
     * 授权，提供用户信息返回权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("权限认证方法1：MyShiroRealm.doGetAuthorizationInfo().................");
        UserVO user = (UserVO) SecurityUtils.getSubject().getPrincipal();
        Integer user_id = user.getUser_id();
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        //根据用户ID查询角色（role），放入到Authorization里。
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		List<SysRole> roleList = sysRoleService.selectByMap(map);
		Set<String> roleSet = new HashSet<String>();
		for(SysRole role : roleList){
			roleSet.add(role.getType());
		}*/
        Set<String> roleSet = new HashSet<String>();
        roleSet.add("100002");
        roleSet.add("admin");
        info.setRoles(roleSet);
        //根据用户ID查询权限（permission），放入到Authorization里。
		/*List<SysPermission> permissionList = sysPermissionService.selectByMap(map);
		Set<String> permissionSet = new HashSet<String>();
		for(SysPermission Permission : permissionList){
			permissionSet.add(Permission.getName());
		}*/
        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("权限添加");
        permissionSet.add("权限删除");
        info.setStringPermissions(permissionSet);
        return info;
    }

    /**
     * 认证，AuthenticationInfo代表了用户的角色信息集合
     * @param
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("登录验证：MyShiroRealm.doGetAuthenticationInfo().................");
        //获取用户的输入的账号.
        MyShiroToken myShiroToken = (MyShiroToken) token;
        UserVO user = userService.getUserByPhone(myShiroToken.getPhone());
        if(user==null || !user.getPass_word().equals(myShiroToken.getPass_word())) {
            throw new AccountException("帐号或密码不正确！");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                user.getPass_word(), //密码
                ByteSource.Util.bytes(myShiroToken.getPass_word()),
                getName()  //realm name
        );
        // 当验证都通过后，把用户信息放在session里
//        Session session = SecurityUtils.getSubject().getSession();
//        session.setAttribute("userSession", user);
//        session.setAttribute("userSessionId", user.getUser_id());
        return authenticationInfo;
    }

    /**
     * 指定principalCollection 清除
     */
  /*  public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {

        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
*/
}
