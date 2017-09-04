package com.ninesky.classtao.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.classtao.wechat.service.VoteService;
import com.ninesky.classtao.wechat.vo.VoteOptionVO;
import com.ninesky.classtao.wechat.vo.VoteQuestionVO;
import com.ninesky.classtao.wechat.vo.VoteVO;
import com.ninesky.framework.GeneralDAO;

@Service("voteService")
public class VoteServiceImpl implements VoteService{

	@Autowired
	private GeneralDAO dao;
	
	@Override
	public void addVote(VoteVO vo) {
		vo.setVote_id(dao.insertObjectReturnID("wxVoteMap.insertVote", vo));
	}

	@Override
	public void updateVote(VoteVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxVoteMap.updateVoteById", vo);
	}

	@Override
	public List<VoteVO> getVoteByAccount(Integer accountId) {
		return dao.queryForList("wxVoteMap.getVoteByAccount", accountId);
	}
	
	@Override
	public List<VoteVO> getPublishVoteByAccount(Integer accountId) {
		return dao.queryForList("wxVoteMap.getPublishVoteByAccount", accountId);
	}

	@Override
	public VoteVO getVoteById(Integer voteId) {
		return dao.queryObject("wxVoteMap.getVoteById", voteId);
	}

	@Override
	public void deleteVoteById(Integer voteId) {
		dao.deleteObject("wxVoteMap.deleteById", voteId);
	}

	@Override
	public void addQuestion(VoteQuestionVO vo) {
		vo.setQuestion_id(dao.insertObjectReturnID("wxVoteQuestionMap.insertVoteQuestion", vo));
	}

	@Override
	public void updateQuestion(VoteQuestionVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxVoteQuestionMap.updateVoteQuestionById", vo);
	}

	@Override
	public List<VoteQuestionVO> getQuestionByVote(Integer voteId) {
		return dao.queryForList("wxVoteQuestionMap.getVoteQuestionByVote", voteId);
	}

	@Override
	public VoteQuestionVO getQuestionById(Integer questionId) {
		return dao.queryObject("wxVoteQuestionMap.getVoteQuestionById", questionId);
	}

	@Override
	public void deleteQuestionById(Integer questionId) {
		dao.deleteObject("wxVoteQuestionMap.deleteById", questionId);
	}

	@Override
	public void addOption(VoteOptionVO vo) {
		vo.setOption_id(dao.insertObjectReturnID("wxVoteOptionMap.insertVoteOption", vo));
	}

	@Override
	public void updateOption(VoteOptionVO vo) {
		vo.setUpdate_date(new Date());
		dao.updateObject("wxVoteOptionMap.updateVoteOptionById", vo);
	}

	@Override
	public List<VoteOptionVO> getOptionByQuestion(Integer questionId) {
		return dao.queryForList("wxVoteOptionMap.getVoteOptionByQuestion", questionId);
	}

	@Override
	public VoteOptionVO getOptionById(Integer optionId) {
		return dao.queryObject("wxVoteOptionMap.getVoteOptionById", optionId);
	}

	@Override
	public void deleteOptionById(Integer optionId) {
		dao.deleteObject("wxVoteOptionMap.deleteById", optionId);
	}
}
