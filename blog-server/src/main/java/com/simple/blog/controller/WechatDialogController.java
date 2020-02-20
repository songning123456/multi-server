package com.simple.blog.controller;

import com.simple.blog.dto.WechatDialogDTO;
import com.simple.blog.service.WechatDialogService;
import com.simple.blog.vo.WechatDialogVO;
import com.sn.common.annotation.AControllerAspect;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/2/20 21:07
 */
@RestController
@Slf4j
@RequestMapping(value = "/wechat")
public class WechatDialogController {

    @Autowired
    private WechatDialogService wechatDialogService;

    @PostMapping("/getDialog")
    @AControllerAspect(description = "获取对话信息")
    public CommonDTO<WechatDialogDTO> getDialogs(@RequestBody CommonVO<WechatDialogVO> commonVO) {
        CommonDTO<WechatDialogDTO> commonDTO = wechatDialogService.getDialog(commonVO);
        return commonDTO;
    }
}
