package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author sn
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BlogDTO {
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
