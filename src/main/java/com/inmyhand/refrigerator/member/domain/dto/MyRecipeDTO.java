package com.inmyhand.refrigerator.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyRecipeDTO {
    private String nickname;
    private String recipeName;
    private String createdAt;

    public MyRecipeDTO(String nickname, String recipeName, String createdAt) {
        this.nickname = nickname;
        this.recipeName = recipeName;
        this.createdAt = createdAt;
    }
}
