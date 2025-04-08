package com.inmyhand.refrigerator.security.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    //    @Value("${jwt.secret}")
    private String secret = "ZHNhZmxrZGpzZmxrZGpzYWZsa2pkc2Zsa2RqZmxra2pkc2Zsa2pkc2Zsa2pzZGZsa3NkamZsa3NqZGZsa2oK";  // Base64로 인코딩된 시크릿 키

    //    @Value("${jwt.expiration}")
    private long expiration = 86400000L;  // 24시간(ms 단위)

    // 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 추가 클레임 설정 가능
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
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
