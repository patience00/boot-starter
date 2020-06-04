package com.linchtech.boot.starter.config;

import com.linchtech.boot.starter.common.HttpResult;
import com.linchtech.boot.starter.common.ResultVO;
import com.linchtech.boot.starter.common.exceptions.BusinessException;
import com.linchtech.boot.starter.common.exceptions.ParameterException;
import com.linchtech.boot.starter.properties.SystemProperties;
import com.linchtech.boot.starter.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;

/**
 * @author 107
 * @date 2018-05-21 15:13
 **/
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private SystemProperties systemProperties;

    /**
     * 参数异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResultVO methodArgumentNotValidException(Exception exception) {
        log.error(exception.toString());
        return ResultVO.fail(HttpResult.PARAMETER_ERROR, exception.getMessage());
    }

    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(HttpResult.PARAMETER_ERROR,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO requestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(HttpResult.PARAMETER_ERROR, e.getParameterName());
    }

    /**
     * 不支持改请求方法
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(HttpResult.METHOD_ERROR);
    }

    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO bindException(BindException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(HttpResult.PARAMETER_ERROR,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResultVO methodArgumentNotValidException(DataIntegrityViolationException exception) {
        log.error(exception.toString());
        return ResultVO.fail(HttpResult.PARAMETER_ERROR);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AccessDeniedException.class)
    public ResultVO accessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResultVO.fail(e.getMessage());
    }

    /**
     * 参数异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ParameterException.class)
    public ResultVO exception(ParameterException exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        log.error(sw.toString());
        return ResultVO.fail(HttpResult.PARAMETER_ERROR, exception.getMessage());
    }

    /**
     * 业务异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResultVO exception(BusinessException exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        log.error(sw.toString());
        return ResultVO.fail(HttpResult.BUSINESS_ERROR, exception.getMessage());
    }

    /**
     * 错误异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultVO exception(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        log.error(sw.toString());
        emailUtil.sendBySpringMail(systemProperties.getEmailAddr(), "未知错误",
                exception.getMessage() + "\n" + sw.toString(), null);
        return ResultVO.fail(HttpResult.SYSTEM_ERROR);
    }
}
