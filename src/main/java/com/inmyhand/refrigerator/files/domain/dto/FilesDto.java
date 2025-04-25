package com.inmyhand.refrigerator.files.domain.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilesDto {

    private Long id;

    private String orgName;
    private String sysName;

    private String fileType;
    private String fileStatus;
    private String fileSize;
    private String fileUrl;

    private Date createdAt;

    private Long recipeId;     // RecipeInfoEntity -> ID
    private Long memberId;     // MemberEntity -> ID
    private Long stepId;       // RecipeStepsEntity -> ID
}
