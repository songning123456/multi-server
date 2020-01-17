package com.simple.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author songning
 * @date 2019/11/1
 * description
 */
@Data
public class RegisterVO {

    private String username;

    private String realName;

    private String author;

    private String email;

    private String motto;

    private String profession;

    private String telephone;

    private Integer age;

    private String gender;

    private String headPortrait;

    private String password;

    private List<LabelVO> labelVOS;
}
