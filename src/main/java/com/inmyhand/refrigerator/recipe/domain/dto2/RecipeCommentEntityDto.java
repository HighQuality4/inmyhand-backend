package com.inmyhand.refrigerator.recipe.domain.dto2;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity}
 */
@Data
public class RecipeCommentEntityDto implements Serializable {

    private Long id;

    @NotBlank
    private String commentContents;

    private Date createdAt;

    private String nickname;


}