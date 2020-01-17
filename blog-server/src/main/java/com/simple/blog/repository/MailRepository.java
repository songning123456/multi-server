package com.simple.blog.repository;

import com.simple.blog.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author songning
 * @date 2019/12/18
 * description
 */
@Repository
public interface MailRepository extends JpaRepository<Mail, String> {
}
