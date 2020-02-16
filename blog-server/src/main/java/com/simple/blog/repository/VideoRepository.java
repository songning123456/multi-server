package com.simple.blog.repository;

import com.simple.blog.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author: songning
 * @date: 2020/2/11 11:57
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    @Query(value = "select * from video where username = ?1 order by update_time desc",
            countQuery = "select count(*) from video where username = ?1 order by update_time desc", nativeQuery = true)
    Page<Video> findVideoByUsernameNative(String username, Pageable pageable);

    @Query(value = "select * from video where user_id = ?1 order by update_time desc",
            countQuery = "select count(*) from video where user_id = ?1 order by update_time desc", nativeQuery = true)
    Page<Video> findVideoByUserIdNative(String userId, Pageable pageable);
}
