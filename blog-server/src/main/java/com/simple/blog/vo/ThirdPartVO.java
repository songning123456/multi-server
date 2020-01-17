package com.simple.blog.vo;

import lombok.Data;

/**
 * @author songning
 * @date 2019/12/23
 * description
 */
@Data
public class ThirdPartVO {

    private String clientId;

    private String clientSecret;

    private String code;

    private String accessTokenURL;

    private String userURL;
}
