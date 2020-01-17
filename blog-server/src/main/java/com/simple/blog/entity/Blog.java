package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sn
 * 博客信息表
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "Blog")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Blog {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "title", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '标题'")
    private String title;

    @Column(name = "readTimes", columnDefinition = "INT NOT NULL default 0 COMMENT '阅读次数'")
    private Integer readTimes;

    @Column(name = "kinds", columnDefinition = "VARCHAR(255)  NOT NULL COMMENT '种类'")
    private String kinds;

    @Column(name = "author", columnDefinition = "VARCHAR(255)  NOT NULL COMMENT '作者'")
    private String author;

    @Column(name = "content", columnDefinition = "TEXT  NOT NULL COMMENT '内容'")
    private String content;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;

    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;
}
