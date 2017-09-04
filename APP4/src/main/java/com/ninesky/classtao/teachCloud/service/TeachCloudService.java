package com.ninesky.classtao.teachCloud.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.teachCloud.vo.SourceVO;

public interface TeachCloudService {
	
	/**
	 * 添加教师资源推荐
	 * @param vo
	 */
	public void addSource(SourceVO vo);
	
	/**
	 * 获取教师资源推荐列表
	 * @param paramMap
	 */
	public List<?> getSourceList(Map<String, String> paramMap);
	
	/**
	 * 接收组添加到teach_group中
	 * @param vo
	 * @param receive_list
	 */
	public void addSourceGroup(SourceVO vo, String receive_list) ;
	
	/**
	 * 跟据条件查询资源（年级、科目、版本）
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	public List<SourceVO> getTeachCloudSource(Map<String, String> Map) throws Exception;
	
	/**
	 * 通过资源主键ID获取资源数据
	 * @param id
	 * @return
	 */
	public SourceVO getSourceByID(Integer id);

	/**
	 * 添加教学云资源目录
	 * @param Map
	 */
	public void addSourceCatalogList(Map<String, String> Map) throws Exception;
}
