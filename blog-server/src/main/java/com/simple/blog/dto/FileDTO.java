package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songning
 * @date 2020/2/24
 * description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileDTO {

    private String fileType;

    private String fileName;

    private String fileSrc;

    private String userId;

    private String username;

    private String coverSrc;

    private String updateTime;
}
