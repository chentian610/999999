package com.ninesky.classtao.school.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.school.vo.GroupVO;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.common.vo.annotation.PutCache;
import com.ninesky.classtao.user.vo.ParentVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;


@RestController
@RequestMapping(value="classAction")
public class ClassController extends BaseController{
	@Autowired
	private ClassService classService;
	
	/**
	 * 添加班级
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addClass")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object addClass(HttpServletRequest request){
		ClassVO vo=BeanUtil.formatToBean(ClassVO.class);
		return ResponseUtils.sendSuccess(this.classService.insertClass(vo));
	}
	
	/**
	 * 获取用户班级列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getClassListBySchool")
	public @ResponseBody Object getClassListBySchool(){
		List<ClassVO> list = classService.getClassListBySchoolID(ActionUtil.getSchoolID());
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 获取用户班级列表
	 * @param
	 * @return
	 */
	@PutCache(name="updateClassList",value="school_id,user_id")
	@RequestMapping(value="/updateClassList")
	public @ResponseBody Object updateClassList(){
//		List<ClassVO> list = classService.getClassListBySchoolID(ActionUtil.getSchoolID());
		return ResponseUtils.sendSuccess("haaaaaaaaaaaaahahahaahah");
	}
	
	/**
	 * 获取用户班级列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getClassListOfTeacher")
	public @ResponseBody Object getClassListByTeacher(){
		TeacherVO vo=BeanUtil.formatToBean(TeacherVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<TeacherVO> list = classService.getClassListByUserID(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
/**
 * 获取家长用户对应的孩子列表
 * @return
 */
	@RequestMapping(value="/getChildOfParent")
	public @ResponseBody Object getChildOfParent(){
		ParentVO vo=new ParentVO();
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<ParentVO> list = classService.getChildListByUserID(vo);
		return ResponseUtils.sendSuccess(list);
	}
	
	@RequestMapping(value="/getClassBySchoolID",method=RequestMethod.GET)
	public @ResponseBody Object getClassBySchoolID(){
		HashMap<String,Object> paramap = new HashMap<String,Object>();
//		paramap.put("school_id", user.getSchool_id());
		List<ClassVO> list = classService.getClassBySchoolID(paramap);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 更新班级信息
	 * @param
	 * @return
	 */
	@RequestMapping(value="updateClass")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object updateClass(HttpServletRequest request) {
		ClassVO vo=BeanUtil.formatToBean(ClassVO.class);
		classService.updateClass(vo);
		return ResponseUtils.sendSuccess("更新成功",vo);
	}
	
	/**
	 * 获取学校班级列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getClassList")
public @ResponseBody Object getClassList(HttpServletRequest request){
	ClassVO classVO=BeanUtil.formatToBean(ClassVO.class);
	List<ClassVO> list=classService.getClassList(classVO);
	return ResponseUtils.sendSuccess(list);
}
	
	/**
	 * 获取教师列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTeacherList")
	public @ResponseBody Object getTeacherList(HttpServletRequest request){
		TeacherVO teacherVO=BeanUtil.formatToBean(TeacherVO.class);
		List<TeacherVO> list=classService.getTeacherList(teacherVO);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 获取学生列表(并且添加孩子时判断是否重名)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getStudentList")
	@GetCache(name="getStudentList",value="school_id,class_id")
	public Object getStudentList(HttpServletRequest request){
		StudentVO studentVO=BeanUtil.formatToBean(StudentVO.class);
		return classService.getStudentList(studentVO);
	}
	
	/**
	 * 通过学生姓名获取学校学生列表，模糊匹配
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getStudentListByStuName")
	public Object getStudentListByStuName(StudentVO studentVO){
		return classService.getStudentList(studentVO);
	}
	
	/**
	 * 添加学生
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addStudent")
	@PutCache(name="getStudentList",value="school_id,class_id")
	public @ResponseBody Object addStudent(HttpServletRequest request){
		StudentVO studentVO=BeanUtil.formatToBean(StudentVO.class);
		studentVO=classService.addStudent(studentVO);
		return ResponseUtils.sendSuccess(studentVO);
	}
	
	/**
	 * 批量添加学生
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addStudentList")
	public @ResponseBody Object addStudentList(HttpServletRequest request){
		String item_list=request.getParameter("item_list");
		return ResponseUtils.sendSuccess(classService.addStudentList(item_list));
	}
	
	/**
	 * 获取模块收件人分组列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getGroupList")
	public @ResponseBody Object getGroupList(HttpServletRequest request){
		String user_type=request.getParameter("user_type");
		List<GroupVO> list=classService.getGroupList(user_type);
		return ResponseUtils.sendSuccess(list);
	}
	
	/**
	 * 后台管理获取教师列表
	 * @return
	 */
	@RequestMapping(value="getTeacherListOfManager")
	public @ResponseBody Object getTeacherListOfManager(HttpServletRequest request){
		String teacher_name=request.getParameter("teacher_name");
		return ResponseUtils.sendSuccess(classService.getTeacherListOfManager(teacher_name));
	}
	
	/**
	 * 教师分组全体教师（排除已有教师）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getTeacherListOfManagerGroup")
	public @ResponseBody Object getTeacherListOfManagerGroup(HttpServletRequest request){
		String teacher_name=request.getParameter("teacher_name");
		String contact_id=request.getParameter("contact_id");
		return ResponseUtils.sendSuccess(classService.getTeacherListOfManagerGroup(teacher_name,contact_id));
	}
	
	/**
	 * 已有教师的弹窗显示身份
	 * @param request
	 * @return
	 */
	@RequestMapping(value="showDutyNameOfSome")
	public @ResponseBody Object showDutyNameOfSome(HttpServletRequest request){
		String phone=request.getParameter("phone");
		return ResponseUtils.sendSuccess(classService.getDutyNameList(phone));
	}
	
	/**
	 * 获取分组的教师列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getTeacherListOfGroup")
	public @ResponseBody Object getTeacherListOfGroup(HttpServletRequest request){
		ContactListVO vo=BeanUtil.formatToBean(ContactListVO.class);
		return ResponseUtils.sendSuccess(classService.getTeacherListOfGroup(vo));
	}
	
	/**
	 * 获取学生列表，后台使用
	 * @return
	 */
	@RequestMapping(value="getStudentOfManager")
	public @ResponseBody Object getStudentListOfManager(){
		StudentVO vo=BeanUtil.formatToBean(StudentVO.class);
		return ResponseUtils.sendSuccess(classService.getStudentListOfManager(vo));
	}
	
	/**
	 * 学生分组获取全体学生（排除已有学生）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getStudentListOfManagerGroup")
	public @ResponseBody Object getStudentListOfManagerGroup(HttpServletRequest request){
		String student_name=request.getParameter("student_name");
		String class_id=request.getParameter("class_id");
		String contact_id=request.getParameter("contact_id");
		return ResponseUtils.sendSuccess(classService.getStudentListOfManagerGroup(
				student_name, class_id, contact_id));
	}
	
	/**
	 * 获取班级信息，后台使用
	 * @return
	 */
	@RequestMapping(value="getClassOfManager")
	public @ResponseBody Object getClassOfManager(){
		ClassVO vo=BeanUtil.formatToBean(ClassVO.class);
		return ResponseUtils.sendSuccess(classService.getClassListOfManager(vo));
	}
	
	/**
	 * 后台添加班级（按入学年份）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addSomeClass")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object addSomeClass(HttpServletRequest request){
			ClassVO vo=BeanUtil.formatToBean(ClassVO.class);
		return ResponseUtils.sendSuccess(classService.insertClassOfManager(vo));
	}
	
	/**
	 * 后台更新班级数或入学年份
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateClassOfManager")
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object updateClassOfManager(HttpServletRequest request){
		ClassVO vo=BeanUtil.formatToBean(ClassVO.class);
		classService.updateClassGrade(vo);
		return ResponseUtils.sendSuccess();
	}
	
	@RequestMapping(value="{id}",method=RequestMethod.DELETE)
	@PutCache(name="getGradeAndClass",value="school_id")
	public @ResponseBody Object delete(@RequestBody ClassVO vo) throws Exception{
		this.classService.delete(vo);
		return ResponseUtils.sendSuccess("删除成功");
	}
	
	/**
	 * 跳到教室纪律页面(classroomjilv.jsp)
	 * @return
	 */
	@RequestMapping(value="/classroomjilv")
	public @ResponseBody Object classroomjilv(){
		ModelAndView model=new ModelAndView();
		model.setViewName("../zju/classroomjilv.jsp");
		return model;
	}
	
	/**
	 * 跳到教室卫生页面(classroomws.jsp)
	 * @return
	 */
	@RequestMapping(value="/classroomws")
	public @ResponseBody Object classroomws(){
		ModelAndView model=new ModelAndView();
		model.setViewName("../zju/classroomws.jsp");
		return model;
	}
	
	/**
	 * 跳到教室检查页面(classroomjiancha.jsp)
	 * @return
	 */
	@RequestMapping(value="/classroomjiancha")
	public @ResponseBody Object classroomjiancha(HttpServletRequest request){
		ModelAndView model=new ModelAndView();
		model.setViewName("../score/classroomjiancha.jsp");
		return model;
	}
    
	/**
	 * 年级毕业操作
	 * @return
	 */
	@RequestMapping(value="/setClassIsGraduateByGradeID")
	@PutCache(name="setClassIsGraduateByGradeID",value="school_id")
	public @ResponseBody Object setClassIsGraduateByGradeID(Integer grade_id){
		classService.setClassIsGraduateByGradeID(grade_id);
		return ResponseUtils.sendSuccess("该年级已经毕业！");
	}

	/**
	 * 获取全校班级列表(行政班级和兴趣班级)
	 * @return
	 */
	@RequestMapping(value="/getTeamList")
	public @ResponseBody Object getTeamList(Integer school_id){
		return ResponseUtils.sendSuccess(classService.getTeamList(school_id));
	}

	/**
	 * 获取班级学生，排除已在寝室学生
	 * @param student_name
	 * @param class_id
	 * @param bedroom_id
	 * @return
	 */
	@RequestMapping(value = "getStudentListNotBedroom")
	public @ResponseBody Object getStudentListNotBedroom(String student_name,Integer class_id) {
		return classService.getStudentListNotBedroom(student_name,class_id);
	}
}
