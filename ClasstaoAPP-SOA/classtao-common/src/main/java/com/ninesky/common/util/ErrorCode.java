package com.ninesky.common.util;

/**
 * Created by chenth on 2017-08-15 .
 * Company Classtao.inc
 * E-mail: chentian83@sina.com
 */
public enum ErrorCode {
    SUCCESS(0, "操作成功"),
    NO_LOGIN(10, "未登录"),
    TOKEN_EXPIRED(22, "token已经过期"),
    TOKEN_INVALID(21, "无效的token");
    private Integer error_code;
    private String error_desc;

    ErrorCode(Integer error_code, String error_desc) {
        this.error_code = error_code;
        this.error_desc = error_desc;
    }

    public Integer getError_code(){
        return this.error_code;
    };

    public String getError_desc(){
        return this.error_desc;
    };
}
