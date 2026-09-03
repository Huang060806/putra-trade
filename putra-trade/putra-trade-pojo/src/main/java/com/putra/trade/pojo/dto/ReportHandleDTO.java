package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ReportHandleDTO implements Serializable {

    @NotNull(message = "举报 id 不能为空")
    private Long reportId;

    /**
     * 处理动作:
     * DISMISS 驳回;
     * TAKEDOWN 下架商品;
     * BAN_PUBLISH 禁止发布; BAN_CHAT 禁止私聊; BAN_ACCOUNT 封号;
     * UNBAN 解除全部封禁
     */
    @NotBlank(message = "处理动作不能为空")
    private String action;

    @NotBlank(message = "处理说明不能为空")
    private String handleResult;
}
