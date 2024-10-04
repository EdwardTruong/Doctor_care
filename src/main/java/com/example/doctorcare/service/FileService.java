package com.example.doctorcare.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	void init() ;
	
	Path load(String filename);
	
	String storedFileToLocal(MultipartFile file);
	
	void deleteFile(String filename);
}
