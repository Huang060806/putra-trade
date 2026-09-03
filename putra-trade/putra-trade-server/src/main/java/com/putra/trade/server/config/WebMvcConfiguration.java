package com.putra.trade.server.config;

import com.putra.trade.server.interceptor.JwtTokenAdminInterceptor;
import com.putra.trade.server.interceptor.JwtTokenMemberInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 配置：注册 JWT 拦截器
 * <p>
 * /admin/** 走管理员密钥校验，/user/** 走学生密钥校验；
 * 登录/注册接口与接口文档资源放行。
 * （实现 WebMvcConfigurer 而非继承 WebMvcConfigurationSupport，
 *  保留 Spring Boot 默认静态资源映射，否则 knife4j 的 doc.html 会 404）
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenMemberInterceptor jwtTokenMemberInterceptor;
    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Value("${putra.upload.path}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册 JWT 拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenMemberInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/auth/login")
                .excludePathPatterns("/user/auth/register")
                .excludePathPatterns("/user/category/list")
                .excludePathPatterns("/user/item/page")
                .excludePathPatterns("/user/item/*");
    }

    /** 上传文件的静态访问映射 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
