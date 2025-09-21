package com.example.doctorcare.application.service.file.dto;

import com.example.doctorcare.domain.file.FileStore;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class FileDto extends com.example.doctorcare.core.domain.BaseDto<Long>{

    private Long id;
    private String contentId;
    private long contentLength;
    private String contentType;
    private String contentOriginalFilename;
    private boolean publish;
    private String fileUrl;

    public FileStore toEntity() {
        return FileStore.builder()
                .id(id)
                .contentId(contentId)
                .contentLength(contentLength)
                .contentType(contentType)
                .contentOriginalFilename(contentOriginalFilename)
                .publish(publish)
                .build();
    }

    public static FileDto fromEntity(FileStore file) {
        return FileDto.builder()
                .id(file.getId())
                .contentId(file.getContentId())
                .contentLength(file.getContentLength())
                .contentType(file.getContentType())
                .contentOriginalFilename(file.getContentOriginalFilename())
                .publish(file.isPublish())
                .build();
    }
}
