package com.wonjung.budget.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(corsConfig.corsFilter())

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                .authorizeHttpRequests(manager -> manager
//                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()     // 로그인 API
//                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll()   // 회원가입 API
//                        .requestMatchers("/favicon.ico", "/error").permitAll()
//                        .anyRequest().authenticated()   // 그 외에는 무조건 인증 필요
//                )
        ;

        return httpSecurity.build();
    }
}
