package com.inmyhand.refrigerator.recipe.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCommentEntityDto implements Serializable {

    private Long id;

    private String userProfileImageUrl;
    private String nickname;

    @NotBlank
    private String commentContents;

    private Date createdAt;
}