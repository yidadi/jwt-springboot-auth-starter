package com.even.jwt.config;

import com.even.jwt.handler.TokenHandler;
import com.even.jwt.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public TokenHandler tokenHandler(){
        return new TokenHandler();
    }

    @Bean   //把我们的拦截器注入为bean
    public HandlerInterceptor getMyInterceptor(){
        return new JwtInterceptor(tokenHandler());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
