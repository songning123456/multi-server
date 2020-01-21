package com.simple.blog.jpql;

import com.sn.jpql.JpqlParser;
import com.sn.jpql.ParserParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

/**
 * @author songning
 * @date 2020/1/21
 * description
 */
@Component
public class JpqlDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpqlParser jpqlParser;

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, Map<String, Object> params) {
        String updateSql = jpqlParser.parse(new ParserParameter(id, params)).getExecutableSql();
        entityManager.createNativeQuery(updateSql).executeUpdate();
    }
}
