package com.ninesky.classtao.photo.service;

import java.util.HashMap;
import java.util.List;

import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.photo.vo.PhotoReceiveVO;
import com.ninesky.classtao.photo.vo.PhotoVO;

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
	 * @param user_id
	 * @return
	 */
	public Integer getUnreadCount(PhotoReceiveVO photo);
	/**
	 * 添加动态
	 * @param photoList
	 */
	public void addInformation(List<PhotoVO> photoList);
	
	public void insertDynamic(HashMap<String, String> dataMap, List<ReceiveVO> receivelist);
}
