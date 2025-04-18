package com.inmyhand.refrigerator.recipe.domain.dto2;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeViewsEntity}
 */
@Data
public class RecipeViewsEntityDto implements Serializable {
    private Long id;
    private Date viewedAt;

}