package com.ninesky.classtao.file.controller;

import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="fileAction")
public class FileController extends BaseController{
	
	@RequestMapping(value="uploadFile")
	@ResultField(includes={"file_name","store_name","file_url","file_resize_url","file_type","file_real_name","file_size"})
	public @ResponseBody Object uploadFile(HttpServletRequest request) {
		 //获取服务器发布路径
        String parentPath=request.getSession().getServletContext().getRealPath("..");
        String module_code=request.getParameter("module_code");
        if (StringUtil.isEmpty(module_code)) module_code="null";
        String file_path="";
        switch (module_code){
        case DictConstants.MODULE_CODE_USER://用户模块
        	file_path=Constants.FILE_PATH_USER;
        	break;
        case DictConstants.MODULE_CODE_NOTICE://通知模块
        	file_path=Constants.FILE_PATH_NOTICE;
        	break;
        case DictConstants.MODULE_CODE_NOTICE_SCH://校务通知
        	file_path=Constants.FILE_PATH_NOTICE;
        	break;
        case DictConstants.MODULE_CODE_HOMEWORK://作业
        	file_path=Constants.FILE_PATH_HOMEWORK;
        	break;
        case DictConstants.MODULE_CODE_NOTE://课件
        	file_path=Constants.FILE_PATH_NOTE;
        	break;
        case DictConstants.MODULE_CODE_PHOTO://相册
        	file_path=Constants.FILE_PATH_PHOTO;
        	break;
        case DictConstants.MODULE_CODE_SCHOOLSTYLE://校园风采
        	file_path=Constants.FILE_PATH_NEWS;
        	break;
        case DictConstants.MODULE_CODE_PARTYBUILD://党建
        	file_path=Constants.FILE_PATH_NEWS;
        	break;
        default :
        	file_path=Constants.FILE_PATH_DEFAULT;//公用
        	break;
        }
        Map<String,String> voMap = FileOperateUtil.upload(request, parentPath+file_path);
        if (voMap==null) throw new BusinessException(MsgService.getMsg("NO_FILE_UPLOAD"));
        String[] fileURLArray = voMap.get("fileURL").toString().split(Constants.SPLIT_FLAG);
        String[] fileNameArray = voMap.get("fileURL").toString().split(Constants.SPLIT_FLAG);
        String[] fileResizeNameArray = voMap.get("fileResizeName").toString().split(Constants.SPLIT_FLAG);
        String[] fileRealName=voMap.get("fileName").toString().split(Constants.SPLIT_FLAG);//文件名称，不是数字的
        String[] fileSize=voMap.get("fileSize").toString().split(Constants.SPLIT_FLAG);//文件大小，单位byte
        List<FileVO> list = new ArrayList<FileVO>();
        for (int i=0;i<fileURLArray.length;i++) {
        	FileVO file = new FileVO();
        	file.setFile_name(fileNameArray[i]);
        	file.setStore_name(fileURLArray[i]);
        	file.setFile_url(SystemConfig.getProperty("host_ip_port")+ file_path+fileURLArray[i]);
        	file.setFile_resize_url(SystemConfig.getProperty("host_ip_port")+ file_path +fileResizeNameArray[i]);
        	file.setFile_real_name(fileRealName[i].substring(0,fileRealName[i].lastIndexOf(".")));
        	file.setFile_size(fileSize[i]);
        	file.setFile_type(fileRealName[i].substring(fileRealName[i].lastIndexOf(".")+1).toUpperCase());//文件类型
        	list.add(file);
        }
        return list;
	}
}
