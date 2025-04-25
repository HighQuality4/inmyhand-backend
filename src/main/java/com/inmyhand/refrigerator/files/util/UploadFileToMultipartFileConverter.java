package com.cleopatra.util;

import com.cleopatra.protocol.data.UploadFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
                // Form field name; can be customized if needed
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return originalFilename;
            }

            @Override
            public String getContentType() {
                try {
                    String probeType = Files.probeContentType(file.toPath());
                    return (probeType != null ? probeType : "application/octet-stream");
                } catch (IOException e) {
                    return "application/octet-stream";
                }
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
