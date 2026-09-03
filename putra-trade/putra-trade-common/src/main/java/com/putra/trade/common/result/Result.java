package com.putra.trade.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装，前端按 code 判断成功/失败
 *
 * @param <T> 响应数据泛型
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; // 1成功 0失败
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
