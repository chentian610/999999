package com.ninesky.classtao.agent.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.ninesky.common.Constants;
import com.ninesky.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.ninesky.classtao.agent.service.AgentService;
import com.ninesky.classtao.agent.vo.AgentVO;
import com.ninesky.common.util.BeanUtil;


@RestController
@RequestMapping(value = "agentAction")
public class AgentController extends BaseController{
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 添加代理商
	 * @param request
	 */
	@RequestMapping(value="/addAgentVO")
	@ResultField(includes={"agent_id","user_id","phone","agent_name","regist_date","valid_date"})
	public @ResponseBody Object addAgentVO(HttpServletRequest request){
		AgentVO vo = BeanUtil.formatToBean(AgentVO.class);
		if (agentService.getAgentByPhone(vo.getPhone()) != null)
			throw new BusinessException(MsgService.getMsg("REGISTERED_PHONE"));
		if (agentService.getAgentByAgentName(vo.getAgent_name()) != null)
			throw new BusinessException(MsgService.getMsg("AGENT_NAME_EXIST"));
		vo.setIp_address(request.getHeader("X-Real-IP")==null?request.getRemoteAddr():request.getHeader("X-Real-IP"));
		return agentService.addAgent(vo);
	}
	
	/**
	 * 获取指定代理商 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAgentListByID")
	@ResultField(includes={"agent_id","user_id","phone","agent_name","regist_date","valid_date"})
	public @ResponseBody Object getAgentListByID(Integer agent_id,HttpServletRequest request){
 		return agentService.getAgentByID(agent_id);
	}
	
	/**
	 * 获取代理商列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAgentList")
	@ResultField(includes={"agent_id","user_id","phone","agent_name","regist_date","valid_date"})
	public @ResponseBody Object getAgentList(HttpServletRequest request){
		AgentVO vo = BeanUtil.formatToBean(AgentVO.class);
		return agentService.getAgentList(vo);
	}

	/**
	 * 获取代理商列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAgentBasicList")
	public @ResponseBody Object getAgentBasicList(HttpServletRequest request){
		AgentVO vo = BeanUtil.formatToBean(AgentVO.class);
		return agentService.getAgentBasicList(vo);
	}

	/**
	 * 删除指定代理商
	 * @param request
	 */
	@RequestMapping(value="/dateleAgentByID")
	public @ResponseBody Object dateleAgentByID(Integer agent_id,HttpServletRequest request){
		return agentService.deleteAgent(agent_id);
	}
	
	/**
	 * 更新代理商
	 * @param request
	 */
	@RequestMapping(value="/updateAgentByID")
	public @ResponseBody Object updateAgentByID(HttpServletRequest request){
		AgentVO vo = BeanUtil.formatToBean(AgentVO.class);
		agentService.updateAgent(vo);
		return ResponseUtils.sendSuccess();
	}

	/**
	 * 更新代理商
	 * @param request
	 */
	@RequestMapping(value="/disableAgentAccount")
	public @ResponseBody Object disableAgentAccount(AgentVO vo,HttpServletRequest request){
		agentService.disableAgentAccount(vo);
		return ResponseUtils.sendSuccess();
	}
}
