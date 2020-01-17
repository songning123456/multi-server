package com.simple.blog.controller;

import com.sn.common.annotation.ControllerAspectAnnotation;
import com.simple.blog.configure.ValidateCode;
import com.sn.common.dto.CommonDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning
 * @date 2019/9/17
 * description
 */
@RestController
@RequestMapping("/validate")
public class ValidateCodeController {

    /**
     * 生成验证码图片
     *
     * @param request
     * @param response
     */
    @RequestMapping("/getCaptchaImage")
    @ResponseBody
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");
            ValidateCode validateCode = new ValidateCode();

            // 直接返回图片
            validateCode.getRandomCodeImage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ControllerAspectAnnotation(description = "生成图片base64")
    @RequestMapping("/getCaptchaBase64")
    @ResponseBody
    public <T> CommonDTO<T> getCaptchaBase64(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>(2);
        CommonDTO<T> commonDTO = new CommonDTO<>();
        try {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");

            ValidateCode validateCode = new ValidateCode();

            // 返回base64
            String base64String = validateCode.getRandomCodeBase64(request, response);
            result.put("url", "data:image/png;base64," + base64String);
            commonDTO.setDataExt(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonDTO;
    }
}
