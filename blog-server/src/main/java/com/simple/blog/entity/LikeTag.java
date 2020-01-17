package com.simple.blog.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author songning
 * @date 2019/9/26
 * description
 */
@Data
@Entity
@Table(name = "LikeTag",uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "articleId"})})
public class LikeTag {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '用户名'")
    private String username;

    @Column(name = "love", columnDefinition = "INT NOT NULL DEFAULT 0 COMMENT '是否点赞'")
    private Integer love;

    @Column(name = "has_read", columnDefinition = "INT NOT NULL DEFAULT 0 COMMENT '是否已读'")
    private Integer hasRead;

    @Column(name = "articleId", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '文章ID'")
    private String articleId;
}
