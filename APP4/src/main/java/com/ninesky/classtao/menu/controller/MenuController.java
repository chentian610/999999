package com.ninesky.classtao.menu.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.ninesky.classtao.menu.vo.RoleMenuVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import com.ninesky.framework.SchoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.menu.service.MenuService;
import com.ninesky.classtao.menu.vo.MenuVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value = "menuAction")
public class MenuController extends BaseController{
	@Autowired
	private MenuService menuService;
	
	/**
	 * 获取功能列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getParentMenuList")
	public Object getParentMenuList(HttpServletRequest request){
		MenuVO vo = BeanUtil.formatToBean(MenuVO.class);
		if (DictConstants.USERTYPE_ADMIN.equals(vo.getUser_type())) {
			return menuService.getSchoolMenuList(vo);
		} else {
			return menuService.getMenuList(vo);
		}
	}

	/**
	 * 获取功能列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/updateRoleMenu")
	public Object updateRoleMenu(String menu_ids,HttpServletRequest request){
		menuService.updateRoleMenu(menu_ids);
		return  ResponseUtils.sendSuccess();
	}

	/**
	 * 获取功能列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getSchoolRoleMenu")
	@ResultField(includes = {"id","school_id","role_code","menu_id","is_active"})
	public Object getSchoolRoleMenu(HttpServletRequest request){
		RoleMenuVO vo = BeanUtil.formatToBean(RoleMenuVO.class);
		return  menuService.getSchoolRoleMenu(vo);
	}
}
