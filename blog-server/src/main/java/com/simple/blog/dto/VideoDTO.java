package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: songning
 * @date: 2020/2/11 10:59
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VideoDTO {

    /**
     * 路径
     */
    private String src;

    /**
     * 类型 'video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'
     */
    private String type;

    private String cover;

    private String name;

    private String updateTime;
}
