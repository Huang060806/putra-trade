package com.putra.trade.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private Long id;
    private Long receiverId;
    private Integer type;       // 1新订单提醒 2订单状态变更 3平台通知 4有人想要
    private String content;
    private Long orderId;
    private Integer isRead;     // 1已读 0未读
    private LocalDateTime createTime;
}
