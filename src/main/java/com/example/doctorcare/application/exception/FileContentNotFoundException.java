package com.example.doctorcare.application.exception;

import com.example.doctorcare.domain.file.FileStore;

/**
 * Exception được ném ra khi không tìm thấy nội dung của một file (ví dụ: file trên S3/DB đã bị xóa).
 * Kế thừa từ {@link ResourceNotFoundException} để trả về HTTP status 404 Not Found.
 */
public class FileContentNotFoundException extends ResourceNotFoundException {

    public FileContentNotFoundException(Object entityIdentifier) {
        super(FileStore.class.getSimpleName() + " content", "ID", entityIdentifier);
    }
}
