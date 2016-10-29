package com.github.hronom.words.counter.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .pathMapping("/")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.github.hronom.words.counter"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            "Words Counter REST API",
            "Calculate how often words appears in base texts and how often users asks for word.",
            "1.0.0",
            "http://www.apache.org/licenses/#2.0",
            new Contact("Eugene Tenkaev", "https://github.com/Hronom", "hronom@gmail.com"),
            "Apache License Version 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0");
    }
}
