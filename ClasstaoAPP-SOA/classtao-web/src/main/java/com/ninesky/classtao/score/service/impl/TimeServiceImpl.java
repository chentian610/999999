package com.ninesky.classtao.score.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;

@Service("timeServiceImpl")
public class TimeServiceImpl {
	
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private GeneralDAO dao;
	
	//考勤的定时推送内容（普通教师和管理层）
	public void timePush(){
		TeacherVO tvo=new TeacherVO();
		List<TeacherVO> tlist=classService.getTeacherList(tvo);//全校教师
		for(TeacherVO teacher:tlist){
			ScoreVO vo=new ScoreVO();
			vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);//教室考勤
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			try {
				vo.setScore_date(DateUtil.formatDate(ActionUtil.getSysTime(), "yyyy-MM-dd"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			InfoVO ivo=new InfoVO();
			ivo.setModule_code(DictConstants.MODULE_CODE_ATTEND);
			List<String> accountList=new ArrayList<String>();
			if(DictConstants.DICT_TEACHER_CLASS.equals(teacher.getDuty())){//普通任课教师
				vo.setGroup_id(teacher.getGrade_id());
				vo.setTeam_id(teacher.getClass_id());
				ScoreVO score=dao.queryObject("scoreMap.getScore1", vo);//最新一条考勤信息
				int actually=score.getTeam_count()-score.getCount();
				int rate=(actually/score.getTeam_count())*100;
				String msg="您的班级："+teacher.getClass_name()+"今日实到"+actually+"人，出勤率"+rate+
						"%，统计时间"+score.getCreate_date()+"，详情请点击进入查看";
				ivo.setTransmissionContent(msg);//推送内容

				//教师推送手机消息
				messageService.sendMessage(teacher.getPhone(), MsgService.getMsg("MESSAGE_TEACHER_ATTEND",teacher.getClass_name(),actually,rate+"%",score.getCreate_by()));
				ivo.setSchool_id(teacher.getSchool_id());	
//title和content为空可以吗
				ivo.setModule_pkid(score.getScore_id());
				
				
				accountList.add(teacher.getUser_id().toString());
				infoService.gtPushListToApp(ivo, accountList);
				
				
			}else{//管理层
				int count=dao.queryObject("scoreMap.getCount", vo);//今日考勤过的班级数
				List<ScoreVO> list=dao.queryForList("scoreMap.getScore", vo);
				int actually=0;//实到数
				int sum=0;//总人数
				for(ScoreVO svo:list){
					actually=actually+(svo.getTeam_count()-svo.getCount());
					sum=sum+svo.getTeam_count();
				}
				int rate=(actually/sum)*100;//出勤率
				String time="";
				try {
					time=DateUtil.formatDate(ActionUtil.getSysTime(), "HH:mm");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String msg="本校今日统计班级"+count+"个，实到"+actually+"人，出勤率"+rate+"%，统计时间"+time+
						"，详情请点击进入";
				ivo.setTransmissionContent(msg);//推送内容
				
				//管理层推送手机消息
				messageService.sendMessage(teacher.getPhone(), MsgService.getMsg("MESSAGE_TEACHER_LEADER_ATTEND",count,actually,rate+"%",time));
				ivo.setSchool_id(ActionUtil.getSchoolID());
				//pkid没有
				accountList.add(teacher.getUser_id().toString());
				infoService.gtPushListToApp(ivo, accountList);
			}
		}
	}
}


