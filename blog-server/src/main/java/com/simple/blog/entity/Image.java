package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author songning
 * @date 2019/12/11
 * description
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Entity
@Table(name = "Image")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Image {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "username", columnDefinition = "VARCHAR(60) COMMENT '用户名'")
    private String username;

    @Column(name = "imageSrc", columnDefinition = "VARCHAR(120) COMMENT '图片地址'")
    private String imageSrc;

    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;

    @Column(name = "name", columnDefinition = "VARCHAR(120) COMMENT '图片名称'")
    private String name;
}
