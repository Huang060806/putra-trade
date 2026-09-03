package com.putra.trade.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性，对应 application.yml 的 putra.jwt.*
 */
@Component
@ConfigurationProperties(prefix = "putra.jwt")
@Data
public class JwtProperties {

    /**
     * 用户端 Token 密钥与有效期
     */
    private String memberSecretKey;
    private long memberTtl;
    private String memberTokenName;

    /**
     * 管理端 Token 密钥与有效期
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;
}
