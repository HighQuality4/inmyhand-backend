package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCommentDTO {
    private String nickname;
    private String commentContents;
    private Date createdAt;
}