package com.simple.blog.repository;

import com.simple.blog.entity.LabelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author songning
 * @date 2019/10/8
 * description
 */
@Repository
public interface LabelConfigRepository extends JpaRepository<LabelConfig, String> {

    @Query(value = "select * from label_config where label_name like %?1% order By label_name asc", nativeQuery = true)
    List<LabelConfig> findAllByLabelNameLikeNative(String labelName);

    @Query(value = "select label_name from label_config", nativeQuery = true)
    List<String> findAllLabelNameNative();
}
