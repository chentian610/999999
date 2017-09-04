package com.ninesky.classtao.excel.service;

import java.util.HashMap;
import java.util.List;

import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {
	public String exportTeacher(List<TeacherVO> list, String path);//导出花名册
	
	public String exportStudent(List<StudentVO> list, String Path);//导出学生花名册
	
	public String importStudentNew(List<StudentVO> list, String grade_id, String class_id);//导入学生excel表格（新）
	
	public String importInterestStudent(List<StudentVO> list, String contact_id);//导入学生excel表格（兴趣班）

	public String importTeacher(List<TeacherVO> list) ;//导入教师excel表格

    public HashMap<String,String> exportTongji(TableVO vo, String path);//导出统计信息

    public void download(String path,String title,HttpServletResponse response);//下载文件
}
