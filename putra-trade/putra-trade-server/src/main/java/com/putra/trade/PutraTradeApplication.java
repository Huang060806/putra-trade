package com.putra.trade;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Putra Trade 后端启动类
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@MapperScan("com.putra.trade.server.mapper")
public class PutraTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PutraTradeApplication.class, args);
        log.info("Putra Trade 后端服务启动成功，接口文档: http://localhost:8080/doc.html");
    }
}
