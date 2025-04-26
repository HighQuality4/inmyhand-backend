package com.inmyhand.refrigerator.files.domain.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 요청을 위한 DTO.
 * Multipart 요청 시 @ModelAttribute 로 바인딩하거나,
 * @RequestPart 로 파일과 파라미터를 함께 받을 때 사용합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRequest {
    /**
     * 업로드할 파일
     */
    private MultipartFile file;

    /**
     * 회원용 파일인 경우 전달
     */
    private Long memberId;

    /**
     * 레시피용 파일인 경우 전달
     */
    private Long recipeId;

    /**
     * 레시피 단계용 파일인 경우 전달
     */
    private Long stepId;
}
