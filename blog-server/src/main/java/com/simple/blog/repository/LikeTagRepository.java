package com.simple.blog.repository;

import com.simple.blog.entity.LikeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2019/9/26
 * description
 */
@Repository
public interface LikeTagRepository extends JpaRepository<LikeTag, Long> {

    @Query(value = "select * from like_tag where username = ?1 and article_id = ?2", nativeQuery = true)
    LikeTag getNative(String username, String articleId);

    @Query(value = "select SUM(love) as tags from like_tag where article_id = ?1", nativeQuery = true)
    Map<String, Object> sumByArticleIdNative(String articleId);

    @Modifying
    @Transactional
    @Query(value = "update like_tag set love = ?3 where username = ?1 and article_id = ?2", nativeQuery = true)
    void updateTagLikeByUsernameAndArticleIdNative(String username, String articleId, Integer love);

    @Modifying
    @Transactional
    @Query(value = "update like_tag set has_read = 1 where username = ?1 and article_id = ?2", nativeQuery = true)
    void updateHasReadByUsernameAndArticleIdNative(String username, String articleId);

    @Query(value = "select article_id from like_tag where username = ?1 and love = 1", nativeQuery = true)
    List<String> getArticleIdByUserNameAndLoveNative(String username);

    @Modifying
    @Transactional
    void deleteByArticleId(String articleId);
}
