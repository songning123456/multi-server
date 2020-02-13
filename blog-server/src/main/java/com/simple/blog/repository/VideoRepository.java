package com.simple.blog.repository;

import com.simple.blog.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/2/11 11:57
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    @Query(value = "select * from video where username = ?1", nativeQuery = true)
    List<Video> findVideoByUsernameNative(String username);
}
