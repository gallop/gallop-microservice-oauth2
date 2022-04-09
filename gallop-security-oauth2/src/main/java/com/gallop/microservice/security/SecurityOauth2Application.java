package com.gallop.microservice.security;

import com.gallop.microservice.security.enhancer.JwtTokenEnhancer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootApplication
public class SecurityOauth2Application {

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    JwtTokenEnhancer jwtTokenEnhancer;

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauth2Application.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            System.err.println("==================启动完成！===================");
            System.err.println("start--------------jwtTokenEnhancer="+jwtTokenEnhancer);
            System.err.println("start--------------jwtAccessTokenConverter"+jwtAccessTokenConverter);
        };
    }

}
