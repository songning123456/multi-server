package com.simple.blog.repository;

import com.simple.blog.entity.Image;
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

    @Query(value = "select image_src from image where username = ?1", nativeQuery = true)
    List<String> findImageSrcByUsernameNative(String username);

    @Query(value = "select image_src from image where user_id = ?1", nativeQuery = true)
    List<String> findImageSrcByUserIdNative(String userId);
}
