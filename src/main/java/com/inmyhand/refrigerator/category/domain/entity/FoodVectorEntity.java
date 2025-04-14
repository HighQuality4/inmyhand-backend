package com.inmyhand.refrigerator.category.domain.entity;

import com.inmyhand.refrigerator.category.PGvectorToStringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "food_vector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodVectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "expiration_info", nullable = false)
    private int expirationInfo;

    @Column(name = "natural_text", nullable = false)
    private String naturalText;

    @Column(name = "embedding", columnDefinition = "vector")
    @Convert(converter = PGvectorToStringConverter.class)
    private String embedding;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}
