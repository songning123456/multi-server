package com.es.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleDTO {
    private String id;

    private String title;

    private Integer readTimes;

    private String kinds;

    private String content;

    private String author;

    private Date updateTime;

    private List<String> searchResult;

    private String userId;
}
