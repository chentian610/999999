package com.ninesky.classtao.picture.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ninesky.framework.BusinessException;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.classtao.picture.service.PictureService;
import com.ninesky.classtao.picture.vo.PictureVO;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.GeneralDAO;
@Service("PictureServiceImpl")
public class PictureServiceImpl implements PictureService {
	@Resource
	private GeneralDAO dao;
	
	/**
	 * 添加条目
	 * @param vo
	 * @return
	 */
	public List<PictureVO> addPicture(PictureVO vo){
		List<PictureVO> returnList = new ArrayList<PictureVO>();
		if(StringUtil.isEmpty(vo.getFile_list())) 
			throw new BusinessException("没有照片可以添加...");
		List<FileVO> fileList = BeanUtil.jsonToList(vo.getFile_list(), FileVO.class);
		for(FileVO file : fileList){
			PictureVO picture = new PictureVO();
			picture.setSchool_id(vo.getSchool_id());
			picture.setClass_id(vo.getClass_id());
			picture.setPicture_type(vo.getPicture_type());
			picture.setTitle(vo.getTitle());	
			picture.setPicture_url(file.getFile_url());
			picture.setPicture_resize_url(file.getFile_resize_url());
			picture.setAdd_date(vo.getAdd_date());
			picture.setCreate_by(vo.getCreate_by());
			picture.setCreate_date(vo.getCreate_date());
			returnList.add(picture);
		}
		dao.insertObject("pictureMap.insertPictureBatch", returnList);
		return returnList;
	}
	
	/**
	 * 获取条目列表
	 * @param vo
	 * @return
	 */
	public List<PictureVO> getPictureList(PictureVO vo){
		if(StringUtil.isEmpty(vo.getPicture_type()))
			throw new BusinessException("图片类型不能为空...");
		return dao.queryForList("pictureMap.getPictureList", vo);
	}
	
	/**
	 * 删除条目
	 * @param vo
	 */
	public void deletePicture(PictureVO vo){
		dao.deleteObject("pictureMap.deletePicture", vo);
	}
}
