package com.simple.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.blog.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning on 2019/9/21 3:51 PM
 */
@Slf4j
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AsyncService asyncService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        log.info("登录成功！");

        //登录成功后设置JWT
        String token = JwtUtil.generateToken(authentication);
        httpServletResponse.addHeader("Authorization", token);
        httpServletResponse.setStatus(200);
        //要做的工作就是将Authentication以json的形式返回给前端。 需要工具类ObjectMapper，Spring已自动注入。
        //设置返回类型
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Map<String, Object> tokenInfo = new HashMap<>(2);
        tokenInfo.put("Authorization", token);
        // 同 CommonDTO 中的status
        tokenInfo.put("status", 200);
        try {
            asyncService.refreshPersonalCache(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("刷新个人缓存失败");
        }
        //将token信息写入
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(tokenInfo));
    }
}
