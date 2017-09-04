package com.ninesky.framework;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    /**
     * 错误编码
     */
    private Integer error_code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer error_code, String message) {
        super(message);
        this.error_code = error_code;
    }

    public Integer getError_code() {
        return error_code;
    }
}
