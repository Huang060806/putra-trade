package com.putra.trade.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密码工具：自带盐值，同一密码每次哈希不同，抗彩虹表
 * （课程用 MD5，本项目升级为 BCrypt）
 */
public class BCryptUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 明文 → BCrypt 哈希（注册/改密时入库）
     */
    public static String hash(String plainPassword) {
        return ENCODER.encode(plainPassword);
    }

    /**
     * 校验明文与哈希是否匹配（登录）
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return ENCODER.matches(plainPassword, hashedPassword);
    }
}
