package br.com.nexalty.template_rest_profissional.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContextProperties {

    private static Environment environment;
    private final ResourceLoader resourceLoader;
    @Autowired
    public ContextProperties(Environment environment, ResourceLoader resourceLoader) {
        ContextProperties.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    public static <T> T get(String key) {
        return (T) environment.getProperty(key);
    }

    @PostConstruct
    public void init() {
        Resource resource = resourceLoader.getResource("classpath:/application-commons.yml");
        log.info("[ContextProperties] - Loaded Commons property source: {}, exists: {}",  StringUtils.defaultIfBlank(get("commons.loaded"), "false"), resource.exists());
    }
}
