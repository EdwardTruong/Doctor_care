package com.example.doctorcare.application.service.file;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.application.service.file.dto.FileDto;
import com.example.doctorcare.domain.file.FileStore;


/**
 * Service interface cho các hoạt động quản lý file.
 * Bao gồm tải lên, tải xuống, truy vấn và xóa file.
 */
public interface FileStoreService {

    /**
     * Tải lên một file và lưu trữ siêu dữ liệu của nó.
     *
     * @param multipartFile File được tải lên.
     * @return FileDto chứa thông tin về file đã lưu.
     */
    FileDto uploadFile(MultipartFile multipartFile);

    /**
     * Tạo một ảnh thumbnail từ một file ảnh đã có.
     *
     * @param imageFileId ID của file ảnh gốc.
     * @param width Chiều rộng mong muốn của thumbnail.
     * @return FileDto chứa thông tin về thumbnail đã tạo.
     */
    FileDto buildThumbnailFromImageFile(Long imageFileId, int width);

    /**
     * Tìm thông tin file bằng contentId.
     *
     * @param contentId ID nội dung của file.
     * @return Một Optional chứa FileDto nếu tìm thấy.
     */
    @Transactional(readOnly = true)
    Optional<FileDto> findByContentId(String contentId);

    /**
     * Tìm thông tin file bằng ID.
     *
     * @param id ID của file.
     * @return Một Optional chứa FileDto nếu tìm thấy.
     */
    @Transactional(readOnly = true)
    Optional<FileDto> findById(Long id);

    InputStream getFileContent(String contentId);

    InputStream getContent(FileStore file);

    Resource getResource(String contentId);
    /**
     * Xóa một file dựa trên ID của nó.
     * @param fileId ID của file cần xóa.
     */
    void deleteFile(Long fileId);
}
