package com.example.doctorcare.application.service.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.application.exception.BadRequestException;
import com.example.doctorcare.application.exception.FileContentNotFoundException;
import com.example.doctorcare.application.exception.FileNotFoundException;
import com.example.doctorcare.application.service.file.dto.FileDto;
import com.example.doctorcare.domain.file.FileContentStore;
import com.example.doctorcare.domain.file.FileStore;
import com.example.doctorcare.domain.file.FileStoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStoreServiceImpl implements FileStoreService {

    private final FileStoreRepository fileStoreRepository;
    private final FileContentStore fileContentStore;

    @Value("${application.file.max-size-mb:5}")
    private long maxSizeInMb;

    @Value("#{'${spring.content.allowed-mime-types:image/jpeg,image/png,application/pdf}'.split(',')}")
    private List<String> allowedMimeTypes;

    private final Tika tika = new Tika();

    @Override
    @Transactional
    public FileDto uploadFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BadRequestException("Tệp tải lên không được để trống.", "error.file.empty");
        }

        if (multipartFile.getSize() > maxSizeInMb * 1024 * 1024) {
            throw new BadRequestException(
                String.format("Kích thước file không được vượt quá %d MB.", maxSizeInMb),
                "error.file.sizeExceeded", maxSizeInMb);
        }

        String detectedType;
        byte[] fileBytes;
        try {
            // Đọc file vào byte array một lần duy nhất để tránh lỗi stream đã đóng
            fileBytes = multipartFile.getBytes();
            // Sử dụng Tika để xác định loại MIME từ nội dung file, an toàn hơn là tin vào client
            detectedType = tika.detect(fileBytes);
        } catch (IOException e) {
            log.error("Không thể đọc hoặc xác định loại file: {}", multipartFile.getOriginalFilename(), e);
            throw new BadRequestException("Không thể đọc hoặc xác định loại file.", "error.file.undetectable");
        }

        if (!allowedMimeTypes.contains(detectedType)) {
            throw new BadRequestException("Loại file không được hỗ trợ. Chỉ chấp nhận: " + allowedMimeTypes, "error.file.invalidType");
        }

        FileStore fileStore = new FileStore();
        // Sanitize filename to prevent path traversal attacks
        fileStore.setContentOriginalFilename(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        fileStore.setContentType(detectedType); // Sử dụng loại MIME đã được xác thực
        fileStore.setContentLength(fileBytes.length);
        fileStore.setContentPath("");
        fileStore.setPublish(true);

        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            String contentPath = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "/" + UUID.randomUUID();
            fileStore.setContentPath(contentPath);
            fileContentStore.setContent(fileStore, inputStream);
            if (fileStore.getContentId() == null) {
                throw new IllegalStateException("ContentId was not set by the ContentStore. Check ContentStoreConfig.");
            }
//            String contentPath = LocalDate.now().toString("yyyyMMdd") + "/" + fileStore.getContentId();
//            fileStore.setContentPath(contentPath);
            FileStore savedFile = fileStoreRepository.save(fileStore);

            log.info("File uploaded successfully {},", savedFile);
            return FileDto.fromEntity(savedFile);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional
    public FileDto buildThumbnailFromImageFile(Long imageFileId, int width) {
        // 1. Find the original image
        FileStore originalImage = fileStoreRepository.findById(imageFileId)
                .orElseThrow(() -> new FileNotFoundException(imageFileId));

        // 2. Basic check if it's an image
        if (originalImage.getContentType() == null || !originalImage.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File with id " + imageFileId + " is not an image.");
        }

        try (InputStream originalContent = fileContentStore.getContent(originalImage)) {
            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();

            // 3. Generate thumbnail using Thumbnailator
            Thumbnails.of(originalContent)
                    .width(width)
                    .outputFormat("jpeg") // Force a common format for thumbnails
                    .toOutputStream(thumbnailOutputStream);

            // 4. Create a new FileStore entity for the thumbnail
            FileStore thumbnailFile = new FileStore();
            thumbnailFile.setContentType("image/jpeg");
            String originalName = originalImage.getContentOriginalFilename();
            String thumbnailName = "thumb_" + width + "px_" + originalName;
            thumbnailFile.setContentOriginalFilename(thumbnailName);

            byte[] thumbnailBytes = thumbnailOutputStream.toByteArray();
            thumbnailFile.setContentLength((long) thumbnailBytes.length);

            // 5. Save the thumbnail content and metadata
            fileContentStore.setContent(thumbnailFile, new ByteArrayInputStream(thumbnailBytes));
            FileStore savedThumbnail = fileStoreRepository.save(thumbnailFile);

            log.info("Thumbnail created for imageId {}: new thumbnailId={}", imageFileId, savedThumbnail.getId());
            return FileDto.fromEntity(savedThumbnail);

        } catch (IOException e) {
            log.error("Could not generate thumbnail for imageId: {}", imageFileId, e);
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDto> findByContentId(String contentId) {
        return fileStoreRepository.findByContentId(contentId).map(FileDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDto> findById(Long id) {
        return fileStoreRepository.findById(id).map(FileDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStream getFileContent(String contentId) {
        return fileStoreRepository.findByContentId(contentId)
                .map(this::getContent)
                .orElseThrow(() -> new FileContentNotFoundException(contentId));
    }
    
    @Override
    @Transactional
    public InputStream getContent(FileStore file) {
        return fileContentStore.getContent(file);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource getResource(String contentId) {
        return fileStoreRepository.findByContentId(contentId)
                .map(fileContentStore::getResource)
                .orElseThrow(() -> new FileContentNotFoundException(contentId));
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId) {
        FileStore fileToDelete = fileStoreRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(fileId));

        fileContentStore.unsetContent(fileToDelete); // Deletes the physical file
        fileStoreRepository.delete(fileToDelete); // Deletes the metadata from the DB
        log.info("File deleted successfully: id={}", fileId);
    }

}
