package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fridge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"fridgeUserList", "fridgeFoodList"})
public class FridgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id")
    private Long id;

    private String fridgeName;

    private Boolean favoriteState;

    @OneToMany(mappedBy = "fridgeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("fridgeEntity")
    private List<FridgeMemberEntity> fridgeUserList = new ArrayList<>();

    @OneToMany(mappedBy = "fridgeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("fridgeEntity")
    private List<FridgeFoodEntity> fridgeFoodList = new ArrayList<>();
}
