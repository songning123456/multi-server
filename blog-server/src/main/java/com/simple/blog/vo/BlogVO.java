package com.simple.blog.vo;

import lombok.Data;

/**
 * @author sn
 */
@Data
public class BlogVO {
    private String id;

    private String title;

    private Integer readTimes;

    private String kinds;

    private String content;

    private String author;

    /**
     * 模糊查询时的参数
     */
    private String param;

    /**
     * 模糊查询的类型
     */
    private String type;
}
