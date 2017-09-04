package com.ninesky.classtao.school.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.framework.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.BedroomService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.vo.BedVO;
import com.ninesky.classtao.school.vo.BedroomVO;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.common.util.ListUtil;
import com.ninesky.common.util.StringUtil;

@Service("BedroomServiceImpl")
public class BedroomServiceImpl implements BedroomService{
	
	@Autowired
	private GeneralDAO dao;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassService classService;
	@Autowired
	private RedisService redisService;
	/**
	 * 添加寝室
	 */
	public List<BedroomVO> insertBedroom(BedroomVO vo){
		List<BedroomVO> list = new ArrayList<BedroomVO>();
		for(int i=vo.getStart_num(); i<vo.getEnd_num()+1; i++)
		{	
			//插入数据封装
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			Integer id = dao.insertObjectReturnID("bedroomMap.insertBedroom", vo);
			//返回数据封装
			BedroomVO returnVo = new BedroomVO();
			returnVo.setBedroom_id(id);
			returnVo.setBedroom_name(vo.getBedroom_pre()+"#"+i);
			returnVo.setSex(vo.getSex());
			returnVo.setVersion(0);
			list.add(returnVo);
		}
		return list;
	}

	
	/**
	 * 根据bedroom_id查询寝室信息
	 * @return
	 */
	public BedroomVO getBedroomInfoById(Integer bedroom_id){
		return dao.queryObject("bedroomMap.getBedroomInfoById", bedroom_id);
	}
	
	/**
	 * 根据前缀，楼层查询寝室信息
	 */
	public List<BedroomVO> getBedroomInfoByFloor(String bedroom){
		List<BedroomVO> list = new ArrayList<BedroomVO>();
		list = dao.queryForList("bedroomMap.getBedroomInfoByFloor", bedroom);
		return list;
	}
	
	
	/**
	 * 设置寝室床位人员
	 */
	public void bindBedForStudent(BedVO vo)
	{	
		//查询该学生是否存在
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("school_id", vo.getSchool_id());
		paramMap.put("student_code", vo.getStudent_code());
		StudentVO student = userService.getStudentByCode(paramMap);
		if(student == null) 
			throw new BusinessException("该学生不存在，请检查学生号");
		//判断床位是否有人使用
		if(dao.queryObject("bedroomMap.getStudentByBedCode", vo)!=null)
			throw new BusinessException("该床位已经被使用");
		//查看学生是否绑定床位
		if(dao.queryObject("bedroomMap.getBedListByStudentCode", vo)!=null)
			throw new BusinessException("该学生已经绑定了床位");
		vo.setStudent_id(student.getStudent_id());
		vo.setStudent_name(student.getStudent_name());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("bedroomMap.insertBedOfStudent", vo);
	}
	
	//寝室添加人员
	public void addStudentForBed(BedVO vo) {
		//查询该学生是否存在
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("school_id", vo.getSchool_id());
				paramMap.put("student_code", vo.getStudent_code());
				StudentVO student = userService.getStudentByCode(paramMap);
				//查看学生是否绑定床位
				if(dao.queryObject("bedroomMap.getBedListByStudentCode", vo)!=null)
					throw new BusinessException(student.getStudent_name()+"学生已经绑定了床位");
				vo.setStudent_id(student.getStudent_id());
				vo.setStudent_name(student.getStudent_name());
				vo.setCreate_by(ActionUtil.getUserID());
				vo.setCreate_date(ActionUtil.getSysTime());
				String bed_code=dao.queryObject("bedroomMap.getMax", vo.getBedroom_id());
				if(StringUtil.isEmpty(bed_code))//还没有人入住时
					vo.setBed_code("1");
				else
					vo.setBed_code(bed_code);
				dao.insertObject("bedroomMap.insertBedOfStudent", vo);
	}
	
	/**
	 * 获取寝室床位人员信息
	 */
	public List<BedVO> getBedListByBedroomId(Integer bedroom_id){
		List<BedVO> list=dao.queryForList("bedroomMap.getBedListByBedroomId", bedroom_id);
		for(BedVO vo:list){
			StudentVO svo=userService.getStudentById(vo.getStudent_id());
			ClassVO cvo=new ClassVO();
			cvo.setClass_id(svo.getClass_id());
			ClassVO classvo=dao.queryObject("classMap.getClassList", cvo);
			vo.setClass_name(classService.getClassByID(classvo.getClass_id()).getClass_name());
			vo.setSex(svo.getSex());
		}
		return list;
	}
	
	/**
	 * 删除寝室床位人员信息
	 */
	public void deleteBedOfStudent(BedVO vo){
		dao.deleteObject("bedroomMap.deleteBedOfStudent", vo);
		//redis
		redisService.updateStudentCountToRedis(DictConstants.TEAM_TYPE_BEDROOM, 0, vo.getBedroom_id(), -1);
		//redis.usersGroup
		//删除该学生的寝室身份
		redisService.deleteTeamFromUserGroup(DictConstants.TEAM_TYPE_BEDROOM,DictConstants.USERTYPE_STUDENT,
				0,vo.getBedroom_id(),0, vo.getStudent_id());
	}

	/**
	 * 获取寝室学生列表
	 */
	public List<BedVO> getBedPositionList(Integer bedroom_id) {
		return dao.queryForList("bedroomMap.getBedPositionList",bedroom_id);
	}


	public List<BedroomVO> getBedroomList(BedroomVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		return dao.queryForList("bedroomMap.getBedroomList", vo);
	}

	//后台寝室设置页面
	public List<BedroomVO> getBedroomListOfManager() {
		BedroomVO vo=new BedroomVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<BedroomVO> list=dao.queryForList("bedroomMap.getBedroomList", vo);
		if(ListUtil.isEmpty(list)) return list;
		for(BedroomVO bvo:list){
			int count=dao.queryObject("bedroomMap.getCount", bvo.getBedroom_id());
			bvo.setCount(count);
		}
		return list;
	}

	//后台寝室设置页面（添加寝室）
	public BedroomVO addBedroom(BedroomVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		int id=dao.insertObjectReturnID("bedroomMap.addBedroom", vo);
		vo.setBedroom_id(id);
		return vo;
	}

	//后台寝室设置页面（删除寝室）
	public void deleteBedroom(Integer bedroom_id) {
		dao.deleteObject("bedroomMap.deleteBedroom", bedroom_id);
		BedVO vo=new BedVO();
		vo.setBedroom_id(bedroom_id);
		dao.deleteObject("bedroomMap.deleteBedOfStudent", vo);
		dao.deleteObject("scoreMap.deleteByClassid", bedroom_id);
		dao.deleteObject("scoreListMap.deleteByClassid", bedroom_id);
	}

	//批量添加寝室人员
	public String addStudentListForBed(String item_list) {
		List<BedVO> list=BeanUtil.jsonToList(item_list, BedVO.class);
		String msg="";
		int count=0;
		String bedCode="1";
		boolean flag=true;
		for(BedVO vo:list){
			vo.setSchool_id(ActionUtil.getSchoolID());
			StudentVO svo=new StudentVO();
			svo.setSchool_id(ActionUtil.getSchoolID());
			svo.setStudent_code(vo.getStudent_code());
			StudentVO student=userService.getStudentByStudentCode(svo);
			//查看学生是否绑定床位
			if(dao.queryObject("bedroomMap.getBedListByStudentCode", vo)!=null){
				if(StringUtil.isEmpty(msg)){
					msg=student.getStudent_name();
				}else{
					msg=msg+","+student.getStudent_name();
				}
				continue;
			}
			vo.setStudent_id(student.getStudent_id());
			vo.setStudent_name(student.getStudent_name());
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			String bed_code=dao.queryObject("bedroomMap.getMax", vo.getBedroom_id());
			if(StringUtil.isEmpty(bed_code))//还没有人入住时
				vo.setBed_code("1");
			else {//遍历哪个床位是空的
				while (flag){
					vo.setBed_code(bedCode);
					BedVO bvo=dao.queryObject("bedroomMap.getStudentByBedCode",vo);
					if (bvo!=null){
						bedCode=Integer.parseInt(bedCode.trim())+1+"";
					} else {
						flag=false;
					}
				}
			}
			dao.insertObject("bedroomMap.insertBedOfStudent", vo);
			count++;
			bedCode=Integer.parseInt(bedCode.trim())+1+"";
			flag=true;
			//redis.usersGroup
			redisService.addUserToUserGroup(DictConstants.USERTYPE_STUDENT,DictConstants.TEAM_TYPE_BEDROOM, 0,
					vo.getBedroom_id(), 0,student.getStudent_id());
		}
		//redis
		redisService.updateStudentCountToRedis(DictConstants.TEAM_TYPE_BEDROOM, 0, list.get(0).getBedroom_id(), count);
		return msg;
	}
}
