package com.ninesky.classtao.login.service;

import com.ninesky.classtao.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class QRcodeContext {
	public static ConcurrentHashMap<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();
	public static ConcurrentHashMap<String, UserVO> userMap = new ConcurrentHashMap<String, UserVO>();
	
	private static Logger logger = LoggerFactory.getLogger(QRcodeContext.class);
	/**
	 * 将Web端生成的唯一uuid作为key，当前线程作为value存放到threadMap中
	 * @param uuid
	 */
	public static void registerSub(String uuid){
		threadMap.put(uuid,Thread.currentThread());
		if (threadMap.containsKey(uuid)) {
			logger.error(uuid+"存放成功！");
		} else {
			logger.error(uuid+"存放失败！");
		}
	}
	
	/**
	 * 将Web端生成的唯一uuid作为key，当前线程作为value存放到threadMap中
	 * @param uuid
	 */
	public static void removeSub(String uuid){
		threadMap.remove(uuid);
		userMap.remove(uuid);
	}
	
	public static void pubToClient(String uuid,UserVO user){
		if (threadMap.containsKey(uuid)) {
			userMap.put(uuid,user);
			threadMap.get(uuid).interrupt();
		} else {
			logger.error(uuid+"对应线程不存在？");
		}
	}
	
	
	public static UserVO getUserInfo(String uuid){
		return userMap.get(uuid);
	}
}
