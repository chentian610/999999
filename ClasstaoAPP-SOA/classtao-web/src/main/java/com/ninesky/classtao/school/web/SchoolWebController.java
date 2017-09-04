package com.ninesky.classtao.school.web;

//import com.alibaba.dubbo.config.annotation.Reference;
import com.ninesky.classtao.capital.service.GetuiService;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.common.DictConstants;
import com.ninesky.framework.MsgService;
import com.ninesky.framework.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
class SchoolWebController {

    @Autowired
    private GetuiService getuiService;

    @Autowired
    private SchoolService schoolService;


    /**
     * 获取学校申请列表
     * @param request
     * @author chenth123
     * @return
     */
    @GetMapping(value = "s/{domain}")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public Object redirectSchoolByDomain(@PathVariable String domain, HttpServletRequest request, Model model) {
        if ("agent".equals(domain)) {
            request.setAttribute("logo", "agent/images/img_glod.pn");
            request.setAttribute("school_name", MsgService.getMsg("HELLO_AGENT"));
            request.setAttribute("school_id", 0);
            request.setAttribute("user_type", DictConstants.USERTYPE_AGENT);
        } else if ("manager".equals(domain)) {
            request.setAttribute("logo", "audit/home/images/img_glod.png");
            request.setAttribute("school_name", MsgService.getMsg("HELLO_MANAGER"));
            request.setAttribute("school_id", 0);
            request.setAttribute("user_type", DictConstants.USERTYPE_SUPER);
        } else {
            SchoolVO vo = schoolService.getSchoolByDomain(domain);
            if (vo == null) return "error_404";
            request.setAttribute("logo", vo.getOrganize_pic_url());
            request.setAttribute("school_name", vo.getSchool_name());
            request.setAttribute("school_id", vo.getSchool_id());
            request.setAttribute("user_type", DictConstants.USERTYPE_ADMIN);
            request.setAttribute("install_url",vo.getInstall_url());
        }
        request.setAttribute("web_domain_record", SystemConfig.getProperty("WEB_DOMAIN_RECORD"));
        return "login";
    }


}
