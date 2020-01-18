package com.es.blog.vo;

import lombok.Data;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@Data
public class ArticleVO {
    private String id;

    private String title;

    private Integer readTimes;

    private String kinds;

    private String content;

    private String author;
}
