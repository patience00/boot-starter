package com.linchtech.boot.starter.config;

import com.linchtech.boot.starter.common.AccessUser;
import com.linchtech.boot.starter.common.SystemErrorCode;
import com.linchtech.boot.starter.common.entity.vo.ResultVO;
import com.linchtech.boot.starter.common.exceptions.BusinessException;
import com.linchtech.boot.starter.common.exceptions.ParameterException;
import com.linchtech.boot.starter.utils.DingTalkMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.linchtech.boot.starter.config.WebInterceptor.USER_INFO;

/**
 * @author 107
 * @date 2018-05-21 15:13
 **/
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Autowired
    private DingTalkMessage dingTalkMessage;

    /**
     * 参数异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResultVO methodArgumentNotValidException(HttpMessageNotReadableException exception) {
        log.error(exception.toString());
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, exception.getMessage());
    }

    /**
     * 参数异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultVO methodArgumentNotValidException(MethodArgumentTypeMismatchException exception) {
        log.error(exception.toString());
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, SystemErrorCode.VALIDATE_ERROR.getMsg() + ":" + exception.getName());
    }

    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        ObjectError objectError = allErrors.get(0);
        FieldError fieldError = (FieldError) objectError;
        log.error("参数校验异常 {}:{} ", fieldError.getField(), fieldError.getRejectedValue());
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO requestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, SystemErrorCode.VALIDATE_ERROR.getMsg() + ":" + e.getParameterName());
    }

    /**
     * 不支持改请求方法
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(SystemErrorCode.METHOD_NO_SUPPORT, e.getMessage());
    }

    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVO bindException(BindException e) {
        log.error(e.getMessage(), e);
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, e.getBindingResult().getFieldError().getField() + ":" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 数据库异常拦截.数据重复违反唯一约束
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResultVO methodArgumentNotValidException(DataIntegrityViolationException dataIntegrityViolationException) {
        log.error("dataIntegrityViolationException:", dataIntegrityViolationException);
        return ResultVO.fail(SystemErrorCode.VALIDATE_ERROR, dataIntegrityViolationException.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AccessDeniedException.class)
    public ResultVO accessDeniedException(AccessDeniedException accessDeniedException) {
        log.error("accessDeniedException:", accessDeniedException);
        return ResultVO.fail(accessDeniedException.getMessage());
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
        return ResultVO.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * 业务异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResultVO exception(BusinessException businessException) {
        log.error("businessException:", businessException);
        return ResultVO.fail(businessException.getCode(), businessException.getMessage());
    }

    /**
     * 错误异常拦截.
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultVO exception(Exception exception, WebRequest request, HttpServletRequest servletRequest) {
        // 获取请求的URI
        String uri = null;
        try {
            uri = request.getDescription(false);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.error("exception:", exception);
        AccessUser accessUser = USER_INFO.get();
        String userId = accessUser == null ? "null" : accessUser.getUserId() == null ? "null" : accessUser.getUserId().toString();
        //        String uri = accessUser == null ? "null" : accessUser.getRequestUri() == null ? "null" :
        //                accessUser.getRequestUri();
        dingTalkMessage.sendErrorMsg(exception, userId, uri);
        return ResultVO.fail(SystemErrorCode.UNKNOWN_ERROR, SystemErrorCode.UNKNOWN_ERROR.getMsg());
    }
}
