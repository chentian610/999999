package com.ninesky.classtao.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserRoleVO;
import com.ninesky.classtao.user.vo.UserSnsVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.classtao.user.vo.ValidateCodeVO;
import com.ninesky.common.vo.ReceiveVO;

public interface UserService {
	
	public UserVO insertUser(UserVO vo, int school_id);
	/**
	 * 添加学校管理员
	 * @param vo
	 * @return
	 */
	public UserVO insertAdmin(UserVO vo);
	
	public UserVO validateUser(UserVO vo);
	
	public UserVO validateAdmin(UserVO vo);
	
	public UserVO getUserByID(Integer user_id);
	
	public UserVO getUserByPhone(String phone);
	
	public void updateValidateCode(ValidateCodeVO vo);

	public boolean checkValidateCode(HashMap<String, String> map);
	
	public void completeUserInfo(UserVO vo);

	public void resetPassword(UserVO vo);

	public void modifyPhone(UserVO vo);//修改手机号码
	
	public void updatePassword(UserVO vo);//修改密码

	public void signToXinge(UserVO vo);
	
	public List<TeacherVO> addTeacherDuty(TeacherVO vo);//给教师添加职务
	
	public List<TeacherVO> addTeacherInterestDuty(TeacherVO vo);
	
	public ParentVO addParentChild(ParentVO vo);//给家长添加孩子

	public List<ReceiveVO> getTeamList(ParentVO vo);//孩子所在的班级，兴趣班集合

	public ParentVO updateChildByStudentCode(ParentVO vo);//完善(更新)孩子信息
	
	public UserSnsVO addUserSns(UserSnsVO vo);//绑定社交帐号
	
	public List<UserSnsVO> getUserSnsList(UserSnsVO vo);//获取社交帐号
	
	public List<UserSnsVO> getParentSnsAccount(String snsType, Integer studentId);//返回学生家长指定社交类型的社交账号
	
	public List<StudentVO> getStuUserList(StudentVO vo);//根据年级、班级获取学生user列表
	
	public List<TeacherVO> getTeaUserList(TeacherVO vo);//根据年级、班级获取教师user列表
	
	public List<TeacherVO> getTeaUserListByDuty(TeacherVO vo);//根据年级、班级和duit获取教师user列表
	
	public List<ParentVO> getParUserList(ParentVO vo);//根据年级、班级获取家长user列表
	
	public void judgeStudent(ParentVO vo);//家长添加孩子时，判断该学生是否符合条件
	
	public List<TeacherVO> deleteTeacherDuty(Integer teacher_id);//删除教师职务
	
	public StudentVO getStudentById(Integer student_id);
	
	public StudentVO getStudentByCode(Map<String, Object> paramMap);//根据学生号获取学生
	
	public List<TeacherVO> showClassOfGrade(TeacherVO vo);
	
	public TeacherVO addTeacher(TeacherVO vo);//添加教师
	
	public TeacherVO addInterestTeacher(TeacherVO vo);//添加兴趣班教师
	
	public List<TeacherVO> addTeacherList(String item_list);//批量添加教师
	
	public void deleteStudent(StudentVO vo);//删除学生
	/**
	 * 更新教师班级信息
	 * @param vo
	 */
	public void updateClassOfTea(ClassVO vo);
	/**
	 * 更新家长班级信息
	 * @param vo
	 */
	public void udpateClassOfPar(ClassVO vo);
	
	public void confirmTeacher(Integer teacher_id);//教师确认身份
	
	public List<TeacherVO> updateTeacher(TeacherVO vo);//更新教师身份信息
	
	public List<TeacherVO> updateTeacherOfManager(TeacherVO vo);
	
	public void deleteChild(Integer parent_id);//删除孩子
	
	public void deleteTeacherOfManager(String phone);//删除教师
	
	public void updateTeacherNamePhone(TeacherVO vo);//修改教师姓名，手机号
	
	public void updateStudent(StudentVO vo);//修改学生信息
	
	public List<StudentVO> getParentOfMng(String search);//新  家长花名册获取列表
	
	public void addParent(ParentVO vo);//后台添加家长
	
	public void deleteAllChild(String student_code);//删除某个学生的所有家长
	
	public void updateParent(ParentVO vo);//后台修改家长
	
	public List<StudentVO> getStudentListOfGroup(ContactListVO vo);//获取学生分组列表
	
	public void updateLetter();//添加教师表和家长表的all_letter,first_letter
	
	public UserVO validateLezhiAdmin(UserVO vo);
	
	public UserRoleVO getUserRoleByCondition(UserRoleVO vo);
	
	public void addUserRole(UserRoleVO vo);
	
	public List<TeacherVO> getAllTeacher(TeacherVO vo);//校务通知获取全部教师，除了自己

	public void deleteTeacherByContactID(Integer contact_id);//删除一个兴趣班的所有教师

	public StudentVO getStudentByStudentCode(StudentVO vo);//通过学号获取学生信息

	public List<String> getParentPhoneList();//获取全校家长手机号

	public List<String> getTeacherPhoneList();//获取全校教师手机号

	public List<TeacherVO> getTeacherListByUserID(TeacherVO vo);//获取用户所有教师身份

	public List<Integer> getTeacherUserID(ReceiveVO vo);//获取相关班级教师USERID

	public List<Integer> getContactTeacherUserID(Integer team_id);//获取相关兴趣班教师USERID

	public List<TeacherVO> getChargeTeacherList(TeacherVO vo);//获取班主任教师

	public List<TeacherVO> getDutyTeacherList(TeacherVO vo);//获取任课教师

    public List<ParentVO> getParentList(ParentVO vo);//获取家长孩子信息

    public StudentVO addStudent(StudentVO vo);//添加学生

    public List<String> getTeacherListOfSearch(TeacherVO vo);//获取校教师列表（带搜索功能）

    public List<String> getTeacherListNotContact(Map<String, Object> map);//查询某个兴趣班以外的所有教师

    public List<TeacherVO> getTeacherListByPhone(TeacherVO vo);//通过手机号获取教师身份信息

	public List<StudentVO> getStudentListOfSearch(StudentVO vo);//获取学生列表（带搜索)

	public List<StudentVO> getStudentListNotContact(Map<String, Object> map);//查询某个兴趣班以外的所有学生

	public void deleteUserAndStudentInfo(Integer class_id);//删除班级相关的所有教师身份，家长孩子，学生

	public String addStudentList(List<StudentVO> list);//批量添加学生

	public void setTeacherIsGraduate(ClassVO vo);//设置教师身份毕业

	public List<StudentVO> getStudentList(String student_name);//获取教师身份相关的学生列表

	public StudentVO getStudentInformationByID(Integer student_id);//获取学生基本信息

	public List<TeacherVO> getDutyNameList(String phone) ;//合并多个身份 例：数学（2个班级）

	public List<UserRoleVO> getUserRoleList(UserRoleVO vo);//获取信息管理用户列表

	public List<UserRoleVO> getUserRoleByPhone(UserRoleVO vo);

	public List<TeacherVO> getTeacherListByRole(TeacherVO vo);//根据角色获取教师列表

	public List<TeacherVO> deleteTeacherList(String teacher_ids);//批量删除角色职务信息
  
	public List<TeacherVO> getTeacherDuty(String phone);//获取教师角色
}
