package com.ninesky.classtao.school.service.impl;


import com.ninesky.classtao.balance.service.BalanceService;
import com.ninesky.classtao.balance.vo.BalanceVO;
import com.ninesky.classtao.menu.service.MenuService;
import com.ninesky.classtao.message.service.MessageService;
import com.ninesky.classtao.module.service.ModuleService;
import com.ninesky.classtao.module.vo.SchoolModuleVO;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.LinkManVO;
import com.ninesky.classtao.school.vo.SchoolConfigVO;
import com.ninesky.classtao.school.vo.SchoolMainVO;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.score.service.ScoreTableService;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.AppVO;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.template.service.TemplateService;
import com.ninesky.classtao.template.vo.ModuleVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserRoleVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("schoolServiceImpl")
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private ModuleService schoolModuleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private ScoreTableService scoreTableService;

	@Autowired
	private DictService dictService;

	@Autowired
	private BalanceService balanceService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private MenuService menuService;

	public SchoolVO getSchoolByAdminPhone(String phone) {
		// TODO Auto-generated method stub
		return dao.queryObject("schoolMap.getSchoolByAdminPhone", phone);
	}

	//获取校方管理员联系方式
	public String getAdminPhone(Integer school_id) {
		return dao.queryObject("schoolMap.getSchoolAdminPhone", school_id);
	}
	/**
	 * 获取学校信息
	 * @param school_id
	 * @return
	 */
	public SchoolVO getSchoolById(Integer school_id){
		return dao.queryObject("schoolMap.getSchoolInfo", school_id);
	}
	/**
	 * 添加学校
	 * @param vo
	 * @return
	 */ 
	public SchoolVO addSchool(SchoolVO vo){
		if (IntegerUtil.isEmpty(vo.getSchool_id())) {//初始化新建学校
            UserRoleVO roleVO = new UserRoleVO();
            roleVO.setUser_type(DictConstants.USERTYPE_ADMIN);
            String[] school_admin_phones = vo.getSchool_admin_phone().split(",");
            for(String phone : school_admin_phones) {
                roleVO.setPhone(phone);
                List<UserRoleVO> ulist = userService.getUserRoleByPhone(roleVO);
                if (ListUtil.isNotEmpty(ulist))
                    throw new BusinessException(MsgService.getMsg("SCH_REPEAT_PHONE", phone));//如果该电话号码已经是某学校的管理人员就抛出异常
            }
			vo.setApp_status(DictConstants.SCH_STATUS_APPLY);
			vo.setStatus(DictConstants.SCH_STATUS_APPLY);
			vo.setCreate_by(ActionUtil.getUserID());
			vo.setCreate_date(ActionUtil.getSysTime());
			vo.setSchool_id(dao.insertObjectReturnID("schoolMap.insertSchool", vo));//插入新学校
            initSchoolUserRole(vo);//初始化学校管理员
			//添加学校模块
			addSchoolModules(vo);
			//添加默认的扣分原因
			vo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("scoreReasonMap.initSchoolScoreReason", vo);
			//添加默认的学校字典项目
			dictService.initSchoolDict(vo.getSchool_id());
			//初始化统计表头
			scoreTableService.initTableHead(vo.getSchool_id());
			//初始化服务器配置
			insertSchoolServerConfig(vo);
			//初始化学校角色菜单
			menuService.initSchoolRoleMenu(vo.getSchool_id());
            //插入学校APP配置
            dictService.insertSchoolAPPDict(vo);
		} else {//当学校已经存在时，修改模块列表和服务器配置走这里
			if (IntegerUtil.isEmpty(vo.getSchool_id())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
			updateSchoolModules(vo);//修改模块列表
			updateSchoolServer(vo);//修改服务器配置
			updateSchoolMainInfo(new SchoolMainVO(vo.getSchool_id(),vo.getMain_url()));//修改学校官网
			menuService.updateRoleMenu(vo);//修改学校菜单
		}
		return vo;
	}

    private void initSchoolUserRole(SchoolVO vo){
        if (StringUtil.isEmpty(vo.getUser_role_list()))  throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        List<UserVO> list = BeanUtil.jsonToList(vo.getUser_role_list(),UserVO.class);
        for (UserVO userVO:list) {
            //添加学校管理身份
            UserRoleVO userRoleVO = new UserRoleVO();
            userRoleVO.setSchool_id(vo.getSchool_id());
            userRoleVO.setUser_type(DictConstants.USERTYPE_ADMIN);
            dao.deleteObject("userRoleMap.deleteUserRole", userRoleVO);
            userRoleVO.setPhone(userVO.getPhone());
            userRoleVO.setCreate_by(ActionUtil.getUserID());
            userRoleVO.setCreate_date(ActionUtil.getSysTime());
            userService.addUserRole(userRoleVO);
            //将管理员数据同步到用户表
            userVO.setStatus(DictConstants.STATUS_NORMAL);
            userVO.setPass_word(Constants.RESET_PASSWORD);
            userVO.setCreate_date(ActionUtil.getSysTime());
            userVO.setUpdate_date(ActionUtil.getSysTime());
            userVO.setUpdate_by(ActionUtil.getUserID());
            userService.insertAdmin(userVO);
        }
    }

	/**
	 * 获取学校申请列表
	 * @param vo
	 * @return
	 */
	public List<SchoolVO> getSchoolApplyList(SchoolVO vo){
		vo.setStatus(DictConstants.SCH_STATUS_APPLY);
		return dao.queryForList("schoolMap.getSchoolApplyList", vo);
	}
	/**
	 * 获取申请状态的指定学校
	 * @param vo
	 * @return
	 */
	public SchoolVO getApplySchoolByAdminPhone(SchoolVO vo) {
		return dao.queryObject("schoolMap.getApplySchoolByAdminPhone", vo);
	}

	/**
	 * 通过或者拒绝学校学校申请
	 * @param vo
	 * @return
	 */
	public void passSchoolApply(SchoolVO vo){
		dao.updateObject("schoolMap.updateSchoolById", vo);
		//通过审核及就为管理员添加身份
		if(DictConstants.SCH_STATUS_PASS.equals(vo.getStatus()))
			insertUserInfo(vo);
		sendMessageForApply(vo);//短信提醒
	}

	private void updateSchoolModules(SchoolVO vo){//修改学校模块列表
		if (StringUtil.isEmpty(vo.getModule_ids())) return;
		String[] module_ids = vo.getModule_ids().split(",");
		List<SchoolModuleVO> list = new ArrayList<SchoolModuleVO>();
		for(String module_id : module_ids) {
			SchoolModuleVO schoolModuleVO = new SchoolModuleVO(vo.getSchool_id(),IntegerUtil.getValue(module_id));
			SchoolModuleVO schoolModuleVO1 = dao.queryObject("schoolModuleMap.getSchoolModuleByModuleID",schoolModuleVO);
			if (schoolModuleVO1 == null) {
				configSchoolModule(vo,list,module_id);
			} else {
				dao.insertObject("schoolModuleMap.deleteSchoolModule", schoolModuleVO1);
			}
		}
		schoolModuleService.addModuleList(list);
	}

	private void configSchoolModule(SchoolVO vo,List<SchoolModuleVO> list ,String module_id){//配置SchoolMoudleVO实体类
		ModuleVO module = templateService.getModuleById(Integer.parseInt(module_id));
		if(module == null) return;
		SchoolModuleVO schoolMod = new SchoolModuleVO();
		schoolMod.setSchool_id(vo.getSchool_id());
		schoolMod.setUser_type(module.getUser_type());//默认003
		schoolMod.setModule_code(module.getModule_code());
		schoolMod.setModule_name(module.getModule_name());
		schoolMod.setIcon_url(module.getIcon_url());
		schoolMod.setCreate_by(vo.getCreate_by());
		schoolMod.setCreate_date(vo.getCreate_date());
		schoolMod.setModule_url(module.getModule_url());
		schoolMod.setParent_code(module.getParent_code());
		schoolMod.setPartner_code(module.getPartner_code());
		list.add(schoolMod);
	}

	//添加学校模块
	private void addSchoolModules(SchoolVO vo){
		if(StringUtil.isEmpty(vo.getModule_ids())) return;
		String[] module_ids = vo.getModule_ids().split(",");
		List<SchoolModuleVO> list = new ArrayList<SchoolModuleVO>();
		for(String module_id : module_ids) {
			configSchoolModule(vo,list,module_id);
		}
		schoolModuleService.addModuleList(list);
	}

	//为管理员注册用户
	private void insertUserInfo(SchoolVO vo){
		addUserRoleList(vo);//更新学校管理账号
		String[] school_admin_phones = vo.getSchool_admin_phone().split(",");
		for(String phone : school_admin_phones) {
			UserVO user = new UserVO();
			user.setPhone(phone);
			user.setPass_word(MD5Util.toMd5(Constants.DEFAULT_PASSWORD));
			user.setStatus(DictConstants.STATUS_NORMAL);
			user.setHead_url(Constants.HEAD_URL_DEFAULT);
			user.setCreate_by(ActionUtil.getUserID());
			user.setCreate_date(ActionUtil.getSysTime());
			userService.insertAdmin(user);
		}
	}
	//发送短信提醒申请情况
	private void sendMessageForApply(SchoolVO vo){
		if (DictConstants.SCH_STATUS_APPLY.equals(vo.getStatus())) return;
		String[] school_admin_phones = vo.getSchool_admin_phone().split(",");
		for(String phone : school_admin_phones) {
			if(DictConstants.SCH_STATUS_PASS.equals(vo.getStatus())){
				UserVO user=userService.getUserByPhone(phone);
				if(MD5Util.toMd5(Constants.DEFAULT_PASSWORD).equals(user.getPass_word()))
					//vo.setContent("恭喜您！学校APP审核通过！请用注册手机号登录,初始密码："+Constants.DEFAULT_PASSWORD);
					vo.setContent(MsgService.getMsg("SCHOOL_APPLY_PASS",redisService.getSchoolName(vo.getSchool_id()),phone,Constants.DEFAULT_PASSWORD));
				else vo.setContent("您申请的"+redisService.getSchoolName(vo.getSchool_id())+"APP已经生成，请使用账号"+phone+",该手机号已使用过，请使用原有密码登录");
			}else if(DictConstants.SCH_STATUS_RETURN.equals(vo.getStatus())){
				vo.setContent(MsgService.getMsg("SCHOOL_APPLY_RETURN",redisService.getSchoolName(vo.getSchool_id())));
			}
			messageService.sendMessage(phone, vo.getContent());
		}
	}

	@Override
	public List<SchoolVO> getSchoolList(SchoolVO vo) {
		return dao.queryForList("schoolMap.getSchoolList", vo);
	}

	@Override
	public SchoolVO getSchoolByDomain(String domain) {
		dao.insertObject("schoolMap.insertSchoolTT",ActionUtil.getSysTime()+"");
		SchoolVO vo = dao.queryObject("schoolMap.getSchoolByDomain", domain);
		if (vo != null) throw new RuntimeException("dddd");
		return vo;
	}

	@Override
	public void examineDomainName(SchoolMainVO vo) {
		SchoolVO school = dao.queryObject("schoolMap.getSchoolInfoByDomain", vo);
		if (school != null) throw new BusinessException(MsgService.getMsg("USED_DOMAIN"));
	}

    @Override
    public void addLinkManFromBaidu(LinkManVO vo) {
        dao.insertObject("schoolMap.insertSchoolLinkman",vo);
    }

    private void addUserRoleList(SchoolVO vo){
		UserRoleVO userRoleVO = new UserRoleVO();
		userRoleVO.setSchool_id(vo.getSchool_id());
		userRoleVO.setUser_type(DictConstants.USERTYPE_ADMIN);
		dao.deleteObject("userRoleMap.deleteUserRole",userRoleVO);
		String[] school_admin_phones = vo.getSchool_admin_phone().split(",");
		for(String phone : school_admin_phones) {
			UserRoleVO roleVO = new UserRoleVO();
			roleVO.setSchool_id(vo.getSchool_id());
			roleVO.setPhone(phone);
			roleVO.setCreate_by(ActionUtil.getUserID());
			roleVO.setCreate_date(ActionUtil.getSysTime());
			userService.addUserRole(roleVO);
		}
	}

	//清除与相应学校所匹配的各项系统配置
	public void  removeSchool(SchoolVO vo) {
        if (IntegerUtil.isEmpty(vo.getSchool_id())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		dao.deleteObject("schoolMap.removeSchoolByID",vo.getSchool_id());//删除学校
		dao.deleteObject("schoolModuleMap.deleteSchoolModuleBySchoolID",vo.getSchool_id());//删除学校模板
		dao.deleteObject("userRoleMap.deleteUserRole",new UserRoleVO(vo.getSchool_id()));//删除学校管理用户
		dao.deleteObject("scoreReasonMap.deleteScoreReasonBySchoolID",vo.getSchool_id());//删除扣分类型
		dao.deleteObject("dictSchoolMap.deleteSchoolDict",vo.getSchool_id());//删除学校字典项目
		dao.deleteObject("tableMap.deleteTableHeadByID", vo.getSchool_id());//删除表头
        dao.deleteObject("roleMenuMap.deleteRoleMenuBySchoolID",vo.getSchool_id());
	}

	public List<Map<String,Object>> getSchoolAPPUpdateList(Map<String,String> paramMap){
    	if (StringUtil.isEmpty(paramMap.get("status"))) paramMap.put("status",DictConstants.SCH_STATUS_GO_ONLINE+","+DictConstants.SCH_STATUS_OFFLINE);
    	return dao.queryForList("schoolMap.getSchoolAPPUpdateList",paramMap);
	}

	public List<AppVO> getSchoolAPPUpdateListByID(SchoolVO vo){
		return dao.queryForList("schoolMap.getSchoolAPPUpdateHistoryList",vo);
	}

	//修改服务器配置
	public void updateSchoolServer(SchoolVO vo){
		if (StringUtil.isEmpty(vo.getSchool_server())) return;
		dao.deleteObject("schoolConfigMap.deleteSchoolServerBySchoolID",vo.getSchool_id());
		insertSchoolServerConfig(vo);
	}

	//初始化学校服务器配置
	private void insertSchoolServerConfig(SchoolVO vo){
		if (StringUtil.isEmpty(vo.getSchool_server())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		List<SchoolConfigVO> configVOList = BeanUtil.jsonToList(vo.getSchool_server(),SchoolConfigVO.class);
		configVOList.get(0).setSchool_id(vo.getSchool_id());
		configVOList.get(0).setCreate_by(ActionUtil.getUserID());
		configVOList.get(0).setCreate_date(ActionUtil.getSysTime());
		dao.insertObjectReturnID("schoolConfigMap.insertServerConfig",configVOList.get(0));
	}

	public List<SchoolVO> getAgentApplySchoolList(SchoolVO vo){
		return dao.queryForList("schoolMap.getAgentApplySchoolList",vo);
	}

	public SchoolConfigVO getSchoolServerConfig(Integer school_id){
		return dao.queryObject("schoolConfigMap.getSchoolServerConfig",school_id);
	}

	public void updateSchool(SchoolVO vo){
		if (DictConstants.SCH_STATUS_PASS.equals(vo.getApp_status())) {
			BalanceVO balanceVO = new BalanceVO();
			balanceVO.setAgent_id(vo.getAgent_id());
			balanceVO.setConsumption_money(vo.getUnit_price());
			balanceService.consumptionBalance(balanceVO);
			if (getSchoolServerConfig(vo.getSchool_id()) == null) throw new BusinessException(MsgService.getMsg("SCHOOL_SERVER_CONFIG_ERROR"));
		}
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		if (DictConstants.SCH_STATUS_PASS.equals(vo.getStatus()) || DictConstants.SCH_STATUS_RETURN.equals(vo.getStatus())) {
			vo.setSchool_admin_phone(getSchoolById(vo.getSchool_id()).getSchool_admin_phone());
			passSchoolApply(vo);
		} else dao.updateObject("schoolMap.updateSchoolById", vo);
	}

	public SchoolMainVO getSchoolMainInfo(SchoolMainVO vo) {
		return dao.queryObject("schoolMap.getSchoolMainInfo",vo);
	}

	public SchoolMainVO updateSchoolMainInfo(SchoolMainVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("schoolMap.updateSchoolMainInfo",vo);
		return vo;
	}

	public List<DictSchoolVO> getSchoolColumnAndNewsList(Integer school_id) {
		DictSchoolVO vo = new DictSchoolVO(school_id,DictConstants.DCIT_GROUP_NEWS);
		List<DictSchoolVO> list = dao.queryForList("dictSchoolMap.getDictListByGroup",vo);
		for (DictSchoolVO dictSchoolVO:list) {
			vo.setDict_group(dictSchoolVO.getDict_code());
			List<DictSchoolVO> list1 = dao.queryForList("dictSchoolMap.getDictListByGroup",vo);
			for (DictSchoolVO dictSchoolVO1: list1) {
				List<NewsVO> newsVOList = dao.queryForList("newsMap.getSchoolMainNewsListByCode",dictSchoolVO1);
				//if (ListUtil.isEmpty(newsVOList)) continue;
				dictSchoolVO1.setNews_list(BeanUtil.ListTojson(newsVOList,false));
			}
			dictSchoolVO.setChild_node_list(BeanUtil.ListTojson(list1,false));
		}
		return list;
	}

	public List<NewsVO> getSchoolColumnNewsList(Map<String,String> map) {
		return dao.queryForList("newsMap.getSchoolMainNewsListByGroup",map);
	}


	public void updateSchoolSetInfo(SchoolVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("schoolMap.updateSchoolInfo",vo);
  }
  
	//添加学校考勤定位信息
	public void addLocationInfo(SchoolVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("schoolMap.addLocationInfo",vo);
	}
}
