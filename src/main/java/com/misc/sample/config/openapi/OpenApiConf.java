package com.misc.sample.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConf {

    @Bean
    public OpenAPI customOpenAPI() {
        String applicationTitle = "TEMPORAL PLAYGROUND API";

        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(applicationTitle)
                        .description("This is a documentation of " + applicationTitle + " with OpenAPI 3.0.0"));
    }
}
