package com.putra.trade.common.context;

/**
 * 基于 ThreadLocal 的当前登录用户上下文
 * <p>
 * JWT 拦截器解析 Token 后写入，Service/Mapper 层通过 getCurrentId() 取当前用户，
 * 请求结束后拦截器负责 remove，防止线程复用导致的数据串号。
 */
public class BaseContext {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentId() {
        return THREAD_LOCAL.get();
    }

    public static void removeCurrentId() {
        THREAD_LOCAL.remove();
    }
}
