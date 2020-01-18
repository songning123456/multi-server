package com.es.blog.service.impl;

import com.es.blog.repository.ArticleRepository;
import com.es.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
}
