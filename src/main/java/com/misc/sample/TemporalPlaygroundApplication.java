package com.misc.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;


@Slf4j
@SpringBootApplication(scanBasePackages = {"com.misc.sample"})
public class TemporalPlaygroundApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplicationBuilder().sources(TemporalPlaygroundApplication.class).build();
        Properties properties = new Properties();
        properties.put("spring.main.allow-bean-definition-overriding", true);
        application.setDefaultProperties(properties);
        ConfigurableApplicationContext context = application.run(args);
        logApplicationStartup(context);
    }


    private static void logApplicationStartup(
            ConfigurableApplicationContext context) {

        Environment env = context.getEnvironment();

        String protocol = env.getProperty(
                "server.ssl.enabled", Boolean.class, false
        ) ? "https" : "http";

        int port = ((ServletWebServerApplicationContext) context)
                .getWebServer()
                .getPort();

        String contextPath = env.getProperty(
                "server.servlet.context-path", ""
        );

        String swaggerPath = env.getProperty(
                "springdoc.swagger-ui.path",
                "/swagger-ui.html"
        );

        String apiDocsPath = env.getProperty(
                "springdoc.api-docs.path",
                "/v3/api-docs"
        );

        String appName = env.getProperty(
                "spring.application.name",
                "Collateral"
        );

        String hostAddress = "localhost";

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Could not determine host IP address", e);
        }

        String basePath = normalizePath(contextPath);
        String swaggerUrl = basePath + normalizePath(swaggerPath);
        String apiDocsUrl = basePath + normalizePath(apiDocsPath);

        String localBase = protocol + "://localhost:" + port;
        String externalBase = protocol + "://" + hostAddress + ":" + port;

        log.info("""
                
                ----------------------------------------------------------
                Application '{}' is running!
                
                Swagger UI:
                  Local:     {}{}
                  External:  {}{}
                
                API Docs:
                  Local:     {}{}
                  External:  {}{}
                
                ----------------------------------------------------------
                """,
                appName,
                localBase, swaggerUrl,
                externalBase, swaggerUrl,
                localBase, apiDocsUrl,
                externalBase, apiDocsUrl
        );
    }

    private static String normalizePath(String path) {
        if (!StringUtils.hasText(path) || "/".equals(path)) {
            return "";
        }

        return path.startsWith("/") ? path : "/" + path;
    }
}
