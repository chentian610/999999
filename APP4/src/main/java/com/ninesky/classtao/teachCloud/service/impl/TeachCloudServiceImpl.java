package com.ninesky.classtao.teachCloud.service.impl;

import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.teachCloud.service.TeachCloudService;
import com.ninesky.classtao.teachCloud.vo.SourceGroupVO;
import com.ninesky.classtao.teachCloud.vo.SourceVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.PostUtil;
import com.ninesky.framework.SystemConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TeachCloudServiceImpl")
public class TeachCloudServiceImpl implements TeachCloudService {

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private UserService userService;
	
	/**
	 * 添加教师资源推荐
	 * @param vo
	 */
	public void addSource(SourceVO vo) {
		//添加教学资源推荐
		int id=dao.insertObjectReturnID("sourceMap.insertSource", vo);
		vo.setId(id);
	}
	
	/**
	 * 添加云资源接收群体到teach_group中
	 */
	public void addSourceGroup(SourceVO vo,String receive_list) {
		List<SourceGroupVO> sourceList = new ArrayList<SourceGroupVO>();
		List<ReceiveVO> list=BeanUtil.jsonToList(receive_list, ReceiveVO.class);
		for (ReceiveVO rvo : list) {//兴趣班
			if (DictConstants.TEAM_TYPE_INTEREST.equals(rvo.getTeam_type())) {
				SourceGroupVO groupVO = new SourceGroupVO();
				groupVO.setTeach_source_id(vo.getId());
				groupVO.setSchool_id(vo.getSchool_id());
				groupVO.setSender_id(vo.getSender_id());
				groupVO.setGroup_id(0);
				groupVO.setTeam_id(rvo.getTeam_id());
				groupVO.setTeam_type(rvo.getTeam_type());
				groupVO.setUser_type(DictConstants.USERTYPE_STUDENT);
				groupVO.setUser_id(0);
				groupVO.setCreate_by(vo.getCreate_by());
				groupVO.setCreate_date(vo.getCreate_date());
				sourceList.add(groupVO);
			} else {
				SourceGroupVO groupVO = new SourceGroupVO();
				groupVO.setTeach_source_id(vo.getId());
				groupVO.setSchool_id(vo.getSchool_id());
				groupVO.setSender_id(vo.getSender_id());
				groupVO.setGroup_id(rvo.getGroup_id());
				groupVO.setTeam_id(rvo.getTeam_id());
				groupVO.setTeam_type(rvo.getTeam_type());
				groupVO.setUser_type(DictConstants.USERTYPE_STUDENT);
				groupVO.setUser_id(0);
				groupVO.setCreate_by(vo.getCreate_by());
				groupVO.setCreate_date(vo.getCreate_date());
				sourceList.add(groupVO);
			}
			
		}
			dao.insertObject("sourceGroupMap.insertSourceGroup", sourceList);
	}
			
			
	/**
	 * 获取教师资源推荐列表
	 * @param paramMap
	 */
	public List<?> getSourceList(Map<String, String> paramMap) {
		if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {	
			//获取发送者云资源推荐列表
			SourceGroupVO rvo = new SourceGroupVO();
			rvo.setSchool_id(ActionUtil.getSchoolID());
			rvo.setUser_id(ActionUtil.getUserID());
			List<SourceVO> sourceList = dao.queryForList("sourceMap.getSourceListBySenderId", paramMap);
			for (SourceVO vo:sourceList) {
				vo.setSend_time(vo.getCreate_date());
			}
			return sourceList;
		} else {
			//家长端获取云资源推荐列表 (家长发现)
			SourceGroupVO rvo = new SourceGroupVO();
			rvo.setSchool_id(ActionUtil.getSchoolID());
			StudentVO svo = userService.getStudentById( ActionUtil.getStudentID());
			rvo.setUser_type(DictConstants.USERTYPE_STUDENT);
			rvo.setGroup_id(svo.getGrade_id());
			rvo.setTeam_id(svo.getClass_id());
			rvo.setTeam_type("011005");//普通班级
			rvo.setTeam_type_interest("011015");//兴趣班级
			rvo.setUser_id(ActionUtil.getStudentID());
			rvo.setApp_sql(ActionUtil.getParameter("app_sql"));
			rvo.setOrder_sql(ActionUtil.getParameter("order_sql"));
			List<SourceGroupVO> receiveList = dao.queryForList("sourceGroupMap.getSourceListByStu", rvo);
			//HashMap<Integer, String> map = new HashMap<Integer, String>();
			for (SourceGroupVO vo:receiveList) {
				vo.setSend_time(vo.getCreate_date());
				vo.setSender_name(redisService.getUserName(vo.getSchool_id(), DictConstants.USERTYPE_TEACHER, vo.getCreate_by(), null));
			}
			return receiveList;
		}	
	}
		
		/**
		 * 加载资源列表
		 * @throws Exception 
		 * 		
		 */
		public List<SourceVO> getTeachCloudSource(Map<String, String> Map) throws Exception{
			HashMap<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("JsonInfo", "{\"VersionId\":\""+Map.get("VersionId")+"\",\"ExtensionName\":\""+Map.get("ExtensionName")+"\",\"Limit\":\""+Map.get("limit")+"\",\"Page_num\":\""+Map.get("page")+"\",\"IsCount_size\":\""+1+"\",\"GradeId\":\""+Map.get("GradeId")+"\",\"CoursesId\":\""+Map.get("CourseId")+"\",\"ResourceName\":\"\",\"StreamingNo\":\""+RandomStringUtils.randomAlphanumeric(25)+"\",\"TimeStamp\":\""+DateUtil.getNow("yyyy-MM-dd HH:mm:ss")+"\",\"AppId\":\""+SystemConfig.getProperty("JIAOXUEYUN_APPID")+"\",\"Source\":\""+SystemConfig.getProperty("JIAOXUEYUN_SOURCE")+"\",\"Sign\":\""+getTeachCloudSign()+"\"} ");
			String returnJson = PostUtil.doPost(SystemConfig.getProperty("JIAOXUEYUN_URL"),paraMap);
			paraMap.put("JsonInfo", "{\"VersionId\":\""+Map.get("VersionId")+"\",\"ExtensionName\":\""+Map.get("ExtensionName")+"\",\"Limit\":\""+Map.get("limit")+"\",\"Page_num\":\""+Map.get("page")+"\",\"IsCount_size\":\""+0+"\",\"GradeId\":\""+Map.get("GradeId")+"\",\"CoursesId\":\""+Map.get("CourseId")+"\",\"ResourceName\":\"\",\"StreamingNo\":\""+RandomStringUtils.randomAlphanumeric(25)+"\",\"TimeStamp\":\""+DateUtil.getNow("yyyy-MM-dd HH:mm:ss")+"\",\"AppId\":\""+SystemConfig.getProperty("JIAOXUEYUN_APPID")+"\",\"Source\":\""+SystemConfig.getProperty("JIAOXUEYUN_SOURCE")+"\",\"Sign\":\""+getTeachCloudSign()+"\"} ");
			String totalJson = PostUtil.doPost(SystemConfig.getProperty("JIAOXUEYUN_URL"),paraMap);
			JSONObject jsonObjectTotal = JSONObject.fromObject(totalJson);
			ActionUtil.setTotal(jsonObjectTotal.getInt("Total"));
			JSONArray jsonArray = JSONArray.fromObject(returnJson);
			List<SourceVO> list = new ArrayList<SourceVO>();
			for (int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				SourceVO item = new SourceVO();
				item.setSource_data(jsonObject.toString());
				item.setSource_type(jsonObject.getString("ExtensionName"));
				item.setSource_id(jsonObject.getString("ResourceId"));
				item.setSource_name(jsonObject.getString("ResourceName"));
				item.setUpdateTime(jsonObject.getString("UpdateTime"));
				item.setResourceSize(jsonObject.getString("ResourceSize"));
				item.setBrowse_count(jsonObject.getInt("Browse_count"));
				item.setPraise_count(jsonObject.getInt("Praise_count"));
				item.setDownload_count(jsonObject.getInt("Download_count"));
				item.setFav_count(jsonObject.getInt("Fav_count"));
				item.setResourceUrl(jsonObject.getString("ResourceUrl"));
				list.add(item);
			}
			return list;
			
		}

		/**
		 * 通过资源主键ID获取资源数据
		 */
		@Override
		public SourceVO getSourceByID(Integer id) {
			// TODO Auto-generated method stub
			return dao.queryObject("sourceMap.getSourceByID",id);
		}

	/**
	 * 获取教学云接口签名Sign值
	 * 签名：(AppId+Source+ secretKey+日期(如：2015-10-26)+03:30:00) 通过MD5加密构成，其中AppId+Source SecretKey由教育云平台提供
	 * @return
	 */
	private static String getTeachCloudSign(){
		return MD5Util.toMd5(SystemConfig.getProperty("JIAOXUEYUN_APPID")+SystemConfig.getProperty("JIAOXUEYUN_SOURCE")+SystemConfig.getProperty("JIAOXUEYUN_SECRETKEY")+ DateUtil.getNow("yyyy-MM-dd")+" 03:30:00");
	}

	public void addSourceCatalogList(Map<String, String> map) throws Exception{
		addTS2DB(map);
	}


	public void addTS2DB(Map<String, String> map) throws Exception{
		List<HashMap> versionList = dao.queryForList("sourceCatalogMap.getz_version");
		List<HashMap> gradeList = dao.queryForList("sourceCatalogMap.getz_grade");
		List<HashMap> courseList = dao.queryForList("sourceCatalogMap.getz_course");
		for (HashMap vmap:versionList) {
			HashMap version_no = new HashMap<String,String>();
			version_no.put("version_no",(vmap.get("version_no")+"").toLowerCase());
			dao.deleteObject("sourceCatalogMap.clearTable",version_no);
			dao.deleteObject("sourceCatalogMap.clearRemark",version_no);
			for (HashMap vgrade:gradeList) {
				for (HashMap cmap:courseList) {
					HashMap paramMap = new HashMap<String,String>();
					String versionId = vmap.get("version_no")+"";
					String gradeId = vgrade.get("grade_no")+"";
					String courseId = cmap.get("course_no")+"";
					String version_name = vmap.get("version_name")+"";
					String grade_name = vgrade.get("grade_name")+"";
					String course_name = cmap.get("course_name")+"";
					System.out.println("version_name="+version_name+",grade_name="+grade_name+",course_name="+course_name);
					paramMap.put("versionId",versionId);
					paramMap.put("courseId",courseId);
					paramMap.put("gradeId",gradeId);
					String returnJson = PostUtil.doPost("http://jiaoxueyun.cn/resources-more-inter!getVolumeAjaxs.do",paramMap);
					if (StringUtil.isEmpty(returnJson)) {
						HashMap insertRemark = new HashMap<String,String>();
						insertRemark.put("remark","出版社"+version_name+"年级"+grade_name+"课程"+course_name+"没有数据！");
						insertRemark.put("url","http://jiaoxueyun.cn/resources-more-inter!getVolumeAjaxs.do?"+prepareParam(paramMap));
						insertRemark.put("version",versionId.toLowerCase());
						dao.insertObject("sourceCatalogMap.insertRemark",insertRemark);
						continue;
					}
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					JSONArray jsonArray = JSONArray.fromObject(returnJson);
					for (int i=0;i<jsonArray.size();i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						String courseListId= jsonObject.getString("courseListId");
						String up_down_name = jsonObject.getString("courseListName");
						paramMap.put("volumeId",courseListId);
						String courseListJson = PostUtil.doPost("http://jiaoxueyun.cn/resources-more-inter!getTrees.do",paramMap);
						if (StringUtil.isEmpty(courseListJson)) {
							HashMap insertRemark = new HashMap<String,String>();
							insertRemark.put("remark","出版社"+version_name+"年级"+grade_name+"课程"+course_name+"没有数据！");
							insertRemark.put("url","http://jiaoxueyun.cn/resources-more-inter!getTrees.do?"+prepareParam(paramMap));
							insertRemark.put("version",versionId.toLowerCase());
							dao.insertObject("sourceCatalogMap.insertRemark",insertRemark);
							continue;
						}
						JSONArray json2Array = JSONArray.fromObject(courseListJson);
						String temp_part_name = "";
						String temp_parentID = courseListId;

						for (int j=0;j<json2Array.size();j++) {
							JSONObject cObject = (JSONObject) json2Array.get(j);
							Map<String, Object> hashMap = BeanUtil.jsonToMap(cObject.toString());
							hashMap.put("version_name",version_name);
							hashMap.put("grade_name",grade_name);
							hashMap.put("course_name",course_name);
							hashMap.put("table_name",versionId.toLowerCase());
							hashMap.put("up_down_name",up_down_name);
							if (hashMap.get("ParentId") == null || "null".equals(hashMap.get("ParentId")+"")) continue;//第一层;
							if (hashMap.get("ParentId").equals(temp_parentID)) {
								temp_part_name = hashMap.get("CourseListName")+"";
								hashMap.put("CourseListName","");
							}
							hashMap.put("part_name",temp_part_name);
							list.add(hashMap);
						}
						if (list.isEmpty()) {
							HashMap insertRemark = new HashMap<String,String>();
							insertRemark.put("remark","出版社"+version_name+"年级"+grade_name+"课程"+course_name+"没有数据！");
							insertRemark.put("url","http://jiaoxueyun.cn/resources-more-inter!getVolumeAjaxs.do?"+prepareParam(paramMap));
							insertRemark.put("version",versionId.toLowerCase());
							dao.insertObject("sourceCatalogMap.insertRemark",insertRemark);
							continue;
						}


						dao.insertObject("sourceCatalogMap.insertData"+versionId.toLowerCase(),list);
					}
				}
			}
		}
	}

	private String prepareParam(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		if (paramMap.isEmpty()) return "";
		for (String key : paramMap.keySet()) {
			String value = (String) paramMap.get(key);
			if (sb.length() < 1) sb.append(key).append("=").append(value);
			else sb.append("&").append(key).append("=").append(value);
		}
		return sb.toString();
	}
}
