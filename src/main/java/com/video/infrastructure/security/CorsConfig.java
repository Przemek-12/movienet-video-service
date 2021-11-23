package com.video.infrastructure.security;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

//    @Value("${allowed.origins}")
//    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS"))
                .allowedOrigins("http://localhost:8005");
    }

}
