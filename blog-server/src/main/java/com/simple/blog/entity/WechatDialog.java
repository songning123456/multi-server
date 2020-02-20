package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: songning
 * @date: 2020/2/19 21:37
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Entity
@Table(name = "WechatDialog")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class WechatDialog {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "author", columnDefinition = "VARCHAR(64) NOT NULL COMMENT '作者'")
    private String author;

    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;

    @Column(name = "headPortrait", columnDefinition = "VARCHAR(255) COMMENT '头像'")
    private String headPortrait;

    @Column(name = "message", columnDefinition = "TEXT COMMENT '对话消息'")
    private String message;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;
}
