package com.example.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "httt://localhost:8080/h2-console/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Refresh", "Content-Type")
                .maxAge(3600);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}