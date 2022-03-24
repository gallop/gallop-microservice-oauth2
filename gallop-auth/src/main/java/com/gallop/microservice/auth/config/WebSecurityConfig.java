package com.gallop.microservice.auth.config;

import com.gallop.microservice.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@Order(-100)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/userlogin","/userlogout","/userjwt");

    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    //采用bcrypt对密码进行编码
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic() //clientId 的登入认证是通过basic方式登入认证，此项必须设置
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/key/**").permitAll()
                .antMatchers("/login/*").permitAll()
                .anyRequest().authenticated();

    }

}
