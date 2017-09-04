package com.ninesky.classtao.photo.service.impl;

import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.photo.service.PhotoService;
import com.ninesky.classtao.photo.vo.PhotoCommentVO;
import com.ninesky.classtao.photo.vo.PhotoReceiveVO;
import com.ninesky.classtao.photo.vo.PhotoVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.ClassService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service("photoServiceImpl")
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private GeneralDAO dao;
	@Autowired
	private InfoService infoService;
	@Autowired
	private UserService userService;
	@Autowired
	private ClassService classService;
    @Autowired
    private RedisService redisService;
	@Autowired
	private JedisDAO jedisDAO;

	/**
	 * 上传相片
	 * 
	 * @param vo
	 */
	public void insertPhoto(PhotoVO vo) {
		// 相片
		vo.setPhoto_id(dao.insertObjectReturnID("photoMap.insertPhoto", vo));
		// 相片接收
		//List<PhotoReceiveVO> receiveList = new ArrayList<PhotoReceiveVO>();
		//if (DictConstants.PHOTOTYPE_PERSON.equals(vo.getPhoto_type()))
		//	receiveList.add(setPersonalReceive(vo));// 私人相片
		//else if (DictConstants.PHOTOTYPE_CLASS.equals(vo.getPhoto_type())) {
		//	receiveList.addAll(setPublicReceive(vo));// 班级相片
		//} else {
		//	receiveList.add(setPersonalReceive(vo));
		//	receiveList.addAll(setPublicReceive(vo));
		//}
		//dao.insertObject("photoReceiveMap.insertPhotoReceiveBatch", receiveList);
	}

	/**
	 * 获取相片列表
	 * 
	 * @param photo
	 * @return
	 */
	public List<PhotoVO> getPhotoList(PhotoReceiveVO photo) {
		if (photo.getClass_id() == null)
			throw new BusinessException("请选择确定班级，否则您无法查看相册...");
		List<PhotoVO> list = dao.queryForList("photoMap.getPhotoList", photo);
		for (PhotoVO vo : list) {
			vo.setSender_id(vo.getCreate_by());
			vo.setSend_time(vo.getCreate_date());
		}
		return list;
	}

	/**
	 * 删除相片
	 * 
	 * @param vo
	 * @param parentPath
	 */
	public void deletePhoto(PhotoVO vo, String parentPath) {
		if (StringUtil.isEmpty(vo.getPhoto_ids()))
			throw new BusinessException("请指定要删除的照片...");
		String[] photo_ids = vo.getPhoto_ids().split(",");
		PhotoVO photo = null;
		File file = null;
		File fileResize = null;
		List<String> keyList = new ArrayList<String>();
		for (int i = 0; i < photo_ids.length; i++) {
			vo.setPhoto_id(Integer.parseInt(photo_ids[i]));
			photo = dao.queryObject("photoMap.getPhotoById", vo);
			if (photo == null)
				continue;
			// 删除表纪录
			dao.deleteObject("photoMap.deletePhotoById", vo);
			//dao.deleteObject("photoReceiveMap.deletePhotoById", vo);
			// 删除磁盘纪录
			String photoName = photo.getPhoto_url().substring(
					photo.getPhoto_url().lastIndexOf("/") + 1);
			String photoResizeName = photo.getPhoto_resize_url().substring(
					photo.getPhoto_resize_url().lastIndexOf("/") + 1);
			file = new File(parentPath + Constants.FILE_PATH_PHOTO + photoName);
			fileResize = new File(parentPath + Constants.FILE_PATH_PHOTO + photoResizeName);
			file.delete();
			fileResize.delete();
			keyList.add(photo.getAdd_date().replaceAll("-", "") + photo.getClass_id());
		}
		deletekeyValueOfRedis(keyList);
	}

	/**
	 * 更新redis
	 * @param keyList
	 */
	private void deletekeyValueOfRedis(List<String> keyList) {
		for (String key : keyList) {
			Integer class_id =Integer.parseInt(key.substring(8));
			String DynamicKey = RedisKeyUtil.getDynamicKey(DictConstants.MODULE_CODE_PHOTO,Long.parseLong(key), "")+"";
			String delete_date = DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd");
            String class_name = redisService.getTeamName(ActionUtil.getParameter("team_type"),ActionUtil.getParameter("group_id")==null?0:classService.getClassByID(IntegerUtil.getValue(ActionUtil.getParameter("class_id"))).getGrade_id(),IntegerUtil.getValue(ActionUtil.getParameter("class_id")));
            long total = jedisDAO.hincrBy(DynamicKey, "total", -1);
			jedisDAO.hset(DynamicKey, "info_title", MsgService.getMsg("DYNAMIC_PHOTO",class_name,total,delete_date));
			String addDate = key.substring(0,4)+"-"+key.substring(4,6)+"-"+key.substring(6,8);
			PhotoVO photo = new PhotoVO();
			photo.setSchool_id(ActionUtil.getSchoolID());
			photo.setClass_id(class_id);
			photo.setAdd_date(addDate);
			List<PhotoVO> photoList = dao.queryForList("photoMap.getPhotoList", photo);
			List<FileVO> list = new ArrayList<FileVO>();
			for (PhotoVO photoVO : photoList) {
				FileVO fileVO = new FileVO();
	        	fileVO.setFile_url(photoVO.getPhoto_url());
	        	fileVO.setFile_resize_url(photoVO.getPhoto_resize_url());
	        	list.add(fileVO);
			}
			jedisDAO.hset(DynamicKey, "photo_url_list", BeanUtil.ListTojson(list)+"");
		}
	}

	/**
	 * 获取相册未读数量
	 * @param photo
	 * @return
	 */
	public Integer getUnreadCount(PhotoReceiveVO photo) {
		return dao.queryObject("photoReceiveMap.getUnreadCount", photo);
	}

	/**
	 * 添加动态
	 * 
	 * @param photoList
	 */
	public void addInformation(List<PhotoVO> photoList) {
		// 判断动态是否存在
		InfoReceiveVO receiveVO = getReceiveInfo(photoList.get(0));
		// 添加动态
		if (receiveVO == null)
			addInfo(photoList);
		// 更新动态
		else
			updateInfo(photoList, receiveVO);
	}

	// 私人相册(只发给自己)
	private PhotoReceiveVO setPersonalReceive(PhotoVO vo) {
		PhotoReceiveVO recVO = new PhotoReceiveVO();
		recVO.setSchool_id(vo.getSchool_id());
		recVO.setClass_id(vo.getClass_id());
		recVO.setUser_type(vo.getUser_type());
        recVO.setTeam_type(ActionUtil.getParameter("team_type"));
		recVO.setUser_id(vo.getUser_id());
		recVO.setStudent_id(vo.getStudent_id());
		recVO.setPhoto_id(vo.getPhoto_id());
		recVO.setPhoto_type(DictConstants.PHOTOTYPE_PERSON);
		recVO.setPhoto_url(vo.getPhoto_url());
		recVO.setPhoto_resize_url(vo.getPhoto_resize_url());
		recVO.setAdd_date(vo.getAdd_date());
		recVO.setCreate_date(vo.getCreate_date());
		recVO.setCreate_by(vo.getCreate_by());
		return recVO;
	}

	// 班级相册
	private List<PhotoReceiveVO> setPublicReceive(PhotoVO vo) {
		List<PhotoReceiveVO> receiveList = new ArrayList<PhotoReceiveVO>();
		receiveList.addAll(getStuUserList(vo));
		receiveList.addAll(getTeaUserList(vo));
		return receiveList;
	}

	// 根据班级设置学生接收
	private List<PhotoReceiveVO> getStuUserList(PhotoVO photo) {
		List<PhotoReceiveVO> receiveList = new ArrayList<PhotoReceiveVO>();
		StudentVO stuVO = new StudentVO();
		stuVO.setSchool_id(photo.getSchool_id());
		stuVO.setClass_id(photo.getClass_id());
		List<StudentVO> stuUserList = null;
        if (DictConstants.TEAM_TYPE_INTEREST.equals(photo.getTeam_type())) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("contact_id", photo.getClass_id());
            map.put("user_type", DictConstants.USERTYPE_STUDENT);
            map.put("school_id", photo.getSchool_id());
            stuUserList = getStuUserListByID(map);
        } else stuUserList = userService.getStuUserList(stuVO);
		if (ListUtil.isEmpty(stuUserList))
			return receiveList;
		for (StudentVO student : stuUserList) {
			PhotoReceiveVO recVO = new PhotoReceiveVO();
			recVO.setPhoto_id(photo.getPhoto_id());
			recVO.setUser_id(0);// 学生不注册
			recVO.setUser_type(DictConstants.USERTYPE_STUDENT);
            recVO.setTeam_type(ActionUtil.getParameter("team_type"));
			recVO.setSchool_id(photo.getSchool_id());
			recVO.setClass_id(photo.getClass_id());
			recVO.setStudent_id(student.getStudent_id());// 家长根据student_id和user_type
			recVO.setPhoto_type(DictConstants.PHOTOTYPE_CLASS);
			recVO.setPhoto_url(photo.getPhoto_url());
			recVO.setPhoto_resize_url(photo.getPhoto_resize_url());
			recVO.setAdd_date(photo.getAdd_date());
			recVO.setCreate_date(photo.getCreate_date());
			recVO.setCreate_by(photo.getCreate_by());
			receiveList.add(recVO);
		}
		return receiveList;
	}

    private List<StudentVO> getStuUserListByID(Map<String, Object> map){
        return dao.queryForList("contactListMap.getStudentContactList",map);
    }

    private List<TeacherVO> getTeaUserListByID(Map<String, Object> map){
        return dao.queryForList("contactListMap.getTeacherContactList",map);
    }
	// 根据班级设置教师接收
	private List<PhotoReceiveVO> getTeaUserList(PhotoVO photo) {
		List<PhotoReceiveVO> receiveList = new ArrayList<PhotoReceiveVO>();
		TeacherVO teaVO = new TeacherVO();
		teaVO.setSchool_id(photo.getSchool_id());
		teaVO.setClass_id(photo.getClass_id());
		List<TeacherVO> teaUserList = null;
        if (DictConstants.TEAM_TYPE_INTEREST.equals(photo.getTeam_type())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("contact_id", photo.getClass_id());
            map.put("user_type", DictConstants.USERTYPE_TEACHER);
            map.put("school_id", photo.getSchool_id());
            teaUserList = getTeaUserListByID(map);
        } else teaUserList = userService.getTeaUserList(teaVO);
		if (ListUtil.isEmpty(teaUserList))
			return receiveList;
		for (TeacherVO teacher : teaUserList) {
			// 老师没注册就跳过不发送
			if (teacher.getUser_id() == null)
				continue;
			PhotoReceiveVO recVO = new PhotoReceiveVO();
			recVO.setPhoto_id(photo.getPhoto_id());
			recVO.setUser_id(teacher.getUser_id());// 老师根据user_id,user_type查询相片
			recVO.setUser_type(DictConstants.USERTYPE_TEACHER);
            recVO.setTeam_type(ActionUtil.getParameter("team_type"));
			recVO.setSchool_id(photo.getSchool_id());
			recVO.setClass_id(photo.getClass_id());
			recVO.setStudent_id(0);
			recVO.setPhoto_type(DictConstants.PHOTOTYPE_CLASS);
			recVO.setPhoto_url(photo.getPhoto_url());
			recVO.setPhoto_resize_url(photo.getPhoto_resize_url());
			recVO.setAdd_date(photo.getAdd_date());
			recVO.setCreate_date(photo.getCreate_date());
			recVO.setCreate_by(photo.getCreate_by());
			receiveList.add(recVO);
		}
		// 班级里一个老师如果教多门课程
		filterRepeatReceive(receiveList);
		return receiveList;
	}

	// 从动态接收表 （根据学校，班级，相册模块，日期）查寻动态
	private InfoReceiveVO getReceiveInfo(PhotoVO photo) {
		InfoReceiveVO receiveVO = new InfoReceiveVO();
		receiveVO.setSchool_id(photo.getSchool_id());
		receiveVO.setClass_id(photo.getClass_id());
		receiveVO.setModule_code(DictConstants.MODULE_CODE_PHOTO);
		receiveVO.setModule_pkid(getModule_pkid());
		return infoService.getReceiveInfoByModule(receiveVO);
	}

	// 添加动态
	private void addInfo(List<PhotoVO> photoList) {
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(0);// 相册动态按天更新，不考虑上传者
		infoVO.setModule_code(DictConstants.MODULE_CODE_PHOTO);// 相册模块
		infoVO.setModule_pkid(getModule_pkid());
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);// 本地模块消息
		infoVO.setInfo_title("相册更新了" + photoList.size() + "张照片");
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		infoVO.setInfo_date(DateUtil.formatDateToString(
				ActionUtil.getSysTime(), "yyyy-MM-dd"));
		infoVO.setPhoto_list(getPhotoUrls(photoList));
		infoVO.setReceive_list(getReceiveList(photoList.get(0)));
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoService.addInfo(infoVO);
	}

	// 更新动态
	private void updateInfo(List<PhotoVO> photoList, InfoReceiveVO receiveVO) {
		String photo_list = getPhotoUrls(photoList) + ","
				+ receiveVO.getPhoto_list();
		receiveVO.setPhoto_list(photo_list);
		receiveVO.setInfo_title("相册更新了" + photo_list.split(",").length + "张照片");
		receiveVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		receiveVO.setUpdate_by(ActionUtil.getUserID());
		receiveVO.setUpdate_date(ActionUtil.getSysTime());
		infoService.updateInformation(receiveVO);
	}

	// 获取上传相片的url串
	private String getPhotoUrls(List<PhotoVO> photoList) {
		if (ListUtil.isEmpty(photoList))
			return null;
		Map<String, String> photoMap = new HashMap<String, String>();
		String photoUrl = null;
		for (PhotoVO vo : photoList) {
			photoUrl = photoMap.get("photo_url_resize");
			if (StringUtil.isEmpty(photoUrl))
				photoMap.put("photo_url_resize", vo.getPhoto_resize_url());
			else
				photoMap.put("photo_url_resize",
						photoUrl + "," + vo.getPhoto_resize_url());
		}
		return photoMap.get("photo_url_resize");
	}

	// 获取接收对象的json数组格式
	private String getReceiveList(PhotoVO photo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grade_id", 0);
		map.put("class_id", photo.getClass_id());
		map.put("user_type", "");// 默认全部发送动态
		JSONArray array = JSONArray.fromObject(map);
		return array.toString();
	}

	// 模块pkid(日期组合)
	private Integer getModule_pkid() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = "0" + (cal.get(Calendar.MONTH) + 1);
		month = month.substring(month.length() - 2);
		String day = "0" + cal.get(Calendar.DATE);
		day = day.substring(day.length() - 2);
		return Integer.parseInt(year + month + day);
	}

	// 过滤重复
	private void filterRepeatReceive(List<PhotoReceiveVO> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getUser_id().equals(list.get(i).getUser_id())
						&& list.get(j).getUser_type()
								.equals(list.get(i).getUser_type())) {
					list.remove(j);
				}
			}
		}
	}

	/**
	 * 相册添加动态 1、发送者教师添加动态 2、学生家长接受动态
	 */
	public void insertDynamic(HashMap<String, String> dataMap,List<ReceiveVO> receivelist) {
		String module_code = dataMap.get("module_code");
		Long pkID = Long.parseLong(dataMap.get("module_pkid"));
		String class_name = redisService.getTeamName(ActionUtil.getParameter("team_type"),ActionUtil.getParameter("group_id")==null?0:classService.getClassByID(IntegerUtil.getValue(ActionUtil.getParameter("class_id"))).getGrade_id(),IntegerUtil.getValue(ActionUtil.getParameter("class_id")));
		String DynamicKey = RedisKeyUtil.getDynamicKey(module_code, pkID, "");
		Long count = LongUtil.getValue(dataMap.get("photo_count"));
		dataMap.remove("photo_count");
		long total = jedisDAO.hincrBy(DynamicKey, "total",count);
		dataMap.put("info_title",MsgService.getMsg("DYNAMIC_PHOTO", class_name, total, dataMap.get("add_date")));
		dataMap.put("photo_url", "");
		jedisDAO.hsetAll(DynamicKey, dataMap);
		for (ReceiveVO vo : receivelist) {
            if (DictConstants.TEAM_TYPE_CLASS.equals(vo.getTeam_type())) vo.setGroup_id(classService.getClassByID(vo.getTeam_id()).getGrade_id());
			// 将该条动态加入到分组动态中
			String setKey = RedisKeyUtil.KEY_SET_PRE+ RedisKeyUtil.KEY_DYNAMIC_PRE+ RedisKeyUtil.getGroupKey(vo);
			jedisDAO.zadd(setKey, ActionUtil.getSysTime().getTime() ,DynamicKey);
		}
		return;
	}

	public void insertPhotoComment(PhotoCommentVO vo) {
		if (StringUtil.isEmpty(vo.getAdd_date())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("photoCommentMap.insertPhotoComment",vo);
	}

	public void insertPhotoPointPraise(PhotoCommentVO vo) {
		if (StringUtil.isEmpty(vo.getAdd_date())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        PhotoCommentVO photoCommentVO = new PhotoCommentVO();
        photoCommentVO.setSchool_id(ActionUtil.getSchoolID());
        photoCommentVO.setAdd_date(vo.getAdd_date());
        photoCommentVO.setUser_type(ActionUtil.getUserType());
        if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {
            photoCommentVO.setUser_id(ActionUtil.getUserID());
        } else {
            photoCommentVO.setStudent_id(ActionUtil.getStudentID());
        }
		PhotoCommentVO photoComment = getPhotoComment(photoCommentVO);
		if (photoComment == null) {
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("photoCommentMap.insertPhotoPointPraise",vo);
			return;
		}
        vo.setUpdate_by(ActionUtil.getUserID());
        vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("photoCommentMap.updatePhotoPointPraise",vo);
	}

    public List<PhotoVO> getClassCircleList(Map<String,String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("class_id")))
            throw new BusinessException(MsgService.getMsg("ERROR_SCHOOL_CLASS_ID_NULL"));
        List<PhotoVO> list = dao.queryForList("photoMap.getPhotoAddDateList",paramMap);
        for (PhotoVO vo : list) {
            vo.setPhoto_list(getPhotoListByDate(vo));
            vo.setComment_list(setPhotoCommentList(vo.getAdd_date()));
            vo.setPoint_praise(setPointPraise(vo.getAdd_date()));
            vo.setPoint_praise_total(setPointPraiseTotal(vo.getAdd_date()));
        }
        return list;
    }

    private String getPhotoListByDate(PhotoVO photo) {
        if (photo.getClass_id() == null)
            throw new BusinessException("请选择确定班级，否则您无法查看相册...");
        List<PhotoVO> list = dao.queryForList("photoMap.getPhotoList", photo);
        for (PhotoVO vo : list) {
            vo.setSender_id(vo.getCreate_by());
            vo.setSend_time(vo.getCreate_date());
        }
        return BeanUtil.ListTojson(list,false);
    }

    private String setPhotoCommentList(String add_date){
        if (StringUtil.isEmpty(add_date)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        PhotoCommentVO photoCommentVO = new PhotoCommentVO();
        photoCommentVO.setSchool_id(ActionUtil.getSchoolID());
        photoCommentVO.setAdd_date(add_date);
        List<PhotoCommentVO> list = getPhotoCommentList(photoCommentVO);
        if (ListUtil.isEmpty(list)) return "";
        for (PhotoCommentVO vo : list) {
            if (DictConstants.USERTYPE_TEACHER.equals(vo.getUser_type())) {
                vo.setReviewer_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_TEACHER,vo.getUser_id(),0)+"老师");
            } else {
                String relation = "";
                if (DictConstants.USERTYPE_PARENT_OTHER.equals(vo.getRelation())) relation = "家长";
                else relation = redisService.getDictValue(vo.getRelation());
                vo.setReviewer_name(redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,vo.getStudent_id())+relation);
            }
        }
        return BeanUtil.ListTojson(list,false);
    }

    private Integer setPointPraiseTotal(String add_date) {
        if (StringUtil.isEmpty(add_date)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        PhotoCommentVO vo = new PhotoCommentVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setAdd_date(add_date);
        vo.setPoint_praise(DictConstants.TRUE);
        return dao.queryObject("photoCommentMap.getPointPraiseCount",vo);
    }

    private Integer setPointPraise(String add_date){
        if (StringUtil.isEmpty(add_date)) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        PhotoCommentVO vo = new PhotoCommentVO();
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setAdd_date(add_date);
        vo.setUser_id(ActionUtil.getUserID());
        vo.setUser_type(ActionUtil.getUserType());
        if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) vo.setUser_id(ActionUtil.getUserID());
        else vo.setStudent_id(ActionUtil.getStudentID());
        PhotoCommentVO photoCommentVO = getPhotoComment(vo);
        if (photoCommentVO == null) return DictConstants.FALSE;
        return photoCommentVO.getPoint_praise();
    }

    private List<PhotoCommentVO> getPhotoCommentList(PhotoCommentVO vo) {
        return dao.queryForList("photoCommentMap.getPhotoCommentList",vo);
    }

    private PhotoCommentVO getPhotoComment(PhotoCommentVO vo) {
        return dao.queryObject("photoCommentMap.getPhotoComment",vo);
    }

}
