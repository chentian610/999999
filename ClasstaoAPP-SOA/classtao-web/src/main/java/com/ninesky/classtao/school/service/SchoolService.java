package com.ninesky.classtao.school.service;

import java.util.List;
import java.util.Map;

import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.school.vo.LinkManVO;
import com.ninesky.classtao.school.vo.SchoolMainVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.school.vo.SchoolConfigVO;
import com.ninesky.classtao.system.vo.AppVO;
import com.ninesky.classtao.system.vo.DictSchoolVO;

public interface SchoolService {
	/**
	 * 通过管理员手机号码获取学校
	 * @param vo
	 * @return
	 */
	public SchoolVO getSchoolByAdminPhone(String phone);//获取学校年级列表
	
	public String getAdminPhone(Integer school_id);//获取学校管理员联系方式
	
	/**
	 * 获取学校信息
	 * @param vo
	 * @return
	 */
	public SchoolVO getSchoolById(Integer vo);
	
	/**
	 * 通过域名获取学校信息
	 * @param domain
	 * @return
	 */
	public SchoolVO getSchoolByDomain(String domain);
	
	/**
	 * 添加学校
	 * @param vo
	 * @return
	 */
	public SchoolVO addSchool(SchoolVO vo);
	
	/**
	 * 获取学校申请列表
	 * @param vo
	 * @return
	 */
	public List<SchoolVO> getSchoolApplyList(SchoolVO vo);
	
	
	/**
	 * 获取学校申请列表
	 * @param vo
	 * @return
	 */
	public List<SchoolVO> getSchoolList(SchoolVO vo);

	/**
	 * 获取代理商申请学校列表
	 * @param vo
	 * @return
	 */
	public List<SchoolVO> getAgentApplySchoolList(SchoolVO vo);
	
	/**
	 * 通过学校学校申请
	 * @param vo
	 * @return
	 */
	public void passSchoolApply(SchoolVO vo);
	
	/**
	 * 判断域名是否重复
	 * @param vo
	 * @return
	 */
	public void examineDomainName(SchoolMainVO vo);

	/**
	 * 添加学校联系人到临时表
	 * @param vo
	 * @return
	 */
	public void addLinkManFromBaidu(LinkManVO vo);

	public void  removeSchool(SchoolVO vo);

	/**
	 * 获取学校APP更新情况
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getSchoolAPPUpdateList(Map<String, String> paramMap);

	/**
	 * 获取学校APP更新情况
	 * @param vo
	 * @return
	 */
	public List<AppVO> getSchoolAPPUpdateListByID(SchoolVO vo);

	/**
	 * 获取学校服务器配置
	 * @param school_id
	 * @return
	 */
	public SchoolConfigVO getSchoolServerConfig(Integer school_id);

	/**
	 * 修改学校相关信息
	 * @param vo
	 */
	public void updateSchool(SchoolVO vo);

	/**
	 * 获取学校官网信息
	 * @param vo
	 * @return
	 */
	public SchoolMainVO getSchoolMainInfo(SchoolMainVO vo);

	/**
	 * 修改学校官网信息
	 * @param vo
	 * @return
	 */
	public SchoolMainVO updateSchoolMainInfo(SchoolMainVO vo);

	/**
	 * 获取学校栏目和对应新闻列表
	 * @param school_id
	 * @return
	 */
	public List<DictSchoolVO> getSchoolColumnAndNewsList(Integer school_id);

	/**
	 * 获取学校栏目对应新闻集合
	 * @param map
	 * @return
	 */
	public List<NewsVO> getSchoolColumnNewsList(Map<String, String> map);
}
