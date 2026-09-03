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
public class ChatMessage implements Serializable {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long itemId;        // 从商品页发起时关联的商品
    private String content;
    private Integer isRead;     // 1已读 0未读
    private LocalDateTime createTime;
}
