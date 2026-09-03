package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.vo.EmployeeLoginVO;
import com.putra.trade.server.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 管理端 - 管理员认证
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
@RequiredArgsConstructor
@Tag(name = "管理端-管理员接口", description = "管理员登录")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录", description = "用户名 + 密码，返回 JWT Token")
    public Result<EmployeeLoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("管理员登录: {}", loginDTO.getAccount());
        return Result.success(employeeService.login(loginDTO));
    }
}
