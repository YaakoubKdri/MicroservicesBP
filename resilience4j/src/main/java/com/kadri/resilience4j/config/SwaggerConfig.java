package com.kadri.resilience4j.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Resilience4j Demo")
                        .version("1.0.0")
                        .description("resilience4j API documentation"));
    }

    @Bean
    public GroupedOpenApi resilience4jApi(){
        return GroupedOpenApi.builder()
                .group("resilience4j")
                .packagesToScan("com.kadri.resilience4j.web")
                .pathsToMatch("/**").build();
    }
}
