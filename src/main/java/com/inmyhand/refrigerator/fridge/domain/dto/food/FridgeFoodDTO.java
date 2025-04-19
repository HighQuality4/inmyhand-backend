package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeFoodDTO {
    private Long id;
    private String foodName;
    private Long foodAmount;
    private Date endDate;
    private Date chargeDate;
    private Date saveDate;
    private Long foodCategoryId;
    private Long fridgeId;

    // Reflection을 이용하여 자동으로 Map으로 변환
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields(); // 클래스의 모든 필드 가져오기

        for (Field field : fields) {
            field.setAccessible(true); // private 필드도 접근 가능하게 설정
            try {
                map.put(field.getName(), field.get(this));  // 필드명과 해당 값을 map에 넣기
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
