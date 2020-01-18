package com.es.blog.controller;

import com.es.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@RestController
@RequestMapping("/es/blog")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
}
