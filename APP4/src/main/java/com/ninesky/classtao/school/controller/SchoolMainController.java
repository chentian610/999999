package com.ninesky.classtao.school.controller;

import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolMainVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.ResponseUtils;
import com.ninesky.framework.ResultField;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by TOOTU on 2017/6/30.
 */
@RestController
@RequestMapping(value="schoolMainAction")
public class SchoolMainController {
    @Autowired
    private SchoolService schoolService;

    /**
     * 根据学校官网获取学校信息
     * @param request
     * @return
     */
    @RequestMapping(value="/getSchoolInfo")
    @ResultField(excludes= {"direction","app_sql","order_sql","create_by","create_date","version","update_by","update_date","start_time","end_time","start","limit"})
    public @ResponseBody Object getSchoolInfo(HttpServletRequest request){
        SchoolMainVO vo = BeanUtil.formatToBean(SchoolMainVO.class);
        if (IntegerUtil.isEmpty(vo.getSchool_id()) && StringUtil.isEmpty(vo.getMain_url()))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        return schoolService.getSchoolMainInfo(vo);
    }

    /**
     * 根据学校id完善学校信息
     * @param request
     * @return
     */
    @RequestMapping(value="/updateSchoolInfo")
    public @ResponseBody Object updateSchoolInfo(HttpServletRequest request){
        SchoolMainVO vo = BeanUtil.formatToBean(SchoolMainVO.class);
        if (IntegerUtil.isEmpty(vo.getSchool_id()))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        schoolService.updateSchoolMainInfo(vo);
        return ResponseUtils.sendSuccess(vo);
    }

    /**
     * 根据学校官网获取学校信息
     * @param request
     * @return
     */
    @RequestMapping(value="/getSchoolColumnAndNewsList")
    @ResultField(includes = {"child_node_list","dict_code","dict_group","school_id","dict_value"})
    public @ResponseBody Object getSchoolColumnAndNewsList(Integer school_id,HttpServletRequest request){
        if (IntegerUtil.isEmpty(school_id))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        return schoolService.getSchoolColumnAndNewsList(school_id);
    }

    /**
     * 根据学校官网获取学校信息
     * @param request
     * @return
     */
    @RequestMapping(value="/getSchoolColumnNewsList")
    @ResultField(includes = {"news_id","school_id","news_code","title","content_text","main_pic_url","dept_name","deploy_date"})
    public @ResponseBody Object getSchoolColumnNewsList(HttpServletRequest request){
        Map<String,String> map = ActionUtil.getParameterMap();
        return schoolService.getSchoolColumnNewsList(map);
    }
}
