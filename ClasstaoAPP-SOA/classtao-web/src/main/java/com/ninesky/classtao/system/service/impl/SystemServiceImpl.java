package com.ninesky.classtao.system.service.impl;


import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ninesky.common.util.ListUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.system.service.SystemService;
import com.ninesky.classtao.system.vo.AppVO;
import com.ninesky.classtao.system.vo.MsgVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.common.util.StringUtil;

@Service("systemServiceImpl")
public class SystemServiceImpl implements SystemService {

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;
	
	public String getMainView() {
		return dao.queryObject("systemMap.getMainUrl",ActionUtil.getSchoolID());
	}

	public AppVO getLastVersion(AppVO vo) {
		// TODO Auto-generated method stub
		return dao.queryObject("appMap.getAppVersion",vo);
	}

	//增加APP版本
	public void insertAppVersion(AppVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("appMap.insertApp", vo);
	}

	//删除APP版本信息
	public void delectAppVersion(Integer id) {
		dao.deleteObject("appMap.deleteApp", id);
	}

	//修改APP版本信息
	public void updateAppVersion(AppVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("appMap.updateApp", vo);
	}

	//发送APP信息短信邀请用户
	public void sendAPPMsg(MsgVO vo) {
		List<String> phoneList=new ArrayList<String>();
		if(DictConstants.USERTYPE_PARENT.equals(vo.getUser_type())){//发送给所有家长
			phoneList=userService.getParentPhoneList();
		}else if(DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())){//发送给全体教师
			phoneList=userService.getTeacherPhoneList();
		}else{//发送给全体教师和家长
			phoneList=userService.getParentPhoneList();
			List<String> list=userService.getTeacherPhoneList();
			phoneList.addAll(list);
		}
		SchoolVO svo=dao.queryObject("schoolMap.getSchoolInfo", ActionUtil.getSchoolID());
		for(String phone:phoneList){
			if (StringUtil.isEmpty(phone)) continue;
			messageService.sendMessage(phone, vo.getText()+"  ("+svo.getSchool_name()+")");
		}
	}

	public List<Map<String,Object>> getHeaderDataByTableName(String table_name){return dao.queryForList("systemMap.getHeaderDataByTableName",table_name);}

	public List<Map<String,Object>> getDataByTableName(Map<String,String> map){return dao.queryForList("systemMap.getDataByTableName",map);}

	public List<Map<String,Object>> getServerConfigList(){
		return dao.queryForList("systemMap.getServerConfigList");
	}

	public Map<String,String> updateTableData(Map<String,String> map){
		if (StringUtil.isEmpty(map.get("column_list"))) return map;
		List<Map<String,Object>> column_list = jsonToMapList(map.get("column_list"));
		for (Map<String,Object>  column : column_list) {
			map.put("column_name",column.get("column_name").toString());
			map.put("column_value",column.get("column_value").toString());
			dao.queryForList("systemMap.updateTableData",map);
		}
		return map;
	}

	public Map<String,String> deleteTableData(Map<String,String> map){
		dao.queryForList("systemMap.deleteTableData",map);
		return map;
	}

	public Map<String,String> insertTableData(Map<String,String> map){
		String[] keys = map.get("insert_field").split(",");
		Map<String,Object> mapData = BeanUtil.jsonToMap(map.get("data"));
		for (String key : keys) {
			String value = map.get("insert_data");
			if (StringUtil.isNotEmpty(value)){
				map.put("insert_data",value+",'"+mapData.get(key).toString()+"'");
			} else {
				map.put("insert_data","'"+mapData.get(key).toString()+"'");
			}
		}
		dao.queryForList("systemMap.insertTableData",map);
		return map;
	}

	public List<Map<String,Object>> jsonToMapList(String jsonStr) {
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		if (StringUtil.isEmpty(jsonStr)) return mapList;
		JSONArray array = JSONArray.parseArray(jsonStr);
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = (JSONObject) array.get(i);
			Map<String,Object> map = new HashMap<String ,Object>();
			map.put("column_name",json.getString("column_name"));
			map.put("column_value",json.getString("column_value"));
			mapList.add(map);
		}
		return mapList;
	}
}
