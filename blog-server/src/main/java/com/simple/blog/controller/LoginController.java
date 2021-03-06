package com.simple.blog.controller;

import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.util.HttpServletRequestUtil;
import com.sn.common.annotation.AControllerAspect;
import com.sn.common.constant.HttpStatus;
import com.sn.common.dto.CommonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author songning
 * @date 2019/9/24
 * description
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private BloggerRepository bloggerRepository;

    /**
     * 无需登陆
     *
     * @return
     */
    @GetMapping("/hello")
    public String main() {
        try {
            Thread.sleep(20000);
            log.info("^^^^^^hello^^^^^^");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    @RequestMapping("/loginInfo")
    @AControllerAspect(description = "未登陆时的情况")
    public <T> CommonDTO<T> loginInfo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        commonDTO.setMessage("请先登录");
        commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        httpServletResponse.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        return commonDTO;
    }

    /**
     * 需要admin权限
     *
     * @return
     */
    @GetMapping("/admin")
    public String needAdminRole() {
        return "needAdminRole";
    }

    /**
     * 需要users权限
     *
     * @return
     */
    @GetMapping("/users")
    public String needUsersRole() {
        return "users角色才能访问";
    }

    @RequestMapping("/logoutSuccess")
    @AControllerAspect(description = "注销登录成功后跳转的路由")
    public <T> CommonDTO<T> logoutSuccess() {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        try {
            String username = httpServletRequestUtil.getUsername();
            bloggerRepository.updateByUsernameAndOnlineNative(username, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        commonDTO.setMessage("退出成功");
        return commonDTO;
    }
}
