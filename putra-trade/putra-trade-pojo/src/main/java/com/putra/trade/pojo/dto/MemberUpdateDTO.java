package com.putra.trade.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 个人资料修改请求（只允许非敏感字段，密码走独立接口）
 */
@Data
@Schema(description = "个人资料修改请求")
public class MemberUpdateDTO implements Serializable {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "微信号")
    private String wechat;

    @Schema(description = "WhatsApp 号")
    private String whatsapp;

    @Schema(description = "宿舍区域")
    private String dormArea;
}
