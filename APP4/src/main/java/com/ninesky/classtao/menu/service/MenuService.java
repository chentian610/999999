package com.ninesky.classtao.menu.service;

import java.util.List;

import com.ninesky.classtao.menu.vo.MenuVO;
import com.ninesky.classtao.menu.vo.RoleMenuVO;
import com.ninesky.classtao.school.vo.SchoolVO;

public interface MenuService{
	/**
	 * 添加功能
	 * @param vo
	 */
	public MenuVO addMenu(MenuVO vo);
	
	/**
	 * 获取功能列表
	 * @param vo
	 * @return
	 */
	public List<MenuVO> getMenuList(MenuVO vo);

	/**
	 * 清空相应学校角色菜单信息
	 * @param dict_code
	 */
	public void removeSchoolRoleMenuInfo(String dict_code);

	/**
	 * 修改校园学校角色菜单信息
	 * @param menu_ids
	 */
	public void updateRoleMenu(String menu_ids);

	/**
	 * 初始化学校角色菜单
	 * @param school_id
	 */
	public void initSchoolRoleMenu(Integer school_id);

	/**
	 * 获取学校相应菜单
	 * @param vo
	 * @return
	 */
	public List<MenuVO> getSchoolMenuList(MenuVO vo);

	/**
	 * 获取学校菜单权限列表
	 * @param vo
	 * @return
	 */
	public List<RoleMenuVO> getSchoolRoleMenu(RoleMenuVO vo);

	/**
	 * 批量插入角色信息
	 * @param SchoolVO
	 */
	public void updateRoleMenu(SchoolVO vo);
}
