package com.simple.blog.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author sn
 */

@Data
public class BloggerDTO {
    private String id;

    private String author;

    private String realName;

    private String gender;

    private Integer age;

    private String profession;

    private String telephone;

    private String email;

    private String motto;

    private Date updateTime;

    private String headPortrait;
}
