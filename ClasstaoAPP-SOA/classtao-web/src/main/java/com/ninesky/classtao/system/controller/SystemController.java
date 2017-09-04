package com.ninesky.classtao.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.common.util.ActionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.service.SystemService;
import com.ninesky.classtao.system.vo.AppVO;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.system.vo.MsgVO;
import com.ninesky.common.DictConstants;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import com.ninesky.framework.SchoolConfig;
import com.ninesky.framework.SystemConfig;

@RestController
@RequestMapping(value="systemAction")
public class SystemController extends BaseController{
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value="getMainView")
	@ResultField(includes={"url"})
	public @ResponseBody Object getMainView() throws Exception{
		return systemService.getMainView();
	}
	
	@RequestMapping(value="getLastVersion")
	@ResultField(includes={"all_version","all_url","min_version","min_url","directional_version","directional_url","directional_phone"})
	public Object getLastVersion() throws Exception{
		AppVO vo = BeanUtil.formatToBean(AppVO.class);
		vo.setIs_disable(DictConstants.FALSE);
		vo.setIs_all(DictConstants.TRUE);
		AppVO all = systemService.getLastVersion(vo);
		vo.setIs_all(DictConstants.FALSE);
		AppVO min = systemService.getLastVersion(vo);
		vo.setIs_all(DictConstants.DIRECTIONAL_UPDATE);
		AppVO directionalUpdate = systemService.getLastVersion(vo);
		HashMap<String,String> map = new HashMap<String,String>();
		if (all==null) {
			map.put("all_version", "1.0.0");
			map.put("all_url", null);
			if (min!=null)
			{
				map.put("min_version", min.getApp_version());
				map.put("min_url", min.getUpdate_url());
			}
			if (directionalUpdate!=null) {
				map.put("directional_version", directionalUpdate.getApp_version());
				map.put("directional_url", directionalUpdate.getUpdate_url());
				map.put("directional_phone", directionalUpdate.getUpdate_phone());
			}
			return map;
		}
		map.put("all_version", all.getApp_version());
		map.put("all_url", all.getUpdate_url());
		if (min !=null && all.getCreate_date().getTime()<min.getCreate_date().getTime())
		{
			map.put("min_version", min.getApp_version());
			map.put("min_url", min.getUpdate_url());
		}
		if (directionalUpdate !=null && all.getCreate_date().getTime()<directionalUpdate.getCreate_date().getTime())
		{
			map.put("directional_version", directionalUpdate.getApp_version());
			map.put("directional_url", directionalUpdate.getUpdate_url());
			map.put("directional_phone", directionalUpdate.getUpdate_phone());
		}
		return map;
	}
	
	/**
	 * 添加APP版本
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addAppVersion")
	public @ResponseBody Object addAppVersion(HttpServletRequest request){
		AppVO vo = BeanUtil.formatToBean(AppVO.class);
		systemService.insertAppVersion(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 删除APP版本信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="deleteAppVersion")
	public @ResponseBody Object deleteAppVersion(HttpServletRequest request){
		int id=Integer.parseInt(request.getParameter("id"));
		systemService.delectAppVersion(id);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 更新APP版本信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateAppVersion")
	public @ResponseBody Object updateAppVersion(HttpServletRequest request){
		AppVO vo = BeanUtil.formatToBean(AppVO.class);
		systemService.updateAppVersion(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获取数据字典
	 * @param request
	 * @return
	 */
	@GetCache(name="getDictionary",value="dict_group")
	@RequestMapping(value="getDictionary")
	@ResultField(excludes={"app_sql","order_sql","start_time","end_time","start","limit","direction","create_by","create_date","update_by","update_date"})
	public Object getDictionary(HttpServletRequest request){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		return dictService.getDictList(vo);
	}
	
	/**
	 * 添加数据字典
	 * @param
	 * @return
	 */
	@RequestMapping(value="addDictionary")
	public @ResponseBody Object addDictionary(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		vo=dictService.addDictionary(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 删除数据字典
	 * @return
	 */
	@RequestMapping(value="deleteDictionary")
	public @ResponseBody Object deleteDictionary(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		dictService.deleteDictionary(vo);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 修改数据字典
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateDictSort")
	public @ResponseBody Object updateDictSort(HttpServletRequest request){
		String jsonArray=request.getParameter("json_array");//排序array
		String dict_group=request.getParameter("dict_group");
		dictService.updateDictSort(jsonArray,dict_group);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 发送APP信息短信邀请用户
	 * @return
	 */
	@RequestMapping(value="sendMsg")
	public @ResponseBody Object sendMsg(){
		MsgVO vo=BeanUtil.formatToBean(MsgVO.class);
		systemService.sendAPPMsg(vo);
		return ResponseUtils.sendSuccess("短信发送成功！", vo);
	}
	
	/**
	 * 重命名
	 * @return
	 */
	@RequestMapping(value="updateDictName")
	public @ResponseBody Object updateDictName(){
		DictVO vo=BeanUtil.formatToBean(DictVO.class);
		dictService.updateDictName(vo);
		return ResponseUtils.sendSuccess();
	}
	
	
	/**
	 * 获取系统配置
	 * @param key
	 * @return
	 */
	@RequestMapping(value="getSysConfig")
	public Object getSysConfig(String key) {
        return ResponseUtils.sendSuccess(SystemConfig.getProperty(key));
	}
	
	/**
	 * 获取系统配置
	 * @param key
	 * @return
	 */
	@RequestMapping(value="getSchoolNewsConfig")
	public Object getSchoolNewsConfig(String key) {
        return ResponseUtils.sendSuccess(SchoolConfig.getProperty(key));
	}

	/**
	 * 获取系统配置
	 * @return
	 */
	@RequestMapping(value="getAllSysConfig")
	public Object getAllSysConfig() {
		return ResponseUtils.sendSuccess(SystemConfig.getConfigMap());
	}

	/**
	 * 获取表头
	 * @return
	 */
	@RequestMapping(value="getHeaderDataByTableName")
	public Object getHeaderDataByTableName(String table_name) {
		return ResponseUtils.sendSuccess(systemService.getHeaderDataByTableName(table_name));
	}

	/**
	 * 获取数据
	 * @return
	 */
	@RequestMapping(value="getDataByTableName")
	public Object getDataByTableName() {
		Map<String,String> map = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(systemService.getDataByTableName(map));
	}

	@RequestMapping(value="getServerConfigList")
	public @ResponseBody Object getServerConfigList(){
		return systemService.getServerConfigList();
	}

	/**
	 * 修改数据
	 * @return
	 */
	@RequestMapping(value="updateTableData")
	public Object updateTableData() {
		Map<String,String> map = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(systemService.updateTableData(map));
	}
	/**
	 * 删除数据
	 * @return
	 */
	@RequestMapping(value="deleteTableData")
	public Object deleteTableData() {
		Map<String,String> map = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(systemService.deleteTableData(map));
	}
	/**
	 * 插入数据
	 * @return
	 */
	@RequestMapping(value="insertTableData")
	public Object insertTableData() {
		Map<String,String> map = ActionUtil.getParameterMap();
		return ResponseUtils.sendSuccess(systemService.insertTableData(map));
	}
}
