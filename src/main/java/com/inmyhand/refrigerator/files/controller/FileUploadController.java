package com.inmyhand.refrigerator.files.controller;

import com.inmyhand.refrigerator.files.domain.entity.FileUploadRequest;
import com.inmyhand.refrigerator.files.domain.entity.FileUploadResponse;
import com.inmyhand.refrigerator.files.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 이미지 파일 업로드 처리
     * @param file 업로드할 이미지 파일
     * @param memberId 회원 ID (선택적)
     * @param recipeId 레시피 ID (선택적)
     * @param stepId 레시피 단계 ID (선택적)
     * @return 업로드된 이미지에 대한 정보 (URL 등)
     */
    @PostMapping("/upload-image")
    public FileUploadResponse uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "recipeId", required = false) Long recipeId,
            @RequestParam(value = "stepId", required = false) Long stepId) {

        // 이미지 파일 업로드 서비스 호출
        FileUploadRequest request = new FileUploadRequest(file, memberId, recipeId, stepId);
        return fileUploadService.uploadByType(request);  // 서비스로 파일 업로드 처리
    }

    /**
     * 업로드된 이미지 파일 미리보기 (이미지 URL로 미리보기)
     * @param fileUrl 이미지 파일의 URL 경로
     * @return 이미지 미리보기
     */
    @GetMapping("/preview-image")
    public ResponseEntity<Resource> previewImage(@RequestParam("fileUrl") String fileUrl) {
        Path path = Paths.get(uploadDir).resolve(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
