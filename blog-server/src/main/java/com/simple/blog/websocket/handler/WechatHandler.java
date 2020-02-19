package com.simple.blog.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songning
 * @date 2020/2/18
 * description
 */
@Slf4j
@Component
public class WechatHandler implements org.springframework.web.socket.WebSocketHandler {
    public static final Map<String, WebSocketSession> WECHAT_MAP;
    private static final String PING = "ping";
    private static final String PONG = "pong";

    static {
        WECHAT_MAP = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        log.info("webSocket打开: {}", webSocketSession.getId());
        WECHAT_MAP.put(webSocketSession.getId(), webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        try {
            String payloadStr = webSocketMessage.getPayload().toString();
            if (PING.equals(payloadStr)) {
                webSocketSession.sendMessage(new TextMessage(PONG));
            } else {
                for (Map.Entry<String, WebSocketSession> entry : WECHAT_MAP.entrySet()) {
                    WebSocketSession wss = entry.getValue();
                    if (wss.isOpen()) {
                        try {
                            wss.sendMessage(new TextMessage(payloadStr));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        WECHAT_MAP.remove(webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        WECHAT_MAP.remove(webSocketSession.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
