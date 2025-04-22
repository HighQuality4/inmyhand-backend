package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginRepository loginRepository;

    public CustomUserDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // 실제 환경에서는 UserRepository를 주입받아 DB에서 사용자 정보를 조회해야 함
    // private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. 실제 구현에서는 DB에서 사용자 정보를 조회
        MemberEntity memberEntity = loginRepository.findByEmail(email)
             .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        
        // 2. 테스트 목적으로 임시 사용자 정보 반환 (실제 구현 시 수정 필요)
        return User.builder()
                .username(email)
                .password(memberEntity.getPassword()) // {noop}은 비밀번호 인코딩 없음을 의미
                .roles("USER")
                .build();
    }
}