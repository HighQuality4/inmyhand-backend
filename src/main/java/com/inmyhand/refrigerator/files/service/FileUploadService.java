package com.inmyhand.refrigerator.files.service;

import com.inmyhand.refrigerator.files.domain.entity.FileUploadRequest;
import com.inmyhand.refrigerator.files.domain.entity.FileUploadResponse;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import com.inmyhand.refrigerator.files.repository.FilesRepository;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FilesRepository filesRepository;
    private final MemberRepository memberRepository;
    private final RecipeInfoRepository recipeInfoRepository;
    private final RecipeStepRepository recipeStepRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public FileUploadResponse uploadByType(FileUploadRequest req) {
        MultipartFile file = req.getFile();

        // 1) 파일 메타 추출 및 저장명 생성
        String orgName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType = file.getContentType();
        String fileSize = String.valueOf(file.getSize());
        String sysName  = UUID.randomUUID() + "_" + orgName;

        Path subDir;      // 실제 저장 위치
        String fileUrl;   // 접근 URL

        if (req.getMemberId() != null) {
            subDir = Paths.get(uploadDir, "member", req.getMemberId().toString());
            fileUrl = "/files/member/" + req.getMemberId() + "/" + sysName;
        } else if (req.getRecipeId() != null) {
            subDir = Paths.get(uploadDir, "recipe", req.getRecipeId().toString());
            fileUrl = "/files/recipe/" + req.getRecipeId() + "/" + sysName;
        } else if (req.getStepId() != null) {
            subDir = Paths.get(uploadDir, "step", req.getStepId().toString());
            fileUrl = "/files/step/" + req.getStepId() + "/" + sysName;
        } else {
            throw new IllegalArgumentException("memberId, recipeId, stepId 중 하나는 필수입니다.");
        }

        // 2) 업로드 폴더 생성 및 파일 복사
        try {
            Files.createDirectories(subDir);
            Path savePath = subDir.resolve(sysName);
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        // 3) FilesEntity 빌더에 공통 필드 세팅
        FilesEntity.FilesEntityBuilder builder = FilesEntity.builder()
                .orgName(orgName)
                .sysName(sysName)
                .fileType(fileType)
                .fileSize(fileSize)
                .fileUrl(fileUrl)
                .fileStatus("ACTIVE")
                .createdAt(new Date());

        // 4) 연관 설정
        if (req.getMemberId() != null) {
            MemberEntity member = memberRepository.findById(req.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다. id=" + req.getMemberId()));
            builder.memberEntity(member);
        } else if (req.getRecipeId() != null) {
            RecipeInfoEntity recipe = recipeInfoRepository.findById(req.getRecipeId())
                    .orElseThrow(() -> new IllegalArgumentException("레시피가 없습니다. id=" + req.getRecipeId()));
            builder.recipeInfoEntity(recipe);
        } else if (req.getStepId() != null) {
            RecipeStepsEntity step = recipeStepRepository.findById(req.getStepId())
                    .orElseThrow(() -> new IllegalArgumentException("단계가 없습니다. id=" + req.getStepId()));
            builder.recipeStepEntity(step);
        }

        // 5) 저장 & 응답
        FilesEntity saved = filesRepository.save(builder.build());
        return new FileUploadResponse(
                saved.getFileUrl(),
                saved.getOrgName(),
                saved.getFileType(),
                saved.getFileSize(),
                saved.getCreatedAt()
        );
    }




}
