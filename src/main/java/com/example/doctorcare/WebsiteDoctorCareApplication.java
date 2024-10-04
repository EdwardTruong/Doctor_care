package com.example.doctorcare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.doctorcare.configuration.StorageProperties;
import com.example.doctorcare.service.FileService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WebsiteDoctorCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsiteDoctorCareApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(FileService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
