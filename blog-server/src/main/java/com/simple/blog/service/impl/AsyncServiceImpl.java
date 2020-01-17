package com.simple.blog.service.impl;

import com.simple.blog.service.AsyncService;
import com.simple.blog.service.CacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: songning
 * @date: 2019/11/30 17:13
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private CacheService cacheService;

    @Override
    @Async
    public void refreshPersonalCache(String token){
        // 解析token.
        Claims claims = Jwts.parser().setSigningKey("blogJWT").parseClaimsJws(token).getBody();
        String username = String.valueOf(((Map) claims.get("authentication")).get("name"));
        cacheService.refreshPersonAttentionLabel(username);
        cacheService.refreshSystemConfig(username);
    }
}
