package com.example.doctorcare.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	boolean isImage(MultipartFile file);
    
	boolean isImageExtension(String fileName);
	
	boolean checkSizeImage(MultipartFile file);
	
	 void renameFilesInFolder(String folderPath);
	 
	 String getNewFileName(String fileName, String folderPath);

	 String setImageForObject(MultipartFile image);
}
