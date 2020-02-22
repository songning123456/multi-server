package com.simple.blog.repository;

import com.simple.blog.entity.WechatDialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/2/19 22:55
 */
@Repository
public interface WechatDialogRepository extends JpaRepository<WechatDialog, String> {

    @Query(value = "select * from wechat_dialog order by update_time asc limit ?1,?2", nativeQuery = true)
    List<WechatDialog> findLimitNative(int offset, int size);
}
