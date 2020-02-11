package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author: songning
 * @date: 2020/2/11 11:13
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Entity
@Table(name = "Video")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Video {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "username", columnDefinition = "VARCHAR(60) COMMENT '用户名'")
    private String username;


    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;

    @Column(name = "src", columnDefinition = "VARCHAR(120) COMMENT '视频地址'")
    private String src;

    @Column(name = "kind", columnDefinition = "VARCHAR(120) COMMENT '视频格式类型'")
    private String type;

    @Column(name = "name", columnDefinition = "VARCHAR(120) COMMENT '视频名称'")
    private String name;

    @Column(name = "cover", columnDefinition = "VARCHAR(120) COMMENT '视频封面地址'")
    private String cover;
}
