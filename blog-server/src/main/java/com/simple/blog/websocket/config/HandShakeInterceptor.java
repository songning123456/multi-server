package com.simple.blog.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author songning
 * @date 2020/2/18
 * description
 */
@Slf4j
public class HandShakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        //获取token认证
        String token = req.getServletRequest().getParameter("Authorization");
        if (StringUtils.isEmpty(token) || "undefined".equals(token)) {
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        log.info("Websocket connected. remoteAddress :[{}]", request.getRemoteAddress());
    }
}
