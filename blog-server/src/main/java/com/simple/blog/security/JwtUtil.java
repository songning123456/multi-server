package com.simple.blog.security;

import com.sn.common.util.MapConvertEntityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author on 2019/9/21 5:07 PM
 */
@Slf4j
public class JwtUtil {
    private static Map<String, Authentication> hashMap = new HashMap<>();

    /**
     * 生成JWT token
     *
     * @param authentication
     * @return
     */
    public static String generateToken(Authentication authentication) {
        hashMap.put("authentication", authentication);
        String token = Jwts.builder()
                //设置token的信息
                //将认证后的authentication写入token，验证时，直接验证它
                .claim("authentication", authentication)
                //设置主题
                .setSubject("simpleBlogSecurity")
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                //加密方式及秘钥
                .signWith(SignatureAlgorithm.HS512, "blogJWT")
                .compact();

        return token;
    }

    public static void tokenParser(String token) {
        Authentication authentication1 = hashMap.get("authentication");
        // 解析token.
        Claims claims = Jwts.parser()
                .setSigningKey("blogJWT")
                .parseClaimsJws(token)
                .getBody();
        // 获取过期时间
        Date claimsExpiration = claims.getExpiration();
        log.info("过期时间" + claimsExpiration);
        //判断是否过期
        Date now = new Date();
        if (now.getTime() > claimsExpiration.getTime()) {
            throw new AuthenticationServiceException("凭证已过期，请重新登录！");
        }
        // 获取保存在token中的登录认证成功的authentication，
        // 利用UsernamePasswordAuthenticationToken生成新的authentication
        // 放入到SecurityContextHolder，表示认证通过
        Object tokenInfo = claims.get("authentication");
        //通过com.alibaba.fastjson将其在转换
        JwtAuthentication tokenAuthentication = null;
        try {
            tokenAuthentication = (JwtAuthentication) MapConvertEntityUtil.mapToEntity(JwtAuthentication.class, (Map) tokenInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
    }
}
