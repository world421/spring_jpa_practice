package com.study.jpa;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration() // 설정을 담당하는 클래스
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Spring boot Board API Example 입니다 히히 ")
                        .description("Spring boot Board API 예시 프로젝트입니다.")
                        .version("v1.0.0")
                );


    }
}
