package com.ninesky.classtao.wechat.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninesky.classtao.wechat.service.VoteService;
import com.ninesky.classtao.wechat.vo.VoteOptionVO;
import com.ninesky.classtao.wechat.vo.VoteQuestionVO;
import com.ninesky.classtao.wechat.vo.VoteVO;
import com.ninesky.classtao.wechat.vo.WxAccountVO;
import com.ninesky.common.Constants;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.SystemConfig;

@RestController
@RequestMapping(value="wxVoteAction")
public class WxVoteController extends WxBaseController{
	
	@Autowired
	private VoteService voteService;
	
	/**
	 * 获得公众号下的投票主题
	 * @return
	 */
	@RequestMapping(value="/getVotes")
	public @ResponseBody Object getVotes(){
		WxAccountVO account = getSessionWxAccount();
		if(account == null){
			return ResponseUtils.sendFailure("请在创建微信公众账号后，再管理校园投票");
		}
		return ResponseUtils.sendSuccess(voteService.getVoteByAccount(account.getAccount_id()));
	}
	
	/**
	 * 获得某个票主题
	 * @return
	 */
	@RequestMapping(value="/getVote")
	public @ResponseBody Object getVote(HttpServletRequest request){
		
		Integer voteId = Integer.parseInt(request.getParameter("voteId"));
		if(voteId == null || voteId <= 0){
			return ResponseUtils.sendFailure("缺少参数voteId");
		}
		
		return ResponseUtils.sendSuccess(voteService.getVoteById(voteId));
	}
	
	/**
	 * 保存投票主题
	 * @return
	 */
	@RequestMapping(value="/saveVote")
	public @ResponseBody Object saveVote(HttpServletRequest request, VoteVO vo){

		if(vo.getVote_id() != null && vo.getVote_id() > 0){
			voteService.updateVote(vo);
		}else{
			WxAccountVO account = getSessionWxAccount();
			if(account == null){
				return ResponseUtils.sendFailure("请在创建微信公众账号后，再添加投票主题");
			}
			if(StringUtils.isEmpty(account.getPlatform_account_id())){
				return ResponseUtils.sendFailure("微信公众账号未能创建成功，请联系管理员进行核实");
			}
			vo.setAccount_id(account.getAccount_id());
			vo.setCreate_date(new Date());
			vo.setStatement(Constants.WECHAT_VOTE_STATEMENT_UNPUBLISH);
			voteService.addVote(vo);
		}
		
		return ResponseUtils.sendSuccess(vo);
	}
	
	/**
	 * 删除公众号下的投票主题
	 * @return
	 */
	@RequestMapping(value="/deleteVote")
	public @ResponseBody Object deleteVote(HttpServletRequest request){
		Integer voteId = Integer.parseInt(request.getParameter("voteId"));
		if(voteId == null || voteId <= 0){
			return ResponseUtils.sendFailure("缺少参数voteId");
		}
		voteService.deleteVoteById(voteId);
		return ResponseUtils.sendSuccess();
	}
	
	/**
	 * 获得投票主题下的所有问题
	 * @return
	 */
	@RequestMapping(value="/getQuestionsByVote")
	public @ResponseBody Object getQuestionsByVote(HttpServletRequest request){
		Integer voteId = Integer.parseInt(request.getParameter("voteId"));
		if(voteId == null || voteId <= 0){
			return ResponseUtils.sendFailure("缺少参数voteId");
		}
		return ResponseUtils.sendSuccess(voteService.getQuestionByVote(voteId));
	}
	
	/**
	 * 根据问题id返回问题，包含问题下的选项
	 * @return
	 */
	@RequestMapping(value="/getQuestionsById")
	public @ResponseBody Object getQuestionsById(HttpServletRequest request){
		Integer questionId = Integer.parseInt(request.getParameter("questionId"));
		if(questionId == null || questionId <= 0){
			return ResponseUtils.sendFailure("缺少参数questionId");
		}
		
		VoteQuestionVO question = voteService.getQuestionById(questionId);
		List<VoteOptionVO> options = voteService.getOptionByQuestion(questionId);
		question.setOptions(options);
		
		return ResponseUtils.sendSuccess(question);
	}
	
	/**
	 * 根据questionId删除问题
	 * @return
	 */
	@RequestMapping(value="/deleteQuestion")
	public @ResponseBody Object deleteQuestion(HttpServletRequest request){
		Integer questionId = Integer.parseInt(request.getParameter("questionId"));
		if(questionId == null || questionId <= 0){
			return ResponseUtils.sendFailure("缺少参数questionId");
		}
		
		voteService.deleteQuestionById(questionId);
		
		return ResponseUtils.sendSuccess("删除成功");
	}
	
	/**
	 * 保存问题和选项
	 * @return
	 */
	@RequestMapping(value="/saveQuestion")
	public @ResponseBody Object saveQuestion(VoteQuestionVO vo){

		List<VoteOptionVO> bOptions = new ArrayList<VoteOptionVO>();
		
		if(vo.getQuestion_id() != null && vo.getQuestion_id() > 0){
			VoteQuestionVO q = voteService.getQuestionById(vo.getQuestion_id());
			q.setTitle(vo.getTitle());
			q.setDescription(vo.getDescription());
			q.setTitle(vo.getType());
			q.setSeq(vo.getSeq());
			voteService.updateQuestion(vo);
			bOptions = voteService.getOptionByQuestion(q.getQuestion_id());
		}else{
			vo.setCreate_date(new Date());
			voteService.addQuestion(vo);
		}
		List<VoteOptionVO> options = vo.getOptions();
		Map<Integer, VoteOptionVO> optMap = new HashMap<>();
		Integer questionId = vo.getQuestion_id();
		if(options != null && options.size() > 0){
			for(VoteOptionVO opt : options){
				optMap.put(opt.getOption_id(), opt);
				opt.setQuestion_id(questionId);
				if(opt.getOption_id() != null && opt.getOption_id() > 0){
					voteService.updateOption(opt);
				}else{
					opt.setCreate_date(new Date());
					voteService.addOption(opt);
				}
			}
		}
		
		if(bOptions.size() > 0){
			for(VoteOptionVO opt : bOptions){
				if(optMap.get(opt.getOption_id()) == null){
					voteService.deleteOptionById(opt.getOption_id());
				}
			}
		}
		
		return ResponseUtils.sendSuccess(vo);
	}
	
	 /** 发布投票活动
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/publishVote")
	public @ResponseBody Object publish(HttpServletRequest request){
		
		Integer voteId = Integer.parseInt(request.getParameter("voteId"));
		final VoteVO vote = voteService.getVoteById(voteId);
		if(vote == null){
			return ResponseUtils.sendFailure("未找到指定id的投票主题");
		}
		List<VoteQuestionVO> questions = voteService.getQuestionByVote(voteId);
		if(questions != null && questions.size() > 0){
			for(VoteQuestionVO q : questions){
				List<VoteOptionVO> options = voteService.getOptionByQuestion(q.getQuestion_id());
				q.setOptions(options);
			}
		}
		vote.setQuestions(questions);
		vote.setStatement(Constants.WECHAT_VOTE_STATEMENT_PUBLISHING);
		voteService.updateVote(vote);
		final Integer vid = vote.getVote_id();
		new Thread(){
			public void run() {
				wxApiService.syncVote(vid);
			};
		}.start();
		
		return ResponseUtils.sendSuccess(null);
	}
	
	 /** 获取投票结果预览地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getResultUrl")
	public @ResponseBody Object getResultUrl(){
		return ResponseUtils.sendSuccess(SystemConfig.getProperty(Constants.WECHAT_PLATFORM_CONFIG_KEY) + Constants.WECHAT_VOTE_RESULT_URL);
	}
}
