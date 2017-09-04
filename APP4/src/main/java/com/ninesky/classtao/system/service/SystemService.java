package com.ninesky.classtao.system.service;


import com.ninesky.classtao.system.vo.AppVO;
import com.ninesky.classtao.system.vo.MsgVO;

import java.util.List;
import java.util.Map;

public interface SystemService {

	public String getMainView();

	public AppVO getLastVersion(AppVO vo);
	
	public void insertAppVersion(AppVO vo);//增加APP版本
	
	public void delectAppVersion(Integer id);//删除APP版本信息
	
	public void updateAppVersion(AppVO vo);//修改APP版本信息
	
	public void sendAPPMsg(MsgVO vo);//发送APP信息短信邀请用户

	public List<Map<String,Object>> getHeaderDataByTableName(String table_name);//获取数据库表中字段名和注释

	public List<Map<String,Object>> getDataByTableName(Map<String, String> map);//获取数据库表中相应字段的数据

	public List<Map<String,Object>> getServerConfigList();

	public Map<String,String> updateTableData(Map<String, String> map);

	public Map<String,String> deleteTableData(Map<String, String> map);

	public Map<String,String> insertTableData(Map<String, String> map);
}
