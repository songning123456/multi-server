package com.simple.blog.websocket.publish;

import com.simple.blog.websocket.handler.WechatHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * @author songning
 * @date 2020/2/18
 * description
 */
@Component
@Slf4j
public class WechatPublish {

    private Map<String, WebSocketSession> wechatMap = WechatHandler.WECHAT_MAP;

    public void send() {
    }
}
