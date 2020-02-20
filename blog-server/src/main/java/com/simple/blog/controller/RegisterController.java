package com.simple.blog.controller;

import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.RegisterDTO;
import com.simple.blog.service.RegisterService;
import com.simple.blog.vo.RegisterVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2019/11/1
 * description
 */
@RestController
@Slf4j
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/all")
    @AControllerAspect(description = "注册users, system-config, blogger, labelRelation表")
    public CommonDTO<RegisterDTO> registerAll(@RequestBody CommonVO<RegisterVO> commonVO) {
        CommonDTO<RegisterDTO> commonDTO = registerService.registerAll(commonVO);
        return commonDTO;
    }
}
