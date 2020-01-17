package com.simple.blog.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author songning on 2019/9/13 9:56 PM
 */
@Data
public class MailVO {

    /**
     * 邮件发送人
     */
    private String sender;

    /**
     * 邮件接收人（多个邮箱则用逗号","隔开）
     */
    private String recipient;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 抄送（多个邮箱则用逗号","隔开）
     */
    private String cc;

    /**
     * 密送（多个邮箱则用逗号","隔开）
     */
    private String bcc;

    /**
     * 授权码
     */
    private String password;

    /**
     * 邮件附件
     */
    @JsonIgnore
    private MultipartFile[] multipartFiles;
}
