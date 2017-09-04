package com.ninesky.classtao.teacherAttend.service.impl;

import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.teacherAttend.service.TeacherAttendService;
import com.ninesky.classtao.teacherAttend.vo.TeacherAttendVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.ListUtil;
import com.ninesky.framework.GeneralDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("teacherAttendServiceImpl")
public class TeacherAttendServiceImpl implements TeacherAttendService{

    @Autowired
    private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SchoolService schoolService;

    //教师考勤打卡
    public TeacherAttendVO addTeacherAttend(TeacherAttendVO vo) {
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setUser_id(ActionUtil.getUserID());
        vo.setAttend_time(ActionUtil.getSysTime());
        vo.setCreate_by(ActionUtil.getUserID());
        vo.setCreate_date(ActionUtil.getSysTime());
        SchoolVO school=schoolService.getSchoolById(ActionUtil.getSchoolID());
        if (DictConstants.TEACHER_ATTEND_TYPE_GOWORK.equals(vo.getAttend_type())) {//上班
            if (vo.getAttend_time().getTime()>DateUtil.formatStringToDate(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd")+" "+school.getStart_work_date(),"yyyy-MM-dd HH:mm:ss").getTime()){
                vo.setAttend_status(DictConstants.TEACHER_ATTEND_LATE);
            } else vo.setAttend_status(DictConstants.TEACHER_ATTEND_NORMAL);
        } else {
            if (vo.getAttend_time().getTime()>DateUtil.formatStringToDate(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd")+" "+school.getEnd_work_date(),"yyyy-MM-dd HH:mm:ss").getTime()){
                vo.setAttend_status(DictConstants.TEACHER_ATTEND_NORMAL);
            } else vo.setAttend_status(DictConstants.TEACHER_ATTEND_LEAVE_EARLY);//早退
        }
        vo.setAttend_id(dao.insertObjectReturnID("teacherAttendMap.insertTeacherAttend",vo));
        vo.setWork_time(school.getStart_work_date().substring(0,5));
        vo.setClose_time(school.getEnd_work_date().substring(0,5));
        vo.setWork_address(school.getAddress());
        vo.setWork_longitude(school.getLongitude());//公司经度
        vo.setWork_latitude(school.getLatitude());
        vo.setAttend_range(school.getAttend_range());
        return vo;
    }

    //教师考勤打卡更新
    public TeacherAttendVO updateTeacherAttend(TeacherAttendVO vo){
        vo.setAttend_time(ActionUtil.getSysTime());
        SchoolVO school=schoolService.getSchoolById(ActionUtil.getSchoolID());
        if (DictConstants.TEACHER_ATTEND_TYPE_GOWORK.equals(vo.getAttend_type())) {
            if (vo.getAttend_time().getTime()>DateUtil.formatStringToDate(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd")+" "+school.getStart_work_date(),"yyyy-MM-dd HH:mm:ss").getTime()){
                vo.setAttend_status(DictConstants.TEACHER_ATTEND_LATE);
            } else vo.setAttend_status(DictConstants.TEACHER_ATTEND_NORMAL);
        } else {
            if (vo.getAttend_time().getTime()>DateUtil.formatStringToDate(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd")+" "+school.getEnd_work_date(),"yyyy-MM-dd HH:mm:ss").getTime()){
                vo.setAttend_status(DictConstants.TEACHER_ATTEND_NORMAL);
            } else vo.setAttend_status(DictConstants.TEACHER_ATTEND_LEAVE_EARLY);
        }
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("teacherAttendMap.updateTeacherAttend",vo);
        vo.setWork_time(school.getStart_work_date().substring(0,5));
        vo.setClose_time(school.getEnd_work_date().substring(0,5));
        vo.setWork_address(school.getAddress());
        vo.setWork_longitude(school.getLongitude());//公司经度
        vo.setWork_latitude(school.getLatitude());
        vo.setAttend_range(school.getAttend_range());
        return vo;
    }

    //获取当天打卡记录
    public List<TeacherAttendVO> getAttendByUserID(){
        TeacherAttendVO vo=new TeacherAttendVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setUser_id(ActionUtil.getUserID());
        vo.setSearch_time(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));//当天
        List<TeacherAttendVO> list=dao.queryForList("teacherAttendMap.getTeacherAttend",vo);
        if (ListUtil.isEmpty(list)){
            TeacherAttendVO attend=new TeacherAttendVO();
            attend.setAttend_type(DictConstants.TEACHER_ATTEND_TYPE_GOWORK);//上班
            list.add(attend);
            TeacherAttendVO attend1=new TeacherAttendVO();
            attend1.setAttend_type(DictConstants.TEACHER_ATTEND_TYPE_OFFWORK);//下班
            list.add(attend1);
        } else if (list.size()==1){
            TeacherAttendVO attend1=new TeacherAttendVO();
            attend1.setAttend_type(DictConstants.TEACHER_ATTEND_TYPE_OFFWORK);
            list.add(attend1);
        }
        SchoolVO school=schoolService.getSchoolById(ActionUtil.getSchoolID());
        for (TeacherAttendVO attendVO:list){
            if (DictConstants.TEACHER_ATTEND_TYPE_GOWORK.equals(attendVO.getAttend_type()))
                attendVO.setWork_time(school.getStart_work_date().substring(0,5));//上班时间
            else attendVO.setClose_time(school.getEnd_work_date().substring(0,5));
            attendVO.setWork_address(school.getAddress());
            attendVO.setWork_longitude(school.getLongitude());//公司经度
            attendVO.setWork_latitude(school.getLatitude());
            attendVO.setAttend_range(school.getAttend_range());
        }
        return list;
    }

    //获取教师打卡记录列表
    public List<TeacherAttendVO> getAttendListByUserID(String search_time){
        TeacherAttendVO vo=new TeacherAttendVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setUser_id(ActionUtil.getUserID());
        vo.setSearch_time(search_time);
        List<TeacherAttendVO> list=dao.queryForList("teacherAttendMap.getTeacherAttendList",vo);
        SchoolVO school=schoolService.getSchoolById(ActionUtil.getSchoolID());
        for (TeacherAttendVO attendVO:list){
            attendVO.setWork_time(school.getStart_work_date().substring(0,5));
            attendVO.setClose_time(school.getEnd_work_date().substring(0,5));
        }
        return list;
    }

    //统计
    public List<TeacherAttendVO> getTeacherAttendCount(){
        TeacherAttendVO vo=new TeacherAttendVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setSearch_time(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));
        List<TeacherAttendVO> list=dao.queryForList("teacherAttendMap.getTeacherAttendCount",vo);
        //取得异常人数(迟到早退)
        for (TeacherAttendVO attend:list) {
            if (DictConstants.TEACHER_ATTEND_NORMAL.equals(attend.getAttend_status())){
                list.remove(attend);
                break;
            }
        }
        //正常
        List<TeacherAttendVO> attendList=dao.queryForList("teacherAttendMap.getTeacherAttendNormalCount",vo);
        int count=0;
        for (TeacherAttendVO attendVO:attendList) {
            if (attendVO.getCount()==2) count++;
        }
        TeacherAttendVO teacherAttendVO=new TeacherAttendVO();
        teacherAttendVO.setAttend_status(DictConstants.TEACHER_ATTEND_NORMAL);
        teacherAttendVO.setCount(count);
        list.add(teacherAttendVO);
        //缺勤
        TeacherVO teacher=new TeacherVO();
        teacher.setSchool_id(ActionUtil.getSchoolID());
        int teacherCount=dao.queryObject("teacherMap.getTeacherCount",teacher);
        int teacherAttendCount=dao.queryObject("teacherAttendMap.getTeacherCount",vo);
        TeacherAttendVO attendVO1=new TeacherAttendVO();
        attendVO1.setAttend_status(DictConstants.TEACHER_ATTEND_ABSENCE);
        attendVO1.setCount(teacherCount-teacherAttendCount);
        list.add(attendVO1);
        return list;
    }

    //统计详情
    public List<TeacherAttendVO> getTeacherAttendCountDetail(String attend_status){
        TeacherAttendVO vo=new TeacherAttendVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setSearch_time(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));
        List<TeacherAttendVO> list=new ArrayList<TeacherAttendVO>();
        if (DictConstants.TEACHER_ATTEND_NORMAL.equals(attend_status)) {//正常
            List<TeacherAttendVO> tlist=dao.queryForList("teacherAttendMap.getTeacherAttendNormalCount",vo);
            for (TeacherAttendVO teacherAttendVO:tlist){
                if (teacherAttendVO.getCount()==2) list.add(teacherAttendVO);
            }
        } else if (DictConstants.TEACHER_ATTEND_ABSENCE.equals(attend_status)) {//缺勤
            list=dao.queryForList("teacherAttendMap.getAbsenceTeacher",vo);
            return list;
        } else {//迟到，早退
            vo.setAttend_status(attend_status);
            list=dao.queryForList("teacherAttendMap.getTeacherAttendByStatus",vo);
        }
        for (TeacherAttendVO attend:list){
            attend.setTeacher_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,attend.getUser_id(),0));
        }
        return list;
    }
}
