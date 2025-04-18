package com.inmyhand.refrigerator.recipe.domain.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity}
 */
@Data
public class RecipeLikesEntityDto implements Serializable {

    private Long id;
    private Date likedAt;

    @NotBlank
    private Long likeMemberId;

}