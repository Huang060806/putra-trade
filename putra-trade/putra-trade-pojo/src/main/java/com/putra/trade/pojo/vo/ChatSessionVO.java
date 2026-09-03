package com.putra.trade.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话列表项：对方信息 + 最后一条消息 + 未读数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionVO implements Serializable {
    private Long peerId;
    private String peerNickname;
    private String peerAvatar;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Integer unreadCount;
}
