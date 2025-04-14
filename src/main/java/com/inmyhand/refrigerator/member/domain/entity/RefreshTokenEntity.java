package com.inmyhand.refrigerator.member.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity"})
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "token_value", unique = true, length = 100)
    private String tokenValue;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    @JsonIgnoreProperties("refreshTokenEntity")
    private MemberEntity memberEntity;
}

