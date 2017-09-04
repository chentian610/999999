package com.ninesky.classtao.school.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.service.GradeService;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.school.vo.GradeVO;
import com.ninesky.classtao.school.vo.GroupVO;
import com.ninesky.classtao.school.vo.TeamVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.Constants;
import com.ninesky.framework.GeneralDAO;

@Service("ClassServiceImpl")
public class ClassServiceImpl implements ClassService {

	@Autowired
	private GeneralDAO dao;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private UserService userService;
	@Autowired
	private DictService dictService;
	@Autowired
	private RedisService redisService;

	public List<ClassVO> list(Integer userId) {
		return dao.queryForList("classMap.getClassVOList",userId);
	}
	
	//获取教师班级列表
	public List<TeacherVO> getClassListByUserID(TeacherVO vo) {
		List<TeacherVO> list = userService.getTeacherListByUserID(vo);
		HashMap<String,TeacherVO> tempMap = new HashMap<String,TeacherVO>();
		List<TeacherVO> teacherList=new ArrayList<TeacherVO>();
		//班级去重
		for (TeacherVO rvo:list) {
			if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type())) {
				rvo.setClass_name(redisService.getTeamName(rvo.getTeam_type(),rvo.getGrade_id(),rvo.getClass_id()));
				tempMap.put(RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), rvo.getTeam_type(), rvo.getGrade_id(), rvo.getClass_id()), rvo);
			}else {
				rvo.setClass_name(redisService.getTeamName(rvo.getTeam_type(),0,rvo.getContact_id()));
				tempMap.put(RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), rvo.getTeam_type(), 0, rvo.getContact_id()), rvo);
			}
		}
		if (vo.getIs_filtered()==null) vo.setIs_filtered(0);
		if (vo.getIs_filtered()==1) {//过滤掉重复的班级
			for (TeacherVO rvo : tempMap.values()) {
				teacherList.add(rvo);
			}
			return teacherList;
		}
		return list;
	}
	
	public List<ClassVO> getClassBySchoolID(HashMap<String,Object> p) {
		return dao.queryForList("classMap.getClassBySchoolID",p);
	}
	
	//更新班级信息（学生表只有班级id，暂不更新）
	public void updateClass(ClassVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("classMap.updateClass", vo);//更新班级信息
		userService.updateClassOfTea(vo);//更新老师表班级信息
		userService.udpateClassOfPar(vo);//更新家长表班级信息
	}

	//添加班级
	public ClassVO insertClass(ClassVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		//普通班级
		if(DictConstants.CLASSTYPE_COMMON.equals(vo.getClass_type())) {
			if (IntegerUtil.isEmpty(vo.getGrade_num()) || 
					IntegerUtil.isEmpty(vo.getClass_num())) 
				throw new BusinessException("年级号和班级号不能为空....");
			addGradeAndClassName(vo);//根据年级号和班级号设置年级名和班级名
		//自定义班级
		} else if (DictConstants.CLASSTYPE_SPECIAL.equals(vo.getClass_type())) {
			if(StringUtil.isEmpty(vo.getClass_name()))
				throw new BusinessException("自定义班级名称不能为空....");
		}else{
			throw new BusinessException("请指定班级的添加方式....");
		}
		vo.setStatus(DictConstants.STATUS_NORMAL);
		vo.setImg_url(Constants.DEFAULT_CLASS_IMG_URL);
		vo.setClass_code(this.GenerateClassCode(null));//生成6位随机数，作为班级的唯一编码
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setClass_id(dao.insertObjectReturnID("classMap.insertClass", vo));
		return vo;
	}
	//为班级添加年级信息
	private void addGradeAndClassName(ClassVO vo){
		GradeVO paramVO = new GradeVO();
		paramVO.setSchool_id(vo.getSchool_id());
		paramVO.setGrade_num(vo.getGrade_num());
		GradeVO gradeVO = gradeService.getGradeByNum(paramVO);
		//没有该年级就添加
		if(gradeVO == null){
			switch(vo.getGrade_num()){
			case 1 : paramVO.setGrade_name("一年级"); break;
			case 2 : paramVO.setGrade_name("二年级"); break;
			case 3 : paramVO.setGrade_name("三年级"); break;
			case 4 : paramVO.setGrade_name("四年级"); break;
			case 5 : paramVO.setGrade_name("五年级"); break;
			case 6 : paramVO.setGrade_name("六年级"); break;
			case 7 : paramVO.setGrade_name("初一"); break;
			case 8 : paramVO.setGrade_name("初二"); break;
			case 9 : paramVO.setGrade_name("初三"); break;
			case 10 : paramVO.setGrade_name("高一"); break;
			case 11 : paramVO.setGrade_name("高二"); break;
			case 12 : paramVO.setGrade_name("高三"); break;
			}
			paramVO.setSort(vo.getGrade_num()%10);
			gradeVO = gradeService.addGrade(paramVO);
		}
		vo.setGrade_id(gradeVO.getGrade_id());
		vo.setGrade_name(gradeVO.getGrade_name());
		vo.setClass_name(gradeVO.getGrade_name()+"（"+vo.getClass_num()+"）班");
	}
	/**
	 * 判断新添加的班级名是否存在
	 * @param vo
	 * @return
	 */
	public Integer getClassCountByName(ClassVO vo){
		return dao.queryObject("classMap.getClassCountByName", vo);
	}
	/**
	 * 生成6位随机数，作为班级的唯一编码
	 * @return
	 * @throws Exception
	 */
	private String GenerateClassCode(ClassVO vo) {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<6;i++)
			sb.append((int)(Math.random()*10));
		return sb.toString();
	}

	public void delete(ClassVO vo) throws Exception {

	}

	public ClassVO getClassByID(Integer class_id) {
		if (IntegerUtil.isEmpty(class_id)) return new ClassVO();
		ClassVO vo = dao.queryObject("classMap.getClassByID",class_id);
		vo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),
				vo.getClass_id()));
		return vo;
	}


	public List<ClassVO> getClassListBySchoolID(Integer schoolID) {
		List<ClassVO> list=new ArrayList<ClassVO>();
		ClassVO vo = new ClassVO();
		vo.setSchool_id(schoolID);
		list=dao.queryForList("classMap.getClassList",vo);
		for(ClassVO cvo:list){
			cvo.setClass_name(getClassByID(cvo.getClass_id()).getClass_name());
		}
		return list;
	}


	public List<ParentVO> getChildListByUserID(ParentVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ParentVO> list = userService.getParentList(vo);
		for(ParentVO pvo:list){
			pvo.setClass_name(getClassByID(pvo.getClass_id()).getClass_name());
			pvo.setGrade_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS, pvo.getGrade_id(), 0));
			pvo.setTeam_list(BeanUtil.ListTojson(userService.getTeamList(pvo)).toString());
		}
		return list;
	}


	public List<ClassVO> getClassList(ClassVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
	List<ClassVO> list=dao.queryForList("classMap.getClassListByClass", vo);
	for(ClassVO classVO:list){//添加年级名称属性
		if(classVO.getGrade_id()!=null){
		GradeVO gradeVO=new GradeVO();
		gradeVO.setGrade_id(classVO.getGrade_id());
		gradeVO.setSchool_id(classVO.getSchool_id());
		gradeVO=dao.queryObject("gradeMap.getGradeList", gradeVO);
		classVO.setGrade_name(gradeService.getGradeByID(gradeVO.getGrade_id()).getGrade_name());
		}
		classVO.setClass_name(getClassByID(classVO.getClass_id()).getClass_name());
	}
		return list;
	}
	
	public List<TeamVO> getClassListOfGrade(ClassVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<TeamVO> list=dao.queryForList("classMap.getClassListByGrade", vo);
		for(TeamVO teamVO:list){//添加年级名称属性
			teamVO.setClass_name(getClassByID(teamVO.getClass_id()).getClass_name());
			teamVO.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			Integer count=dao.queryObject("studentMap.getStudentCount",teamVO.getClass_id());
			teamVO.setCount(count);
		}
		return list;
	}

	//获取教师列表
	public List<TeacherVO> getTeacherList(TeacherVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<TeacherVO> list=userService.getTeaUserListByDuty(vo);
		for(TeacherVO tvo:list){
			if(!IntegerUtil.isEmpty(tvo.getClass_id())){
				tvo.setClass_name(getClassByID(tvo.getClass_id()).getClass_name());
			}else{
				if(!IntegerUtil.isEmpty(tvo.getGrade_id())){
					tvo.setClass_name(gradeService.getGradeByID(tvo.getGrade_id()).getGrade_name());
				}
			}
		}
		return list;
	}

	//获取学生列表
	public List<StudentVO> getStudentList(StudentVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<StudentVO> list=userService.getStuUserList(vo);
		if(list.size()==0) throw new BusinessException(MsgService.getMsg("UNFIND_STUDENT"));
		for(StudentVO svo:list){//设置班级名称
			svo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,svo.getGrade_id(),
					svo.getClass_id()));
		}
		return list;
	}

	//添加学生
	public StudentVO addStudent(StudentVO vo) {
		return userService.addStudent(vo);
	}

	//获取模块收件人分组列表
	public List<GroupVO> getGroupList(String user_type) {
		List<GroupVO> glist=new ArrayList<GroupVO>();
		if (DictConstants.USERTYPE_TEACHER.equals(user_type)){//校务通知:所有教师，职务，班主任
			GroupVO vo=new GroupVO();
			vo.setGroup_name(MsgService.getMsg("ALL_TEACHER"));
			vo.setDuty(DictConstants.DICT_TEACHER);
			int count=dao.queryObject("teacherMap.getTeacherTotal",ActionUtil.getSchoolID());
			vo.setCount(count);//所有教师人数
			glist.add(vo);
			DictVO dvo=new DictVO();
			dvo.setSchool_id(ActionUtil.getSchoolID());
			dvo.setDict_group(DictConstants.DICT_TEACHER);
			List<DictVO> list=dictService.getDictSchoolList(dvo);//获取该校所有教师职务
			for (DictVO dict:list){
				GroupVO gvo=new GroupVO();
				gvo.setGroup_name(dict.getDict_value());
				gvo.setDuty(dict.getDict_code());
                TeacherVO teacher=new TeacherVO();
                teacher.setSchool_id(ActionUtil.getSchoolID());
                teacher.setDuty(dict.getDict_code());
                int teacherCount=dao.queryObject("teacherMap.getTeacherCount",teacher);
                gvo.setCount(teacherCount);
				glist.add(gvo);
			}
			GroupVO gvo=new GroupVO();
			gvo.setGroup_name(MsgService.getMsg("HEADMASTER"));
			gvo.setDuty(DictConstants.DICT_TEACHER_ADVISER);
            TeacherVO teacherVO=new TeacherVO();
            teacherVO.setSchool_id(ActionUtil.getSchoolID());
            teacherVO.setIs_charge(1);
            int chargeCount=dao.queryObject("teacherMap.getTeacherCount",teacherVO);
            gvo.setCount(chargeCount);
			glist.add(gvo);
			return glist;
		}
		TeacherVO teacherVO=new TeacherVO();
		teacherVO.setUser_id(ActionUtil.getUserID());
		teacherVO.setSchool_id(ActionUtil.getSchoolID());
		List<TeacherVO> list=userService.getTeacherListByUserID(teacherVO);//用户的职务身份列表
		setClassName(list);//设置班级名称
		List<Integer> ilist=new ArrayList<Integer>();
		for(TeacherVO tvo:list){
			if(DictConstants.DICT_TEACHER_CLASS.equals(tvo.getDuty())){//普通任课教师,显示班级
				GroupVO groupVO=new GroupVO();
				groupVO.setGroup_type(DictConstants.TEAM_TYPE_CLASS);//普通分组
				groupVO.setUser_type(DictConstants.USERTYPE_STUDENT);
				groupVO.setGroup_name(tvo.getClass_name());
				groupVO.setGrade_id(tvo.getGrade_id());
				groupVO.setClass_id(tvo.getClass_id());
				glist.add(groupVO);
			} else if (DictConstants.DICT_TEACHER_INTEREST.equals(tvo.getDuty())){//兴趣班
				GroupVO groupVO=new GroupVO();
				groupVO.setGroup_type(tvo.getTeam_type());//普通分组
				groupVO.setUser_type(DictConstants.USERTYPE_STUDENT);
				groupVO.setGroup_name(tvo.getClass_name());
				groupVO.setGrade_id(0);
				groupVO.setClass_id(tvo.getContact_id());
				glist.add(groupVO);
			} else {
				if(!ilist.contains(tvo.getGrade_id())){//显示去重
					getOtherContact(glist, tvo,user_type);//年级组长，校长等的收件人列表
					ilist.add(tvo.getGrade_id());
				}
			}
		}
		return glist;
	}

	//设置班级名称
	private void setClassName(List<TeacherVO> list) {
		for (TeacherVO tevo:list){
			if (IntegerUtil.isNotEmpty(tevo.getClass_id())){
				tevo.setClass_name(getClassByID(tevo.getClass_id()).getClass_name());
			}else if(IntegerUtil.isNotEmpty(tevo.getGrade_id())){
				tevo.setClass_name(gradeService.getGradeByID(tevo.getGrade_id()).getGrade_name());
			} else if (DictConstants.TEAM_TYPE_INTEREST.equals(tevo.getTeam_type())){
				ContactVO cvo=dao.queryObject("contactMap.getContactById", tevo.getContact_id());
				if (cvo!=null) tevo.setClass_name(cvo.getContact_name());
			}
		}
	}

	//年级组长，校长等的收件人列表
	public void getOtherContact(List<GroupVO> glist, TeacherVO tvo,String user_type) {
		if(!DictConstants.USERTYPE_TEACHER.equals(user_type)){//校务通知不发给学生
		GroupVO groupVO=new GroupVO();
		groupVO.setUser_type(DictConstants.USERTYPE_STUDENT);
		groupVO.setGroup_type(DictConstants.TEAM_TYPE_CLASS);//普通分组
		groupVO.setGroup_name(tvo.getClass_name());
		groupVO.setGrade_id(tvo.getGrade_id());
		groupVO.setClass_id(tvo.getClass_id());
		glist.add(groupVO);
		}
	}

	//管理后台获取教师列表
	public List<TeacherVO> getTeacherListOfManager(String teacher_name) {
		TeacherVO vo=new TeacherVO();
		vo.setTeacher_name("%"+teacher_name+"%");//模糊查询
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<String> list=userService.getTeacherListOfSearch(vo);
		List<TeacherVO> tList=new ArrayList<TeacherVO>();
		getDutyName(list, tList);//显示教师职务信息
		return tList;
	}
	
	//教师分组获取全体教师（排除已有教师）
	public List<TeacherVO> getTeacherListOfManagerGroup(String teacher_name,String contact_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("school_id", ActionUtil.getSchoolID());
		map.put("teacher_name", teacher_name);//用于模糊查询
		map.put("contact_id", contact_id);//用于排除分组里的教师
		List<String> list=userService.getTeacherListNotContact(map);//手机号
		List<TeacherVO> tList=new ArrayList<TeacherVO>();
		getDutyName(list, tList);//显示教师职务信息
		return tList;
	}

	//显示教师职务信息
	public void getDutyName(List<String> list, List<TeacherVO> tList) {
		for(String phone:list){
			TeacherVO tvo=new TeacherVO();
			tvo.setSchool_id(ActionUtil.getSchoolID());
			tvo.setPhone(phone);
			ActionUtil.setPage_app(false);      //内部代码list不分页
			ActionUtil.setPage_web(false);   
			List<TeacherVO> teacherList=userService.getTeacherListByPhone(tvo);
			if(ListUtil.isEmpty(teacherList)) continue;
			setClassName(teacherList);//设置班级名称
			tvo.setTeacher_name(teacherList.get(0).getTeacher_name());
			tvo.setUser_id(teacherList.get(0).getUser_id());
			tvo.setSex(teacherList.get(0).getSex());
			String duty_name = showTeacherDuty(teacherList);//教师身份情况的显示
			tvo.setDuty_name(duty_name);
			tList.add(tvo);
		}
	}

	//教师身份情况的显示
	private String showTeacherDuty(List<TeacherVO> teacherList) {
		String dutyName="";
		for(TeacherVO teachervo:teacherList){
			if (teachervo.getIs_graduate() == 1) continue;
			if (StringUtil.isEmpty(teachervo.getDuty())) continue;
			if(DictConstants.DICT_TEACHER_CLASS.equals(teachervo.getDuty())){//任课教师
				DictVO dvo=dao.queryObject("dictMap.getDictInfo", teachervo.getCourse());
				if(dvo==null) continue;
				if(dutyName.equals("")){
					if(teachervo.getIs_charge()!=null && teachervo.getIs_charge()==1)
					dutyName=teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER")+
							"   "+MsgService.getMsg("HEADMASTER");//例：高一（1）班数学老师
					else
						dutyName=teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER");//例：高一（1）班数学老师
				}else{
					if(teachervo.getIs_charge()!=null && teachervo.getIs_charge()==1)
					dutyName=dutyName+"、"+teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER")+
							"   "+MsgService.getMsg("HEADMASTER");
					else
						dutyName=dutyName+"、"+teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER");
				}
			}else if(DictConstants.DICT_TEACHER_GRADER.equals(teachervo.getDuty())){//年级主任
				if(dutyName.equals("")){
					dutyName=teachervo.getClass_name()+MsgService.getMsg("DIRECTOR");//一年级主任
				}else{
					dutyName=dutyName+"、"+teachervo.getClass_name()+MsgService.getMsg("DIRECTOR");
				}
			}else if(DictConstants.DICT_TEACHER_ADMIN.equals(teachervo.getDuty())){//行政管理
				DictVO dvo=dao.queryObject("dictMap.getDictInfo", DictConstants.DICT_TEACHER_ADMIN);
				if(dutyName.equals("")){
					dutyName=dvo.getDict_value();//行政管理
				}else{
					dutyName=dutyName+"、"+dvo.getDict_value();
				}
			}else if(DictConstants.DICT_TEACHER_INTEREST.equals(teachervo.getDuty())){//兴趣班教师
				if(dutyName.equals("")){
					dutyName=teachervo.getClass_name()+MsgService.getMsg("TEACHER");
				}else{
					dutyName=dutyName+"、"+teachervo.getClass_name()+MsgService.getMsg("TEACHER");
				}
			}else {//校领导，自定义身份
				DictVO dict=new DictVO();
				dict.setSchool_id(ActionUtil.getSchoolID());
				dict.setDict_code(teachervo.getDuty());
				DictVO dvo=dao.queryObject("dictMap.getDictSchoolList", dict);
				if(dutyName.equals("")){
					dutyName=dvo.getDict_value();
				}else{
					dutyName=dutyName+"、"+dvo.getDict_value();
				}
			}
		}
		return dutyName;
	}

	//显示教师职务信息，不拼接
	public List<TeacherVO> getDutyNameList(String phone) {
		List<TeacherVO> list = new ArrayList<TeacherVO>();
		TeacherVO tvo=new TeacherVO();
		tvo.setSchool_id(ActionUtil.getSchoolID());
		tvo.setPhone(phone);
		List<TeacherVO> teacherList=userService.getTeacherListByPhone(tvo);
		for(TeacherVO vo:teacherList){
			if (vo.getIs_graduate() == 1) continue;
			if(IntegerUtil.isNotEmpty(vo.getClass_id())){
				vo.setClass_name(getClassByID(vo.getClass_id()).getClass_name());
				vo.setGrade_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),0));
			}else{
				if(IntegerUtil.isNotEmpty(vo.getGrade_id())){
					vo.setClass_name(gradeService.getGradeByID(vo.getGrade_id()).getGrade_name());
					vo.setGrade_name(vo.getClass_name());
				} else if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type())){
					ContactVO cvo=dao.queryObject("contactMap.getContactById", vo.getContact_id());
					if (cvo!=null) vo.setClass_name(cvo.getContact_name());
				}
			}
			list.add(vo);
		}
		for(TeacherVO teachervo:list){
			if(DictConstants.DICT_TEACHER_CLASS.equals(teachervo.getDuty())){//任课教师
				DictVO dvo=dao.queryObject("dictMap.getDictInfo", teachervo.getCourse());
				if(dvo==null) continue;
				if(teachervo.getIs_charge()!=null && teachervo.getIs_charge()==1){
				String dutyName=teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER")+
						"   "+MsgService.getMsg("HEADMASTER");;//例：高一（1）班数学老师
				teachervo.setDuty_name(dutyName);
				}else{
					String dutyName=teachervo.getClass_name()+dvo.getDict_value()+MsgService.getMsg("TEACHER");//例：高一（1）班数学老师
					teachervo.setDuty_name(dutyName);
				}
			}else if(DictConstants.DICT_TEACHER_GRADER.equals(teachervo.getDuty())){//年级主任
					String dutyName=teachervo.getClass_name()+MsgService.getMsg("DIRECTOR");//一年级主任
					teachervo.setDuty_name(dutyName);
			}else if(DictConstants.DICT_TEACHER_INTEREST.equals(teachervo.getDuty())){//兴趣班教师
				teachervo.setDuty_name(teachervo.getClass_name()+MsgService.getMsg("TEACHER"));
			}else {//校领导，行政管理，自定义
				if (StringUtil.isEmpty(teachervo.getDuty())) continue;
				else {
					DictVO dict = new DictVO();
					dict.setSchool_id(ActionUtil.getSchoolID());
					dict.setDict_code(teachervo.getDuty());
					DictVO dvo = dao.queryObject("dictMap.getDictSchoolList", dict);
					teachervo.setDuty_name(dvo.getDict_value());
				}
			}
			if (IntegerUtil.isNotEmpty(teachervo.getUser_id()))
				teachervo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
					teachervo.getUser_id(), 0));
		}
		return list;
	}

	//获取分组教师列表
	public List<TeacherVO> getTeacherListOfGroup(ContactListVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ContactListVO> list=dao.queryForList("contactListMap.getContactListByMGroup", vo);
		List<String> plist=new ArrayList<String>();
		for(ContactListVO cvo:list){
			plist.add(cvo.getPhone());
			if (IntegerUtil.isEmpty(cvo.getUser_id()) && DictConstants.USERTYPE_PARENT.equals(cvo.getUser_type())) {
				ParentVO pvo=new ParentVO();
				pvo.setSchool_id(ActionUtil.getSchoolID());
				pvo.setPhone(cvo.getPhone());
				ActionUtil.setPage_app(false);      //内部代码list不分页
				ActionUtil.setPage_web(false);   
				List<ParentVO> parentlist=userService.getParentList(pvo);
				cvo.setUser_name(parentlist.get(0).getParent_name());
			} else if (IntegerUtil.isEmpty(cvo.getUser_id()) && DictConstants.USERTYPE_TEACHER.equals(cvo.getUser_type())) {
				TeacherVO tvo=new TeacherVO();
				tvo.setSchool_id(ActionUtil.getSchoolID());
				tvo.setPhone(cvo.getPhone());
				ActionUtil.setPage_app(false);      //内部代码list不分页
				ActionUtil.setPage_web(false);   
				List<TeacherVO> tlist=userService.getTeacherListByPhone(tvo);
				if (ListUtil.isEmpty(tlist)) continue;
				cvo.setUser_name(tlist.get(0).getTeacher_name());
			} else if (IntegerUtil.isNotEmpty(cvo.getUser_id()) && DictConstants.USERTYPE_PARENT.equals(cvo.getUser_type())) {
				UserVO uvo=userService.getUserByPhone(cvo.getPhone());
				cvo.setUser_name(uvo.getUser_name());
			} else {
				cvo.setUser_name(redisService.getUserName(ActionUtil.getSchoolID(), cvo.getUser_type(), 
						cvo.getUser_id(), cvo.getStudent_id()));
			}
		}
		List<TeacherVO> tList=new ArrayList<TeacherVO>();
		getDutyName(plist, tList);//显示教师职务信息
		return tList;
	}


	//获取学生列表，后台使用
	public List<StudentVO> getStudentListOfManager(StudentVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<StudentVO> list=userService.getStudentListOfSearch(vo);
		for(StudentVO svo:list){//设置班级名称
			ClassVO classVO=new ClassVO();
			classVO.setClass_id(svo.getClass_id());
			classVO.setSchool_id(ActionUtil.getSchoolID());
			classVO=dao.queryObject("classMap.getClassListByClass", classVO);
			svo.setClass_name(getClassByID(classVO.getClass_id()).getClass_name());
		}
		return list;
	}

	//学生分组里的全体学生（排除已有学生）
	public List<StudentVO> getStudentListOfManagerGroup(String student_name,String class_id,String contact_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("school_id", ActionUtil.getSchoolID());
		map.put("student_name", student_name);//用于模糊查询
		map.put("contact_id", contact_id);//用于排除分组里的教师
		map.put("class_id", class_id);
		List<StudentVO> list=userService.getStudentListNotContact(map);
		for(StudentVO svo:list){//设置班级名称
			svo.setClass_name(getClassByID(Integer.parseInt(class_id)).getClass_name());
		}
		return list;
	}
	
	//获取班级信息，后台使用
	public List<ClassVO> getClassListOfManager(ClassVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ClassVO> list=dao.queryForList("classMap.getClassListByClass", vo);
		for(ClassVO cvo:list){
			cvo.setClass_name(getClassByID(cvo.getClass_id()).getClass_name());
		}
		return list;
	}
	

	//后台添加班级
	public List<ClassVO> insertClassOfManager(ClassVO vo) {
		List<ClassVO> list=new ArrayList<ClassVO>();
		GradeVO gvo=new GradeVO();
		gvo.setSchool_id(ActionUtil.getSchoolID());
		gvo.setEnrollment_year(vo.getEnrollment_year());
		gvo.setCreate_by(ActionUtil.getUserID());
		gvo.setCreate_date(ActionUtil.getSysTime());
		int grade_id=dao.insertObjectReturnID("gradeMap.addGrade1", gvo);
		for (int j=1;j<vo.getClass_num()+1;j++) {
			ClassVO cvo = new ClassVO();
			cvo.setSchool_id(ActionUtil.getSchoolID());
			cvo.setClass_type(DictConstants.CLASSTYPE_COMMON);
			cvo.setGrade_id(grade_id);
			cvo.setEnrollment_year(vo.getEnrollment_year());
			cvo.setClass_num(j);
			cvo.setCreate_date(ActionUtil.getSysTime());
			cvo.setCreate_by(ActionUtil.getUserID());
			cvo.setStatus(DictConstants.STATUS_NORMAL);
			cvo.setImg_url(Constants.DEFAULT_CLASS_IMG_URL);
			cvo.setClass_code(this.GenerateClassCode(null));
			dao.insertObjectReturnID("classMap.insertNewClass", cvo);
			list.add(cvo);
		}
		return list;
	}

	//后台修改班级数量或入学年份
	public void updateClassGrade(ClassVO vo) {
		//年份没变的情况
		if(vo.getEnrollment_year().equals(vo.getGrade_num())){//enrollment_year放旧的年份，grade_num放新的年份
			vo.setSchool_id(ActionUtil.getSchoolID());
			int count=dao.queryObject("classMap.getClassCount", vo);
			updateClassCount(vo, count);//修改班级数量
		}else{
			GradeVO gvo=new GradeVO();
			gvo.setSchool_id(ActionUtil.getSchoolID());
			gvo.setEnrollment_year(vo.getEnrollment_year());
			int grade_id=dao.queryObject("gradeMap.getGradeByEnrollment", gvo);//获取要修改的该年份对应的grade_id
			gvo.setGrade_id(grade_id);
			gvo.setEnrollment_year(vo.getGrade_num());
			dao.updateObject("gradeMap.updateGrade", gvo);//修改年级表中的入学年份
			ClassVO cvo=new ClassVO();
			cvo.setGrade_id(grade_id);
			cvo.setEnrollment_year(vo.getGrade_num());
			dao.updateObject("classMap.updateEnrollment", cvo);//修改班级表中的入学年份
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setEnrollment_year(vo.getGrade_num());
			int count=dao.queryObject("classMap.getClassCount", vo);//获取该年份的班级数，判断是否要更新
			updateClassCount(vo, count);//修改班级数量
		}
	}

	//修改班级数量
	private void updateClassCount(ClassVO vo, int count) {
		if(vo.getClass_num()>count){
			GradeVO gvo=new GradeVO();
			gvo.setSchool_id(ActionUtil.getSchoolID());
			gvo.setEnrollment_year(vo.getEnrollment_year());
			int grade_id=dao.queryObject("gradeMap.getGradeByEnrollment", gvo);//获取原有班级数
			for(int i=count+1;i<=vo.getClass_num();i++){//添加班级
				ClassVO cvo=new ClassVO();
				cvo.setSchool_id(ActionUtil.getSchoolID());
				cvo.setEnrollment_year(vo.getEnrollment_year());
				cvo.setGrade_id(grade_id);
				cvo.setClass_code(this.GenerateClassCode(null));
				cvo.setClass_type(DictConstants.CLASSTYPE_COMMON);
				cvo.setClass_num(i);
				cvo.setImg_url(Constants.DEFAULT_CLASS_IMG_URL);
				cvo.setCreate_by(ActionUtil.getUserID());
				cvo.setCreate_date(ActionUtil.getSysTime());
				dao.queryObject("classMap.insertNewClass", cvo);
			}
		}else if(vo.getClass_num()<count){//删除班级
			for(int i=vo.getClass_num()+1;i<count+1;i++){
				ClassVO cvo=new ClassVO();
				cvo.setSchool_id(ActionUtil.getSchoolID());
				cvo.setEnrollment_year(vo.getEnrollment_year());
				cvo.setClass_num(i);
				int class_id=dao.queryObject("classMap.getClassByClassNum", cvo);//删除后面几个班级
				dao.deleteObject("classMap.deleteClass", class_id);
				deleteClassRelate(class_id);//删除该班级的所有业务数据
			}
		}
	}

	//删除该班级的所有业务数据,还需再审查是否有缺失的
	private void deleteClassRelate(int class_id) {
		userService.deleteUserAndStudentInfo(class_id);
		dao.deleteObject("commentMap.deleteByClassid", class_id);
		dao.deleteObject("infoReceiveMap.deleteByClassid", class_id);
		dao.deleteObject("noteMap.deleteByClassid", class_id);
		dao.deleteObject("noticeReceiveMap.deleteByClassid", class_id);
		dao.deleteObject("photoMap.deleteByClassid", class_id);
		dao.deleteObject("photoReceiveMap.deleteByClassid", class_id);
		dao.deleteObject("scoreMap.deleteByClassid", class_id);
		dao.deleteObject("scoreListMap.deleteByClassid", class_id);
	}

	//批量添加学生
	public String addStudentList(String item_list) {
		List<StudentVO> list=BeanUtil.jsonToList(item_list, StudentVO.class);
		for (StudentVO vo:list) {//排除最后一条为空
			if (StringUtil.isEmpty(vo.getStudent_name()) || StringUtil.isEmpty(vo.getStudent_code())) {
				list.remove(vo);
				break;
			}
		}
		if (ListUtil.isEmpty(list)) return "";
		return userService.addStudentList(list);
	}

	public void setClassIsGraduateByGradeID(Integer grade_id) {
		ClassVO vo = new ClassVO();
		vo.setGrade_id(grade_id);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("classMap.updataClassIsGraduateByGradeID", vo);
		userService.setTeacherIsGraduate(vo);
		gradeService.setGradeIsGraduateByGradeID(grade_id);
		//删除寝室已经毕业的学生
        dao.deleteObject("bedroomMap.deleteGraduateStudent",grade_id);
		//删除兴趣班已经毕业的学生
		dao.deleteObject("contactListMap.deleteGradeStudent",grade_id);
	}

	public List<TeamVO> getTeamList(Integer school_id) {
		TeamVO vo = new TeamVO();
		vo.setSchool_id(school_id);
		vo.setIs_graduate(DictConstants.FALSE);
		vo.setUser_type(DictConstants.USERTYPE_STUDENT);
		List<TeamVO> list = dao.queryForList("classMap.getTeamList",vo);
		if (ListUtil.isEmpty(list)) return list;
		for (TeamVO teamVO : list) {
			teamVO.setClass_name(redisService.getTeamName(teamVO.getTeam_type(),teamVO.getGroup_id(),teamVO.getTeam_id()));
		}
		return list;
	}

	//获取班级学生（排除已在寝室学生）
	public List<StudentVO> getStudentListNotBedroom(String student_name,Integer class_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("school_id", ActionUtil.getSchoolID());
		map.put("student_name", student_name);//用于模糊查询
		map.put("class_id", class_id);
		List<StudentVO> list=dao.queryForList("studentMap.getStudentNotBedroom",map);
		for(StudentVO svo:list){//设置班级名称
			svo.setClass_name(getClassByID(class_id).getClass_name());
		}
		return list;
	}
}
