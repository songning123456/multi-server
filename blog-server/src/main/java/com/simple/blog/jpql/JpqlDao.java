package com.simple.blog.jpql;

import com.sn.jpql.JpqlParser;
import com.sn.jpql.ParserParameter;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
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

    public List query(String id, Map<String, Object> params) {
        String querySql = jpqlParser.parse(new ParserParameter(id, params)).getExecutableSql();
        Query query = entityManager.createNativeQuery(querySql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public <T> List query(String id, Map<String, Object> params, Class<? extends T> clazz) {
        String querySql = jpqlParser.parse(new ParserParameter(id, params)).getExecutableSql();
        Query query = entityManager.createNativeQuery(querySql, clazz);
        return query.getResultList();
    }

    public long count(String id, Map<String, Object> params) {
        String countSql = jpqlParser.parse(new ParserParameter(id, params)).getExecutableSql();
        return ((BigInteger) entityManager.createNativeQuery(countSql).getSingleResult()).longValue();
    }
}
