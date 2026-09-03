package com.putra.trade.server.interceptor;

import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.constant.MessageConstant;
import com.putra.trade.common.properties.JwtProperties;
import com.putra.trade.common.utils.JwtUtil;
import com.putra.trade.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 用户端 JWT 校验拦截器
 * <p>
 * 校验通过：把 member id 存入 BaseContext（ThreadLocal）放行；
 * 校验失败：直接写回 401 + Result JSON，不进 Controller。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenMemberInterceptor implements HandlerInterceptor {

    public static final String CLAIM_MEMBER_ID = "memberId";

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非 Controller 请求（静态资源等）直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getMemberTokenName());
        try {
            log.info("用户端 JWT 校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getMemberSecretKey(), token);
            Long memberId = Long.valueOf(claims.get(CLAIM_MEMBER_ID).toString());
            BaseContext.setCurrentId(memberId);
            return true;
        } catch (Exception ex) {
            log.warn("用户端 Token 校验失败: {}", ex.getMessage());
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error(MessageConstant.TOKEN_INVALID)));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContext.removeCurrentId();
    }
}
