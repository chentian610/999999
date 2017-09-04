package com.ninesky.classtao.dynamic.service.impl;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.dynamic.vo.DynamicVO;
import com.ninesky.classtao.dynamic.vo.NewInfoVO;
import com.ninesky.classtao.notice.vo.NoticeVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.SchoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class DynamicServiceImpl implements DynamicService {

	@Autowired
	private ClassService classService;

	@Autowired
	private RedisService redisServece;
	
	@Autowired
	private JedisDAO jedisDao;
	
	
	public List<Map<String, Object>> getDynamicList(DynamicVO vo) {
		String personKey = RedisKeyUtil.getUnionKey();
		Set<String> teamSet = redisServece.getTeamKeyList(ActionUtil.getSchoolID(), ActionUtil.getUserType(), ActionUtil.getUserID(), ActionUtil.getStudentID());
		//临时分组Key：将数据放到该key中
		String tempSetKey = RedisKeyUtil.KEY_TEMP_PRE + RedisKeyUtil.KEY_DYNAMIC_PRE + jedisDao.incrBy(RedisKeyUtil.KEY_SEQUENCE_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE, 1);
		for (Object team:teamSet) {
			//存放动态数据的团队Key
			String teamDataKey = RedisKeyUtil.KEY_SET_PRE + RedisKeyUtil.KEY_DYNAMIC_PRE + team;
			jedisDao.zUnionStore(tempSetKey,teamDataKey, tempSetKey);
		}
		//将个人动态添加的临时集合
		long result = jedisDao.zUnionStore(RedisKeyUtil.KEY_SET_PRE+RedisKeyUtil.KEY_DYNAMIC_PRE+personKey,tempSetKey, tempSetKey);
		jedisDao.expire(tempSetKey, 60);
		if (result == 0) return new ArrayList<>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Set<String> IDList = getKeyList(vo, tempSetKey,personKey);
		if (IDList==null) return new ArrayList<>();
		Integer i = 0;
		for (String obj:IDList) {
			if (i!=0 && i.equals(ActionUtil.getLimit())) break;
			Map<String, String> map = jedisDao.hgetAll(obj);
			Boolean hExists = jedisDao.hexists(RedisKeyUtil.KEY_READ_PRE+obj, personKey);
			Iterator<Entry<String, String>> iter = map.entrySet().iterator();
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("is_read", hExists);
			resultMap.put("key", obj);
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String strkey = entry.getKey();
				Object value = entry.getValue();
				resultMap.put(strkey, value);
			}
			list.add(resultMap);  
			i++;
		}
		//connection.del(tempZkey);
		//更新动态最新数据的时间
		double time=getNewInfoTime();
		String infoKey =RedisKeyUtil.KEY_INFO_PRE + RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),ActionUtil.getUserType(),ActionUtil.getUserID(),ActionUtil.getStudentID());
		jedisDao.set(infoKey,time+"");
		return list;
	}
	
	/**
	 * 获取和本人相关的的Key列表
	 * @param vo
	 * @param tempSetkey
	 * @param personKey
	 * @return
	 */
	private Set<String> getKeyList(DynamicVO vo, String tempSetkey, String personKey) {
		long start,end;
		boolean isPullDown = (Constants.IS_PULLDOWN==vo.getDirection());
		//如果下拉刷新
		if (StringUtil.isEmpty(vo.getStart_key())) {
			start = 0;
			end = vo.getLimit()-1;
			isPullDown = false;
		} else if (isPullDown) {
			long keyIndex = getIndexByKey(vo.getStart_key(), tempSetkey);
			//说明下拉刷新没有数据
			if (keyIndex<1) return null;
			start = keyIndex-vo.getLimit()<0?0:keyIndex-vo.getLimit();
			end = keyIndex-1;
		} else {
			long keyIndex = getIndexByKey(vo.getStart_key(), tempSetkey);
			start = keyIndex + 1;
			end = keyIndex +vo.getLimit();
		}
		Set<String> list = new LinkedHashSet<String>();
		while (list.size()<vo.getLimit()) {
			Set<String> IDList = jedisDao.zrevrange(tempSetkey, start, end);
			//没有数据了，跳出循环
			if (IDList==null || IDList.size()==0) break;
			String temp_key = null;//循环到的Key
			for (String obj:IDList) {
				temp_key=obj.toString();
				String key = obj.toString();
				if (checkAuth(key,vo.getModule_code(),vo.isUn_read(),personKey))
					list.add(obj);
			}
			//说明已经没有最新数据了
			if (IDList.size()<vo.getLimit()) break;
			if (isPullDown) {
				long keyIndex = getIndexByKey(temp_key, tempSetkey);
				//说明下拉刷新没有数据
				if (keyIndex<1) break;
				start = keyIndex-vo.getLimit()<0?0:keyIndex-vo.getLimit();
				end = keyIndex-1;
			} else {
				long keyIndex = getIndexByKey(temp_key, tempSetkey);
				if (keyIndex<1) break;
				start = keyIndex + 1;
				end = keyIndex +vo.getLimit();
			}
		}
		return list;
	}

	/**
	 * 校验是否具备该动态的显示权限
	 * @param key
	 * @param module_code
	 * @param isUn_read
	 * @param personKey
	 * @return
	 */
	private boolean checkAuth(String key, String module_code, boolean isUn_read, String personKey) {
		String user_type = jedisDao.hget(key, "user_type");
		if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {
			if (DictConstants.USERTYPE_STUDENT.equals(user_type)) return false;
			Integer user_id = IntegerUtil.getValue(jedisDao.hget(key, "user_id"));
			if (IntegerUtil.isNotEmpty(user_id) && !ActionUtil.getUserID().equals(user_id))
				return false;
		} else {
			if (DictConstants.USERTYPE_TEACHER.equals(user_type)) return false;
			Integer student_id = IntegerUtil.getValue(jedisDao.hget(key, "student_id"));
			if (IntegerUtil.isNotEmpty(student_id) && !ActionUtil.getStudentID().equals(student_id))
				return false;
		}
		//将不是要求的模块过滤
		if (StringUtil.isNotEmpty(module_code)) {
			String[] codes = module_code.split(",");
			List<String> list = Arrays.asList(codes);
			if (!list.contains(jedisDao.hget(key, "module_code")))
				return false;
		}
		//将已读的过滤
		if (isUn_read && jedisDao.hexists(RedisKeyUtil.KEY_READ_PRE+key, personKey))
			return false;
		return true;
	}
	
	private long getIndexByKey(String start_key,String tempSetkey) {
		long keyIndex;
		//获取到Key的集合
		try	{
			 keyIndex = jedisDao.zrevrank(tempSetkey, start_key);
		} catch (Exception e) {
			keyIndex = -1;
		}
		return keyIndex;
	}
	
	@Override
	public void updateReadFlag(final String key) {
		String unionKey = RedisKeyUtil.getUnionKey();
		String readKey = key+RedisKeyUtil.KEY_READ_PRE;
		if (jedisDao.hexists(readKey, unionKey)) return;
		jedisDao.hset(readKey, unionKey, ActionUtil.getSysTime().toString());
		//将对应业务的已读数加1，未读数减1
		jedisDao.hincrBy(key, "un_read_num", -1);
		jedisDao.hincrBy(key, "read_num", 1);
	}

	@Override
	public void insertDynamic(HashMap<String, String> dataMap,List<ReceiveVO> receivelist) {
		if (ListUtil.isEmpty(receivelist)) return;
		//添加默认合作伙伴类型，方便APP拉起不同的模块
		dataMap.put(Constants.PARTNER_CODE, SchoolConfig.getProperty(Constants.PARTNER_CODE));
		String DynamicKey = RedisKeyUtil.getDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")), dataMap.get("link_type"));
		jedisDao.hsetAll(DynamicKey, dataMap);
		for (ReceiveVO vo:receivelist) {
			//设置对应年级ID
			if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && vo.getGroup_id() == null)
				vo.setGroup_id(classService.getClassByID(vo.getTeam_id()).getGrade_id());
			Integer total = 0;
			String DynamicKeySet = null;
			//发送给团队的
			if (IntegerUtil.isEmpty(vo.getUser_id()) && IntegerUtil.isEmpty(vo.getStudent_id()))
			{
				DynamicKeySet =	RedisKeyUtil.getGroupDynamicSetKey(vo);
				total = redisServece.getTotalFromTeam(vo);
			} else {
				DynamicKeySet = RedisKeyUtil.getUnionDynamicSetKey(vo.getSchool_id(), vo.getUser_type(), vo.getUser_id(), vo.getStudent_id());
				total = 1;
			}
			jedisDao.hincrBy(DynamicKey, "total", total);
			jedisDao.hincrBy(DynamicKey, "un_read_num", total);
			//将该条动态加入到分组动态中 
			jedisDao.zadd(DynamicKeySet, ActionUtil.getSysTime().getTime(),DynamicKey);
		}
	}

	@Override
	public void insertSingleDynamic(HashMap<String, String> dataMap,ReceiveVO vo) {
		if (vo == null) return;
		//添加默认合作伙伴类型，方便APP拉起不同的模块
		dataMap.put(Constants.PARTNER_CODE, SchoolConfig.getProperty(Constants.PARTNER_CODE));
		String DynamicKey = RedisKeyUtil.getSingleDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")), vo.getUser_id(),dataMap.get("link_type"));
		jedisDao.hsetAll(DynamicKey, dataMap);
		//设置对应年级ID
		if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type()) && vo.getGroup_id() == null)
			vo.setGroup_id(classService.getClassByID(vo.getTeam_id()).getGrade_id());
		Integer total = 0;
		String DynamicKeySet = null;
		//发送给团队的
		if (IntegerUtil.isEmpty(vo.getUser_id()) && IntegerUtil.isEmpty(vo.getStudent_id()))
		{
			DynamicKeySet =	RedisKeyUtil.getGroupDynamicSetKey(vo);
			total = redisServece.getTotalFromTeam(vo);
		} else {
			DynamicKeySet = RedisKeyUtil.getUnionDynamicSetKey(vo.getSchool_id(), vo.getUser_type(), vo.getUser_id(), vo.getStudent_id());
			total = 1;
		}
		jedisDao.hincrBy(DynamicKey, "total", total);
		jedisDao.hincrBy(DynamicKey, "un_read_num", total);
		//将该条动态加入到分组动态中
		jedisDao.zadd(DynamicKeySet, ActionUtil.getSysTime().getTime(),DynamicKey);
	}

    @Override
    public void removeSingleDynamic(HashMap<String, String> dataMap,ReceiveVO vo) {
        if (vo == null) return;
        String DynamicKey = "";
        if (DictConstants.LEAVE_STATUS_NEW.equals(dataMap.get("info_leave_status")))
            DynamicKey = RedisKeyUtil.getSingleDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")), vo.getUser_id(),dataMap.get("link_type"));
        else DynamicKey = RedisKeyUtil.getDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")),dataMap.get("link_type"));
        jedisDao.del(DynamicKey);
        String DynamicKeySet = RedisKeyUtil.getUnionDynamicSetKey(vo.getSchool_id(), vo.getUser_type(), vo.getUser_id(), vo.getStudent_id());
        jedisDao.zrem(DynamicKeySet,DynamicKey);
    }

    @Override
    public void removeDynamicList(HashMap<String, String> dataMap,List<ReceiveVO> receivelist) {
        if (ListUtil.isEmpty(receivelist)) return;
        String DynamicKey = "";
        for (ReceiveVO vo:receivelist) {
            if (DictConstants.LEAVE_STATUS_NEW.equals(dataMap.get("info_leave_status")))
                DynamicKey = RedisKeyUtil.getSingleDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")), vo.getUser_id(), dataMap.get("link_type"));
            else
                DynamicKey = RedisKeyUtil.getDynamicKey(dataMap.get("module_code"), Integer.parseInt(dataMap.get("module_pkid")), dataMap.get("link_type"));
            jedisDao.del(DynamicKey);
            String DynamicKeySet = RedisKeyUtil.getUnionDynamicSetKey(vo.getSchool_id(), vo.getUser_type(), vo.getUser_id(), vo.getStudent_id());
            jedisDao.zrem(DynamicKeySet, DynamicKey);
        }
    }

	
	@Override
	public void insertDynamicByStuID(HashMap<String, String> dataMap,Integer student_id) {
		//添加默认合作伙伴类型，方便APP拉起不同的模块
		dataMap.put(Constants.PARTNER_CODE, SchoolConfig.getProperty(Constants.PARTNER_CODE));
		String link_type = dataMap.get("link_type").toString();
		String module_code = dataMap.get("module_code").toString();
		Integer pkID = Integer.parseInt(dataMap.get("module_pkid"));
		String DynamicKey = RedisKeyUtil.getDynamicKey(module_code, pkID, "STUDENT_ID:"+student_id);
		jedisDao.hsetAll(DynamicKey, dataMap);
		String zsetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,null,student_id);
		jedisDao.zadd(zsetKey,ActionUtil.getSysTime().getTime(),DynamicKey);
	}
	
	/**
	 * 更新Redis回复汇总数，更新动态的回复条数，更新通知的回复人数
	 * 业务模块将需要更新的字段、操作类型等封装到Map中
	 * @param notice 通知对象，有标题、学校ID、发送人用户类型、发送人ID等关键信息
	 * @param reply_content：回复内容，生成动态的时候使用
	 * @return 返回更新掉的通知回复人员数（每个人员只能算一次）
	 */
	@Override
	public void updateDynamicReply(NoticeVO notice,String reply_content) {
		String module_code = notice.getModule_code();
		Integer module_pkid = notice.getNotice_id();
		//回复动态Key
		String replyKey = RedisKeyUtil.getDynamicKey(module_code, module_pkid, DictConstants.LINK_TYPE_REPLY);
		long reply_num = jedisDao.hincrBy(replyKey, "reply_num", 1);//回复条数加1
		//redisHash.putIfAbsent(replyKey, "module_code", module_code);
		jedisDao.hsetnx(replyKey, "module_code", module_code);
		jedisDao.hsetnx(replyKey, "module_pkid", module_pkid.toString());
		jedisDao.hset(replyKey, "info_title", MsgService.getMsg("DYNAMIC_NOTICE_REPLY", StringUtil.subString(notice.getNotice_title(),15),reply_num));
		jedisDao.hset(replyKey, "info_content", StringUtil.subString(reply_content, 20));
		jedisDao.hsetnx(replyKey, "link_type", DictConstants.LINK_TYPE_DETAIL);
		jedisDao.hsetnx(replyKey, "info_url", "detail.html");
		jedisDao.hset(replyKey, "info_date", ActionUtil.getSysTime().getTime()+"");
		jedisDao.hset(replyKey, "user_type", DictConstants.USERTYPE_TEACHER);
		jedisDao.hset(replyKey, "user_id", notice.getSender_id().toString());
		String senderSetKey = RedisKeyUtil.getUnionDynamicSetKey(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,notice.getSender_id(),null);
		jedisDao.zadd(senderSetKey,  ActionUtil.getSysTime().getTime(),replyKey);
	}

	/**
	 * 更新动态信息
	 * @param redisKey 动态的key
	 * @param map 需要替换的参数
	 * @return
	 */
	public void updateDynamic(String redisKey, HashMap<String, String> map) {
		jedisDao.hsetAll(redisKey, map);
	}

	//true表示有新数据，false表示没有新数据
	public NewInfoVO haveNewInfo(){
		NewInfoVO vo=new NewInfoVO();
		vo.setHaveNewInfo(false);
		//存放是否有新动态的个人key
		String infoKey =RedisKeyUtil.KEY_INFO_PRE + RedisKeyUtil.getUnionKey(ActionUtil.getSchoolID(),ActionUtil.getUserType(),ActionUtil.getUserID(),ActionUtil.getStudentID());
		double i=getNewInfoTime();//获取最新的动态时间
		String lastTime=jedisDao.get(infoKey);
		if (StringUtil.isEmpty(lastTime) && i>0) vo.setHaveNewInfo(true);//第一次
		if (StringUtil.isNotEmpty(lastTime) && i>Double.parseDouble(lastTime.trim())) vo.setHaveNewInfo(true);
		return vo;
	}

	//获取最新的动态时间
	private Double getNewInfoTime(){
		String personKey = RedisKeyUtil.getUnionKey();//个人key
		Set<String> teamSet = redisServece.getTeamKeyList(ActionUtil.getSchoolID(), ActionUtil.getUserType(), ActionUtil.getUserID(), ActionUtil.getStudentID());
		double i=0;
		for (Object team:teamSet) {
			//存放动态数据的团队Key
			String teamDataKey = RedisKeyUtil.KEY_SET_PRE + RedisKeyUtil.KEY_DYNAMIC_PRE + team;
			Set<String> dynamicSet=jedisDao.zrevrange(teamDataKey,0,-1);
			if (dynamicSet.size()>0){
				double time=jedisDao.zscore(teamDataKey,dynamicSet.toArray()[0].toString());//最新的时间
				if (time>i) i=time;
			}
		}
		//存放动态数据的个人key
		String personDataKey=RedisKeyUtil.KEY_SET_PRE + RedisKeyUtil.KEY_DYNAMIC_PRE +personKey;
		Set<String> personDynamicSet=jedisDao.zrevrange(personDataKey,0,-1);
		if (personDynamicSet.size()>0) {
			double personTime=jedisDao.zscore(personDataKey,personDynamicSet.toArray()[0].toString());//最新的时间
			if (personTime>i) i=personTime;
		}
		return i;
	}
}
