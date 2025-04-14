package com.inmyhand.refrigerator.category.repository;

import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoodVectorRepositoryImpl implements FoodVectorRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<FoodVectorRequestDTO> findMostSimilarCategoryByVector(String inputText, String vector) {

        String query = """
        SELECT id, category_name, natural_text, embedding,
        (embedding <=> CAST(%s AS vector)) AS distance
        FROM food_vector
        ORDER BY embedding <=> CAST(%s AS vector)
        LIMIT 1
    """.formatted(vector, vector);

        Object[] result = (Object[]) entityManager.createNativeQuery(query).getSingleResult();

        PGobject embeddingObj = (PGobject) result[3];
        String embeddingStr = embeddingObj.getValue();
        Double distance = Double.parseDouble(result[4].toString());

        return Optional.ofNullable(FoodVectorRequestDTO.builder()
                .inputText(inputText)  // 사용자가 넣은 원래 텍스트
                .categoryName((String) result[1])
                .naturalText((String) result[2])
                .distance(Math.round(Double.parseDouble(result[4].toString()) * 10000.0) / 10000.0)  // 소수 4자리로 반올림
                .build());
    }

    @Override
    @Transactional
    public void insertFoodVector(String categoryName, String naturalText, int expirationInfo, String embeddingStr) {
        String sql = """
            INSERT INTO food_vector (category_name, natural_text, expiration_info, embedding)
            VALUES (:categoryName, :naturalText, :expirationInfo, CAST(:embedding AS vector))
        """;

        jakarta.persistence.Query query = entityManager.createNativeQuery(sql)
                .setParameter("categoryName", categoryName)
                .setParameter("naturalText", categoryName)
                .setParameter("expirationInfo", expirationInfo)
                .setParameter("embedding", embeddingStr);

        query.executeUpdate();
    }

}
