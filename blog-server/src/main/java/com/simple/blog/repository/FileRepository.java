package com.simple.blog.repository;

import com.simple.blog.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * @author songning
 * @date 2020/2/24
 * description
 */
@Repository
public interface FileRepository extends JpaRepository<File, String> {

    @Query(value = "select * from file where username = ?1 and file_type = ?2 order by update_time desc", countQuery = "select count(1) from file where username = ?1", nativeQuery = true)
    Page<File> findByUsernameNative(String username, String fileType, Pageable pageable);

    @Query(value = "select * from file where user_id = ?1 and file_type = ?2 order by update_time desc", countQuery = "select count(1) from file where user_id = ?1", nativeQuery = true)
    Page<File> findByUserIdNative(String userId, String fileType, Pageable pageable);
}
