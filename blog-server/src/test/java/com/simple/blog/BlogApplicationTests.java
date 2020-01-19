package com.simple.blog;


import com.sleepy.jpql.JpqlParser;
import com.sleepy.jpql.ParserParameter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {
    @Autowired
    private JpqlParser jpqlParser;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Session getSession() {
        return entityManagerFactory.unwrap(SessionFactory.class).openSession();
    }

    @Test
    public void test1() {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("tagNames", Arrays.asList("平台", "后台"));
        String sql = jpqlParser.parse(new ParserParameter("testJpql.customSQL", parameters, "mysql")).getExecutableSql();
        System.out.println();
    }
}
