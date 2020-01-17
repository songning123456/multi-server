package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author songning
 * @date 2019/10/22
 * description
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "History")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class History {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "title", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '标题'")
    private String title;

    @Column(name = "username", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '用户名'")
    private String username;

    @Column(name = "articleId", columnDefinition = "VARCHAR(60) COMMENT '文章ID'")
    private String articleId;

    @Column(name = "description", columnDefinition = "VARCHAR(255) COMMENT '描述详情'")
    private String description;

    @Column(name = "time", columnDefinition = "VARCHAR(50) NOT NULL COMMENT '时间'")
    private String time;
}
