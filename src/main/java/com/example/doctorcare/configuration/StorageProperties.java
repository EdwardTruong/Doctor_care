package com.example.doctorcare.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.example.doctorcare.utils.Const.*;
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
