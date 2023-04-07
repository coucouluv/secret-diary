package com.project.secretdiary.config;

import com.project.secretdiary.controller.AuthenticationArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationArgumentResolver authenticationArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://d3vyn6lxoxdh55.cloudfront.net",
                        "https://d3vyn6lxoxdh55.cloudfront.net")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "HEAD" ,"PUT", "PATCH", "DELETE", "OPTIONS", "TRACE")
                .allowCredentials(true);

    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}