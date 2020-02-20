package com.simple.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.MailDTO;
import com.simple.blog.service.MailService;
import com.simple.blog.vo.MailVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author songning on 2019/9/14 3:37 PM
 */

@RestController
@Slf4j
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    @AControllerAspect(description = "发送邮件")
    public CommonDTO<MailDTO> sendMail(@RequestParam("jsonData") String jsonData, @RequestParam("file") MultipartFile[] files) throws Exception {
        CommonVO<JSONObject> tempVO = JSON.toJavaObject(JSON.parseObject(jsonData), CommonVO.class);
        MailVO mailVO = JSON.toJavaObject(tempVO.getCondition(), MailVO.class);
        CommonVO<MailVO> commonVO = new CommonVO<>();
        commonVO.setCondition(mailVO);
        commonVO.getCondition().setMultipartFiles(files);
        CommonDTO<MailDTO> commonDTO = mailService.sendMail(commonVO);
        return commonDTO;
    }

    @PostMapping("/draft")
    @AControllerAspect(description = "保存草稿")
    public CommonDTO<MailDTO> saveDrafts(@RequestBody CommonVO<MailVO> commonVO) {
        CommonDTO<MailDTO> commonDTO = mailService.saveDraft(commonVO);
        return commonDTO;
    }
}
