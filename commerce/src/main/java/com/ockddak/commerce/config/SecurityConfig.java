package com.ockddak.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 기본 설정 및 PasswordEncoder 빈 등록 클래스
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    /**
     * 비밀번호를 안전하게 해시하기 위한 PasswordEncoder 빈을 등록합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 알고리즘은 Spring Security에서 권장하는 암호화 방식입니다.
        return new BCryptPasswordEncoder();
    }

    /**
     * Security Filter Chain을 구성합니다.
     * 현재는 JWT 필터가 없으므로 인증이 필요한 요청에 대해 403 Forbidden을 반환하는 기본 설정만 합니다.
     * 다음 단계에서 JWT 관련 설정 및 필터를 추가하여 완성할 예정입니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 공격 방지 기능 비활성화 (REST API이므로 토큰 방식 사용)
                .csrf(AbstractHttpConfigurer::disable)
                // API 경로별 인가 설정
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입 및 로그인 경로는 모두 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
