package com.campus.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/ws/admin")
@Slf4j
public class WebSocketServer {

    // 存放当前连接的所有管理员的 Session
    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        log.info("管理员已连接，当前在线管理员数：{}", sessions.size());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        log.info("管理员断开连接，当前在线管理员数：{}", sessions.size());
    }

    /**
     * 群发消息给所有在线的管理员
     */
    public void sendToAllAdmin(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("WebSocket推送消息失败", e);
            }
        }
    }
}