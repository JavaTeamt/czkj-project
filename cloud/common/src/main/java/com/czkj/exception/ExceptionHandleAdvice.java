package com.czkj.exception;

import com.czkj.res.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandleAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleAdvice.class);

    public ExceptionHandleAdvice() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败", e);
        return Response.failure("could_not_read_json");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        return Response.failure("request_method_not_supported");
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("不支持当前媒体类型", e);
        return Response.failure("content_type_not_supported");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ValidateException.class})
    public Response handleValidateException(ValidateException e) {
        log.error("验证异常", e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BusinessException.class})
    public Response handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public Response handleException(Exception e) {
        log.error("服务运行异常", e);
        return Response.failure(e.getMessage());
    }
}
