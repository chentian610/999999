package com.ninesky.classtao.fame.service;

import java.util.List;

import com.ninesky.classtao.fame.vo.FameVO;

public interface fameService {
	
	/**
	 * 添加名人信息
	 * @param vo
	 * @return FameVO
	 */
	public FameVO addFame(FameVO vo);
		
      /**
       * 获取名人信息 
       * @param fame_id
       * @return
       */
	public FameVO getFameList(Integer fame_id);
	
    /**
     * 获取名人信息 列表
     * @param
     * @return
     */
	public List<FameVO> getFameListForWeb(FameVO vo);
	
    /**
     * 删除名人信息
     * @param vo
     * @param famePath
     */
	public void deleteFame(FameVO vo);
	
	/**
	 * 更新名人信息
	 * @param vo
	 */
	public void updateFame(FameVO vo);
	

}
