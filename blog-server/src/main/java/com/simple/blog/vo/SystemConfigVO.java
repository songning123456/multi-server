package com.simple.blog.vo;

import lombok.Data;

/**
 * @author: songning
 * @date: 2019/10/7 11:58
 */
@Data
public class SystemConfigVO {

    private String configKey;

    private String configValue;

    private String username;

    private String valueDescription;
}
