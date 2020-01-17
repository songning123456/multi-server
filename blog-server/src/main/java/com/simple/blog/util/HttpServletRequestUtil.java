package com.simple.blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author songning
 * @date 2019/10/21
 * description
 */
@Component
public class HttpServletRequestUtil {

    public String getUsername() {
        String username = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("Authorization");
            // 解析token.
            Claims claims = Jwts.parser().setSigningKey("blogJWT").parseClaimsJws(token).getBody();
            username = String.valueOf(((Map) claims.get("authentication")).get("name"));
        }
        return username;
    }
}
