package com.bestbranch.geulboxapi.common.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bestbranch.geulboxapi"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("글을 담다 API")
                .description("베타: http://133.186.240.191, 운영: http://geulbox.com")
                .version("1.4")
                .build();
    }

//    @Primary
//    @Bean
//    public SwaggerResourcesProvider swaggerResourcesProvider() {
//        return () -> {
//            SwaggerResource resource = new SwaggerResource();
//            resource.setName("Documentation");
//            resource.setSwaggerVersion("2.0");
//            resource.setLocation("/swagger/" + profile + "/api_doc.yml");
//            List<SwaggerResource> resources = Collections.singletonList(resource);
//            return resources;
//        };
//    }
}