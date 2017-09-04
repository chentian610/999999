package com.ninesky.framework;

@SuppressWarnings("serial")
public class AuthException extends RuntimeException {
    /**
     * 错误编码
     */
    private Integer error_code;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(Integer error_code, String message) {
        super(message);
        this.error_code = error_code;
    }

    public Integer getError_code() {
        return error_code;
    }
}
