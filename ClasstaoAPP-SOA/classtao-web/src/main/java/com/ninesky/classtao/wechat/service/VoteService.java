package com.ninesky.classtao.wechat.service;

import java.util.List;

import com.ninesky.classtao.wechat.vo.VoteOptionVO;
import com.ninesky.classtao.wechat.vo.VoteQuestionVO;
import com.ninesky.classtao.wechat.vo.VoteVO;

public interface VoteService {
	
	//投票主题
	public void addVote(VoteVO vo);
	
	public void updateVote(VoteVO vo);
	
	public List<VoteVO> getVoteByAccount(Integer accountId);
	
	public List<VoteVO> getPublishVoteByAccount(Integer accountId);
	
	public VoteVO getVoteById(Integer voteId);
	
	public void deleteVoteById(Integer voteId);
	
	//投票问题
	public void addQuestion(VoteQuestionVO vo);
	
	public void updateQuestion(VoteQuestionVO vo);
	
	public List<VoteQuestionVO> getQuestionByVote(Integer voteId);
	
	public VoteQuestionVO getQuestionById(Integer questionId);
	
	public void deleteQuestionById(Integer questionId);
	
	//投票问题选项
	public void addOption(VoteOptionVO vo);
	
	public void updateOption(VoteOptionVO vo);
	
	public List<VoteOptionVO> getOptionByQuestion(Integer questionId);
	
	public VoteOptionVO getOptionById(Integer optionId);
	
	public void deleteOptionById(Integer optionId);
}
