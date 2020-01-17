package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: songning
 * @date: 2019/10/13 18:32
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LabelDTO {

    private String id;

    private String labelName;

    private String labelPhoto;

    /**
     * 此标签的文章总数
     */
    private Long numOfArticle;

    /**
     * 此标签的关注总数
     */
    private Long numOfAttention;

    private Integer isAttention;
}
