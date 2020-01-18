package com.es.blog.repository;

import com.es.blog.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
}
