package com.inmyhand.refrigerator.files.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class FileUploadResponse {
    private String fileUrl;
    private String orgName;
    private String fileType;
    private String fileSize;
    private Date createdAt;
}
