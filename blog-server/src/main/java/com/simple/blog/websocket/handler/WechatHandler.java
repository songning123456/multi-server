package com.simple.blog.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        try {
            String payloadStr = webSocketMessage.getPayload().toString();
            if (PING.equals(payloadStr)) {
                webSocketSession.sendMessage(new TextMessage(PONG));
            } else {
                JSONObject jsonObject = JSON.parseObject(payloadStr);
                String id = jsonObject.get("userId").toString();
                WECHAT_MAP.put(id, webSocketSession);
                for (Map.Entry<String, WebSocketSession> entry : WECHAT_MAP.entrySet()) {
                    WebSocketSession wss = entry.getValue();
                    wss.sendMessage(new TextMessage(payloadStr));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
