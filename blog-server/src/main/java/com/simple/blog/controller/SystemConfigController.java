package com.simple.blog.controller;

import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.SystemConfigDTO;
import com.simple.blog.service.SystemConfigService;
import com.simple.blog.vo.SystemConfigVO;
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
 * @date: 2019/10/7 12:02
 */

@RestController
@Slf4j
@RequestMapping("/systemConfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/getLike")
    @AControllerAspect(description = "模糊查询系统配置")
    public CommonDTO<SystemConfigDTO> getLikeSystemConfig(@RequestBody CommonVO<SystemConfigVO> commonVO) {
        CommonDTO<SystemConfigDTO> commonDTO = systemConfigService.getSystemConfig(commonVO);
        return commonDTO;
    }

    @PostMapping("/update")
    @AControllerAspect(description = "更新系统配置")
    public <T> CommonDTO<T> updateSystemConfig(@RequestBody CommonVO<SystemConfigVO> commonVO) {
        CommonDTO<T> commonDTO = systemConfigService.updateSystemConfig(commonVO);
        return commonDTO;
    }
}
