package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songning
 * @date 2019/10/24
 * description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsersDTO {

    private Boolean isExist;

    private String permission;
}
