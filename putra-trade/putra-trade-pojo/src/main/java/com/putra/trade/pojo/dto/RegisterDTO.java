package com.putra.trade.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 学生注册请求
 */
@Data
@Schema(description = "学生注册请求")
public class RegisterDTO implements Serializable {

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[A-Za-z]{1,4}\\d{5,7}$", message = "学号格式不正确（如 BC123456）")
    @Schema(description = "学号（如 BC123456）", required = true)
    private String studentNo;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "UPM 邮箱", required = true)
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需为 6-20 位")
    @Schema(description = "密码", required = true)
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", required = true)
    private String confirmPassword;

    @Schema(description = "昵称（默认学号）")
    private String nickname;
}
