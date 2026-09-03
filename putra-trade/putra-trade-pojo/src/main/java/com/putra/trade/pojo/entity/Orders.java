package com.putra.trade.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    private Long id;
    private String orderNo;
    private Long buyerId;
    private Long sellerId;
    private Long itemId;
    private String itemTitle;       // 快照
    private BigDecimal price;       // 快照
    private String meetSpotInfo;    // 快照
    private Integer status;         // 0待支付 1待面交 2已完成 3已取消
    private Integer payMethod;
    private String cancelReason;
    private LocalDateTime orderTime;
    private LocalDateTime payTime;
    private LocalDateTime completeTime;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
