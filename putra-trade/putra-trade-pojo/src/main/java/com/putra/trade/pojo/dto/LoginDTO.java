package com.putra.trade.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求（学生/管理员通用：学号或邮箱 + 密码）
 */
@Data
@Schema(description = "登录请求")
public class LoginDTO implements Serializable {

    @NotBlank(message = "登录账号不能为空")
    @Schema(description = "登录账号（学号或邮箱）", required = true)
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true)
    private String password;
}
