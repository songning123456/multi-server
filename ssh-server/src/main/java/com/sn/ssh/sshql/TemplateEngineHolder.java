package com.sn.ssh.sshql;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.directive.Directive;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sonin
 * @date 2020/10/05
 */
@Getter
@Slf4j
public class TemplateEngineHolder {
    private VelocityEngine velocityEngine;

    public TemplateEngineHolder(List<Directive> list) {
        VelocityEngine engine = new VelocityEngine();
        engine.addProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.addProperty("input.encoding", "UTF-8");
        engine.addProperty("output.encoding", "UTF-8");
        List<String> directives = list.stream().map(directive -> directive.getClass().getName()).collect(Collectors.toList());
        engine.addProperty("userdirective", String.join(",", directives));
        log.info("load userdirective:{} ", directives);
        this.velocityEngine = engine;
    }
}