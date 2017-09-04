package com.ninesky.classtao.news.service.impl;

import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.news.service.NewsService;
import com.ninesky.classtao.news.vo.NewsFileVO;
import com.ninesky.classtao.news.vo.NewsItemVO;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.system.vo.SortVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value="NewsServiceImpl")
public class NewsServiceImpl implements NewsService{
	@Autowired
	private GeneralDAO dao;
	@Autowired
	private UserService userService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private JedisDAO jedisDao;
	/**
	 * 添加校园风采
	 * @param vo
	 */
	public void addNews(NewsVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		if (StringUtil.isEmpty(vo.getTemplate_type())) vo.setTemplate_type(DictConstants.NEWS_TEMPLATE_TYPE_DEFINE);
		if (StringUtil.isEmpty(vo.getDept_name()))
			vo.setDept_name(ActionUtil.getParameter("user_name"));
		if (StringUtil.isEmpty(vo.getDeploy_date()))
			vo.setDeploy_date(DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd"));
		vo.setNews_id(dao.insertObjectReturnID("newsMap.insertNews", vo));
		String value = SchoolConfig.getProperty("NEWS_TEMPLATE_ON");
		if (Constants.ON.equals(value)) {
			addNewsItemList(vo.getNews_id(),BeanUtil.jsonToList(vo.getItem_list(),NewsItemVO.class));
			addNewsFileList(vo.getNews_id(),BeanUtil.jsonToList(vo.getFile_list(),NewsFileVO.class));
		}
		redisService.addNewsToRedis(vo);
	}
	
	/**
	 * 获取校园风采列表
	 * @param paramMap
	 * @return
	 */
	public List<NewsVO> getNewsList(Map<String, String> paramMap){
		paramMap.put("school_id",ActionUtil.getSchoolID()+"");
		List<NewsVO> newsList = dao.queryForList("newsMap.getNewsList", paramMap);
		for (NewsVO newsVO : newsList) {
			if (StringUtil.isEmpty(newsVO.getContent_text())) continue;
			if (DictConstants.NEWS_CONTENT_IS_RESIZE.equals(paramMap.get("is_resize"))) {
				newsVO.setContent_text(subContentText(newsVO.getContent_text()));
			}
		}
		return newsList;
	}

	/**
	 * 获取指定校园风采
	 * @param news_id
	 * @return
	 */
	public NewsVO getNewsByID(Integer news_id){
		NewsVO vo = dao.queryObject("newsMap.getNewsByID", news_id);
		if (vo == null) throw new BusinessException(MsgService.getMsg("NEWS_NOT_EXISTS_OR_DELETE"));
		vo.setCss_list(getNewsCssOtherFieldByCode(vo));
		if (Constants.ON.equals(SchoolConfig.getProperty("NEWS_TEMPLATE_ON"))) {
			vo.setItem_list(BeanUtil.ListTojson(getNewsItemList(news_id))+"");
			vo.setFile_list(BeanUtil.ListTojson(getNewsFileList(news_id))+"");
		}
		return vo;
	}

	private String getNewsCssOtherFieldByCode(NewsVO vo){
		DictSchoolVO dict = new DictSchoolVO();
		dict.setDict_code(vo.getNews_code());
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict = dao.queryObject("dictSchoolMap.getDictSchoolListForNews",dict);
		return StringUtil.isNotEmpty(dict.getCss_list())?dict.getCss_list():"";
	}

	/**
	 * 删除指定校园风采
	 * @param news_id
	 */
	public void deleteNews(Integer news_id){
		dao.deleteObject("newsMap.deleteNewsById", news_id);
		if (Constants.ON.equals(SchoolConfig.getProperty("NEWS_TEMPLATE_ON"))) {
			deleteNewsItem(news_id);
			deleteNewsFile(news_id);
		}
		redisService.deleteNewsList(ActionUtil.getParameterMap());
	}

	/**
	 * 更新校园风采
	 * @param vo
	 */
	public void updateNews(NewsVO vo){
		dao.updateObject("newsMap.updateNewsById", vo);
		if (Constants.ON.equals(SchoolConfig.getProperty("NEWS_TEMPLATE_ON"))) {
			deleteNewsItem(vo.getNews_id());
			deleteNewsFile(vo.getNews_id());
			addNewsItemList(vo.getNews_id(),BeanUtil.jsonToList(vo.getItem_list(),NewsItemVO.class));
			addNewsFileList(vo.getNews_id(),BeanUtil.jsonToList(vo.getFile_list(),NewsFileVO.class));
		}
		String newsKey = RedisKeyUtil.getNewsKey(ActionUtil.getSchoolID(),vo.getDict_group(),vo.getNews_id());
		jedisDao.hset(newsKey, "news_id",vo.getNews_id().toString().trim());
		jedisDao.hset(newsKey, "dict_group",vo.getDict_group());
		jedisDao.hset(newsKey, "news_code", vo.getNews_code());
		jedisDao.hset(newsKey, "title",  StringUtil.subString(vo.getTitle(), 20));
		jedisDao.hset(newsKey, "content_text",  StringUtil.subString(vo.getContent_text(),50));
		if (StringUtil.isNotEmpty(vo.getMain_pic_url())) jedisDao.hset(newsKey, "main_pic_url", vo.getMain_pic_url());
		jedisDao.hset(newsKey, "deploy_date", vo.getDeploy_date());
	}
	/**
	 * 添加动态
	 * @param vo
	 */
	public void addInformation(NewsVO vo) {
		if(StringUtil.isEmpty(vo.getTemplate_type()))
			throw new BusinessException("请指定具体模块...");
		InfoVO infoVO = new InfoVO();
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setCreate_date(vo.getCreate_date());
		infoVO.setCreate_by(vo.getCreate_by());
		infoVO.setInfo_date(DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd"));
        infoVO.setModule_code(vo.getModule_code());
		infoVO.setModule_pkid(vo.getNews_id());
		infoVO.setInfo_title(vo.getTitle());
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		infoVO.setInfo_content(StringUtil.isEmpty(vo.getContent_text())?vo.getTitle():vo.getContent_text());
		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
		receiveList.addAll(getStuUserList(infoVO));
		receiveList.addAll(getTeacherUserList(infoVO));
		infoService.addInfo(infoVO, receiveList);
	}

	//获取学校学生接收
	private List<InfoReceiveVO> getStuUserList(InfoVO infoVO){
		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
		StudentVO stuVO = new StudentVO();
		stuVO.setSchool_id(infoVO.getSchool_id());
		List<StudentVO> stuUserList = userService.getStuUserList(stuVO);
		if(ListUtil.isEmpty(stuUserList)) return receiveList;
		for(StudentVO student : stuUserList)
		{
			InfoReceiveVO vo = new InfoReceiveVO();
			vo.setInfo_id(infoVO.getInfo_id());
			vo.setSchool_id(infoVO.getSchool_id());
			vo.setGrade_id(student.getGrade_id());
			vo.setClass_id(student.getClass_id());
			vo.setUser_id(0);
			vo.setModule_code(infoVO.getModule_code());
			vo.setModule_pkid(infoVO.getModule_pkid());
			vo.setUser_type(DictConstants.USERTYPE_STUDENT);
			vo.setReceive_name(student.getStudent_name());
			vo.setInfo_date(DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd"));
			vo.setInfo_type(infoVO.getInfo_type());
			vo.setInfo_url(infoVO.getInfo_url());
			vo.setInfo_title(infoVO.getInfo_title());
			vo.setInfo_content(infoVO.getInfo_content());
			vo.setShow_type(infoVO.getShow_type());
			vo.setInit_data(infoVO.getInit_data());
			vo.setPhoto_list(infoVO.getPhoto_list());
			vo.setCount_info(infoVO.getCount_info());
			vo.setCreate_date(infoVO.getCreate_date());
			vo.setCreate_by(infoVO.getCreate_by());
			receiveList.add(vo);
		}
		return receiveList;
	}


	//获取学校老师接收
	private List<InfoReceiveVO> getTeacherUserList(InfoVO infoVO){
		List<InfoReceiveVO> receiveList = new ArrayList<InfoReceiveVO>();
		TeacherVO teaVO = new TeacherVO();
		teaVO.setSchool_id(infoVO.getSchool_id());
		List<TeacherVO> teaUserList = userService.getTeaUserList(teaVO);
		if(ListUtil.isEmpty(teaUserList)) return receiveList;
		for(TeacherVO teacher : teaUserList)
		{
			if(teacher.getUser_id()==null) continue;
			InfoReceiveVO vo = new InfoReceiveVO();
			vo.setInfo_id(infoVO.getInfo_id());
			vo.setSchool_id(teacher.getSchool_id());
			vo.setGrade_id(teacher.getGrade_id());
			vo.setClass_id(teacher.getClass_id());
			vo.setUser_id(teacher.getUser_id());
			vo.setModule_code(infoVO.getModule_code());
			vo.setModule_pkid(infoVO.getModule_pkid());
			vo.setUser_type(DictConstants.USERTYPE_TEACHER);
			vo.setReceive_name(teacher.getTeacher_name());
			vo.setInfo_date(DateUtil.formatDateToString(ActionUtil.getSysTime(), "yyyy-MM-dd"));
			vo.setInfo_type(infoVO.getInfo_type());
			vo.setInfo_url(infoVO.getInfo_url());
			vo.setInfo_title(infoVO.getInfo_title());
			vo.setInfo_content(infoVO.getInfo_content());
			vo.setShow_type(infoVO.getShow_type());
			vo.setInit_data(infoVO.getInit_data());
			vo.setPhoto_list(infoVO.getPhoto_list());
			vo.setCount_info(infoVO.getCount_info());
			vo.setCreate_date(infoVO.getCreate_date());
			vo.setCreate_by(infoVO.getCreate_by());
			receiveList.add(vo);
		}
		filterRepeatReceive(receiveList);
		return receiveList;
	}
	//内容截取截取
	private String subContentText(String contentText){
		if(StringUtil.isEmpty(contentText)) return null;
		if(contentText.length()<Constants.DEFAULT_SUBSTRING_LENGTH)
			return contentText;
		return contentText.substring(0,Constants.DEFAULT_SUBSTRING_LENGTH) + "......";
	}
	//过滤重复的动态接收者(用户id和用户类型都相同的过滤)
	private void filterRepeatReceive(List<InfoReceiveVO> list){
		 for (int i = 0; i < list.size() - 1; i++) {
	           for (int j = list.size() - 1; j > i; j--) {
	               if(list.get(j).getUser_id().equals(list.get(i).getUser_id()) &&
	            		 list.get(j).getUser_type().equals(list.get(i).getUser_type())){
	                list.remove(j);
	               }
	           }
	     }
	}

	@Override
	public List<DictSchoolVO> getNewsListOfLogin(DictSchoolVO vo) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("school_id",ActionUtil.getSchoolID());
		paramMap.put("is_text", DictConstants.TRUE);
		List<DictSchoolVO> dictList = dao.queryForList("dictSchoolMap.getDictSchoolListForNews", vo);
		String newsKey = RedisKeyUtil.getNewsListOfLoginKey(ActionUtil.getSchoolID(),vo.getDict_group());//判断dict_group是否存在,空就取search的值
		if (jedisDao.exists(newsKey) || StringUtil.isNotEmpty(jedisDao.keys(newsKey+"*"))) {//判断key是否存在
			for (DictSchoolVO dictVO:dictList) {
				String newsGroupKey = RedisKeyUtil.getNewsListOfLoginKey(ActionUtil.getSchoolID(), dictVO.getDict_group());
				dictVO.setNews_list(jedisDao.hget(newsGroupKey, dictVO.getDict_code()));
			}
		} else {
			for (DictSchoolVO dict:dictList) {//循环二级栏目列表(可能是一个新闻模块的二级栏目或者是所有新闻模块的二级栏目)
				String newsGroupKey = RedisKeyUtil.getNewsListOfLoginKey(ActionUtil.getSchoolID(), dict.getDict_group());
                Map<String,Object> map = BeanUtil.jsonToMap(dict.getCss_list());
				ActionUtil.getActionParam().setLimit(IntegerUtil.getValue(map.get("limit")));//限制每个栏目所获取的新闻条数
				paramMap.put("news_code", dict.getDict_code());
				paramMap.put("dict_group", dict.getDict_group());
				List<NewsVO> newsList = dao.queryForList("newsMap.getNewsList", paramMap);
				for (NewsVO news:newsList) {
					news.setContent_text(StringUtil.subString(news.getContent_text(),50));
				}
				dict.setNews_list(BeanUtil.ListTojson(newsList) + "");//将获取到的新闻信息转换成json格式的字符串
				jedisDao.hset(newsGroupKey, dict.getDict_code(), dict.getNews_list());
				jedisDao.expire(newsKey, 180);//设置key的过期时间为3分钟
			}
		}
		return dictList;
	}

	/**
	 * 获取新闻信息
	 * @param paramMap
	 * @return
	 */
	public List<DictSchoolVO> getNewsListForAPP(DictSchoolVO vo) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("school_id",ActionUtil.getSchoolID());
		paramMap.put("is_text", "1");
		List<DictSchoolVO> dictSchoolList = dao.queryForList("dictSchoolMap.getDictSchoolListForNews", vo);
		for (DictSchoolVO dictSchoolVO : dictSchoolList) {//循环二级栏目列表(可能是一个新闻模块的二级栏目或者是所有新闻模块的二级栏目)
            Map<String,Object> map = BeanUtil.jsonToMap(dictSchoolVO.getCss_list());
            ActionUtil.getActionParam().setLimit(IntegerUtil.getValue(map.get("limit")));//限制每个栏目所获取的新闻条数
			paramMap.put("news_code", dictSchoolVO.getDict_code());
			paramMap.put("dict_group", dictSchoolVO.getDict_group());
			List<NewsVO> newsList = dao.queryForList("newsMap.getNewsList", paramMap);
//			for (NewsVO news:newsList) {
//				news.setContent_text(StringUtil.subString(news.getContent_text(),50));
//			}
			dictSchoolVO.setNews_list(BeanUtil.ListTojson(newsList)+"");//转换成json字符串
		}
		return dictSchoolList;
	}

	/**
	 * 将分段内容插入到子表中
	 * @param news_id
	 * @param itemList
	 */
	private void addNewsItemList(Integer news_id,List<NewsItemVO> itemList){
		if (ListUtil.isEmpty(itemList)) return;
		for (NewsItemVO itemVO:itemList) {
			itemVO.setNews_id(news_id);
			itemVO.setSchool_id(ActionUtil.getSchoolID());
			itemVO.setCreate_by(ActionUtil.getUserID());
			itemVO.setCreate_date(ActionUtil.getSysTime());
		}
		dao.insertObjectReturnID("newsItemMap.insertNewsItemBatch",itemList);
	}
	/**
	 * 将图片插入到子表中
	 * @param news_id
	 * @param fileList
	 */
	private void addNewsFileList(Integer news_id,List<NewsFileVO> fileList){
		if (ListUtil.isEmpty(fileList)) return;
		for (NewsFileVO itemVO:fileList) {
			itemVO.setNews_id(news_id);
			itemVO.setSchool_id(ActionUtil.getSchoolID());
			itemVO.setCreate_by(ActionUtil.getUserID());
			itemVO.setCreate_date(ActionUtil.getSysTime());
		}
		dao.insertObjectReturnID("newsFileMap.insertNewsFileBatch",fileList);
	}

	/**
	 * 获取新闻资讯子项列表
	 * @param news_id
	 */
	private List<NewsItemVO> getNewsItemList(Integer news_id){
		return dao.queryForList("newsItemMap.getNewsItemList",news_id);
	}
	/**
	 * 获取新闻资讯图片列表
	 * @param news_id
	 */
	private List<NewsFileVO> getNewsFileList(Integer news_id){
		return dao.queryForList("newsFileMap.getNewsFileList",news_id);
	}


    /**
     * 删除新闻子项
     * @param news_id
     */
	private void deleteNewsItem(Integer news_id) {
		dao.deleteObject("newsItemMap.deleteNewsItemByID", news_id);
	}


	/**
	 * 删除附件子项
	 * @param news_id
	 */
	private void deleteNewsFile(Integer news_id) {
		dao.deleteObject("newsFileMap.deleteNewsFileByID", news_id);
	}

	public void addSchoolDictGroupList(Integer school_id){
		List<DictVO> dictList = dao.queryForList("dictMap.getSubDictListByGroup","022");
		for (DictVO dict:dictList){
			dict.setSchool_id(school_id);
			dict.setId(dao.insertObjectReturnID("dictMap.addInterestCourse",dict));
		}
	}
}
