package com.example.doctorcare.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.doctorcare.common.utils.Const.*;
@ConfigurationProperties("storage")
public class StorageProperties {
	private String location = DIRECTORY_IMAGE.PATH_PERMANENT;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
