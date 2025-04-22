package com.inmyhand.refrigerator.category.repository;

import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoodVectorRepositoryImpl implements FoodVectorRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<FoodVectorRequestDTO> findMostSimilarCategoryByVector(String inputText, String vector) {

        String query = """
        SELECT id, category_name, natural_text, embedding, expiration_info,
        (embedding <=> CAST(%s AS vector)) AS distance
        FROM food_vector
        ORDER BY embedding <=> CAST(%s AS vector)
        LIMIT 1
    """.formatted(vector, vector);

        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(query).getSingleResult();

            PGobject embeddingObj = (PGobject) result[3]; // 이건 임베딩 값
            String embeddingStr = embeddingObj.getValue();
            System.out.println(embeddingStr);
            // select문에서 선언한 컬럼 순서대로 나옴!!!!!
            return Optional.of(FoodVectorRequestDTO.builder()
                    .inputText(inputText)
                    .categoryName((String) result[1])
                    .naturalText((String) result[2])
                    .expirationInfo(((Number) result[4]).intValue()) // PGObject -> Int로 변환
                    .distance(Math.round(Double.parseDouble(result[5].toString()) * 10000.0) / 10000.0)
                    .build());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void insertFoodVector(String categoryName, String naturalText, int expirationInfo, String embeddingStr) {
        String sql = """
            INSERT INTO food_vector (category_name, natural_text, expiration_info, embedding)
            VALUES (:categoryName, :naturalText, :expirationInfo, CAST(:embeddingStr AS vector))
        """;

        jakarta.persistence.Query query = entityManager.createNativeQuery(sql)
                .setParameter("categoryName", categoryName)
                .setParameter("naturalText", naturalText)
                .setParameter("expirationInfo", expirationInfo)
                .setParameter("embeddingStr", embeddingStr);

        query.executeUpdate();
    }

    @Override
    public List<FoodVectorRequestDTO> findByCategoryNameContaining(String keyword) {
        String query = """
        SELECT id, category_name, natural_text, embedding, expiration_info
        FROM food_vector
        WHERE category_name ILIKE :keyword
        ORDER BY category_name ASC
        LIMIT 10
    """;

        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        List<FoodVectorRequestDTO> dtoList = new ArrayList<>();

        for (Object[] result : results) {
            Long id = ((Number) result[0]).longValue();
            String categoryName = (String) result[1];
            String naturalText = (String) result[2];
            PGobject embeddingObj = (PGobject) result[3];
            String embeddingStr = embeddingObj.getValue();
            int expirationInfo = ((Number) result[4]).intValue();

            dtoList.add(FoodVectorRequestDTO.builder()
                    .id(id)
                    .inputText(null)
                    .categoryName(categoryName)
                    .naturalText(naturalText)
                    .expirationInfo(expirationInfo)
                    .distance(null)
                    .build());
        }

        return dtoList;
    }

}
