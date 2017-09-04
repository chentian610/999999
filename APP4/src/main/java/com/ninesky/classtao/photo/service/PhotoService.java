package com.ninesky.classtao.photo.service;

import com.ninesky.classtao.photo.vo.PhotoCommentVO;
import com.ninesky.classtao.photo.vo.PhotoReceiveVO;
import com.ninesky.classtao.photo.vo.PhotoVO;
import com.ninesky.common.vo.ReceiveVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PhotoService {
	/**
	 * 上传相片
	 * @param vo
	 */
	public void insertPhoto(PhotoVO vo);
	/**
	 * 获取相片列表
	 * @param photo
	 * @return
	 */
	public List<?> getPhotoList(PhotoReceiveVO photo);
	/**
	 * 删除相片
	 * @param vo
	 * @param parentPath
	 */
	public void deletePhoto(PhotoVO vo, String parentPath);
	
	
	/**
	 * 获取相册未读数量
	 * @param photo
	 * @return
	 */
	public Integer getUnreadCount(PhotoReceiveVO photo);
	/**
	 * 添加动态
	 * @param photoList
	 */
	public void addInformation(List<PhotoVO> photoList);
	
	public void insertDynamic(HashMap<String, String> dataMap, List<ReceiveVO> receivelist);

	/**
	 * 添加用户点评
	 * @param vo
	 */
	public void insertPhotoComment(PhotoCommentVO vo);

	/**
	 * 添加用户点赞
	 * @param vo
	 */
	public void insertPhotoPointPraise(PhotoCommentVO vo);

	public List<PhotoVO> getClassCircleList(Map<String,String> paramMap);
}
