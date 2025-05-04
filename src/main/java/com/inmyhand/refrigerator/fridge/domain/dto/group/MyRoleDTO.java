package com.inmyhand.refrigerator.fridge.domain.dto.group;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyRoleDTO {
    private String editor;
    private String writer;

    // (1) 불리언을 받아 문자열로 저장해 주는 생성자 추가
    public MyRoleDTO(boolean isEditor, boolean isWriter) {
        this.editor = Boolean.toString(isEditor);
        this.writer = Boolean.toString(isWriter);
    }

}

