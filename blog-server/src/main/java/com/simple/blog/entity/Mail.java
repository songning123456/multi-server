package com.simple.blog.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by songning on 2019/9/14 2:01 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "Mail")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Mail {
    /**
     * 邮件id
     */
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "sender", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '邮件发送人'")
    private String sender;

    @Column(name = "password", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '授权码'")
    private String password;

    @Column(name = "recipient", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '邮件接收人'")
    private String recipient;

    @Column(name = "subject", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '邮件主题'")
    private String subject;

    @Column(name = "content", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '邮件内容'")
    private String content;

    @Column(name = "sentDate", columnDefinition = "DATETIME NOT NULL COMMENT '标题'")
    private Date sentDate;

    @Column(name = "cc", columnDefinition = "VARCHAR(255) COMMENT '抄送'")
    private String cc;

    @Column(name = "bcc", columnDefinition = "VARCHAR(255) COMMENT '密送'")
    private String bcc;

    @Column(name = "status", columnDefinition = "VARCHAR(12) NOT NULL COMMENT '已发送,未发送'")
    private String status;
}
