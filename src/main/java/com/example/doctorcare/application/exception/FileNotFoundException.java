package com.example.doctorcare.application.exception;

import com.example.doctorcare.domain.file.FileStore;

/**
 * Exception được ném ra khi một bản ghi file (FileStore) không được tìm thấy trong DB.
 */
public class FileNotFoundException extends ResourceNotFoundException {

    public FileNotFoundException(Object entityIdentifier) {
        super(FileStore.class.getSimpleName(), "ID", entityIdentifier);
    }
}