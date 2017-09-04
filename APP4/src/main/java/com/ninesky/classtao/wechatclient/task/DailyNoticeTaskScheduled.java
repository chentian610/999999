package com.ninesky.classtao.wechatclient.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.dynamic.vo.DynamicVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserSnsVO;
import com.ninesky.classtao.wechat.service.WxAccountService;
import com.ninesky.classtao.wechat.service.WxApiService;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.classtao.wechatclient.message.WechatDailyNoticeMessage;
import com.ninesky.classtao.wechatclient.message.WechatGetuiPush;
import com.ninesky.classtao.wechatclient.service.WechatService;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionParam;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.SystemConfig;

@Component
public class DailyNoticeTaskScheduled {
	
	private static final Logger logger = LoggerFactory.getLogger(DailyNoticeTaskScheduled.class);
	
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private UserService userService;
	@Autowired
	private WxApiService wxApiService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private DynamicService dynamicService;

	//定时发送微信模板消息【老师私信】给学生家长
	@Scheduled(cron="0 0 18 * * ?")
	public void pushDailyNoticeWechatParent(){
		logger.info("---------开始推送学生每日情况汇总消息-------");
		Date now = new Date();
		Date todayBegin;
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			todayBegin = f2.parse(f1.format(now) + " 00:00:00");
		} catch (ParseException e) {
			logger.info("---------推送学生每日情况汇总消息发生时间格式转换错误-------");
			return;
		}
		//只针对开通了微信公众号的学校发送
		ActionParam p = new ActionParam(false);
		ActionUtil.setActionParam(p);
		List<WxAccountVO> accounts = wxAccountService.getAllSchoolAccount();
		if(accounts == null || accounts.size() == 0){
			return;
		}
		logger.info("----共有" + accounts.size() + "个学校推送");
		Map<Integer, WxAccountVO> schoolWxAccountMap = new HashMap<Integer, WxAccountVO>();
		
		List<Integer> schoolIds = new ArrayList<Integer>();
		for(WxAccountVO account : accounts){
			schoolIds.add(account.getSchool_id());
			schoolWxAccountMap.put(account.getSchool_id(), account);
		}

		for(Integer school_id : schoolIds){
			WxAccountVO schoolWxAccount = schoolWxAccountMap.get(school_id);
			String accessToken = wxApiService.loadAccesstoken(schoolWxAccount);
			logger.info("-------开始推送学校【" + school_id + "】的每日老师私信消息");
			StudentVO stu = new StudentVO();
			stu.setSchool_id(school_id);
			List<StudentVO> students = userService.getStuUserList(stu);
			if(students == null || students.size() == 0){
				continue;
			}
			for(StudentVO student : students){
				//发送给绑定了微信家长端的学生家长
				List<UserSnsVO> snses = userService.getParentSnsAccount(Constants.SNS_TYPE_WECHAT, student.getStudent_id());
				if(snses.size() > 0){
					for(UserSnsVO sns : snses){
						List<String> openIds = new ArrayList<String>();
						DynamicVO vo = new DynamicVO();
						vo.setLimit(Constants.DEFAULT_LIMIT);
						p.setUser_type(DictConstants.USERTYPE_PARENT);
						p.setSchool_id(school_id);
						p.setUser_id(sns.getUser_id());
						p.setStudent_id(student.getStudent_id());
						ActionUtil.setActionParam(p);
						List<Map<String, Object>> list = dynamicService.getDynamicList(vo);
						int size = 0;
						for(Map<String, Object> map : list){
							Date d = new Date(Long.parseLong(String.valueOf(map.get("info_date"))));
							if(d.after(todayBegin)){
								size++;
							}
						}
						
					/*	if(size == 0){
							continue;
						}*/
						WechatDailyNoticeMessage message = new WechatDailyNoticeMessage();
						message.setTitle(student.getStudent_name() + "的家长，您好！");
						message.setTeacher("班主任老师");
						message.setDate(DateUtil.formatDateToString(now, DateUtil.Y_M_D_HMS));
						message.setRemark("点击查看具体内容");
						
						TeacherVO t = new TeacherVO();
						t.setClass_id(student.getClass_id());
						t.setSchool_id(school_id);
						t.setIs_charge(1);
						List<TeacherVO> ts = userService.getTeaUserListByDuty(t);
						if(ts != null && ts.size() > 0){
							message.setTeacher(ts.get(0).getTeacher_name());
						}
						
						message.setMessage("您的孩子今日有【" + size + "】条动态消息");
						logger.info("-------开始推送学生【" + student.getStudent_name() + "," + sns.getAccount() + "】的家长每日消息提醒，今日消息数量:" + openIds.size());
						openIds.add(sns.getAccount());
						//url暂时用家长端地址
						new Thread(new WechatGetuiPush(wxAccountService, wxApiService, wechatService, school_id, SystemConfig.getProperty(Constants.APP4_PLATFORM_CONFIG_KEY) + "/wechatAuthAction/login?accountId=" + schoolWxAccount.getAccount_id() + "&studentId=" + student.getStudent_id(), openIds, message, accessToken)).start();
					}
				}
			}
		}
	}
}
