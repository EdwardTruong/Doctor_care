package com.example.doctorcare.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.model.dto.DataMailDto;

import jakarta.mail.MessagingException;

/*
 * The sendHtmlMail method use to send a mail from email of default system to user's mail.
 * 	Step 1 : Adding information and port of mail address (this project using mail of google) in application.properties.\
 * 				and spring-boot-starter-mail in file pom.xml.
 * 	Step 2 : Creating MailConfig use to config with java with application.properties.
 * 	Step 3 : Creating DataMailDto class. This is a main of a mail. (More infomation in DataMailDto.class)
 *  Step 4 : Using in UserService to send e-mail
 */

public interface MailService {

	void sendHtmlMail(DataMailDto dataMail, String templateName, MultipartFile file)
			throws MessagingException, IOException;

	Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data)
			throws MessagingException, IOException;
}
