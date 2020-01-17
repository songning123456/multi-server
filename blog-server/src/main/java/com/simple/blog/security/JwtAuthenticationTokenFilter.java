package com.simple.blog.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author songning on 2019/9/21 4:17 PM
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //获取JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            JwtUtil.tokenParser(authHeader);
        }
        filterChain.doFilter(request, response);
    }
}
