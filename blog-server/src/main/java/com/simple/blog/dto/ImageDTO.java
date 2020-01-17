package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songning
 * @date 2019/10/28
 * description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageDTO {
    private String imageSrc;
}
