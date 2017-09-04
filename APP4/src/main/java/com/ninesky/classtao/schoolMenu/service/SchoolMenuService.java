package com.ninesky.classtao.schoolMenu.service;

import com.ninesky.classtao.schoolMenu.vo.SchoolMenuVO;

import java.util.List;

public interface SchoolMenuService {
	/**
	 * 添加学校菜谱
	 * @param vo
	 */
	public SchoolMenuVO addSchoolMenu(SchoolMenuVO vo);

	/**
	 * 获取学校菜谱列表
	 * @param vo
	 * @return
	 */
	public List<SchoolMenuVO> getSchoolMenuList(SchoolMenuVO vo);

	/**
	 * 修改学校菜谱
	 * @param vo
	 */
	public void updateSchoolMenu(SchoolMenuVO vo);

	/**
	 * 删除学校菜谱
	 * @param vo
	 */
	public void deleteSchoolMenu(Integer school_menu_id);
}
