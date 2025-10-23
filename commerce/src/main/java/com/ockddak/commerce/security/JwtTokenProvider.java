package com.ockddak.commerce.security;

import com.ockddak.commerce.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

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

    /**
     * Access Token을 생성합니다.
     * @param user 인증된 사용자 객체
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail()) // 토큰의 제목(Subject)으로 이메일 사용 (고유 식별자)
                .claim("userId", user.getId()) // 사용자 ID를 Custom Claim으로 저장
                .claim("role", user.getRole().name()) // 사용자 권한을 Custom Claim으로 저장
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 키
                .compact();
    }

    /**
     * JWT 토큰에서 Claims(정보)를 추출합니다.
     * @param token JWT 토큰 문자열
     * @return Claims 객체
     */
    public Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     * @param authToken JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
