package com.example.doctorcare.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityAuditorConfiguration {

	@Bean
	SecurityAuditorAware springSecurityAuditorAware() {
		return new SecurityAuditorAware();
	}
}
