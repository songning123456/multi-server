package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @Author songning
 * @create 2019/8/14 8:13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "SystemConfig", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "configKey"})})
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SystemConfig {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "username", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '用户名'")
    private String username;

    @Column(name = "configKey", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '配置项key'")
    private String configKey;

    @Column(name = "configValue", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '配置项value'")
    private String configValue;

    @Column(name = "valueDescription", columnDefinition = "VARCHAR(255) COMMENT '配置项描述'")
    private String valueDescription;
}
