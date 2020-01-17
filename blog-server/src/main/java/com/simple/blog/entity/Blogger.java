package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sn
 * 博主信息表
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Entity
@Table(name = "Blogger")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Blogger {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "author", columnDefinition = "VARCHAR(64) NOT NULL COMMENT '作者'")
    private String author;

    @Column(name = "realName", columnDefinition = "VARCHAR(64) COMMENT '真实姓名'")
    private String realName;

    @Column(name = "gender", columnDefinition = "VARCHAR(4) COMMENT '性别'")
    private String gender;

    @Column(name = "age", columnDefinition = "INT COMMENT '年龄'")
    private Integer age;

    @Column(name = "profession", columnDefinition = "VARCHAR(64) COMMENT '职业'")
    private String profession;

    @Column(name = "telephone", columnDefinition = "VARCHAR(64) COMMENT '电话'")
    private String telephone;

    @Column(name = "email", columnDefinition = "VARCHAR(64) COMMENT '邮箱'")
    private String email;

    @Column(name = "motto", columnDefinition = "VARCHAR(255) COMMENT '座右铭'")
    private String motto;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;

    @Column(name = "headPortrait", columnDefinition = "VARCHAR(255) COMMENT '头像'")
    private String headPortrait;

    @Column(name = "username", columnDefinition = "VARCHAR(60) NOT NULL UNIQUE COMMENT '用户名'")
    private String username;

    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;
}
