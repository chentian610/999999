package com.ninesky.classtao.school.service;


import java.util.List;

import com.ninesky.classtao.school.vo.BedVO;
import com.ninesky.classtao.school.vo.BedroomVO;

public interface BedroomService {
	/**
	 * 添加寝室
	 */
	public List<BedroomVO> insertBedroom(BedroomVO vo);
	/**
	 * 设置寝室床位人员
	 */
	public void bindBedForStudent(BedVO vo);
	
	/**
	 * 寝室添加人员
	 * @param vo
	 */
	public void addStudentForBed(BedVO vo);
	
	/**
	 * 获取寝室床位人员信息
	 */
	public List<BedVO> getBedListByBedroomId(Integer bedroom_id);
	/**
	 * 删除寝室床位人员信息
	 */
	public void deleteBedOfStudent(BedVO vo);
	/**
	 * 根据bedroom_id查询寝室信息
	 */
	public BedroomVO getBedroomInfoById(Integer bedroom_id);
	/**
	 * 根据前缀，楼层查询寝室信息
	 */
	public List<BedroomVO> getBedroomInfoByFloor(String bedroom);
	
	public List<BedVO> getBedPositionList(Integer bedroom_id);//获取寝室学生列表
	
	public List<BedroomVO> getBedroomList(BedroomVO vo);//点击楼，显示寝室
	
	public List<BedroomVO> getBedroomListOfManager();//后台寝室设置页面
	
	public BedroomVO addBedroom(BedroomVO vo);//后台寝室设置页面（添加寝室）
	
	public void deleteBedroom(Integer bedroom_id);//后台寝室设置页面（删除寝室）
	
	public String addStudentListForBed(String item_list);//批量添加寝室人员
}
