package com.ninesky.classtao.redis.service;

import com.ninesky.classtao.getui.vo.GetuiVO;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.notice.vo.NoticeCountVO;
import com.ninesky.classtao.notice.vo.NoticeReplyVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.classtao.score.vo.ScoreReasonVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.vo.ReceiveVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface RedisService {
	/**
	 * 从Redis中获取班级（寝室）等团队的总人数，如果Redis中没有这个Key，那么生成总人数并缓存下来
	 * @param vo
	 * @return
	 */
	public Integer getTotalFromTeam(ReceiveVO vo);
	
	/**
	 * 从Redis中获取个人的班级列表
	 * 如果为空，那么从数据库中获取数据，并创建Key
	 * @return
	 */
	public Set<String> getTeamKeyList(Integer school_id,String user_type,Integer user_id,Integer student_id);

	/**
	 * 从redis中获取团队名称（年级名称，班级名称）
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @return
	 */
	public String getTeamName(String team_type,Integer group_id,Integer team_id);
	
	/**
	 * 从redis中获取相应年级或班级,寝室的学生人数
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @return
	 */
	public Integer getTeamStudentCount(String team_type,Integer group_id,Integer team_id);
	
	/**
	 * 从redis中获取相应年级或班级,寝室的教师人数
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @param team_type
	 * @param group_id
	 * @param team_id
	 * @return
	 */
	public Integer getTeamTeacherCount(String team_type,Integer group_id,Integer team_id);
	
	/**
	 * 从redis中获取相应年级或班级，寝室的教师，学生总人数
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @param team_type
	 * @param group_id
	 * @param team_id
	 * @return
	 */
	public Integer getTeamTotalCount(String team_type,Integer group_id,Integer team_id);
	
	/**
	 * 从redis中获取教师或学生的姓名
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @param school_id
	 * @param user_type
	 * @param user_id
	 * @param student_id
	 * @return
	 */
	public String getUserName(Integer school_id,String user_type,Integer user_id,Integer student_id);
	
	/**
	 * 从redis中获取教师的电话号码
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @param school_id
	 * @param user_type
	 * @param user_id
	 * @return
	 */
	public String getUserPhone(Integer school_id, String user_type,Integer user_id);
	
	/**
	 * 修改redis中教师或学生的姓名
	 * @param school_id
	 * @param user_type
	 * @param user_id
	 * @param student_id
	 * @param user_name
	 */
	public void updateUserName(Integer school_id,String user_type,Integer user_id,Integer student_id,String user_name);

	/**
	 * 从redis中获取教师或学生的头像
	 * 如果为空，那么从数据库中获取数据，并创建key
	 * @param school_id
	 * @param user_type
	 * @param user_id
	 * @param student_id
	 * @return
	 */
	public String getUserHeadUrl(Integer school_id,String user_type,Integer user_id,Integer student_id);

	//获取用户姓名首字母
	public String getAllLetter(Integer school_id,String user_type,Integer user_id,Integer student_id);

	//获取用户姓名全拼
	public String getFirstLetter(Integer school_id,String user_type,Integer user_id,Integer student_id);
	
	/**
	 * 修改redis中教师或学生的头像url
	 * @param school_id
	 * @param user_type
	 * @param user_id
	 * @param student_id
	 * @param head_url
	 */
	public void updateHeadUrl(Integer school_id,String user_type,Integer user_id,Integer student_id,String head_url);
	
	/**
	 * 获取学生所属班级ID
	 */
	public Integer getStudentClassID(Integer school_id,Integer student_id);
	
	/**
	 * redis中修改学生人数（增加学生或删除学生时）
	 * @param count 增加数或减少数
	 */
	public void updateStudentCountToRedis(String team_type,Integer group_id,Integer team_id,Integer count);
	
	//修改redis中教师人数
	public void updateTeacherCountToRedis(String team_type,Integer group_id,Integer team_id);
	
	/**
	 * 新添加的学生，将信息添加到user_group(redis)中
	 */
	public void addUserToUserGroup(String user_type,String team_type,Integer group_id,Integer team_id,Integer user_id,
			Integer student_id);
	
	/**
	 * 删除学生时，删除user_group中相应的key
	 */
	public void deleteUserFromUserGroup(String user_type,Integer user_id,Integer student_id);
	
	/**
	 * 删除寝室学生时，删除所属寝室信息
	 */
	public void deleteTeamFromUserGroup(String team_type,String user_type,Integer group_id,Integer team_id,
			Integer user_id,Integer student_id);
	
	/**
	 * 保存两条最新的校园新闻信息
	 */
	public void addNewsToRedis(NewsVO vo);
	
	/**
	 * 獲取两条最新的校园新闻信息
	 */
	public List<NewsVO> getNewsList(Integer school_id);
	
	/**
	 * 从redis中获取一条通知的接收情况（几人已读，几人回复）
	 */
	public List<NoticeCountVO> getNoticeReceiveFromRedis(NoticeVO nrvo);
	
	/**
	 * 更新redis中通知已回复数
	 */
	public void updateReplyCountToRedis(NoticeVO notice,NoticeReplyVO vo);
	
	/**
	 * 添加通知到redis中，用于（未读，已回复，已查看等的统计）
	 */
	public void addNoticeToRedis(NoticeVO vo,List<ReceiveVO> list);
	
	//public void addNoticeToRedisByStuID(NoticeVO vo, String student_list);//可以发给某一个学生
	
	/**
	 * 添加校务通知到redis中，用于（未读，已回复，已查看等的统计）
	 */
	public void addSchoolNoticeToRedis(NoticeVO vo,String receivelist,String duty);

	public void addSchoolNoticeToRedisByDuty(NoticeVO vo, List<TeacherVO> list);
	
	/**
	 * 从redis中获取用户通知未读数
	 */
	public Integer getUnReadCountFromRedis(String module_code);
	
	/**
	 * 从redis中判断用户是否已读该条通知
	 */
	public boolean is_readFromRedis(String unionKey);
	
	/**
	 * 通知接收者查阅，修改redis中的已读数
	 */
	public void updateIs_readCount(NoticeVO vo);
	
	/**
	 * 后台页面清理新闻信息时，删除了首页页面中所包含的新闻信息时执行
	 * @param paramMap
	 */
	public void deleteNewsList(Map<String, String> paramMap);
	
	/**
	 * 从redis中获取个推配置
	 */
	public GetuiVO getGetuiFromRedis(Integer school_id);
	
	/**
	 * 更新redis中score的记录信息
	 */
	public void addScoreToRedis(Integer school_id);
	
	/**
	 * 从redis中获取扣分原因信息
	 */
	public ScoreReasonVO getScoreReasonFromRedis(Integer school_id, String score_code, String score_type, String team_type);
	
	/**
	 * 修改redis中扣分原因信息
	 */
	public void saveScoreReasonToRedis(Integer school_id,String code,String name,Integer score);

	/**
	 * 从redis获取作业信息
	 * @param homework_id
	 * @return
	 */
	public String getHomeworkContent(Integer homework_id);

	/**
	 * 初始化作业统计数据到redis
	 * @param school_id
	 * @throws ParseException
	 */
	public void initHomeworkToRedis(Integer school_id) throws ParseException;

	/**
	 * 从redis获取dict_value
	 * @param
	 * @return
	 */
	public String getDictValue(String dict_code);

	/**
	 * 修改redis参数
	 * @param dict_code
	 * @param dict_value
	 */
	public void saveDictValue(String dict_code,String dict_value);

	/**
	 * 从Redis中获取代理商当前余额
	 * @param agent_id 代理商ID
	 * @return
	 */
	public double getBalanceOfAgent(Integer agent_id);

	/**
	 * 修改相应代理商余额
	 * @param agent_id
	 * @param balance
	 */
	public void updateBalanceOfAgent(Integer agent_id,double balance);

	/**
	 * 获取学校名称
	 * @param school_id
	 * @return
	 */
	public String getSchoolName(Integer school_id);

	/**
	 * 获取缴费团队名称合集
	 * @param school_id
	 * @param user_type
	 * @param pay_id
	 * @return
	 */
	public String getPayTeamNames(Integer school_id,String user_type,Integer pay_id);

	/**
	 * 获取学校教师角色合集
	 * @param phone
	 * @return
	 */
	public List<TeacherVO> getSchoolTeacherRoleCodes(String phone);

	public List<TeacherVO> getTeacherDuty(String phone);//教师所在职务

	public void updateTeacherDuty(String phone);//更新redis中教师所在职务
}