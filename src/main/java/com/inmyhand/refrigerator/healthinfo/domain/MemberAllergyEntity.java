package com.inmyhand.refrigerator.healthinfo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_allergy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity"})
public class MemberAllergyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Long id;

    @Column(name = "allergy", length = 100)
    private String allergy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties("memberAllergyList")
    private MemberEntity memberEntity;
}

