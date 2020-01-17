package com.simple.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sn.common.constant.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning on 2019/9/21 5:13 PM
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> message = new HashMap<>(2);
        message.put("message", "当前角色无权访问");
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        //将token信息写入
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
