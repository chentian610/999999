package com.ninesky.classtao.teachCloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.framework.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.teachCloud.service.TeachCloudService;
import com.ninesky.classtao.teachCloud.vo.SourceVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResultField;
@RestController
@RequestMapping(value="teachCloudAction")
public class TeachCloudController extends BaseController {
	
	@Autowired
	private TeachCloudService teachCloudService;
	
	@Autowired
	private DynamicService dynamicService;
	
	@Autowired
	private GetuiService getuiService;
	
	
	/**
	 * 教师添加教学资源推荐
	 * @param
	 * @return
	 */
	@RequestMapping(value="/addSource")
	public @ResponseBody Object addSource (HttpServletRequest request){
		List<SourceVO> sv=BeanUtil.jsonToList(request.getParameter("source_list"), SourceVO.class);
		for (SourceVO item:sv) {
			SourceVO vo = BeanUtil.formatToBean(SourceVO.class);
			vo.setSender_id(ActionUtil.getUserID());
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setSender_name(ActionUtil.getParameter("user_name"));
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			vo.setSource_id(item.getSource_id());
			vo.setSource_data(item.getSource_data());
			vo.setSource_name(item.getSource_name());
			vo.setSource_type(item.getSource_type());
			//vo.setResourceUrl(item.getResourceUrl());
			
			teachCloudService.addSource(vo);
			String receive_list=request.getParameter("receive_list");
			teachCloudService.addSourceGroup(vo, receive_list);
			//存放一组接收者的信息
			List<ReceiveVO> receivelist=BeanUtil.jsonToList(request.getParameter("receive_list"), ReceiveVO.class);
			List<ReceiveVO> receive = BeanUtil.removeDuplicate(receivelist);
			for (ReceiveVO Rvo: receive) {
				Rvo.setSchool_id(ActionUtil.getSchoolID());
				Rvo.setStudent_id(0);
			}
			//向一组学生发送动态和推送
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("info_title",vo.getSource_name());
			dataMap.put("info_content",vo.getRemark());
			dataMap.put("module_code",DictConstants.MODULE_CODE_RECOMMEND);
			dataMap.put("module_pkid",vo.getId().toString());
			dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url", "detail.html");
			dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
			dataMap.put("user_id", ActionUtil.getUserID().toString());
			dataMap.put("student_id","0");
			dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
			dynamicService.insertDynamic(dataMap,receive);
			getuiService.pushMessage(dataMap,receive);
			
			//添加一条发给老师自己的动态
			List<ReceiveVO> receiveTer = new ArrayList<>();
			receiveTer.add(new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID()));
			HashMap<String,String> dataMapTer = new HashMap<String,String>();
			dataMapTer.put("info_title",vo.getSource_name());
			dataMapTer.put("info_content",vo.getRemark());
			//判断是教学课件还是教学视频
			if (DictConstants.MODULE_CODE_VIDEO.equals(vo.getModule_code())) {
				dataMapTer.put("module_code",DictConstants.MODULE_CODE_VIDEO);
			} else if (DictConstants.MODULE_CODE_COURSEWARE.equals(vo.getModule_code())) {
				dataMapTer.put("module_code",DictConstants.MODULE_CODE_COURSEWARE);
			}
			dataMapTer.put("module_pkid",vo.getId().toString());
			dataMapTer.put("link_type", DictConstants.LINK_TYPE_DETAIL);
			dataMapTer.put("info_url", "detail.html");
			dataMapTer.put("user_type", DictConstants.USERTYPE_TEACHER);
			dataMapTer.put("user_id", ActionUtil.getUserID().toString());
			dataMapTer.put("student_id","0");
			dataMapTer.put("info_date", ActionUtil.getSysTime().getTime()+"");
			dynamicService.insertDynamic(dataMapTer,receiveTer);
			
		}
		return true;
	}
	
	/**
	 * 获取教师资源推荐列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSourceList")
	@ResultField
	public Object getSourceList(HttpServletRequest request){
		SourceVO vo = BeanUtil.formatToBean(SourceVO.class);
		vo.setSender_id(ActionUtil.getUserID());
		vo.setSender_name(ActionUtil.getParameter("user_name"));
		vo.setCreate_by(ActionUtil.getUserID());
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		List<?> list = teachCloudService.getSourceList(paramMap);
		return list;
	}
	
	/**
	 * 加载资源列表(Web端)
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(value="/getTeachCloudSource")
	public Object getSourceQuery(HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		return teachCloudService.getTeachCloudSource(paramMap);
	}
	
	/**
	 * 通过主键ID获取教师资源推荐
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/getSourceByID")
	@ResultField(includes={"source_id","source_name","source_type","remark","source_data","send_time","sender_id","sender_name"})
	public Object getSourceByID(Integer id){
		return teachCloudService.getSourceByID(id);
	}


	/**
	 * 添加教学云资源目录
	 * @throws Exception
	 *
	 */
	@RequestMapping(value="/addSourceCatalogList")
	public Object addSourceCatalogList(HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		teachCloudService.addSourceCatalogList(paramMap);
		return ResponseUtils.sendSuccess("添加成功!");
	}
}