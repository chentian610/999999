package com.ninesky.classtao.contact.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.classtao.contact.vo.ScheduleVO;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;

public interface ContactService {
	/**
	 * 获取学校用户通讯录
	 * @param vo
	 * @return
	 */
	public List<ContactListVO> getContactList(String duty);
	/**
	 * 新增自定义通讯录组及组成员
	 * @param vo
	 * @return
	 */
	public void addContactGroup(ContactVO vo);
	/**
	 * 删除自定义通讯录组及组成员
	 * @param request
	 * @return
	 */
	public void deleteContactGroup(ContactVO vo);
	/**
	 * 为指定的组添加成员
	 * @param vo
	 * @return
	 */
	public void addContactListByGroup(ContactVO vo);
	/**
	 * 为指定的组删除成员
	 * @param vo
	 * @return
	 */
	public void deleteContactByGorup(ContactListVO vo);
	/**
	 * 获取自定义通讯录组集合
	 * @param vo
	 * @return
	 */
	public List<ContactVO> getContactGroupList(ContactVO vo);
	/**
	 * 获取自定义通讯录组中的联系人集合
	 * @param vo
	 * @return
	 */
	public List<ContactListVO> getContactListByGroup(ContactListVO vo);
	/**
	 * 用户信息变更时候（姓名，头像，电话）修改自定义分组中的相应联系人
	 * @param paramMap
	 */
	public void updateContactList(Map<String, Object> paramMap);
	
	public ContactVO addContact(ContactVO vo);//花名册新建分组,只输分组名
	
	public List<ContactVO> getContactGroupListOfManager(ContactVO vo);//后台花名册使用，获取分组集合
	
	public List<TeacherVO> getTeacherContactList(TeacherVO vo);//根据班级id获取教师信息
	
	public List<ParentVO> getParentContactListByStudentID(ParentVO vo);//根据班级id获取教师信息
	
	public List<StudentVO> getStudentContactListByStudentID(StudentVO vo);//根据班级id获取教师信息

	public List<ContactListVO> getContactBySearch(Map<String, String> map);//根据关键字查询全校师生

	public List<ContactListVO> getTeaStudentContactList(Map<String, String> map);

	public List<ContactVO> getInterestClassList(ContactVO vo);//获取兴趣班

	public void addSchedule(String schedule_list, Integer contact_id);//创建自定义课程表

	public void enrollClass(ContactListVO vo);//报名兴趣班

	public List<StudentVO> getUnContactStuList(Integer contact_id);//已报名，但还未被选择到兴趣班

	public ContactVO getContactByID(Integer contact_id);//获取兴趣班详情

	public void updateInterestClass(ContactVO vo);//修改兴趣班

	public List<ContactVO> getInterestByName(ContactVO vo);//通过班级名称获取兴趣班

	public List<TeacherVO> getTeacherListByPhone(String phone);//根据电话号码获取职务信息
}
