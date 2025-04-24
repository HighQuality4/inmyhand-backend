package com.inmyhand.refrigerator.security.jwt;


import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    //    @Value("${jwt.secret}")
    private String secret = "ZHNhZmxrZGpzZmxrZGpzYWZsa2pkc2Zsa2RqZmxra2pkc2Zsa2pkc2Zsa2pzZGZsa3NkamZsa3NqZGZsa2oK";  // Base64로 인코딩된 시크릿 키

    //    @Value("${jwt.expiration}")
    private final long access_expiration = 3600000L;  // 1시간(ms 단위)

    private final long refresh_expiration = 604800000L; // 7일

    // 토큰 생성
    public String generateAccessToken(MemberEntity member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", member.getId());
        claims.put("nickname", member.getNickname());
        // 추가 클레임 설정 가능
        return createToken(claims, member.getEmail(), access_expiration);
    }

    public String generateRefreshToken(MemberEntity member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", member.getId());
        claims.put("nickname", member.getNickname());
        return createToken(claims, member.getEmail(), refresh_expiration);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)  // setClaims() -> claims()로 변경
                .subject(subject)  // setSubject() -> subject()로 변경
                .issuedAt(now)  // setIssuedAt() -> issuedAt()로 변경
                .expiration(expiryDate)  // setExpiration() -> expiration()로 변경
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // setSigningKey() -> verifyWith()로 변경
                .build()
                .parseSignedClaims(token)  // parseClaimsJws() -> parseSignedClaims()로 변경
                .getPayload()  // getBody() -> getPayload()로 변경
                .getSubject();
    }

    // 토큰에서 사용자id(번호) 추출
    public Long getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    // 토큰에서 남은 시간 정보 추출
    public Long getRemainingTimeFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Math.max((claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000, 0L);
    }


    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())  // setSigningKey() -> verifyWith()로 변경
                    .build()
                    .parseSignedClaims(token);  // parseClaimsJws() -> parseSignedClaims()로 변경
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 서명 키 생성 메소드
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
