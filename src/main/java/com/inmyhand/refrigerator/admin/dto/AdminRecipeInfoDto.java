package com.inmyhand.refrigerator.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecipeInfoDto implements Serializable {

    @NotNull
    private Long id;

    @NotBlank
    private String recipeName;

    private Date createdAt;

    private int likeCount;

    private int viewCount;

}