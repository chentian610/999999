package com.ninesky.classtao.user.service.impl;

import com.ninesky.classtao.contact.service.ContactService;
import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.service.GradeService;
import com.ninesky.classtao.school.vo.BedVO;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.*;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private RedisService redisService;
	
	public UserVO insertUser(UserVO vo,int school_id) {
		if(StringUtil.isEmpty(vo.getHead_url())){
			vo.setHead_url(Constants.HEAD_URL_DEFAULT);
		}
		vo.setStatus(DictConstants.STATUS_NORMAL);
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setUser_id(dao.insertObjectReturnID("userMap.insertUser",vo));
		int a=dao.updateObject("teacherMap.updateUserID", vo);//老师注册时，修改teacher表相应老师的user_id
		if(a>0){//得到teacher_name作为user_name，避免第一次登录时，名字为null
			TeacherVO tvo=new TeacherVO();
			tvo.setPhone(vo.getPhone());
			tvo.setSchool_id(school_id);
			List<TeacherVO> tlist=dao.queryForList("teacherMap.getTeacherListByPhone", tvo);
			vo.setUser_name(tlist.get(0).getTeacher_name());
			dao.updateObject("userMap.completeUserInfo", vo);//更新用户
			//redis,user_group
			redisService.getTeamKeyList(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(),0);
		}
		int b=dao.updateObject("parentMap.updateParentUserID", vo);//家长注册时，修改parent表相应家长的user_id
		if(b>0){//得到parent_name作为user_name，避免第一次登录时，名字为null
			ParentVO pvo=new ParentVO();
			pvo.setPhone(vo.getPhone());
			pvo.setSchool_id(school_id);
			List<ParentVO> plist=dao.queryForList("parentMap.getParent", pvo);
			vo.setUser_name(plist.get(0).getParent_name());
			dao.updateObject("userMap.completeUserInfo", vo);//更新用户
		}
		dao.updateObject("contactListMap.updateUserID", vo);//注册后，修改通讯录表中的相关信息(通讯录里的人有可能还没注册过)
		ValidateCodeVO vCodeVO = new ValidateCodeVO();
		vCodeVO.setPhone(vo.getPhone());
		vCodeVO.setUpdate_date(ActionUtil.getSysTime());
		vCodeVO.setUpdate_by(vo.getUser_id());
		vCodeVO.setIs_use(Constants.TRUE_FLAG);
		dao.updateObject("validateCodeMap.updateValidateCode",vCodeVO);
		return vo;
	}
	/**
	 * 添加学校管理员
	 * @param vo
	 * @return
	 */
	public UserVO insertAdmin(UserVO vo){
		int i = dao.updateObject("userMap.updateUserStatusByPhone",vo);
		if (i==0) vo.setUser_id(dao.insertObjectReturnID("userMap.insertUser",vo));
		return vo;
	}
	public UserVO validateUser(UserVO vo){
		if(DictConstants.STATUS_INACTIVE.equals(vo.getStatus())) throw new BusinessException("帐号未激活！");
        if(DictConstants.STATUS_STOP.equals(vo.getStatus())) throw new BusinessException("帐号被禁用了！");
		return vo;
    }
	
	public UserVO validateAdmin(UserVO vo){
		return dao.queryObject("userMap.getUserByPhone",vo.getPhone());
    }


	public UserVO validateLezhiAdmin(UserVO vo){
		UserVO user = dao.queryObject("userMap.getUserByPhone",vo.getPhone());
		if (user == null) throw new BusinessException(MsgService.getMsg("LOGIN_USER_NOT_EXISTS_TRY_AGAIN"));
        if (StringUtil.isEmpty(user.getPass_word())|| !user.getPass_word().equals(vo.getPass_word())) throw new BusinessException(MsgService.getMsg("LOGIN_USER_PASSWORD_ERROR_TRY_AGAIN"));
		validateUser(user);
		user.setUser_type(ActionUtil.getUserType());
		user.setSchool_id(ActionUtil.getSchoolID());
		return user;
    }
	
	public UserVO getUserByID(Integer user_id){
		UserVO user = dao.queryObject("userMap.getUserByID",user_id);
		if (user==null)  throw new BusinessException(MsgService.getMsg("NO_USER"));
        if (IntegerUtil.isNotEmpty(ActionUtil.getSchoolID())) {
            TeacherVO tvo = new TeacherVO();
            tvo.setUser_id(user_id);
            tvo.setSchool_id(ActionUtil.getSchoolID());
            List<TeacherVO> tlist = classService.getDutyNameList(user.getPhone());
            user.setTeacher_list(BeanUtil.ListTojson(tlist).toString());
            ParentVO vo = new ParentVO();
            vo.setUser_id(user_id);
            vo.setSchool_id(ActionUtil.getSchoolID());
            List<ParentVO> plist = classService.getChildListByUserID(vo);
            user.setParent_list(BeanUtil.ListTojson(plist).toString());
        }
        return user;
    }
	
	public UserVO getUserByPhone(String phone){
		return dao.queryObject("userMap.getUserByPhone",phone);
    }
	
	public void updateValidateCode(ValidateCodeVO vo) {
		vo.setUpdate_date(ActionUtil.getSysTime());
		vo.setUpdate_by(ActionUtil.getUserID());
		Integer count = dao.updateObject("validateCodeMap.updateValidateCode",vo);
		if (count == 0) {
			vo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("validateCodeMap.insertValidateCode",vo);
		}
	}
	
	public void completeUserInfo(UserVO vo) {
        if (StringUtil.isEmpty(vo.getUser_name())) throw new BusinessException(MsgService.getMsg("TEACHER_NAME_NULL"));
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("userMap.completeUserInfo", vo);
		updateInfoOfUser(vo);//修改用户关联表的身份信息（姓名，头像）
		//redis,有教师身份的,redis都改，保持统一
		List<TeacherVO> teacherList=dao.queryForList("teacherMap.getTeacherListByUserID", vo);
		if (ListUtil.isNotEmpty(teacherList) && StringUtil.isNotEmpty(vo.getUser_name())) 
			redisService.updateUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 
					ActionUtil.getStudentID(), vo.getUser_name());
		if (ListUtil.isNotEmpty(teacherList) && StringUtil.isNotEmpty(vo.getHead_url())) 
			redisService.updateHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(),
					ActionUtil.getStudentID(), vo.getHead_url());
	}

	public void updateInfoOfUser(UserVO vo) {
		ParentVO pvo = new ParentVO();// 完善个人信息后，修改家长表中的相关信息
		pvo.setParent_name(vo.getUser_name());
		pvo.setUser_id(ActionUtil.getUserID());
		pvo.setUpdate_by(ActionUtil.getUserID());
		pvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("parentMap.updateParentName", pvo);
		TeacherVO tvo=new TeacherVO();// 完善个人信息后，修改教师表中的相关信息
		tvo.setTeacher_name(vo.getUser_name());
		tvo.setSex(vo.getSex());
		tvo.setHead_url(vo.getHead_url());
		String all_letter=LetterUtil.converterToSpell(vo.getUser_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getUser_name());
		tvo.setAll_letter(all_letter);
		tvo.setFirst_letter(first_letter);
		tvo.setUser_id(ActionUtil.getUserID());
		tvo.setUpdate_by(ActionUtil.getUserID());
		tvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("teacherMap.updateTeacher", tvo);
	}

	public boolean checkValidateCode(HashMap<String,String> map) {
		ValidateCodeVO vo =  dao.queryObject("validateCodeMap.checkValidateCode",map);
		if (vo==null) throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
		if (!vo.getValidate_code().equals(map.get("validate_code"))) throw new BusinessException(MsgService.getMsg("UN_VALIDATECODE"));
		if (Constants.TRUE_FLAG.equals(vo.getIs_use()))  throw new BusinessException(MsgService.getMsg("USED_VALIDATECODE"));
		if (DateUtil.addMinute(vo.getUpdate_date(),10).before(ActionUtil.getSysTime()))
			 throw new BusinessException(MsgService.getMsg("INVALID_CODE"));
		return true;
	}

	public void resetPassword(UserVO vo) {
		if (StringUtil.isEmpty(vo.getPhone())) {
			UserVO user = dao.queryObject("userMap.getUserByID",vo.getUser_id());
			if (user == null)
				throw new BusinessException("该用户不存在！");
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("userMap.resetPasswordByID", vo);
		} else {
			UserVO user = getUserByPhone(vo.getPhone());
			if (user == null)
				throw new BusinessException("该用户不存在！");
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("userMap.resetPassword", vo);
		}
	}

	//修改手机号码
	public void modifyPhone(UserVO vo) {
		ValidateCodeVO item = new ValidateCodeVO();
		item.setPhone(vo.getPhone());
		item.setIs_use(Constants.TRUE_FLAG);
		item.setUpdate_by(ActionUtil.getUserID());
		item.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("validateCodeMap.updateValidateCode",item);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("userMap.modifyPhone",vo);
		dao.updateObject("parentMap.updateParentPhone", vo);
		dao.updateObject("teacherMap.updateTeacherPhone", vo);
		updateContactInfo(vo);//更新自定义通讯录分组信息
	}

	public void signToXinge(UserVO vo) {
		UserVO user = dao.queryObject("userMap.getUserByID",ActionUtil.getUserID());
		//如果Token变化(一般更换手机登陆，会引起变化),
	}

	//给教师添加职务
	public List<TeacherVO> addTeacherDuty(TeacherVO vo) {
		if(IntegerUtil.isNull(vo.getGrade_id()) || IntegerUtil.isNull(vo.getClass_id()))
			throw new BusinessException("班级信息不能为空（grade_id,class_id,class_name）!");//必填判断
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
		vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		vo.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(), ActionUtil.getUserType(),
				ActionUtil.getUserID(), ActionUtil.getStudentID()));//教师姓名
		vo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		//TeacherVO tvo=dao.queryObject("teacherMap.getNewTeacher", vo);//查询该老师是否有无绑定职务的记录(duty是空)
		List<TeacherVO> tlist=dao.queryForList("teacherMap.getNewTeacher", vo);
		if(ListUtil.isNotEmpty(tlist)){
			vo.setTeacher_id(tlist.get(0).getTeacher_id());
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			vo.setIs_confirm(1);//已确认的职务
			dao.updateObject("teacherMap.addDuty", vo);
		}else{
			if(vo.getTeacher_id()==null){
				String all_letter=LetterUtil.converterToSpell(vo.getTeacher_name());//获取首字母缩写converterToFirstSpell
				String first_letter=LetterUtil.converterToFirstSpell(vo.getTeacher_name());
				vo.setAll_letter(all_letter);
				vo.setFirst_letter(first_letter);
				vo.setIs_confirm(1);//已确认的职务
				vo.setSex(getUserByID(ActionUtil.getUserID()).getSex());//教师性别
				int id=dao.insertObjectReturnID("teacherMap.insertTeacher", vo);//教师自主另外绑定某班级职务
				vo.setTeacher_id(id);
			}
		}
		//redis,user_group
		redisService.addUserToUserGroup(DictConstants.USERTYPE_TEACHER, DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(),
				vo.getClass_id(), vo.getUser_id(), 0);
		redisService.updateTeacherCountToRedis(DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),vo.getClass_id());
		redisService.updateTeacherDuty(vo.getPhone());
		return classService.getDutyNameList(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
	}

	public List<TeacherVO> addTeacherInterestDuty(TeacherVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
		vo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
		vo.setIs_charge(0);
		vo.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(), ActionUtil.getUserType(),
				ActionUtil.getUserID(), ActionUtil.getStudentID()));//教师姓名
		vo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setUser_id(ActionUtil.getUserID());
		List<TeacherVO> tlist=dao.queryForList("teacherMap.getNewTeacher", vo);
		if(ListUtil.isNotEmpty(tlist)){
			vo.setTeacher_id(tlist.get(0).getTeacher_id());
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			vo.setIs_confirm(1);//已确认的职务
			dao.updateObject("teacherMap.addInterestDuty", vo);
		}else{
			if(vo.getTeacher_id()==null){
				String all_letter=LetterUtil.converterToSpell(vo.getTeacher_name());//获取首字母缩写converterToFirstSpell
				String first_letter=LetterUtil.converterToFirstSpell(vo.getTeacher_name());
				vo.setAll_letter(all_letter);
				vo.setFirst_letter(first_letter);
				vo.setIs_confirm(1);//已确认的职务
				vo.setSex(getUserByID(ActionUtil.getUserID()).getSex());//教师性别
				int id=dao.insertObjectReturnID("teacherMap.addInterestTeacher", vo);//教师自主另外绑定某班级职务
				vo.setTeacher_id(id);
			}
		}
		//redis,user_group
		redisService.addUserToUserGroup(DictConstants.USERTYPE_TEACHER, DictConstants.TEAM_TYPE_INTEREST, 0,
				vo.getContact_id(), vo.getUser_id(), 0);
		redisService.updateTeacherDuty(vo.getPhone());
		return classService.getDutyNameList(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
	}

	//给家长添加孩子
	public ParentVO addParentChild(ParentVO vo) {
		if(StringUtil.isEmpty(vo.getStudent_name())) throw new BusinessException("请填写学生姓名！");//学生姓名不能为空
		judgeStudent(vo);//家长添加孩子时，判断该学生是否符合条件
		UserVO userParent = getUserByID(ActionUtil.getUserID());
		vo.setParent_name(userParent.getUser_name());//家长姓名
		vo.setSchool_id(ActionUtil.getSchoolID());
		ClassVO classVO=new ClassVO();//根据class_id来查class_name
		classVO.setClass_id(vo.getClass_id());
		classVO.setSchool_id(ActionUtil.getSchoolID());//学校ID
		List<ClassVO> clist=classService.getClassList(classVO);
		vo.setGrade_id(clist.get(0).getGrade_id());//根据class_id来查grade_id
		vo.setClass_name(classService.getClassByID(vo.getClass_id()).getClass_name());
		vo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,
				0, vo.getStudent_id()));//孩子默认图像
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setPhone(userParent.getPhone());//家长手机号
		String all_letter=LetterUtil.converterToSpell(vo.getParent_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getParent_name());
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		int id=dao.insertObjectReturnID("parentMap.insertParent", vo);
		vo.setParent_id(id);
		//孩子所在班级，兴趣班集合
		List<ReceiveVO> rlist = getTeamList(vo);
		vo.setTeam_list(BeanUtil.ListTojson(rlist).toString());
		return vo;
	}

	//孩子所在班级，兴趣班集合
	public List<ReceiveVO> getTeamList(ParentVO vo) {
		List<ReceiveVO> rlist=new ArrayList<ReceiveVO>();
		ReceiveVO rvo=new ReceiveVO();
		rvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		rvo.setGroup_id(vo.getGrade_id());
		rvo.setTeam_id(vo.getClass_id());
		rvo.setTeam_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,rvo.getGroup_id(),rvo.getTeam_id()));
		rlist.add(rvo);
		List<Integer> ilist=dao.queryForList("contactListMap.getContactListByStuID", vo.getStudent_id());
		for (Integer i:ilist) {
			ReceiveVO rvo1=new ReceiveVO();
			rvo1.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
			rvo1.setGroup_id(0);
			rvo1.setTeam_id(i);
			rvo1.setTeam_name(redisService.getTeamName(DictConstants.TEAM_TYPE_INTEREST,0,rvo1.getTeam_id()));
			rlist.add(rvo1);
		}
		return rlist;
	}

	//完善更新孩子信息
	public ParentVO updateChildByStudentCode(ParentVO vo) {
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("parentMap.updateParent", vo);
		updateStudentHeadurl(vo);//更新学生表中头像
		dao.updateObject("parentMap.updateAllParents", vo);
		vo=dao.queryObject("parentMap.getParentList", vo);
		vo.setClass_name(classService.getClassByID(vo.getClass_id()).getClass_name());
		List<ReceiveVO> rlist = getTeamList(vo);//孩子所在班级，兴趣班集合
		vo.setTeam_list(BeanUtil.ListTojson(rlist).toString());
		return vo;
	}

	public void updateStudentHeadurl(ParentVO vo) {
		if(StringUtil.isNotEmpty(vo.getHead_url())){//如果有更新头像，学生表中的头像也需更新
		StudentVO svo=new StudentVO();
		svo.setHead_url(vo.getHead_url());//设置头像
		svo.setStudent_code(vo.getStudent_code());
		svo.setUpdate_by(ActionUtil.getUserID());
		svo.setUpdate_date(ActionUtil.getSysTime());
		svo.setSchool_id(ActionUtil.getSchoolID());
		dao.updateObject("studentMap.updateStudentHeadurl",svo);
		redisService.updateHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, 0, vo.getStudent_id(),
				vo.getHead_url());
		}
	}

	//绑定社交帐号
	public UserSnsVO addUserSns(UserSnsVO vo) {
		vo.setCreate_date(ActionUtil.getSysTime());
		int id=dao.insertObjectReturnID("userSnsMap.insertUserSns", vo);
		vo.setSns_id(id);
		vo.setIs_inactive("0");//默认帐号为开启
		return vo;
	}

	//获取社交帐号
	public List<UserSnsVO> getUserSnsList(UserSnsVO vo) {
		return dao.queryForList("userSnsMap.getUserSnsList", vo);
	}
	
	/**
	 * 根据用户id和社交类型返回社交账号
	 */
	public List<UserSnsVO> getParentSnsAccount(String snsType, Integer studentId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("snsType", snsType);
		param.put("studentId", studentId);
		return dao.queryForList("userSnsMap.getParentSnsAccount", param);
	}

	/**
	 * 根据年级、班级获取家长user列表
	 */
	public List<ParentVO> getParUserList(ParentVO vo) {
		List<ParentVO> list=dao.queryForList("parentMap.getParUserList", vo);
		for(ParentVO pvo:list){
			pvo.setClass_name(classService.getClassByID(pvo.getClass_id()).getClass_name());
		}
		return list;
	}

	/**
	 * 根据年级、班级获取学生user列表
	 */
	public List<StudentVO> getStuUserList(StudentVO vo) {
		return dao.queryForList("studentMap.getStuUserList", vo);
	}

	/**
	 * 根据年级、班级获取教师user列表
	 */
	public List<TeacherVO> getTeaUserList(TeacherVO vo) {
		List<TeacherVO> list= dao.queryForList("teacherMap.getTeaUserList", vo);
		for(TeacherVO tvo:list){
			if (DictConstants.TEAM_TYPE_CLASS.equals(tvo.getTeam_type()))
				tvo.setClass_name(redisService.getTeamName(tvo.getTeam_type(),tvo.getGrade_id(),tvo.getClass_id()));
		}
		return list;
	}

	//获取全部联系人，校务通知（除了自己）
	public List<TeacherVO> getAllTeacher(TeacherVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		Map<String,Object> map=new HashMap<String, Object>();
		List<TeacherVO> list = dao.queryForList("teacherMap.getTeaUserList", vo);
		List<TeacherVO> teacherList=new ArrayList<TeacherVO>();
		for(TeacherVO tvo:list){
			if (map.containsKey(tvo.getPhone())) continue;
			if(IntegerUtil.isNotEmpty(tvo.getClass_id())){
				tvo.setClass_name(classService.getClassByID(tvo.getClass_id()).getClass_name());
			}else{
				if(IntegerUtil.isNotEmpty(tvo.getGrade_id())){
					tvo.setClass_name(gradeService.getGradeByID(tvo.getGrade_id()).getGrade_name());
				}
			}
			tvo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, tvo.getUser_id(), 0));
			List<TeacherVO> dutys=redisService.getTeacherDuty(tvo.getPhone());
			tvo.setDuty_name(BeanUtil.ListTojson(dutys,false));
			map.put(tvo.getPhone(),tvo);
			teacherList.add(tvo);
		}
		if (vo.getIs_filtered()==null || vo.getIs_filtered()==1) {//过滤掉用户本身
			for (TeacherVO teacher : teacherList) {
				if (ActionUtil.getUserID().equals(teacher.getUser_id())) {
					teacherList.remove(teacher);
					break;
				}
			}
		}
		return teacherList;
	}


	/**
	 * 家长添加孩子时，判断该学生是否符合条件
	 */
	public void judgeStudent(ParentVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		StudentVO svo=new StudentVO();
		svo.setStudent_code(vo.getStudent_code());//学号
		svo.setStudent_name(vo.getStudent_name());
		svo.setClass_id(vo.getClass_id());
		svo.setSchool_id(ActionUtil.getSchoolID());
		svo=dao.queryObject("studentMap.getStudent", svo);//获取学生其他信息
		vo.setStudent_sex(svo.getSex());//学生性别
		if(dao.queryObject("parentMap.getParentList", vo)!=null)
			throw new BusinessException(MsgService.getMsg("USED_RELATION"));
	}

		//删除教师职务
		public List<TeacherVO> deleteTeacherDuty(Integer teacher_id) {
			TeacherVO vo=new TeacherVO();
			vo.setTeacher_id(teacher_id);
			TeacherVO tvo=dao.queryObject("teacherMap.getTeacherById", teacher_id);
			List<TeacherVO> list=dao.queryForList("teacherMap.getTeacherListByPhone", tvo);
			if(list.size()>1){
				dao.deleteObject("teacherMap.deleteTeacher", vo);
				//redis,user_group
				if (IntegerUtil.isNotEmpty(tvo.getUser_id()) && StringUtil.isNotEmpty(tvo.getDuty())) {
					redisService.deleteUserFromUserGroup(DictConstants.USERTYPE_TEACHER, tvo.getUser_id(), 0);
					redisService.getTeamKeyList(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, tvo.getUser_id(),0);
				}
			}else if(list.size()==1){//只剩一个身份时，不删除，进行修改
				vo.setUpdate_by(ActionUtil.getUserID());
				vo.setUpdate_date(ActionUtil.getSysTime());
				dao.updateObject("teacherMap.updateTeacherOne", vo);
				if (IntegerUtil.isNotEmpty(tvo.getUser_id()) && StringUtil.isNotEmpty(tvo.getDuty())) {
					redisService.deleteUserFromUserGroup(DictConstants.USERTYPE_TEACHER, tvo.getUser_id(), 0);
				}
			}
			redisService.updateTeacherDuty(tvo.getPhone());
			if (IntegerUtil.isNotEmpty(tvo.getUser_id()) && DictConstants.TEAM_TYPE_CLASS.equals(tvo.getTeam_type()))
				redisService.updateTeacherCountToRedis(tvo.getTeam_type(), tvo.getGrade_id(), tvo.getClass_id());
			return classService.getDutyNameList(tvo.getPhone());
		}

	//根据学生号获取学生信息
	public StudentVO getStudentById(Integer student_id){
		return dao.queryObject("studentMap.getStudentById", student_id);
	}

	public List<TeacherVO> showClassOfGrade(TeacherVO vo){
		List<TeacherVO> list=new ArrayList<TeacherVO>();
		vo.setUser_id(ActionUtil.getUserID());
		list=dao.queryForList("teacherMap.getTeacherClass", vo);
		for(TeacherVO tvo:list){
			if(IntegerUtil.isNotEmpty(tvo.getClass_id())){
				tvo.setClass_name(classService.getClassByID(tvo.getClass_id()).getClass_name());
			}else{
				if(IntegerUtil.isNotEmpty(tvo.getGrade_id())){
					tvo.setClass_name(gradeService.getGradeByID(tvo.getGrade_id()).getGrade_name());
				}
			}
		}
		return list;
	}

	//添加教师
	public TeacherVO addTeacher(TeacherVO vo) {
		if (StringUtil.isEmpty(vo.getTeacher_name())) throw new BusinessException(MsgService.getMsg("TEACHER_NAME_NULL"));
		vo.setTeacher_name(vo.getTeacher_name().replace(" ",""));
        vo.setSchool_id(ActionUtil.getSchoolID());
		UserVO userVO=new UserVO();
		userVO.setPhone(vo.getPhone());
		userVO=dao.queryObject("userMap.getUserByPhone", vo.getPhone());//判断该教师是否已注册过
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		if (userVO!=null){
			vo.setUser_id(userVO.getUser_id());
//			UserVO uvo=new UserVO();
//			uvo.setUser_name(vo.getTeacher_name());
//			uvo.setPhone(vo.getPhone());
//			uvo.setSex(vo.getSex());
//			uvo.setUser_id(vo.getUser_id());
//			uvo.setUpdate_by(ActionUtil.getUserID());
//			uvo.setUpdate_date(ActionUtil.getSysTime());
//			dao.updateObject("userMap.updateUser", uvo);//更新user表
		}
		vo.setIs_confirm(0);//未确认职务
		String all_letter=LetterUtil.converterToSpell(vo.getTeacher_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getTeacher_name());
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		List<TeacherVO> ttlist=dao.queryForList("teacherMap.getNewTeachermana", vo);
		if (ListUtil.isNotEmpty(ttlist)){//已存在一条职务为空的教师记录（添加教师时没加职务）
			vo.setTeacher_id(ttlist.get(0).getTeacher_id());
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			dao.updateObject("teacherMap.addDuty", vo);
		} else {
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			vo.setTeacher_id(dao.insertObjectReturnID("teacherMap.addTeacher", vo));
		}
		// List<TeacherVO> tlist=classService.getDutyNameList(vo.getPhone());
		 //redis
		 if (IntegerUtil.isNotEmpty(vo.getUser_id()) && StringUtil.isNotEmpty(vo.getDuty())) {
				redisService.addUserToUserGroup(DictConstants.USERTYPE_TEACHER, DictConstants.TEAM_TYPE_CLASS,
						vo.getGrade_id(),vo.getClass_id(), vo.getUser_id(), 0);
				if (!DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty()))
					 redisService.updateTeacherCountToRedis(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(),
							 vo.getClass_id());
		 }
		 redisService.updateTeacherDuty(vo.getPhone());
		return vo;
	}

	//添加兴趣班教师
	public TeacherVO addInterestTeacher(TeacherVO vo) {
		if (StringUtil.isEmpty(vo.getTeacher_name())) throw new BusinessException(MsgService.getMsg("TEACHER_NAME_NULL"));
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setContact_id(vo.getClass_id());
		vo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
		UserVO userVO=new UserVO();
		userVO.setPhone(vo.getPhone());
		userVO=dao.queryObject("userMap.getUserByPhone", vo.getPhone());//判断该教师是否已注册过
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		if (userVO!=null){
			vo.setUser_id(userVO.getUser_id());
		}
		vo.setIs_confirm(0);//未确认职务
		String all_letter=LetterUtil.converterToSpell(vo.getTeacher_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getTeacher_name());
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		List<TeacherVO> ttlist=dao.queryForList("teacherMap.getNewTeachermana", vo);
		ContactVO contact=dao.queryObject("contactMap.getContactById", vo.getContact_id());
		vo.setCourse(contact.getCourse());
		if (ListUtil.isNotEmpty(ttlist)){//已存在一条职务为空的教师记录（添加教师时没加职务）
			vo.setTeacher_id(ttlist.get(0).getTeacher_id());
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("teacherMap.addInterestDuty", vo);
		} else {
			vo.setTeacher_id(dao.insertObjectReturnID("teacherMap.addInterestTeacher", vo));
		}
		//redis,user_group
		if (IntegerUtil.isNotEmpty(vo.getUser_id()) && StringUtil.isNotEmpty(vo.getDuty())) {
			redisService.addUserToUserGroup(DictConstants.USERTYPE_TEACHER, DictConstants.TEAM_TYPE_INTEREST, 0,
					vo.getContact_id(), vo.getUser_id(), 0);
		}
		redisService.updateTeacherDuty(vo.getPhone());
		 return vo;
	}

	//删除学生
	public void deleteStudent(StudentVO vo) {
		StudentVO svo=dao.queryObject("studentMap.getStudentById", vo.getStudent_id());
		dao.deleteObject("studentMap.deleteStudent", vo);
		dao.deleteObject("bedroomMap.deleteStudent",vo.getStudent_id());//删除寝室人员
        dao.deleteObject("contactListMap.deleteStudent",vo.getStudent_id());//删除兴趣班该学生
		//redis
		redisService.updateStudentCountToRedis(DictConstants.TEAM_TYPE_CLASS, svo.getGrade_id(), svo.getClass_id(),
				-1);
		//redis,userGroups
		redisService.deleteUserFromUserGroup(DictConstants.USERTYPE_STUDENT,0,vo.getStudent_id());
	}

	//根据学号获取学生
	public StudentVO getStudentByCode(Map<String, Object> paramMap){
		return dao.queryObject("studentMap.getStudentByCode", paramMap);
	}

	/**
	 * 更新教师班级信息
	 * @param vo
	 */
	public void updateClassOfTea(ClassVO vo){
		vo.setClass_name(classService.getClassByID(vo.getClass_id()).getClass_name());
		dao.updateObject("teacherMap.updateClassname", vo);
	}
	/**
	 * 更新家长班级信息
	 * @param vo
	 */
	public void udpateClassOfPar(ClassVO vo){
		vo.setClass_name(classService.getClassByID(vo.getClass_id()).getClass_name());
		dao.updateObject("parentMap.updateClassname", vo);
	}

	//教师确认身份
	public void confirmTeacher(Integer teacher_id) {
		dao.updateObject("teacherMap.isconfirmTeacher", teacher_id);
	}

	//更新教师身份信息
	public List<TeacherVO> updateTeacher(TeacherVO vo) {
		vo.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		TeacherVO old=dao.queryObject("teacherMap.getTeacherById", vo.getTeacher_id());
		if (!DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty())){
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			vo.setContact_id(null);
			dao.updateObject("teacherMap.updateTeacherDuty", vo);
			redisService.updateTeacherCountToRedis(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(), vo.getClass_id());
		} else {
			vo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
			vo.setClass_id(null);
			vo.setGrade_id(null);
			dao.updateObject("teacherMap.updateInterestTeacherDuty", vo);
		}
		//redis,user_group
		updateUserGroupOfRedis(old);
		redisService.updateTeacherDuty(vo.getPhone());
		return classService.getDutyNameList(vo.getPhone());
	}

	//redis,user_group
	private void updateUserGroupOfRedis(TeacherVO old) {
		if (IntegerUtil.isNotEmpty(old.getUser_id())) {
			redisService.deleteUserFromUserGroup(DictConstants.USERTYPE_TEACHER, old.getUser_id(), 0);
			redisService.getTeamKeyList(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, old.getUser_id(),0);
			if (DictConstants.TEAM_TYPE_CLASS.equals(old.getTeam_type()))
				redisService.updateTeacherCountToRedis(old.getTeam_type(), old.getGrade_id(), old.getClass_id());
		}
	}

	public List<TeacherVO> updateTeacherOfManager(TeacherVO vo) {
		//vo.setPhone(redisService.getUserPhone(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID()));
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		TeacherVO old=dao.queryObject("teacherMap.getTeacherById", vo.getTeacher_id());
		if (!DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty())){
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			vo.setContact_id(null);
			dao.updateObject("teacherMap.updateTeacherDuty", vo);
			if (IntegerUtil.isNotEmpty(old.getUser_id()))
				redisService.updateTeacherCountToRedis(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(), vo.getClass_id());
		} else {
			vo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
			vo.setContact_id(vo.getClass_id());
			vo.setClass_id(null);
			vo.setGrade_id(null);
			ContactVO contact=dao.queryObject("contactMap.getContactById", vo.getContact_id());
			vo.setCourse(contact.getCourse());
			dao.updateObject("teacherMap.updateInterestTeacherDuty", vo);
		}
		//redis,user_group
		updateUserGroupOfRedis(old);
		redisService.updateTeacherDuty(old.getPhone());
		return classService.getDutyNameList(old.getPhone());
	}

	//用户信息（电话，头像，姓名）改变时更改通讯录模块的自定义分组信息
	private void updateContactInfo(UserVO vo){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("phone", vo.getPhone());
		contactService.updateContactList(paramMap);
	}

	//删除孩子
	public void deleteChild(Integer parent_id) {
		dao.deleteObject("parentMap.deleteParent", parent_id);
	}

	//删除教师
	public void deleteTeacherOfManager(String phone) {
		UserVO userVO=dao.queryObject("userMap.getUserByPhone", phone);//判断该教师是否已注册过
		List<TeacherVO> list=new ArrayList<TeacherVO>();
		if (userVO!=null) {
			TeacherVO teacher=new TeacherVO();
			teacher.setSchool_id(ActionUtil.getSchoolID());
			teacher.setUser_id(userVO.getUser_id());
			list=dao.queryForList("teacherMap.getTeacherListById", teacher);
		}
		TeacherVO vo=new TeacherVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setPhone(phone);
		dao.deleteObject("teacherMap.deleteTeacherOfManager", vo);
		ContactListVO cvo=new ContactListVO();//同时删除通讯录分组里的该老师
		cvo.setSchool_id(ActionUtil.getSchoolID());
		cvo.setPhone(phone);
		dao.deleteObject("contactListMap.deleteContactList", cvo);
		//redis,groupkey
		if (userVO!=null) {
			redisService.deleteUserFromUserGroup(DictConstants.USERTYPE_TEACHER, userVO.getUser_id(), 0);
			for (TeacherVO tvo:list) {
				if (DictConstants.TEAM_TYPE_CLASS.equals(tvo.getTeam_type()))
					redisService.updateTeacherCountToRedis(tvo.getTeam_type(), tvo.getGrade_id(), tvo.getClass_id());
			}
		}
	}

	//修改教师姓名或手机号
	public void updateTeacherNamePhone(TeacherVO vo) {
		if (StringUtil.isEmpty(vo.getTeacher_name())) throw new BusinessException(MsgService.getMsg("TEACHER_NAME_NULL"));
		String teacher_name=vo.getTeacher_name().replace(" ","");//去掉文字中间的空格
		String all_letter=LetterUtil.converterToSpell(teacher_name);//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(teacher_name);
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		vo.setTeacher_name(teacher_name);
		if(vo.getUser_id()==null)//未注册的教师
		dao.updateObject("teacherMap.updateTeacherOfManager", vo);//更新教师表
		else{
			registeredTeacher(vo);//已注册的教师
		}
	}

	//已注册的教师
	private void registeredTeacher(TeacherVO vo) {
		UserVO user=dao.queryObject("userMap.getUserByID", vo.getUser_id());
		dao.updateObject("teacherMap.updateTeacher", vo);
		UserVO uvo=new UserVO();
		uvo.setUser_name(vo.getTeacher_name());
		uvo.setPhone(vo.getPhone());
		uvo.setSex(vo.getSex());
		uvo.setUser_id(vo.getUser_id());
		uvo.setUpdate_by(ActionUtil.getUserID());
		uvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("userMap.updateUser", uvo);//更新user表
		ParentVO pvo=new ParentVO();
		pvo.setParent_name(vo.getTeacher_name());
		pvo.setPhone(vo.getPhone());
		pvo.setAll_letter(vo.getAll_letter());
		pvo.setFirst_letter(vo.getFirst_letter());
		pvo.setUser_id(vo.getUser_id());
		pvo.setUpdate_by(ActionUtil.getUserID());
		pvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("parentMap.updateParentName", pvo);//更新家长表
		ContactListVO cvo=new ContactListVO();
		cvo.setPhone(vo.getPhone());
		cvo.setAll_letter(vo.getAll_letter());
		cvo.setFirst_letter(vo.getFirst_letter());
		cvo.setUpdate_by(ActionUtil.getUserID());
		cvo.setUpdate_date(ActionUtil.getSysTime());
		cvo.setUser_id(vo.getUser_id());
		cvo.setSchool_id(ActionUtil.getSchoolID());
		dao.updateObject("contactListMap.updateContact", cvo);//更新通讯录表相关信息
		if (StringUtil.isNotEmpty(vo.getTeacher_name()))
			redisService.updateUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, vo.getUser_id(),
					0, vo.getTeacher_name());
		if (!user.getPhone().equals(vo.getPhone())) {//只有修改手机号才发短信
			SchoolVO schoolvo=dao.queryObject("schoolMap.getSchoolInfo", ActionUtil.getSchoolID());
			messageService.sendMessage(vo.getPhone(),MsgService.getMsg("MESSAGE_NOTICE_ACCOUNT",vo.getPhone(),schoolvo.getSchool_name()));
		}
	}

	//修改学生信息
	public void updateStudent(StudentVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		StudentVO svo=dao.queryObject("studentMap.getStudentByStudentcode", vo);
		if( svo!=null && !vo.getStudent_id().equals(svo.getStudent_id())) throw new BusinessException(MsgService.getMsg("USED_STUDENTCODE"));
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		String all_letter=LetterUtil.converterToSpell(vo.getStudent_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getStudent_name());
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		dao.updateObject("studentMap.updateStudent", vo);//修改学生表
		if (StringUtil.isNotEmpty(vo.getStudent_name()))
			redisService.updateUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, 0,
					vo.getStudent_id(), vo.getStudent_name());
		ParentVO pvo=new ParentVO();
		if(vo.getClass_id()!=null){//修改所在班级的情况
			ClassVO cvo=new ClassVO();
			cvo.setSchool_id(ActionUtil.getSchoolID());
			cvo.setClass_id(vo.getClass_id());
			ClassVO classVO=dao.queryObject("classMap.getClassListByClass", cvo);
			pvo.setGrade_id(vo.getGrade_id());
			pvo.setClass_id(vo.getClass_id());
			pvo.setClass_name(classService.getClassByID(classVO.getClass_id()).getClass_name());
		}
		pvo.setStudent_code(vo.getStudent_code());
		pvo.setStudent_name(vo.getStudent_name());
		pvo.setStudent_sex(vo.getSex());
		pvo.setStudent_id(vo.getStudent_id());
		pvo.setSchool_id(ActionUtil.getSchoolID());
		pvo.setUpdate_by(ActionUtil.getUserID());
		pvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("parentMap.updateParentOfManager", pvo);//修改家长表中的学生信息
		BedVO bvo=new BedVO();
		bvo.setStudent_id(vo.getStudent_id());
		bvo.setSchool_id(ActionUtil.getSchoolID());
		bvo.setStudent_code(vo.getStudent_code());
		bvo.setStudent_name(vo.getStudent_name());
		bvo.setUpdate_by(ActionUtil.getUserID());
		bvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("bedroomMap.updateStudent", bvo);//更新寝室床位表的学生信息
	}

	//家长花名册获取列表(新版本)
	public List<StudentVO> getParentOfMng(String search) {
		List<StudentVO> slist=new ArrayList<StudentVO>();
		StudentVO svo=new StudentVO();
		svo.setSchool_id(ActionUtil.getSchoolID());
		svo.setStudent_name("%"+search+"%");
		List<StudentVO> list=dao.queryForList("studentMap.getStudentOfMng", svo);
		for(StudentVO student:list){
			ParentVO vo=new ParentVO();
			vo.setStudent_id(student.getStudent_id());
			vo.setSchool_id(ActionUtil.getSchoolID());
			ActionUtil.setPage_app(false);      //内部代码list不分页
			ActionUtil.setPage_web(false);
			List<ParentVO> plist=dao.queryForList("parentMap.getParentList", vo);
			StudentVO studentVO=dao.queryObject("studentMap.getStudentById", student.getStudent_id());
			if(ListUtil.isEmpty(plist)) {
				studentVO.setClass_name(classService.getClassByID(studentVO.getClass_id()).getClass_name());
				slist.add(studentVO); continue;
			}
			for(ParentVO parentvo:plist){
				parentvo.setClass_name(classService.getClassByID(parentvo.getClass_id()).getClass_name());
			}
			String count_info=BeanUtil.ListTojson(plist).toString();//统计信息
			studentVO.setParent_list(count_info);
			studentVO.setClass_name(classService.getClassByID(studentVO.getClass_id()).getClass_name());
			slist.add(studentVO);
		}
		return slist;
	}

	//后台添加家长
	public void addParent(ParentVO vo) {
        vo.setParent_name(vo.getParent_name().replace(" ",""));
		UserVO uvo=dao.queryObject("userMap.getUserByPhone", vo.getPhone());
		if(uvo!=null) vo.setUser_id(uvo.getUser_id());
		vo.setSchool_id(ActionUtil.getSchoolID());
		StudentVO svo=new StudentVO();
		svo.setStudent_code(vo.getStudent_code());
		svo.setSchool_id(ActionUtil.getSchoolID());
		StudentVO studentvo=dao.queryObject("studentMap.getStudent", svo);
		vo.setGrade_id(studentvo.getGrade_id());//根据class_id来查grade_id
		vo.setClass_id(studentvo.getClass_id());
		ClassVO cvo=new ClassVO();
		cvo.setClass_id(vo.getClass_id());
		cvo.setSchool_id(ActionUtil.getSchoolID());
		ClassVO classvo=dao.queryObject("classMap.getClassList", cvo);
		vo.setClass_name(classService.getClassByID(classvo.getClass_id()).getClass_name());
		vo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, 0,
				studentvo.getStudent_id()));
		vo.setStudent_id(studentvo.getStudent_id());
		String all_letter=LetterUtil.converterToSpell(vo.getParent_name());//获取首字母缩写converterToFirstSpell
		String first_letter=LetterUtil.converterToFirstSpell(vo.getParent_name());
		vo.setAll_letter(all_letter);
		vo.setFirst_letter(first_letter);
		vo.setStudent_sex(studentvo.getSex());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObjectReturnID("parentMap.insertParent", vo);
	}

	//删除某位学生的所有家长
	public void deleteAllChild(String student_code) {
		ParentVO vo=new ParentVO();
		vo.setStudent_code(student_code);
		vo.setSchool_id(ActionUtil.getSchoolID());
		dao.deleteObject("parentMap.deleteAllParent", vo);
	}

	//后台修改家长
	public void updateParent(ParentVO vo) {
		if(StringUtil.isNotEmpty(vo.getParent_name())){
			String parent_name=vo.getParent_name().replace(" ","");//去除名字中的空格
			String all_letter=LetterUtil.converterToSpell(parent_name);//获取首字母缩写converterToFirstSpell
			String first_letter=LetterUtil.converterToFirstSpell(parent_name);
		    vo.setAll_letter(all_letter);
		    vo.setFirst_letter(first_letter);
            vo.setParent_name(parent_name);
		}
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("parentMap.updateRelation", vo);//修改家长表
		ParentVO pvo=dao.queryObject("parentMap.getParentById", vo);
		if(pvo!=null && StringUtil.isNotEmpty(vo.getPhone())){//已注册的用户
			UserVO uvo=new UserVO();
			uvo.setUser_name(vo.getParent_name());
			uvo.setPhone(vo.getPhone());
			uvo.setUser_id(pvo.getUser_id());
			uvo.setUpdate_by(ActionUtil.getUserID());
			uvo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("userMap.updateUser", uvo);//修改用户表
			ContactListVO cvo=new ContactListVO();
			cvo.setPhone(vo.getPhone());
			cvo.setAll_letter(vo.getAll_letter());
			cvo.setFirst_letter(vo.getFirst_letter());
			cvo.setUpdate_by(ActionUtil.getUserID());
			cvo.setUpdate_date(ActionUtil.getSysTime());
			cvo.setUser_id(pvo.getUser_id());
			cvo.setSchool_id(ActionUtil.getSchoolID());
			dao.updateObject("contactListMap.updateContact", cvo);//更新通讯录表相关信息
			TeacherVO tvo=new TeacherVO();
			tvo.setTeacher_name(vo.getParent_name());
			tvo.setPhone(vo.getPhone());
			tvo.setUser_id(pvo.getUser_id());
			tvo.setAll_letter(vo.getAll_letter());
			tvo.setFirst_letter(vo.getFirst_letter());
			tvo.setUpdate_by(ActionUtil.getUserID());
			tvo.setUpdate_date(ActionUtil.getSysTime());
			dao.updateObject("teacherMap.updateTeacher", tvo);//更新教师表
			SchoolVO schoolvo=dao.queryObject("schoolMap.getSchoolInfo", ActionUtil.getSchoolID());
			messageService.sendMessage(vo.getPhone(),MsgService.getMsg("MESSAGE_NOTICE_ACCOUNT",vo.getPhone(),schoolvo.getSchool_name()));
		}
	}

	//获取学生分组列表
	public List<StudentVO> getStudentListOfGroup(ContactListVO vo) {
		List<StudentVO> slist=new ArrayList<StudentVO>();
		if (StringUtil.isNotEmpty(vo.getUser_name()))
			vo.setAll_letter(LetterUtil.converterToSpell(vo.getUser_name()));
		List<Integer> list=dao.queryForList("contactListMap.getStudentOfManager", vo);
		for(int studentid:list){
			StudentVO studentVO=dao.queryObject("studentMap.getStudentById", studentid);
			ClassVO classVO=new ClassVO();
			classVO.setClass_id(studentVO.getClass_id());
			classVO.setSchool_id(ActionUtil.getSchoolID());
			classVO=dao.queryObject("classMap.getClassListByClass", classVO);
			studentVO.setClass_name(classService.getClassByID(classVO.getClass_id()).getClass_name());
			slist.add(studentVO);
		}
		return slist;
	}

	//添加教师表和家长表的all_letter,first_letter
	public void updateLetter(){
		if(DictConstants.USERTYPE_PARENT.equals(ActionUtil.getUserType())){//修改家长表
		ParentVO vo=new ParentVO();
		List<ParentVO> list=dao.queryForList("parentMap.getParentList", vo);
		for(ParentVO pvo:list){
			if(pvo.getParent_name()==null)
				continue;
			String all_letter=LetterUtil.converterToSpell(pvo.getParent_name());//获取首字母缩写converterToFirstSpell
			String first_letter=LetterUtil.converterToFirstSpell(pvo.getParent_name());
			pvo.setAll_letter(all_letter);
			pvo.setFirst_letter(first_letter);
			dao.updateObject("parentMap.updateRelation", pvo);
		}
		}else if(DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())){//修改教师表
			TeacherVO vo=new TeacherVO();
			List<TeacherVO> list=dao.queryForList("teacherMap.getTeaUserList", vo);
			for(TeacherVO tvo:list){
				if(tvo.getTeacher_name()==null)
					continue;
				String all_letter=LetterUtil.converterToSpell(tvo.getTeacher_name());//获取首字母缩写converterToFirstSpell
				String first_letter=LetterUtil.converterToFirstSpell(tvo.getTeacher_name());
				tvo.setAll_letter(all_letter);
				tvo.setFirst_letter(first_letter);
				dao.updateObject("teacherMap.updateLetter", tvo);
				if (IntegerUtil.isNotEmpty(tvo.getUser_id()))
					redisService.updateUserName(tvo.getSchool_id(),DictConstants.USERTYPE_TEACHER,tvo.getUser_id(),0,tvo.getTeacher_name());
			}
		} else if (DictConstants.USERTYPE_STUDENT.equals(ActionUtil.getUserType())) {
			List<StudentVO> list=dao.queryForList("studentMap.getStuUserList",new StudentVO());
			for (StudentVO svo:list){
				if (svo.getStudent_name()==null) continue;
				svo.setAll_letter(LetterUtil.converterToSpell(svo.getStudent_name()));
				svo.setFirst_letter(LetterUtil.converterToFirstSpell(svo.getStudent_name()));
				dao.updateObject("studentMap.updateLetter",svo);
				redisService.updateUserName(svo.getSchool_id(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id(),svo.getStudent_name());
			}
		}
	}

		public UserRoleVO getUserRoleByCondition(UserRoleVO vo) {
			return dao.queryObject("userRoleMap.getUserRoleList",vo);
		}

		public List<UserRoleVO> getUserRoleList(UserRoleVO vo) {
			return dao.queryForList("userRoleMap.getUserRoleList",vo);
		}

		public List<TeacherVO> getTeaUserListByDuty(TeacherVO vo) {
			List<TeacherVO> list = dao.queryForList("teacherMap.getTeacherList", vo);
			for (TeacherVO tvo:list) {
				if (StringUtil.isEmpty(tvo.getHead_url()))
					tvo.setHead_url(Constants.HEAD_URL_DEFAULT);
			}
			return list;
		}

		public List<UserRoleVO> getUserRoleByPhone(UserRoleVO vo) {	return dao.queryForList("userRoleMap.getUserRoleByPhone",vo);}

		public void addUserRole(UserRoleVO vo) {
			if (vo.getPhone()==null)
				throw new BusinessException("请输入管理员电话！");
			vo.setUser_type(DictConstants.USERTYPE_ADMIN);
			dao.queryObject("userRoleMap.insertUserRole",vo);
		}

		public void updatePassword(UserVO vo) {
			if (StringUtil.isEmpty(vo.getPhone())) throw new BusinessException("请输入用户手机号码！");
			if (StringUtil.isEmpty(vo.getPass_word())) throw new BusinessException("设置的密码不能为空！");
			UserVO user = getUserByPhone(vo.getPhone());
			//数据库密码和传入的旧密码匹配上：为空与为null理解为匹配，其他相等理解为匹配
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			if ((StringUtil.isEmpty(user.getPass_word()) && StringUtil.isEmpty(vo.getPass_word_old()))
				|| (user.getPass_word().equals(vo.getPass_word_old()))) dao.updateObject("userMap.updatePasswordByPhone", vo);
			else throw new BusinessException(MsgService.getMsg("OLD_PASSWORD_ERROR"));
		}

		//添加教师集合
		public List<TeacherVO> addTeacherList(String item_list) {
			List<TeacherVO> list=BeanUtil.jsonToList(item_list, TeacherVO.class);
			String mobile="^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
			List<TeacherVO> teacherList=new ArrayList<TeacherVO>();
			for (TeacherVO vo:list) {//过滤掉不符合要求的教师信息
				if (StringUtil.isEmpty(vo.getTeacher_name()) || StringUtil.isEmpty(vo.getPhone()) ||
						!vo.getPhone().matches(mobile)) continue;
				vo.setSchool_id(ActionUtil.getSchoolID());
				if (IntegerUtil.isEmpty(IntegerUtil.getValue(ActionUtil.getParameter("is_new")))) {
					List<TeacherVO> tlist = dao.queryForList("teacherMap.getTeacherListByPhone", vo);
					if (ListUtil.isNotEmpty(tlist)) continue;
				}
				teacherList.add(vo);
			}
			for (TeacherVO vo:teacherList) {
				if (DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty())) {//兴趣班教师
					addInterestTeacher(vo);
				} else {
					addTeacher(vo);
				}
			}
			return teacherList;
		}

	//删除一个兴趣班的所有教师
	public void deleteTeacherByContactID(Integer contact_id){
		TeacherVO tvo=new TeacherVO();
		tvo.setSchool_id(ActionUtil.getSchoolID());
		tvo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
		tvo.setContact_id(contact_id);
		dao.deleteObject("teacherMap.deleteInterestTeacher", tvo);
	}

	//通过学号获取学生信息
	public StudentVO getStudentByStudentCode(StudentVO vo){
		return dao.queryObject("studentMap.getStudentByStudentcode", vo);
	}

	//获取全校家长手机号
	public List<String> getParentPhoneList(){
		return dao.queryForList("parentMap.getParentListOfManager", ActionUtil.getSchoolID());
	}

	//获取全校教师手机号
	public List<String> getTeacherPhoneList(){
		return dao.queryForList("teacherMap.getTeacherListOfManager", ActionUtil.getSchoolID());
	}

	//获取用户所有教师身份
	public List<TeacherVO> getTeacherListByUserID(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeacherListById", vo);
	}

	//获取相关班级教师USERID
	public List<Integer> getTeacherUserID(ReceiveVO vo){
		return dao.queryForList("teacherMap.getTeacherListByClass", vo);
	}

	//获取相关兴趣班教师USERID
	public List<Integer> getContactTeacherUserID(Integer team_id){
		return dao.queryForList("teacherMap.getTeacherListByInterest", team_id);
	}

	//获取班主任教师
	public List<TeacherVO> getChargeTeacherList(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeacherOfCharge", vo);
	}

	//获取任课教师
	public List<TeacherVO> getDutyTeacherList(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeacherOfDuty", vo);
	}

	//获取家长孩子信息
	public List<ParentVO> getParentList(ParentVO vo){
		return dao.queryForList("parentMap.getParentList",vo);
	}

	//添加学生
	public StudentVO addStudent(StudentVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		StudentVO studentVO=dao.queryObject("studentMap.getStudentByStudentcode", vo);
		if(studentVO!=null){
			throw new BusinessException(studentVO.getStudent_code()+"学号已存在，请重新填写！");
		}
		vo.setUser_id(0);
		vo.setHead_url(Constants.HEAD_URL_DEFAULT);//学生默认头像
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setAll_letter(LetterUtil.converterToSpell(vo.getStudent_name()));
		vo.setFirst_letter(LetterUtil.converterToFirstSpell(vo.getStudent_name()));
		Integer student_id=dao.insertObjectReturnID("studentMap.addStudent", vo);
		vo.setStudent_id(student_id);
		//redis,groupKey
		redisService.updateStudentCountToRedis(DictConstants.TEAM_TYPE_CLASS, vo.getGrade_id(), vo.getClass_id(),1);
		//redis,user_group
		redisService.addUserToUserGroup(DictConstants.USERTYPE_STUDENT,DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),
				vo.getClass_id(), 0,student_id);
		return vo;
	}

	//获取校教师列表（带搜索功能）
	public List<String> getTeacherListOfSearch(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeaUserListOfManager", vo);
	}

	//查询某个兴趣班以外的所有教师
	public List<String> getTeacherListNotContact(Map<String,Object> map){
		return dao.queryForList("teacherMap.getTeacherNotContact", map);
	}

	//通过手机号获取教师身份信息
	public List<TeacherVO> getTeacherListByPhone(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeacherListByPhone", vo);
	}

	//获取学生列表(带搜索)
	public List<StudentVO> getStudentListOfSearch(StudentVO vo){
		return dao.queryForList("studentMap.getStudentOfManager",vo);
	}

	//查询某个兴趣班以外的所有学生
	public List<StudentVO> getStudentListNotContact(Map<String,Object> map){
		return dao.queryForList("studentMap.getStudentNotContact",map);
	}

	//删除班级相关的教师身份，家长孩子，学生
	public void deleteUserAndStudentInfo(Integer class_id){
		dao.deleteObject("parentMap.deleteByClassid", class_id);
		dao.deleteObject("studentMap.deleteByClassid", class_id);
		dao.deleteObject("teacherMap.deleteByClassid", class_id);
	}

	//批量添加学生
	public String addStudentList(List<StudentVO> list){
		String msg="";
		int i=0;//存放实际添加的个数
		for (StudentVO vo:list){
			vo.setSchool_id(ActionUtil.getSchoolID());
			StudentVO studentVO=dao.queryObject("studentMap.getStudentByStudentcode", vo);//判断该学号是否已被使用过
			if (studentVO!=null){
				if ("".equals(msg)){
					msg=vo.getStudent_code();
				} else {
					msg=msg+","+vo.getStudent_code();
				}
				continue;
			}
			vo.setUser_id(0);
			vo.setHead_url(Constants.HEAD_URL_DEFAULT);//学生默认头像
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			vo.setAll_letter(LetterUtil.converterToSpell(vo.getStudent_name()));
			vo.setFirst_letter(LetterUtil.converterToFirstSpell(vo.getStudent_name()));
			Integer student_id=dao.insertObjectReturnID("studentMap.addStudent", vo);
			vo.setStudent_id(student_id);
			i++;
			//redis,userGroups
			redisService.addUserToUserGroup(DictConstants.USERTYPE_STUDENT,DictConstants.TEAM_TYPE_CLASS,
					vo.getGrade_id(), vo.getClass_id(), 0,student_id);
		}
		//redis,groupKey
		redisService.updateStudentCountToRedis(DictConstants.TEAM_TYPE_CLASS, list.get(0).getGrade_id(),
				list.get(0).getClass_id(), i);
		return msg;//内容是已重复的学号
	}

	//设置教师身份毕业
	public void setTeacherIsGraduate(ClassVO vo){
		dao.updateObject("teacherMap.updateTeacherIsGraduateByGradeID", vo);
	}

	//获取学生基本信息
	public StudentVO getStudentInformationByID(Integer student_id){
		StudentVO stuVO = dao.queryObject("studentMap.getStudentById", student_id);
		ParentVO parentVO = new ParentVO();
		parentVO.setStudent_id(student_id);
		parentVO.setSchool_id(ActionUtil.getSchoolID());
		parentVO.setGrade_id(stuVO.getGrade_id());
		parentVO.setClass_id(stuVO.getClass_id());
		List<ParentVO> plist = getParentList(parentVO);
		stuVO.setParent_list(BeanUtil.ListTojson(plist)+"");
		stuVO.setTeam_list(BeanUtil.ListTojson(getTeamList(parentVO),false));
		stuVO.setAll_letter(LetterUtil.converterToSpell(stuVO.getStudent_name()));
		stuVO.setFirst_letter(LetterUtil.converterToFirstSpell(stuVO.getStudent_name()));
		stuVO.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,student_id));
		stuVO.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,stuVO.getGrade_id(),stuVO.getClass_id()));
		return stuVO;
	}

	//获取教师身份相关的学生列表
	public List<StudentVO> getStudentList(String student_name){
		TeacherVO vo=new TeacherVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUser_id(ActionUtil.getUserID());
		List<TeacherVO> list=dao.queryForList("teacherMap.getTeacherListById",vo);
		List<TeacherVO> returnList=new ArrayList<TeacherVO>();
		HashMap<String,TeacherVO> tempMap = new HashMap<String,TeacherVO>();
		//先将所有接受者存放到HashMap中
		for (TeacherVO rvo:list) {
			if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type()))
				tempMap.put(RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),rvo.getGrade_id(),rvo.getClass_id()),rvo);
		}
		//遍历，去重复
		for (TeacherVO rvo:list) {
			//班级或者寝室
			if (IntegerUtil.isNotEmpty(rvo.getGrade_id()) && IntegerUtil.isNotEmpty(rvo.getClass_id())) {
				String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),rvo.getGrade_id(),0);
				String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),0,0);
				if (tempMap.containsKey(groupKey) || tempMap.containsKey(schoolKey)) continue;
				returnList.add(rvo);
			} else if (IntegerUtil.isNotEmpty(rvo.getGrade_id()) && IntegerUtil.isEmpty(rvo.getClass_id())) {//年级
				String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),0,0);
				if (tempMap.containsKey(schoolKey)) continue;
				returnList.add(rvo);
			} else returnList.add(rvo);
		}
		List<StudentVO> stuList=new ArrayList<StudentVO>();
		HashMap<Integer,Integer> stuMap=new HashMap<Integer,Integer>();
		for (TeacherVO rvo:returnList){
			if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type())){
				StudentVO svo=new StudentVO();
				svo.setSchool_id(ActionUtil.getSchoolID());
				svo.setGrade_id(rvo.getGrade_id());
				svo.setClass_id(rvo.getClass_id());
				svo.setStudent_name(student_name);
				List<StudentVO> slist=dao.queryForList("studentMap.getStudentList",svo);
				stuList.addAll(slist);
				if (IntegerUtil.isEmpty(rvo.getGrade_id()) && IntegerUtil.isEmpty(rvo.getClass_id())) break;
			}
		}
		String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),DictConstants.TEAM_TYPE_CLASS,0,0);
		if (!tempMap.containsKey(schoolKey)) {//如果有全校的身份，那就不用查兴趣班
			for (StudentVO svo : stuList) {
				stuMap.put(svo.getStudent_id(), svo.getStudent_id());
			}
			TeacherVO tvo = new TeacherVO();
			tvo.setSchool_id(ActionUtil.getSchoolID());
			tvo.setUser_id(ActionUtil.getUserID());
			tvo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
			tvo.setTeacher_name(student_name);
			List<StudentVO> ilist = dao.queryForList("contactListMap.getStudentOfTeacher", tvo);
			for (StudentVO i : ilist) {
				if (stuMap.containsKey(i.getStudent_id())) continue;
				stuList.add(i);
				stuMap.put(i.getStudent_id(), i.getStudent_id());
			}
		}
		for (StudentVO svo:stuList) {
			svo.setAll_letter(redisService.getAllLetter(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
			svo.setFirst_letter(redisService.getFirstLetter(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
			svo.setHead_url(redisService.getUserHeadUrl(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
			svo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,svo.getGrade_id(),svo.getClass_id()));
		}
		return stuList;
	}

	//合并多个身份 例：数学（2个班级）
	public List<TeacherVO> getDutyNameList(String phone) {
		TeacherVO tvo=new TeacherVO();
		tvo.setSchool_id(ActionUtil.getSchoolID());
		tvo.setPhone(phone);
		List<TeacherVO> teacherList= dao.queryForList("teacherMap.getTeacherByPhone", tvo);
		for(TeacherVO teachervo:teacherList){
			List<TeacherVO> classlist=dao.queryForList("teacherMap.getTeacherByCourse",teachervo);
			if(DictConstants.DICT_TEACHER_CLASS.equals(teachervo.getDuty())){//任课教师
				DictVO dvo = dao.queryObject("dictMap.getDictInfo", teachervo.getCourse());
				if (dvo == null) continue;
				if (teachervo.getCount()==1) {//一个班级
					teachervo.setClass_name(classService.getClassByID(classlist.get(0).getClass_id()).getClass_name());
					teachervo.setClass_id(classlist.get(0).getClass_id());
					teachervo.setTeacher_id(classlist.get(0).getTeacher_id());
					if (teachervo.getIs_charge() != null && teachervo.getIs_charge() == 1) {
						String dutyName =  dvo.getDict_value() + MsgService.getMsg("TEACHER") + "   " +
								MsgService.getMsg("HEADMASTER")+"("+teachervo.getClass_name() +")";//例：数学老师(高一（1）班)
						teachervo.setDuty_name(dutyName);
					} else {
						String dutyName =  dvo.getDict_value() + MsgService.getMsg("TEACHER")+"("+teachervo.getClass_name() +")";
						teachervo.setDuty_name(dutyName);
					}
				} else {//多个班级
					String classes="";
					String teacherids="";
					for (TeacherVO vo:classlist) {
						if (StringUtil.isEmpty(classes)) classes=vo.getClass_id().toString();
						else classes=classes+"."+vo.getClass_id();
						if (StringUtil.isEmpty(teacherids)) teacherids=vo.getTeacher_id().toString();
						else teacherids=teacherids+"."+vo.getTeacher_id();
					}
					teachervo.setClass_ids(classes);
					teachervo.setTeacher_ids(teacherids);
					if (teachervo.getIs_charge() != null && teachervo.getIs_charge() == 1) {
						String dutyName = dvo.getDict_value()+MsgService.getMsg("TEACHER")+"   " + MsgService.getMsg("HEADMASTER")+ MsgService.getMsg("TEACHER_LIST",teachervo.getCount());//例：数学(2个班级) 班主任
						teachervo.setDuty_name(dutyName);
					} else {
						String dutyName = dvo.getDict_value()+MsgService.getMsg("TEACHER")+ MsgService.getMsg("TEACHER_LIST",teachervo.getCount());
						teachervo.setDuty_name(dutyName);
					}
				}
			}else if(DictConstants.DICT_TEACHER_GRADER.equals(teachervo.getDuty())){//年级主任
				teachervo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,teachervo.getGrade_id(),0));
				String dutyName=teachervo.getClass_name()+MsgService.getMsg("DIRECTOR");//一年级主任
				teachervo.setDuty_name(dutyName);
				teachervo.setClass_id(0);
				teachervo.setTeacher_id(classlist.get(0).getTeacher_id());
			}else if(DictConstants.DICT_TEACHER_INTEREST.equals(teachervo.getDuty())){//兴趣班教师
				teachervo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_INTEREST,0,teachervo.getContact_id()));
				teachervo.setDuty_name(teachervo.getClass_name()+MsgService.getMsg("TEACHER"));
				teachervo.setTeacher_id(classlist.get(0).getTeacher_id());
			}else {//校领导，行政管理，自定义
				if (StringUtil.isEmpty(teachervo.getDuty())) continue;
				else {
					DictVO dict = new DictVO();
					dict.setSchool_id(ActionUtil.getSchoolID());
					dict.setDict_code(teachervo.getDuty());
					DictVO dvo = dao.queryObject("dictMap.getDictSchoolList", dict);
					teachervo.setDuty_name(dvo.getDict_value());
					teachervo.setClass_id(0);
					TeacherVO teacher = dao.queryObject("teacherMap.getTeacherByDuty", teachervo);
					teachervo.setTeacher_id(teacher.getTeacher_id());
				}
            }
		}
		return teacherList;
	}

	public List<TeacherVO> getTeacherListByRole(TeacherVO vo){
		return dao.queryForList("teacherMap.getTeacherListByDuty",vo);
	}

	public List<TeacherVO> deleteTeacherList(String teacher_ids) {
		List<TeacherVO> tlist = new ArrayList<TeacherVO>();
		if (StringUtil.isEmpty(teacher_ids)) return tlist;
		String[] teacherIDs = teacher_ids.split(",");
		for (String teacher_id : teacherIDs) {
			if (StringUtil.isEmpty(teacher_id)) continue;
			List<TeacherVO> list = deleteTeacherDuty(IntegerUtil.getValue(teacher_id));
			tlist.addAll(list);
		}
		return tlist;
	}

	//获取教师角色
	public List<TeacherVO> getTeacherDuty(String phone){
		TeacherVO vo=new TeacherVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setPhone(phone);
		return dao.queryForList("teacherMap.getTeacherDuty",vo);
	}
}
