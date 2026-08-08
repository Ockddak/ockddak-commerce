package com.ockddak.commerce.service;

import com.ockddak.commerce.domain.user.Role;
import com.ockddak.commerce.domain.user.User;
import com.ockddak.commerce.dto.AuthRequest.LoginRequest;
import com.ockddak.commerce.dto.AuthRequest.SignUpRequest;
import com.ockddak.commerce.dto.AuthRequest.TokenResponse;
import com.ockddak.commerce.repository.UserRepository;
import com.ockddak.commerce.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 회원가입 로직
     * 이메일 중복 체크 후, 비밀번호를 해시 처리하여 사용자 정보를 저장합니다.
     * @param request 회원가입 요청 DTO
     * @return 저장된 사용자 엔티티
     *
     * TODO: 이메일 중복 확인에서 더 구체적인 HTTP 상태 코드(409 Conflict)를 반환하도록 추후 예외 핸들링을 구현 필요.
     */
    @Transactional
    public User signUp(SignUpRequest request) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 엔티티 생성 및 저장
        User newUser = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.ROLE_USER) // 기본 권한은 ROLE_USER로 설정
                .build();

        return userRepository.save(newUser);
    }

    /**
     * 로그인 로직
     * 이메일과 비밀번호 검증 후, 인증이 성공하면 JWT Access Token을 발급합니다.
     * @param request 로그인 요청 DTO
     * @return JWT 토큰을 담은 응답 DTO
     */
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Spring Security의 BadCredentialsException을 사용하여 일관된 에러 처리
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(user);

        return new TokenResponse(accessToken);
    }

}
