package com.deopraglabs.api_prysme.utils.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Prysme")
                        .version("v1")
                        .description("API utilizada no Prysme CRM")
                        .termsOfService("")
                        .license(new License()
                                .name("MIT license")
                                .url("https://github.com/Deoprag/api_prysme/blob/main/LICENSE")
                        )
                );
    }
}
