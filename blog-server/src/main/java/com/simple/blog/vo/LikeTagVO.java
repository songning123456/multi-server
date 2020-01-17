package com.simple.blog.vo;

import lombok.Data;

/**
 * @author songning
 * @date 2019/9/26
 * description
 */
@Data
public class LikeTagVO {

    private String username;

    private String articleId;

    private Integer love;

    private Integer hasRead;
}
