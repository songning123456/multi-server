package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author songning
 * @create 2019/8/5 10:57
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "LabelRelation", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "labelName"})})
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class LabelRelation {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "username", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '用户名'")
    private String username;

    @Column(name = "labelName", columnDefinition = "VARCHAR(50)  NOT NULL COMMENT '标签名'")
    private String labelName;

    @Column(name = "attention", columnDefinition = "INT NOT NULL DEFAULT 0 COMMENT '是否关注'")
    private Integer attention;
}
