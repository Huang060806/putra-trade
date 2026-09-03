package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OrdersSubmitDTO implements Serializable {

    @NotNull(message = "商品 id 不能为空")
    private Long itemId;

    @NotNull(message = "面交地点不能为空")
    private Long meetSpotId;
}
