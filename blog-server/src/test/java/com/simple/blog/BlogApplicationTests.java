package com.simple.blog;

import com.sn.jpql.JpqlParser;
import com.sn.jpql.ParserParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.HashMap;
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

    /**
     * 测试jpql是否可用
     */
    @Test
    public void testJpql() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("author", "凌晨");
        String sql = jpqlParser.parse(new ParserParameter("testJpql.customSQL", params)).getExecutableSql();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //定义Connection对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pst = conn.prepareStatement(sql);
            //执行sql语句
            ResultSet rs = pst.executeQuery();
            rs.next();
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
