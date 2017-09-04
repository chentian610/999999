package com.ninesky.classtao.contact.service.impl;

import com.ninesky.classtao.contact.service.ContactService;
import com.ninesky.classtao.contact.vo.*;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service("ContactServiceImpl")
public class ContactServiceImpl implements ContactService {

	private Lock lock = new ReentrantLock();

	private Map<Integer,ReadWriteLock> lockMap = new ConcurrentHashMap<Integer,ReadWriteLock>();

	@Resource
	private GeneralDAO dao;
	@Resource
	private UserService userService;
	@Resource
	private ClassService classService;
	@Resource
	private RedisService redisService;
	@Resource
	private InfoService infoService;
	@Resource
	private DictService dictService;
	@Autowired
	private JedisDAO jedisDao;
	@Autowired
	private DynamicService dynamicService;
	@Autowired
	private GetuiService getuiService;

	/**
	 * 获取学校用户通讯录
	 * 
	 * @param duty
	 * @return
	 */
	public List<ContactListVO> getContactList(String duty) {
		if (StringUtil.isEmpty(duty)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		if (DictConstants.DICT_TEACHER.equals(duty))  duty = "";
		List<ContactListVO> contactList = new ArrayList<ContactListVO>();
		setTeacherContactListByDuty(contactList,duty);
		if (ListUtil.isEmpty(contactList))
			throw new BusinessException(MsgService.getMsg("NO_CONTACT"));
		// 按名字拼音排序
		Collections.sort(contactList);
		return contactList;    
	}

	/**
	 * 新增自定义通讯录组及组成员
	 * 
	 * @param vo
	 * @return
	 */
	public void addContactGroup(ContactVO vo) {
		if (StringUtil.isEmpty(vo.getContact_name()))
			throw new BusinessException(MsgService.getMsg("NO_CONTACT_NAME"));
		if (DictConstants.USERTYPE_ADMIN.equals(vo.getUser_type()))
			vo.setUser_id(0);
		vo.setContact_id(dao.insertObjectReturnID("contactMap.insertContact",
				vo));
		if (StringUtil.isEmpty(vo.getItem_list()))
			return;
		List<ContactListVO> contacts = BeanUtil.jsonToList(vo.getItem_list(),
				ContactListVO.class);
		for (ContactListVO contact : contacts) {
			contact.setUser_id(vo.getUser_id());
			contact.setContact_id(vo.getContact_id());
			contact.setSchool_id(vo.getSchool_id());
			contact.setCreate_by(vo.getCreate_by());
			contact.setCreate_date(vo.getCreate_date());
		}
		dao.insertObject("contactListMap.insertContactList", contacts);
	}

	/**
	 * 删除自定义通讯录组及组成员
	 * 
	 * @param vo
	 * @return
	 */
	public void deleteContactGroup(ContactVO vo) {
        vo.setIs_active(0);
        vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("contactMap.updateContact", vo);
		//删除绑定该兴趣班的相关教师
		userService.deleteTeacherByContactID(vo.getContact_id());
	}

	/**
	 * 为指定的组添加成员
	 * 
	 * @param vo
	 * @return
	 */
	public void addContactListByGroup(ContactVO vo) {
		if (IntegerUtil.isEmpty(vo.getContact_id()))
			throw new BusinessException(MsgService.getMsg("SPECIFY_CONTACT"));
		if (StringUtil.isEmpty(vo.getItem_list()))
			throw new BusinessException(MsgService.getMsg("NO_CONTACT_ADD"));
		List<ContactListVO> contacts = BeanUtil.jsonToList(vo.getItem_list(),ContactListVO.class);
		for (ContactListVO contact : contacts) {
			if (IntegerUtil.isEmpty(contact.getUser_id())) contact.setUser_id(0);// 未注册的教师的user_id为0
			contact.setContact_id(vo.getContact_id());
			contact.setSchool_id(ActionUtil.getSchoolID());
			String all_letter = LetterUtil.converterToSpell(contact.getUser_name());// 获取首字母缩写converterToFirstSpell
			String first_letter = LetterUtil.converterToFirstSpell(contact.getUser_name());
			contact.setAll_letter(all_letter);
			contact.setFirst_letter(first_letter);
			contact.setCreate_by(vo.getCreate_by());
			contact.setCreate_date(vo.getCreate_date());
		}
		dao.insertObject("contactListMap.insertContactList", contacts);
		ContactVO contactVO=dao.queryObject("contactMap.getContactById",vo.getContact_id());
		String teacher_name="";
		if (StringUtil.isNotEmpty(contactVO.getPhone())) {
			TeacherVO teacher = new TeacherVO();
			teacher.setSchool_id(ActionUtil.getSchoolID());
			teacher.setPhone(contactVO.getPhone());
			teacher_name = userService.getTeacherListByPhone(teacher).get(0).getTeacher_name();
		}
		String date = DateUtil.formatDate(ActionUtil.getSysTime(), "yyyy-MM-dd HH:mm");
		String stuContent = MsgService.getMsg("APPLY_SUCCESS_CONTENT", date, teacher_name);
		for (ContactListVO contact : contacts) {//若已退出班级表中有添加的学生，则删除
			InterestOutVO out=new InterestOutVO();
			out.setContact_id(vo.getContact_id());
			out.setStudent_id(contact.getStudent_id());
			InterestOutVO outVO=dao.queryObject("interestOutMap.getInterestOut",out);
			if (outVO!=null) dao.deleteObject("interestOutMap.deleteOut",out);
			//动态推送  在报名时间之内才推送
			if (ActionUtil.getSysTime().getTime()<contactVO.getApply_end_date().getTime()) {
				String stuTitle = MsgService.getMsg("APPLY_SUCCESS_TITLE", contact.getUser_name(), contactVO.getContact_name());
				HashMap<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("info_title", stuTitle);
				dataMap.put("info_content", stuContent);
				dataMap.put("module_code", DictConstants.MODULE_CODE_CURRICULA_VARIABLE);
				dataMap.put("module_pkid", vo.getContact_id().toString());
				dataMap.put("student_id", contact.getStudent_id().toString());
				dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
				dataMap.put("info_url", "detail.html");
				dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
				dataMap.put("contact_id", contactVO.getContact_id().toString());
				dynamicService.insertDynamicByStuID(dataMap, contact.getStudent_id());
				dataMap.remove("info_content");
				getuiService.pushMessageByStuID(dataMap, contact.getStudent_id());
			}
		}
		//redis,user_group
		for (ContactListVO contact:contacts) {
			if (DictConstants.USERTYPE_STUDENT.equals(contact.getUser_type())) {
				redisService.addUserToUserGroup(DictConstants.USERTYPE_STUDENT,DictConstants.TEAM_TYPE_INTEREST, 0,
						contact.getContact_id(),0,contact.getStudent_id());
			}
		}
	}

	/**
	 * 为指定的组删除成员
	 * 
	 * @param vo
	 * @return
	 */
	public void deleteContactByGorup(ContactListVO vo) {
		dao.deleteObject("contactListMap.deleteContactById", vo);
		ContactVO contact=dao.queryObject("contactMap.getContactById",vo.getContact_id());
		if (contact.getStart_date().getTime()<=ActionUtil.getSysTime().getTime()){//已开课的情况下，删除学生要加入已退出班级表
			dao.insertObject("interestOutMap.insertOut",vo);
		} else {
			//动态推送
			TeacherVO teacher=new TeacherVO();
			teacher.setSchool_id(ActionUtil.getSchoolID());
			teacher.setPhone(contact.getPhone());
			String student_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,vo.getStudent_id());
			String stuTitle=MsgService.getMsg("APPLY_FAIL_TITLE",student_name,contact.getContact_name());
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("info_title",stuTitle);
			dataMap.put("info_content","");
			dataMap.put("module_code",DictConstants.MODULE_CODE_CURRICULA_VARIABLE);
			dataMap.put("module_pkid",vo.getContact_id().toString());
			dataMap.put("student_id", vo.getStudent_id().toString());
			dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url","detail.html");
			dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
			dataMap.put("contact_id",vo.getContact_id().toString());
			dynamicService.insertDynamicByStuID(dataMap,vo.getStudent_id());
			getuiService.pushMessageByStuID(dataMap,vo.getStudent_id());
		}
		//redis,user_group
		if (IntegerUtil.isNotEmpty(vo.getStudent_id())) {
			redisService.deleteTeamFromUserGroup(DictConstants.TEAM_TYPE_INTEREST,DictConstants.USERTYPE_STUDENT,
					0,vo.getContact_id(),0, vo.getStudent_id());
		}
	}

	/**
	 * 获取自定义通讯录组集合
	 * 
	 * @param vo
	 * @return
	 */
	public List<ContactVO> getContactGroupList(ContactVO vo) {
		vo.setUser_id(0);
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ContactVO> list = dao.queryForList(
				"contactMap.getContactListByManager", vo);
		vo.setUser_id(ActionUtil.getUserID());
		if (DictConstants.USERTYPE_ADMIN.equals(vo.getUser_type()))
			vo.setUser_id(0);
		List<ContactVO> list1 = dao.queryForList(
				"contactMap.getContactListByUserId", vo);
		list.addAll(list1);
		DictVO dict = new DictVO();
		dict.setDict_group(DictConstants.DICT_TEACHER);
		List<DictVO> listDict = dictService.getDictList(dict);
		ContactVO cvo = new ContactVO();
		cvo.setSchool_id(ActionUtil.getSchoolID());
		cvo.setContact_name("班主任");
		cvo.setUser_type(DictConstants.DICT_TEACHER_ADVISER);
		list.add(cvo);
		for (DictVO dictVO : listDict) {
			ContactVO contactVO = new ContactVO();
			contactVO.setSchool_id(dictVO.getSchool_id());
			contactVO.setContact_name(dictVO.getDict_value());
			contactVO.setUser_type(dictVO.getDict_code());
			list.add(contactVO);
		}
		return list;
	}

	/**
	 * 获取自定义通讯录组中的联系人集合
	 * 
	 * @param vo
	 * @return
	 */
	public List<ContactListVO> getContactListByGroup(ContactListVO vo) {
		return dao.queryForList("contactListMap.getContactListByGroup", vo);
	}

	/**
	 * 用户信息变更时候（姓名，头像，电话）修改自定义分组中的相应联系人
	 * 
	 * @param paramMap
	 */
	public void updateContactList(Map<String, Object> paramMap) {
		dao.updateObject("contactListMap.updateContactList", paramMap);
	}

	// 获取学校或班级所有老师联系人
	private List<ContactListVO> setTeacherContactListByDuty(List<ContactListVO> list, String duty) {
		Integer is_charge = 1;
		List<ContactListVO> teacherList = new ArrayList<ContactListVO>();
		TeacherVO teacherVO = new TeacherVO();
		teacherVO.setSchool_id(ActionUtil.getSchoolID());
		teacherVO.setIs_graduate(0);
		if (Constants.SORT_UP.equals(ActionUtil.getParameter("is_filtered"))) teacherVO.setIs_filtered(IntegerUtil.getValue(ActionUtil.getParameter("is_filtered")));
		if (DictConstants.DICT_TEACHER_ADVISER.equals(duty)){
			teacherVO.setIs_charge(is_charge);
		} else if (DictConstants.DICT_TEACHER_INTEREST.equals(duty)){
			teacherVO.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
		} else {
			teacherVO.setDuty(duty);
		}
		List<TeacherVO> teachers = userService.getTeaUserListByDuty(teacherVO);
		teachers = removeDuplicate(teachers);
		for (TeacherVO teacher : teachers) {
			ContactListVO contact = new ContactListVO();
			contact.setUser_id(teacher.getUser_id());
			contact.setUser_name(teacher.getTeacher_name());
			contact.setHead_url(teacher.getHead_url());
			contact.setUser_type(DictConstants.USERTYPE_TEACHER);
			contact.setFirst_letter(teacher.getFirst_letter());
			contact.setAll_letter(teacher.getAll_letter());
			contact.setStudent_id(0);
			contact.setRole_tags(setRoleTags(teacher.getPhone()));
			contact.setPhone(teacher.getPhone());
			teacherList.add(contact);
		}
		list.addAll(teacherList);
		return list;
	}

	private String setRoleTags(String phone){
		List<TeacherVO> list = redisService.getTeacherDuty(phone);
		for (TeacherVO vo:list) {
			if (StringUtil.isEmpty(vo.getDuty())) continue;
			if (DictConstants.DICT_TEACHER_CLASS.equals(vo.getDuty()) && StringUtil.isNotEmpty(vo.getCourse())) {
				vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
				vo.setDuty_name(redisService.getDictValue(vo.getCourse())+"老师");
			} else if (DictConstants.DICT_TEACHER_INTEREST.equals(vo.getDuty()) && StringUtil.isNotEmpty(vo.getCourse())) {
				vo.setTeam_type(DictConstants.TEAM_TYPE_INTEREST);
				vo.setDuty_name(redisService.getDictValue(vo.getCourse())+"老师");
			} else {
				vo.setDuty_name(redisService.getDictValue(vo.getDuty()));
			}
		}
		return BeanUtil.ListTojson(list,false);
	}

	//去除重复的教师user_id
	private List<TeacherVO> removeDuplicate(List<TeacherVO> list){
		List<TeacherVO> teacherList = new ArrayList<TeacherVO>();
		HashMap<String,TeacherVO> teacherMap = new HashMap<String,TeacherVO>();
		//先将所有接受者存放到HashMap中
		for (TeacherVO vo:list) {
			if (teacherMap.containsKey(vo.getPhone())) continue;//判断键值是否存在，(存在直接返回)
			teacherMap.put(vo.getPhone(),null);//先将所有接受者存放到HashMap中
			teacherList.add(vo);
		}
		return teacherList;
	}

	// 根据class_ID获取班级老师联系人
	@Override
	public List<TeacherVO> getTeacherContactList(TeacherVO vo) {
		List<TeacherVO> teachers = userService.getTeaUserListByDuty(vo);
		teachers = removeDuplicate(teachers);//过滤重复的教师身份
        for (TeacherVO teacherVO :teachers) {
            teacherVO.setRole_tags(setRoleTags(teacherVO.getPhone()));
        }
		return teachers;
	}

	public List<TeacherVO> getTeacherListByPhone(String phone) {
		List<TeacherVO> list = classService.getDutyNameList(phone);
		for (TeacherVO teacherVO :list) {
			teacherVO.setRole_tags(setRoleTags(teacherVO.getPhone()));
		}
		return list;
	}

	// 发送动态
	private void addInfomation() {
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setModule_code(DictConstants.MODULE_CODE_CONTACT);
		infoVO.setModule_pkid(0);
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);// 本地模块消息
		infoVO.setInfo_title(MsgService.getMsg("UPDATED_CONTACT"));
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		infoVO.setInfo_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		infoVO.setReceive_list(getReceiveList());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoService.addInfo(infoVO);
	}

	// 获取接收对象的json数组格式
	private String getReceiveList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grade_id", 0);
		map.put("class_id", 0);// 全校都发送动态
		map.put("user_type", "");// 默认全部发送动态
		JSONArray array = JSONArray.fromObject(map);
		return array.toString();
	}

	// 花名册新建分组，只输分组名
	public ContactVO addContact(ContactVO vo) {
		List<ContactVO> contactVOList=getInterestByName(vo);//判断班级名称是否已被使用过
		if (ListUtil.isNotEmpty(contactVOList)) throw new BusinessException(MsgService.getMsg("SAME_INTEREST_NAME"));
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUser_id(0);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setContact_id(dao.insertObjectReturnID("contactMap.insertContactManager", vo));
		DictVO dict=dao.queryObject("dictMap.getCourse", vo);
		vo.setCourse_name(dict.getDict_value());
		if (StringUtil.isNotEmpty(vo.getSchedule())){
			addSchedule(vo.getSchedule(),vo.getContact_id());//自定义课程表
		}
		String key = RedisKeyUtil.getInterstKey(ActionUtil.getSchoolID(),vo.getContact_id());
		jedisDao.hset(key,"count",vo.getApply_count().toString());
		//添加教师身份
		TeacherVO teacher=new TeacherVO();
		teacher.setClass_id(vo.getContact_id());
		teacher.setPhone(vo.getPhone());
		teacher.setTeacher_name(vo.getTeacher_name());
		teacher.setClass_name(vo.getContact_name());
		teacher.setDuty(DictConstants.DICT_TEACHER_INTEREST);
		teacher.setIs_charge(0);
		userService.addInterestTeacher(teacher);
		return vo;
	}

	// 后台花名册使用，获取分组集合
	public List<ContactVO> getContactGroupListOfManager(ContactVO vo) {
		vo.setUser_id(0);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setIs_active(1);
		vo.setUser_type(DictConstants.USERTYPE_STUDENT);
		List<ContactVO> list = dao.queryForList("contactMap.getContactListByManager", vo);
		for (ContactVO cvo : list) {
			cvo.setUser_type(DictConstants.USERTYPE_STUDENT);
			int count = dao.queryObject("contactListMap.getCount",cvo);
			cvo.setCount(count);
			DictVO dict=dao.queryObject("dictMap.getCourse", cvo);
			if (dict==null) continue;
			cvo.setCourse_name(dict.getDict_value());
		}
		return list;
	}

	//获取兴趣班列表
	public List<ContactVO> getInterestClassList(ContactVO vo) {
		vo.setUser_type(DictConstants.USERTYPE_STUDENT);
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ContactVO> list=new ArrayList<ContactVO>();
		if (DictConstants.USERTYPE_ADMIN.equals(ActionUtil.getUserType())){//网页端
			if (vo.getIs_my()==1) vo.setCreate_by(ActionUtil.getUserID());//我创建的
			vo.setEnd_date(ActionUtil.getSysTime());
			list=dao.queryForList("contactMap.getInterestClassToManage",vo);
		}else {//APP端
			if (vo.getIs_my()==0) {//全部（报名时间未结束）
				vo.setCreate_date(ActionUtil.getSysTime());
				list = dao.queryForList("contactMap.getInterestClass", vo);
			} else if (vo.getIs_my()==1) {//我报名的
				list=dao.queryForList("interestApplyMap.getApplyListByStuID",vo);
			}
		}
		for (ContactVO cvo : list) {
			setDetail(cvo);
			setApplyStatus(cvo);
		}
		return list;
	}

	private void setDetail(ContactVO cvo) {
		ActionUtil.setPage_app(false);      //内部代码list不分页
		ActionUtil.setPage_web(false);
		if (IntegerUtil.isNotEmpty(cvo.getUser_id()))
			cvo.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,cvo.getUser_id(),0));
		else if (StringUtil.isNotEmpty(cvo.getPhone())){
			TeacherVO tvo=new TeacherVO();
			tvo.setSchool_id(ActionUtil.getSchoolID());
			tvo.setPhone(cvo.getPhone());
			List<TeacherVO> teacherList=userService.getTeacherListByPhone(tvo);
			cvo.setTeacher_name(teacherList.get(0).getTeacher_name());
		} else if (StringUtil.isEmpty(cvo.getPhone())) {//历史数据，取所有绑定该兴趣班的教师姓名
			List<String> list=dao.queryForList("teacherMap.getTeacherNameByInterest",cvo.getContact_id());
			String teacher_name="";
			for (String ss:list){
				if (StringUtil.isEmpty(teacher_name)) teacher_name=ss;
				else teacher_name=teacher_name+"，"+ss;
			}
			cvo.setTeacher_name(teacher_name);
		}
		if (IntegerUtil.isNotEmpty(cvo.getApply_count())) {
			List<InterestApplyVO> applyList = dao.queryForList("interestApplyMap.getApplyList",cvo.getContact_id());
			cvo.setCount(cvo.getApply_count() - applyList.size());//剩余人数
		}
		DictVO dict=dao.queryObject("dictMap.getCourse", cvo);
		cvo.setCourse_name(dict.getDict_value());
		int count = dao.queryObject("contactListMap.getCount",cvo);
		cvo.setExist_count(count);//目前兴趣班人数
		if (StringUtil.isEmpty(cvo.getSchedule_url())){//查自定义课程表
			List<ScheduleVO> scheList=dao.queryForList("scheduleMap.getScheduleList",cvo.getContact_id());
			cvo.setSchedule(BeanUtil.ListTojson(scheList));
		}
	}

	@Override
	public List<ParentVO> getParentContactListByStudentID(ParentVO vo) {
		List<ParentVO> parentVO = userService.getParUserList(vo);
		return parentVO;
	}

	@Override
	public List<StudentVO> getStudentContactListByStudentID(StudentVO vo) {
		List<StudentVO> studentList = userService.getStuUserList(vo);
		if (ListUtil.isEmpty(studentList)) studentList = getStudentContactList(vo);
		for (StudentVO studentVO : studentList) {
			String first = LetterUtil.converterToFirstSpell(redisService.getUserName(studentVO.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, studentVO.getStudent_id()));
			String all = LetterUtil.converterToSpell(redisService.getUserName(studentVO.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, studentVO.getStudent_id()));
			studentVO.setAll_letter(all);
			studentVO.setFirst_letter(first);
			studentVO.setUser_type(DictConstants.USERTYPE_STUDENT);
			vagueStudentCode(studentVO);//模糊student_code
		}
		return studentList;
	}

	public void vagueStudentCode(StudentVO vo){
		if (StringUtil.isEmpty(vo.getStudent_code())) return;
		if (18 > vo.getStudent_code().length()) return;
		String student_code = "";
		for (int i = 0; i < vo.getStudent_code().length() ; i++ ) {
			if (7 < i && 15 > i) student_code += "*";
			else student_code += vo.getStudent_code().charAt(i)+"";
		}
		vo.setStudent_code(student_code);
	}
	
	private List<StudentVO> getStudentContactList(StudentVO vo){
		Map<String, String> map = new HashMap<String, String>();
		map.put("contact_id", vo.getClass_id()+"");
		map.put("school_id", vo.getSchool_id()+"");
		map.put("user_type", DictConstants.USERTYPE_STUDENT);
		List<StudentVO> studentList  = dao.queryForList("contactListMap.getStudentContactList", map);
		if (ListUtil.isEmpty(studentList)) return studentList;
		for (StudentVO studentVO : studentList) {
			studentVO.setStudent_name(redisService.getUserName(studentVO.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, studentVO.getStudent_id()));
			studentVO.setHead_url(redisService.getUserHeadUrl(studentVO.getSchool_id(), DictConstants.USERTYPE_STUDENT, 0, studentVO.getStudent_id()));
		}
		return studentList;
	}

	public List<ContactListVO> getContactBySearch(Map<String,String> map){
		List<ContactListVO> contact = dao.queryForList("contactListMap.getContactBySearch",map);
		for (ContactListVO vo:contact){
			if (IntegerUtil.isEmpty(vo.getUser_id())) continue;
			vo.setUser_name(redisService.getUserName(vo.getSchool_id(), vo.getUser_type(), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?vo.getUser_id():0), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?0:vo.getUser_id())));
			vo.setHead_url(redisService.getUserHeadUrl(vo.getSchool_id(), vo.getUser_type(), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?vo.getUser_id():0), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?0:vo.getUser_id())));
			if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())) {
				vo.setPhone(redisService.getUserPhone(vo.getSchool_id(),vo.getUser_type(),vo.getUser_id()));
				vo.setRole_tags(setRoleTags(vo.getPhone()));
			} else {
				vo.setClass_name(redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,vo.getGrade_id(),vo.getClass_id()));
			}
			if (StringUtil.isEmpty(vo.getFirst_letter())){
				vo.setFirst_letter(LetterUtil.converterToFirstSpell(vo.getUser_name()));
			}
			if (StringUtil.isEmpty(vo.getAll_letter())){
				vo.setAll_letter(LetterUtil.converterToSpell(vo.getUser_name()));
			}
		}
		return contact;
	}

	public List<ContactListVO> getTeaStudentContactList(Map<String,String> map){
		List<ContactListVO> contact = new ArrayList<ContactListVO>();
		if (DictConstants.TEAM_TYPE_INTEREST.equals(map.get("team_type")))//根据team_type获取相应类型的师生列表
			contact = dao.queryForList("contactListMap.getContactTeaStuContactList",map);
		else contact = dao.queryForList("contactListMap.getClassTeaStuContactList",map);
		if (ListUtil.isEmpty(contact)) return contact;
		for (ContactListVO vo:contact){
			if (IntegerUtil.isEmpty(vo.getUser_id())) continue;
			vo.setUser_name(redisService.getUserName(vo.getSchool_id(), vo.getUser_type(), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?vo.getUser_id():0), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?0:vo.getUser_id())));
			vo.setHead_url(redisService.getUserHeadUrl(vo.getSchool_id(), vo.getUser_type(), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?vo.getUser_id():0), (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())?0:vo.getUser_id())));
			if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())) vo.setPhone(redisService.getUserPhone(vo.getSchool_id(),vo.getUser_type(),vo.getUser_id()));
			vo.setFirst_letter(LetterUtil.converterToFirstSpell(vo.getUser_name()));
			vo.setAll_letter(LetterUtil.converterToSpell(vo.getUser_name()));
		}
		return contact;
	}

	//自定义课程表
	public void addSchedule(String schedule_list,Integer contact_id){
		List<ScheduleVO> list=BeanUtil.jsonToList(schedule_list,ScheduleVO.class);
		for (ScheduleVO vo:list){
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
            vo.setContact_id(contact_id);
			dao.insertObject("scheduleMap.insertSchedule",vo);
		}
	}

	//报名
	public void enrollClass(ContactListVO vo){
		String key = RedisKeyUtil.getInterstKey(ActionUtil.getSchoolID(),vo.getContact_id());
		Integer enroll_count = IntegerUtil.getValue(jedisDao.hget(key,"enroll_count"));//已报名数
		Integer count = IntegerUtil.getValue(jedisDao.hget(key,"count"));//可报名数
		if (enroll_count!=null && enroll_count >= count) throw new BusinessException(MsgService.getMsg("INTERST_CLASS_ISFULL"));
		if (!lockMap.containsKey(vo.getContact_id())) lockMap.put(vo.getContact_id(), new
				ReentrantReadWriteLock());
		lockMap.get(vo.getContact_id()).writeLock().lock();
		try {
			//防止并发重复写入，超出范围
			if (IntegerUtil.getValue(jedisDao.hget(key,"enroll_count"))!=null &&
					IntegerUtil.getValue(jedisDao.hget(key,"enroll_count"))>= count) {
				throw new BusinessException(MsgService.getMsg("INTERST_CLASS_ISFULL"));
			}
			//具体业务
			jedisDao.hincrBy(key,"enroll_count",1);
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("interestApplyMap.insertApply",vo);
			ContactVO cvo=dao.queryObject("contactMap.getContactById",vo.getContact_id());
			if (cvo.getIs_grab()==1){//抢报模式,直接报名
				vo.setSchool_id(ActionUtil.getSchoolID());
				vo.setUser_type(DictConstants.USERTYPE_STUDENT);
				String all_letter = LetterUtil.converterToSpell(vo.getUser_name());// 获取首字母缩写converterToFirstSpell
				String first_letter = LetterUtil.converterToFirstSpell(vo.getUser_name());
				vo.setFirst_letter(first_letter);
				vo.setAll_letter(all_letter);
				vo.setCreate_by(ActionUtil.getUserID());
				vo.setCreate_date(ActionUtil.getSysTime());
				vo.setUser_id(0);
				Integer id=dao.insertObjectReturnID("contactListMap.insertContact",vo);
				//动态推送
				TeacherVO teacher=new TeacherVO();
				teacher.setSchool_id(ActionUtil.getSchoolID());
				teacher.setPhone(cvo.getPhone());
				String teacher_name=userService.getTeacherListByPhone(teacher).get(0).getTeacher_name();
				String date=DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd HH:mm");
				String stuTitle=MsgService.getMsg("APPLY_SUCCESS_TITLE",vo.getUser_name(),cvo.getContact_name());
				String stuContent=MsgService.getMsg("APPLY_SUCCESS_CONTENT",date,teacher_name);
				HashMap<String,String> dataMap = new HashMap<String,String>();
				dataMap.put("info_title",stuTitle);
				dataMap.put("info_content",stuContent);
				dataMap.put("module_code",DictConstants.MODULE_CODE_CURRICULA_VARIABLE);
				dataMap.put("module_pkid",vo.getContact_id().toString());
				dataMap.put("student_id", vo.getStudent_id().toString());
				dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
				dataMap.put("info_url","detail.html");
				dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
				dataMap.put("contact_id",vo.getContact_id().toString());
				dynamicService.insertDynamicByStuID(dataMap,vo.getStudent_id());
				dataMap.remove("info_content");
				getuiService.pushMessageByStuID(dataMap,vo.getStudent_id());
			}
		} finally {
			lockMap.get(vo.getContact_id()).writeLock().unlock();
		}
	}

	//已报名，但还未被选择到兴趣班
	public List<StudentVO> getUnContactStuList(Integer contact_id){
		List<StudentVO> slist=new ArrayList<StudentVO>();
		List<InterestApplyVO> list=dao.queryForList("interestApplyMap.getApplyUnContactList",contact_id);
		for(InterestApplyVO vo:list){
			StudentVO studentVO=dao.queryObject("studentMap.getStudentById", vo.getStudent_id());
			ClassVO classVO=new ClassVO();
			classVO.setClass_id(studentVO.getClass_id());
			classVO.setSchool_id(ActionUtil.getSchoolID());
			classVO=dao.queryObject("classMap.getClassListByClass", classVO);
			studentVO.setClass_name(classService.getClassByID(classVO.getClass_id()).getClass_name());
			slist.add(studentVO);
		}
		return slist;
	}

	//获取兴趣班详情
	public ContactVO getContactByID(Integer contact_id) {
		ContactVO vo=dao.queryObject("contactMap.getContactById",contact_id);
		setApplyStatus(vo);//设置报名状态
		setDetail(vo);
		return vo;
	}

	//设置报名状态
	private void setApplyStatus(ContactVO vo) {
		Date date = ActionUtil.getSysTime();
		ContactListVO clvo = new ContactListVO();
		clvo.setContact_id(vo.getContact_id());
		clvo.setSchool_id(ActionUtil.getSchoolID());
		clvo.setStudent_id(ActionUtil.getStudentID());
		ContactListVO conVO = dao.queryObject("contactListMap.getStudent", clvo);
		InterestOutVO out = new InterestOutVO();
		out.setContact_id(vo.getContact_id());
		out.setStudent_id(ActionUtil.getStudentID());
		InterestOutVO outVO = dao.queryObject("interestOutMap.getInterestOut", out);
		InterestApplyVO apply=new InterestApplyVO();
		apply.setContact_id(vo.getContact_id());
		apply.setStudent_id(ActionUtil.getStudentID());
		InterestApplyVO applyVO=dao.queryObject("interestApplyMap.getApplyByStuID",apply);
		if (applyVO==null && conVO==null){
			vo.setIs_success(DictConstants.UN_APPLY);//未报名
		} else if (outVO != null) {
			vo.setIs_success(DictConstants.APPLY_OUT_CLASS);//已退出班级
		} else if (conVO != null) {
			vo.setIs_success(DictConstants.APPLY_SUCCESS);//成功报名
		} else if (conVO == null && vo.getIs_grab() == 0 && vo.getStart_date().getTime() > date.getTime()) {
			vo.setIs_success(DictConstants.APPLY_WAIT_SCREEN);//等待筛选(未开课前，非抢报)
		} else if (conVO == null && vo.getIs_grab() == 0 && vo.getStart_date().getTime() < date.getTime()) {
			vo.setIs_success(DictConstants.APPLY_FAIL);//报名失败（已开课）
		} else if (conVO == null && vo.getIs_grab() == 1) {
			vo.setIs_success(DictConstants.APPLY_FAIL);//报名失败
		}
	}

	//修改兴趣班
	public void updateInterestClass(ContactVO vo){
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("contactMap.updateInterestClass",vo);
		dao.deleteObject("scheduleMap.deleteSchedule",vo.getContact_id());
		if (StringUtil.isNotEmpty(vo.getSchedule())) {
			List<ScheduleVO> list=BeanUtil.jsonToList(vo.getSchedule(),ScheduleVO.class);
			for (ScheduleVO scheduleVO:list) {
				scheduleVO.setContact_id(vo.getContact_id());
				scheduleVO.setCreate_date(ActionUtil.getSysTime());
				scheduleVO.setCreate_by(ActionUtil.getUserID());
				dao.insertObject("scheduleMap.insertSchedule",scheduleVO);
			}
		}
	}

	//通过兴趣班名称获取兴趣班
	public List<ContactVO> getInterestByName(ContactVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		return dao.queryForList("contactMap.getInterestByName",vo);
	}
}