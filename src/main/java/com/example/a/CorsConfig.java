package com.example.a;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static javax.swing.text.html.HTML.Tag.HEAD;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 通过WebMvcConfigurer配置CORS（推荐）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 匹配所有API路径
                .allowedOriginPatterns("*")  // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(false)  // 不允许携带凭证，避免跨域问题
                .allowedOriginPatterns("*")  // 允许所有来源（开发环境）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许的请求头
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")  // 暴露的响应头
                .allowCredentials(true)  // 允许携带凭证（如Cookie、认证信息）
                .maxAge(3600);  // 预检请求的缓存时间（秒）
    }
}