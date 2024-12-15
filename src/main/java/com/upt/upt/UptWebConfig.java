package com.upt.upt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up web configurations such as CORS.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Configuration
public class UptWebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow CORS for all paths
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false); // If true, cookies are allowed
            }
        };
    }
}

