package com.ninesky.classtao.balance.service.impl;

import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.classtao.balance.service.BalanceService;
import com.ninesky.classtao.balance.vo.BalanceLogVO;
import com.ninesky.classtao.balance.vo.BalanceVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.MathUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service(value="BalanceServiceImpl")
public class BalanceServiceImpl implements BalanceService {
	@Autowired
	private GeneralDAO dao;

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	// 定义锁对象
	private Lock lock = new ReentrantLock();
	private Map<Integer,ReadWriteLock> lockMap = new ConcurrentHashMap<Integer,ReadWriteLock>();

	public void addBalance(BalanceVO vo) {
		if (dao.queryObject("balanceMap.getBalanceByID",vo) != null) return;
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setBalance_id(dao.insertObjectReturnID("balanceMap.insertBalance",vo));
		addBalanceLog(vo.getUser_id(),vo.getBalance(),vo.getBalance(),vo.getBalance(),"用户余额"+vo.getBalance()+"元",vo.getIp_address(),vo.getTrade_no(),vo.getOut_trade_no(),vo.getTrade_status());
	}

	public BalanceVO getBalanceByID(BalanceVO vo) { return dao.queryObject("balanceMap.getBalanceByID",vo);}

	public BalanceVO getBalanceByPhone(BalanceVO vo){return dao.queryObject("balanceMap.getBalanceByPhone",vo);}

	public double rechargeBalance(BalanceVO vo) {
		vo.setConsumption_type(DictConstants.TRUE);
		return consumption(vo);
	}

	public void updatePhone(BalanceVO vo) {
		vo.setUpdate_by(ActionUtil.getUserID());
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("balanceMap.updateBalance", vo);
	}

	public double consumptionBalance(BalanceVO vo) {
		vo.setConsumption_type(DictConstants.FALSE);
		return consumption(vo);
	}

	public AgentVO getAgentByAgentID(Integer agent_id){
		return dao.queryObject("agentMap.getAgentListByID",agent_id);
	}

	private double consumption(BalanceVO vo) {
		if (!lockMap.containsKey(vo.getAgent_id())) lockMap.put(vo.getAgent_id(), new ReentrantReadWriteLock());
		lockMap.get(vo.getAgent_id()).writeLock().lock();
		double balance = redisService.getBalanceOfAgent(vo.getAgent_id());
		double recharge_balance;
		String content;
		if (DictConstants.TRUE.equals(vo.getConsumption_type())) {
			recharge_balance = MathUtil.add(balance, vo.getConsumption_money());
			if (DictConstants.USERTYPE_AGENT.equals(vo.getUser_type()))
				content = MsgService.getMsg("BALANCE_AGENT_RECHARGE_CONTENT",redisService.getUserName(0,DictConstants.USERTYPE_AGENT,getAgentByAgentID(vo.getAgent_id()).getUser_id(),0),vo.getConsumption_money());
			else
				content = MsgService.getMsg("BALANCE_ADMIN_RECHARGE_CONTENT",redisService.getUserName(0,DictConstants.USERTYPE_AGENT,getAgentByAgentID(vo.getAgent_id()).getUser_id(),0),vo.getConsumption_money());
		} else {
			recharge_balance = MathUtil.sub(balance, vo.getConsumption_money());
			if (MathUtil.judge(recharge_balance)) throw new BusinessException(MsgService.getMsg("BANLANCE_CONSUMPTION_ERROR"));
			content = MsgService.getMsg("BALANCE_AGENT_CONSUMPTION_CONTENT",redisService.getUserName(0,DictConstants.USERTYPE_AGENT,getAgentByAgentID(vo.getAgent_id()).getUser_id(),0),vo.getConsumption_money());
		}
		try {
			vo.setUpdate_by(ActionUtil.getUserID());
			vo.setUpdate_date(ActionUtil.getSysTime());
			vo.setBalance(recharge_balance);
			redisService.updateBalanceOfAgent(vo.getAgent_id(),recharge_balance);
			dao.updateObject("balanceMap.updateBalance", vo);
		} finally {
			lockMap.get(vo.getAgent_id()).writeLock().unlock();
		}
		addBalanceLog(vo.getAgent_id(), balance, vo.getBalance(), vo.getConsumption_money(),content,vo.getIp_address(),vo.getTrade_no(),vo.getOut_trade_no(),vo.getTrade_status());
		return redisService.getBalanceOfAgent(vo.getAgent_id());
	}

	private void addBalanceLog(Integer agent_id,double original_balance,double current_balance,double consumption,String content,String ip_address,String trade_no,String out_trade_no,String trade_status){
		BalanceLogVO vo = new BalanceLogVO();
		vo.setAgent_id(agent_id);
		vo.setPre_balance(original_balance);
		vo.setCurrent_balance(current_balance);
		vo.setMoney(consumption);
		vo.setContent(content);
		vo.setIp_address(ip_address);
		vo.setTrade_no(trade_no);
		vo.setOut_trade_no(out_trade_no);
		vo.setTrade_status(trade_status);
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setLog_id(dao.insertObjectReturnID("balanceLogMap.insertBalanceLog",vo));
	}
}
