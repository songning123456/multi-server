package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author songning
 * @date 2020/2/24
 * description
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Entity
@Table(name = "File")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class File {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "fileType", columnDefinition = "VARCHAR(20) COMMENT '文件类型'")
    private String fileType;

    @Column(name = "fileName", columnDefinition = "VARCHAR(120) COMMENT '文件名称'")
    private String fileName;

    @Column(name = "fileSrc", columnDefinition = "VARCHAR(255) COMMENT '文件地址'")
    private String fileSrc;

    @Column(name = "userId", columnDefinition = "VARCHAR(60) COMMENT '用户ID'")
    private String userId;

    @Column(name = "username", columnDefinition = "VARCHAR(60) COMMENT '用户名'")
    private String username;

    @Column(name = "coverSrc", columnDefinition = "VARCHAR(255) COMMENT '封面地址'")
    private String coverSrc;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;
}
