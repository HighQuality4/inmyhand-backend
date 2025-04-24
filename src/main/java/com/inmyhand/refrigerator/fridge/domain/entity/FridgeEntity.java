package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fridge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"fridgeMemberList", "fridgeFoodList"})
public class FridgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id")
    private Long id;

    private String fridgeName;


    @OneToMany(mappedBy = "fridgeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("fridgeEntity")
    @BatchSize(size = 10)
    private List<FridgeMemberEntity> fridgeMemberList = new ArrayList<>();


    @OneToMany(mappedBy = "fridgeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("fridgeEntity")
    @BatchSize(size = 10)
    private List<FridgeFoodEntity> fridgeFoodList = new ArrayList<>();
}
