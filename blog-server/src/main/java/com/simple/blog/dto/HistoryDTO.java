package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songning
 * @date 2019/10/22
 * description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistoryDTO {

    private String title;

    private String description;

    private String time;

    private String articleId;

    private String username;
}
