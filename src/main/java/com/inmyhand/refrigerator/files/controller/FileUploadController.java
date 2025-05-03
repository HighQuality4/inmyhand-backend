package com.inmyhand.refrigerator.files.controller;

import com.cleopatra.XBConfig;
import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.UploadFile;

import com.inmyhand.refrigerator.files.domain.entity.FileUploadRequest;
import com.inmyhand.refrigerator.files.domain.entity.FileUploadResponse;
import com.inmyhand.refrigerator.files.service.FileUploadService;
import com.inmyhand.refrigerator.files.util.UploadFileToMultipartFileConverter;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.OcrFoodDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeAutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    private final FridgeAutoService fridgeAutoService;

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
        log.info("uploadImage 실행 >>>>>>>>>");
        FileUploadRequest request = new FileUploadRequest(file, memberId, recipeId, stepId);
        return fileUploadService.uploadByType(request);  // 서비스로 파일 업로드 처리
    }

    @PostMapping("/upload-test")
    public ResponseEntity<?> fileUploadTest(DataRequest dataRequest) throws IOException {
        Map<String, UploadFile[]> uploadFiles = dataRequest.getUploadFiles();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            for (Map.Entry<String, UploadFile[]> entry : uploadFiles.entrySet()) {
                UploadFile[] uFiles = entry.getValue();

                if (uFiles.length == 1) {
                    // 단일 파일
                    UploadFile single = uFiles[0];
                    MultipartFile mf = UploadFileToMultipartFileConverter.toMultipartFile(single);

                    log.info("단일 파일 업로드 처리: {}, size: {}",
                    mf.getOriginalFilename(), mf.getSize());

                    Long memberId = 23L;
                    Long recipeId = null;
                    Long stepId = null;

                    // 이미지 파일 업로드 서비스 호출
                    FileUploadRequest request = new FileUploadRequest(mf, memberId, recipeId, stepId);
                    fileUploadService.uploadByType(request);


                } else if (uFiles.length > 1) {
                    // 다중 파일
                    // 추후를 위한 로직 설계
                    List<MultipartFile> mfs = Arrays.stream(uFiles)
                            .map(UploadFileToMultipartFileConverter::toMultipartFile)
                            .toList();

                    log.info("다중 파일 업로드 처리: count = {}", mfs.size());
                }
            }
        }
        return ResponseEntity.ok("ok");
    }


    // 이미지 업로드 후 동작 ( ocr 추출 , 카테고리 추출, 결과값 화면 반환)
    @PostMapping("/ocr-upload-test")
    public ResponseEntity<?> ocrFileUploadTest(DataRequest dataRequest) throws IOException {
        Map<String, UploadFile[]> uploadFiles = dataRequest.getUploadFiles();

        List<ReceiptDTO> resultReceipt = new ArrayList<>();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            for (Map.Entry<String, UploadFile[]> entry : uploadFiles.entrySet()) {
                UploadFile[] uFiles = entry.getValue();

                    // 단일 파일
                    UploadFile single = uFiles[0];
                    MultipartFile mf = UploadFileToMultipartFileConverter.toMultipartFile(single);

                    log.info("단일 파일 업로드 처리: {}, size: {}",
                    mf.getOriginalFilename(), mf.getSize());

                    // ======================== ocr 로직 text 변환로직 시작   =============================
                    System.out.println("파일명: " + mf.getOriginalFilename());

                    List<MultipartFile> fileList =  new ArrayList<>();;
                    fileList.add(mf);

                    resultReceipt = fridgeAutoService.svcOcrTotalTest(fileList);

                    for (ReceiptDTO receipt : resultReceipt) {
                        System.out.println(receipt);
                    }

            }
        }

        // ========================  ReceiptDTO => OcrFoodDTO  =============================
        List<OcrFoodDTO> ocrFoodList = new ArrayList<>();

        for (ReceiptDTO receipt : resultReceipt) {
            String cleanedProduct = receipt.getProduct().replaceFirst("^\\(S\\)\\s*", ""); // "(S) " 제거

            OcrFoodDTO foodDTO = OcrFoodDTO.builder()
                    .foodName(cleanedProduct)
                    .foodAmount(Long.parseLong(receipt.getQuantity()))
                    .chargeDate(receipt.getPurchaseDate())
                    .endDate(receipt.getExpirationDate())
                    .categoryName(receipt.getCategory())
                    .build();

            ocrFoodList.add(foodDTO);
            System.out.println("OCR 변환 결과 → " + foodDTO);
        }


        log.info("받은 파일: {}", uploadFiles);
        log.info("처리한 데이터: {}", ocrFoodList);
        log.info("데이터 보냅니다 >>>>>>>>>>>>>>>>>>>>>>>>>");
        return ResponseEntity.ok(Map.of("ocrResultList", ocrFoodList));
    }


}
