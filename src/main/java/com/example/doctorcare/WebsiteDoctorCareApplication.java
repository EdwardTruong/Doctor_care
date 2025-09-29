package com.example.doctorcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.doctorcare.infrastructure.common.configuration.StorageProperties;

@EnableConfigurationProperties(StorageProperties.class)
public class WebsiteDoctorCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsiteDoctorCareApplication.class, args);
	}
	

}
