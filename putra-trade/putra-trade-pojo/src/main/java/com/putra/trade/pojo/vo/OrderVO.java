package com.putra.trade.pojo.vo;

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
public class OrderVO implements Serializable {
    private Long id;
    private String orderNo;
    private Long buyerId;
    private String buyerNickname;
    private Long sellerId;
    private String sellerNickname;
    private Long itemId;
    private String itemTitle;
    private String itemCover;
    private BigDecimal price;
    private String meetSpotInfo;
    private Integer status;
    private String cancelReason;
    private LocalDateTime orderTime;
    private LocalDateTime payTime;
    private LocalDateTime completeTime;
    private LocalDateTime cancelTime;
}
