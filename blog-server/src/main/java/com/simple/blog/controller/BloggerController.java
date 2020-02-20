package com.simple.blog.controller;

import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.BloggerDTO;
import com.simple.blog.service.BloggerService;
import com.simple.blog.vo.BloggerVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sn
 */
@RestController
@Slf4j
@RequestMapping(value = "/blogger")
public class BloggerController {

    @Autowired
    private BloggerService bloggerService;

    @PostMapping("/getBlogger")
    @AControllerAspect(description = "获取登陆信息")
    public CommonDTO<BloggerDTO> getBloggerInfo(@RequestBody CommonVO<BloggerVO> commonVO) {
        CommonDTO<BloggerDTO> commonDTO = bloggerService.getBlogger(commonVO);
        return commonDTO;
    }

    @PostMapping("/update")
    @AControllerAspect(description = "修改个人信息")
    public CommonDTO<BloggerDTO> updateBloggers(@RequestBody CommonVO<BloggerVO> commonVO) {
        CommonDTO<BloggerDTO> commonDTO = bloggerService.updateBlogger(commonVO);
        return commonDTO;
    }
}
