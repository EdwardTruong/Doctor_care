package com.example.doctorcare.security.jwt;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.doctorcare.dto.response.HttpResponse;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Any URLs have error, this class is calling.
 * I write 2 method : 
 * 	1. form https://stackoverflow.com/questions/63448838/convert-string-to-localdatetime-java-8
 * 	2. create HttpResponse use show field 
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
				
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		HttpResponse httpResponse = HttpResponse.builder()
					.timeStamp(new Date())
					.httpStatusCode(HttpStatus.UNAUTHORIZED.value())
					.httpStatus(HttpStatus.UNAUTHORIZED)
					.message(MESSENGER_ERROR.SECURITY_ERROR)
				.build();
				
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();

		mapper.writeValue(outputStream, httpResponse);
		outputStream.flush();
	}
}
