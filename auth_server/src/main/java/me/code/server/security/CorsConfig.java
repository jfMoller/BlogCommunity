package me.code.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.eris.notnull.NotNull;

@Configuration
public class CorsConfig {

    private final static String FRONTEND_ORIGIN_URL = "http://localhost:5173";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(FRONTEND_ORIGIN_URL)
                        .allowedMethods("GET");
            }
        };
    }
}