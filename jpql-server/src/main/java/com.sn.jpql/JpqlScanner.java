package com.sn.jpql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql扫描器
 *
 * @author gehoubao
 * @create 2020-01-18 9:58
 **/
@Slf4j
@Component
public class JpqlScanner {
    private Map<String, Object> cache = new HashMap<>(16);

    public JpqlScanner() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:jpql/**/*.xml");

        for (Resource resource : resources) {
            SAXReader saxReader = new SAXReader();
            try {
                saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                Document document = saxReader.read(resource.getInputStream());
                String module = document.getRootElement().attributeValue("module");
                List<Node> notes = document.selectNodes("/jpa/jpql");

                for (Node node : notes) {
                    Element element = (Element) node;
                    String id = element.attribute("id").getStringValue();
                    Jpql jpql = new Jpql();
                    jpql.setId(id);
                    jpql.setJpql(element.getTextTrim());
                    StringBuilder uid = new StringBuilder(StringUtils.isNotEmpty(module) ? module.concat(".") : "").append(id);
                    if (!cache.containsKey(uid.toString())) {
                        cache.put(uid.toString(), jpql);
                    } else {
                        log.error("duplicate id {} found in {}", id, resource.getURI());
                    }
                }
                log.info("Jpql file loaded: {}", resource.getURI());
            } catch (Exception e) {
                log.error("error to load Jpql file {}", resource.getURI());
            }

        }
    }

    Map getCachedJpql() {
        Map<String, String> map = new HashMap<>(16);
        for (String key : cache.keySet()) {
            map.put(key, ((Jpql) cache.get(key)).getJpql());
        }
        return Collections.unmodifiableMap(map);
    }

    Jpql load(String id) {
        return (Jpql) cache.get(id);
    }
}