package com.putra.trade.server.handler;

import com.putra.trade.common.constant.MessageConstant;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：把异常统一转成 Result，避免 500 错误页和堆栈外泄
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常（Service 层主动抛出）
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 参数校验异常（@Valid 触发，取第一条错误信息）
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse(MessageConstant.UNKNOWN_ERROR);
        log.error("参数校验失败: {}", msg);
        return Result.error(msg);
    }

    /**
     * 兜底：未预期的系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
