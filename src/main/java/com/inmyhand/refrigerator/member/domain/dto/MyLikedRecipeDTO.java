package com.inmyhand.refrigerator.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyLikedRecipeDTO {
    private String recipeId;
    private String nickname;
    private String recipeName;
    private String likedAt;

    public MyLikedRecipeDTO(Long recipeId, String nickname, String recipeName, Date likedAt) {
        this.recipeId = recipeId.toString();
        this.nickname = nickname;
        this.recipeName = recipeName;
        this.likedAt = new SimpleDateFormat("yyyy-MM-dd").format(likedAt);
    }
}
