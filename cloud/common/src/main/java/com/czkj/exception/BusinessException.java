package com.czkj.exception;

/**
 * @author zhangqh
 * @date 2020-04-01
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code = "1000";

    public BusinessException() {
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return this.code;
    }
}
