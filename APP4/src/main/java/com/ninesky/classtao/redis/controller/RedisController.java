package com.ninesky.classtao.redis.controller;

import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value="redisAction")
public class RedisController extends BaseController{
	
	@Autowired
	private GeneralDAO dao;
	

	@Autowired
	private RedisService redisService;

	/**
	 * 将mysql中的数据根据不同时间类别存入redis中
	 * @return
	 */
	@RequestMapping(value="/addscoreToRedis")
	public Object getStudentCount(){
		redisService.addScoreToRedis(ActionUtil.getSchoolID());
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 将mysql中的数据根据不同时间类别存入redis中
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/initHomeworkToRedis")
	public Object initHomeworkToRedis() throws ParseException{
		redisService.initHomeworkToRedis(ActionUtil.getSchoolID());
		return ResponseUtils.sendSuccess();
	}
}
