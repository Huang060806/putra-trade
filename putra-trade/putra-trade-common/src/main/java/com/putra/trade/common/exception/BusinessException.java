package com.putra.trade.common.exception;

/**
 * 业务异常，Service 层校验失败时抛出，由全局异常处理器统一转为 Result
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
