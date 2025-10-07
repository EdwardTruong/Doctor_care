package com.example.doctorcare;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Configuration properties for file storage
 */
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    /**
     * Maximum file size allowed (in MB)
     */
    private long maxFileSize = 10;

    /**
     * Maximum request size allowed (in MB)
     */
    private long maxRequestSize = 10;

    /**
     * Allowed file extensions
     */
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"};
}