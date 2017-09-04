package com.ninesky.classtao.picture.service;

import java.util.List;

import com.ninesky.classtao.picture.vo.PictureVO;

public interface PictureService {
	/**
	 * 添加条目
	 * @param vo
	 * @return
	 */
	public List<PictureVO> addPicture(PictureVO vo);
	/**
	 * 获取条目列表
	 * @param vo
	 * @return
	 */
	public List<PictureVO> getPictureList(PictureVO vo);
	/**
	 * 删除条目
	 * @param vo
	 */
	public void deletePicture(PictureVO vo);
}
