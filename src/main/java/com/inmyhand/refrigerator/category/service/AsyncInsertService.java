package com.inmyhand.refrigerator.category.service;

import com.inmyhand.refrigerator.category.repository.FoodVectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AsyncInsertService {

    private final FoodVectorRepository foodVectorRepository;

    @Async
    public void insertVector(String category, String naturalText, int expirationInfo, Float[] embedding) {
        String vectorStr = "[" + Arrays.stream(embedding)
                .map(f -> String.format(Locale.US, "%.8f", f))
                .collect(Collectors.joining(",")) + "]";

        foodVectorRepository.insertFoodVector(category, naturalText, expirationInfo, vectorStr);
    }
}

