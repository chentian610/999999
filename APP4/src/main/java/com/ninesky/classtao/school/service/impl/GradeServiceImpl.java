package com.ninesky.classtao.school.service.impl;

import java.util.List;

import com.ninesky.framework.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.service.GradeService;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.school.vo.GradeVO;
import com.ninesky.classtao.school.vo.TeamVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.GeneralDAO;

@Service("gradeServiceImpl")
public class GradeServiceImpl implements GradeService{

	@Autowired
	private GeneralDAO dao;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ClassService classService;
	
	//获取学校年级列表
	public List<GradeVO> getGradeList(GradeVO gradeVO) {
		gradeVO.setSchool_id(ActionUtil.getSchoolID());
		List<GradeVO> list = dao.queryForList("gradeMap.getGradeList",gradeVO);
		for (GradeVO vo:list) {
			vo.setGrade_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(),null));
		}
		return list;
	}

	//添加学校年级
	public GradeVO addGrade(GradeVO vo) {
		GradeVO gradeVO=dao.queryObject("gradeMap.getGradeByNumOrName", vo);//年级号，年级名称需唯一
		if(gradeVO!=null){
			throw new BusinessException("该年级号或年级名称已经存在！");
		}
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		int id=dao.insertObjectReturnID("gradeMap.addGrade", vo);
		vo.setGrade_id(id);
		return vo;
	}

	//更新学校年级信息
	public void updateGrade(GradeVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<GradeVO> gradeList=dao.queryForList("gradeMap.getGradeByNumOrName", vo);//年级号，年级名称需唯一
		if(gradeList.size()>0){
			throw new BusinessException("该年级号或年级名称已经存在！");
		}
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("gradeMap.updateGrade", vo);
	}

	//归档学校年级信息
	public void deleteGrade(GradeVO vo) {
		dao.deleteObject("gradeMap.deleteGrade", vo);
	}
	
	//根据学校和年级num获取年级信息
	public GradeVO getGradeByNum(GradeVO vo){
		GradeVO gvo=dao.queryObject("gradeMap.getGradeByNumOrName", vo);
		gvo.setGrade_name(getGradeByID(gvo.getGrade_id()).getGrade_name());
		return gvo;
	}

	//根据年级ID获取年级信息
	public GradeVO getGradeByID(Integer grade_id) {
		GradeVO vo=dao.queryObject("gradeMap.getGradeListByID", grade_id);
		vo.setGrade_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(),null));
		return vo;
	}

	//获取年级信息，包括该年级的班级信息
	public List<GradeVO> getGradeAndClass() {
		GradeVO vo=new GradeVO();
		List<GradeVO> list=getGradeList(vo);
		for(GradeVO gvo:list){
			ClassVO cvo=new ClassVO();
			cvo.setGrade_id(gvo.getGrade_id());
			List<TeamVO> clist=classService.getClassListOfGrade(cvo);
			String classlist=BeanUtil.ListTojson(clist).toString();
			gvo.setClass_list(classlist);
		}
		return list;
	}

	@Override
	public void setGradeIsGraduateByGradeID(Integer grade_id) {
		GradeVO vo = new GradeVO();
		vo.setGrade_id(grade_id);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("gradeMap.updateGradeIsGraduateByID",vo);
	}
}
