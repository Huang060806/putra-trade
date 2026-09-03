package com.putra.trade.server.service.impl;

import com.putra.trade.common.constant.MessageConstant;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.properties.JwtProperties;
import com.putra.trade.common.utils.BCryptUtil;
import com.putra.trade.common.utils.JwtUtil;
import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.entity.Employee;
import com.putra.trade.pojo.vo.EmployeeLoginVO;
import com.putra.trade.server.interceptor.JwtTokenAdminInterceptor;
import com.putra.trade.server.mapper.EmployeeMapper;
import com.putra.trade.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final JwtProperties jwtProperties;

    @Override
    public EmployeeLoginVO login(LoginDTO loginDTO) {
        Employee employee = employeeMapper.getByUsername(loginDTO.getAccount());

        if (employee == null) {
            throw new BusinessException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!BCryptUtil.matches(loginDTO.getPassword(), employee.getPassword())) {
            throw new BusinessException(MessageConstant.PASSWORD_ERROR);
        }
        if (employee.getStatus() == 0) {
            throw new BusinessException(MessageConstant.ACCOUNT_LOCKED);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenAdminInterceptor.CLAIM_EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        log.info("管理员登录成功, id={}, username={}", employee.getId(), employee.getUsername());
        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
    }
}
