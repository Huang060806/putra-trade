package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserBanDTO implements Serializable {

    @NotNull(message = "用户 id 不能为空")
    private Long memberId;

    private Integer banPublish; // 1禁止 0恢复
    private Integer banChat;
    private Integer status;     // 1正常 0封号
}
