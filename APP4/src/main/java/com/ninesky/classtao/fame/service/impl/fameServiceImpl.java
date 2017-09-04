package com.ninesky.classtao.fame.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ninesky.classtao.fame.service.fameService;
import com.ninesky.classtao.fame.vo.FameVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;

@Service("fameServiceImpl")
public class fameServiceImpl implements fameService {

	@Autowired
	private GeneralDAO dao;
	
	/**
	 * 添加名人信息
	 * @param vo
	 * @return
	 */
	public FameVO addFame(FameVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		int fame_id = dao.insertObjectReturnID("fameMap.insertFame", vo);		
		vo.setFame_id(fame_id);		
		return vo;
	}

	/**
	 * 利用fame_id获取名人信息 
	 * @param school_id
	 * @return
	 */
	public FameVO getFameList(Integer fame_id){
		return 	 dao.queryObject("fameMap.getFameList", fame_id);
		
	}
	
	/**
	 * 获取名人墙列表
	 * @param paramMap
	 * @return
	 */
	public List<FameVO> getFameListForWeb(FameVO vo){
		return dao.queryForList("fameMap.getFameListForWeb", vo);
	}

	/**
	 * 删除名人信息
	 * @param vo
	 * @param delete
	 */
	public void deleteFame(FameVO vo) {
		dao.deleteObject("fameMap.deleteFame", vo);

	}
	
	/**
	 * 更新名人信息
	 * @param vo
	 */
	public void updateFame(FameVO vo){
		dao.updateObject("fameMap.updateFame", vo);
	}

	
}
