package com.putra.trade.server.service;

import com.github.pagehelper.PageHelper;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.entity.Message;
import com.putra.trade.server.mapper.MessageMapper;
import com.putra.trade.server.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    public static final int TYPE_ORDER = 1;
    public static final int TYPE_ORDER_CHANGE = 2;
    public static final int TYPE_PLATFORM = 3;
    public static final int TYPE_WANT = 4;

    private final MessageMapper messageMapper;

    /** 落库 + WebSocket 实时推送 */
    public void send(Long receiverId, Integer type, String content, Long orderId) {
        Message message = Message.builder()
                .receiverId(receiverId).type(type).content(content).orderId(orderId).isRead(0).build();
        messageMapper.insert(message);
        WebSocketServer.sendToMember(receiverId, content);
    }

    public PageResult page(Long memberId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        var p = messageMapper.pageByReceiver(memberId);
        return new PageResult(p.getTotal(), p.getResult());
    }

    public int unread(Long memberId) {
        return messageMapper.countUnread(memberId);
    }

    public void readAll(Long memberId) {
        messageMapper.readAll(memberId);
    }
}
