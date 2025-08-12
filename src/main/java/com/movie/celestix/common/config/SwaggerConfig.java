package com.movie.celestix.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("celestix-public")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public OpenAPI lmsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Celestix API")
                        .description("Learning Management System API documentation")
                        .version("v1.0.0"));
    }
}