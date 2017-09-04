package com.ninesky.classtao.agent.service;

import java.util.List;

import com.ninesky.classtao.agent.vo.AgentVO;

public interface AgentService {
	/**
	 * 添加代理商
	 * @param vo
	 */
	public AgentVO addAgent(AgentVO vo);
	
	/**
	 * 获取代理商列表
	 * @param 
	 * @return
	 */
	public List<AgentVO> getAgentList(AgentVO vo);

	/**
	 * 获取代理商列表
	 * @param
	 * @return
	 */
	public List<AgentVO> getAgentBasicList(AgentVO vo);
	
	/**
	 * 获取指定代理商
	 * @param 
	 * @return
	 */
	public AgentVO getAgentByID(Integer Agent_id);
	
	/**
	 * 获取指定代理商
	 * @param 
	 * @return
	 */
	public AgentVO getAgentByPhone(String phone);
	
	/**
	 * 获取指定代理商
	 * @param 
	 * @return
	 */
	public AgentVO getAgentByAgentName(String agent_name);
	/**
	 * 删除指定代理商
	 * @param 
	 * @return 
	 */
	public Object deleteAgent(Integer Agent_id);
	
	/**
	 * 更新代理商
	 * @param vo
	 * @return 
	 */
	public void updateAgent(AgentVO vo);

	/**
	 * 使代理商账户不能使用
	 * @param vo
	 */
	public void disableAgentAccount(AgentVO vo);
	
}
