package com.simple.blog.repository;

import com.simple.blog.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author songning
 * @date 2019/12/11
 * description
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    @Query(value = "select * from image where username = ?1 order by update_time desc",
            countQuery = "select count(*) from image where username = ?1 order by update_time desc", nativeQuery = true)
    Page<Image> findImageSrcByUsernameNative(String username, Pageable pageable);

    @Query(value = "select * from image where user_id = ?1 order by update_time desc",
            countQuery = "select count(*) from image where user_id = ?1 order by update_time desc", nativeQuery = true)
    Page<Image> findImageSrcByUserIdNative(String userId, Pageable pageable);
}
