package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: songning
 * @date: 2020/2/20 21:05
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WechatDialogDTO {

    private String id;

    private String author;

    private String headPortrait;

    private String message;

    private String updateTime;

    private String userId;
}
