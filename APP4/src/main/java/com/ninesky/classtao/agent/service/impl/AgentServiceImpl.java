package com.ninesky.classtao.agent.service.impl;

import java.util.Dictionary;
import java.util.List;

import com.ninesky.classtao.balance.service.BalanceService;
import com.ninesky.classtao.balance.vo.BalanceVO;
import com.ninesky.classtao.system.controller.DictController;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.MathUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ninesky.classtao.agent.service.AgentService;
import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.framework.GeneralDAO;

@Service(value="AgentServiceImpl")
public class AgentServiceImpl implements AgentService{
	@Autowired
	private GeneralDAO dao;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BalanceService balanceService;
	/**
	 * 添加代理商
	 * @param vo
	 */
	public AgentVO addAgent(AgentVO vo) {
		UserVO userVO = userService.getUserByPhone(vo.getPhone());
		if (userVO == null) {
			UserVO uvo = new UserVO();
			uvo.setPhone(vo.getPhone());
			uvo.setUser_name(vo.getAgent_name());
			uvo.setHead_url(Constants.DEFALHEADPATH);
			uvo.setPass_word(Constants.RESET_PASSWORD);
			uvo.setStatus(DictConstants.STATUS_NORMAL);
			vo.setUser_id(userService.insertAdmin(uvo).getUser_id());
		} else vo.setUser_id(userVO.getUser_id());
		vo.setIs_enable(DictConstants.STATUS_NORMAL);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setAgent_id(dao.insertObjectReturnID("agentMap.insertAgent", vo));
		BalanceVO balanceVO = new BalanceVO();
		balanceVO.setUser_id(vo.getUser_id());
		balanceVO.setRecharge_type(vo.getRecharge_type());
		balanceVO.setAgent_id(vo.getAgent_id());
		balanceVO.setBalance(MathUtil.judge(vo.getBalance())?0:vo.getBalance());
		balanceVO.setIp_address(vo.getIp_address());
		balanceService.addBalance(balanceVO);
		return vo;
	}
	
	/**
	 * 获取代理商列表
	 * @param 
	 * @return
	 */
	public List<AgentVO> getAgentList(AgentVO vo){	
		return dao.queryForList("agentMap.getAgentList", vo);
	}
	
	
	/**
	 * 获取指定代理商
	 * @param 
	 * @return
	 */
	public AgentVO getAgentByID(Integer agent_id){
		return dao.queryObject("agentMap.getAgentListByID", agent_id);
	}
	
	/**
	 * 删除指定代理商
	 * @param 
	 */
	public Object deleteAgent(Integer agent_id){
		return dao.deleteObject("agentMap.deleteAgentByID", agent_id);
	}
	
	/**
	 * 更新代理商
	 * @param vo
	 */
	public void updateAgent(AgentVO vo){
		if (StringUtil.isNotEmpty(vo.getPass_word())) updateUserPassword(vo);
		if (StringUtil.isNotEmpty(vo.getPhone())) updateUserPhone(vo);
		if (MathUtil.judge(vo.getBalance())) rechargeBalance(vo);
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("agentMap.updateAgentByID", vo);
	}

	private void updateUserPassword(AgentVO vo){
		UserVO user = new UserVO();
		user.setUser_id(vo.getUser_id());
		user.setPass_word(vo.getPass_word());
		userService.resetPassword(user);
	}

	private void rechargeBalance(AgentVO vo){
		BalanceVO balanceVO = new BalanceVO();
		balanceVO.setUser_id(vo.getUser_id());
		if (MathUtil.judge(vo.getBalance())) {
			balanceVO.setBalance(balanceService.getBalanceByID(balanceVO).getBalance());
			balanceVO.setConsumption_money(vo.getBalance());
			balanceVO.setVersion(vo.getVersion());
		}
		balanceService.rechargeBalance(balanceVO);
	}

	private void updateUserPhone(AgentVO vo){
		UserVO userVO = new UserVO();
		userVO.setPhone(vo.getPhone());
		userVO.setUser_id(vo.getUser_id());
		userService.modifyPhone(userVO);
	}

	@Override
	public AgentVO getAgentByPhone(String phone) {
		return dao.queryObject("agentMap.getAgentByPhone", phone);
	}

	@Override
	public AgentVO getAgentByAgentName(String agent_name) {
		return dao.queryObject("agentMap.getAgentByAgentName", agent_name);
	}

	public List<AgentVO> getAgentBasicList(AgentVO vo){
		return dao.queryForList("agentMap.getAgentBasicList", vo);
	}

	public void disableAgentAccount(AgentVO vo){
		vo.setIs_enable(ActionUtil.getParameter("is_enable"));
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("agentMap.updateAgentByID", vo);
	}
}
