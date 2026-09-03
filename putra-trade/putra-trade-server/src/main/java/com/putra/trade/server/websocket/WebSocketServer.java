package com.putra.trade.server.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 站内实时推送：ws://localhost:8080/ws/{memberId}
 * <p>
 * 前端登录后用 memberId 建立连接；下单/被想要/平台通知时服务端主动 push。
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{memberId}")
public class WebSocketServer {

    /** memberId → session */
    private static final Map<Long, Session> SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("memberId") Long memberId) {
        SESSIONS.put(memberId, session);
        log.info("WebSocket 连接建立: memberId={}, 在线数={}", memberId, SESSIONS.size());
    }

    @OnClose
    public void onClose(@PathParam("memberId") Long memberId) {
        SESSIONS.remove(memberId);
        log.info("WebSocket 连接关闭: memberId={}", memberId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("memberId") Long memberId) {
        log.info("收到客户端心跳: memberId={}, msg={}", memberId, message);
    }

    /**
     * 向指定用户推送（不在线则静默丢弃，消息已落库，用户上线后拉取）
     */
    public static void sendToMember(Long memberId, String content) {
        Session session = SESSIONS.get(memberId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(content);
            } catch (IOException e) {
                log.error("WebSocket 推送失败: memberId={}", memberId, e);
            }
        }
    }
}
