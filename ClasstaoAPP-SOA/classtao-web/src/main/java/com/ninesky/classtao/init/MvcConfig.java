package com.ninesky.classtao.init;

import com.ninesky.framework.DataInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by xin on 15/12/27.
 */
@Configuration
@ComponentScan(basePackages = "com.ninesky")
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private DataInterceptor dataInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/**.html").addResourceLocations("/");
        super.addResourceHandlers(registry);
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
////        registry.addViewController("/").setViewName("index");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer() {
//
//        return (container -> {
//            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error_401.html");
//            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error_404.html");
//            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error_500.html");
//
//            container.addErrorPages(error401Page, error404Page, error500Page);
//        });
//    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver bean = new CommonsMultipartResolver();
        bean.setDefaultEncoding("UTF-8");
        bean.setMaxUploadSize(8388608);
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册数据初始化拦截器
        InterceptorRegistration ir = registry.addInterceptor(dataInterceptor);
        // 配置拦截的路径
        ir.addPathPatterns("/*Action/**");
        ir.addPathPatterns("/index");
        // 配置不拦截的路径
//        ir.excludePathPatterns("/static/**");
//        ir.excludePathPatterns("/**.js");
    }

}
