package com.ninesky.classtao.teacherDisk.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.teacherDisk.service.CloudDiskService;
import com.ninesky.classtao.teacherDisk.vo.DiskVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.ResponseUtils;


@RestController
@RequestMapping(value="cloudDiskAction")
public class CloudDiskController extends BaseController {
	
	@Autowired
	private CloudDiskService cloudDiskService;
	
	@Autowired
	private DynamicService dynamicService;
	
	@Autowired
	private GetuiService getuiService;
	
	/**
	 * 获取云盘列表
	 */
	@RequestMapping(value="getCloudDiskList")
	public  Object getCloudDiskList (DiskVO vo){
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<?> list = cloudDiskService.getCloudDiskList(vo);
		return list;
		
	}	
	
	/**
	 * 获取所有文件类型
	 */
	@RequestMapping(value="getFileType")
	public Object getFileType(HttpServletRequest request){
		DiskVO vo= BeanUtil.formatToBean(DiskVO.class);
		vo.setUser_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<DiskVO> list = cloudDiskService.getFileType(vo);
		return list;
	
	}
	
	
	/**
	 * 添加文件到云盘(或新建文件夹)
	 */
	@RequestMapping(value="/addCloudDisk")
	public @ResponseBody Object addCloudDisk(HttpServletRequest request){
		DiskVO vo = BeanUtil.formatToBean(DiskVO.class);
		vo.setParent_id(Integer.parseInt(request.getParameter("parent_id")));
		cloudDiskService.addCloudDisk(vo);
		
		return vo;
	}
	
	/**
	 * 修改云盘文件(移动到)
	 */
	@RequestMapping(value="/updateCloudDisk")
	public @ResponseBody Object updateCloudDisk(HttpServletRequest request){
		
		DiskVO vo = BeanUtil.formatToBean(DiskVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setParent_id(Integer.parseInt(request.getParameter("parent_id")));
		vo.setFile_id(Integer.parseInt(request.getParameter("file_id")));
		cloudDiskService.updateCloudDisk(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 修改云盘文件名
	 */
	@RequestMapping(value="/updateCloudDiskName")
	public @ResponseBody Object updateCloudDiskName(HttpServletRequest request){
		
		DiskVO vo = BeanUtil.formatToBean(DiskVO.class);
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setFile_id(Integer.parseInt(request.getParameter("file_id")));
		cloudDiskService.updateCloudDiskName(vo);
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 删除文件
	 */
	@RequestMapping(value="deleteCloudDisk")
	public @ResponseBody Object deleteCloudDisk(HttpServletRequest request){
		List<DiskVO> list=BeanUtil.jsonToList(request.getParameter("file_ids"), DiskVO.class);
		cloudDiskService.deleteCloudDisk(list);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 发送文件给班级
	 */
	@RequestMapping(value="sendCloudDisk")
	public @ResponseBody Object sendCloudDisk(HttpServletRequest request){
		List<DiskVO> dvo = BeanUtil.jsonToList(request.getParameter("file_list"), DiskVO.class);
		for(DiskVO item:dvo){
			DiskVO vo = BeanUtil.formatToBean(DiskVO.class);
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setFile_id(item.getFile_id());
			vo.setFile_name(item.getFile_name());
			vo.setFile_type(item.getFile_type());
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			
			String receive_list = request.getParameter("receive_list");
			
			cloudDiskService.sendCloudDisk(vo, receive_list);
			
			List<ReceiveVO> receivelist=BeanUtil.jsonToList(request.getParameter("receive_list"), ReceiveVO.class);
			List<ReceiveVO> receive = BeanUtil.removeDuplicate(receivelist);
			for (ReceiveVO Rvo: receive) {
				Rvo.setSchool_id(ActionUtil.getSchoolID());
				Rvo.setStudent_id(0);
			}
			//向一组学生发送动态和推送
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("info_title",vo.getFile_name());
			dataMap.put("info_content", "");
			dataMap.put("module_code",DictConstants.MODULE_CODE_FILE);
			dataMap.put("module_pkid",vo.getFile_id().toString());
			dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
			dataMap.put("info_url", "detail.html");
			dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
			dataMap.put("user_id", ActionUtil.getUserID().toString());
			dataMap.put("student_id","0");
			dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
			dynamicService.insertDynamic(dataMap,receive);
			getuiService.pushMessage(dataMap,receive);

			List<ReceiveVO> receiveTer = new ArrayList<>();
			receiveTer.add(new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID()));
			HashMap<String,String> dataMapTer = new HashMap<String,String>();
			dataMapTer.put("info_title",vo.getFile_name());
			dataMapTer.put("info_content","");
			dataMapTer.put("module_code",DictConstants.MODULE_CODE_FILE);
			dataMapTer.put("module_pkid",vo.getFile_id().toString());
			dataMapTer.put("link_type", DictConstants.LINK_TYPE_DETAIL);
			dataMapTer.put("info_url", "detail.html");
			dataMapTer.put("user_type", DictConstants.USERTYPE_STUDENT);
			dataMapTer.put("user_id", ActionUtil.getUserID().toString());
			dataMapTer.put("student_id","0");
			dataMapTer.put("info_date", ActionUtil.getSysTime().getTime()+"");
			
			dynamicService.insertDynamic(dataMapTer,receiveTer);
			
		}
		return true;
	}
}
