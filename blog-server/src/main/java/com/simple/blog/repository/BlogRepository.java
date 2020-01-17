package com.simple.blog.repository;

import com.simple.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author sn
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {

    /**
     * 查询摘要等信息
     *
     * @param pageable
     * @return
     */
    @Query(value = "select id, title, user_id as userId,read_times as readTimes, kinds, author, update_time as updateTime " +
            "from blog where kinds = :kinds order by update_time desc",
            countQuery = "select count(*) from blog where kinds = :kinds", nativeQuery = true)
    Page<Map<String, Object>> findAbstract(@Param("kinds") String kinds, Pageable pageable);

    @Query(value = "select id, title, user_id as userId,read_times as readTimes, kinds, author, update_time as updateTime " +
            "from blog where user_id = ?1 order by update_time desc",
            countQuery = "select count(*) from blog where user_id = ?1", nativeQuery = true)
    Page<Map<String, Object>> findByUserIdNative(String userId, Pageable pageable);

    @Query(value = "select id, title, user_id as userId,read_times as readTimes, kinds, author, update_time as updateTime " +
            "from blog where id in (?1) order by update_time desc",
            countQuery = "select count(*) from blog where id in (?1)", nativeQuery = true)
    Page<Map<String, Object>> findByIdNative(List<String> articleIds, Pageable pageable);


    /**
     * 查询文章详情
     *
     * @param id
     * @return
     */
    @Query(value = "select author, title, content, update_time as updateTime, read_times as readTimes from blog where id= :id", nativeQuery = true)
    Map<String, Object> findByIdNative(@Param("id") String id);

    @Query(value = "select id, author,user_id as userId, kinds, title, read_times as readTimes, update_time as updateTime from blog where kinds = :kinds order by readTimes desc, update_time desc limit 5", nativeQuery = true)
    List<Map<String, Object>> findHotArticle(@Param("kinds") String kinds);

    @Transactional
    @Modifying
    @Query(value = "update blog set read_times = :readTimes where id = :id", nativeQuery = true)
    Integer updateReadTimes(@Param("id") String id, @Param("readTimes") Integer readTimes);

    /**
     * 主要用于测试输出内容到文件和Map<String ,Object>
     *
     * @return
     */
    @Query(value = "select author, title from blog order by update_time desc", nativeQuery = true)
    List<Map<String, Object>> getAllAuthorAndTitle();

    @Query(value = "select id, title, author, update_time as updateTime, content,user_id as userId from blog where content like %?1%", nativeQuery = true)
    List<Map<String, Object>> findByLikeContentNative(String content, Pageable pageable);

    @Query(value = "select count(*) as yAxis, kinds as xAxis from blog where update_time >= ?1 and update_time <= ?2 group by kinds", nativeQuery = true)
    List<Map<String, Object>> statisticKinds(String startTime, String endTime);

    @Query(value = "select count(*) as yAxis, author as xAxis from blog where update_time >= ?1 and update_time <= ?2 group by author", nativeQuery = true)
    List<Map<String, Object>> statisticAuthor(String startTime, String endTime);

    Long countAllByKinds(String kinds);

    @Query(value = "select count(*) as total from blog where title = ?1", nativeQuery = true)
    Map<String, Object> countArticleByTitleNative(String title);

    @Query(value = "select distinct(author) from simple_blog.blog", nativeQuery = true)
    List<String> getAllAuthorNative();
}
