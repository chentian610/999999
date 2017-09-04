package com.ninesky.classtao.photo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ninesky.framework.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.photo.service.PhotoService;
import com.ninesky.classtao.photo.vo.PhotoReceiveVO;
import com.ninesky.classtao.photo.vo.PhotoVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.BaseController;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.DateUtil;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import com.ninesky.common.util.StringUtil;

@RestController
@RequestMapping(value="photoAction")
public class PhotoController extends BaseController{
	@Autowired
	private PhotoService photoService;

	@Autowired
	private GetuiService getuiService;

	/**
	 * 用户上传照片
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/addPhoto")
	@ResultField(includes={"photo_id","student_id","photo_url","photo_resize_url","create_date","team_type"})
	public @ResponseBody Object addPhoto(HttpServletRequest request) {
		System.out.println("ddd="+ActionUtil.getSysTime());
		if(StringUtil.isEmpty(request.getParameter("fileURL"))) throw new BusinessException("没有照片可以上传");
        if(StringUtil.isEmpty(request.getParameter("class_id")) || "0".equals(request.getParameter("class_id")))
        	throw new BusinessException("您没有选择明确的班级，无法上传图片...");
        List<PhotoVO> photoList = new ArrayList<PhotoVO>();
        String[] fileNames = request.getParameter("fileURL").split(",");
//      String[] fileResizeNames = request.getParameter("fileResizeName").split(",");
        String[] fileResizeURLs = request.getParameter("fileResizeURL").split(",");
        Integer class_id = Integer.parseInt(request.getParameter("class_id"));
        String add_date = DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd");
        List<FileVO> list = new ArrayList<FileVO>();
        for(int i=0; i<fileNames.length; i++)
        {
        	PhotoVO vo = new PhotoVO();
        	vo.setSchool_id(ActionUtil.getSchoolID());
        	vo.setClass_id(class_id);
        	vo.setStudent_id(Integer.parseInt(request.getParameter("student_id")));
        	vo.setPhoto_type(request.getParameter("photo_type"));
        	vo.setUser_id(ActionUtil.getUserID());
        	vo.setUser_type(ActionUtil.getUserType());
			vo.setTeam_type(request.getParameter("team_type"));
        	vo.setCreate_by(ActionUtil.getUserID());
        	vo.setCreate_date(ActionUtil.getSysTime());
        	vo.setPhoto_resize_url(fileResizeURLs[i]);
        	vo.setPhoto_url(fileNames[i]);
        	vo.setAdd_date(add_date);
        	photoService.insertPhoto(vo);
        	photoList.add(vo);
        	FileVO fileVO = new FileVO();
        	fileVO.setFile_url(fileNames[i]);
        	fileVO.setFile_resize_url(fileResizeURLs[i]);
        	list.add(fileVO);
        }
        //添加动态
//        photoService.addInformation(photoList);
		addDynamicAndPushGETUI(fileNames.length, class_id, add_date,list);
        return photoList;		
	}

	private void addDynamicAndPushGETUI(Integer file_count, Integer class_id,String add_date,List<FileVO> fileUrl) {
		List<ReceiveVO> receivelist = new ArrayList<ReceiveVO>();
		if (StringUtil.isEmpty(fileUrl)) throw new BusinessException("图片路径为空.............");
		ReceiveVO vo = new ReceiveVO(ActionUtil.getSchoolID(),ActionUtil.getParameter("team_type"),null,class_id);
		vo.setUser_type(DictConstants.USERTYPE_ALL);
		receivelist.add(vo);
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("module_code",DictConstants.MODULE_CODE_PHOTO);
		//日期加班级作为唯一ID
		dataMap.put("module_pkid",add_date.replaceAll("-", "")+class_id);//201607111103
		dataMap.put("link_type", DictConstants.LINK_TYPE_DEFUALT);
		dataMap.put("info_url", "index.html");
		dataMap.put("class_id", class_id.toString());
		dataMap.put("photo_count", file_count.toString());
		dataMap.put("add_date",add_date);
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", DictConstants.USERTYPE_ALL);
		dataMap.put("photo_url_list", BeanUtil.ListTojson(fileUrl).toString());
		photoService.insertDynamic(dataMap,receivelist);
		//如果教师家长都发的话，那么推送也只有一条
		dataMap.remove("photo_url_list");//防止图片太多，导致转换成JSON字符串长度超过2048[个推最长限制数]
		getuiService.pushMessage(dataMap,receivelist);
	}
	
	/**
	 * 获取照片列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPhotoList")
	@ResultField(includes={"photo_id","student_id","photo_url","photo_resize_url","send_time","sender_id","version"})
	public @ResponseBody Object getPhotoList(HttpServletRequest request){
		PhotoReceiveVO photo = BeanUtil.formatToBean(PhotoReceiveVO.class);
		List<?> list = photoService.getPhotoList(photo);
		return list;
	}
	
	/**
	 * 用户删除相片
	 */
	@RequestMapping(value="/deletePhoto")
	public @ResponseBody Object deletePhoto(HttpServletRequest request){
		String parentPath=request.getSession().getServletContext().getRealPath("..");
		PhotoVO vo = BeanUtil.formatToBean(PhotoVO.class);
		photoService.deletePhoto(vo, parentPath);
		return ResponseUtils.sendSuccess();
	}
	///**
	// * 获取未查看的相册数量
	// * @return
	// */
	//@RequestMapping(value="/getUnreadCount")
	//@ResultField(includes={"count"})
	//public @ResponseBody Object getUnreadCount(HttpServletRequest request){
	//	PhotoReceiveVO photo = BeanUtil.formatToBean(PhotoReceiveVO.class);
	//	return photoService.getUnreadCount(photo);
	//}
	
}
