package com.inmyhand.refrigerator.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String nickname;
    private final String email;
    private final List<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요시 실제 만료 로직 추가 가능
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요시 잠금 로직 추가 가능
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 필요시 비밀번호 만료 로직 추가 가능
    }

    @Override
    public boolean isEnabled() {
        return true; // 필요시 계정 활성화 여부 추가 가능
    }
}