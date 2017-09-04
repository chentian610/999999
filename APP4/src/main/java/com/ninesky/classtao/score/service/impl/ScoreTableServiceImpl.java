package com.ninesky.classtao.score.service.impl;

import java.util.*;

import com.ninesky.classtao.leave.service.LeaveService;
import com.ninesky.classtao.leave.vo.LeaveCountVO;
import com.ninesky.classtao.leave.vo.LeaveVO;
import com.ninesky.classtao.module.service.ModuleService;
import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.classtao.school.vo.GradeVO;
import com.ninesky.classtao.score.service.ScoreService;
import com.ninesky.classtao.score.vo.*;
import com.ninesky.classtao.studentLeave.service.StudentLeaveService;
import com.ninesky.classtao.studentLeave.vo.StudentLeaveVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.framework.JedisDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.vo.BedroomVO;
import com.ninesky.classtao.school.vo.ClassVO;
import com.ninesky.classtao.score.service.ScoreTableService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.ListUtil;
import com.ninesky.common.util.RedisKeyUtil;
import com.ninesky.common.util.StringUtil;

@Service("scoreTableServiceImpl")
public class ScoreTableServiceImpl implements ScoreTableService{

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private JedisDAO jedisDao;

	@Autowired
	private DictService dictService;

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private StudentLeaveService studentLeaveService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private ScoreService scoreService;
	
	@Override
	public List<TableHeadVO> getTableHead(TableHeadVO vo) {
		return dao.queryForList("tableMap.getTableList", vo);
	}
	
	

	/**
	 * 添加记录到Redis数据库
	 * @param vo 
	 */
	public void addScoreSumToRedis(List<ScoreListVO> itemlist, ScoreVO vo) {
		Date now = new Date();
		String day = DateUtil.getNow("yyyyMMdd");
		String week = DateUtil.getFirstDayOfWeek(now);
		String month = DateUtil.getFirstDayOfMonth(now);
		String term = DateUtil.getYearAndTerm(now);
		String year = DateUtil.getFirstDayOfYear(day);
		
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			String teamKey = RedisKeyUtil.getScoreTeamKeyAttend(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id(),vo.getAttend_item());
			jedisDao.hincrBy(teamKey+":DAY"+day, "total_day", 1);
			jedisDao.hincrBy(teamKey+":WEEK"+week, "total_day", 1);
			jedisDao.hincrBy(teamKey+":MONTH"+month, "total_day", 1);
			jedisDao.hincrBy(teamKey+":TERM"+term, "total_day", 1);
			jedisDao.hincrBy(teamKey+":YEAR"+year, "total_day", 1);
			
			for (ScoreListVO avo: itemlist) {
				if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
				addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code(),1);
				if (StringUtil.isNotEmpty(avo.getScore_code_old()))
				addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code_old(),-1);
			}
		} else {
			for (ScoreListVO avo: itemlist) {
				if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
				//学校ID+team_type+TeamID+班级ID+学生ID+扣分编码
				if (avo.getScore_old()==null) avo.setScore_old(0);
				Integer score = avo.getScore()*avo.getCount()-avo.getScore_old();
				//个人
				String personKey = RedisKeyUtil.getStudentKey(vo.getSchool_id(),avo.getStudent_id());
				jedisDao.hincrBy(personKey+":DAY"+day, avo.getScore_code(), score);
				jedisDao.hincrBy(personKey+":WEEK"+week, avo.getScore_code(), score);
				jedisDao.hincrBy(personKey+":MONTH"+month, avo.getScore_code(), score);
				jedisDao.hincrBy(personKey+":TERM"+term, avo.getScore_code(), score);
				jedisDao.hincrBy(personKey+":YEAR"+year, avo.getScore_code(), score);
				
				//班级
				String teamKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id());
				jedisDao.hincrBy(teamKey+":DAY"+day, avo.getScore_code(), score);
				jedisDao.hincrBy(teamKey+":WEEK"+week, avo.getScore_code(), score);
				jedisDao.hincrBy(teamKey+":MONTH"+month, avo.getScore_code(), score);
				jedisDao.hincrBy(teamKey+":TERM"+term, avo.getScore_code(), score);
				jedisDao.hincrBy(teamKey+":YEAR"+year, avo.getScore_code(), score);
				
				//年级
				String groupKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),0);
				jedisDao.hincrBy(groupKey+":DAY"+day, avo.getScore_code(), score);
				jedisDao.hincrBy(groupKey+":WEEK"+week, avo.getScore_code(), score);
				jedisDao.hincrBy(groupKey+":MONTH"+month, avo.getScore_code(), score);
				jedisDao.hincrBy(groupKey+":TERM"+term, avo.getScore_code(), score);
				jedisDao.hincrBy(groupKey+":YEAR"+year, avo.getScore_code(), score);
			}
		}
		
	}



	private void addAttemItemSumToRedis(ScoreVO vo, String day, String week,String month, String term,String year,Integer student_id,String score_code,Integer score) {
		String attendPersonKey = RedisKeyUtil.getScoreStudentKeyAttend(ActionUtil.getSchoolID(),student_id,vo.getAttend_item());
		jedisDao.hincrBy(attendPersonKey+":DAY"+day, score_code, score);
		jedisDao.hincrBy(attendPersonKey+":WEEK"+week, score_code, score);
		jedisDao.hincrBy(attendPersonKey+":MONTH"+month, score_code, score);
		jedisDao.hincrBy(attendPersonKey+":TERM"+term, score_code, score);
		jedisDao.hincrBy(attendPersonKey+":YEAR"+year, score_code, score);
		
		//班级
		String attendTeamKey =  RedisKeyUtil.getScoreTeamKeyAttend(ActionUtil.getSchoolID(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id(),vo.getAttend_item());
		jedisDao.hincrBy(attendTeamKey+":DAY"+day, score_code, score);
		jedisDao.hincrBy(attendTeamKey+":WEEK"+week, score_code, score);
		jedisDao.hincrBy(attendTeamKey+":MONTH"+month, score_code, score);
		jedisDao.hincrBy(attendTeamKey+":TERM"+term, score_code, score);
		jedisDao.hincrBy(attendTeamKey+":YEAR"+year, score_code, score);
	}
	
	
	
	/**
	 * 添加记录到Redis数据库
	 * @param vo 
	 */
	public List<HashMap<String,Object>> getScoreCountFromRedis(TableVO vo) {
		List<HashMap<String,Object>> list;
		List<TableHeadVO> field_list = BeanUtil.jsonToList(vo.getField_list(), TableHeadVO.class);
		if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())){
			list = getListFromBedroom(vo);
			vo.setGroup_id(0);
		} else list = getListFromClassroom(vo);
		String key = getScoreDateKey(vo);
		HashMap<String,Object> sumMap = new HashMap<String,Object>();
		if  (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {
				String teamKey = RedisKeyUtil.getScoreTeamKeyAttend(ActionUtil.getSchoolID(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id(),vo.getAttend_item());
				sumMap.put("total_day", jedisDao.hget(teamKey+key, "total_day"));
			}
			for (HashMap<String,Object> stuMap: list) {
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) 
					stuMap.putAll(sumMap);
				else {
					String teamKey = RedisKeyUtil.getScoreTeamKeyAttend(ActionUtil.getSchoolID(),vo.getTeam_type(),vo.getScore_type(),Integer.parseInt(stuMap.get("group_id").toString()),Integer.parseInt(stuMap.get("team_id").toString()),vo.getAttend_item());
					stuMap.put("total_day", jedisDao.hget(teamKey+key, "total_day"));	
				}
				String queryKey = "";
				//查找班级或者寝室学生分数汇总
				if (IntegerUtil.isNotEmpty(vo.getTeam_id()))
					queryKey += RedisKeyUtil.getStudentKey(ActionUtil.getSchoolID(), (Integer) stuMap.get("student_id"));
				else queryKey += RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(),vo.getScore_type(), Integer.parseInt(stuMap.get("group_id").toString()),Integer.parseInt(stuMap.get("team_id").toString()));
				queryKey += ":ATTEND_ITEM" + vo.getAttend_item();
				queryKey += key;
				Map<String,String> dataMap = jedisDao.hgetAll(queryKey);
				stuMap.putAll(dataMap);
				for (TableHeadVO field:field_list) 
				{
					if (StringUtil.isEmpty(field.getField_func())) continue;
					stuMap.put(field.getField(), getFunctionValue(field.getField_func(),stuMap));
				}
			}
		} else {
			for (HashMap<String,Object> stuMap: list) {
				String queryKey = "";
				//查找班级或者寝室学生分数汇总
				//if (IntegerUtil.isNotEmpty(vo.getTeam_id()))
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type()))
					queryKey += RedisKeyUtil.getStudentKey(ActionUtil.getSchoolID(), Integer.parseInt(stuMap.get("student_id").toString()));
				else queryKey += RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(),vo.getScore_type(), Integer.parseInt(stuMap.get("group_id").toString()),Integer.parseInt(stuMap.get("team_id").toString()));
				queryKey += key;
				Map<String,String> dataMap = jedisDao.hgetAll(queryKey);
				stuMap.putAll(dataMap);
				for (TableHeadVO field:field_list) 
				{
					if (StringUtil.isEmpty(field.getField_func())) continue;
					stuMap.put(field.getField(), getFunctionValue(field.getField_func(),stuMap));
				}
			}	
		}
		return list;
	}

	//自定义统计
	public List<HashMap<String,Object>> getScoreCountOfCustom(TableVO vo){
		List<HashMap<String,Object>> list;
		List<TableHeadVO> field_list = BeanUtil.jsonToList(vo.getField_list(), TableHeadVO.class);
		if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())){
			list = getListFromBedroom(vo);
			vo.setGroup_id(0);
		} else list = getListFromClassroom(vo);
		HashMap<String,Object> sumMap = new HashMap<String,Object>();
		if  (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())) {
				int total_day=dao.queryObject("scoreListMap.getStudentTotalDay",vo);
				sumMap.put("total_day", total_day);
			}
			for (HashMap<String,Object> stuMap: list) {
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type()))
					stuMap.putAll(sumMap);
				else {
					vo.setGroup_id(Integer.parseInt(stuMap.get("group_id").toString()));
					vo.setTeam_id(Integer.parseInt(stuMap.get("team_id").toString()));
					int total_day=dao.queryObject("scoreListMap.getStudentTotalDay",vo);
					stuMap.put("total_day", total_day);
				}
				//查找班级或者寝室学生分数汇总
				if (IntegerUtil.isNotEmpty(vo.getTeam_id())){
					vo.setStudent_id((Integer)stuMap.get("student_id"));
					List<ScoreListVO> scoreList=dao.queryForList("scoreListMap.getStudentCount",vo);
					for (ScoreListVO score:scoreList) {
						stuMap.put(score.getScore_code(),score.getCount());
					}
				} else {
					vo.setGroup_id(Integer.parseInt(stuMap.get("group_id").toString()));
					vo.setTeam_id(Integer.parseInt(stuMap.get("team_id").toString()));
					List<ScoreListVO> scoreList=dao.queryForList("scoreListMap.getStudentCount",vo);
					for (ScoreListVO score:scoreList) {
						stuMap.put(score.getScore_code(),score.getCount());
					}
				}
				for (TableHeadVO field:field_list) {
					if (StringUtil.isEmpty(field.getField_func())) continue;
					stuMap.put(field.getField(), getFunctionValue(field.getField_func(),stuMap));
				}
			}
		} else {
			for (HashMap<String,Object> stuMap: list) {
				if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type())){
					vo.setStudent_id((Integer)stuMap.get("student_id"));
					List<ScoreListVO> scoreList=dao.queryForList("scoreListMap.getStudentCount",vo);
					for (ScoreListVO score:scoreList){
						stuMap.put(score.getScore_code(),score.getScore());
					}
				} else {
					vo.setGroup_id(Integer.parseInt(stuMap.get("group_id").toString()));
					vo.setTeam_id(Integer.parseInt(stuMap.get("team_id").toString()));
					List<ScoreListVO> scoreList=dao.queryForList("scoreListMap.getStudentCount",vo);
					for (ScoreListVO score:scoreList){
						stuMap.put(score.getScore_code(),score.getScore());
					}
				}
				for (TableHeadVO field:field_list) {
					if (StringUtil.isEmpty(field.getField_func())) continue;
					stuMap.put(field.getField(), getFunctionValue(field.getField_func(),stuMap));
				}
			}
		}
		return list;
	}

	public List<HashMap<String,Object>> getStudentLeaveCount(TableVO vo){
		List<HashMap<String,Object>> list =new ArrayList<HashMap<String,Object>>();
		StudentLeaveVO leave=new StudentLeaveVO();
		leave.setSchool_id(ActionUtil.getSchoolID());
		leave.setStart_date(vo.getStart_date());
		leave.setEnd_date(vo.getEnd_date());
		leave.setGroup_id(vo.getGroup_id());
		leave.setTeam_id(vo.getTeam_id());
		List<StudentLeaveVO> leaveVOList=studentLeaveService.getStudentLeaveCount(leave);
		List<Integer> idList=new ArrayList<Integer>();
		HashMap<Integer,Object> map=new HashMap<Integer, Object>();
		for (StudentLeaveVO studentLeaveVO:leaveVOList){
			if (map.containsKey(studentLeaveVO.getStudent_id())) continue;
			map.put(studentLeaveVO.getStudent_id(),studentLeaveVO);
			idList.add(studentLeaveVO.getStudent_id());
		}
		for (int student_id:idList) {
			HashMap<String,Object> stuMap=new HashMap<String,Object>();
			for (StudentLeaveVO studentLeaveVO:leaveVOList) {
				if (student_id==studentLeaveVO.getStudent_id()) {
					stuMap.put(studentLeaveVO.getLeave_type(),studentLeaveVO.getDays());
				}
			}
			ActionUtil.setPage_app(false);      //内部代码list不分页
			ActionUtil.setPage_web(false);
			leave.setStudent_id(student_id);
			List<StudentLeaveVO> leaveList=studentLeaveService.getStudentLeaveCountDetail(leave);
			stuMap.put("count",leaveList.size());
			stuMap.put("student_name",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,student_id));
			list.add(stuMap);
		}
		return list;
	}

	public List<HashMap<String,Object>> getTeacherLeaveCount(TableVO vo,List<DictVO> dictList){
		List<HashMap<String,Object>> list =new ArrayList<HashMap<String,Object>>();
		LeaveVO leaveVO=new LeaveVO();
		leaveVO.setSchool_id(ActionUtil.getSchoolID());
		Date end = DateUtil.formatDateEnd(DateUtil.formatStringToDate(vo.getEnd_date(),"yyyy-MM-dd"));
		Date start = DateUtil.formatDateStart(DateUtil.formatStringToDate(vo.getStart_date(),"yyyy-MM-dd"));
		leaveVO.setEnd_date(DateUtil.formatDateToString(end,"yyyy-MM-dd HH:mm:ss"));
		leaveVO.setStart_date(DateUtil.formatDateToString(start,"yyyy-MM-dd HH:mm:ss"));
		leaveVO.setLeave_status(DictConstants.LEAVE_STATUS_PASS);
		List<LeaveCountVO> countList=leaveService.getLeaveListOfSomeDay(leaveVO);
		for (LeaveCountVO count:countList) {
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("teacher_name",count.getLeave_name());
			map.put("count",count.getLeave_counts());
			map.put("day",count.getLeave_days_sum()+"天"+count.getLeave_hours_sum()+"小时");
			for (DictVO dictVO:dictList) {
				leaveVO.setLeave_type(dictVO.getDict_code());
				leaveVO.setCreate_by(count.getUser_id());
				List<LeaveCountVO> countList1=leaveService.getLeaveListOfSomeDay(leaveVO);
				if (ListUtil.isNotEmpty(countList1))
					map.put(dictVO.getDict_code(),countList1.get(0).getLeave_days_sum()+"天"+countList1.get(0).getLeave_hours_sum()+"小时");
			}
			list.add(map);
		}
		return list;
	}

	public List<HashMap<String,Object>> getPersonScoreCount(TableVO vo){
		List<HashMap<String,Object>> list =new ArrayList<HashMap<String,Object>>();
		ScoreCountVO countVO=new ScoreCountVO();
		countVO.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		countVO.setScore_type(vo.getScore_type());
		countVO.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		countVO.setGroup_id(vo.getGroup_id());
		countVO.setTeam_id(vo.getTeam_id());
		countVO.setModule_code(vo.getModule_code());
		countVO.setStart_date(vo.getStart_date());
		countVO.setEnd_date(vo.getEnd_date());
		Map<String,Object> map=scoreService.getScoreCount(countVO);
		List<ScoreVO> slist=BeanUtil.jsonToList(map.get("ascDetail").toString(),ScoreVO.class);
		for (ScoreVO score:slist) {
			HashMap<String,Object> smap=new HashMap<String,Object>();
			smap.put("student_code",score.getStudent_code());
			smap.put("student_name",score.getStudent_name());
			smap.put("total",score.getScore());
			ScoreCountVO scoreCount=new ScoreCountVO();
			scoreCount.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			scoreCount.setScore_type(vo.getScore_type());
			scoreCount.setStudent_id(score.getStudent_id());
			scoreCount.setModule_code(vo.getModule_code());
			scoreCount.setStart_date(vo.getStart_date());
			scoreCount.setEnd_date(vo.getEnd_date());
			Map<String,Object> dmap=scoreService.getScoreDetail(scoreCount);
			List<DashBoardVO> dlist=BeanUtil.jsonToList(dmap.get("codeDetail").toString(),DashBoardVO.class);
			for (DashBoardVO dash:dlist) {
				smap.put(dash.getName(),dash.getInfo());
			}
			list.add(smap);
		}
		return list;
	}

	//班级评分
	public List<HashMap<String,Object>> getClassScoreCount(TableVO vo){
		List<HashMap<String,Object>> list =new ArrayList<HashMap<String,Object>>();
		ScoreCountVO countVO=new ScoreCountVO();
		countVO.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		countVO.setScore_type(vo.getScore_type());
		countVO.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		countVO.setGroup_id(vo.getGroup_id());
		countVO.setTeam_id(vo.getTeam_id());
		countVO.setModule_code(vo.getModule_code());
		countVO.setStart_date(vo.getStart_date());
		countVO.setEnd_date(vo.getEnd_date());
		Map<String,Object> map=scoreService.getScoreCount(countVO);
		List<ScoreVO> slist=BeanUtil.jsonToList(map.get("ascDetail").toString(),ScoreVO.class);
		for (ScoreVO score:slist) {
			HashMap<String,Object> smap=new HashMap<String,Object>();
			smap.put("team_name",score.getTeam_name());
			smap.put("total",score.getScore());
			Integer count=dao.queryObject("studentMap.getStudentCount",score.getTeam_id());
			smap.put("count",count);
			ScoreCountVO scoreCount=new ScoreCountVO();
			scoreCount.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			scoreCount.setScore_type(vo.getScore_type());
			scoreCount.setModule_code(vo.getModule_code());
			scoreCount.setStart_date(vo.getStart_date());
			scoreCount.setEnd_date(vo.getEnd_date());
			scoreCount.setGroup_id(score.getGroup_id());
			scoreCount.setTeam_id(score.getTeam_id());
			Map<String,Object> dmap=scoreService.getScoreDetail(scoreCount);
			List<DashBoardVO> dlist=BeanUtil.jsonToList(dmap.get("codeDetail").toString(),DashBoardVO.class);
			for (DashBoardVO dash:dlist) {
				smap.put(dash.getName(),dash.getInfo());
			}
			list.add(smap);
		}
		return list;
	}

	/**
	 * 生成打分日期的Key
	 * @param vo
	 * @return
	 */
	private String getScoreDateKey(TableVO vo) {
		String key;
		if (StringUtil.isEmpty(vo.getScore_date())) 
			vo.setScore_date(DateUtil.getNow(DateUtil.YMD));
		switch (vo.getSum_type()) {
			case DictConstants.SUM_TYPE_DAY:
				key = ":DAY"+ vo.getScore_date().replaceAll("-", "");	
				break;
			case DictConstants.SUM_TYPE_WEEK:
				key = ":WEEK"+ DateUtil.getFirstDayOfWeek(vo.getScore_date());	
				break;
			case DictConstants.SUM_TYPE_MONTH:
				key = ":MONTH"+ DateUtil.getFirstDayOfMonth(DateUtil.smartFormat(vo.getScore_date()));	
				break;
			case DictConstants.SUM_TYPE_TERM:
				key = ":TERM"+ DateUtil.getYearAndTerm(DateUtil.smartFormat(vo.getScore_date()));
				break;
			case DictConstants.SUM_TYPE_YEAR:
				key = ":YEAR"+ DateUtil.getFirstDayOfYear(DateUtil.smartFormat(vo.getScore_date()));
				break;
			default: key = "";
		}
		return key;
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


	/**
	 * 函数：当前只支持简单的函数，加减乘除
	 * @param field_func
	 * @param dataMap
	 * @return
	 */
	private Object getFunctionValue(String field_func,HashMap<String, Object> dataMap) {
		String mathString = new String(field_func);
		int preIndex=0;
		for (int i=0;i<field_func.length();i++) {
			String a = field_func.substring(i, i+1);
			if ("{".equals(a)) preIndex = i;
			if ("}".equals(a)) {
				String field = field_func.substring(preIndex+1,i);
				mathString = mathString.replaceAll(field, (dataMap.containsKey(field)&&dataMap.get(field)!=null)?dataMap.get(field).toString():"0");
			}
		}
		mathString = mathString.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("%2B", "+");
		return StringUtil.mathByString(mathString);
	}



	private List<HashMap<String,Object>> getListFromBedroom(TableVO vo) {
		if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type()))
			return dao.queryForList("tableMap.getStudentListByBedID",vo.getTeam_id());
		List<HashMap<String,Object>> list =	dao.queryForList("tableMap.getBedListBySchoolID",ActionUtil.getSchoolID());
		return list;
	}


	private List<HashMap<String,Object>> getListFromClassroom(TableVO vo) {
		if (DictConstants.COUNT_TYPE_STUDENT.equals(vo.getCount_type()))
			return dao.queryForList("tableMap.getStudentListByClassID",vo.getTeam_id());
		List<HashMap<String,Object>> list =	dao.queryForList("tableMap.getClassListBySchoolID",vo);
		for (HashMap<String,Object> map:list) 
			//map.put("team_name", TeamNameUtil.getClassname(school.getSchool_type(), (Integer) map.get("enrollment_year"),(Integer) map.get("class_num")));
			map.put("team_name", redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,(Integer)map.get("group_id"),(Integer) map.get("team_id")));
		return list;
	}
	

	//删除主表扣分记录时，修改redis中数据
	public void deleteScoreToRedis(List<ScoreListVO> itemlist, ScoreVO vo) {
		Date now = DateUtil.dateTimeFormat(vo.getScore_date()+" 00:00:00");
		String day = vo.getScore_date().replace("-", "");
		String week = DateUtil.getFirstDayOfWeek(now);//本周一
		String month = day.substring(0, 6)+"01";
		String term = DateUtil.getYearAndTerm(now);
		String year = day.substring(0, 4)+"0101";;//当前年1号：20160101		
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			String teamKey = RedisKeyUtil.getScoreTeamKeyAttend(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id(),vo.getAttend_item());
			jedisDao.hincrBy(teamKey+":DAY"+day, "total_day", -1);
			jedisDao.hincrBy(teamKey+":WEEK"+week, "total_day", -1);
			jedisDao.hincrBy(teamKey+":MONTH"+month, "total_day", -1);
			jedisDao.hincrBy(teamKey+":TERM"+term, "total_day", -1);
			jedisDao.hincrBy(teamKey+":YEAR"+year, "total_day", -1);			
			for (ScoreListVO avo: itemlist)	{
				addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code(),-1);
			}
		} else {
			for (ScoreListVO avo: itemlist)	{
				if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
				//学校ID+team_type+TeamID+班级ID+学生ID+扣分编码
				//个人
				int score=avo.getScore()*avo.getCount();//可多个
				String personKey = RedisKeyUtil.getStudentKey(vo.getSchool_id(),avo.getStudent_id());
				jedisDao.hincrBy(personKey+":DAY"+day, avo.getScore_code(), -score);
				jedisDao.hincrBy(personKey+":WEEK"+week, avo.getScore_code(), -score);
				jedisDao.hincrBy(personKey+":MONTH"+month, avo.getScore_code(), -score);
				jedisDao.hincrBy(personKey+":TERM"+term, avo.getScore_code(), -score);
				jedisDao.hincrBy(personKey+":YEAR"+year, avo.getScore_code(), -score);
				//班级
				String teamKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id());
				jedisDao.hincrBy(teamKey+":DAY"+day, avo.getScore_code(), -score);
				jedisDao.hincrBy(teamKey+":WEEK"+week, avo.getScore_code(), -score);
				jedisDao.hincrBy(teamKey+":MONTH"+month, avo.getScore_code(), -score);
				jedisDao.hincrBy(teamKey+":TERM"+term, avo.getScore_code(), -score);
				jedisDao.hincrBy(teamKey+":YEAR"+year, avo.getScore_code(), -score);				
				//年级
				String groupKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),0);
				jedisDao.hincrBy(groupKey+":DAY"+day, avo.getScore_code(), -score);
				jedisDao.hincrBy(groupKey+":WEEK"+week, avo.getScore_code(), -score);
				jedisDao.hincrBy(groupKey+":MONTH"+month, avo.getScore_code(), -score);
				jedisDao.hincrBy(groupKey+":TERM"+term, avo.getScore_code(), -score);
				jedisDao.hincrBy(groupKey+":YEAR"+year, avo.getScore_code(), -score);
			}
		}
	}

	//修改扣分后，修改redis中数据
	public void updateScoreToRedis(List<ScoreListVO> itemlist, ScoreVO vo) {
		ScoreVO svo=dao.queryObject("scoreMap.getScoreList", vo.getScore_id());
		Date now = DateUtil.dateTimeFormat(svo.getScore_date()+" 00:00:00");
		String day=svo.getScore_date().replace("-", "");
		String week = DateUtil.getFirstDayOfWeek(now);
		String month = DateUtil.getFirstDayOfMonth(now);
		String term = DateUtil.getYearAndTerm(now);
		String year = DateUtil.getFirstDayOfYear(day);
		
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			for (ScoreListVO avo: itemlist) {
				if (StringUtil.isEmpty(avo.getScore_code())) {
					addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code_old(),-1);
				} else {
				addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code(),1);
				if (StringUtil.isNotEmpty(avo.getScore_code_old()))
				addAttemItemSumToRedis(vo, day, week, month,term,year,avo.getStudent_id(), avo.getScore_code_old(),-1);
				}
			}
		} else {
			for (ScoreListVO avo: itemlist) {
				//学校ID+team_type+TeamID+班级ID+学生ID+扣分编码
				String personKey = RedisKeyUtil.getStudentKey(vo.getSchool_id(),avo.getStudent_id());
				String teamKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),vo.getTeam_id());
				String groupKey = RedisKeyUtil.getScoreTeamKey(vo.getSchool_id(),vo.getTeam_type(),vo.getScore_type(),vo.getGroup_id(),0);
				if (StringUtil.isNotEmpty(avo.getScore_code())) {
					if (IntegerUtil.isEmpty(avo.getCount())) avo.setCount(1);//默认一个
					Integer score = avo.getScore() * avo.getCount();//可多个
					//个人
					jedisDao.hincrBy(personKey + ":DAY" + day, avo.getScore_code(), score);
					jedisDao.hincrBy(personKey + ":WEEK" + week, avo.getScore_code(), score);
					jedisDao.hincrBy(personKey + ":MONTH" + month, avo.getScore_code(), score);
					jedisDao.hincrBy(personKey + ":TERM" + term, avo.getScore_code(), score);
					jedisDao.hincrBy(personKey + ":YEAR" + year, avo.getScore_code(), score);
					//班级
					jedisDao.hincrBy(teamKey + ":DAY" + day, avo.getScore_code(), score);
					jedisDao.hincrBy(teamKey + ":WEEK" + week, avo.getScore_code(), score);
					jedisDao.hincrBy(teamKey + ":MONTH" + month, avo.getScore_code(), score);
					jedisDao.hincrBy(teamKey + ":TERM" + term, avo.getScore_code(), score);
					jedisDao.hincrBy(teamKey + ":YEAR" + year, avo.getScore_code(), score);
					//年级
					jedisDao.hincrBy(groupKey + ":DAY" + day, avo.getScore_code(), score);
					jedisDao.hincrBy(groupKey + ":WEEK" + week, avo.getScore_code(), score);
					jedisDao.hincrBy(groupKey + ":MONTH" + month, avo.getScore_code(), score);
					jedisDao.hincrBy(groupKey + ":TERM" + term, avo.getScore_code(), score);
					jedisDao.hincrBy(groupKey + ":YEAR" + year, avo.getScore_code(), score);
				}
				if (StringUtil.isNotEmpty(avo.getScore_code_old())) {
					Integer score_old = avo.getScore_old();
					//个人
					jedisDao.hincrBy(personKey+":DAY"+day, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(personKey+":WEEK"+week, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(personKey+":MONTH"+month, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(personKey+":TERM"+term, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(personKey+":YEAR"+year, avo.getScore_code_old(), -score_old);
					
					//班级
					jedisDao.hincrBy(teamKey+":DAY"+day, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(teamKey+":WEEK"+week, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(teamKey+":MONTH"+month, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(teamKey+":TERM"+term, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(teamKey+":YEAR"+year, avo.getScore_code_old(), -score_old);
					
					//年级
					jedisDao.hincrBy(groupKey+":DAY"+day, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(groupKey+":WEEK"+week, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(groupKey+":MONTH"+month, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(groupKey+":TERM"+term, avo.getScore_code_old(), -score_old);
					jedisDao.hincrBy(groupKey+":YEAR"+year, avo.getScore_code_old(), -score_old);
				}
			}
		}
	}
	
	/**
	 * 以下是仪表盘的相关代码   
	 */
	
	//获取考勤出勤率
	private String getAttendRate(TableVO vo) {
		String dateKey=getScoreDateKey(vo);
		String date=getScoreDate(vo);
		String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
				vo.getGroup_id(), vo.getTeam_id());
		String field_func="({total_day}*{team_count}-{count})*100/({total_day}*{team_count})%";//考勤率计算函数
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
			queryKey+=":ATTEND_ITEM"+vo.getAttend_item();
		else queryKey+=":ATTEND_ITEM";
		queryKey+=dateKey;
		Integer count=0;
		Integer total_day=0;
		Integer team_count=0;
		List<CountInfoVO> clist=new ArrayList<CountInfoVO>();//存放考勤明细（几人未到，几人迟到，几人请假）
		if (IntegerUtil.isNotEmpty(vo.getTeam_id())){//计算具体班级的考勤率
			String totalday=jedisDao.hget(queryKey, "total_day");
			if (StringUtil.isEmpty(totalday)) return null;
			total_day=Integer.parseInt(totalday.trim());//考勤次数
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
				team_count=dao.queryObject("redisMap.getStudentCountByClass", vo.getTeam_id());
			else{
				ReceiveVO rvo=new ReceiveVO();
				rvo.setTeam_id(vo.getTeam_id());
				team_count=dao.queryObject("redisMap.getBedRoomCountByTeam", rvo);
			}
			count=getAttendCount(vo, queryKey, count,clist);//获取未准时到达的次数（013041...）
		} else if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())){
			String pattern="";
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
				pattern="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
					":SCORE_TYPE"+vo.getScore_type()+":*ATTEND_ITEM"+vo.getAttend_item()+dateKey;
			else pattern="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
					":SCORE_TYPE"+vo.getScore_type()+":*ATTEND_ITEM"+dateKey;
			Set<String> set=jedisDao.keys(pattern);//所有符合条件的记录
			for (String key:set) {
				total_day+=Integer.parseInt(jedisDao.hget(key, "total_day").trim());
				count=getAttendCount(vo,key,count,clist);//未实到的人数
			}
			vo.setScore_date(date);
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {
				team_count=dao.queryObject("scoreMap.getStudentCountOfClass", vo);//打过分的班级人数总和
			} else {
				team_count=dao.queryObject("scoreMap.getStudentCountOfBed", vo);//打过分的寝室人数总和
			}
		}
		HashMap<String,Object> smap=new HashMap<String,Object>();
		smap.put("total_day", total_day);//考勤次数
		smap.put("team_count", team_count);
		smap.put("count", count);
		return getFunctionValue(field_func,smap).toString();//计算函数
	}
	
	//获取考勤明细(迟到，未到，请假的人数)
	public String getAttenddetails(TableVO vo) {
		String dateKey=getScoreDateKey(vo);
		String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
				vo.getGroup_id(), vo.getTeam_id());
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))//教室考勤时才有晨检和晚自习之分
			queryKey+=":ATTEND_ITEM"+vo.getAttend_item();
		else queryKey+=":ATTEND_ITEM";
		queryKey+=dateKey;
		Integer count=0;
		Integer total_day=0;
		List<CountInfoVO> clist=new ArrayList<CountInfoVO>();//存放考勤明细（几人未到，几人迟到，几人请假）
		if (IntegerUtil.isNotEmpty(vo.getGroup_id()) && IntegerUtil.isNotEmpty(vo.getTeam_id())){//计算具体班级的考勤率
			String totalday=jedisDao.hget(queryKey, "total_day");
			if (StringUtil.isEmpty(totalday)) return null;
			getAttendCount(vo, queryKey, count,clist);//获取未准时到达的次数（013041...）
		} else if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())){
			String pattern="";
			String pattern1="";
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {
				pattern = "SCORE:SCHOOL_ID" + ActionUtil.getSchoolID() + ":TEAM_TYPE" + vo.getTeam_type() +
						":SCORE_TYPE" + vo.getScore_type() + ":*ATTEND_ITEM" + vo.getAttend_item() + dateKey;
				pattern1 = "SCORE:SCHOOL_ID" + ActionUtil.getSchoolID() + ":TEAM_TYPE" + vo.getTeam_type() +
						":SCORE_TYPE" + vo.getScore_type() + ":*TEAM_ID0:ATTEND_ITEM" + vo.getAttend_item() + dateKey;
			}else {
				pattern = "SCORE:SCHOOL_ID" + ActionUtil.getSchoolID() + ":TEAM_TYPE" + vo.getTeam_type() +
						":SCORE_TYPE" + vo.getScore_type() + ":*ATTEND_ITEM" + dateKey;
				pattern1 = "SCORE:SCHOOL_ID" + ActionUtil.getSchoolID() + ":TEAM_TYPE" + vo.getTeam_type() +
						":SCORE_TYPE" + vo.getScore_type() + ":*TEAM_ID0:ATTEND_ITEM" + dateKey;
			}
			Set<String> set=jedisDao.keys(pattern);//所有符合条件的记录
			Set<String> gradeSet=jedisDao.keys(pattern1);
			for (String key:set) {
				if (gradeSet.contains(key)) continue;
				if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {
					int i = key.indexOf("GROUP_ID");
					int j = key.indexOf(":TEAM_ID");
					String group_id = key.substring(i + 8, j);
					GradeVO grade = dao.queryObject("gradeMap.getGradeListByID", Integer.parseInt(group_id.trim()));
					if (grade.getIs_graduate() == 1) continue;//已经毕业的班级
				}
				total_day+=Integer.parseInt(jedisDao.hget(key, "total_day").trim());
				getAttendCount(vo,key,count,clist);//获取明细
			}
		}
		return BeanUtil.ListTojson(clist).toString();//计算函数
	}

	//获取扣分总分（教师卫生，纪律等）(用于排序),单个班级
	public Integer getSumScore(TableVO vo){
		String dateKey=getScoreDateKey(vo);
		String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
				vo.getGroup_id(), vo.getTeam_id());
		queryKey+=dateKey;
		Integer count=0;//获取总分
		if (IntegerUtil.isNotEmpty(vo.getTeam_id())){
			ScoreReasonVO rvo=new ScoreReasonVO();
			rvo.setSchool_id(ActionUtil.getSchoolID());
			rvo.setTeam_type(vo.getTeam_type());
			rvo.setScore_type(vo.getScore_type());
			List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", rvo);//扣分项
			for (ScoreReasonVO srvo:list) {
				String score=jedisDao.hget(queryKey, srvo.getScore_code());//获取具体打分项的个数(分数)
				if (StringUtil.isNotEmpty(score)) 
					count+=Integer.parseInt(score.trim());//合计
			}
		}
		return count;
	}
	
	//获取未准时到达的次数（考勤）(明细)
	private Integer getAttendCount(TableVO vo, String queryKey, Integer count,List<CountInfoVO> clist) {
		ScoreReasonVO rvo=new ScoreReasonVO();
		rvo.setSchool_id(ActionUtil.getSchoolID());
		rvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		rvo.setScore_type(vo.getScore_type());
		List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", rvo);//扣分项
		for (ScoreReasonVO srvo:list) {
			if (DictConstants.ATTEND_STATUS_ARRIVE.equals(srvo.getScore_code())) continue;
			String score=jedisDao.hget(queryKey, srvo.getScore_code());//获取具体打分项的个数(分数)
			if (StringUtil.isEmpty(score)) continue;
			CountInfoVO cvo=new CountInfoVO();
			cvo.setCount(Integer.parseInt(score.trim()));//次数
			cvo.setScore_code(srvo.getScore_reason());
			clist.add(cvo);
			count=count+Integer.parseInt(score.trim());//合计
		}
		return count;
	}
	
	//向redis中存放出勤率或总分，用于计算排名
	public void addRankToRedis(TableVO vo) {
		//排名key
		String rankKey=RedisKeyUtil.getRankKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type());
		String dateKey=getScoreDateKey(vo);
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))//教室
				rankKey+=":ATTEND_ITEM"+vo.getAttend_item();
			else rankKey+=":ATTEND_ITEM";
			rankKey+=dateKey;
			String rate=getAttendRate(vo);//考勤率
			if (StringUtil.isEmpty(rate)) return;
			Double drate=Double.parseDouble(rate.replaceAll("%", "").trim());
			jedisDao.zadd(rankKey, drate, vo.getGroup_id()+":"+vo.getTeam_id());//把出勤率存放在redis中
		} else {
			rankKey+=dateKey;
			String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
					vo.getGroup_id(), vo.getTeam_id());
			queryKey+=dateKey;
			if (!jedisDao.exists(queryKey)) return;//这得加个判断，可以判断key在不在
			Integer count=getSumScore(vo);
			jedisDao.zadd(rankKey, count, vo.getGroup_id()+":"+vo.getTeam_id());//将总扣分存放在redis中
		}
	}
		
	//获取班级出勤率或扣分总分的排名
	public List<DashBoardVO> getRank(TableVO vo ,int i,int start,int end){
		List<DashBoardVO> dlist=new ArrayList<DashBoardVO>();
		//排名key
		String rankKey=RedisKeyUtil.getRankKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type());
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && 
				DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type()))
			rankKey+=":ATTEND_ITEM"+vo.getAttend_item();
		else if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type()) && 
				DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type()))//寝室点到
			rankKey+=":ATTEND_ITEM";
		String dateKey=getScoreDateKey(vo);
		List<ClassVO> list=dao.queryForList("redisMap.getClassInfo", ActionUtil.getSchoolID());//全校所有班级
		List<BedroomVO> blist=dao.queryForList("redisMap.getBedRommInfo", ActionUtil.getSchoolID());
		jedisDao.del(rankKey+dateKey);//先清空，后添加
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())){
			for (ClassVO cvo:list) {//对所有班级都计算一遍
				vo.setGroup_id(cvo.getGrade_id());
				vo.setTeam_id(cvo.getClass_id());
				addRankToRedis(vo);//将总分存在redis中
			}
		} else {
			for (BedroomVO bvo:blist) {
				vo.setGroup_id(0);//寝室的group_id为0
				vo.setTeam_id(bvo.getBedroom_id());
				addRankToRedis(vo);//将总分存在redis中
			}
		}
		Set<String> set;
		if (i>0)
			set=jedisDao.zrange(rankKey+dateKey, start, end);//升序
		else
			set=jedisDao.zrevrange(rankKey+dateKey, start, end);
		ScoreReasonVO rvo=new ScoreReasonVO();
		rvo.setSchool_id(ActionUtil.getSchoolID());
		rvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		rvo.setScore_type(vo.getScore_type());
		List<ScoreReasonVO> slist=dao.queryForList("scoreReasonMap.getScoreReasonList", rvo);//扣分项
		for (String ss: set) {
			int ii=ss.indexOf(":");
			if (ii==-1) continue;
			DashBoardVO dbvo=new DashBoardVO();
			int group_id=Integer.parseInt(ss.substring(0, ss.indexOf(":")).trim());
			int team_id=Integer.parseInt(ss.substring(ss.indexOf(":")+1).trim());
			String team_name=redisService.getTeamName(vo.getTeam_type(), group_id, team_id);//团队名称
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){
				String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
						group_id, team_id);
				if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))//考勤分attend_item,寝室点到不分
					queryKey+=":ATTEND_ITEM"+vo.getAttend_item();
				else queryKey+=":ATTEND_ITEM";
				queryKey+=dateKey;
				List<CountInfoVO> clist=new ArrayList<CountInfoVO>();
				setAttendInfo(slist, queryKey, clist);//考勤扣分情况
				String score=BeanUtil.ListTojson(clist).toString();//考勤统计信息
				dbvo.setInfo(score);
			} else {
				String score=jedisDao.zscore(rankKey+dateKey, ss).intValue()+"";//扣分统计信息
				dbvo.setInfo(score);
			}
			dbvo.setName(team_name);
			dlist.add(dbvo);
		}
		return dlist;
	}

	//考勤扣分情况
	private void setAttendInfo(List<ScoreReasonVO> slist, String queryKey, List<CountInfoVO> clist) {
		for (ScoreReasonVO srvo:slist) {
            String score=jedisDao.hget(queryKey, srvo.getScore_code());//获取具体打分项的个数(分数)
            if (StringUtil.isEmpty(score)) continue;
            CountInfoVO cvo=new CountInfoVO();
            cvo.setCount(Integer.parseInt(score.trim()));//次数
            cvo.setScore_code(srvo.getScore_reason());
            clist.add(cvo);
        }
	}

	//获取学生扣分信息（带排序）
	public List<DashBoardVO> getStudentRank(TableVO vo ,int i,int start,int end){
		List<DashBoardVO> dlist=new ArrayList<DashBoardVO>();
		//排名key
		String rankKey=RedisKeyUtil.getRankKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type());
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type()))
			rankKey+=":ATTEND_ITEM"+vo.getAttend_item();
		rankKey+=":GROUP_ID"+vo.getGroup_id()+":TEAM_ID"+vo.getTeam_id();
		String dateKey=getScoreDateKey(vo);
		List<Integer> list=dao.queryForList("redisMap.getStudentByTeamID", vo.getTeam_id());//该班级所有学生
		ScoreReasonVO rvo=new ScoreReasonVO();
		rvo.setSchool_id(ActionUtil.getSchoolID());
		rvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		rvo.setScore_type(vo.getScore_type());
		List<ScoreReasonVO> slist=dao.queryForList("scoreReasonMap.getScoreReasonList", rvo);//扣分项
		for (Integer cvo:list) {//对所有学生都计算一遍
			String studentKey=RedisKeyUtil.getStudentKey(ActionUtil.getSchoolID(), cvo);
			Integer score=0;
			boolean flag=false;//用于判断该学生有无扣分
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){//统计未实到的个数
				studentKey+=":ATTEND_ITEM"+vo.getAttend_item();
				studentKey+=dateKey;
				if (jedisDao.exists(studentKey)){
					List<String> value=jedisDao.hvals(studentKey);
					for (String ss:value) {
						if (StringUtil.isNotEmpty(ss)) {
							score+=Integer.parseInt(ss.trim());
							flag=true;
						}
					}
				}
			} else {
				studentKey+=dateKey;
				if (jedisDao.exists(studentKey)){
					for (ScoreReasonVO srvo:slist) {//统计相应模块的扣分总分
						String ss=jedisDao.hget(studentKey, srvo.getScore_code());
						if (StringUtil.isNotEmpty(ss)) {
							score+=Integer.parseInt(ss.trim());
							flag=true;
						}
					}
				}
			}
			if (flag) jedisDao.zadd(rankKey+dateKey, score, cvo.toString());//有扣分，则添加，用于排序
		}
		Set<String> set;
		if (i>0)
			set=jedisDao.zrange(rankKey+dateKey, start, end);//升序
		else
			set=jedisDao.zrevrange(rankKey+dateKey, start, end);
		for (String ss: set) {
			if (StringUtil.isEmpty(ss)) continue;
			int student_id=Integer.parseInt(ss.trim());//学号
			DashBoardVO dbvo=new DashBoardVO();
			String student_name=redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,
					0, student_id);
			String studentkey=RedisKeyUtil.getStudentKey(ActionUtil.getSchoolID(), student_id);
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){//考勤
				studentkey+=":ATTEND_ITEM"+vo.getAttend_item();
				studentkey+=dateKey;
				List<CountInfoVO> clist=new ArrayList<CountInfoVO>();
				setAttendInfo(slist, studentkey, clist);//考勤扣分情况
				String score=BeanUtil.ListTojson(clist).toString();//考勤统计信息
				dbvo.setInfo(score);
			} else {
				studentkey+=dateKey;
				int sum=0;//总分
				for (ScoreReasonVO srvo:slist) {
					String score=jedisDao.hget(studentkey, srvo.getScore_code());//获取具体打分项的分数
					if (StringUtil.isEmpty(score)) continue;
					sum+=Integer.parseInt(score.trim());
				}
				String score=sum+"";
				dbvo.setInfo(score);
			}
			dbvo.setName(student_name);//学生姓名
			dlist.add(dbvo);
		}
		return dlist;
	}
	
	//获取班级最近扣分时间
	public long getLastTime(TableVO vo){
		ScoreListVO svo=dao.queryObject("scoreListMap.getLastDate", vo);
		if (svo==null) return 0;
		else return svo.getCreate_date().getTime();
	}
	
	//将最新的扣分记录存放在redis中，最多存放3条（要在扣分模块中调用）
	public void addNewRecord(List<ScoreListVO> itemlist, ScoreVO vo){
		String key = RedisKeyUtil.getRecordKey(ActionUtil.getSchoolID(),vo.getTeam_type(), vo.getScore_type(),
			vo.getAttend_item(),vo.getGroup_id(),vo.getTeam_id());
		TableVO tvo=new TableVO();//今天，本周，本月都要存储最新3条扣分记录
		tvo.setSum_type(DictConstants.SUM_TYPE_DAY);
		String key1=key+getScoreDateKey(tvo);
		tvo.setSum_type(DictConstants.SUM_TYPE_WEEK);//本周
		String key2=key+getScoreDateKey(tvo);
		tvo.setSum_type(DictConstants.SUM_TYPE_MONTH);
		String key3=key+getScoreDateKey(tvo);//本月
		if (DictConstants.SCORE_TYPE_HYGIENE.equals(vo.getScore_type())) {//卫生
			for (ScoreListVO svo : itemlist) {
				String record = svo.getScore_code() + ":" + svo.getScore();// 具体班级的卫生是显示扣分项
				jedisDao.lpush(key1, record);
				jedisDao.lpush(key2, record);
				jedisDao.lpush(key3, record);//将一个或多个值插入到列表头部
			}
		} else if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){//考勤
			for (ScoreListVO svo : itemlist) {
				String record = svo.getStudent_id() + ":" + svo.getScore_code();
				jedisDao.lpush(key1, record);
				jedisDao.lpush(key2, record);
				jedisDao.lpush(key3, record);
			}
		} else {
			for (ScoreListVO svo : itemlist) {
				String record = svo.getStudent_id() + ":" + svo.getScore();// 纪律和在校表现显示的是学生
				jedisDao.lpush(key1, record);
				jedisDao.lpush(key2, record);
				jedisDao.lpush(key3, record);//将一个或多个值插入到列表头部
			}
		}
		addClassNewRecord(itemlist, vo);//以班级为单位添加新记录到redis中，全校仪表盘中显示
	}

	//以班级为单位添加新记录到redis中，全校仪表盘中显示
	private void addClassNewRecord(List<ScoreListVO> itemlist, ScoreVO vo) {
		String skey = RedisKeyUtil.getRecordKey(ActionUtil.getSchoolID(),vo.getTeam_type(), vo.getScore_type(),
				vo.getAttend_item(),0, 0);
		TableVO table=new TableVO();
		table.setSum_type(DictConstants.SUM_TYPE_DAY);//今天
		String skey1=skey+getScoreDateKey(table);
		table.setSum_type(DictConstants.SUM_TYPE_WEEK);
		String skey2=skey+getScoreDateKey(table);//本周
		table.setSum_type(DictConstants.SUM_TYPE_MONTH);
		String skey3=skey+getScoreDateKey(table);
		String record="";
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
			List<CountInfoVO> list=new ArrayList<CountInfoVO>();
			int count41=0;
			int count42=0;
			int count43=0;
			for (ScoreListVO svo : itemlist) {
				if (DictConstants.ATTEND_STATUS_LATE.equals(svo.getScore_code())) count41++;
				else if (DictConstants.ATTEND_STATUS_LEAVE.equals(svo.getScore_code())) count42++;
				else count43++;
			}
			CountInfoVO cvo41=new CountInfoVO();
			cvo41.setCount(count41);
			cvo41.setScore_code(DictConstants.ATTEND_STATUS_LATE);
			list.add(cvo41);
			CountInfoVO cvo42=new CountInfoVO();
			cvo42.setCount(count42);
			cvo42.setScore_code(DictConstants.ATTEND_STATUS_LEAVE);
			list.add(cvo42);
			CountInfoVO cvo43=new CountInfoVO();
			cvo43.setCount(count43);
			cvo43.setScore_code(DictConstants.ATTEND_STATUS_NONARRIVAL);
			list.add(cvo43);
			record=vo.getGroup_id() + ":" + vo.getTeam_id() + ":" +BeanUtil.ListTojson(list);
		} else {
			Integer score = 0;//总分
			for (ScoreListVO svo : itemlist) {// 计算出班级该次总扣分
				score += svo.getScore();
			}
			record = vo.getGroup_id() + ":" + vo.getTeam_id() + ":" + score;
		}
		jedisDao.lpush(skey1, record);//将一个或多个值插入到列表头部
		jedisDao.lpush(skey2, record);
		jedisDao.lpush(skey3, record);
	}
	
	//获取排序扣分记录（3条）或全部
	public List<CountInfoVO> getNewRecord(TableVO vo){
		List<CountInfoVO> slist=new ArrayList<CountInfoVO>();
		ScoreVO svo=new ScoreVO();
		svo.setSchool_id(ActionUtil.getSchoolID());
		svo.setTeam_type(vo.getTeam_type());
		svo.setTeam_id(vo.getTeam_id());
		svo.setScore_type(vo.getScore_type());
		svo.setSum_type(vo.getSum_type());
		setStartEndTime(svo);//设置查询时间
		List<ScoreListVO> list=new ArrayList<ScoreListVO>();
		if (!vo.is_detail())//仪表盘首页
			list=dao.queryForList("scoreListMap.getNewRecord", svo);
		else 
			list=dao.queryForList("scoreListMap.getAllNewRecord", svo);//详情页面
		for (ScoreListVO slvo:list) {
			ScoreReasonVO reason=redisService.getScoreReasonFromRedis(ActionUtil.getSchoolID(),slvo.getScore_code(),vo.getScore_type(),vo.getTeam_type());
			CountInfoVO cvo=new CountInfoVO();
			cvo.setScore_code(reason.getScore_reason());
			cvo.setCount(slvo.getScore()*slvo.getCount());//可多个
			slist.add(cvo);
		}
		return slist;
	}//要注意寝室考勤

	//设置查询时间
	private void setStartEndTime(ScoreVO svo) {
		if (DictConstants.SUM_TYPE_DAY.equals(svo.getSum_type())) {
			svo.setStart_time(DateUtil.formatStringToDate(DateUtil.getNow("yyyy-MM-dd"),"yyyy-MM-dd"));
			svo.setEnd_time(DateUtil.formatDateEnd(svo.getStart_time()));
		} else if (DictConstants.SUM_TYPE_WEEK.equals(svo.getSum_type())) {//本周
			svo.setStart_time(DateUtil.formatStringToDate(DateUtil.getFirstDayOfWeek(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
			svo.setEnd_time(DateUtil.formatDateEnd(DateUtil.formatStringToDate(DateUtil.getNow("yyyy-MM-dd"),"yyyy-MM-dd")));
		} else if (DictConstants.SUM_TYPE_MONTH.equals(svo.getSum_type())) {//本月
			svo.setStart_time(DateUtil.formatStringToDate(DateUtil.getFirstDayOfMonth(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
			svo.setEnd_time(DateUtil.formatDateEnd(DateUtil.formatStringToDate(DateUtil.getNow("yyyy-MM-dd"),"yyyy-MM-dd")));
		}
	}

	//考勤班级完成比例
	public String completeCount(TableVO vo){
		String dateKey=getScoreDateKey(vo);
		String pattern="";
		String pattern1="";//年级的记录（用于过滤年级的记录）
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())){//班级
			pattern="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
				":SCORE_TYPE"+vo.getScore_type()+":*ATTEND_ITEM"+vo.getAttend_item()+dateKey;
			pattern1="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
					":SCORE_TYPE"+vo.getScore_type()+":*TEAM_ID0:ATTEND_ITEM"+vo.getAttend_item()+dateKey;
		}else{
			pattern="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
					":SCORE_TYPE"+vo.getScore_type()+":*ATTEND_ITEM"+dateKey;
			pattern1="SCORE:SCHOOL_ID"+ActionUtil.getSchoolID()+":TEAM_TYPE"+vo.getTeam_type()+
					":SCORE_TYPE"+vo.getScore_type()+":*TEAM_ID0:ATTEND_ITEM"+dateKey;
		} 
		Set<String> set=jedisDao.keys(pattern);//符合条件的记录条数就是已考勤的班级数
		Set<String> gradeSet=jedisDao.keys(pattern1);//年级的条数，需排除
		int size=set.size();
		int gradeSize=gradeSet.size();
		for (String s:set){//记录删完后，redis中存的是0，要排除
			if (Integer.parseInt(jedisDao.hget(s,"total_day").trim())==0)
				size=size-1;
		}
		for (String s:gradeSet){
			if (Integer.parseInt(jedisDao.hget(s,"total_day").trim())==0)
				gradeSize=gradeSize-1;
		}
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())){
			size = getSize(set, size);//过滤掉已经毕业的班级
			gradeSize = getSize(gradeSet, gradeSize);
			List<ClassVO> list=dao.queryForList("redisMap.getClassInfo", ActionUtil.getSchoolID());
			Integer classCount=list.size();//校班级数
			return size-gradeSize+"/"+classCount;
		} else {
			List<BedroomVO> list=dao.queryForList("redisMap.getBedRommInfo", ActionUtil.getSchoolID());
			Integer classCount=list.size();//校寝室数
			return size-gradeSize+"/"+classCount;
		}
		
	}

	//过滤掉已经毕业的班级
	private int getSize(Set<String> gradeSet, int gradeSize) {
		for (String s:gradeSet){
            int i=s.indexOf("GROUP_ID");
            int j=s.indexOf(":TEAM_ID");
            String group_id=s.substring(i+8,j);
            GradeVO grade=dao.queryObject("gradeMap.getGradeListByID",Integer.parseInt(group_id.trim()));
            if (grade.getIs_graduate()==1) gradeSize=gradeSize-1;
        }
		return gradeSize;
	}

	//未考勤的班级或寝室
	public List<ScoreVO> unAttendTeam(ScoreVO vo){
		List<ScoreVO> slist=new ArrayList<ScoreVO>();
		setStartEndTime(vo);//设置查询时间
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {
			List<ClassVO> list=dao.queryForList("scoreMap.getUnAttendTeamOfClass", vo);//班级未考勤班级
			for (ClassVO cvo:list) {
				ScoreVO svo=new ScoreVO();
				svo.setTeam_name(redisService.getTeamName(vo.getTeam_type(), cvo.getGrade_id(), cvo.getClass_id()));
				slist.add(svo);
			}
		} else if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())){
			List<BedroomVO> list=dao.queryForList("scoreMap.getUnAttendTeamOfBed", vo);//寝室未考勤班级
			for (BedroomVO bvo:list){
				ScoreVO svo=new ScoreVO();
				svo.setTeam_name(redisService.getTeamName(vo.getTeam_type(), 0, bvo.getBedroom_id()));
				slist.add(svo);
			}
		}
		return slist;
	}
	
	//获取仪表盘信息
	public List<LinkedHashMap<String,Object>> getDashBoardFromRedis(TableVO vo){
		List<LinkedHashMap<String,Object>> list=new ArrayList<LinkedHashMap<String,Object>>();
		List<SchoolModuleVO> modulelist=moduleService.getScoreModule();//正在使用的扣分模块
		if (IntegerUtil.isNotEmpty(vo.getGroup_id()) && IntegerUtil.isNotEmpty(vo.getTeam_id())) {//具体班级的仪表盘信息
			//考勤（晨检）
			vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
			DictVO dict=new DictVO();
			dict.setSchool_id(ActionUtil.getSchoolID());
			dict.setDict_group(DictConstants.ATTEND_ITEM);
			List<DictVO> dictList=dictService.getDictSchoolList(dict);//学校配置考勤项目
			for (DictVO dictVO:dictList){
				if (dictVO.getIs_active()==0) continue;
				vo.setAttend_item(dictVO.getDict_code());
				vo.setModule_code(DictConstants.MODULE_CODE_ATTEND);
				setInfoToList(vo, list);
			}
			DictVO dic=new DictVO();
			dic.setSchool_id(ActionUtil.getSchoolID());
			dic.setDict_group("012");
			List<DictVO> dictlist=dictService.getDictSchoolList(dic);//学校配置考勤项目
			for (SchoolModuleVO module:modulelist){
				if (DictConstants.MODULE_CODE_CLASSROOM.equals(module.getModule_code())){
					//班级纪律
					vo.setScore_type(DictConstants.SCORE_TYPE_DISCIPLINE);
					vo.setModule_code(DictConstants.MODULE_CODE_CLASSROOM);
					setInfoToList(vo,list);
					//班级卫生
					vo.setScore_type(DictConstants.SCORE_TYPE_HYGIENE);
					setInfoToList(vo,list);
				}
				if (DictConstants.MODULE_CODE_PERFORMANCE.equals(module.getModule_code())){
					//在校表现
					vo.setScore_type(DictConstants.SCORE_TYPE_PERFORMANCE);
					vo.setModule_code(DictConstants.MODULE_CODE_PERFORMANCE);
					setInfoToList(vo,list);
				}
				if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(module.getModule_code())){
					for (DictVO dictVO:dictlist){
						if (dictVO.getIs_active()==0) continue;
						if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(dictVO.getOther_field())) {
							vo.setScore_type(dictVO.getDict_code());
							vo.setModule_code(DictConstants.MODULE_CODE_CLASS_SCORE);
							setInfoToList(vo, list);
						}
					}
				}
				if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(module.getModule_code())){
					for (DictVO dictVO:dictlist){
						if (dictVO.getIs_active()==0) continue;
						if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(dictVO.getOther_field())) {
							vo.setScore_type(dictVO.getDict_code());
							vo.setModule_code(DictConstants.MODULE_CODE_PERSON_SCORE);
							setInfoToList(vo, list);
						}
					}
				}
			}
		} else if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())){//全校
			getSchoolDashBoard(vo, list,modulelist);//全校仪表盘统计信息
		}
		return list;
	}

	//全校仪表盘统计信息
	private void getSchoolDashBoard(TableVO vo,List<LinkedHashMap<String, Object>> list,List<SchoolModuleVO> modulelist) {
		//考勤晨检
		vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_group(DictConstants.ATTEND_ITEM);
		List<DictVO> dictList=dictService.getDictSchoolList(dict);//学校配置考勤项目
		for (DictVO dictVO:dictList){
			if (dictVO.getIs_active()==0) continue;
			vo.setAttend_item(dictVO.getDict_code());
			vo.setModule_code(DictConstants.MODULE_CODE_ATTEND);
			setInfoToList(vo, list);
		}
		//寝室点到
		vo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		setInfoToList(vo,list);
		DictVO dic=new DictVO();
		dic.setSchool_id(ActionUtil.getSchoolID());
		dic.setDict_group("012");
		List<DictVO> dictlist=dictService.getDictSchoolList(dic);//学校配置考勤项目
		for (SchoolModuleVO module:modulelist){
			if (DictConstants.MODULE_CODE_CLASSROOM.equals(module.getModule_code())){
				//班级纪律
				vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
				vo.setScore_type(DictConstants.SCORE_TYPE_DISCIPLINE);
				vo.setModule_code(DictConstants.MODULE_CODE_CLASSROOM);
				setInfoToList(vo,list);
				//班级卫生
				vo.setScore_type(DictConstants.SCORE_TYPE_HYGIENE);
				setInfoToList(vo,list);
			}
			if (DictConstants.MODULE_CODE_BEDROOM.equals(module.getModule_code())){
				//寝室纪律
				vo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
				vo.setScore_type(DictConstants.SCORE_TYPE_DISCIPLINE);
				vo.setModule_code(DictConstants.MODULE_CODE_BEDROOM);
				setInfoToList(vo,list);
				//寝室卫生
				vo.setScore_type(DictConstants.SCORE_TYPE_HYGIENE);
				setInfoToList(vo,list);
			}
			if (DictConstants.MODULE_CODE_PERFORMANCE.equals(module.getModule_code())){
				//在校表现
				vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
				vo.setScore_type(DictConstants.SCORE_TYPE_PERFORMANCE);
				vo.setModule_code(DictConstants.MODULE_CODE_PERFORMANCE);
				setInfoToList(vo,list);
			}
			if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(module.getModule_code())){
				for (DictVO dictVO:dictlist){
					if (dictVO.getIs_active()==0) continue;
					if (DictConstants.MODULE_CODE_CLASS_SCORE.equals(dictVO.getOther_field())) {
						vo.setScore_type(dictVO.getDict_code());
						vo.setModule_code(DictConstants.MODULE_CODE_CLASS_SCORE);
						vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
						setInfoToList(vo, list);
					}
				}
			}
			if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(module.getModule_code())){
				for (DictVO dictVO:dictlist){
					if (dictVO.getIs_active()==0) continue;
					if (DictConstants.MODULE_CODE_PERSON_SCORE.equals(dictVO.getOther_field())) {
						vo.setScore_type(dictVO.getDict_code());
						vo.setModule_code(DictConstants.MODULE_CODE_PERSON_SCORE);
						vo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
						setInfoToList(vo, list);
					}
				}
			}
		}
	}

	private void setInfoToList(TableVO vo,List<LinkedHashMap<String, Object>> list) {
		if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){
		String rate=getAttendRate(vo);
		if (StringUtil.isNotEmpty(rate)) {
			LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
			map.put("module_code",vo.getModule_code());
			map.put("score_type", vo.getScore_type());
			map.put("team_type", vo.getTeam_type());
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
				map.put("attend_item", vo.getAttend_item());
			map.put("rate", rate);
			int group_id=vo.getGroup_id();
			int team_id=vo.getTeam_id();
			if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id()))
				map.put("complete", completeCount(vo));
			vo.setGroup_id(group_id);
			vo.setTeam_id(team_id);
			map.put("detail", getAttenddetails(vo));
			list.add(map);
		} else {
			LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
			map.put("module_code",vo.getModule_code());
			map.put("score_type", vo.getScore_type());
			map.put("team_type", vo.getTeam_type());
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()))
				map.put("attend_item", vo.getAttend_item());
			list.add(map);
		}
		} else {
			setScoreInfoToList(vo, list);//获取非考勤的仪表盘统计信息
		}
	}

	//获取非考勤的仪表盘统计信息
	private void setScoreInfoToList(TableVO vo,List<LinkedHashMap<String, Object>> list) {
		if (IntegerUtil.isNotEmpty(vo.getGroup_id()) && IntegerUtil.isNotEmpty(vo.getTeam_id())){
			String dateKey=getScoreDateKey(vo);
			String queryKey=RedisKeyUtil.getScoreTeamKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
					vo.getGroup_id(), vo.getTeam_id());
			queryKey+=dateKey;
			if (jedisDao.exists(queryKey)) {
				LinkedHashMap<String,Object> dmap=new LinkedHashMap<String,Object>();
				dmap.put("module_code",vo.getModule_code());
				dmap.put("score_type", vo.getScore_type());
				dmap.put("team_type", vo.getTeam_type());
				int group_id=vo.getGroup_id();
				int team_id=vo.getTeam_id();
				vo.setGroup_id(group_id);
				vo.setTeam_id(team_id);
				if (DictConstants.SCORE_TYPE_HYGIENE.equals(vo.getScore_type()) ||
						DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code())){
					vo.setIs_detail(false);//仪表盘首页
					dmap.put("detail", BeanUtil.ListTojson(getNewRecord(vo)).toString());
				}else {
					dmap.put("ascDetail", getStudentRank(vo,1,0,2));//升序前3
					dmap.put("desDetail", getStudentRank(vo,-1,0,2));//降序前3
				}
				dmap.put("date", getLastTime(vo)+"");//新加的
				list.add(dmap);
			} else {
				LinkedHashMap<String,Object> dmap=new LinkedHashMap<String,Object>();
				dmap.put("module_code",vo.getModule_code());
				dmap.put("score_type", vo.getScore_type());
				dmap.put("team_type", vo.getTeam_type());
				list.add(dmap);
			}
		} else if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())){
			List<CountInfoVO> slist=getNewRecord(vo);
			if (ListUtil.isNotEmpty(slist)) {
				LinkedHashMap<String,Object> dmap=new LinkedHashMap<String,Object>();
				dmap.put("module_code",vo.getModule_code());
				dmap.put("score_type", vo.getScore_type());
				dmap.put("team_type", vo.getTeam_type());
				dmap.put("date",getLastTime(vo)+"");
				int group_id=vo.getGroup_id();
				int team_id=vo.getTeam_id();
				dmap.put("ascDetail", getRank(vo,1,0,2));//升序前3
				vo.setGroup_id(group_id);
				vo.setTeam_id(team_id);
				dmap.put("desDetail", getRank(vo,-1,0,2));//降序前3
				vo.setGroup_id(group_id);
				vo.setTeam_id(team_id);
				list.add(dmap);
			} else {
				LinkedHashMap<String,Object> dmap=new LinkedHashMap<String,Object>();
				dmap.put("module_code",vo.getModule_code());
				dmap.put("score_type", vo.getScore_type());
				dmap.put("team_type", vo.getTeam_type());
				list.add(dmap);
			}
		}
	}

	//获取相应仪表盘的详细信息
	public List<DashBoardVO> getDashBoardDetails(ScoreVO vo) {
		List<DashBoardVO> dlist=new ArrayList<DashBoardVO>();
		String key=RedisKeyUtil.getRecordKey(ActionUtil.getSchoolID(), vo.getTeam_type(), vo.getScore_type(),
				vo.getAttend_item(), vo.getGroup_id(), vo.getTeam_id());
		TableVO tvo=new TableVO();
		tvo.setSum_type(vo.getSum_type());
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && 
				DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())){
			tvo.setAttend_item(vo.getAttend_item());
		}
		tvo.setGroup_id(vo.getGroup_id());
		tvo.setSchool_id(ActionUtil.getSchoolID());
		tvo.setScore_type(vo.getScore_type());
		tvo.setTeam_id(vo.getTeam_id());
		tvo.setTeam_type(vo.getTeam_type());
		key=key+getScoreDateKey(tvo);
		if (IntegerUtil.isEmpty(vo.getGroup_id()) && IntegerUtil.isEmpty(vo.getTeam_id())) {
			List<DashBoardVO> blist=getRank(tvo,-1,0,-1);//降序
			dlist.addAll(blist);
			List<ScoreVO> slist=unAttendTeam(vo);
			for (ScoreVO svo:slist) {//还未打过分的班级
				DashBoardVO dvo=new DashBoardVO();
				dvo.setName(svo.getTeam_name());
				dlist.add(dvo);
			}
		} else if (IntegerUtil.isNotEmpty(vo.getGroup_id()) && IntegerUtil.isNotEmpty(vo.getTeam_id())){
			getClassDashBoardDetails(vo, dlist, tvo);//获取班级仪表盘详情信息
		}
		return dlist;
	}

	//获取班级仪表盘详情信息
	private void getClassDashBoardDetails(ScoreVO vo, List<DashBoardVO> dlist,TableVO tvo) {
		//具体班级仪表盘详细信息
		if (DictConstants.SCORE_TYPE_HYGIENE.equals(vo.getScore_type()) ||
				DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code())) {
			tvo.setIs_detail(true);//详情页面
			List<CountInfoVO> clist=getNewRecord(tvo);
			for (CountInfoVO cvo:clist) {
				DashBoardVO dash=new DashBoardVO();
				dash.setInfo(cvo.getCount().toString());
				dash.setName(cvo.getScore_code());
				dlist.add(dash);
			}
		} else {
			List<DashBoardVO> blist=getStudentRank(tvo,-1,0,-1);//降序
			dlist.addAll(blist);
		}
		if (!DictConstants.SCORE_TYPE_HYGIENE.equals(vo.getScore_type()) &&
				!DictConstants.MODULE_CODE_CLASS_SCORE.equals(vo.getModule_code())){//非卫生模块要加上未扣分过的学生
			getUnScoreStudent(vo, dlist);//未打分的学生
		}
	}

	//未打分的学生
	private void getUnScoreStudent(ScoreVO vo, List<DashBoardVO> dlist) {
		setStartEndTime(vo);//设置查询时间
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {//班级
			List<StudentVO> stulist=dao.queryForList("scoreListMap.getUnScoreStudentOfClass", vo);//班级未考勤班级
			for (StudentVO cvo:stulist) {
				DashBoardVO svo=new DashBoardVO();
				svo.setName(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,
						0, cvo.getStudent_id()));
				dlist.add(svo);
			}
		} else if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())){//寝室
			List<StudentVO> stulist=dao.queryForList("scoreListMap.getUnScoreStudentOfBed", vo);//寝室未考勤班级
			for (StudentVO bvo:stulist){
				DashBoardVO svo=new DashBoardVO();
				svo.setName(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,
						0, bvo.getStudent_id()));
				dlist.add(svo);
			}
		}
	}
	
	/**
	 * 初始化统计表头
	 */
	public void initTableHead(Integer school_id){
		TableHeadVO tableHeadVO=new TableHeadVO();
		tableHeadVO.setSchool_id(school_id);
		tableHeadVO.setScore_type(DictConstants.MODULE_CODE_HOMEWORK);
		dao.deleteObject("tableMap.deleteTableHeadBySchoolID", tableHeadVO);//删除旧表头（除了作业统计）
		//扣分项
		TableHeadVO thvo=new TableHeadVO();
		thvo.setSchool_id(school_id);
		thvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		thvo.setFunc_type(DictConstants.TABLE_SUM);//汇总
		thvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initTableHeadScoreCode", thvo);
		thvo.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		dao.insertObject("tableMap.initTableHeadScoreCode", thvo);
		
		//学号，姓名，班级，班级人数
		thvo.setFunc_type("");
		thvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		thvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		thvo.setField("student_code");
		thvo.setField_name("学号");
		thvo.setSort(1);
		thvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setField("student_name");
		thvo.setField_name("姓名");
		thvo.setSort(2);
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		thvo.setField("team_name");
		thvo.setField_name("班级");
		thvo.setSort(1);
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setField("team_count");
		thvo.setField_name("班级人数");
		thvo.setSort(2);
		thvo.setFunc_type(DictConstants.TABLE_SUM);
		dao.insertObject("tableMap.initTableHead", thvo);
		
		//床位号，姓名，寝室，寝室人数
		thvo.setFunc_type("");//不需要汇总
		thvo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		thvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		thvo.setField("bed_code");
		thvo.setField_name("床位号");
		thvo.setSort(1);
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setField("student_name");
		thvo.setField_name("姓名");
		thvo.setSort(2);
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		thvo.setField("team_name");
		thvo.setField_name("寝室");
		thvo.setSort(1);
		dao.insertObject("tableMap.initTableHead", thvo);
		thvo.setField("team_count");
		thvo.setField_name("寝室人数");
		thvo.setSort(2);
		thvo.setFunc_type(DictConstants.TABLE_SUM);
		dao.insertObject("tableMap.initTableHead", thvo);
		
		//考勤的床位号，姓名
		TableHeadVO hh=new TableHeadVO();
		hh.setSchool_id(school_id);
		hh.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		hh.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		hh.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		hh.setField("bed_code");
		hh.setField_name("床位号");
		hh.setSort(1);
		hh.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", hh);
		hh.setField("student_name");
		hh.setField_name("姓名");
		hh.setSort(2);
		dao.insertObject("tableMap.initHead", hh);
		
		//考勤的寝室，寝室人数
		TableHeadVO hh1=new TableHeadVO();
		hh1.setSchool_id(school_id);
		hh1.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		hh1.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		hh1.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		hh1.setField("team_name");
		hh1.setField_name("寝室");
		hh1.setSort(1);
		hh1.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", hh1);
		hh1.setField("team_count");
		hh1.setField_name("寝室人数");
		hh1.setSort(2);
		hh1.setFunc_type(DictConstants.TABLE_SUM);
		dao.insertObject("tableMap.initHead", hh1);
		//考勤的考勤次数
		addScoreTotal(thvo);
		//除考勤的扣分总分
		addScoreTotal(school_id);
		//rate
		ScoreReasonVO srvo5=new ScoreReasonVO();
		srvo5.setSchool_id(school_id);
		srvo5.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		srvo5.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		srvo5.setIs_active(1);
		List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", srvo5);
		//考勤率
		addScoreRate(srvo5, list);
		//寝室考勤的扣分项
		addBedroomAttend(srvo5, list);
		TableHeadVO head=new TableHeadVO();
		head.setSchool_id(ActionUtil.getSchoolID());
		head.setField(DictConstants.ATTEND_STATUS_ARRIVE);
		dao.deleteObject("tableMap.deleteTableHead", head);
	}

	//考勤的考勤次数
	private void addScoreTotal(TableHeadVO thvo) {
		thvo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		thvo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		thvo.setField("total_day");
		thvo.setField_name("考勤次数");
		thvo.setSort(3);
		thvo.setFunc_type(DictConstants.TABLE_SUM);
		thvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", thvo);
		thvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		dao.insertObject("tableMap.initHead", thvo);
		thvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		dao.insertObject("tableMap.initHead", thvo);
		thvo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		dao.insertObject("tableMap.initHead", thvo);
	}

	//除考勤的扣分总分
	private void addScoreTotal(Integer school_id) {
		ScoreReasonVO srvo=new ScoreReasonVO();
		srvo.setSchool_id(school_id);
		srvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		srvo.setScore_type(DictConstants.SCORE_TYPE_DISCIPLINE);
		addTotal(srvo);
		ScoreReasonVO srvo1=new ScoreReasonVO();
		srvo1.setSchool_id(school_id);
		srvo1.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		srvo1.setScore_type(DictConstants.SCORE_TYPE_HYGIENE);
		addTotal(srvo1);
		ScoreReasonVO srvo2=new ScoreReasonVO();
		srvo2.setSchool_id(school_id);
		srvo2.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		srvo2.setScore_type(DictConstants.SCORE_TYPE_DISCIPLINE);
		addTotal(srvo2);
		ScoreReasonVO srvo3=new ScoreReasonVO();
		srvo3.setSchool_id(school_id);
		srvo3.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		srvo3.setScore_type(DictConstants.SCORE_TYPE_HYGIENE);
		addTotal(srvo3);
		ScoreReasonVO srvo4=new ScoreReasonVO();
		srvo4.setSchool_id(school_id);
		srvo4.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		srvo4.setScore_type(DictConstants.SCORE_TYPE_PERFORMANCE);
		addTotal(srvo4);
		//班级评分,个人评分
        DictVO dict=new DictVO();
        dict.setIs_active(1);
        dict.setSchool_id(school_id);
        dict.setOther_field(DictConstants.MODULE_CODE_CLASS_SCORE);
        dict.setDict_group("012");
        List<DictVO> list=dictService.getDictSchoolList(dict);
        for (DictVO dictVO:list){
            ScoreReasonVO srvo5=new ScoreReasonVO();
            srvo5.setSchool_id(school_id);
            srvo5.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
            srvo5.setScore_type(dictVO.getDict_code());
            addTotal(srvo5);
        }
		dict.setOther_field(DictConstants.MODULE_CODE_PERSON_SCORE);
		List<DictVO> list1=dictService.getDictSchoolList(dict);
		for (DictVO dictVO:list1){
			ScoreReasonVO srvo5=new ScoreReasonVO();
			srvo5.setSchool_id(school_id);
			srvo5.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			srvo5.setScore_type(dictVO.getDict_code());
			addTotal(srvo5);
		}
	}

	//考勤率
	private void addScoreRate(ScoreReasonVO srvo5, List<ScoreReasonVO> list) {
		String total="";
		String sum="";//field_func_sum
		for (ScoreReasonVO vo:list) {//({total_day}-{013041}-{013042}-{013043})*100/{total_day}%
			if (DictConstants.ATTEND_STATUS_ARRIVE.equals(vo.getScore_code())) continue;
			if (StringUtil.isEmpty(total)) {//([{total_day}*{team_count})]-[{013041}+{013042}+{013043}])*100/([{total_day}*{team_count}])%
				total+="({total_day}-{"+vo.getScore_code()+"}";
				sum+="([{total_day}*{team_count})]-[{"+vo.getScore_code()+"}";
			} else {
				total+="-{"+vo.getScore_code()+"}";
				sum+="+{"+vo.getScore_code()+"}";
			}
		}
		total+=")*100/{total_day}%";
		sum+="])*100/([{total_day}*{team_count}])%";
		addRate(srvo5, total,DictConstants.COUNT_TYPE_STUDENT,sum);
		String total1="";
		for (ScoreReasonVO vo:list) {//({total_day}*{team_count}-{013041}-{013042}-{013043})*100/({total_day}*{team_count})%
			if (DictConstants.ATTEND_STATUS_ARRIVE.equals(vo.getScore_code())) continue;
			if (StringUtil.isEmpty(total1)) {
				total1+="({total_day}*{team_count}-{"+vo.getScore_code()+"}";
			} else {
				total1+="-{"+vo.getScore_code()+"}";
			}
		}
		total1+=")*100/({total_day}*{team_count})%";
		srvo5.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		addRate(srvo5, total1,DictConstants.COUNT_TYPE_TEAM,sum);
	}

	//寝室考勤的扣分项
	private void addBedroomAttend(ScoreReasonVO srvo5, List<ScoreReasonVO> list) {
		for (ScoreReasonVO vo:list) {
			TableHeadVO h=new TableHeadVO();
			h.setSchool_id(srvo5.getSchool_id());
			h.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
			h.setScore_type(srvo5.getScore_type());
			h.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
			h.setField(vo.getScore_code());
			h.setField_name(vo.getScore_reason());
			h.setSort(5);
			h.setFunc_type(DictConstants.TABLE_SUM);//汇总
			h.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("tableMap.initHead", h);
		}
		for (ScoreReasonVO vo:list) {
			TableHeadVO h=new TableHeadVO();
			h.setSchool_id(srvo5.getSchool_id());
			h.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
			h.setScore_type(srvo5.getScore_type());
			h.setCount_type(DictConstants.COUNT_TYPE_TEAM);
			h.setField(vo.getScore_code());
			h.setField_name(vo.getScore_reason());
			h.setSort(5);
			h.setFunc_type(DictConstants.TABLE_SUM);
			h.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("tableMap.initHead", h);
		}
	}

	private void addRate(ScoreReasonVO srvo, String total,String count_type,String sum) {
		TableHeadVO hvo=new TableHeadVO();
		hvo.setSchool_id(srvo.getSchool_id());
		hvo.setTeam_type(srvo.getTeam_type());
		hvo.setScore_type(srvo.getScore_type());
		hvo.setCount_type(count_type);
		hvo.setField("rate");
		hvo.setField_name("出勤率");
		hvo.setSort(15);
		hvo.setField_func(total);
		hvo.setField_func_sum(sum);
		hvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", hvo);
		hvo.setTeam_type(DictConstants.TEAM_TYPE_BEDROOM);
		dao.insertObject("tableMap.initHead", hvo);
	}

	private void addTotal(ScoreReasonVO srvo) {
		srvo.setIs_active(1);
		List<ScoreReasonVO> list=dao.queryForList("scoreReasonMap.getScoreReasonList", srvo);
		String total="";
		for (ScoreReasonVO vo:list) {//{013051}%2B{013052}%2B{013053}
			if (StringUtil.isEmpty(total)) {
				total+="{"+vo.getScore_code()+"}";
			} else {
				total+="%2B{"+vo.getScore_code()+"}";
			}
		}
		TableHeadVO hvo=new TableHeadVO();
		hvo.setSchool_id(srvo.getSchool_id());
		hvo.setTeam_type(srvo.getTeam_type());
		hvo.setScore_type(srvo.getScore_type());
		hvo.setCount_type(DictConstants.COUNT_TYPE_STUDENT);
		hvo.setField("total");
		hvo.setField_name("总分");
		hvo.setSort(15);
		hvo.setField_func(total);
		hvo.setFunc_type(DictConstants.TABLE_SUM);//汇总
		hvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("tableMap.initHead", hvo);
		hvo.setCount_type(DictConstants.COUNT_TYPE_TEAM);
		dao.insertObject("tableMap.initHead", hvo);
	}
}