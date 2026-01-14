package com.projeto.modelo.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Bean
    public org.springframework.boot.web.servlet.FilterRegistrationBean<SimpleCorsFilter> corsFilter() {
        org.springframework.boot.web.servlet.FilterRegistrationBean<SimpleCorsFilter> registrationBean = new org.springframework.boot.web.servlet.FilterRegistrationBean<>();
        registrationBean.setFilter(new SimpleCorsFilter());
        registrationBean.setOrder(org.springframework.core.Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}