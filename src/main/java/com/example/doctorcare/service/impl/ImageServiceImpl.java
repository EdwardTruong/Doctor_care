package com.example.doctorcare.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.common.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.exception.ImageException;
import com.example.doctorcare.service.FileService;
import com.example.doctorcare.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	
	@Autowired
	FileService fileService;
	
	@Override
	public boolean isImage(MultipartFile file){
		String contentType = file.getContentType();
		if(contentType != null){
			return contentType.startsWith("image/") || isImageExtension(file.getOriginalFilename());
		}
		return false;
	}
	
	@Override
	public boolean isImageExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") ||
               extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp");
    }

	@Override
    public boolean checkSizeImage(MultipartFile file){
        return file.getSize() < 10485760;
    }

	@Override
	public void renameFilesInFolder(String folderPath) {
	    File folder = new File(folderPath);
	    File[] files = folder.listFiles();

	    for (File file : files) {
	        if (file.isFile()) {
	            String fileName = file.getName();
	            String newName = getNewFileName(fileName, folderPath);
	            
	            // Rename file
	            File newFile = new File(folderPath + File.separator + newName);
	            file.renameTo(newFile);
	        }
	    }
	}

	@Override
	public String getNewFileName(String fileName, String folderPath) {
	    String newFileName = fileName;
	    File file = new File(folderPath + File.separator + newFileName);
	    int count = 1;

	    while (file.exists()) {
	        int dotIndex = fileName.lastIndexOf('.');
	        String name = fileName.substring(0, dotIndex);
	        String extension = fileName.substring(dotIndex);
	        
	        newFileName = name + "_" + count + extension;
	        file = new File(folderPath + File.separator + newFileName);
	        count++;
	    }

	    return newFileName;
	}

	@Override
	public String setImageForObject(MultipartFile image) {

		if (!this.isImage(image)) {
			throw new ImageException(MESSENGER_ERROR.NOT_IMAGE);
		}
		if (!this.checkSizeImage(image)) {
			throw new ImageException(MESSENGER_ERROR.ORVER_SIZE);
		}

		String fileName = fileService.storedFileToLocal(image);

		return fileName;
	}

}
