package com.inmyhand.refrigerator.files.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.cleopatra.protocol.data.UploadFile;

/**
 * Utility to convert a custom UploadFile object into Spring's MultipartFile.
 */
public class UploadFileToMultipartFileConverter {

    /**
     * Converts the given UploadFile into a MultipartFile implementation.
     *
     * @param uploadFile the custom UploadFile containing the File and fileName
     * @return a MultipartFile representing the same file
     * @throws IllegalArgumentException if uploadFile or its internal File is null
     */
    public static MultipartFile toMultipartFile(UploadFile uploadFile) {
        if (uploadFile == null || uploadFile.getFile() == null) {
            throw new IllegalArgumentException("UploadFile or underlying File must not be null");
        }
        File file = uploadFile.getFile();
        String originalFilename = uploadFile.getFileName();

        return new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return originalFilename;
            }

            @Override
            public String getContentType() {
                // 1차 시도: probeContentType 사용
                try {
                    String probeType = Files.probeContentType(file.toPath());
                    if (probeType != null) {
                        return probeType;
                    }
                } catch (IOException ignored) {
                }

                // 2차 시도: 파일 확장자 기반 매핑
                String lowerName = originalFilename.toLowerCase();
                if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) return "image/jpeg";
                if (lowerName.endsWith(".png")) return "image/png";
                if (lowerName.endsWith(".gif")) return "image/gif";
                if (lowerName.endsWith(".pdf")) return "application/pdf";
                if (lowerName.endsWith(".txt")) return "text/plain";

                // 최종 fallback
                return "application/octet-stream";
            }

            @Override
            public boolean isEmpty() {
                return file.length() == 0;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return Files.readAllBytes(file.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(file);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Path targetPath = dest.toPath();
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            @Override
            public void transferTo(Path dest) throws IOException, IllegalStateException {
                Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            }
        };
    }
}
