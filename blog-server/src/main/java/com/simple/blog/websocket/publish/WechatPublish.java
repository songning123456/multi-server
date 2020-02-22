package com.simple.blog.websocket.publish;

import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.websocket.handler.WechatHandler;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2020/2/18
 * description
 */
@Component
@Slf4j
public class WechatPublish {

    @Autowired
    private BloggerRepository bloggerRepository;

    /**
     * 通知 在线人数信息
     */
    public void publish() {
        List<Map<String, Object>> list = bloggerRepository.findByOnlineNative();
        JSONArray jsonArray = new JSONArray(list);
        for (Map.Entry<String, WebSocketSession> entry : WechatHandler.WECHAT_MAP.entrySet()) {
            WebSocketSession wss = entry.getValue();
            synchronized (wss) {
                if (wss.isOpen()) {
                    try {
                        wss.sendMessage(new TextMessage(jsonArray.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
