package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ReportSubmitDTO implements Serializable {

    @NotNull(message = "举报对象类型不能为空")
    private Integer targetType; // 1商品 2用户

    @NotNull(message = "举报对象 id 不能为空")
    private Long targetId;

    @NotBlank(message = "举报原因不能为空")
    private String reason;

    /** 凭证图片 URL 列表（选填） */
    private List<String> images;
}
