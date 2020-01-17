package com.simple.blog.repository;

import com.simple.blog.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author songning
 * @date 2019/10/22
 * description
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    @Query(value = "select * from history where username = ?1", countQuery = "select count(*) from history where username = ?1", nativeQuery = true)
    Page<History> findHistoryNative(String username, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByArticleId(String articleId);
}
