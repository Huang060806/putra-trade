package com.putra.trade.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.ChatSendDTO;
import com.putra.trade.pojo.entity.ChatMessage;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.pojo.vo.ChatSessionVO;
import com.putra.trade.server.mapper.ChatMapper;
import com.putra.trade.server.mapper.MemberMapper;
import com.putra.trade.server.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;
    private final MemberMapper memberMapper;
    private final SensitiveWordService sensitiveWordService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 发送私聊：ban_chat 校验 → 敏感词拦截 → 落库 → WebSocket 实时推送（JSON）
     */
    public ChatMessage send(ChatSendDTO dto) {
        Long senderId = BaseContext.getCurrentId();
        if (senderId.equals(dto.getReceiverId())) {
            throw new BusinessException("不能和自己私聊");
        }

        Member sender = memberMapper.getById(senderId);
        if (sender.getBanChat() == 1) {
            throw new BusinessException("你已被禁止私聊，请联系管理员");
        }
        Member receiver = memberMapper.getById(dto.getReceiverId());
        if (receiver == null) {
            throw new BusinessException("对方用户不存在");
        }

        // 私聊内容命中敏感词直接拒绝（不走进人工审核，避免泄露给第三人）
        List<String> hits = sensitiveWordService.scan(dto.getContent());
        if (!hits.isEmpty()) {
            throw new BusinessException("消息包含违规内容，发送失败");
        }

        ChatMessage message = ChatMessage.builder()
                .senderId(senderId)
                .receiverId(dto.getReceiverId())
                .itemId(dto.getItemId())
                .content(dto.getContent())
                .isRead(0)
                .build();
        chatMapper.insert(message);

        // 在线实时推送；离线则等对方上线拉历史
        try {
            Map<String, Object> push = new HashMap<>();
            push.put("type", "chat");
            push.put("data", message);
            push.put("senderNickname", sender.getNickname());
            WebSocketServer.sendToMember(dto.getReceiverId(), objectMapper.writeValueAsString(push));
        } catch (Exception ignored) {
        }
        return message;
    }

    public List<ChatSessionVO> sessions() {
        return chatMapper.listSessions(BaseContext.getCurrentId());
    }

    /** 聊天记录（正序返回，便于直接渲染） */
    public PageResult history(Long peerId, int page, int pageSize) {
        Long memberId = BaseContext.getCurrentId();
        chatMapper.readAllFrom(memberId, peerId);
        Page<ChatMessage> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> chatMapper.pageHistory(memberId, peerId));
        List<ChatMessage> records = p.getResult();
        Collections.reverse(records);
        return new PageResult(p.getTotal(), records);
    }

    public int unread() {
        return chatMapper.countUnread(BaseContext.getCurrentId());
    }
}
