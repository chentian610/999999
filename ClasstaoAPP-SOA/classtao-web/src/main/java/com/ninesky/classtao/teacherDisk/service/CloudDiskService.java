package com.ninesky.classtao.teacherDisk.service;

import java.util.List;

import com.ninesky.classtao.teacherDisk.vo.DiskVO;

public interface CloudDiskService {

	/**
	 * 获取云盘列表
	 */
	public List<?> getCloudDiskList(DiskVO disk);
	
	
	/**
	 * 获取所有文件类型
	 */
	public List<DiskVO> getFileType(DiskVO vo);
	
	/**
	 * 添加文件到云盘（或新建文件夹）
	 */
	public DiskVO addCloudDisk(DiskVO vo);
	
	/**
	 * 修改云盘文件
	 */
	public void updateCloudDisk(DiskVO vo);
	
	/**
	 * 修改云盘文件名
	 */
	public void updateCloudDiskName(DiskVO vo);
	
	/**
	 * 删除文件
	 */
	public void deleteCloudDisk(List<DiskVO> list);
	
	/**
	 * 发送文件到班级
	 */
	public void sendCloudDisk(DiskVO vo, String receive_list);
}
