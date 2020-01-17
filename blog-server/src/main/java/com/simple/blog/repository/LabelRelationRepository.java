package com.simple.blog.repository;

import com.simple.blog.entity.LabelRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author songning
 * @create 2019/8/6 8:25
 */
@Repository
public interface LabelRelationRepository extends JpaRepository<LabelRelation, String> {

    @Query(value = "select label_name from label_relation where username = ?1 and attention = ?2 order by label_name asc", nativeQuery = true)
    List<String> findLabelNameByUsernameAndSelectedNative(String username, Integer attention);

    @Query(value = "select sum(attention) as total from label_relation where label_name = ?1", nativeQuery = true)
    Map<String, Object> countAttentionNative(String labelName);

    @Query(value = "select attention from label_relation where username = ?1 and label_name = ?2", nativeQuery = true)
    Map<String, Object> findAttentionByUsernameAndLabelNameNative(String username, String labelName);

    @Modifying
    @Transactional
    @Query(value = "update label_relation set attention = ?3 where username = ?1 and label_name = ?2", nativeQuery = true)
    Integer updateByUsernameAndLabelNameAndAttentionNative(String username, String labelName, Integer attention);

    @Transactional
    @Modifying
    void deleteAllByUsername(String username);
}
