package com.example.doctorcare.domain.file;

import java.util.Optional;

import com.example.doctorcare.core.domain.BaseRepository;


public interface FileStoreRepository extends BaseRepository<FileStore, Long> {

    Optional<FileStore> findByContentId(String contentId);

}
