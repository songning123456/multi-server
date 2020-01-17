package com.simple.blog.dto;

import lombok.Data;

/**
 * @author: songning
 * @date: 2019/10/7 11:57
 */
@Data
public class SystemConfigDTO {

    private String configKey;

    private String configValue;

    private String valueDescription;
}
