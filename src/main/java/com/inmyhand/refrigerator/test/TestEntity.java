package com.inmyhand.refrigerator.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test1" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id
    private Long id;

    private String name;
    private String description;
}
