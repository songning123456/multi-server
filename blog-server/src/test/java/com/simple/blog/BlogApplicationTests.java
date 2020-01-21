package com.simple.blog;

import com.simple.blog.entity.Blog;
import com.sn.jpql.JpqlParser;
import com.sn.jpql.ParserParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Autowired
    private JpqlParser jpqlParser;

    //注入的是实体管理器,执行持久化操作
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 测试jpql是否可用
     */
    @Test
    public void testJpql() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("author", "凌晨");
        String sql = jpqlParser.parse(new ParserParameter("testJpql.customSQL", params)).getExecutableSql();
        List list = entityManager.createNativeQuery(sql, Blog.class).getResultList();
        System.out.println(list);
    }
}
