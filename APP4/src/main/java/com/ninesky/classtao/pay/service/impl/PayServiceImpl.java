package com.ninesky.classtao.pay.service.impl;

import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.homework.vo.CountVO;
import com.ninesky.classtao.pay.service.PayService;
import com.ninesky.classtao.pay.vo.PayVO;
import com.ninesky.classtao.pay.vo.PayDetailVO;
import com.ninesky.classtao.pay.vo.PayGroupVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.vo.TeamVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.GeneralDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("PayServiceImpl")
public class PayServiceImpl implements PayService {
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	@Autowired
	private GeneralDAO dao;

	@Autowired
	private RedisService redisService;

	@Autowired
	private DynamicService dynamicService;

	@Autowired
	private GetuiService getuiService;

	/**
	 * 添加缴费
	 * @param vo
	 */
	public PayVO addPay(PayVO vo){
		if (DictConstants.USERTYPE_ALL.equals(vo.getPay_group_list())) vo.setUser_type(DictConstants.USERTYPE_ALL);
		else vo.setUser_type(DictConstants.USERTYPE_STUDENT);
		vo.setPay_title("【"+redisService.getSchoolName(ActionUtil.getSchoolID())+"】"+redisService.getDictValue(vo.getPay_category())+"");
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setPay_id(dao.insertObjectReturnID("payMap.insertPay",vo));
		//添加缴费团队
		addPayGroup(vo);
		return vo;
	}
	//添加缴费团队
	private void addPayGroup(PayVO vo){
		if (StringUtil.isEmpty(vo.getPay_group_list())) return;
		List<ReceiveVO> receiveVOList = new ArrayList<ReceiveVO>();
		if (DictConstants.USERTYPE_ALL.equals(vo.getPay_group_list())) {
			receiveVOList.add(new ReceiveVO(ActionUtil.getSchoolID(),DictConstants.TEAM_TYPE_CLASS,0,0));
			PayGroupVO payGroupVO = new PayGroupVO();
			payGroupVO.setPay_id(vo.getPay_id());
			payGroupVO.setUser_id(0);
			payGroupVO.setStudent_id(0);
			payGroupVO.setSchool_id(ActionUtil.getSchoolID());
			payGroupVO.setCreate_by(ActionUtil.getUserID());
			payGroupVO.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("payGroupMap.insertPayGroupDate",payGroupVO);
		} else{
			List<PayGroupVO> list = BeanUtil.jsonToList(vo.getPay_group_list(),PayGroupVO.class);
			for (PayGroupVO payGroupVO:list) {
				payGroupVO.setPay_id(vo.getPay_id());
				payGroupVO.setUser_id(0);
				payGroupVO.setStudent_id(0);
				payGroupVO.setSchool_id(ActionUtil.getSchoolID());
				payGroupVO.setCreate_by(ActionUtil.getUserID());
				payGroupVO.setCreate_date(ActionUtil.getSysTime());
				List<ReceiveVO> receive = dao.queryForList("payGroupMap.getPayReceiveList",payGroupVO);
				receiveVOList.addAll(receive);
			}
			dao.insertObject("payGroupMap.insertPayGroupBath",list);
			receiveVOList = removeDuplicate(receiveVOList);
		}
		HashMap<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("pay_id",vo.getPay_id().toString());
		dataMap.put("info_content",StringUtil.subString(vo.getPay_content(),15));
 		dataMap.put("info_title",vo.getPay_title());
		dataMap.put("module_code",DictConstants.PAY_TYPE_SCHOOL_CHARGE.equals(vo.getPay_type())?DictConstants.MODULE_CODE_SCHOOL_PAY:DictConstants.MODULE_CODE_CONVENIENCE_PAY);
		dataMap.put("module_pkid",vo.getPay_id().toString());
		dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
		dataMap.put("info_url", "detail.html");
		dataMap.put("user_id", ActionUtil.getUserID().toString());
		dataMap.put("student_id","0");
		dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
		dataMap.put("user_type", DictConstants.USERTYPE_STUDENT);
		dynamicService.insertDynamic(dataMap, receiveVOList);
		getuiService.pushMessage(dataMap, receiveVOList);
	}
	private List<PayGroupVO> getAllTeamList(){
		TeamVO teamVO = new TeamVO();
		teamVO.setSchool_id(ActionUtil.getSchoolID());
		teamVO.setIs_graduate(DictConstants.FALSE);
		return dao.queryForList("classMap.getAllClassList",teamVO);
	}
	/**
	 * 获取发布缴费的记录
	 * @param vo
	 * @return
	 */
	public List<PayVO> getPayList(PayVO vo) {
		List<PayVO> list = null;
		if (IntegerUtil.isNotEmpty(vo.getStudent_id())) {
			list = dao.queryForList("payMap.getPayListByStudentID",vo);
		} else {
			list = dao.queryForList("payMap.getPayList",vo);
		}
		for (PayVO payVO:list) {
			if (DictConstants.USERTYPE_PARENT.equals(ActionUtil.getUserType()))
			    payVO.setPay_status(getUserPayStatus(payVO));
			payVO.setPay_team_names(redisService.getPayTeamNames(ActionUtil.getSchoolID(),payVO.getUser_type(),payVO.getPay_id()));
			payVO.setIs_expired(getPayIsExpired(payVO));
			payVO.setPay_type_name(redisService.getDictValue(payVO.getPay_type()));
			payVO.setPay_category_name(redisService.getDictValue(payVO.getPay_category()));
		}
		return list;
	}
	//获取缴费团队列表
	public String getPayGroupList(PayVO vo){
		vo.setIs_graduate(DictConstants.FALSE);
		vo.setTrade_status(DictConstants.TRADE_STATUS);
		List<PayGroupVO> list = null;
		if (DictConstants.USERTYPE_ALL.equals(vo.getUser_type())) list = dao.queryForList("payGroupMap.getAllPayGroupList",vo);
		else list = dao.queryForList("payGroupMap.getPayGroupList",vo);
		for (PayGroupVO payGroupVO:list) {
			vo.setTeam_type(payGroupVO.getTeam_type());
			vo.setTeam_id(payGroupVO.getTeam_id());
			CountVO countVO = new CountVO();
			countVO.setCount(redisService.getTeamStudentCount(payGroupVO.getTeam_type(),payGroupVO.getGroup_id(),payGroupVO.getTeam_id()));
			countVO.setCount_done(IntegerUtil.getValue(dao.queryObject("payDetailMap.getPayDetailCount",vo)));
			payGroupVO.setPay_group_count(BeanUtil.beanToJson(countVO,false)+"");
			payGroupVO.setTeam_name(redisService.getTeamName(payGroupVO.getTeam_type(),payGroupVO.getGroup_id(),payGroupVO.getTeam_id()));
		}
		return BeanUtil.ListTojson(list)+"";
	}
	/**
	 * 获取学生/教师缴费情况
	 * @param vo
	 * @return
	 */
	public List<Map<String,Object>> getUserPayRecordList(PayVO vo) {
		List<Map<String,Object>> list = dao.queryForList("payGroupMap.getALLPayStuTeamList", vo);
		if (ListUtil.isEmpty(list)) return list;
		for (Map<String,Object> map:list) {
			map.put("class_name",redisService.getTeamName(map.get("team_type")+"",IntegerUtil.getValue(map.get("grade_id")),IntegerUtil.getValue(map.get("team_id"))));
			map.put("pay_id",vo.getPay_id());
			map.put("user_name",redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,IntegerUtil.getValue(map.get("student_id"))));
			map.put("pay_status",getUserListPayStatus(vo,map));
		}
		return list;
	}
	/**
	 * 添加缴费明细
	 * @return
	 */
	public void addPayDetail(PayDetailVO vo) {
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setId(dao.insertObjectReturnID("payDetailMap.insertPayDetail",vo));
	}
	public PayVO getPayByID(Integer pay_id){
		return dao.queryObject("payMap.getPayListByID",pay_id);
	}
	//判断用户集合是否缴费
	private Integer getUserListPayStatus(PayVO vo, Map<String,Object> map){
		PayGroupVO payGroupVO = new PayGroupVO();
		payGroupVO.setPay_id(vo.getPay_id());
		payGroupVO.setSchool_id(vo.getSchool_id());
		payGroupVO.setTrade_status(DictConstants.TRADE_STATUS);
		payGroupVO.setStudent_id(IntegerUtil.getValue(map.get("student_id")));
		return dao.queryObject("payDetailMap.getPayDetailList",payGroupVO) == null?0:1;
	}

	//判断相应用户是否缴费
	private Integer getUserPayStatus(PayVO vo){
		PayGroupVO payGroupVO = new PayGroupVO();
		payGroupVO.setPay_id(vo.getPay_id());
		payGroupVO.setSchool_id(vo.getSchool_id());
		payGroupVO.setStudent_id(ActionUtil.getStudentID());
		payGroupVO.setTrade_status(DictConstants.TRADE_STATUS);
		return dao.queryObject("payDetailMap.getPayDetailList",payGroupVO) == null?0:1;
	}

	//判断是否已经过期
	private Integer getPayIsExpired(PayVO vo){
		Date pay_end = DateUtil.formatDateEnd(vo.getEnd_date());
		return pay_end.getTime()>ActionUtil.getSysTime().getTime()?0:1;
	}
	//去除重复的学生
	private List<ReceiveVO> removeDuplicate(List<ReceiveVO> list){
		List<ReceiveVO> receiveVOList = new ArrayList<ReceiveVO>();
		HashMap<Object,ReceiveVO> teacherMap = new HashMap<Object,ReceiveVO>();
		//先将所有接受者存放到HashMap中
		for (ReceiveVO vo:list) {
			if (teacherMap.containsKey(vo.getStudent_id())) continue;//判断键值是否存在，(存在直接返回)
			teacherMap.put(vo.getStudent_id(),null);//先将所有接受者存放到HashMap中
			receiveVOList.add(vo);
		}
		return receiveVOList;
	}
}
