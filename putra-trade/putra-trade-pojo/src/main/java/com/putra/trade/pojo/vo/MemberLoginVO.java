package com.putra.trade.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生登录响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学生登录响应")
public class MemberLoginVO implements Serializable {

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "学号")
    private String studentNo;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "JWT Token")
    private String token;
}
