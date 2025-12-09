package com.revature.SongSearcher;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Fields
    private final BasicAuthInterceptor basicAuthInterceptor;
    private final JwtInterceptor jwtInterceptor;

    // Constructor
    public WebConfig(BasicAuthInterceptor bai,
                     JwtInterceptor jwt) {
        this.basicAuthInterceptor = bai;
        this.jwtInterceptor = jwt;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // Method
    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        // adding interceptors to the list of active/running interceptors
        // that are scanning requests as they come in
//        reg.addInterceptor(basicAuthInterceptor)
//                .addPathPatterns("/api/auth/**");
        reg.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**");
    }
}
