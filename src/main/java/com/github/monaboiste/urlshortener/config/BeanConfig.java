package com.github.monaboiste.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class BeanConfig {

    @Bean
    SecureRandom random() {
        return new SecureRandom();
    }
}
