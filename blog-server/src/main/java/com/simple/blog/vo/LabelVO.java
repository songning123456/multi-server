package com.simple.blog.vo;

import lombok.Data;

/**
 * @author: songning
 * @date: 2019/10/13 18:39
 */
@Data
public class LabelVO {
    private String labelName;

    private String username;

    private String labelFuzzyName;

    private Integer attention;
}
