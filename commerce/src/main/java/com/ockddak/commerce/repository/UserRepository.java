package com.ockddak.commerce.repository;

import com.ockddak.commerce.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * User 엔티티에 대한 데이터 접근을 담당하는 JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일을 통해 User 엔티티를 조회합니다.
     * 로그인 과정에서 사용자의 존재 여부를 확인하고 인증 정보를 가져오는 핵심 메서드입니다.
     * @param email 사용자 이메일
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);
}
