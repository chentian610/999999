package com.ninesky.classtao.schoolMenu.service.impl;

import com.ninesky.classtao.file.service.FileService;
import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.schoolMenu.service.SchoolMenuService;
import com.ninesky.classtao.schoolMenu.vo.SchoolMenuVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("schoolMenuServiceImpl")
public  class SchoolMenuServiceImpl implements SchoolMenuService{

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private RedisService redisService;

    @Autowired
    private FileService fileService;

	@Override
	public SchoolMenuVO addSchoolMenu(SchoolMenuVO vo) {
        if (StringUtil.isEmpty(vo.getMenu_name())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setCreate_by(ActionUtil.getUserID());
        vo.setCreate_date(ActionUtil.getSysTime());
        vo.setSchool_menu_id(dao.insertObjectReturnID("schoolMenuMap.insertSchoolMenu",vo));
        if (StringUtil.isEmpty(vo.getFile_list())) return vo;
        fileService.addFileList(vo.getFile_list(),vo.getSchool_menu_id(),DictConstants.MODULE_CODE_SCHOOL_MENU);
        return vo;
	}

	@Override
	public List<SchoolMenuVO> getSchoolMenuList(SchoolMenuVO vo) {
		List<SchoolMenuVO> list = dao.queryForList("schoolMenuMap.getSchoolMenuList",vo);
        if (ListUtil.isEmpty(list))  return list;
        for (SchoolMenuVO schoolMenuVO:list) {
            schoolMenuVO.setFile_list(BeanUtil.ListTojson(fileService.getFileList(new FileVO(ActionUtil.getSchoolID(),DictConstants.MODULE_CODE_SCHOOL_MENU,schoolMenuVO.getSchool_menu_id())),false));
        }
        return list;
	}

	@Override
	public void updateSchoolMenu(SchoolMenuVO vo) {
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("schoolMenuMap.updateSchoolMenu",vo);
        if (StringUtil.isEmpty(vo.getFile_list())) return;
        fileService.deleteFile(new FileVO(ActionUtil.getSchoolID(),DictConstants.MODULE_CODE_SCHOOL_MENU,vo.getSchool_menu_id()));
        fileService.addFileList(vo.getFile_list(),vo.getSchool_menu_id(),DictConstants.MODULE_CODE_SCHOOL_MENU);
	}

	@Override
	public void deleteSchoolMenu(Integer school_menu_id) {
        if (IntegerUtil.isEmpty(school_menu_id)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        dao.deleteObject("schoolMenuMap.deleteSchoolMenuByID",school_menu_id);
        fileService.deleteFile(new FileVO(ActionUtil.getSchoolID(),DictConstants.MODULE_CODE_SCHOOL_MENU,school_menu_id));
	}
}