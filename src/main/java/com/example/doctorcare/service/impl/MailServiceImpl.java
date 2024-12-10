package com.example.doctorcare.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.doctorcare.common.utils.Const.MESSENGER;
import com.example.doctorcare.common.utils.Const.VIEW;
import com.example.doctorcare.model.dto.DataMailDto;
import com.example.doctorcare.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/*
 * 	
 *	If the 'from' field of DataMailDto has a value, it means that someone wants to send an email, 
 *		then MimeMessageHelper will use 'from' as the sender's email address.
 *	If the 'from' field is empty, MimeMessageHelper will use the default email configuration set in the MailConfig.class.
 *	   	   
 */

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	// @Value("${spring.mail.host}")
	// private String host;
	//
	// @Value("${spring.mail.port}")
	// private Integer port;
	//
	// @Value("${spring.mail.isSSL}")
	// private String isSSL;

	@Override
	public void sendHtmlMail(DataMailDto dataMail, String templateName, MultipartFile file)
			throws MessagingException, IOException {

		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

		Context context = new Context();
		context.setVariables(dataMail.getProps());

		String html = templateEngine.process(templateName, context);

		if (dataMail.getFrom() != null) {
			helper.setFrom(dataMail.getFrom()); // Dùng để send mail không đụng với appilication.properties default !
			helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
		}

		helper.setTo(dataMail.getTo());
		helper.setSubject(dataMail.getSubject());
		helper.setText(html, true);

		mailSender.send(message);
	}

	@Override
	public Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data)
			throws MessagingException, IOException {
		Map<String, Object> props = new HashMap<>();
		props.put("tokenUrl", tokenUrl);

		DataMailDto dataMail = DataMailDto.builder()
				.to(email)
				.subject(MESSENGER.HEADER_MAIL_PASSWORD)
				.content(tokenUrl)
				.props(props)
				.build();

		this.sendHtmlMail(dataMail, VIEW.EMAIL_PASSWORD, null);

		Map<String, String> result = new HashMap<>();
		result.put("Message", MESSENGER.MAIL_SUCCESS);
		result.put("token", data);
		result.put("type", "Bearer");
		result.put("URL", tokenUrl);

		return result;
	}
}
