package com.ninesky.classtao.redis.service.impl;

import com.ninesky.classtao.balance.vo.BalanceVO;
import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.classtao.getui.vo.GetuiVO;
import com.ninesky.classtao.homework.vo.HomeworkGroupVO;
import com.ninesky.classtao.homework.vo.HomeworkReceiveVO;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.notice.vo.NoticeCountVO;
import com.ninesky.classtao.notice.vo.NoticeReadVO;
import com.ninesky.classtao.notice.vo.NoticeReplyVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.*;
import com.ninesky.classtao.score.vo.ScoreListVO;
import com.ninesky.classtao.score.vo.ScoreReasonVO;
import com.ninesky.classtao.score.vo.ScoreVO;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service("RedisServiceImpl")
public class RedisServiceImpl implements RedisService {
	@Autowired
	private GeneralDAO dao;

	@Autowired
	private JedisDAO jedisDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SchoolService schoolService;

	//private static Logger logger = LoggerFactory.getLogger(RedisService.class);
	
	public Integer getTotalFromTeam(ReceiveVO vo) {
		String groupKey = RedisKeyUtil.getGroupKey(vo);
		if (jedisDao.hexists(groupKey, "total"+vo.getUser_type()))
			return IntegerUtil.getValue(jedisDao.hget(groupKey, "total"+vo.getUser_type()));
		else if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) {
			//计算学生数量
			Integer student_count = 0;
			Integer teacher_count = 0;
			if (IntegerUtil.isEmpty(vo.getTeam_id()) && IntegerUtil.isEmpty(vo.getGroup_id()))
			{
				student_count = dao.queryObject("redisMap.getStudentCountBySchool",vo.getSchool_id());
				teacher_count = dao.queryObject("redisMap.getTeacherCountBySchool",vo.getSchool_id());
			} else if (IntegerUtil.isEmpty(vo.getTeam_id()))
			{
				student_count = dao.queryObject("redisMap.getStudentCountByGrade",vo.getGroup_id());
				teacher_count = dao.queryObject("redisMap.getTeacherCountByGrade",vo.getGroup_id());
			}
			else {
				student_count = dao.queryObject("redisMap.getStudentCountByClass",vo.getTeam_id());
				teacher_count = dao.queryObject("redisMap.getTeacherCountByClass",vo.getTeam_id());
			}
			jedisDao.hset(groupKey, "total"+DictConstants.USERTYPE_STUDENT, student_count.toString());
			jedisDao.hset(groupKey, "total"+DictConstants.USERTYPE_TEACHER, teacher_count.toString());
			jedisDao.hset(groupKey, "total"+DictConstants.USERTYPE_ALL, (teacher_count+student_count)+"");
			return IntegerUtil.getValue(jedisDao.hget(groupKey, "total"+vo.getUser_type()));
		} else if (DictConstants.TEAM_TYPE_BEDROOM.equals(vo.getTeam_type())) {
			Integer count = dao.queryObject("redisMap.getBedRoomCountByTeam",vo);
			jedisDao.hset(groupKey, "total"+DictConstants.USERTYPE_STUDENT, count.toString());
			return count;
		} else return 0;//自定义
	}

	public Set<String> getTeamKeyList(Integer school_id,String user_type,Integer user_id,Integer student_id) {
		String personUnionKey = RedisKeyUtil.getUnionKey(school_id, user_type, user_id, student_id);
		String userGroupKey = RedisKeyUtil.KEY_GROUP_PRE + personUnionKey;
		Set<String> teamList = jedisDao.sMembers(userGroupKey);
		if (teamList==null || teamList.isEmpty()) {
			//添加老师身份到个人分组配置
			if (DictConstants.USERTYPE_TEACHER.equals(user_type)) {
				TeacherVO vo = new TeacherVO();
				vo.setSchool_id(school_id);
				vo.setUser_id(user_id);
				List<TeacherVO> list = userService.getTeacherListByUserID(vo);
				if (ListUtil.isEmpty(list)) throw new BusinessException("该用户没有分配教师权限，请核实身份！");
				for (TeacherVO teacher:list) {
					if (DictConstants.TEAM_TYPE_CLASS.equals(teacher.getTeam_type())){//普通班级
						String groupKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, teacher.getGrade_id(), teacher.getClass_id());
						jedisDao.sadd(userGroupKey, groupKey);
						String gradeKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, teacher.getGrade_id(), 0);
						if (!teamList.contains(gradeKey))
						{
							jedisDao.sadd(userGroupKey, gradeKey);
							teamList.add(gradeKey);
						}
						String schoolKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, 0, 0);
						if (!teamList.contains(schoolKey))
						{
							jedisDao.sadd(userGroupKey, schoolKey);
							teamList.add(schoolKey);
						}
						teamList.add(groupKey);
					} else if (DictConstants.TEAM_TYPE_INTEREST.equals(teacher.getTeam_type())) {//兴趣班
						String groupKey=RedisKeyUtil.getGroupKey(school_id, teacher.getTeam_type(), 0, teacher.getContact_id());
						jedisDao.sadd(userGroupKey, groupKey);
						teamList.add(groupKey);
					}
				}
			} else {
				StudentVO stu = userService.getStudentById(student_id);
				if (stu == null) throw new BusinessException("该学生信息不存在，请核实身份！");
				String groupKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, stu.getGrade_id(), stu.getClass_id());
				jedisDao.sadd(userGroupKey, groupKey);
				String gradeKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, stu.getGrade_id(), 0);
				if (!teamList.contains(gradeKey))
				{
					jedisDao.sadd(userGroupKey, gradeKey);
					teamList.add(gradeKey);
				}
				String schoolKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_CLASS, 0, 0);
				if (!teamList.contains(schoolKey))
				{
					jedisDao.sadd(userGroupKey, schoolKey);
					teamList.add(schoolKey);
				}
				teamList.add(groupKey);
				BedVO bed = new BedVO();
				bed.setSchool_id(school_id);
				bed.setStudent_id(student_id);
				List<BedVO> stuBedList = dao.queryForList("bedroomMap.getBedroomByStuID",bed);
				for (BedVO bedvo:stuBedList) {
					String bedGroupKey = RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_BEDROOM, 0, bedvo.getBedroom_id());
					jedisDao.sadd(userGroupKey, bedGroupKey);
					teamList.add(bedGroupKey);
				}
				List<Integer> contactList=dao.queryForList("contactListMap.getContactListByStuID", student_id);
				for (Integer contact_id:contactList) {
					String contactGroupKey=RedisKeyUtil.getGroupKey(school_id, DictConstants.TEAM_TYPE_INTEREST, 0, contact_id);
					jedisDao.sadd(userGroupKey, contactGroupKey);
					teamList.add(contactGroupKey);
				}
			}
		} 
		if (teamList==null || teamList.isEmpty()) 
			throw new BusinessException("账户权限获取失败，请核实身份！");
		return teamList;
	}

	//从redis中获取团队名称（年级名称，班级名称）
	public String getTeamName(String team_type,Integer group_id,Integer team_id) {
		if (IntegerUtil.isEmpty(group_id)) group_id=0;
		if (IntegerUtil.isEmpty(team_id)) team_id=0;
		String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		//判断redis中是否已有该值,如果有值，直接返回
		if (jedisDao.hexists(groupKey, "team_name")) return jedisDao.hget(groupKey, "team_name");
			String teamname = getTeamNameByTeamType(team_type, group_id, team_id); 
		if (StringUtil.isNotEmpty(teamname)) {
			jedisDao.hset(groupKey, "team_name", teamname);//如果为空，那么从数据库中获取数据，并创建key
			jedisDao.expireAt(groupKey, DateUtil.teamnameUpdateTime(9, 1));
		}
		return teamname;
	}


	/**
	 * 从数据库中获取相关参数，并生成班级、年级（寝室、兴趣班）名称
	 * @param team_type
	 * @param group_id
	 * @param team_id
	 * @return 团队名称
	 */
	private String getTeamNameByTeamType(String team_type, Integer group_id,Integer team_id) {
		if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {//班级
			SchoolVO schoolVO = dao.queryObject("schoolMap.getSchoolInfo", ActionUtil.getSchoolID());
			if (IntegerUtil.isEmpty(team_id) && IntegerUtil.isNotEmpty(group_id)) {
				GradeVO vo=dao.queryObject("gradeMap.getGradeListByID", group_id);//年级名称
				return TeamNameUtil.getGradename(schoolVO.getSchool_type(), vo.getEnrollment_year());
			} else if (IntegerUtil.isNotEmpty(team_id)){
				ClassVO vo = dao.queryObject("classMap.getClassByID",team_id);//班级名称
				return TeamNameUtil.getClassname(schoolVO.getSchool_type(), vo.getEnrollment_year(), vo.getClass_num());
			} else if (IntegerUtil.isEmpty(team_id) && IntegerUtil.isEmpty(group_id))
				return "全校";
		} else if (DictConstants.TEAM_TYPE_BEDROOM.equals(team_type)) {
			if (IntegerUtil.isEmpty(team_id)) return "全校";
			BedroomVO vo=dao.queryObject("bedroomMap.getBedroomInfoById", team_id);//寝室名称
			return vo.getBedroom_name();
		} else if  (DictConstants.TEAM_TYPE_INTEREST.equals(team_type)) {
			ContactVO vo=dao.queryObject("contactMap.getContactById", team_id);//寝室名称
			return vo.getContact_name();
		}
		return "";
	}

	//从redis中获取相应年级或班级的学生人数
	public Integer getTeamStudentCount(String team_type,Integer group_id,Integer team_id) {
		String groupKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		boolean flag=jedisDao.hexists(groupKey, "total003010");//判断redis中是否已有该值
		if (!flag) {
			Integer count=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {//教室
				if (IntegerUtil.isEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {
					count=dao.queryObject("redisMap.getStudentCountBySchool", ActionUtil.getSchoolID());
				} else if (IntegerUtil.isNotEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {//年级学生人数
					count=dao.queryObject("redisMap.getStudentCountByGrade", group_id);
				} else {
					count=dao.queryObject("redisMap.getStudentCountByClass", team_id);//班级学生人数
				}
			} else {
				ReceiveVO vo=new ReceiveVO();
				vo.setTeam_id(team_id);
				count=dao.queryObject("redisMap.getBedRoomCountByTeam", vo);//寝室学生人数
			}
			jedisDao.hset(groupKey, "total003010", count.toString());
			return count;
		} else {
			return Integer.parseInt(jedisDao.hget(groupKey, "total003010").toString().trim());
		}
	}

	// 从redis中获取相应年级或班级,寝室的教师人数
	public Integer getTeamTeacherCount(String team_type, Integer group_id,Integer team_id) {
		String groupKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		boolean flag=jedisDao.hexists(groupKey, "total003005");//判断redis中是否已有该值
		if (!flag) {
			Integer count=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {
				if (IntegerUtil.isEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {//全校的教师数
					count=dao.queryObject("redisMap.getTeacherCountBySchool", ActionUtil.getSchoolID());
				} else if (IntegerUtil.isNotEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {
					count=dao.queryObject("redisMap.getTeacherCountByGrade", group_id);//年级教师数
				} else {
					count=dao.queryObject("redisMap.getTeacherCountByClassID", team_id);
				}
			} else {
				count=0;//目前，寝室教师数设为0
			}
			jedisDao.hset(groupKey, "total003005", count.toString());
			return count;
		} else {
			return Integer.parseInt(jedisDao.hget(groupKey, "total003005").toString().trim());
		}
	}

	//从redis中获取相应年级或班级，寝室的教师，学生总人数
	public Integer getTeamTotalCount(String team_type, Integer group_id,Integer team_id) {
		String groupKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		boolean flag=jedisDao.hexists(groupKey, "total003");
		if (!flag) {
			Integer count=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {
				if (IntegerUtil.isEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {//全校师生总人数
					int tcount=dao.queryObject("redisMap.getTeacherCountBySchool", ActionUtil.getSchoolID());
					int scount=dao.queryObject("redisMap.getStudentCountBySchool", ActionUtil.getSchoolID());
					count=tcount+scount;
				} else if (IntegerUtil.isNotEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {//该年级段师生总人数
					int tcount=dao.queryObject("redisMap.getTeacherCountByGrade", group_id);
					int scount=dao.queryObject("redisMap.getStudentCountByGrade", group_id);
					count=tcount+scount;
				} else {//该班级师生总人数
					int tcount=dao.queryObject("redisMap.getTeacherCountByClassID", team_id);
					int scount=dao.queryObject("redisMap.getStudentCountByClass", team_id);
					count=tcount+scount;
				}
			} else {//寝室师生总人数
				ReceiveVO vo=new ReceiveVO();
				vo.setTeam_id(team_id);
				count=dao.queryObject("redisMap.getBedRoomCountByTeam", vo);//寝室学生人数
			}
			jedisDao.hset(groupKey, "total003", count.toString());
			return count;
		} else {
			return Integer.parseInt(jedisDao.hget(groupKey, "total003").toString().trim());
		}
	}

	//从redis中获取教师或学生的姓名
	public String getUserName(Integer school_id, String user_type,Integer user_id, Integer student_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id, user_type, user_id, student_id);
		boolean flag=jedisDao.hexists(userKey, "user_name");
		if (!flag) {
			String user_name="";
			if (DictConstants.USERTYPE_STUDENT.equals(user_type) || DictConstants.USERTYPE_PARENT.equals(user_type)) {
				StudentVO vo=dao.queryObject("redisMap.getStudentInfo", student_id);
				user_name=vo.getStudent_name();
			} else if (DictConstants.USERTYPE_AGENT.equals(user_type)){
				UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);
				user_name=vo.getUser_name();
			} else {
				UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);//统一成用户表中的姓名
				if (vo==null || StringUtil.isEmpty(vo.getUser_name())) {//其实是防止以前的旧数据
					TeacherVO tvo=new TeacherVO();
					tvo.setSchool_id(ActionUtil.getSchoolID());
					tvo.setUser_id(user_id);
					List<TeacherVO> tlist=userService.getTeacherListByUserID(tvo);
					if (ListUtil.isNotEmpty(tlist)) {
						for (TeacherVO teacher:tlist) {
							if (StringUtil.isNotEmpty(teacher.getTeacher_name())){
								user_name=teacher.getTeacher_name();
								break;
							}
						}
					}
				} else user_name=vo.getUser_name();
			}
			if (StringUtil.isNotEmpty(user_name))
				jedisDao.hset(userKey, "user_name", user_name);
			return user_name;
		} else {
			return jedisDao.hget(userKey, "user_name");
		}
	}
	
	//从redis中获取教师的电话号码
	public String getUserPhone(Integer school_id, String user_type,Integer user_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id, user_type, user_id,0);
		boolean flag=jedisDao.hexists(userKey, "phone");
		if (!flag) {
			String phone="";
			UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);//统一成用户表中的电话号码
			if (vo==null) {
				TeacherVO tvo=new TeacherVO();
				tvo.setSchool_id(ActionUtil.getSchoolID());
				tvo.setUser_id(user_id);
				List<TeacherVO> tlist=userService.getTeacherListByUserID(tvo);
				if (ListUtil.isNotEmpty(tlist)) phone=tlist.get(0).getPhone();
			} else phone=vo.getPhone();
			if (StringUtil.isNotEmpty(phone))
				jedisDao.hset(userKey, "phone", phone);
			return phone;
		} else {
			return jedisDao.hget(userKey, "phone");
		}
	}

	//从redis中获取教师或学生的头像
	public String getUserHeadUrl(Integer school_id, String user_type,Integer user_id, Integer student_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,user_type,user_id,student_id);
		boolean flag=jedisDao.hexists(userKey, "head_url");
		if (!flag) {
			String head_url="";
			if (DictConstants.USERTYPE_STUDENT.equals(user_type) || DictConstants.USERTYPE_PARENT.equals(user_type)) {
				StudentVO vo=dao.queryObject("redisMap.getStudentInfo", student_id);
				head_url=vo.getHead_url();
			} else {
				UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);//统一成用户表中的头像
				if (vo==null) return Constants.HEAD_URL_DEFAULT;
				head_url=vo.getHead_url();
			}
			if (StringUtil.isEmpty(head_url)) head_url=Constants.HEAD_URL_DEFAULT;//若头像为空，设成默认头像
			jedisDao.hset(userKey, "head_url", head_url);
			return head_url;
		} else {
			return jedisDao.hget(userKey, "head_url");
		}
	}

	//从redis中获取教师或学生的all_letter
	public String getAllLetter(Integer school_id, String user_type,Integer user_id, Integer student_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,user_type,user_id,student_id);
		boolean flag=jedisDao.hexists(userKey, "all_letter");
		if (!flag) {
			String all_letter="";
			if (DictConstants.USERTYPE_STUDENT.equals(user_type) || DictConstants.USERTYPE_PARENT.equals(user_type)) {
				StudentVO vo=dao.queryObject("redisMap.getStudentInfo", student_id);
				all_letter=vo.getAll_letter();
			} else {
				UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);//统一成用户表中的头像
				all_letter=LetterUtil.converterToSpell(vo.getUser_name());
			}
			jedisDao.hset(userKey, "all_letter", all_letter);
			return all_letter;
		} else {
			return jedisDao.hget(userKey, "all_letter");
		}
	}

	//从redis中获取教师或学生的first_letter
	public String getFirstLetter(Integer school_id, String user_type,Integer user_id, Integer student_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,user_type,user_id,student_id);
		boolean flag=jedisDao.hexists(userKey, "first_letter");
		if (!flag) {
			String first_letter="";
			if (DictConstants.USERTYPE_STUDENT.equals(user_type) || DictConstants.USERTYPE_PARENT.equals(user_type)) {
				StudentVO vo=dao.queryObject("redisMap.getStudentInfo", student_id);
				first_letter=vo.getFirst_letter();
			} else {
				UserVO vo=dao.queryObject("redisMap.getTeacherInfo", user_id);//统一成用户表中的头像
				first_letter=LetterUtil.converterToFirstSpell(vo.getUser_name());
			}
			jedisDao.hset(userKey, "first_letter", first_letter);
			return first_letter;
		} else {
			return jedisDao.hget(userKey, "first_letter");
		}
	}

	//修改redis中教师或学生的姓名
	public void updateUserName(Integer school_id, String user_type,Integer user_id, Integer student_id, 
			String user_name) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,user_type,user_id,student_id);
		jedisDao.hset(userKey, "user_name", user_name);
		jedisDao.hset(userKey,"all_letter",LetterUtil.converterToSpell(user_name));
		jedisDao.hset(userKey,"first_letter",LetterUtil.converterToFirstSpell(user_name));
	}

	//修改redis中教师或学生的头像url
	public void updateHeadUrl(Integer school_id, String user_type,Integer user_id, Integer student_id, 
			String head_url) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,user_type,user_id,student_id);
		jedisDao.hset(userKey, "head_url", head_url);
	}

	public void addNewsToRedis(NewsVO vo) {
		String newsGroupKey = RedisKeyUtil.getNewsDictGroupKey(ActionUtil.getSchoolID(),vo.getDict_group());
		String newsKey = RedisKeyUtil.getNewsKey(ActionUtil.getSchoolID(),vo.getDict_group(),vo.getNews_id());
		jedisDao.hset(newsKey, "news_id",vo.getNews_id().toString().trim());
		jedisDao.hset(newsKey, "dict_group",vo.getDict_group());
		jedisDao.hset(newsKey, "news_code", vo.getNews_code());
		jedisDao.hset(newsKey, "title",  StringUtil.subString(vo.getTitle(), 20));
		jedisDao.hset(newsKey, "content_text",  StringUtil.subString(vo.getContent_text(),50));
		if (StringUtil.isNotEmpty(vo.getMain_pic_url())) jedisDao.hset(newsKey, "main_pic_url", vo.getMain_pic_url());
		jedisDao.hset(newsKey, "deploy_date", vo.getDeploy_date());
		//将news业务数据和分组对应起来
		jedisDao.lpush(newsGroupKey, newsKey);
		long length = DictConstants.NEWS_XXFC_DICT_GROUP.equals(vo.getDict_group())?1:2;
		List<String> dictGroup = jedisDao.lrange(newsGroupKey, length, -1);
		for (String redisKey : dictGroup) {
			jedisDao.del(redisKey);
		}
		jedisDao.ltrim(newsGroupKey, 0, length-1);
	}
	
	public List<NewsVO> getNewsList(Integer school_id) {
		List<NewsVO> newsList = new ArrayList<NewsVO>();
		List<String> dictList = getNewsListByDictGroup(school_id);
		for (String dict_group : dictList) {
			//根据newsGroupKey键模糊查询相关联的newsKey的键名
			String newsGroupKey = RedisKeyUtil.getNewsDictGroupKey(ActionUtil.getSchoolID(),dict_group);
			if (jedisDao.exists(newsGroupKey)) {
				List<String> newsKeys = jedisDao.lrange(newsGroupKey, 0, -1);//使用lpush相关联的数据，需要获取时要使用lrange方法，因为返回值是list集合类型
				for (String newsKey : newsKeys) {
					if (StringUtil.isEmpty(jedisDao.keys(newsKey))) continue;
					NewsVO news = new NewsVO();
					news.setNews_id(Integer.parseInt(jedisDao.hget(newsKey, "news_id")));
					news.setContent_text(jedisDao.hget(newsKey, "content_text"));
					news.setNews_code(jedisDao.hget(newsKey, "news_code"));
					news.setDict_group(jedisDao.hget(newsKey, "dict_group"));
					news.setDeploy_date(jedisDao.hget(newsKey, "deploy_date"));
					news.setMain_pic_url(jedisDao.hget(newsKey, "main_pic_url"));
					news.setTitle(jedisDao.hget(newsKey, "title"));
					newsList.add(news);	
				}		
			} else newsList.addAll(getNewsListByGroup(dict_group));//如果redis数据库崩溃或者新添加一个news_group导致获取不到数据则直接从MySql数据库中查询添加并返回
		}
		return newsList;
	}
	
	/**
	 * 从数据库中查找文章列表并取最新1或者2条缓存到Redis中
	 * @param dict_group 传入的新闻资讯分组
	 * @return
	 */
	private List<NewsVO>  getNewsListByGroup(String dict_group) {
		List<NewsVO> list = new ArrayList<NewsVO>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("school_id", ActionUtil.getSchoolID());
		map.put("dict_group", dict_group);
		Integer length = DictConstants.NEWS_XXFC_DICT_GROUP.equals(dict_group)?1:2;
		ActionUtil.getActionParam().setLimit(length);
		List<NewsVO> newsList = dao.queryForList("newsMap.getNewsListByGroup", map);
		if (newsList.isEmpty()) return list;
		for (NewsVO newsVO : newsList) {
			newsVO.setDict_group(dict_group);
			addNewsToRedis(newsVO);//存入Redis
			list.add(newsVO);
		}
		return list;
	}
	
	//模糊查询键名，根据键名截取最后6位的dict_group
	private List<String> getNewsListByDictGroup(Integer school_id){
		List<String> dictGroup = new ArrayList<String>();
		DictSchoolVO vo = new DictSchoolVO();
		vo.setDict_group(DictConstants.DCIT_GROUP_NEWS);
		vo.setSchool_id(school_id);
		List<DictSchoolVO> dictList = dao.queryForList("dictSchoolMap.getDictListByGroup", vo);
		for (DictSchoolVO dictVO : dictList) {
			if (dictVO.getDict_code().length() > 6) continue;
			dictGroup.add(dictVO.getDict_code());
		}
		return dictGroup;
	}

	//获取学生所属班级ID
	public Integer getStudentClassID(Integer school_id, Integer student_id) {
		String userKey="USER_INFO:"+RedisKeyUtil.getUnionKey(school_id,DictConstants.USERTYPE_STUDENT,0,student_id);
		boolean flag=jedisDao.hexists(userKey, "class_id");
		if (!flag) {
			StudentVO vo=dao.queryObject("redisMap.getStudentInfo", student_id);//获取该学生的班级ID
			jedisDao.hset(userKey, "class_id", vo.getClass_id().toString());
			return vo.getClass_id();
		} else {
			return Integer.parseInt(jedisDao.hget(userKey, "class_id").trim());
		}
	}

	//redis中修改学生人数（增加学生或删除学生时）
	public void updateStudentCountToRedis(String team_type, Integer group_id,Integer team_id,Integer count) {
		//学生人数
		//班级
		String groupKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		boolean flag=jedisDao.hexists(groupKey, "total003010");//判断redis中是否已有该值
		if (!flag) {
			Integer sum;
			if (DictConstants.TEAM_TYPE_CLASS.equals(team_type))//班级
				sum=dao.queryObject("redisMap.getStudentCountByClass", team_id);//班级学生人数
			else {//寝室
				ReceiveVO rvo=new ReceiveVO();
				rvo.setTeam_id(team_id);
				sum=dao.queryObject("redisMap.getBedRoomCountByTeam",rvo);
			}
			jedisDao.hset(groupKey, "total003010", sum.toString());
		} else {
			jedisDao.hincrBy(groupKey, "total003010", count);//已存在，则value值增加
		}
		if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {//班级
			// 年级
			String gradeKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, 0);
			boolean gradeflag = jedisDao.hexists(gradeKey, "total003010");// 判断redis中是否已有该值
			if (!gradeflag) {
				Integer sum = dao.queryObject("redisMap.getStudentCountByGrade", group_id);
				jedisDao.hset(gradeKey, "total003010", sum.toString());
			} else {
				jedisDao.hincrBy(gradeKey, "total003010", count);
			}
			// 校级
			String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, 0, 0);
			boolean schoolFlag = jedisDao.hexists(schoolKey, "total003010");
			if (!schoolFlag) {
				Integer sum = dao.queryObject("redisMap.getStudentCountBySchool",ActionUtil.getSchoolID());
				jedisDao.hset(schoolKey, "total003010", sum.toString());
			} else {
				jedisDao.hincrBy(schoolKey, "total003010", count);
			}
			updateTotalCount(team_type, group_id, team_id, count);//增加total003的数值
		}
	}

	//增加total003的数值
	private void updateTotalCount(String team_type, Integer group_id,Integer team_id, Integer count) {
		//学生和教师总人数
		//班级
		String groupTotalKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, team_id);
		boolean totalflag=jedisDao.hexists(groupTotalKey, "total003");//判断redis中是否已有该值
		if (!totalflag) {
			int tcount=dao.queryObject("redisMap.getTeacherCountByClassID", team_id);
			int scount=dao.queryObject("redisMap.getStudentCountByClass", team_id);//该班级学生数
			jedisDao.hset(groupTotalKey, "total003", (tcount+scount)+"");
		} else {
			jedisDao.hincrBy(groupTotalKey, "total003", count);
		}
		//年级
		String gradetotalKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, 0);
		boolean gradetotalflag=jedisDao.hexists(gradetotalKey, "total003");//判断redis中是否已有该值
		if (!gradetotalflag) {
			int tcount=dao.queryObject("redisMap.getTeacherCountByGrade", group_id);
			int scount=dao.queryObject("redisMap.getStudentCountByGrade", group_id);//该年级学生数
			jedisDao.hset(gradetotalKey, "total003", (tcount+scount)+"");
		} else {
			jedisDao.hincrBy(gradetotalKey, "total003", count);
		}
		//校级
		String schooltotalKey=RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, 0, 0);
		boolean schooltotalFlag=jedisDao.hexists(schooltotalKey, "total003");
		if (!schooltotalFlag) {
			int tcount=dao.queryObject("redisMap.getTeacherCountBySchool", ActionUtil.getSchoolID());//该校教师数
			int scount=dao.queryObject("redisMap.getStudentCountBySchool", ActionUtil.getSchoolID());
			jedisDao.hset(schooltotalKey, "total003", (tcount+scount)+"");
		} else {
			jedisDao.hincrBy(schooltotalKey, "total003", count);
		}
	}

	// redis中修改教师人数（增加教师或删除教师时）
	public void updateTeacherCountToRedis(String team_type, Integer group_id,Integer team_id) {
		// 教师人数
		// 班级
		String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),team_type, group_id, team_id);
		if (IntegerUtil.isEmpty(group_id) && IntegerUtil.isEmpty(team_id)) {// 全校的教师数
			Integer sum = dao.queryObject("redisMap.getTeacherCountBySchool",ActionUtil.getSchoolID());
			jedisDao.hset(groupKey, "total003005", sum.toString());
			//全校total
			int sschoolcount = dao.queryObject("redisMap.getStudentCountBySchool",ActionUtil.getSchoolID());
			jedisDao.hset(groupKey, "total003", (sum + sschoolcount) + "");
		} else if (IntegerUtil.isNotEmpty(group_id)&& IntegerUtil.isEmpty(team_id)) {
			Integer sum = dao.queryObject("redisMap.getTeacherCountByGrade", group_id);// 年级教师数
			jedisDao.hset(groupKey, "total003005", sum.toString());
			Integer schoolsum = dao.queryObject("redisMap.getTeacherCountBySchool",ActionUtil.getSchoolID());//校教师
			String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, 0, 0);//校key
			jedisDao.hset(schoolKey, "total003005", schoolsum.toString());
			int scount = dao.queryObject("redisMap.getStudentCountByGrade",group_id);// 该年级学生数
			jedisDao.hset(groupKey, "total003", (sum + scount) + "");
			int sschoolcount = dao.queryObject("redisMap.getStudentCountBySchool",ActionUtil.getSchoolID());
			jedisDao.hset(schoolKey, "total003", (schoolsum + sschoolcount)+ "");
		} else if (IntegerUtil.isNotEmpty(group_id)&& IntegerUtil.isNotEmpty(team_id)) {
			Integer sum = dao.queryObject("redisMap.getTeacherCountByClassID", team_id);
			jedisDao.hset(groupKey, "total003005", sum.toString());
			Integer gradesum = dao.queryObject("redisMap.getTeacherCountByGrade", group_id);// 年级教师数
			String gradeKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, group_id, 0);
			jedisDao.hset(gradeKey, "total003005", gradesum.toString());
			String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type, 0, 0);
			Integer schoolsum = dao.queryObject("redisMap.getTeacherCountBySchool",ActionUtil.getSchoolID());
			jedisDao.hset(schoolKey, "total003005", schoolsum.toString());
			int scount = dao.queryObject("redisMap.getStudentCountByClass",team_id);
			jedisDao.hset(groupKey, "total003", (sum + scount) + "");
			int gradecount = dao.queryObject("redisMap.getStudentCountByGrade",group_id);// 该年级学生数
			jedisDao.hset(gradeKey, "total003", (gradesum + gradecount)+ "");
			int sschoolcount = dao.queryObject("redisMap.getStudentCountBySchool",ActionUtil.getSchoolID());
			jedisDao.hset(schoolKey, "total003", (schoolsum + sschoolcount)+ "");
		}
	}
		
	//新添加的学生或教师新的职务，将信息添加到user_group(redis)中
	public void addUserToUserGroup(String user_type,String team_type,Integer group_id,Integer team_id,Integer user_id,
			Integer student_id) {
		String personUnionKey = RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(), user_type,user_id, student_id);
		String userGroupKey = RedisKeyUtil.KEY_GROUP_PRE + personUnionKey;
		if (DictConstants.TEAM_TYPE_CLASS.equals(team_type)) {
			String groupKey1 = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS,
					group_id, team_id);// 所属班级
			jedisDao.sadd(userGroupKey, groupKey1);
			String gradeKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS,
					group_id, 0);
			jedisDao.sadd(userGroupKey, gradeKey);
			// 所属学校
			String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, 0,0);
			jedisDao.sadd(userGroupKey, schoolKey);
		} else {
			String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type,0, team_id);
			jedisDao.sadd(userGroupKey, schoolKey);
		}
	}

	//删除学生时，删除user_group中相应的key
	public void deleteUserFromUserGroup(String user_type,Integer user_id,Integer student_id) {
		String personUnionKey = RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(), user_type,
				user_id, student_id);
		String userGroupKey = RedisKeyUtil.KEY_GROUP_PRE + personUnionKey;
		jedisDao.del(userGroupKey);//删除该学生key
	}

	//删除寝室学生时，删除所属寝室信息
	public void deleteTeamFromUserGroup(String team_type,String user_type,Integer group_id,Integer team_id,Integer user_id,
			Integer student_id) {
		String personUnionKey = RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(), user_type,user_id, student_id);
		String userGroupKey = RedisKeyUtil.KEY_GROUP_PRE + personUnionKey;
		String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), team_type,group_id, team_id);
		jedisDao.srem(userGroupKey, schoolKey);
	}

	//从redis中获取一条通知的接收情况（几人已读，几人回复）
	public List<NoticeCountVO> getNoticeReceiveFromRedis(NoticeVO nrvo) {
		List<NoticeCountVO> nlist=new ArrayList<NoticeCountVO>();
		String DynamicKey = RedisKeyUtil.getNoticeKey(nrvo.getModule_code(), nrvo.getNotice_id(),ActionUtil.getSchoolID());
		boolean flag=jedisDao.hexists(DynamicKey, "is_read");//已读数
		if (!flag) {
			NoticeVO notice=dao.queryObject("noticeMap.getNoticeList",nrvo.getNotice_id());
			NoticeCountVO cvo=new NoticeCountVO();
			cvo.setCount(notice.getReadCount());
			cvo.setNotice_id(nrvo.getNotice_id());
			cvo.setNotice_status(DictConstants.INFO_STATUS_SEEN);
			nlist.add(cvo);
			NoticeCountVO ncvo=new NoticeCountVO();
			ncvo.setCount(notice.getReplyCount());
			ncvo.setNotice_id(nrvo.getNotice_id());
			ncvo.setNotice_status(DictConstants.INFO_STATUS_REPLAY);
			nlist.add(ncvo);
		}else {
			String is_read=jedisDao.hget(DynamicKey, "is_read");//已读数
			String is_reply=jedisDao.hget(DynamicKey, "is_reply");//已回复数
			NoticeCountVO cvo=new NoticeCountVO();
			if (StringUtil.isEmpty(is_read)) cvo.setCount(0);
			else cvo.setCount(Integer.parseInt(is_read.trim()));
			cvo.setNotice_id(nrvo.getNotice_id());
			cvo.setNotice_status(DictConstants.INFO_STATUS_SEEN);
			nlist.add(cvo);
			NoticeCountVO ncvo=new NoticeCountVO();
			if (StringUtil.isEmpty(is_reply)) ncvo.setCount(0);
			else ncvo.setCount(Integer.parseInt(is_reply.trim()));
			ncvo.setNotice_id(nrvo.getNotice_id());
			ncvo.setNotice_status(DictConstants.INFO_STATUS_REPLAY);
			nlist.add(ncvo);
		}
		//JSON形式的字符串，统计信息
		return nlist;
	}

	//更新redis中通知已回复数
	public void updateReplyCountToRedis(NoticeVO notice,NoticeReplyVO vo) {
		String unionKey=RedisKeyUtil.getNoticeUnionKey(notice.getModule_code(),ActionUtil.getUserType(), ActionUtil.getSchoolID(), ActionUtil.getUserID(), ActionUtil.getStudentID())+":NOTICE_ID"+vo.getNotice_id();
		if (!jedisDao.hexists(unionKey, "is_reply")) {
			List<NoticeReplyVO> list;
			NoticeReplyVO reply=new NoticeReplyVO();
			reply.setNotice_id(notice.getNotice_id());
			if (DictConstants.MODULE_CODE_NOTICE.equals(notice.getModule_code())) {//家校
				reply.setUser_type(DictConstants.USERTYPE_PARENT);
				reply.setStudent_id(ActionUtil.getStudentID());
				list=dao.queryForList("noticeReplyMap.getNoticeReplyList",reply);
			} else {//校务
				reply.setUser_type(DictConstants.USERTYPE_TEACHER);
				reply.setUser_id(ActionUtil.getUserID());
				list=dao.queryForList("noticeReplyMap.getNoticeReplyList",reply);
			}
			if (list.size()==1){//redis空的话，查询数据库判断是否是第一次回复
				jedisDao.hincrBy(unionKey, "is_reply", 1);
				String DynamicKey = RedisKeyUtil.getNoticeKey(notice.getModule_code(), vo.getNotice_id(),ActionUtil.getSchoolID());
				if (jedisDao.exists(DynamicKey))
					jedisDao.hincrBy(DynamicKey, "is_reply", 1);//更新通知已回复数
				else
					dao.updateObject("noticeMap.updateReplyCount",vo.getNotice_id());
			}
		}
	}
	
	//添加通知到redis中，用于（未读，已回复，已查看等的统计）
	public void addNoticeToRedis(NoticeVO vo,List<ReceiveVO> list) {
		int teamCount = list.size();
		for (int i=0;i<teamCount;i++) {//添加相关班级的教师
			ReceiveVO Rvo = list.get(i);
			if (IntegerUtil.isEmpty(Rvo.getStudent_id())) {
				Rvo.setSchool_id(ActionUtil.getSchoolID());
				ReceiveVO teacher = new ReceiveVO(ActionUtil.getSchoolID(), Rvo.getTeam_type(), Rvo.getGroup_id(), Rvo.getTeam_id());
				teacher.setUser_type(DictConstants.USERTYPE_TEACHER);
				list.add(teacher);
			} else {
				Rvo.setSchool_id(ActionUtil.getSchoolID());
				ReceiveVO teacher = new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID());
				list.add(teacher);
			}
		}
		if (ListUtil.isEmpty(list)) return;
		HashMap<String,Object> map=new HashMap<String,Object>();
		for (ReceiveVO rvo:list) {
			if (DictConstants.USERTYPE_STUDENT.equals(rvo.getUser_type())) {//学生
				List<StudentVO> slist=new ArrayList<StudentVO>();
				if (IntegerUtil.isNotEmpty(rvo.getStudent_id())) {
					StudentVO student=new StudentVO();
					student.setStudent_id(rvo.getStudent_id());
					slist.add(student);
				} else if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type())) {
					StudentVO svo=new StudentVO();
					svo.setSchool_id(ActionUtil.getSchoolID());
					svo.setGrade_id(rvo.getGroup_id());
					svo.setClass_id(rvo.getTeam_id());
					slist=userService.getStuUserList(svo);
				} else if (DictConstants.TEAM_TYPE_INTEREST.equals(rvo.getTeam_type())) {
					ContactListVO cvo=new ContactListVO();
					cvo.setContact_id(rvo.getTeam_id());
					cvo.setUser_type(DictConstants.USERTYPE_STUDENT);
					cvo.setSchool_id(ActionUtil.getSchoolID());
					slist=dao.queryForList("contactListMap.getContactList", cvo);
				}
				for (StudentVO studentvo:slist) {
					String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),rvo.getUser_type(), ActionUtil.getSchoolID(), null, studentvo.getStudent_id())+":NOTICE_ID"+vo.getNotice_id();
					jedisDao.hset(unionKey, "notice_id", vo.getNotice_id().toString());
					String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),rvo.getUser_type(), ActionUtil.getSchoolID(), null, studentvo.getStudent_id());
					if (!map.containsKey(unreadKey)){
						jedisDao.hincrBy(unreadKey, "un_read", 1);//该学生的家校通知的未读数
						map.put(unreadKey, unreadKey);
					}
				}
			} else if (DictConstants.USERTYPE_TEACHER.equals(rvo.getUser_type())){
				List<Integer> slist=new ArrayList<Integer>();
				if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type()))
					slist=userService.getTeacherUserID(rvo);
				else if (DictConstants.TEAM_TYPE_INTEREST.equals(rvo.getTeam_type()))
					slist=userService.getContactTeacherUserID(rvo.getTeam_id());
				for (Integer user_id:slist) {
					if (IntegerUtil.isEmpty(user_id)) continue;
					String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),rvo.getUser_type(), ActionUtil.getSchoolID(), user_id, null)+":NOTICE_ID"+vo.getNotice_id();
					jedisDao.hset(unionKey, "notice_id", vo.getNotice_id().toString());
					String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),rvo.getUser_type(), ActionUtil.getSchoolID(), user_id, null);
					if (!map.containsKey(unreadKey)){
						jedisDao.hincrBy(unreadKey, "un_read", 1);//该教师的家校通知的未读数
						map.put(unreadKey, unreadKey);
					}
				}
			}
		}
	}
	
	//添加校务通知到redis中，用于（未读，已回复，已查看等的统计）
	public void addSchoolNoticeToRedis(NoticeVO vo,String receivelist,String duty){
		if (StringUtil.isNotEmpty(receivelist)) {//user_id组成
			String[] user_ids=receivelist.split(",");
			for (int i=0;i<user_ids.length;i++) {
				if (StringUtil.isEmpty(user_ids[i])) continue;
				int user_id=Integer.parseInt(user_ids[i].trim());
				if (IntegerUtil.isEmpty(user_id) || ActionUtil.getUserID().equals(user_id)) continue;
				String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), user_id, null)+":NOTICE_ID"+vo.getNotice_id();
				jedisDao.hset(unionKey, "notice_id", vo.getNotice_id().toString());
				String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), user_id, null);
				jedisDao.hincrBy(unreadKey, "un_read", 1);//该教师的家校通知的未读数
			}
		}  else if (DictConstants.DICT_TEACHER.equals(duty)){//全体教师
			TeacherVO tvo=new TeacherVO();
			tvo.setSchool_id(ActionUtil.getSchoolID());
			List<TeacherVO> list = userService.getTeaUserList(tvo);// 符合条件的老师列表
			for (TeacherVO teachervo:list) {
				if (IntegerUtil.isEmpty(teachervo.getUser_id()) || ActionUtil.getUserID().equals(teachervo.getUser_id())) 
					continue;
				String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), teachervo.getUser_id(), null)+":NOTICE_ID"+vo.getNotice_id();
				jedisDao.hset(unionKey, "notice_id", vo.getNotice_id().toString());
				String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), teachervo.getUser_id(), null);
				jedisDao.hincrBy(unreadKey, "un_read", 1);//该教师的家校通知的未读数
			}
		}
	}

	public void addSchoolNoticeToRedisByDuty(NoticeVO vo, List<TeacherVO> list){
		Map<String,Object> map=new HashMap<String, Object>();
		for (TeacherVO teacherVO:list){//遍历集合获取接收者信息
			if (map.containsKey(teacherVO.getPhone())) continue;
			if (IntegerUtil.isNotEmpty(teacherVO.getUser_id()) && !ActionUtil.getUserID().equals
					(teacherVO.getUser_id())) {//只发送给已注册的用户
				String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), teacherVO.getUser_id(), null)+":NOTICE_ID"+vo.getNotice_id();
				jedisDao.hincrBy(unionKey, "notice_id", vo.getNotice_id());
				String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), teacherVO.getUser_id(), null);
				jedisDao.hincrBy(unreadKey, "un_read", 1);//该教师的家校通知的未读数
				map.put(teacherVO.getPhone(),teacherVO);
			}
		}
	}

	//从redis中获取用户通知未读数
	public Integer getUnReadCountFromRedis(String module_code) {
		String unreadKey="";
		if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {
			unreadKey=RedisKeyUtil.getNoticeUnionKey(module_code,DictConstants.USERTYPE_TEACHER, ActionUtil.getSchoolID(), ActionUtil.getUserID(),null);
		} else {
			unreadKey=RedisKeyUtil.getNoticeUnionKey(module_code,DictConstants.USERTYPE_STUDENT, ActionUtil.getSchoolID(),null,ActionUtil.getStudentID());
		}
		String count=jedisDao.hget(unreadKey, "un_read");
		if (StringUtil.isEmpty(count)) return 0;
		else return Integer.parseInt(count.trim());
	}

	//从redis中判断用户是否已读该条通知
	public boolean is_readFromRedis(String unionKey) {
		boolean b=jedisDao.hexists(unionKey, "is_read");
		return b;
	}

	//通知接收者查阅，修改redis中的已读数
	public void updateIs_readCount(NoticeVO vo) {
		String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),ActionUtil.getUserType(), ActionUtil.getSchoolID(), ActionUtil.getUserID(), ActionUtil.getStudentID())+":NOTICE_ID"+vo.getNotice_id();
		if (!jedisDao.hexists(unionKey, "is_read")) {
			NoticeReadVO readVO;
			NoticeReadVO read=new NoticeReadVO();
			read.setNotice_id(vo.getNotice_id());
			if (DictConstants.MODULE_CODE_NOTICE.equals(vo.getModule_code())) {//家校
				read.setUser_type(DictConstants.USERTYPE_STUDENT);
				read.setStudent_id(ActionUtil.getStudentID());
				readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
			} else {//校务
				read.setUser_type(DictConstants.USERTYPE_TEACHER);
				read.setUser_id(ActionUtil.getUserID());
				readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
			}
			if (readVO==null) {//redis空时，从数据库中判断是否是第一次读，是的话，加一条已读记录
				jedisDao.hincrBy(unionKey, "is_read", 1);
				read.setSchool_id(ActionUtil.getSchoolID());
				read.setCreate_by(ActionUtil.getUserID());
				read.setCreate_date(ActionUtil.getSysTime());
				dao.insertObject("noticeReadMap.insertNoticeRead",read);
				String DynamicKey = RedisKeyUtil.getNoticeKey(vo.getModule_code(), vo.getNotice_id(),ActionUtil.getSchoolID());
				if (jedisDao.exists(DynamicKey))
					jedisDao.hincrBy(DynamicKey, "is_read", 1);//更新通知已读数
				else
					dao.updateObject("noticeMap.updateReadCount",vo.getNotice_id());
				String unreadKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),ActionUtil.getUserType(), ActionUtil.getSchoolID(), ActionUtil.getUserID(), ActionUtil.getStudentID());
				if (jedisDao.hexists(unreadKey, "un_read"))
					jedisDao.hincrBy(unreadKey, "un_read", -1);// 更新用户通知未读数
			}
		}
	}

	@Override
	public void deleteNewsList(Map<String, String> paramMap) {
		String DynamicKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_SCHOOLSTYLE, Integer.parseInt(paramMap.get("news_id")), DictConstants.LINK_TYPE_DETAIL);
		jedisDao.del(DynamicKey);
		String newsKey = RedisKeyUtil.getNewsKey(ActionUtil.getSchoolID(),paramMap.get("dict_group"),Integer.parseInt(paramMap.get("news_id")));
		String newsGroupKey = RedisKeyUtil.getNewsDictGroupKey(ActionUtil.getSchoolID(),paramMap.get("dict_group"));
		if (StringUtil.isEmpty(jedisDao.keys(newsKey))) return;
		List<String> newsKeys = jedisDao.lrange(newsGroupKey, 0, -1);
		for (String rediskey : newsKeys) {
			jedisDao.del(rediskey);
		}
		jedisDao.del(newsGroupKey);
		getNewsListByGroup(paramMap.get("dict_group"));
	}

	//从redis中获取学校个推配置
	public GetuiVO getGetuiFromRedis(Integer school_id) {
		 String getuiKey=RedisKeyUtil.getGetuiKey(school_id);
		 boolean flag=jedisDao.exists(getuiKey);
		 if (!flag) {
			 GetuiVO vo=dao.queryObject("getuiMap.getGetuiBySchoolID", school_id);
			 jedisDao.hset(getuiKey, "app_id", vo.getApp_id());
			 jedisDao.hset(getuiKey, "app_key", vo.getApp_key());
			 jedisDao.hset(getuiKey, "app_secret", vo.getApp_secret());
			 jedisDao.hset(getuiKey, "master_secret", vo.getMaster_secret());
			 return vo;
		 } else {
			 GetuiVO vo=new GetuiVO();
			 vo.setApp_id(jedisDao.hget(getuiKey, "app_id"));
			 vo.setApp_key(jedisDao.hget(getuiKey, "app_key"));
			 vo.setApp_secret(jedisDao.hget(getuiKey, "app_secret"));
			 vo.setMaster_secret(jedisDao.hget(getuiKey, "master_secret"));
			 vo.setSchool_id(school_id);
			 return vo;
		 }
	}

	//更新redis中的score记录
	public void addScoreToRedis(Integer school_id) {
		//删除旧的数据
		Set<String> set=jedisDao.keys("SCORE:SCHOOL_ID"+school_id+":*");
		for (String scoreKey:set) 
			jedisDao.del(scoreKey);
		Set<String> set1=jedisDao.keys("RANK:SCHOOL_ID"+school_id+":*");
		for (String rankKey:set1) 
			jedisDao.del(rankKey);
		//学生
		List<ScoreListVO> list=dao.queryForList("scoreListMap.getStuDayScore", school_id);//单个学生的扣分情况
		setStuScoreToRedis(list,DictConstants.SUM_TYPE_DAY,school_id);
		List<ScoreListVO> stuWeekList=dao.queryForList("scoreListMap.getStudentWeekScore", school_id);//学生周记录
		setStuScoreToRedis(stuWeekList,DictConstants.SUM_TYPE_WEEK,school_id);
		List<ScoreListVO> stuMonthList=dao.queryForList("scoreListMap.getStuMonthScore", school_id);
		setStuScoreToRedis(stuMonthList,DictConstants.SUM_TYPE_MONTH,school_id);
		List<ScoreListVO> stuYearList=dao.queryForList("scoreListMap.getStuYearScore", school_id);
		setStuScoreToRedis(stuYearList,DictConstants.SUM_TYPE_YEAR,school_id);
		List<ScoreListVO> stuTermList=dao.queryForList("scoreListMap.getStuTermScore", school_id);
		setStuScoreToRedis(stuTermList,DictConstants.SUM_TYPE_TERM,school_id);
		//班级
		ScoreVO vo=new ScoreVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setScore_type(DictConstants.SCORE_TYPE_ATTEND);
		List<ScoreListVO> teamList=dao.queryForList("scoreListMap.getTeamDayScore", school_id);
		setTeamScoreToRedis(teamList,DictConstants.SUM_TYPE_DAY,school_id);
		List<ScoreVO> teamDayTotalDayList=dao.queryForList("scoreListMap.getTeamDayTotalDay", vo);
		setTeamScoreTotalDayToRedis(teamDayTotalDayList,DictConstants.SUM_TYPE_DAY,school_id);
		List<ScoreListVO> teamWeekList=dao.queryForList("scoreListMap.getTeamWeekScore", school_id);//团队周记录
		setTeamScoreToRedis(teamWeekList,DictConstants.SUM_TYPE_WEEK,school_id);
		List<ScoreVO> teamWeekTotalDayList=dao.queryForList("scoreListMap.getTeamWeekTotalDay", vo);
		setTeamScoreTotalDayToRedis(teamWeekTotalDayList,DictConstants.SUM_TYPE_WEEK,school_id);
		List<ScoreListVO> teamMonthList=dao.queryForList("scoreListMap.getTeamMonthScore", school_id);
		setTeamScoreToRedis(teamMonthList,DictConstants.SUM_TYPE_MONTH,school_id);
		List<ScoreVO> teamMonthTotalDayList=dao.queryForList("scoreListMap.getTeamMonthTotalDay", vo);
		setTeamScoreTotalDayToRedis(teamMonthTotalDayList,DictConstants.SUM_TYPE_MONTH,school_id);
		List<ScoreListVO> teamYearList=dao.queryForList("scoreListMap.getTeamYearScore", school_id);
		setTeamScoreToRedis(teamYearList,DictConstants.SUM_TYPE_YEAR,school_id);
		List<ScoreVO> teamYearTotalDayList=dao.queryForList("scoreListMap.getTeamYearTotalDay", vo);
		setTeamScoreTotalDayToRedis(teamYearTotalDayList,DictConstants.SUM_TYPE_YEAR,school_id);
		List<ScoreListVO> teamTermList=dao.queryForList("scoreListMap.getTeamTermScore", school_id);
		setTeamScoreToRedis(teamTermList,DictConstants.SUM_TYPE_TERM,school_id);
		List<ScoreVO> teamTermTotalDayList=dao.queryForList("scoreListMap.getTeamTermTotalDay", vo);
		setTeamScoreTotalDayToRedis(teamTermTotalDayList,DictConstants.SUM_TYPE_TERM,school_id);
		//年级
		List<ScoreListVO> gradeDayList=dao.queryForList("scoreListMap.getGradeDayScore", school_id);
		setGradeScoreToRedis(gradeDayList,DictConstants.SUM_TYPE_DAY,school_id);
		List<ScoreVO> gradeDayTotalDayList=dao.queryForList("scoreListMap.getGradeDayTotalDay", vo);
		setGradeScoreTotalDayToRedis(gradeDayTotalDayList,DictConstants.SUM_TYPE_DAY,school_id);
		List<ScoreListVO> gradeWeekList=dao.queryForList("scoreListMap.getGradeWeekScore", school_id);
		setGradeScoreToRedis(gradeWeekList,DictConstants.SUM_TYPE_WEEK,school_id);
		List<ScoreVO> gradeWeekTotalDayList=dao.queryForList("scoreListMap.getGradeWeekTotalDay", vo);
		setGradeScoreTotalDayToRedis(gradeWeekTotalDayList,DictConstants.SUM_TYPE_WEEK,school_id);
		List<ScoreListVO> gradeMonthList=dao.queryForList("scoreListMap.getGradeMonthScore", school_id);
		setGradeScoreToRedis(gradeMonthList,DictConstants.SUM_TYPE_MONTH,school_id);
		List<ScoreVO> gradeMonthTotalDayList=dao.queryForList("scoreListMap.getGradeMonthTotalDay", vo);
		setGradeScoreTotalDayToRedis(gradeMonthTotalDayList,DictConstants.SUM_TYPE_MONTH,school_id);
		List<ScoreListVO> gradeYearList=dao.queryForList("scoreListMap.getGradeYearScore", school_id);
		setGradeScoreToRedis(gradeYearList,DictConstants.SUM_TYPE_YEAR,school_id);
		List<ScoreVO> gradeYearTotalDayList=dao.queryForList("scoreListMap.getGradeYearTotalDay", vo);
		setGradeScoreTotalDayToRedis(gradeYearTotalDayList,DictConstants.SUM_TYPE_YEAR,school_id);
		List<ScoreListVO> gradeTermList=dao.queryForList("scoreListMap.getGradeTermScore", school_id);
		setGradeScoreToRedis(gradeTermList,DictConstants.SUM_TYPE_TERM,school_id);
		List<ScoreVO> gradeTermTotalDayList=dao.queryForList("scoreListMap.getGradeTermTotalDay", vo);
		setGradeScoreTotalDayToRedis(gradeTermTotalDayList,DictConstants.SUM_TYPE_TERM,school_id);
	}
		
	//学生扣分记录
	private void setStuScoreToRedis(List<ScoreListVO> list,String sum_type,Integer school_id){
		for (ScoreListVO vo:list) {
			if (StringUtil.isEmpty(vo.getScore_code())) continue;
			String dateKey=getScoreDateKey(vo.getScore_date(),sum_type);
			if (DictConstants.SCORE_TYPE_ATTEND.equals(vo.getScore_type())) {
				String studentKey=RedisKeyUtil.getScoreStudentKeyAttend(school_id, vo.getStudent_id(),
						vo.getAttend_item());
				studentKey+=dateKey;
				jedisDao.hset(studentKey, vo.getScore_code(), vo.getCount().toString());//考勤是存的个数
			} else {
				String studentKey=RedisKeyUtil.getStudentKey(school_id, vo.getStudent_id());
				studentKey+=dateKey;
				jedisDao.hset(studentKey, vo.getScore_code(), (vo.getCount()*vo.getScore())+"");//非考勤存的是分数
			}
		}
	}
		
	//班级扣分记录
	private void setTeamScoreToRedis(List<ScoreListVO> list,String sum_type,Integer school_id){
		for (ScoreListVO svo:list) {
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			String dateKey=getScoreDateKey(svo.getScore_date(),sum_type);
			int group_id=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(svo.getTeam_type())) {
				ClassVO cvo=dao.queryObject("classMap.getClassByID", svo.getTeam_id());
				group_id=cvo.getGrade_id();
			}
			if (DictConstants.SCORE_TYPE_ATTEND.equals(svo.getScore_type())) {//考勤
				String teamKey=RedisKeyUtil.getScoreTeamKeyAttend(school_id, svo.getTeam_type(), svo.getScore_type(),
						group_id, svo.getTeam_id(), svo.getAttend_item());
				teamKey+=dateKey;
				jedisDao.hset(teamKey, svo.getScore_code(), svo.getCount().toString());
			} else {//非考勤
				String teamKey=RedisKeyUtil.getScoreTeamKey(school_id, svo.getTeam_type(), svo.getScore_type(), group_id,
						svo.getTeam_id());
				teamKey+=dateKey;
				jedisDao.hset(teamKey, svo.getScore_code(), (svo.getCount()*svo.getScore())+"");
			}
		}
	}
		
	//班级打分次数
	private void setTeamScoreTotalDayToRedis(List<ScoreVO> list,String sum_type,Integer school_id){
		for (ScoreVO svo:list) {
			String dateKey=getScoreDateKey(svo.getScore_date(),sum_type);
			int group_id=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(svo.getTeam_type())) {
				ClassVO cvo=dao.queryObject("classMap.getClassByID", svo.getTeam_id());
				group_id=cvo.getGrade_id();
			}
			if (DictConstants.SCORE_TYPE_ATTEND.equals(svo.getScore_type())) {//考勤
				String teamKey=RedisKeyUtil.getScoreTeamKeyAttend(school_id, svo.getTeam_type(), svo.getScore_type(),
						group_id, svo.getTeam_id(), svo.getAttend_item());
				teamKey+=dateKey;
				jedisDao.hset(teamKey, "total_day", svo.getCount().toString());
			}
		}
	}
		
	//年级扣分记录
	private void setGradeScoreToRedis(List<ScoreListVO> list,String sum_type,Integer school_id){
		for (ScoreListVO svo:list) {
			if (StringUtil.isEmpty(svo.getScore_code())) continue;
			String dateKey=getScoreDateKey(svo.getScore_date(),sum_type);
			int group_id=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(svo.getTeam_type())) {
				ClassVO cvo=dao.queryObject("classMap.getClassByID", svo.getTeam_id());
				group_id=cvo.getGrade_id();
			}
			if (DictConstants.SCORE_TYPE_ATTEND.equals(svo.getScore_type())) {//考勤
				String teamKey=RedisKeyUtil.getScoreTeamKeyAttend(school_id, svo.getTeam_type(), svo.getScore_type(),
						group_id, 0, svo.getAttend_item());
				teamKey+=dateKey;
				jedisDao.hset(teamKey, svo.getScore_code(), svo.getCount().toString());
			} else {//非考勤
				String teamKey=RedisKeyUtil.getScoreTeamKey(school_id, svo.getTeam_type(), svo.getScore_type(), group_id,
						0);
				teamKey+=dateKey;
				jedisDao.hset(teamKey, svo.getScore_code(), (svo.getCount()*svo.getScore())+"");
			}
		}
	}
		
	//年级打分次数
	private void setGradeScoreTotalDayToRedis(List<ScoreVO> list,String sum_type,Integer school_id){
		for (ScoreVO svo:list) {
			String dateKey=getScoreDateKey(svo.getScore_date(),sum_type);
			int group_id=0;
			if (DictConstants.TEAM_TYPE_CLASS.equals(svo.getTeam_type())) {
				ClassVO cvo=dao.queryObject("classMap.getClassByID", svo.getTeam_id());
				group_id=cvo.getGrade_id();
			}
			if (DictConstants.SCORE_TYPE_ATTEND.equals(svo.getScore_type())) {//考勤
				String teamKey=RedisKeyUtil.getScoreTeamKeyAttend(school_id, svo.getTeam_type(), svo.getScore_type(),
						group_id, 0, svo.getAttend_item());
				teamKey+=dateKey;
				jedisDao.hset(teamKey, "total_day", svo.getCount().toString());
			}
		}
	}
		
	//时间key
	private String getScoreDateKey(String score_date,String sum_type) {
		String key;
		switch (sum_type) {
			case DictConstants.SUM_TYPE_DAY:
				key = ":DAY"+ score_date.replaceAll("-", "");	
				break;
			case DictConstants.SUM_TYPE_WEEK:
				key = ":WEEK"+ DateUtil.getFirstDayOfWeek(score_date);	
				break;
			case DictConstants.SUM_TYPE_MONTH:
				key = ":MONTH"+ DateUtil.getFirstDayOfMonth(score_date);	
				break;
			case DictConstants.SUM_TYPE_TERM:
				key = ":TERM"+ DateUtil.getYearAndTerm(DateUtil.smartFormat(score_date));
				break;
			case DictConstants.SUM_TYPE_YEAR:
				key = ":YEAR"+ DateUtil.getFirstDayOfYear(score_date);
				break;
			default: key = "";
		}
		return key;
	}

	//从redis中获取扣分原因信息
	public ScoreReasonVO getScoreReasonFromRedis(Integer school_id,String score_code,String score_type,String team_type) {
		String codeKey=RedisKeyUtil.getCodeKey(school_id, score_code);
		boolean flag=jedisDao.exists(codeKey);
		if (!flag) {//不存在
			ScoreReasonVO avo=new ScoreReasonVO();
			avo.setScore_code(score_code);
			avo.setSchool_id(school_id);
			avo.setScore_type(score_type);
			if (DictConstants.SCORE_TYPE_ATTEND.equals(score_type))
				avo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
			else avo.setTeam_type(team_type);
			ScoreReasonVO reasonVO=dao.queryObject("scoreReasonMap.getScoreReasonByCode",avo);
			jedisDao.hset(codeKey, "code_name", reasonVO.getScore_reason());
			jedisDao.hset(codeKey, "score", reasonVO.getScore().toString());
			return reasonVO;
		} else {
			ScoreReasonVO avo=new ScoreReasonVO();
			avo.setScore_reason(jedisDao.hget(codeKey, "code_name"));
			avo.setScore(Integer.parseInt(jedisDao.hget(codeKey, "score")));
			return avo;
		}
	}

	//修改redis中扣分原因信息
	public void saveScoreReasonToRedis(Integer school_id, String code,String name, Integer score) {
		String codeKey=RedisKeyUtil.getCodeKey(school_id, code);
		jedisDao.hset(codeKey, "code_name", name);
		jedisDao.hset(codeKey, "score", score.toString());
	}

	public String getHomeworkContent(Integer homework_id) {
		String key = RedisKeyUtil.getHomeworkKey(ActionUtil.getSchoolID(), homework_id,0);
		String content = jedisDao.hget(key, "content");
		if (StringUtil.isNotEmpty(content)) return content;
		content = dao.queryObject("homeworkItemMap.getItemContentByID",homework_id);
		if (content.length()>Constants.DEFAULT_SUBSTRING_LENGTH) {
			content = content.substring(0,Constants.DEFAULT_SUBSTRING_LENGTH);
		}
		jedisDao.hset(key, "content", content);
		return content;
	}

	public void initHomeworkToRedis(Integer school_id) throws ParseException {
		String homeworkSchoolKey = RedisKeyUtil.getHomeworkSchoolKey(school_id);
		Set<String> redisKey = jedisDao.keys(homeworkSchoolKey+"*");
		for (String homeworkKey : redisKey) {
			jedisDao.del(homeworkKey);
		}
		//行政班列表
		List<ClassVO> classList = dao.queryForList("redisMap.getClassListByID", school_id);
		for (ClassVO classVO : classList) {
			classVO.setClass_type(DictConstants.TEAM_TYPE_CLASS);
		}
		//兴趣班列表
		List<ContactVO> contactList = dao.queryForList("redisMap.getContactListByID", school_id);
		for (ContactVO contactVO : contactList) {
			ClassVO vo = new ClassVO();
			vo.setClass_id(contactVO.getContact_id());
			vo.setClass_type(DictConstants.TEAM_TYPE_INTEREST);
			vo.setGrade_id(0);
			vo.setSchool_id(school_id);
			//添加进班级列表
			classList.add(vo);
		}
		for (ClassVO classVO : classList) {
			HomeworkGroupVO homeworkGroup = new HomeworkGroupVO();
			homeworkGroup.setSchool_id(ActionUtil.getSchoolID());
			homeworkGroup.setTeam_id(classVO.getClass_id());
			homeworkGroup.setTeam_type(classVO.getClass_type());
			ActionUtil.getActionParam().setPage_app(false);
			List<HomeworkGroupVO> homeworkList = dao.queryForList("homeworkGroupMap.getHomeworkListByTeamID", homeworkGroup);
			if (ListUtil.isEmpty(homeworkList)) continue;
			for (HomeworkGroupVO homeworkGroupVO : homeworkList) {
				String homrworkKey = RedisKeyUtil.getHomeworkCountKey(school_id, classVO.getGrade_id(), classVO.getClass_id(),classVO.getClass_type(),"");
				String homrworkCourseKey = RedisKeyUtil.getHomeworkCountKey(school_id, classVO.getGrade_id(), classVO.getClass_id(),classVO.getClass_type(),homeworkGroupVO.getCourse());
				Date groupTime = homeworkGroupVO.getCreate_date();
				String groupDay = DateUtil.getNow("yyyyMMdd",groupTime);//天
				String groupWeek = DateUtil.getFirstDayOfWeek(groupTime);//星期
				String groupMonth = DateUtil.getFirstDayOfMonth(groupTime);//月
				String groupTerm = DateUtil.getYearAndTerm(groupTime);//学期
				String groupYear = DateUtil.getFirstDayOfYear(groupDay);//年
				jedisDao.hincrBy(homrworkKey+":DAY"+groupDay, "submit_count", 1);
				jedisDao.hincrBy(homrworkKey+":WEEK"+groupWeek, "submit_count", 1);
				jedisDao.hincrBy(homrworkKey+":MONTH"+groupMonth, "submit_count", 1);
				jedisDao.hincrBy(homrworkKey+":TERM"+groupTerm, "submit_count", 1);
				jedisDao.hincrBy(homrworkKey+":YEAR"+groupYear, "submit_count", 1);		
				jedisDao.hincrBy(homrworkCourseKey+":DAY"+groupDay, "submit_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":WEEK"+groupWeek, "submit_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":MONTH"+groupMonth, "submit_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":TERM"+groupTerm, "submit_count", 1);
				jedisDao.hincrBy(homrworkCourseKey+":YEAR"+groupYear, "submit_count", 1);
				HomeworkReceiveVO homeworkReceiveVO = new HomeworkReceiveVO();
				homeworkReceiveVO.setHomework_id(homeworkGroupVO.getHomework_id());
				homeworkReceiveVO.setCourse(homeworkGroupVO.getCourse());
				homeworkReceiveVO.setSchool_id(school_id);
				List<HomeworkReceiveVO> homeworkReceiveList = dao.queryForList("homeworkReceiveMap.getHomeworkReceiveList", homeworkReceiveVO);
				if (ListUtil.isEmpty(homeworkReceiveList)) continue;
				for (HomeworkReceiveVO homeworkReceive : homeworkReceiveList) {
					//统计班级作业完成情况的键
					String homeworkStudentKey = RedisKeyUtil.getHomeworkStudentCountKey(school_id,classVO.getGrade_id(),classVO.getClass_id(),homeworkReceive.getStudent_id(),classVO.getClass_type(),"");
					//统计班级每个科目作业完成情况的键
					String homeworkStudentCourseKey = RedisKeyUtil.getHomeworkStudentCountKey(school_id,classVO.getGrade_id(),classVO.getClass_id(),homeworkReceive.getStudent_id(),classVO.getClass_type(),homeworkGroupVO.getCourse());
					Date receiveDate = StringUtil.isNotEmpty(homeworkReceiveVO.getEnd_date())?DateUtil.dateTimeFormat(homeworkReceiveVO.getEnd_date()+":00"):DateUtil.dateTimeFormat(dao.queryObject("homeworkMap.getHomeworkEndDateByID", homeworkReceiveVO.getHomework_id())+":00");
					if (DictConstants.TRUE!=DateUtil.compDate(homeworkReceive.getSubmit_time(), receiveDate)) {
						//统计班级准时提交数
						jedisDao.hincrBy(homeworkStudentKey+":DAY"+groupDay, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":WEEK"+groupWeek, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":MONTH"+groupMonth, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":TERM"+groupTerm, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":YEAR"+groupYear, "people_submit", 1);
						jedisDao.hincrBy(homrworkKey+":DAY"+groupDay, "people_count", 1);
						jedisDao.hincrBy(homrworkKey+":WEEK"+groupWeek, "people_count", 1);
						jedisDao.hincrBy(homrworkKey+":MONTH"+groupMonth, "people_count", 1);
						jedisDao.hincrBy(homrworkKey+":TERM"+groupTerm, "people_count", 1);
						jedisDao.hincrBy(homrworkKey+":YEAR"+groupYear, "people_count", 1);
						//统计班级每个科目准时提交数
						jedisDao.hincrBy(homeworkStudentCourseKey+":DAY"+groupDay, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":WEEK"+groupWeek, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":MONTH"+groupMonth, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":TERM"+groupTerm, "people_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":YEAR"+groupYear, "people_submit", 1);
						jedisDao.hincrBy(homrworkCourseKey+":DAY"+groupDay, "people_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":WEEK"+groupWeek, "people_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":MONTH"+groupMonth, "people_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":TERM"+groupTerm, "people_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":YEAR"+groupYear, "people_count", 1);
					} else {
						//统计班级迟交数
						jedisDao.hincrBy(homeworkStudentKey+":DAY"+groupDay, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":WEEK"+groupWeek, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":MONTH"+groupMonth, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":TERM"+groupTerm, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentKey+":YEAR"+groupYear, "late_submit", 1);
						jedisDao.hincrBy(homrworkKey+":DAY"+groupDay, "late_count", 1);
						jedisDao.hincrBy(homrworkKey+":WEEK"+groupWeek, "late_count", 1);
						jedisDao.hincrBy(homrworkKey+":MONTH"+groupMonth, "late_count", 1);
						jedisDao.hincrBy(homrworkKey+":TERM"+groupTerm, "late_count", 1);
						jedisDao.hincrBy(homrworkKey+":YEAR"+groupYear, "late_count", 1);
						//统计班级每个科目迟交数
						jedisDao.hincrBy(homeworkStudentCourseKey+":DAY"+groupDay, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":WEEK"+groupWeek, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":MONTH"+groupMonth, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":TERM"+groupTerm, "late_submit", 1);
						jedisDao.hincrBy(homeworkStudentCourseKey+":YEAR"+groupYear, "late_submit", 1);
						jedisDao.hincrBy(homrworkCourseKey+":DAY"+groupDay, "late_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":WEEK"+groupWeek, "late_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":MONTH"+groupMonth, "late_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":TERM"+groupTerm, "late_count", 1);
						jedisDao.hincrBy(homrworkCourseKey+":YEAR"+groupYear, "late_count", 1);
					}
				}	
			}
		}
	}

	public String getDictValue(String dict_code){
		if (StringUtil.isEmpty(dict_code)) return "";
		String key = RedisKeyUtil.getDictSchoolByKey(ActionUtil.getSchoolID(),dict_code);
		if(jedisDao.exists(key)) return jedisDao.hget(key,"dict_value")+"";
        else {
			DictVO vo = new DictVO();
			vo.setDict_code(dict_code);
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo = dao.queryObject("dictSchoolMap.getDictSchoolInfo",vo);
			if (vo == null) vo = dao.queryObject("dictMap.getDictInfo",dict_code);
			if (vo == null) return "";
            jedisDao.hset(key,"dict_value",vo.getDict_value());
			jedisDao.expire(key,108000);
            return vo.getDict_value()+"";
        }
	}

	public void saveDictValue(String dict_code,String dict_value) {
		if (StringUtil.isEmpty(dict_code) || StringUtil.isEmpty(dict_value)) return;
		String key = RedisKeyUtil.getDictSchoolByKey(ActionUtil.getSchoolID(),dict_code);
		jedisDao.hset(key,"dict_value",dict_value);
	}
	public double getBalanceOfAgent(Integer agent_id){
		if (IntegerUtil.isEmpty(agent_id)) return 0;
		String key = RedisKeyUtil.getBalanceOfAgentKey(agent_id);
		if(jedisDao.exists(key)) return Double.parseDouble(jedisDao.hget(key,"balance"));
		else {
			BalanceVO vo = new BalanceVO();
			vo.setAgent_id(agent_id);
			vo = dao.queryObject("balanceMap.getBalanceByID",vo);
			if (vo == null) return 0;
			jedisDao.hset(key,"balance",vo.getBalance()+"");
			return vo.getBalance();
		}
	}

	public void updateBalanceOfAgent(Integer agent_id,double balance) {
		if (IntegerUtil.isEmpty(agent_id) || balance == 0) return;
		String key = RedisKeyUtil.getBalanceOfAgentKey(agent_id);
		jedisDao.hset(key,"balance",balance+"");
	}

	public String getSchoolName(Integer school_id) {
		if (IntegerUtil.isEmpty(school_id)) return "";
		String schoolNameKey = RedisKeyUtil.getSchoolNameKey(school_id);
		if (jedisDao.exists(schoolNameKey)) {
			return jedisDao.hget(schoolNameKey,"school_name");
		} else {
			SchoolVO vo = schoolService.getSchoolById(school_id);
			if (vo == null) return "";
			jedisDao.hset(schoolNameKey,"school_name",vo.getSchool_name());
			return vo.getSchool_name();
		}
	}

	public String getPayTeamNames(Integer school_id,String user_type,Integer pay_id){
		ActionUtil.getActionParam().setLimit(100);
		String payTeamNameListKey = RedisKeyUtil.getPayTeamNamesKey(school_id, user_type, pay_id);
		if (jedisDao.exists(payTeamNameListKey)) {
			return jedisDao.get(payTeamNameListKey)+"";
		} else {
			String team_names = "";
			if (DictConstants.USERTYPE_ALL.equals(user_type)) {
				team_names = "全校...";
			} else {
				List<TeamVO> teamVOList = dao.queryForList("redisMap.getPayTeamListByID",pay_id);
				for (TeamVO vo : teamVOList) {
					if (StringUtil.isNotEmpty(team_names)) team_names = team_names + "，" + getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id());
					else team_names = getTeamName(vo.getTeam_type(),vo.getGroup_id(),vo.getTeam_id());
				}
			}
			if (StringUtil.isEmpty(team_names)) return "";
			jedisDao.set(payTeamNameListKey,team_names);
			jedisDao.expire(payTeamNameListKey,432000);
			return team_names;
		}
	}

	public List<TeacherVO> getSchoolTeacherRoleCodes(String phone) {
		String roleCodesKey = RedisKeyUtil.getSchoolTeacherRoleCodesKey(ActionUtil.getSchoolID(), DictConstants.USERTYPE_ADMIN, phone);
		if (jedisDao.exists(roleCodesKey)) {
			return BeanUtil.jsonToList(jedisDao.get(roleCodesKey),TeacherVO.class);
		} else {
			Map<String,String> teacherMap = new HashMap<>();
			List<TeacherVO> list = new ArrayList<TeacherVO>();
			List<TeacherVO> teacherList = userService.getTeacherListByPhone(new TeacherVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,phone));
			for (TeacherVO vo:teacherList) {
				if (teacherMap.containsKey(vo.getDuty())) continue;//判断键值是否存在，(存在直接返回)
				teacherMap.put(vo.getDuty(),null);//先将所有接受者存放到HashMap中
				list.add(new TeacherVO(ActionUtil.getSchoolID(),vo.getDuty()));
			}
			if (MapUtils.isEmpty(teacherMap)) return list;
			jedisDao.set(roleCodesKey,BeanUtil.ListTojson(list,false));
			jedisDao.expire(roleCodesKey,108000);
			return list;
		}
	}

	//教师所在职务
	public List<TeacherVO> getTeacherDuty(String phone) {
		String userKey="DUTY:UNION_KEY:SCHOOL_ID"+ActionUtil.getSchoolID()+":PHONE"+phone;
		if (jedisDao.hexists(userKey,"duty")) {
			String duty = jedisDao.hget(userKey, "duty");
			if (StringUtil.isEmpty(duty)) {
				List<TeacherVO> list= new ArrayList<TeacherVO>();
				return list;
			} else return BeanUtil.jsonToList(duty,TeacherVO.class);
		}else {
			String duty="";
			List<TeacherVO> list=userService.getTeacherDuty(phone);
			if (ListUtil.isNotEmpty(list)) duty=BeanUtil.ListTojson(list,false);
			jedisDao.hset(userKey,"duty",duty);
			return list;
		}
	}

	//更新redis中教师所在职务
	public void updateTeacherDuty(String phone) {
		String userKey="DUTY:UNION_KEY:SCHOOL_ID"+ActionUtil.getSchoolID()+":PHONE"+phone;
		List<TeacherVO> list=userService.getTeacherDuty(phone);
		String duty="";
		if (ListUtil.isNotEmpty(list)) duty=BeanUtil.ListTojson(list,false);
		jedisDao.hset(userKey,"duty",duty);
	}
}
