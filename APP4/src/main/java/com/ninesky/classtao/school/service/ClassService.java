package com.ninesky.classtao.school.service;

import java.util.HashMap;
import java.util.List;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.school.vo.GroupVO;
import com.ninesky.classtao.school.vo.TeamVO;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;

public interface ClassService {

	public List<ClassVO> list(Integer voId);

	public void updateClass(ClassVO vo) ;//更新班级信息

	public ClassVO insertClass(ClassVO vo);//添加班级

	public void delete(ClassVO vo) throws Exception;

	public List<ClassVO> getClassBySchoolID(HashMap<String, Object> p);

	/**
	 * 获取特定教师的班级列表
	 * @param vo
	 * @return
	 */
	public List<TeacherVO> getClassListByUserID(TeacherVO vo);

	public ClassVO getClassByID(Integer class_id);

	public List<ClassVO> getClassListBySchoolID(Integer schoolID);
	/**
	 * 判断新添加的班级名是否存在
	 * @param vo
	 * @return
	 */
	public Integer getClassCountByName(ClassVO vo);
	/**
	 * 获取家长用户对应的孩子列表
	 * @param vo
	 * @return
	 */
	public List<ParentVO> getChildListByUserID(ParentVO vo);

	/**
	 * 获取学校班级列表
	 * @param vo
	 * @return
	 */
	public List<ClassVO> getClassList(ClassVO vo);

	public List<TeamVO> getClassListOfGrade(ClassVO vo);

	public List<TeacherVO> getTeacherList(TeacherVO vo);//获取教师列表

	public List<StudentVO> getStudentList(StudentVO vo);//获取学生列表

	public StudentVO addStudent(StudentVO vo);//添加学生

	public List<GroupVO> getGroupList(String user_type);//获取模块收件人分组列表

	public List<TeacherVO> getTeacherListOfManager(String teacher_name);//后台获取教师列表

	public List<TeacherVO> getTeacherListOfManagerGroup(String teacher_name, String contact_id);//后台分组中的全体老师，排除已有教师

	public void getDutyName(List<String> list, List<TeacherVO> tList);//显示教师职务信息

	public List<TeacherVO> getDutyNameList(String phone);//显示教师职务信息，不拼接

	public List<TeacherVO> getTeacherListOfGroup(ContactListVO vo);//获取分组教师列表

	public List<StudentVO> getStudentListOfManager(StudentVO vo);//获取学生列表，后台使用

	public List<StudentVO> getStudentListOfManagerGroup(String student_name, String class_id, String contact_id);//获取分组里班级学生列表（排除已有学生）

	public List<ClassVO> getClassListOfManager(ClassVO vo);//获取班级信息，后台使用

	public List<ClassVO> insertClassOfManager(ClassVO vo);//后台添加班级

	public void updateClassGrade(ClassVO vo);//后台修改班级数或入学年份

	public String addStudentList(String item_list);//批量添加学生

	public void setClassIsGraduateByGradeID(Integer grade_id);//根据年级ID设置该年级是/否已经毕业

	public List<TeamVO> getTeamList(Integer school_id);// 获取全校班级列表(行政班级和兴趣班级)

	public List<StudentVO> getStudentListNotBedroom(String student_name,Integer class_id);//获取班级学生（排除已在寝室学生）
}
