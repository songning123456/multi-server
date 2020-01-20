package com.sn.jpql;

import com.sn.jpql.directive.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.*;

/**
 * Jpql分析生成器
 *
 * @author gehoubao
 * @create 2020-01-18 10:36
 **/
@Slf4j
@Component
public class JpqlParser {
    @Autowired
    private JpqlScanner jpqlScanner;
    private TemplateEngineHolder engineHolder = new TemplateEngineHolder(Arrays.asList(
            new EmptyDirective(),
            new InsertDirective(),
            new NotEmptyDirective(),
            new ReplaceDirective(),
            new StripDirective(),
            new UpdateDirective()
    ));

    public ParsedJpql parse(ParserParameter parserParameter) {
        Jpql originalJpql = jpqlScanner.load(parserParameter.getId());
        VelocityContext velocityContext = new VelocityContext();

        for (Map.Entry<String, Object> entry : parserParameter.getParameter().entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        velocityContext.put("refs", jpqlScanner.getCachedJpql());
        StringWriter stringWriter = new StringWriter();
        engineHolder.getVelocityEngine().evaluate(velocityContext, stringWriter, "jpql", originalJpql.getJpql());

        ParsedJpql jpql = new ParsedJpql();
        BeanUtils.copyProperties(originalJpql, jpql);
        jpql.setParsed(stringWriter.toString());
        jpql.setParameterMap(parserParameter.getParameter());
        return jpql;
    }
}