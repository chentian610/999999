package com.ninesky.classtao.enroll.service.impl;

import com.ninesky.classtao.enroll.service.EnrollService;
import com.ninesky.classtao.enroll.vo.StudentEnrollVO;
import com.ninesky.classtao.enroll.vo.StudentRecruitVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;


@Service("enrollServiceImpl")
public class EnrollServiceImpl implements EnrollService {

    @Autowired
    private GeneralDAO dao;

    //获取学校招生简章
    public List<StudentRecruitVO> getRecruitList(StudentRecruitVO vo){
        vo.setApply_end_date(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));
         List<StudentRecruitVO> list=dao.queryForList("studentRecruitMap.getRecruitList",vo);//只获取报名未结束的
        for (StudentRecruitVO rvo:list) {
            List<StudentEnrollVO> slist=dao.queryForList("studentEnrollMap.getStudentList",rvo.getRecruit_id());
            rvo.setCount(slist.size());//报名人数
        }
        return list;
    }

    //获取学校招生简章详情
    public StudentRecruitVO getRecruitByID(Integer recruit_id){
        StudentRecruitVO vo=dao.queryObject("studentRecruitMap.getRecruitByID",recruit_id);
        return vo;
    }

    //新生报名
    public void enroll(StudentEnrollVO vo){
        if (IntegerUtil.isEmpty(vo.getRecruit_id())) throw new BusinessException(MsgService.getMsg("ENROLL_RECRUIT_NULL"));
        StudentEnrollVO svo=dao.queryObject("studentEnrollMap.getStudent",vo);
        if (svo!=null) throw new BusinessException("该学生已经报名过");
        vo.setCreate_date(ActionUtil.getSysTime());
        vo.setEnroll_status(DictConstants.ENROLL_STATUS_NEW);
        dao.insertObject("studentEnrollMap.addStudent",vo);
    }

    //录取率，录取人数，报名人数，男女比例
    public StudentRecruitVO getEnrollStatus(StudentEnrollVO enroll){
        DecimalFormat df = new DecimalFormat("0.00");
        StudentRecruitVO svo=new StudentRecruitVO();
        enroll.setSex(0);
        List<StudentEnrollVO> manList=dao.queryForList("studentEnrollMap.getStudentListBySome",enroll);
        double manSize=manList.size();//录取男生人数
        enroll.setSex(1);
        List<StudentEnrollVO> womanList=dao.queryForList("studentEnrollMap.getStudentListBySome",enroll);
        double womanSize=womanList.size();//录取女生人数
        enroll.setSex(null);
        enroll.setEnroll_status(DictConstants.ENROLL_STATUS_PASS);
        List<StudentEnrollVO> list=dao.queryForList("studentEnrollMap.getStudentListBySome",enroll);
        double pass_count=list.size();//已录取的人数
        List<StudentEnrollVO> slist=dao.queryForList("studentEnrollMap.getStudentList",enroll.getRecruit_id());
        double count=slist.size();//报名人数
        double acceptance_rate=0;
        if (count==0) acceptance_rate=0;
        else acceptance_rate=(pass_count/count)*100;//录取比例
        svo.setAcceptance_rate(df.format(acceptance_rate));
        svo.setCount((int)count);
        svo.setEnroll_count((int)pass_count);
        //男女比例
        if (womanSize==0 || manSize==0) svo.setRatio((int)manSize+":"+(int)womanSize);
        else if (manSize>womanSize)
            svo.setRatio(df.format(manList.size()/womanList.size())+":1");
        else if (manSize<womanSize)
            svo.setRatio("1:"+df.format(womanSize/manSize));
        else if (manSize==womanSize)
            svo.setRatio("1:1");
        return svo;
    }

    //获取报名学生
    public List<StudentEnrollVO> getStudentList(StudentEnrollVO vo){
        return dao.queryForList("studentEnrollMap.getStudentListBySome",vo);
    }

    //获取全部招生简章
    public List<StudentRecruitVO> getAllRecruitList(StudentRecruitVO vo){
        List<StudentRecruitVO> list=dao.queryForList("studentRecruitMap.getRecruitList",vo);
        for (StudentRecruitVO rvo:list) {
            List<StudentEnrollVO> slist=dao.queryForList("studentEnrollMap.getStudentList",rvo.getRecruit_id());
            rvo.setCount(slist.size());//报名人数
            StudentEnrollVO svo=new StudentEnrollVO();
            svo.setRecruit_id(rvo.getRecruit_id());
            svo.setEnroll_status(DictConstants.ENROLL_STATUS_PASS);
            List<StudentEnrollVO> elist=dao.queryForList("studentEnrollMap.getStudentListBySome",svo);
            rvo.setEnroll_count(elist.size());//录取人数
        }
        return list;
    }

    //添加学校招生简章
    public void addRecruit(StudentRecruitVO vo){
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setCreate_by(ActionUtil.getUserID());
        vo.setCreate_date(ActionUtil.getSysTime());
        dao.insertObject("studentRecruitMap.addRecruit",vo);
    }

    //修改学校招生简章
    public void updateRecruit(StudentRecruitVO vo){
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentRecruitMap.updateRecruit",vo);
    }

    //录取
    public void admission(StudentEnrollVO vo){
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentEnrollMap.admission",vo);
    }

    //获取报名学生详情
    public StudentEnrollVO getStudentByID(StudentEnrollVO vo){
        return dao.queryObject("studentEnrollMap.getStudentByID",vo.getId());
    }

    //完成招生录取
    public void completeEnroll(StudentRecruitVO vo){
        vo.setCompletion_date(DateUtil.formatDate(ActionUtil.getSysTime(),"yyyy-MM-dd"));//招生录取完成时间
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
        dao.updateObject("studentRecruitMap.completeEnroll",vo);
    }
}
