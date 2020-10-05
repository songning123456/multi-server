package com.sn.ssh.sshql;

import com.sn.ssh.sshql.directive.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * @author sonin
 * @date 2020/10/05
 */
@Slf4j
@Component
public class SshqlParser {

    @Autowired
    private SshqlScanner sshqlScanner;

    private TemplateEngineHolder templateEngineHolder = new TemplateEngineHolder(Arrays.asList(
            new EmptyDirective(),
            new InsertDirective(),
            new NotEmptyDirective(),
            new ReplaceDirective(),
            new StripDirective(),
            new UpdateDirective()
    ));

    private SpelExpressionParser expressionParser = new SpelExpressionParser();

    public ParsedSshql parse(ParserParameter parserParameter) {
        Sshql originalSshql = sshqlScanner.load(parserParameter.getId());
        VelocityContext velocityContext = new VelocityContext();

        for (Map.Entry<String, Object> entry : parserParameter.getParameter().entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        velocityContext.put("refs", sshqlScanner.getCachedSshql());
        StringWriter stringWriter = new StringWriter();
        templateEngineHolder.getVelocityEngine().evaluate(velocityContext, stringWriter, "sshql", originalSshql.getSshql());

        ParsedSshql sshql = new ParsedSshql();
        BeanUtils.copyProperties(originalSshql, sshql);
        sshql.setParsed(stringWriter.getBuffer().toString());
        Pattern pattern = Pattern.compile(":(" + "[\\p{Lu}\\P{InBASIC_LATIN}\\p{Alnum}._%\\[\\]$]+)", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sshql.getParsed());
        int i = 0;
        String parsedSshql = sshql.getParsed();
        HashMap<String, Object> map = new HashMap<>(16);
        List<String> params = new ArrayList<>();
        while (matcher.find()) {
            String name = matcher.group(0);
            //修复字符串常量里面的冒号解析问题，去除引号里冒号被解析的问题
            int start = matcher.start(0);
            boolean inStr = false;
            for (int j = 0; j < start; j++) {
                char c = parsedSshql.charAt(j);
                if (c == '\'') {
                    inStr = !inStr;
                }
            }
            if (!params.contains(name) && !inStr) {
                params.add(name);
            }
        }
        Map<String, String> paramMapping = new HashMap<>(16);
        for (String param : params) {
            String test = StringUtils.strip(param, ":");
            Object o = getParameter(parserParameter.getParameter(), StringUtils.strip(test, "%"));
            String key = "argument" + (i++);
            StringBuilder value = new StringBuilder();
            prepareForLike(map, test, o, key, value);
            paramMapping.put(param, key);
        }
        sshql.setParsed(replaceParameter(sshql.getParsed(), paramMapping, pattern));
        sshql.setParameterMap(map);
        return sshql;
    }

    private String replaceParameter(String parsed, Map<String, String> mapping, Pattern pattern) {

        Matcher matcher = pattern.matcher(parsed);
        StringBuilder stringBuilder = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            String name = matcher.group(0);
            if (lastEnd >= 0) {
                stringBuilder.append(parsed, lastEnd, matcher.start(0));
            }
            if (mapping.get(name) != null) {
                stringBuilder.append(":".concat(mapping.get(name)));
            } else {
                stringBuilder.append(name);
            }
            lastEnd = matcher.end(0);
        }
        if (lastEnd > 0) {
            stringBuilder.append(parsed.substring(lastEnd));
        }
        return stringBuilder.length() > 0 ? stringBuilder.toString() : parsed;

    }


    private void prepareForLike(HashMap<String, Object> map, String test, Object o, String key, StringBuilder value) {
        boolean start = test.startsWith("%");
        boolean end = test.endsWith("%");
        if (start) {
            value.append("%");
        }
        if (o != null) {
            value.append(o.toString());
        }
        if (end) {
            value.append("%");
        }
        map.put(key, start || end ? value.toString() : o);
    }

    /**
     * 通过Spring EL解析表达式
     *
     * @param parameter
     * @param name
     * @return
     */
    private Object getParameter(Map<String, Object> parameter, String name) {
        EvaluationContext context = new StandardEvaluationContext();
        for (String key : parameter.keySet()) {
            context.setVariable(key, parameter.get(key));
        }
        Expression expression = expressionParser.parseExpression("#" + name);
        return expression.getValue(context);
    }
}
