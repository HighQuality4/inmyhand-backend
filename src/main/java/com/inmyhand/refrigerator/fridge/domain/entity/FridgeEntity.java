package com.inmyhand.refrigerator.fridge.domain.entity;

import jakarta.persistence.*;

//TODO
@Entity
@Table(name = "fridge_users")
public class FridgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
