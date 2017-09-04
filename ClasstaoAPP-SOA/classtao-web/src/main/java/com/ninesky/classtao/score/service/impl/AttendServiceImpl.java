package com.ninesky.classtao.score.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.common.DictConstants;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.score.service.AttendService;
import com.ninesky.classtao.score.vo.AttendCodeVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.common.util.StringUtil;

@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService{
	
	@Autowired
	private GeneralDAO dao;

	//获取学校考勤项目
	public List<AttendCodeVO> getAttendCodeList(String school_id) {
		if (StringUtil.isEmpty(school_id)) {
			school_id=ActionUtil.getSchoolID().toString();
			if (StringUtil.isEmpty(school_id)) throw new BusinessException("请输入school_id!");
		}
		DictVO dict=new DictVO();
		dict.setSchool_id(Integer.parseInt(school_id));
		dict.setIs_active(1);
		dict.setDict_group(DictConstants.ATTEND_ITEM);
		List<DictVO> list=dao.queryForList("dictMap.getDictSchoolList",dict );
		List<AttendCodeVO> attendList=new ArrayList<AttendCodeVO>();
		for (DictVO vo:list){//为了支持旧版本
			AttendCodeVO attend=new AttendCodeVO();
			attend.setAttend_code(vo.getDict_code());
			attend.setAttend_name(vo.getDict_value());
			attend.setDescription(vo.getDescription());
			attend.setSort(vo.getSort());
			attendList.add(attend);
		}
		return attendList;
	}

	//删除学校考勤项目
	public void deleteAttendCode(String attend_code) {
		DictVO dictVO=new DictVO();
		dictVO.setSchool_id(ActionUtil.getSchoolID());
		dictVO.setDict_group(DictConstants.ATTEND_ITEM);
		dictVO.setIs_active(1);
		Integer count=dao.queryForList("dictMap.getDictSchoolList", dictVO).size();
		if (1==count) throw new BusinessException(MsgService.getMsg("LAST_ATTEND"));
		dictVO.setDict_code(attend_code);
		dictVO.setIs_active(0);
		dictVO.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("dictMap.updateDictSchool", dictVO);
	}
}
