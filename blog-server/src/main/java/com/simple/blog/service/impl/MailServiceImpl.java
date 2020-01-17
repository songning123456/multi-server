package com.simple.blog.service.impl;

import com.simple.blog.dto.MailDTO;
import com.simple.blog.entity.Mail;
import com.simple.blog.repository.MailRepository;
import com.simple.blog.service.MailService;
import com.sn.common.util.ClassConvertUtil;
import com.simple.blog.vo.MailVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.Date;

/**
 * @author songning on 2019/9/13 10:05 PM
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private MailRepository mailRepository;

    @Override
    public CommonDTO<MailDTO> sendMail(CommonVO<MailVO> commonVO) throws Exception {
        checkMail(commonVO.getCondition());
        return sendMimeMail(commonVO);
    }

    @Override
    public CommonDTO<MailDTO> saveDraft(CommonVO<MailVO> commonVO) {
        CommonDTO<MailDTO> commonDTO = new CommonDTO<>();
        MailVO mailVO = commonVO.getCondition();
        Mail mail = new Mail();
        ClassConvertUtil.populate(mailVO, mail);
        mail.setSentDate(new Date());
        mail.setStatus("草稿");
        try {
            mailRepository.save(mail);
        } catch (Exception e) {
            commonDTO.setStatus(500);
            return commonDTO;
        }
        return commonDTO;
    }

    private CommonDTO<MailDTO> sendMimeMail(CommonVO<MailVO> commonVO) throws MessagingException {
        CommonDTO<MailDTO> commonDTO = new CommonDTO<>();
        MailVO mailVO = commonVO.getCondition();
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        String[] temps = mailVO.getSender().split("@");
        String host = "smtp." + temps[temps.length - 1];
        javaMailSender.setHost(host);
        javaMailSender.setUsername(mailVO.getSender());
        javaMailSender.setPassword(mailVO.getPassword());
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "UTF-8");
        mimeMessageHelper.setFrom(mailVO.getSender());
        mimeMessageHelper.setTo(mailVO.getRecipient().split(","));
        mimeMessageHelper.setSubject(mailVO.getSubject());
        mimeMessageHelper.setText(mailVO.getContent(), true);
        // 抄送
        if (!StringUtils.isEmpty(mailVO.getCc())) {
            mimeMessageHelper.setCc(mailVO.getCc().split(","));
        }
        //密送
        if (!StringUtils.isEmpty(mailVO.getBcc())) {
            mimeMessageHelper.setCc(mailVO.getBcc().split(","));
        }
        // 添加邮件附件
        if (mailVO.getMultipartFiles() != null) {
            for (MultipartFile multipartFile : mailVO.getMultipartFiles()) {
                mimeMessageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
            }
        }
        Date date = new Date();
        // 发送时间
        mimeMessageHelper.setSentDate(date);
        Mail mail = Mail.builder().sender(mailVO.getSender()).password(mailVO.getPassword()).bcc(mailVO.getBcc()).cc(mailVO.getCc()).content(mailVO.getContent()).recipient(mailVO.getRecipient()).sentDate(date).status("已发送").subject(mailVO.getSubject()).build();
        //正式发送邮件
        try {
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            commonDTO.setStatus(500);
            commonDTO.setMessage("邮件发送失败");
            log.error("邮件发送失败!!!");
            return commonDTO;
        }
        mailRepository.save(mail);
        return new CommonDTO<>();
    }

    private void checkMail(MailVO mailVO) throws Exception {
        if (StringUtils.isEmpty(mailVO.getRecipient())) {
            throw new Exception("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVO.getSubject())) {
            throw new Exception("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVO.getContent())) {
            throw new Exception("邮件内容不能为空");
        }
    }
}
