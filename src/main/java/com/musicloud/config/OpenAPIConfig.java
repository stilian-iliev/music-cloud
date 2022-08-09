package com.musicloud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MusiCloud API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Stilian Iliev")
                                .email("siliev333@gmail.com"))
                        .description("API for my personal project \"MusiCloud\""));
    }

}
