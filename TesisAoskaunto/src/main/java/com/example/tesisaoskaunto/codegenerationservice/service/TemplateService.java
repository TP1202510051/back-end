// src/main/java/com/example/tesisaoskaunto/codegenerationservice/service/TemplateService.java
package com.example.tesisaoskaunto.codegenerationservice.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

@Service
public class TemplateService {

    private final VelocityEngine velocityEngine;

    public TemplateService() {
        this.velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    public String processTemplate(String templatePath, Map<String, Object> context) {
        Template template = velocityEngine.getTemplate(templatePath);
        VelocityContext velocityContext = new VelocityContext(context);
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return writer.toString();
    }
}