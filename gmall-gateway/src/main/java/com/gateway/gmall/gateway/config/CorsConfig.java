package com.gateway.gmall.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        //初始化分类对象
        CorsConfiguration config = new CorsConfiguration();

        //允许的域
        config.addAllowedOrigin("http://manager.gmall.com");
            config.addAllowedOrigin("http://www.gmall.com");
            config.addAllowedOrigin("http://gmall.com");
            //允许的头信息 全部
            config.addAllowedHeader("*");
            //允许的方法 全部
            config.addAllowedMethod("*");
            //是否允许携带Cookie信息 是
            config.setAllowCredentials(true);

        //对所有请求进行，拦截判断
        UrlBasedCorsConfigurationSource corsConfigurationSource =new UrlBasedCorsConfigurationSource();

        corsConfigurationSource.registerCorsConfiguration("/**",config);

        return new CorsWebFilter(corsConfigurationSource);
    }
}
