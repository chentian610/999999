package com.classtao.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.classtao.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;

import com.classtao.vo.FileVO;


@RestController
@RequestMapping(value="fileAction")
public class FileController extends BaseController{
	
	@RequestMapping(value="uploadFile1")
	public Object uploadFile1(HttpServletRequest request) {
		return ResponseUtils.sendSuccess("Test Successful");
	}
	
	@RequestMapping(value="uploadFile")
	public Object uploadFile(HttpServletRequest request) {
		 //获取服务器发布路径
        Map<String,String> voMap = FileOperateUtil.upload(request);
        if (voMap==null) throw new RuntimeException("没有文件上传，请重试！");
        String[] fileURLArray = voMap.get("fileURL").toString().split(Constants.SPLIT_FLAG);
        String[] fileNameArray = voMap.get("fileURL").toString().split(Constants.SPLIT_FLAG);
        String[] fileResizeNameArray = voMap.get("fileResizeName").toString().split(Constants.SPLIT_FLAG);
        String[] fileRealName=voMap.get("fileName").toString().split(Constants.SPLIT_FLAG);//文件名称，不是数字的
        String[] fileSize=voMap.get("fileSize").toString().split(Constants.SPLIT_FLAG);//文件大小，单位byte
        String[] filePath=voMap.get("filePath").toString().split(Constants.SPLIT_FLAG);//文件路径
        String[] fileIndex=voMap.get("fileIndex").toString().split(Constants.SPLIT_FLAG);//文件索引
        List<FileVO> list = new ArrayList<FileVO>();
        for (int i=0;i<fileURLArray.length;i++) {
        	FileVO file = new FileVO();
        	file.setFile_name(fileNameArray[i]);
        	file.setStore_name(fileURLArray[i]);
        	file.setFile_url(SystemConfig.getProperty("host_ip_port")+ voMap.get("file_path")+fileURLArray[i]);
        	file.setFile_resize_url(SystemConfig.getProperty("host_ip_port")+ voMap.get("file_path") +fileResizeNameArray[i]);
        	file.setFile_real_name(fileRealName[i]);
        	file.setFile_size(fileSize[i]);
        	file.setFile_type(fileRealName[i].substring(fileRealName[i].lastIndexOf(".")+1).toUpperCase());//文件类型
        	file.setFile_path(filePath[i]);
        	file.setFile_index(fileIndex[i]);
        	list.add(file);
        }
        if ("true".equals(voMap.get("need_change")) || "1".equals(voMap.get("need_change"))) {
            for (FileVO fileVO: list) {
                if ("AMR".equals(fileVO.getFile_type()))
                    WechatFileOperateUtil.amrCoverToMp3(fileVO.getFile_url());
            }

        }
        return ResponseUtils.sendSuccess(list);
	}

	@RequestMapping(value="cutImage")
	public Object cutImage(HttpServletRequest request) {
		String file_name = request.getParameter("file_name")+"";
		String module_code = request.getParameter("module_code")+"";
		Integer school_id = Integer.parseInt(request.getParameter("school_id")==null?"0":request.getParameter("school_id"));
		String cut_data = (request.getParameter("cut_data")+"").replaceAll("px","");
		String file_path = SystemConfig.getProperty(Constants.FILE_ROOT)+ StringUtil.getFilePathByModuleCode(module_code,school_id)+file_name;
		double resize_rate = StringUtil.isEmpty(request.getParameter("resize_rate")+"")?1:Double.parseDouble(request.getParameter("resize_rate")+"");
		FileOperateUtil.cutPicture(file_path,cut_data,file_name.substring(file_name.lastIndexOf(".")),resize_rate);
		return ResponseUtils.sendSuccess("done");
	}
	
	@RequestMapping(value="uploadWechatFile")
	public Object uploadWechatFile(HttpServletRequest request) {
		List<FileVO> list = WechatFileOperateUtil.upload(request);
		if (list==null) throw new RuntimeException("没有文件上传，请重试！");
        return ResponseUtils.sendSuccess(list); 
	}
	
	@RequestMapping(value="amrToMp3")
	public Object amrTomp3(HttpServletRequest request) {
		String amrUrl = request.getParameter("amrUrl");
		String mp3Url = WechatFileOperateUtil.amrCoverToMp3(amrUrl);
		if(StringUtils.isEmpty(mp3Url)) throw new RuntimeException("转换mp3格式文件失败！");
        return ResponseUtils.sendSuccess(mp3Url); 
	}
}
