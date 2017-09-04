package com.ninesky.framework;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by user on 2017/1/9.
 */
public class LogVO extends BaseVO {

    private Integer id;
    private String method;//接口名称
    private String parameter;//参数
    private String msg;//错误信息
    private String ip_address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
}
