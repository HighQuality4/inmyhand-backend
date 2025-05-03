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
public class MyCommentDTO {
    private String nickname;
    private String recipeName;
    private String createdAt;

    public MyCommentDTO(String nickname, String recipeName, Date createdAt) {
        this.nickname = nickname;
        this.recipeName = recipeName;
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd").format(createdAt);
    }
}
