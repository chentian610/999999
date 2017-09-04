package com.ninesky.classtao.schoolMenu.controller;


import com.ninesky.classtao.schoolMenu.service.SchoolMenuService;
import com.ninesky.classtao.schoolMenu.vo.SchoolMenuVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("schoolMenuAction")
public class SchoolMenuController extends BaseController{

    @Autowired
    private SchoolMenuService schoolMenuService;

    /**
     * 获取一周菜谱
     */
    @RequestMapping(value="/getSchoolMenuList")
    @ResultField(includes = {"school_menu_id","school_id","menu_date","menu_name","file_list","monday_data"})
    public @ResponseBody Object getSchoolMenuList(HttpServletRequest request){
        SchoolMenuVO vo = BeanUtil.formatToBean(SchoolMenuVO.class);
        return schoolMenuService.getSchoolMenuList(vo);
    }


    /**
     * 添加一周菜谱
     */
    @RequestMapping(value="/addSchoolMenu")
    public @ResponseBody Object addSchoolMenu(HttpServletRequest request){
        SchoolMenuVO vo = BeanUtil.formatToBean(SchoolMenuVO.class);
        return schoolMenuService.addSchoolMenu(vo);
    }

    /**
     * 修改一周菜谱
     */
    @RequestMapping(value="/updateSchoolMenu")
    public @ResponseBody Object updateSchoolMenu(HttpServletRequest request){
        SchoolMenuVO vo = BeanUtil.formatToBean(SchoolMenuVO.class);
        schoolMenuService.updateSchoolMenu(vo);
        return ResponseUtils.sendSuccess();
    }

    /**
     * 修改一周菜谱
     */
    @RequestMapping(value="/deleteSchoolMenu")
    public @ResponseBody Object deleteSchoolMenu(Integer school_menu_id,HttpServletRequest request){
        schoolMenuService.deleteSchoolMenu(school_menu_id);
        return ResponseUtils.sendSuccess();
    }
}
