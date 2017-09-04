package com.ninesky.classtao.menu.service.impl;

import com.ninesky.classtao.menu.service.MenuService;
import com.ninesky.classtao.menu.vo.MenuModuleVO;
import com.ninesky.classtao.menu.vo.MenuVO;
import com.ninesky.classtao.menu.vo.RoleMenuVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.template.service.TemplateService;
import com.ninesky.classtao.template.vo.ModuleVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.SchoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "MenuServiceImpl")
public class MenuServiceImpl implements MenuService {
	@Autowired
	private GeneralDAO dao;

	@Autowired
	private DictService dictService;

	@Autowired
	private TemplateService templateService;

	@Override
	public MenuVO addMenu(MenuVO vo) {
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObjectReturnID("MenuMap.insertMenu",vo);
		return vo;
	}

	@Override
	public List<MenuVO> getMenuList(MenuVO vo) {
		vo.setRole_code(null);
		vo.setParent_id(DictConstants.FALSE);
		vo.setPartner_code(SchoolConfig.getProperty(Constants.PARTNER_CODE));
		List<MenuVO> menuList = dao.queryForList("menuMap.getMenuList", vo);
		List<MenuVO> list1 = new ArrayList<MenuVO>();
		for (MenuVO menu:menuList){
			MenuVO menuVO = new MenuVO();
			menuVO.setParent_id(menu.getMenu_id());
			menuVO.setIs_active(vo.getIs_active());
			List<MenuVO> list = dao.queryForList("menuMap.getMenuList", menuVO);
			if (ListUtil.isEmpty(list)) continue;
			list1.addAll(list);
		}
		menuList.addAll(list1);
		return menuList;
	}

	@Override
	public List<MenuVO> getSchoolMenuList(MenuVO vo) {
		vo.setUser_type(null);
		vo.setPartner_code(SchoolConfig.getProperty(Constants.PARTNER_CODE));
		return dao.queryForList("menuMap.getMenuList", vo);
	}

	@Override
	public void updateRoleMenu(String menu_ids) {
		if (StringUtil.isEmpty(menu_ids)) return;
		String[] menuIDs = menu_ids.split(",");
		List<RoleMenuVO> list = new ArrayList<RoleMenuVO>();
		RoleMenuVO vo = new RoleMenuVO(ActionUtil.getSchoolID(),ActionUtil.getParameter("role_code"));
		for (String menu_id:menuIDs) {
			vo.setMenu_id(IntegerUtil.getValue(menu_id));
			RoleMenuVO roleMenuVO = dao.queryObject("roleMenuMap.getSchoolRoleMenu",vo);
			if (roleMenuVO != null) {
				dao.deleteObject("roleMenuMap.deleteRoleMenu", new RoleMenuVO(roleMenuVO.getId()));
				continue;
			}
			list.add(new RoleMenuVO(ActionUtil.getSchoolID(),ActionUtil.getParameter("role_code"),IntegerUtil.getValue(menu_id)));
		}
		insertRoleMenuBath(list);
	}

	private void insertRoleMenuBath(List<RoleMenuVO> list) {
		if (ListUtil.isEmpty(list)) return;
		dao.insertObject("roleMenuMap.insertRoleMenuBath",list);
	}

	@Override
	public void removeSchoolRoleMenuInfo(String dict_code){
		dao.deleteObject("roleMenuMap.deleteRoleMenu",new RoleMenuVO(ActionUtil.getSchoolID(),dict_code));
	}

	@Override
	public void initSchoolRoleMenu(Integer school_id){
		if (IntegerUtil.isEmpty(school_id)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		DictVO dictVO = new DictVO();
		dictVO.setIs_configure(DictConstants.TRUE);
		dictVO.setSchool_id(0);
		dictVO.setDict_group(DictConstants.DICT_TEACHER);
		List<DictVO> list=dao.queryForList("dictMap.getDictList", dictVO);//通用的字典
		list.add(new DictVO(DictConstants.DICT_TEACHER,DictConstants.USERTYPE_ADMIN));//添加后台学校管理员
		List<MenuModuleVO> mList = dao.queryForList("menuModuleMap.getMenuModuleList",new MenuModuleVO(DictConstants.TRUE));//获取默认菜单列表
		if (ListUtil.isEmpty(mList)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		List<RoleMenuVO> rList = new ArrayList<RoleMenuVO>();
		for (DictVO vo : list) {
			if (DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDict_code()) || DictConstants.DICT_TEACHER_ADVISER.equals(vo.getDict_code())) continue;
			for (MenuModuleVO moduleVO : mList) {
				if (!DictConstants.USERTYPE_ADMIN.equals(vo.getDict_code())) {
					if (DictConstants.USERTYPE_ADMIN.equals(moduleVO.getUser_type())) continue;
					rList.add(new RoleMenuVO(school_id, vo.getDict_code(), moduleVO.getMenu_id()));
				} else rList.add(new RoleMenuVO(school_id, vo.getDict_code(), moduleVO.getMenu_id()));
			}
			if (DictConstants.USERTYPE_ADMIN.equals(vo.getDict_code())) {
				List<MenuModuleVO> smList = dao.queryForList("menuModuleMap.getSchoolMenuModuleList",new MenuModuleVO(school_id,DictConstants.TRUE));//获取学校相应模块菜单列表
				if (ListUtil.isEmpty(smList)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
				for (MenuModuleVO moduleVO : smList) {
					rList.add(new RoleMenuVO(school_id,vo.getDict_code(),moduleVO.getMenu_id()));
				}
			}
		}
		insertRoleMenuBath(rList);
	}

	public List<RoleMenuVO> getSchoolRoleMenu(RoleMenuVO vo) {
		return dao.queryForList("roleMenuMap.getSchoolRoleMenu",vo);
	}

	public void updateRoleMenu(SchoolVO vo) {
		if(StringUtil.isEmpty(vo.getModule_ids())) return;
		String[] module_ids = vo.getModule_ids().split(",");
		List<RoleMenuVO> roleList = new ArrayList<RoleMenuVO>();
		for(String module_id : module_ids) {
			ModuleVO module = templateService.getModuleById(Integer.parseInt(module_id));
			if(module == null) return;
			List<RoleMenuVO> list = dao.queryForList("roleMenuMap.getSchoolRoleMenuByModule",new RoleMenuVO(vo.getSchool_id(),module.getModule_code(),module.getUser_type()));
			if (ListUtil.isEmpty(list)) {
				List<RoleMenuVO> list2  = dao.queryForList("menuModuleMap.getMenuModuleListByModule",new MenuModuleVO(module.getModule_code(),module.getUser_type()));
				if (ListUtil.isEmpty(list2)) continue;
				for (RoleMenuVO roleMenuVO:list2) {
					roleMenuVO.setSchool_id(vo.getSchool_id());
					roleMenuVO.setRole_code(DictConstants.USERTYPE_ADMIN);
					roleMenuVO.setCreate_by(ActionUtil.getUserID());
					roleMenuVO.setCreate_date(ActionUtil.getSysTime());
				}
				roleList.addAll(list2);
			} else {
				for (RoleMenuVO roleMenuVO1 : list) {
					dao.deleteObject("roleMenuMap.deleteRoleMenu", roleMenuVO1);
				}
			}
		}
		insertRoleMenuBath(roleList);
	}
}
