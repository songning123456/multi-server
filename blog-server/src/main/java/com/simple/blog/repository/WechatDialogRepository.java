package com.simple.blog.repository;

import com.simple.blog.entity.WechatDialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/2/19 22:55
 */
@Repository
public interface WechatDialogRepository extends JpaRepository<WechatDialog, String> {

    @Query(value = "select author, head_portrait as headPortrait, message, update_time as updateTime, user_id as userId from wechat_dialog", nativeQuery = true)
    List<Map<String, Object>> findNative();
}
