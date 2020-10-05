package com.sn.ssh.sshql;

import com.sn.ssh.entity.Sshql;
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
 * @author sonin
 * @date 2020/10/2 17:16
 **/
@Slf4j
@Component
public class SshqlScanner {
    private Map<String, Object> cache = new HashMap<>(16);

    public SshqlScanner() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:sshql/**/*.xml");

        for (Resource resource : resources) {
            SAXReader saxReader = new SAXReader();
            try {
                saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                Document document = saxReader.read(resource.getInputStream());
                String module = document.getRootElement().attributeValue("module");
                List<Node> notes = document.selectNodes("/ssh/sshql");

                for (Node node : notes) {
                    Element element = (Element) node;
                    String id = element.attribute("id").getStringValue();
                    Sshql sshql = new Sshql();
                    sshql.setId(id);
                    sshql.setSshql(element.getTextTrim());
                    StringBuilder uid = new StringBuilder(StringUtils.isNotEmpty(module) ? module.concat(".") : "").append(id);
                    if (!cache.containsKey(uid.toString())) {
                        cache.put(uid.toString(), sshql);
                    } else {
                        log.error("duplicate id {} found in {}", id, resource.getURI());
                    }
                }
                log.info("Sshql file loaded: {}", resource.getURI());
            } catch (Exception e) {
                log.error("error to load Sshql file {}", resource.getURI());
            }

        }
    }

    Map getCachedSshql() {
        Map<String, String> map = new HashMap<>(16);
        for (String key : cache.keySet()) {
            map.put(key, ((Sshql) cache.get(key)).getSshql());
        }
        return Collections.unmodifiableMap(map);
    }

    Sshql load(String id) {
        return (Sshql) cache.get(id);
    }
}