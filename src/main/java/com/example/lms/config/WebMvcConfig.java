package com.example.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // /upload/**  →  실제 C:/lmsUpload/ 아래 파일들
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/lmsUpload/");
    }
}
