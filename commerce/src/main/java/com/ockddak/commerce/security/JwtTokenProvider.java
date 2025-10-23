package com.ockddak.commerce.security;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT 토큰 생성, 유효성 검증 및 정보 추출을 담당하는 컴포넌트
 */
@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-ms}")
    private long expirationMs;

    private Key key;

    /**
     * Secret Key를 Base64 디코딩하여 암호화 키(Key)를 생성합니다.
     * Bean 초기화 시 딱 한 번 실행됩니다.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
