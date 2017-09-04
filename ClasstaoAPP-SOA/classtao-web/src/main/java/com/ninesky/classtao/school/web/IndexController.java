package com.ninesky.classtao.school.web;
import com.github.pagehelper.PageInfo;
import com.ninesky.classtao.school.service.SchoolService;
import com.ninesky.classtao.school.web.Vo.ContentVo;
import com.ninesky.framework.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;

/**
 * 首页
 * Created by Administrator on 2017/3/8 008.
 */
@Controller
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
    public static String THEME = "themes/default";

    @Autowired
    private SchoolService schoolService;


    /**
     * 首页
     *
     * @return
     */
    @GetMapping(value = {"/", "index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(request, 1, limit);
    }

    /**
     * 首页分页
     *
     * @param request request
     * @param p       第几页
     * @param limit   每页大小
     * @return 主页
     */
    @GetMapping(value = "page/{p}")
    public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        p = p < 0 || p > 20 ? 1 : p;
        PageInfo<ContentVo> articles = new PageInfo<ContentVo>();
        articles.setPageNum(1);
        articles.setPageSize(12);
        articles.setSize(4);
        articles.setStartRow(1);
        articles.setEndRow(4);
        articles.setTotal(4);
        articles.setPages(1);
        articles.setIsFirstPage(true);
        articles.setIsLastPage(true);
        articles.setNavigateFirstPage(1);
        articles.setNavigateLastPage(1);
//        articles.setList(schoolService.getContentList());
        request.setAttribute("articles", articles);
        if (p > 1) {
            this.title(request, "第" + p + "页");
        }
        return this.render("index");
    }
    /**
     * 主页的页面主题
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }
    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
    public void title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
    }
}
