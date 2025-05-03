package com.inmyhand.refrigerator.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentDTO {
    private String comment;
    private String recipeId;
    private String createdAt;
}
