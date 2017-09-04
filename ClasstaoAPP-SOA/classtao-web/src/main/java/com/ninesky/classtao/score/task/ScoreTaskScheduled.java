package com.ninesky.classtao.score.task;


import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.score.service.ScoreService;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.classtao.score.vo.TaskVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.List;



//@Component
public class ScoreTaskScheduled {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private GeneralDAO dao;
	private static Logger logger = LoggerFactory.getLogger(ScoreTaskScheduled.class);
	//定时发送普通教师接收短信
	//@Scheduled(cron="0 0 10 * * ?")
	public void pushScoreDataToClassTeacher(){
		TaskVO tvo=new TaskVO();
		List<TaskVO> list=scoreService.getScoreListTeacher(tvo);//得到所有有考勤班级的老师
		for(TaskVO teacher:list){//遍历教师
		    ScoreVO vo=new ScoreVO();
		    vo.setSchool_id(teacher.getSchool_id());//学校ID
			vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);//考勤
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);//教师考勤
			vo.setScore_date(teacher.getScore_date());//扣分时间
			//判断该教师是否为任课教师
			if(DictConstants.DICT_TEACHER_CLASS.equals(teacher.getDuty())||DictConstants.DICT_TEACHER_ADVISER.equals(teacher.getDuty())){//任课教师和班主任	
				vo.setTeam_id(teacher.getTeam_id());
				ScoreVO score=dao.queryObject("scoreMap.getNewAttendScore", vo);//最新一条考勤信息
				//计算实到人数
				Integer actually=score.getTeam_count()-score.getCount();
				float team_Cont = score.getTeam_count();
				//计算出勤率
				float rate=(actually/team_Cont)*100;
				//用DecimalFormat 返回的是String格式的
				DecimalFormat decimalFormat=new DecimalFormat(".00");//小數位數
				String rateString=decimalFormat.format(rate);//返回的是字符串類型
				
				//普通教师接收短信
				if(score!=null)
				messageService.sendMessage(teacher.getPhone(), MsgService.getMsg("MESSAGE_TEACHER_ATTEND",teacher.getTeam_name(),actually,rateString+"%",score.getCreate_date()));
			}	
		
		}
			
	}
	//定时发送管理层教师短信
	//@Scheduled(cron="0 0 10 * * ?")
	public void pushScoreDataToSchoolLeader() {
		TaskVO tvo=new TaskVO();
		List<TaskVO> list=scoreService.getScoreListSchoolLeader(tvo);//得到所有有考勤班级的老师
			for (TaskVO leader:list) {
				 ScoreVO vo=new ScoreVO();
				 vo.setSchool_id(leader.getSchool_id());//学校ID
				 vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);//考勤
				 vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);//教师考勤
				 String time_attend=DateUtil.getNow("yyyy-MM-dd");
				 vo.setScore_date(time_attend);//统计时间
				 int count=dao.queryObject("scoreMap.getCountClass", vo);//得到今日考勤的班级
				 List<ScoreVO> slist=dao.queryForList("scoreMap.getAttendAmount", vo);
				 int actually=0;//统计班级的实到人数
				 int sum=0;//统计班级的总人数
				 for(ScoreVO svo:slist){//遍历考勤的班级
					 actually=actually+(svo.getTeam_count()-svo.getCount());
					 sum=sum+svo.getTeam_count(); 
				 }
				 float sum_count=sum;
				 float rate=(actually/sum_count)*100;//出勤率
				 //用DecimalFormat 返回的是String格式的
				 DecimalFormat decimalFormat=new DecimalFormat(".00");//小數位數
				 String rateString=decimalFormat.format(rate);//返回的是字符串類型
				 String time=DateUtil.getNow("yyyy-MM-dd");//获取系统时间
				 //管理层接收消息
				 if(count>0)
				 messageService.sendMessage(leader.getPhone(), MsgService.getMsg("MESSAGE_TEACHER_LEADER_ATTEND",count,actually,rateString+"%",time));
					
			}
	  }
}
