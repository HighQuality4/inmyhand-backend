package com.inmyhand.refrigerator.common.redis.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularSearchDto {
    private String keyword;
    private Long count;
    private Long recipeId;  // 해당 레시피의 ID
    private String recipeName; // 레피시 이름

}
