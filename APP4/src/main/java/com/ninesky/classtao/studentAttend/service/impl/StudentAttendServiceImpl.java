package com.ninesky.classtao.studentAttend.service.impl;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.studentAttend.service.StudentAttendService;
import com.ninesky.classtao.studentAttend.vo.StudentAttendVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("studentAttendServiceImpl")
public class StudentAttendServiceImpl implements StudentAttendService{

    @Autowired
    private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GetuiService getuiService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private DynamicService dynamicService;

    //学生打卡
    public StudentAttendVO addStudentAttend(StudentAttendVO vo){
        if (StringUtil.isEmpty(vo.getCard_number())) throw new BusinessException("请输入通勤卡卡号！");
        StudentVO student=new StudentVO();
        student.setCard_number(vo.getCard_number());
        student.setSchool_id(vo.getSchool_id());
        StudentVO svo=dao.queryObject("studentMap.getStudentByCard",student);//根据卡号获取学生信息
        vo.setSchool_id(svo.getSchool_id());
        vo.setGroup_id(svo.getGrade_id());
        vo.setTeam_id(svo.getClass_id());
        vo.setStudent_id(svo.getStudent_id());
        vo.setCreate_date(ActionUtil.getSysTime());
        vo.setAttend_id(dao.insertObjectReturnID("studentAttendMap.insertStudentAttend",vo));
        vo.setStudent_name(redisService.getUserName(svo.getSchool_id(),DictConstants.USERTYPE_STUDENT,0,svo.getStudent_id()));
        //动态推送
        HashMap<String,String> dataMap=new HashMap<String,String>();
        dataMap.put("info_title", MsgService.getMsg("STUDENT_ATTEND_TITLE",vo.getStudent_name()));
        dataMap.put("module_code",DictConstants.MODULE_CODE_STUDENT_ATTEND);
        dataMap.put("module_pkid",vo.getAttend_id().toString());
        dataMap.put("link_type",DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url","detail.html");
        dataMap.put("info_date",ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type",DictConstants.USERTYPE_STUDENT);
        dataMap.put("student_id",svo.getStudent_id().toString());
        dynamicService.insertDynamicByStuID(dataMap,svo.getStudent_id());
        getuiService.pushMessageByStuID(dataMap,svo.getStudent_id());
        return vo;
    }

    //获取学生打卡记录
    public List<StudentAttendVO> getAttendListByStuID(StudentAttendVO svo) {
        svo.setStudent_id(ActionUtil.getStudentID());
        List<StudentAttendVO> list=dao.queryForList("studentAttendMap.getStudentAttendList", svo);
        for (StudentAttendVO vo:list) {
            vo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,0,ActionUtil.getStudentID()));
        }
        return list;
    }

    //获取学生打卡详情
    public StudentAttendVO getAttendByID(Integer attend_id){
        StudentAttendVO vo=dao.queryObject("studentAttendMap.getStudentAttendById",attend_id);
        vo.setHead_url(Constants.HEAD_URL_DEFAULT);
        vo.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,vo.getStudent_id()));
        return vo;
    }

    //统计
    public StudentAttendVO getAttendList(StudentAttendVO vo){
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setSearch_time(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));//当天时间
        SchoolVO school=schoolService.getSchoolById(ActionUtil.getSchoolID());
        vo.setWork_time(DateUtil.formatStringToDate(vo.getSearch_time()+" "+school.getStart_school_date(),"yyyy-MM-dd HH:mm:ss"));
        vo.setClose_time(DateUtil.formatStringToDate(vo.getSearch_time()+" "+school.getEnd_school_date(),"yyyy-MM-dd HH:mm:ss"));
        List<StudentAttendVO> normalList=dao.queryForList("studentAttendMap.getNormalAttendList",vo);
        List<StudentAttendVO> abnormalList=dao.queryForList("studentAttendMap.getAbnormalAttendList",vo);
        StudentAttendVO attend=new StudentAttendVO();
        attend.setNormal_count(normalList.size());//正常次数
        attend.setAbnormal_count(abnormalList.size());
        for (StudentAttendVO attendVO:abnormalList) {
            attendVO.setStudent_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,attendVO.getStudent_id()));
        }
        attend.setAbnormal_list(BeanUtil.ListTojson(abnormalList,false));//异常记录
        return attend;
    }
}
