package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final MemberEntity member;
    private final Map<String, Object> attributes;
    private final String accessToken;

    public CustomOAuth2User(MemberEntity member, Map<String, Object> attributes, String accessToken) {
        this.member = member;
        this.attributes = attributes;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (member.getMemberRoleList() == null || member.getMemberRoleList().isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_FREETIER")); // 디폴트 freetier
        }

        return member.getMemberRoleList().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getUserRole().name().toUpperCase()))
                .collect(Collectors.toList());
    }
    @Override
    public String getName() {
        return member.getEmail();  // 인증 식별자로 이메일 사용
    }
}