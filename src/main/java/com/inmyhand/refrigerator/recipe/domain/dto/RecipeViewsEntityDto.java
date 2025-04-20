package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.Data;

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