package com.example.ezback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("EZ API")
                        .description("EZ 서비스 API 문서")
                        .version("v1.0.0")
                        .contact(new Contact().name("EZ Team"))
                        .license(new License().name("Apache 2.0"))
                );
    }
}
