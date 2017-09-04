package com.ninesky.framework;

import com.ninesky.common.util.SpringBeanUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by chenth on 2017-01-18 .
 * Company Classtao.inc
 * E-mail: chentian83@sina.com
 */
public class ErrorLogRunnable implements Runnable {
    private Exception exception;
    private String request_url;
    private String param;
    private String ip_address;

    public ErrorLogRunnable(Exception exception, String request_url, String param, String ip_address){
        this.exception = exception;
        this.request_url = request_url;
        this.param = param;
        this.ip_address = ip_address;
    }


    @Override
    public void run() {
        //将错误日志存入数据库
        LogVO log = new LogVO();
        log.setMethod(request_url);
        log.setParameter(param);
        log.setIp_address(ip_address);
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw, true));
        log.setMsg(sw.toString());
        SpringBeanUtil.getBean(GeneralDAO.class).insertObject("logMap.insertErrorLog",log);//
    }
}
