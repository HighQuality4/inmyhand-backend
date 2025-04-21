package com.inmyhand.refrigerator.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DtoMapperUtils {

    // 1. 단일 DTO -> Map<String, Object>
    public static Map<String, Object> toMap(Object dto) {
        Map<String, Object> result = new HashMap<>();
        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                result.put(field.getName(), field.get(dto));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 접근 오류", e);
            }
        }
        return result;
    }

    // 2. List<DTO> -> List<Map<String, Object>> ->  Map<String, Object>
    // 2-1. List<DTO> -> List<Map<String, Object>>
    public static <T> List<Map<String, Object>> toMapList(List<T> dtoList) {
        return dtoList.stream()
                .map(DtoMapperUtils::toMap)
                .collect(Collectors.toList());
    }

    // 2-2. 최종 Map 구조 반환 (키 지정)
    // List<Map<String, Object>> ->  Map<String, Object>
    public static <T> Map<String, Object> wrapInMap(String key, List<T> dtoList) {
        Map<String, Object> result = new HashMap<>();
        result.put(key, toMapList(dtoList));
        return result;
    }


}
