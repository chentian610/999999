package com.ninesky.classtao.studentLeave.service.impl;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.studentLeave.service.StudentLeaveService;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveFileVO;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("studentLeaveImpl")
public class StudentLeaveImpl implements StudentLeaveService {

    @Autowired
    private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ClassService classService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private GetuiService getuiService;

    //学生请假
    public void addStudentLeave(StudentLeaveVO vo){
        if (IntegerUtil.isEmpty(vo.getApprover_id())) throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_APPROVER_NULL"));
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setLeave_status(DictConstants.LEAVE_STATUS_NEW);//审批中
        vo.setCreate_by(ActionUtil.getUserID());
        vo.setCreate_date(ActionUtil.getSysTime());
        int id=dao.insertObjectReturnID("studentLeaveMap.insertStudentLeave",vo);
        if (StringUtil.isNotEmpty(vo.getFile_list())) {//请假照片
            List<StudentLeaveFileVO> list= BeanUtil.jsonToList(vo.getFile_list(),StudentLeaveFileVO.class);
            for (StudentLeaveFileVO file:list) {
                file.setLeave_id(id);
                file.setCreate_by(ActionUtil.getUserID());
                file.setCreate_date(ActionUtil.getSysTime());
                dao.insertObject("studentLeaveFileMap.insertStudentLeaveFile",file);
            }
        }
        //动态推送
        String teacher_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getApprover_id(),0);
        String student_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,vo.getStudent_id());
        HashMap<String,String> dataMap=new HashMap<String,String>();
        dataMap.put("info_title",MsgService.getMsg("STUDENT_LEAVE_APPLY",teacher_name,student_name));
        dataMap.put("module_code",DictConstants.MODULE_CODE_STUDENT_LEAVE);
        dataMap.put("module_pkid",id+"");
        dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url","detail.html");
        dataMap.put("info_date",ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type",DictConstants.USERTYPE_TEACHER);
        ReceiveVO receive=new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getApprover_id());
        dynamicService.insertSingleDynamic(dataMap,receive);
        getuiService.pushMessage(dataMap,vo.getApprover_id());
    }

    //获取学生请假记录
    public List<StudentLeaveVO> getStudentLeaveList(StudentLeaveVO vo){
        List<StudentLeaveVO> list = dao.queryForList("studentLeaveMap.getStudentLeaveList",vo);
        for (StudentLeaveVO leaveVO:list) {
            leaveVO.setHead_url(Constants.HEAD_URL_DEFAULT);//学生头像
            leaveVO.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,
                    vo.getStudent_id()));//学生姓名
            leaveVO.setTeam_name(classService.getClassByID(leaveVO.getTeam_id()).getClass_name());//班级名称
        }
        return list;
    }

    //获取学生请假详情
    public StudentLeaveVO getStudentLeave(Integer leave_id){
        StudentLeaveVO vo=dao.queryObject("studentLeaveMap.getStudentLeave",leave_id);
        vo.setHead_url(Constants.HEAD_URL_DEFAULT);//学生头像
        vo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,
                vo.getStudent_id()));//学生姓名
        vo.setTeam_name(classService.getClassByID(vo.getTeam_id()).getClass_name());//班级名称
        if (IntegerUtil.isNotEmpty(vo.getMaster_id())) {
            vo.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
                    vo.getMaster_id(), 0));
            vo.setTransfer_teacher_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
                    vo.getApprover_id(), 0));
            vo.setTransfer_date(vo.getUpdate_date());
        }else vo.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,
                vo.getApprover_id(),0));
        List<StudentLeaveFileVO> list=dao.queryForList("studentLeaveFileMap.getFileList",leave_id);
        if (ListUtil.isNotEmpty(list)) vo.setFile_list(BeanUtil.ListTojson(list));
        return vo;
    }

    //撤销学生请假
    public void  cancelStudentLeave(StudentLeaveVO vo){
        StudentLeaveVO leaveVO=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        if (!DictConstants.LEAVE_STATUS_NEW.equals(leaveVO.getLeave_status()))
            throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_PROCESS"));
        vo.setLeave_status(DictConstants.LEAVE_STATUS_CANCEL);//撤销状态
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentLeaveMap.updateStudentLeaveStatus",vo);
    }

    //删除图片
    public void deleteFileById(Integer id){
        dao.deleteObject("studentLeaveFileMap.deleteFileByID",id);
    }

    //教师待处理列表
    public List<StudentLeaveVO> getUnTreatedList(StudentLeaveVO vo){
        vo.setApprover_id(ActionUtil.getUserID());
        List<StudentLeaveVO> list=dao.queryForList("studentLeaveMap.getUnTreatedList",vo);
        setInfo(list);//设置相关信息
        return list;
    }

    //设置相关请假信息
    private void setInfo(List<StudentLeaveVO> list) {
        for (StudentLeaveVO leaveVO:list) {
            leaveVO.setHead_url(Constants.HEAD_URL_DEFAULT);//学生头像
            leaveVO.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,
                    leaveVO.getStudent_id()));//学生姓名
            leaveVO.setTeam_name(classService.getClassByID(leaveVO.getTeam_id()).getClass_name());//班级名称
            if (ActionUtil.getUserID().equals(leaveVO.getMaster_id())){//收到转交
                leaveVO.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,
                        leaveVO.getApprover_id(),0));//谁转交
                leaveVO.setTransfer_date(leaveVO.getUpdate_date());//转交时间
            }
        }
    }

    //教师已处理请假列表
    public List<StudentLeaveVO> getTreatedList(StudentLeaveVO vo){
        vo.setApprover_id(ActionUtil.getUserID());
        List<StudentLeaveVO> list=dao.queryForList("studentLeaveMap.getTreatedList",vo);
        setInfo(list);//设置请假相关信息
        return list;
    }

    //同意请假
    public void passStudentLeave(StudentLeaveVO vo){
        StudentLeaveVO leaveVO=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        if (DictConstants.LEAVE_STATUS_CANCEL.equals(leaveVO.getLeave_status()) ||
                (IntegerUtil.isEmpty(leaveVO.getMaster_id()) &&
                        !ActionUtil.getUserID().equals(leaveVO.getApprover_id())))
            throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_CANCEL"));
        vo.setLeave_status(DictConstants.LEAVE_STATUS_PASS);//同意状态
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentLeaveMap.updateStudentLeaveStatus",vo);
        //动态推送
        StudentLeaveVO leave=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        String student_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,leave.getStudent_id());
        HashMap<String,String> dataMap=new HashMap<String,String>();
        dataMap.put("info_title",MsgService.getMsg("STUDENT_LEAVE_PASS",student_name,student_name));
        dataMap.put("module_code",DictConstants.MODULE_CODE_STUDENT_LEAVE);
        dataMap.put("module_pkid",vo.getLeave_id().toString());
        dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url","detail.html");
        dataMap.put("info_date",ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
        dataMap.put("student_id",leave.getStudent_id().toString());
        dynamicService.insertDynamicByStuID(dataMap,leave.getStudent_id());
        getuiService.pushMessageByStuID(dataMap,leave.getStudent_id());
    }

    //转交请假申请
    public void transferStudentLeave(StudentLeaveVO vo){
        if (IntegerUtil.isEmpty(vo.getMaster_id())) throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_MASTER_NULL"));
        StudentLeaveVO leaveVO=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        if (DictConstants.LEAVE_STATUS_CANCEL.equals(leaveVO.getLeave_status()))
            throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_CANCEL"));
        vo.setLeave_status(DictConstants.LEAVE_STATUS_MASTER);//转交状态
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentLeaveMap.updateStudentLeaveStatus",vo);
        //动态推送
        String teacher_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getMaster_id(),0);
        StudentLeaveVO leave=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        String student_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,leave.getStudent_id());
        HashMap<String,String> dataMap=new HashMap<String,String>();
        dataMap.put("info_title",MsgService.getMsg("sTUDENT_LEAVE_TRANSFER",teacher_name,student_name));
        dataMap.put("module_code",DictConstants.MODULE_CODE_STUDENT_LEAVE);
        dataMap.put("module_pkid",vo.getLeave_id().toString());
        dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url","detail.html");
        dataMap.put("info_date",ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type",DictConstants.USERTYPE_TEACHER);
        ReceiveVO receive=new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getMaster_id());
        dynamicService.insertSingleDynamic(dataMap,receive);
        getuiService.pushMessage(dataMap,vo.getMaster_id());
    }

    //拒绝请假申请
    public void refuseStudentLeave(StudentLeaveVO vo){
        StudentLeaveVO leaveVO=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        if (DictConstants.LEAVE_STATUS_CANCEL.equals(leaveVO.getLeave_status()) ||
                (IntegerUtil.isEmpty(leaveVO.getMaster_id()) &&
                        !ActionUtil.getUserID().equals(leaveVO.getApprover_id())))
            throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_CANCEL"));
        vo.setLeave_status(DictConstants.LEAVE_STATUS_RECALL);//拒绝状态
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentLeaveMap.updateStudentLeaveStatus",vo);
        //动态推送
        StudentLeaveVO leave=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        String student_name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,leave.getStudent_id());
        HashMap<String,String> dataMap=new HashMap<String,String>();
        dataMap.put("info_title",MsgService.getMsg("STUDENT_LEAVE_REFUSE",student_name,student_name));
        dataMap.put("module_code",DictConstants.MODULE_CODE_STUDENT_LEAVE);
        dataMap.put("module_pkid",vo.getLeave_id().toString());
        dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url","detail.html");
        dataMap.put("info_date",ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
        dataMap.put("student_id",leave.getStudent_id().toString());
        dynamicService.insertDynamicByStuID(dataMap,leave.getStudent_id());
        getuiService.pushMessageByStuID(dataMap,leave.getStudent_id());
    }

    //学生请假统计
    public List<StudentLeaveVO> getStudentLeaveCount(StudentLeaveVO vo){
        vo.setLeave_status(DictConstants.LEAVE_STATUS_PASS);//已通过的才算
        vo.setSchool_id(ActionUtil.getSchoolID());
        List<StudentLeaveVO> list=dao.queryForList("studentLeaveMap.getStudentLeaveCount",vo);
        for (StudentLeaveVO leave:list) {
            leave.setHead_url(Constants.HEAD_URL_DEFAULT);//学生头像
            leave.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,
                    leave.getStudent_id()));//学生姓名
        }
        return list;
    }

    //学生请假统计详情
    public List<StudentLeaveVO> getStudentLeaveCountDetail(StudentLeaveVO vo){
        vo.setLeave_status(DictConstants.LEAVE_STATUS_PASS);//已通过的才算
        return dao.queryForList("studentLeaveMap.getStudentLeaveCountDetail",vo);
    }

    //判断学生是否请假
    public List<StudentLeaveVO> getPassLeaveByStuID (StudentLeaveVO vo){
        return dao.queryForList("studentLeaveMap.getPassLeaveByStuID",vo);
    }

    //撤销转交申请
    public void cancelTransfer(StudentLeaveVO vo){
        StudentLeaveVO leaveVO=dao.queryObject("studentLeaveMap.getStudentLeave",vo.getLeave_id());
        if (!DictConstants.LEAVE_STATUS_MASTER.equals(leaveVO.getLeave_status()))
            throw new BusinessException(MsgService.getMsg("STUDENT_LEAVE_PROCESS"));
        vo.setLeave_status(DictConstants.LEAVE_STATUS_NEW);
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentLeaveMap.cancelTransfer",vo);
    }
}
