package com.example.doctorcare.infrastructure.common.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DoctorCare API")
                        .version("2.1.0")
                        .description("API Documentation for DoctorCare Spring Boot Application"));
    }
}
