package com.example.doctorcare.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.configuration.StorageProperties;
import com.example.doctorcare.exception.StorageException;
import com.example.doctorcare.exception.StorageFileNotFoundException;
import com.example.doctorcare.service.FileService;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;

@Service
public class FileServiceImpl implements FileService {

	private final Path rootLocation;

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Autowired
	public FileServiceImpl(StorageProperties properties) {

		if (properties.getLocation().trim().length() == 0) {
			throw new StorageException(MESSENGER_ERROR.FOLDER_EMPTY);
		}

		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public String storedFileToLocal(MultipartFile file) {

		try {
			if (file.isEmpty()) {
				throw new StorageException(MESSENGER_ERROR.FILE_EMPTY);
			}

			String filename = StringUtils.cleanPath(file.getOriginalFilename());

			if (filename.contains("..")) {
				throw new StorageException(MESSENGER_ERROR.FILE_NAME_ERROR + filename);
			}

			Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize()
					.toAbsolutePath();
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				logger.info(MESSENGER.SUCCESS_FUNTION + filename);
				return filename;
			}

		} catch (IOException e) {
			throw new StorageException(MESSENGER_ERROR.FAILED_STORE, e);

		}
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException(MESSENGER_ERROR.INITALIZE_STORE, e);
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public void deleteFile(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());

			logger.info("resource ready to delete the file : " + resource);

			if (resource.exists() || resource.isReadable()) {
				File fileDelete = new File(resource.getFile().toString());
				logger.info(MESSENGER.SUCCESS_FUNTION + resource);

				fileDelete.delete();
			} else {
				throw new StorageFileNotFoundException(MESSENGER_ERROR.FILE_DELETE + filename);
			}
		} catch (IOException e) {
			throw new StorageFileNotFoundException(MESSENGER_ERROR.FILE_DELETE + filename, e);
		}

	}

}
