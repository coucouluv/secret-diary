package com.project.secretdiary.config;

import com.project.secretdiary.jwt.JwtTokenFilter;
import com.project.secretdiary.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, redisTemplate);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}