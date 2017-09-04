package com.ninesky.classtao.balance.service;

import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.classtao.balance.vo.BalanceVO;

public interface BalanceService {
	public void addBalance(BalanceVO vo);

	public BalanceVO getBalanceByID(BalanceVO vo);

	public BalanceVO getBalanceByPhone(BalanceVO vo);

	public double rechargeBalance(BalanceVO vo);

	public void updatePhone(BalanceVO vo);

	public double consumptionBalance(BalanceVO vo);

	public AgentVO getAgentByAgentID(Integer agent_id);
}
