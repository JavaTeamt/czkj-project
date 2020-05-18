package com.czkj.exception;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
public class ValidateException extends BusinessException{
    private static final long serialVersionUID = 1L;

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String code, String message) {
        super(code, message);
    }
}
