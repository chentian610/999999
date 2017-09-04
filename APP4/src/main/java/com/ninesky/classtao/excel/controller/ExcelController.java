package com.ninesky.classtao.excel.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninesky.classtao.excel.service.ExcelService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.Constants;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.ExcelUtil;
import com.ninesky.framework.ResponseUtils;


@Controller
@RequestMapping(value="excelAction")
public class ExcelController extends BaseController{

	@Autowired
	private ClassService classService;

	@Autowired
	private ExcelService excelService;

	/**
	 * 导出花名册
	 * @param request
	 * @return
	 */
	@RequestMapping(value="exportTeacher")
	public @ResponseBody Object exportTeacher(HttpServletRequest request){
		String  path="g:/teacher.xls";//导出文件存放地址
		List<TeacherVO> list=classService.getTeacherListOfManager("");
		return ResponseUtils.sendSuccess(excelService.exportTeacher(list,path));
	}

	/**
	 * 导出学生花名册
	 * @param request
	 * @return
	 */
	@RequestMapping(value="exportStudent")
	public @ResponseBody Object exportStudent(HttpServletRequest request){
		String path="g:/student.xls";
		StudentVO vo=new StudentVO();
		vo.setStudent_name("");
		List<StudentVO> list=classService.getStudentListOfManager(vo);
		return ResponseUtils.sendSuccess(excelService.exportStudent(list, path));
	}

	/**
	 * 导入学生excel表（新）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="importStudentNew")
	public @ResponseBody Object importStudentNew(HttpServletRequest request){
		String parentPath=request.getSession().getServletContext().getRealPath("..");
        //Excel列名对应对象的字段名
        HashMap<String,String> fieldMap = new HashMap<String,String>();
        fieldMap.put("姓名", "student_name");
        fieldMap.put("学号", "student_code");
        fieldMap.put("性别", "sex_name");//存的是男，女
        String url=request.getParameter("url");
        String grade_id=request.getParameter("grade_id");
        String class_id=request.getParameter("class_id");
        List<StudentVO> list = ExcelUtil.getData(parentPath+Constants.FILE_PATH_DEFAULT+url.substring(url.lastIndexOf("/")+1), fieldMap, StudentVO.class);
        String excel=excelService.importStudentNew(list,grade_id,class_id);
        return ResponseUtils.sendSuccess(excel);
	}

	/**
	 * 导入学生excel表（兴趣班）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="importInterestStudent")
	public @ResponseBody Object importInterestStudent(HttpServletRequest request){
		String parentPath=request.getSession().getServletContext().getRealPath("..");
		HashMap<String,String> fieldMap=new HashMap<String,String>();
		fieldMap.put("姓名", "student_name");
		fieldMap.put("学号", "student_code");
		fieldMap.put("性别", "sex_name");
		String url=request.getParameter("url");
		String contact_id=request.getParameter("contact_id");
		List<StudentVO> list=ExcelUtil.getData(parentPath+Constants.FILE_PATH_DEFAULT+url.substring(url.lastIndexOf("/")+1), fieldMap, StudentVO.class);
		String excel=excelService.importInterestStudent(list,contact_id);
		return ResponseUtils.sendSuccess(excel);
	}

	/**
	 * 导入教师
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "importTeacher")
	public @ResponseBody Object importTeacher(HttpServletRequest request){
		String parentPath=request.getSession().getServletContext().getRealPath("..");
		HashMap<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("姓名","teacher_name");
		fieldMap.put("手机号","phone");
		String url=request.getParameter("url");
		List<TeacherVO> list=ExcelUtil.getData(parentPath+Constants.FILE_PATH_DEFAULT+url.substring(url.lastIndexOf("/")+1),fieldMap,TeacherVO.class);
		String msg=excelService.importTeacher(list);
		return ResponseUtils.sendSuccess(msg);
	}

    /**
     * 导出统计
     * @param
     * @return
     */
    @RequestMapping(value = "exportTongJi")
    public @ResponseBody Object exportTongJi(HttpServletRequest request) {
        String parentPath = request.getSession().getServletContext().getRealPath("..");
        TableVO vo = BeanUtil.formatToBean(TableVO.class);
        return ResponseUtils.sendSuccess(excelService.exportTongji(vo, parentPath));
    }

    /**
     * 下载文件
     * @param path
     * @param title
     * @param response
     * @return
     */
    @RequestMapping(value = "download")
    public @ResponseBody Object download(String path, String title, HttpServletResponse response) {
        excelService.download(path,title,response);
        return ResponseUtils.sendSuccess();
    }
}
