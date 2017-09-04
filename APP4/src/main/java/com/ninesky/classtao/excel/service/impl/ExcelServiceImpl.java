package com.ninesky.classtao.excel.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ninesky.classtao.module.service.ModuleService;
import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.classtao.score.service.ScoreTableService;
import com.ninesky.classtao.score.vo.ScoreReasonVO;
import com.ninesky.classtao.score.vo.TableHeadVO;
import com.ninesky.classtao.score.vo.TableVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.*;
import com.ninesky.framework.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.excel.service.ExcelService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;

import javax.servlet.http.HttpServletResponse;

@Service("excelServiceImpl")
public class ExcelServiceImpl implements ExcelService{
	
	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private ScoreTableService scoreTableService;

	@Autowired
	private ModuleService moduleService;

    @Autowired
    private DictService dictService;
	
	//导出花名册
	public String exportTeacher(List<TeacherVO> list,String path) {
		File teacherExcel=new File(path);
		try {
			teacherExcel.createNewFile();
			HSSFWorkbook wb=new HSSFWorkbook ();//此为2003版本，XSSFWorkbook（2007版本）
			HSSFSheet sheet=wb.createSheet();
			sheet.setColumnWidth(0, 15* 256);//设置每列的宽度
			sheet.setColumnWidth(1, 20* 256);
			sheet.setColumnWidth(2, 20* 256);
			titleRow(wb, sheet);//设置标题行
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				HSSFRow row=sheet.createRow(i+1);
				TeacherVO vo=list.get(i);
				HSSFCell cell=row.createCell(0);
				cell.setCellValue(vo.getTeacher_name());//第一列为姓名
				HSSFCell cell1=row.createCell(1);
				cell1.setCellValue(vo.getPhone());//第二列为手机号
				HSSFCell cell2=row.createCell(2);
				cell2.setCellValue(vo.getDuty_name());//第3列为职务信息
			}
			OutputStream out = new FileOutputStream(path);
			wb.write(out);
			out.flush();
			out.close();
			return  "花名册导出成功：" + path;
		} catch (IOException e) {
			e.printStackTrace();
			return "花名册导出失败";
		}
	}

	//设置标题行
	private void titleRow(HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFRow titlerow=sheet.createRow(0);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());    //标题行的背景色为灰色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFCell titlecell=titlerow.createCell(0);
		titlecell.setCellValue("姓名");
		titlecell.setCellStyle(cellStyle);
		HSSFCell titlecell1=titlerow.createCell(1);
		titlecell1.setCellValue("手机号");
		titlecell1.setCellStyle(cellStyle);
		HSSFCell titlecell2=titlerow.createCell(2);
		titlecell2.setCellValue("职务信息");
		titlecell2.setCellStyle(cellStyle);
	}

	//导出学生花名册
	public String exportStudent(List<StudentVO> list, String path) {
		File studentExcel=new File(path);
		try {
			studentExcel.createNewFile();
			HSSFWorkbook wb=new HSSFWorkbook ();//此为2003版本，XSSFWorkbook（2007版本）
			HSSFSheet sheet=wb.createSheet();
			sheet.setColumnWidth(0, 15* 256);//设置每列的宽度
			sheet.setColumnWidth(1, 20* 256);
			sheet.setColumnWidth(2, 20* 256);
			sheet.setColumnWidth(3, 20* 256);
			studentTitleRow(wb, sheet);//设置标题行
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				HSSFRow row=sheet.createRow(i+1);
				StudentVO vo=list.get(i);
				HSSFCell cell=row.createCell(0);
				cell.setCellValue(vo.getStudent_name());//第一列为姓名
				HSSFCell cell1=row.createCell(1);
				cell1.setCellValue(vo.getSex());//第二列为性别
				HSSFCell cell2=row.createCell(2);
				cell2.setCellValue(vo.getStudent_code());//第3列为学号
				HSSFCell cell3=row.createCell(3);
				cell3.setCellValue(vo.getClass_name());//第3列为所在班级
			}
			OutputStream out = new FileOutputStream(path);
			wb.write(out);
			out.flush();
			out.close();
			return  "花名册导出成功：" + path;
		} catch (IOException e) {
			e.printStackTrace();
			return "花名册导出失败";
		}
	}

	//设置标题行
		private void studentTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
			HSSFRow titlerow=sheet.createRow(0);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());    //标题行的背景色为灰色
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			HSSFCell titlecell=titlerow.createCell(0);
			titlecell.setCellValue("姓名");
			titlecell.setCellStyle(cellStyle);
			HSSFCell titlecell1=titlerow.createCell(1);
			titlecell1.setCellValue("性别");
			titlecell1.setCellStyle(cellStyle);
			HSSFCell titlecell2=titlerow.createCell(2);
			titlecell2.setCellValue("学号");
			titlecell2.setCellStyle(cellStyle);
			HSSFCell titlecell3=titlerow.createCell(3);
			titlecell3.setCellValue("所属班级");
			titlecell3.setCellStyle(cellStyle);
		}

	//导入学生excel表格（新）
	public String importStudentNew(List<StudentVO> list,String grade_id,String class_id) {
		if (StringUtil.isEmpty(grade_id) || StringUtil.isEmpty(class_id)) 
			throw new BusinessException(MsgService.getMsg("UN_GRADEID_CLASSID"));
		if (ListUtil.isEmpty(list)) throw new BusinessException("excel格式与模版不符！");
		String excel = "";
		String student_code = "";
		for (StudentVO vo : list) {
			if (StringUtil.isEmpty(vo.getStudent_name()) || StringUtil.isEmpty(vo.getStudent_code()))
				continue;
			StudentVO studentVO = new StudentVO();// 对传入的数据去空
			studentVO.setStudent_name(vo.getStudent_name().trim());
			studentVO.setStudent_code(vo.getStudent_code().trim());
			if (StringUtil.isNotEmpty(vo.getSex_name()))
				studentVO.setSex_name(vo.getSex_name().trim());
			studentVO.setSchool_id(ActionUtil.getSchoolID());
			StudentVO svo = userService.getStudentByStudentCode(studentVO);// 判断该学号是否已被使用
			if (svo != null) {// 拼接已被使用的学号
				if (student_code.equals("")) {
					student_code = studentVO.getStudent_code();
				} else
					student_code = student_code + ","+ studentVO.getStudent_code();
				continue;
			}
			studentVO.setGrade_id(Integer.parseInt(grade_id.trim()));
			studentVO.setClass_id(Integer.parseInt(class_id.trim()));
			if ("男".equals(studentVO.getSex_name())) {
				studentVO.setSex(0);
			} else if ("女".equals(studentVO.getSex_name())) {
				studentVO.setSex(1);
			} else {
				studentVO.setSex(-1);
			}
			classService.addStudent(studentVO);
		}
		excel = importStudentMsg(student_code);// 导入excel后的返回信息
		return excel;
	}

	//导入excel后的返回信息
	private String importStudentMsg(String student_code) {
		String excel;
		if (student_code.equals("")) {
			excel = MsgService.getMsg("IMPORT_SUCCESS");
		} else
			excel = MsgService.getMsg("IMPORT_FAIL",student_code);
		return excel;
	}

	//导入学生excel表格（兴趣班）
	public String importInterestStudent(List<StudentVO> list,String contact_id){
		if (ListUtil.isEmpty(list)) throw new BusinessException("excel格式与模版不符！");
		for (StudentVO vo : list) {
			if (StringUtil.isEmpty(vo.getStudent_name()) || StringUtil.isEmpty(vo.getStudent_code()))
				continue;
			StudentVO studentVO = new StudentVO();// 对传入的数据去空
			studentVO.setStudent_name(vo.getStudent_name().trim());
			studentVO.setStudent_code(vo.getStudent_code().trim());
			studentVO.setSchool_id(ActionUtil.getSchoolID());
			StudentVO svo = userService.getStudentByStudentCode(studentVO);// 判断该学生是否已在行政班中
			if (svo == null) // 还不是刚学校的学生
				return MsgService.getMsg("NOT_IN_CLASS");
			if (!vo.getStudent_name().trim().equals(svo.getStudent_name()))
				return MsgService.getMsg("ERROR_NAME");
			ContactListVO cvo=new ContactListVO();
			cvo.setSchool_id(ActionUtil.getSchoolID());
			cvo.setContact_id(Integer.parseInt(contact_id.trim()));
			cvo.setStudent_id(svo.getStudent_id());
			ContactListVO clvo=dao.queryObject("contactListMap.getStudent", cvo);
			if (clvo!=null) return MsgService.getMsg("STU_HAVE_BEEN_INTEREST");
			vo.setStudent_id(svo.getStudent_id());
		}
		List<ContactListVO> clist=new ArrayList<ContactListVO>();
		for (StudentVO vo:list) {
			if (StringUtil.isEmpty(vo.getStudent_name()) || StringUtil.isEmpty(vo.getStudent_code()))
				continue;
			ContactListVO cvo=new ContactListVO();
			cvo.setContact_id(Integer.parseInt(contact_id.trim()));
			cvo.setSchool_id(ActionUtil.getSchoolID());
			cvo.setUser_id(0);
			cvo.setUser_type(DictConstants.USERTYPE_STUDENT);
			cvo.setStudent_id(vo.getStudent_id());
			cvo.setAll_letter(LetterUtil.converterToSpell(vo.getStudent_name()));// 获取首字母缩写converterToFirstSpell
			cvo.setFirst_letter(LetterUtil.converterToFirstSpell(vo.getStudent_name()));
			cvo.setCreate_by(ActionUtil.getUserID());
			cvo.setCreate_date(ActionUtil.getSysTime());
			clist.add(cvo);
		}
		dao.insertObject("contactListMap.insertContactList", clist);
		//user_groups
		for (ContactListVO cvo:clist) {
			redisService.addUserToUserGroup(DictConstants.USERTYPE_STUDENT,DictConstants.TEAM_TYPE_INTEREST, 0,
					cvo.getContact_id(),0,cvo.getStudent_id());
			}
		return MsgService.getMsg("IMPORT_SUCCESS");
	}

	//导入excel表格
	public String importTeacher(List<TeacherVO> list) {
		String excel="";
		String teacher_name="";
		String errorPhone="";
		String mobile="^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
		if (ListUtil.isEmpty(list)) throw new BusinessException("excel格式与模版不符！");
		for(TeacherVO vo:list){
			if(!vo.getPhone().matches(mobile)){//对手机号的验证
				if(errorPhone.equals("")){
					errorPhone=vo.getTeacher_name();
				}else
					errorPhone=errorPhone+","+vo.getTeacher_name();
				continue;
			}
			vo.setSchool_id(ActionUtil.getSchoolID());
			List<TeacherVO> tvo=dao.queryForList("teacherMap.getTeacherListByPhone", vo);//判断该老师是否已存在
			if(ListUtil.isNotEmpty(tvo)) {
				if(teacher_name.equals("")){
					teacher_name=vo.getTeacher_name();
				}else
					teacher_name=teacher_name+","+vo.getTeacher_name();
				continue;
			}
			userService.addTeacher(vo);
		}
		excel = importMsg(teacher_name, errorPhone);//导入excel后的返回信息
		return excel;
	}

	//导入excel后的返回信息
	private String importMsg(String teacher_name, String errorPhone) {
		String excel;
		if (teacher_name.equals("") && errorPhone.equals("")) {
			excel = "导入成功!";
		} else if (!teacher_name.equals("") && errorPhone.equals("")) {
			excel = teacher_name + "老师已存在，故导入失败，\n其余部分导入成功！";
		} else if (teacher_name.equals("") && !errorPhone.equals("")) {
			excel = errorPhone + "老师手机号错误，故导入失败，\n其余部分导入成功！";
		} else
			excel = teacher_name + "老师已存在，\n" + errorPhone
					+ "老师手机号错误，故导入失败，\n其余部分导入成功！";
		return excel;
	}

	//导出统计信息
	public HashMap<String,String> exportTongji(TableVO vo,String parentPath ){
		String sheetName;
		String title;
		String date;
		SchoolModuleVO module=new SchoolModuleVO();
		module.setSchool_id(ActionUtil.getSchoolID());
		module.setModule_code(vo.getModule_code());
		List<SchoolModuleVO> moduleVOs=moduleService.getSchoolModuleByCode(module);
		if (StringUtil.isNotEmpty(vo.getSum_type())) {
			switch (vo.getSum_type()) {
				case DictConstants.SUM_TYPE_DAY:
					date = DateUtil.formatDateToString(ActionUtil.getSysTime(),"yyyy.MM.dd");
					break;
				case DictConstants.SUM_TYPE_WEEK:
					date = DateUtil.getFirstDayOfWeek(DateUtil.smartFormat(DateUtil.getNow(DateUtil.YMD)),"yyyy.MM.dd")+ "-" +
                            DateUtil.formatDateToString(ActionUtil.getSysTime(),"yyyy.MM.dd");
					break;
				case DictConstants.SUM_TYPE_MONTH:
					date = DateUtil.getFirstDayOfMonth(DateUtil.smartFormat(DateUtil.getNow(DateUtil.YMD)),"yyyy.MM.dd")
							+ "-" + DateUtil.formatDateToString(ActionUtil.getSysTime(),"yyyy.MM.dd");
					break;
				case DictConstants.SUM_TYPE_YEAR:
					date = DateUtil.getFirstDayOfYear(DateUtil.smartFormat(DateUtil.getNow(DateUtil.YMD)),"yyyy.MM.dd")
							+ "-" + DateUtil.formatDateToString(ActionUtil.getSysTime(),"yyyy.MM.dd");
					break;
				default:
					date = "";
			}
			vo.setStart_date(getScoreDate(vo));
			vo.setEnd_date(DateUtil.getNow(""));
		} else {//自定义统计
			date=vo.getStart_date().replaceAll("-", ".")+"-"+vo.getEnd_date().replaceAll("-", ".");
		}
        DictVO dict=new DictVO();
        dict.setIs_active(1);
        dict.setSchool_id(ActionUtil.getSchoolID());
        dict.setOther_field(vo.getModule_code());
        dict.setDict_code(vo.getScore_type());
        List<DictVO> list=dictService.getDictSchoolList(dict);
		if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {
			if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(vo.getModule_code()))
				sheetName="个人评分";
            else if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code()))
                sheetName="班级评分";
			else if (DictConstants.MODULE_CODE_STUDENT_LEAVE.equals(vo.getModule_code()))
				sheetName="学生请假";
			else if (DictConstants.MODULE_CODE_LEAVE.equals(vo.getModule_code()))
				sheetName="教师请假";
			else sheetName = moduleVOs.get(0).getModule_name() + "统计-班级";
		}else {
            if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(vo.getModule_code()))
                sheetName="个人评分";
            else if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code()))
                sheetName="班级评分";
			else if (DictConstants.MODULE_CODE_STUDENT_LEAVE.equals(vo.getModule_code()))
				sheetName="学生请假";
			else if (DictConstants.MODULE_CODE_LEAVE.equals(vo.getModule_code()))
				sheetName="教师请假";
            else sheetName=moduleVOs.get(0).getModule_name()+"统计-"+redisService.getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id());
		}
        if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){
            if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
				title=MsgService.getMsg("EXPORT_TITLE",redisService.getSchoolName(ActionUtil.getSchoolID()),
					redisService.getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id()),
					moduleVOs.get(0).getModule_name(),redisService.getDictValue(vo.getAttend_item()),date);
            else title=MsgService.getMsg("EXPORT_TITLE",redisService.getSchoolName(ActionUtil.getSchoolID()),
					redisService.getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id()),
					moduleVOs.get(0).getModule_name(),"考勤",date);//寝室考勤
        } else {
            if (DictConstants.MODULE_CODE_PERFORMANCE.equals(vo.getModule_code()) ||
					DictConstants.MODULE_CODE_STUDENT_LEAVE.equals(vo.getModule_code()) )
				title=MsgService.getMsg("EXPORT_TITLE",redisService.getSchoolName(ActionUtil.getSchoolID()),
					redisService.getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id()),
					moduleVOs.get(0).getModule_name(),"",date);
			else if (DictConstants.MODULE_CODE_LEAVE.equals(vo.getModule_code()))
				title=MsgService.getMsg("EXPORT_TITLE",redisService.getSchoolName(ActionUtil.getSchoolID()),
					"",moduleVOs.get(0).getModule_name(),"",date);
			else
				title=MsgService.getMsg("EXPORT_TITLE",redisService.getSchoolName(ActionUtil.getSchoolID()),
						redisService.getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id()),
						moduleVOs.get(0).getModule_name(),list.get(0).getDict_value(),date);
        }
        String path="table"+ActionUtil.getSysTime().getTime()+".xlsx";
        File teacherExcel=new File(parentPath+Constants.FILE_PATH_DEFAULT+path);
		if (DictConstants.MODULE_CODE_STUDENT_LEAVE.equals(vo.getModule_code()))
			setStudentLeaveTable(vo, parentPath, path, teacherExcel, sheetName, title);
		else if (DictConstants.MODULE_CODE_LEAVE.equals(vo.getModule_code()))
			setTeacherLeaveTable(vo, parentPath, path, teacherExcel, sheetName, title);
		else if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(vo.getModule_code()))
			setPersonScoreTable(vo, parentPath, path, teacherExcel, sheetName, title);
		else if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code()))
			setClassScoreTable(vo, parentPath, path, teacherExcel, sheetName, title);
		else setTable(vo, parentPath, path, teacherExcel, sheetName, title);
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("path",parentPath+Constants.FILE_PATH_DEFAULT+path);
		map.put("title",title);
        return  map;
	}

	private void setPersonScoreTable(TableVO vo, String parentPath, String path, File teacherExcel, String sheetName, String title){
		try{
			ScoreReasonVO reasonVO=new ScoreReasonVO();
			reasonVO.setSchool_id(ActionUtil.getSchoolID());
			reasonVO.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			reasonVO.setScore_type(vo.getScore_type());
			List<ScoreReasonVO> slist=dao.queryForList("scoreReasonMap.getScoreReasonList",reasonVO);
			teacherExcel.createNewFile();
			XSSFWorkbook wb=new XSSFWorkbook();//XSSFWorkbook（2007版本）
			XSSFSheet sheet=wb.createSheet(sheetName);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, slist.size()+2));
			XSSFRow titrow=sheet.createRow(0);
			XSSFCell titCell=titrow.createCell(0);
			titCell.setCellValue(title);//标题
			CellStyle cellStyle1 = wb.createCellStyle();
			XSSFFont font2 = wb.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setFont(font2);//选择需要用到的字体格式
			titCell.setCellStyle(cellStyle1);
			XSSFRow titlerow=sheet.createRow(1);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font2);//选择需要用到的字体格式
			sheet.setColumnWidth(0, 14 * 256);
			XSSFCell titlecell=titlerow.createCell(0);
			titlecell.setCellValue("学号");
			titlecell.setCellStyle(cellStyle);
			sheet.setColumnWidth(1, 14 * 256);
			XSSFCell titlecell1=titlerow.createCell(1);
			titlecell1.setCellValue("姓名");
			titlecell1.setCellStyle(cellStyle);
			sheet.setColumnWidth(2, 14 * 256);
			for (int i=0;i<slist.size();i++) {
				sheet.setColumnWidth(i+2, 14 * 256);
				XSSFCell titlecell3=titlerow.createCell(i+2);
				titlecell3.setCellValue(slist.get(i).getScore_reason());
				titlecell3.setCellStyle(cellStyle);
			}
			XSSFCell titlecell2=titlerow.createCell(slist.size()+2);
			titlecell2.setCellValue("总分");
			titlecell2.setCellStyle(cellStyle);
			List<HashMap<String,Object>> list=scoreTableService.getPersonScoreCount(vo);
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				XSSFRow row=sheet.createRow(i+2);
				HashMap<String,Object> map=list.get(i);
				XSSFCell cell=row.createCell(0);
				Object value=map.get("student_code");
				cell.setCellValue(value.toString());
				cell.setCellStyle(cellStyle);

				XSSFCell cell1=row.createCell(1);
				Object value1=map.get("student_name");
				cell1.setCellValue(value1.toString());
				cell1.setCellStyle(cellStyle);

				for (int j=0;j<slist.size();j++) {
					XSSFCell cell3=row.createCell(j+2);
					Object value3=map.get(slist.get(j).getScore_code());
					if (value3==null)
						cell3.setCellValue("0");
					else if (StringUtil.isEmpty(value3.toString()))
						cell3.setCellValue("0");
					else
						cell3.setCellValue(value3.toString());
					cell3.setCellStyle(cellStyle);
				}

				XSSFCell cell2=row.createCell(slist.size()+2);
				Object value2=map.get("total");
				cell2.setCellValue(value2.toString());
				cell2.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(list.size()+2, list.size()+2, 0, slist.size()+2));
			XSSFRow daterow=sheet.createRow(list.size()+2);
			XSSFCell dateCell=daterow.createCell(0);
			dateCell.setCellValue("报表生成时间："+DateUtil.formatDateToString(ActionUtil.getSysTime(),DateUtil.Y_M_D_HM));
			dateCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+3, list.size()+3, 0, slist.size()+2));
			XSSFRow teacherrow=sheet.createRow(list.size()+3);
			XSSFCell teacherCell=teacherrow.createCell(0);
			teacherCell.setCellValue("制表人："+redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
			teacherCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+4, list.size()+4, 0, slist.size()+2));
			XSSFRow qianrow=sheet.createRow(list.size()+4);
			XSSFCell qianCell=qianrow.createCell(0);
			qianCell.setCellValue("签名：");
			qianCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+5, list.size()+5, 0, slist.size()+2));
			XSSFRow beirow=sheet.createRow(list.size()+5);
			XSSFCell beiCell=beirow.createCell(0);
			beiCell.setCellValue("备注：");
			beiCell.setCellStyle(cellStyle);
			OutputStream out = new FileOutputStream(parentPath+ Constants.FILE_PATH_DEFAULT+path);
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void setClassScoreTable(TableVO vo, String parentPath, String path, File teacherExcel, String sheetName, String title){
		try{
			ScoreReasonVO reasonVO=new ScoreReasonVO();
			reasonVO.setSchool_id(ActionUtil.getSchoolID());
			reasonVO.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			reasonVO.setScore_type(vo.getScore_type());
			List<ScoreReasonVO> slist=dao.queryForList("scoreReasonMap.getScoreReasonList",reasonVO);
			teacherExcel.createNewFile();
			XSSFWorkbook wb=new XSSFWorkbook();//XSSFWorkbook（2007版本）
			XSSFSheet sheet=wb.createSheet(sheetName);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, slist.size()+2));
			XSSFRow titrow=sheet.createRow(0);
			XSSFCell titCell=titrow.createCell(0);
			titCell.setCellValue(title);//标题
			CellStyle cellStyle1 = wb.createCellStyle();
			XSSFFont font2 = wb.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setFont(font2);//选择需要用到的字体格式
			titCell.setCellStyle(cellStyle1);
			XSSFRow titlerow=sheet.createRow(1);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font2);//选择需要用到的字体格式
			sheet.setColumnWidth(0, 14 * 256);
			XSSFCell titlecell=titlerow.createCell(0);
			titlecell.setCellValue("班级");
			titlecell.setCellStyle(cellStyle);
			sheet.setColumnWidth(1, 14 * 256);
			XSSFCell titlecell1=titlerow.createCell(1);
			titlecell1.setCellValue("班级人数");
			titlecell1.setCellStyle(cellStyle);
			sheet.setColumnWidth(2, 14 * 256);
			for (int i=0;i<slist.size();i++) {
				sheet.setColumnWidth(i+2, 14 * 256);
				XSSFCell titlecell3=titlerow.createCell(i+2);
				titlecell3.setCellValue(slist.get(i).getScore_reason());
				titlecell3.setCellStyle(cellStyle);
			}
			XSSFCell titlecell2=titlerow.createCell(slist.size()+2);
			titlecell2.setCellValue("总分");
			titlecell2.setCellStyle(cellStyle);
			List<HashMap<String,Object>> list=scoreTableService.getClassScoreCount(vo);
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				XSSFRow row=sheet.createRow(i+2);
				HashMap<String,Object> map=list.get(i);
				XSSFCell cell=row.createCell(0);
				Object value=map.get("team_name");
				cell.setCellValue(value.toString());
				cell.setCellStyle(cellStyle);

				XSSFCell cell1=row.createCell(1);
				Object value1=map.get("count");
				if (value1==null)
					cell1.setCellValue("0");
				else if (StringUtil.isEmpty(value1.toString()))
					cell1.setCellValue("0");
				else
					cell1.setCellValue(value1.toString());
				cell1.setCellStyle(cellStyle);

				for (int j=0;j<slist.size();j++) {
					XSSFCell cell3=row.createCell(j+2);
					Object value3=map.get(slist.get(j).getScore_code());
					if (value3==null)
						cell3.setCellValue("0");
					else if (StringUtil.isEmpty(value3.toString()))
						cell3.setCellValue("0");
					else
						cell3.setCellValue(value3.toString());
					cell3.setCellStyle(cellStyle);
				}

				XSSFCell cell2=row.createCell(slist.size()+2);
				Object value2=map.get("total");
				cell2.setCellValue(value2.toString());
				cell2.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(list.size()+2, list.size()+2, 0, slist.size()+2));
			XSSFRow daterow=sheet.createRow(list.size()+2);
			XSSFCell dateCell=daterow.createCell(0);
			dateCell.setCellValue("报表生成时间："+DateUtil.formatDateToString(ActionUtil.getSysTime(),DateUtil.Y_M_D_HM));
			dateCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+3, list.size()+3, 0, slist.size()+2));
			XSSFRow teacherrow=sheet.createRow(list.size()+3);
			XSSFCell teacherCell=teacherrow.createCell(0);
			teacherCell.setCellValue("制表人："+redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
			teacherCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+4, list.size()+4, 0, slist.size()+2));
			XSSFRow qianrow=sheet.createRow(list.size()+4);
			XSSFCell qianCell=qianrow.createCell(0);
			qianCell.setCellValue("签名：");
			qianCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+5, list.size()+5, 0, slist.size()+2));
			XSSFRow beirow=sheet.createRow(list.size()+5);
			XSSFCell beiCell=beirow.createCell(0);
			beiCell.setCellValue("备注：");
			beiCell.setCellStyle(cellStyle);
			OutputStream out = new FileOutputStream(parentPath+ Constants.FILE_PATH_DEFAULT+path);
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void setTeacherLeaveTable(TableVO vo, String parentPath, String path, File teacherExcel, String sheetName, String title){
		try{
			DictVO dict=new DictVO();
			dict.setSchool_id(ActionUtil.getSchoolID());
			dict.setDict_group("033");
			dict.setIs_active(1);
			List<DictVO> dList=dictService.getDictSchoolList(dict);//请假类型
			teacherExcel.createNewFile();
			XSSFWorkbook wb=new XSSFWorkbook();//XSSFWorkbook（2007版本）
			XSSFSheet sheet=wb.createSheet(sheetName);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, dList.size()+2));
			XSSFRow titrow=sheet.createRow(0);
			XSSFCell titCell=titrow.createCell(0);
			titCell.setCellValue(title);//标题
			CellStyle cellStyle1 = wb.createCellStyle();
			XSSFFont font2 = wb.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setFont(font2);//选择需要用到的字体格式
			titCell.setCellStyle(cellStyle1);
			XSSFRow titlerow=sheet.createRow(1);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font2);//选择需要用到的字体格式
			sheet.setColumnWidth(0, 14 * 256);
			XSSFCell titlecell=titlerow.createCell(0);
			titlecell.setCellValue("姓名");
			titlecell.setCellStyle(cellStyle);
			sheet.setColumnWidth(1, 14 * 256);
			XSSFCell titlecell1=titlerow.createCell(1);
			titlecell1.setCellValue("请假次数");
			titlecell1.setCellStyle(cellStyle);
			sheet.setColumnWidth(2, 14 * 256);
			XSSFCell titlecell2=titlerow.createCell(2);
			titlecell2.setCellValue("总时长");
			titlecell2.setCellStyle(cellStyle);
			for (int i=0;i<dList.size();i++) {
				sheet.setColumnWidth(i+3, 14 * 256);
				XSSFCell titlecell3=titlerow.createCell(i+3);
				titlecell3.setCellValue(dList.get(i).getDict_value());
				titlecell3.setCellStyle(cellStyle);
			}
			List<HashMap<String,Object>> list=scoreTableService.getTeacherLeaveCount(vo,dList);
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				XSSFRow row=sheet.createRow(i+2);
				HashMap<String,Object> map=list.get(i);
				XSSFCell cell=row.createCell(0);
				Object value=map.get("teacher_name");
				cell.setCellValue(value.toString());
				cell.setCellStyle(cellStyle);

				XSSFCell cell1=row.createCell(1);
				Object value1=map.get("count");
				cell1.setCellValue(value1.toString());
				cell1.setCellStyle(cellStyle);

				XSSFCell cell2=row.createCell(2);
				Object value2=map.get("day");
				if (value2==null)
					cell2.setCellValue("0天");
				else if (StringUtil.isEmpty(value2.toString()))
					cell2.setCellValue("0天");
				else
					cell2.setCellValue(value2.toString());
				cell2.setCellStyle(cellStyle);

				for (int j=0;j<dList.size();j++) {
					XSSFCell cell3=row.createCell(j+3);
					Object value3=map.get(dList.get(j).getDict_code());
					if (value3==null)
						cell3.setCellValue("0天");
					else if (StringUtil.isEmpty(value3.toString()))
						cell3.setCellValue("0天");
					else
						cell3.setCellValue(value3.toString());
					cell3.setCellStyle(cellStyle);
				}
			}
			sheet.addMergedRegion(new CellRangeAddress(list.size()+2, list.size()+2, 0, dList.size()+2));
			XSSFRow daterow=sheet.createRow(list.size()+2);
			XSSFCell dateCell=daterow.createCell(0);
			dateCell.setCellValue("报表生成时间："+DateUtil.formatDateToString(ActionUtil.getSysTime(),DateUtil.Y_M_D_HM));
			dateCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+3, list.size()+3, 0, dList.size()+2));
			XSSFRow teacherrow=sheet.createRow(list.size()+3);
			XSSFCell teacherCell=teacherrow.createCell(0);
			teacherCell.setCellValue("制表人："+redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
			teacherCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+4, list.size()+4, 0, dList.size()+2));
			XSSFRow qianrow=sheet.createRow(list.size()+4);
			XSSFCell qianCell=qianrow.createCell(0);
			qianCell.setCellValue("签名：");
			qianCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+5, list.size()+5, 0, dList.size()+2));
			XSSFRow beirow=sheet.createRow(list.size()+5);
			XSSFCell beiCell=beirow.createCell(0);
			beiCell.setCellValue("备注：小时和天单独统计，不互相转换");
			beiCell.setCellStyle(cellStyle);
			OutputStream out = new FileOutputStream(parentPath+ Constants.FILE_PATH_DEFAULT+path);
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void setStudentLeaveTable(TableVO vo, String parentPath, String path, File teacherExcel, String sheetName, String title){
		try{
			teacherExcel.createNewFile();
			XSSFWorkbook wb=new XSSFWorkbook();//XSSFWorkbook（2007版本）
			XSSFSheet sheet=wb.createSheet(sheetName);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			XSSFRow titrow=sheet.createRow(0);
			XSSFCell titCell=titrow.createCell(0);
			titCell.setCellValue(title);//标题
			CellStyle cellStyle1 = wb.createCellStyle();
			XSSFFont font2 = wb.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setFont(font2);//选择需要用到的字体格式
			titCell.setCellStyle(cellStyle1);
			XSSFRow titlerow=sheet.createRow(1);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font2);//选择需要用到的字体格式
			sheet.setColumnWidth(0, 14 * 256);
			XSSFCell titlecell=titlerow.createCell(0);
			titlecell.setCellValue("姓名");
			titlecell.setCellStyle(cellStyle);
			sheet.setColumnWidth(1, 14 * 256);
			XSSFCell titlecell1=titlerow.createCell(1);
			titlecell1.setCellValue("请假次数");
			titlecell1.setCellStyle(cellStyle);
			sheet.setColumnWidth(2, 14 * 256);
			XSSFCell titlecell2=titlerow.createCell(2);
			titlecell2.setCellValue("总时长");
			titlecell2.setCellStyle(cellStyle);
			sheet.setColumnWidth(3, 14 * 256);
			XSSFCell titlecell3=titlerow.createCell(3);
			titlecell3.setCellValue("病假");
			titlecell3.setCellStyle(cellStyle);
			sheet.setColumnWidth(4, 14 * 256);
			XSSFCell titlecell4=titlerow.createCell(4);
			titlecell4.setCellValue("事假");
			titlecell4.setCellStyle(cellStyle);
			List<HashMap<String,Object>> list=scoreTableService.getStudentLeaveCount(vo);
			for(int i=0;i<list.size();i++){//设置数据行显示的内容
				XSSFRow row=sheet.createRow(i+2);
				HashMap<String,Object> map=list.get(i);
				XSSFCell cell=row.createCell(0);
				Object value=map.get("student_name");
				cell.setCellValue(value.toString());
				cell.setCellStyle(cellStyle);

				XSSFCell cell1=row.createCell(1);
				Object value1=map.get("count");
				cell1.setCellValue(value1.toString());
				cell1.setCellStyle(cellStyle);

				Object leave1=map.get("041001");
				if (leave1==null)
					leave1=0;
				else if (StringUtil.isEmpty(leave1.toString()))
					leave1=0;
				Object leave2=map.get("041002");
				if (leave2==null)
					leave2=0;
				else if (StringUtil.isEmpty(leave2.toString()))
					leave2=0;
				XSSFCell cell2=row.createCell(2);
				cell2.setCellValue((Integer)leave1+(Integer)leave2+"天");
				cell2.setCellStyle(cellStyle);

				XSSFCell cell3=row.createCell(3);
				cell3.setCellValue(leave2.toString()+"天");
				cell3.setCellStyle(cellStyle);

				XSSFCell cell4=row.createCell(4);
				cell4.setCellValue(leave1.toString()+"天");
				cell4.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(list.size()+2, list.size()+2, 0, 4));
			XSSFRow daterow=sheet.createRow(list.size()+2);
			XSSFCell dateCell=daterow.createCell(0);
			dateCell.setCellValue("报表生成时间："+DateUtil.formatDateToString(ActionUtil.getSysTime(),DateUtil.Y_M_D_HM));
			dateCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+3, list.size()+3, 0, 4));
			XSSFRow teacherrow=sheet.createRow(list.size()+3);
			XSSFCell teacherCell=teacherrow.createCell(0);
			teacherCell.setCellValue("制表人："+redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
			teacherCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+4, list.size()+4, 0, 4));
			XSSFRow qianrow=sheet.createRow(list.size()+4);
			XSSFCell qianCell=qianrow.createCell(0);
			qianCell.setCellValue("签名：");
			qianCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(list.size()+5, list.size()+5, 0, 4));
			XSSFRow beirow=sheet.createRow(list.size()+5);
			XSSFCell beiCell=beirow.createCell(0);
			beiCell.setCellValue("备注：");
			beiCell.setCellStyle(cellStyle);
			OutputStream out = new FileOutputStream(parentPath+ Constants.FILE_PATH_DEFAULT+path);
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void setTable(TableVO vo, String parentPath, String path, File teacherExcel, String sheetName, String title) {
		try{
            TableHeadVO thvo=new TableHeadVO();
            thvo.setTeam_type(vo.getTeam_type());
            thvo.setScore_type(vo.getScore_type());
            thvo.setCount_type(vo.getCount_type());
            thvo.setSchool_id(ActionUtil.getSchoolID());
            List<TableHeadVO> list=scoreTableService.getTableHead(thvo);//表头
            teacherExcel.createNewFile();
            XSSFWorkbook wb=new XSSFWorkbook();//XSSFWorkbook（2007版本）
            XSSFSheet sheet=wb.createSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, list.size()-1));
            XSSFRow titrow=sheet.createRow(0);
            XSSFCell titCell=titrow.createCell(0);
            titCell.setCellValue(title);//标题
            CellStyle cellStyle1 = wb.createCellStyle();
            XSSFFont font2 = wb.createFont();
            font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle1.setFont(font2);//选择需要用到的字体格式
            titCell.setCellStyle(cellStyle1);
            XSSFRow titlerow=sheet.createRow(1);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(font2);//选择需要用到的字体格式
            for (int i=0;i<list.size();i++) {
                sheet.setColumnWidth(i, 14 * 256);
                XSSFCell titlecell=titlerow.createCell(i);
                titlecell.setCellValue(list.get(i).getField_name());
                titlecell.setCellStyle(cellStyle);
            }
            vo.setField_list(BeanUtil.ListTojson(list));
            vo.setSchool_id(ActionUtil.getSchoolID());
            List<HashMap<String,Object>> scoreList;
            if (StringUtil.isNotEmpty(vo.getSum_type()))
                scoreList=scoreTableService.getScoreCountFromRedis(vo);
            else scoreList=scoreTableService.getScoreCountOfCustom(vo);//自定义统计
            for(int i=0;i<scoreList.size();i++){//设置数据行显示的内容
				HashMap<String,Object> map=scoreList.get(i);
                XSSFRow row=sheet.createRow(i+2);
                for (int j=0;j<list.size();j++){
                    XSSFCell cell=row.createCell(j);
                    Object value=map.get(list.get(j).getField());
                    if (value==null)
                        cell.setCellValue("0");
                    else if (StringUtil.isEmpty(value.toString()))
                        cell.setCellValue("0");
                    else
                        cell.setCellValue(map.get(list.get(j).getField())+"");
                    cell.setCellStyle(cellStyle);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(scoreList.size()+2, scoreList.size()+2, 0, list.size()-1));
            XSSFRow daterow=sheet.createRow(scoreList.size()+2);
            XSSFCell dateCell=daterow.createCell(0);
            dateCell.setCellValue("报表生成时间："+DateUtil.formatDateToString(ActionUtil.getSysTime(),DateUtil.Y_M_D_HM));
            dateCell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(scoreList.size()+3, scoreList.size()+3, 0, list.size()-1));
            XSSFRow teacherrow=sheet.createRow(scoreList.size()+3);
            XSSFCell teacherCell=teacherrow.createCell(0);
            teacherCell.setCellValue("制表人："+redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(),0));
            teacherCell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(scoreList.size()+4, scoreList.size()+4, 0, list.size()-1));
            XSSFRow qianrow=sheet.createRow(scoreList.size()+4);
            XSSFCell qianCell=qianrow.createCell(0);
            qianCell.setCellValue("签名：");
            qianCell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(scoreList.size()+5, scoreList.size()+5, 0, list.size()-1));
            XSSFRow beirow=sheet.createRow(scoreList.size()+5);
            XSSFCell beiCell=beirow.createCell(0);
            beiCell.setCellValue("备注：");
            beiCell.setCellStyle(cellStyle);
            OutputStream out = new FileOutputStream(parentPath+ Constants.FILE_PATH_DEFAULT+path);
            wb.write(out);
            out.flush();
            out.close();
        } catch(IOException e){
            e.printStackTrace();
        }
	}

	private String getScoreDate(TableVO vo) {
		String key;
		switch (vo.getSum_type()) {
			case DictConstants.SUM_TYPE_DAY:
				key = DateUtil.getNow("");
				break;
			case DictConstants.SUM_TYPE_WEEK:
				key = DateUtil.getFirstDayOfWeek(ActionUtil.getSysTime(),"yyyy-MM-dd");
				break;
			case DictConstants.SUM_TYPE_MONTH:
				key =  DateUtil.getFirstDayOfMonth(ActionUtil.getSysTime(),"yyyy-MM-dd");
				break;
			case DictConstants.SUM_TYPE_YEAR:
				key= DateUtil.getFirstDayOfYear(ActionUtil.getSysTime(),"yyyy-MM-dd");
				break;
			default: key = "";
		}
		return key;
	}

	//下载文件，可更改用户所看到的文件名
	public void download(String path , String title,HttpServletResponse response ) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			String file_name=java.net.URLEncoder.encode(title+".xlsx", "UTF-8").replaceAll("\\+"," ");
			response.addHeader("Content-Disposition", "attachment;filename=" + file_name);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
