package com.example.doctorcare.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.doctorcare.common.utils.Const.ACTIVE;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ApplicationUtils {

	public String converActiveUserToString(int active) {
		String result = "";
		switch (active) {
		case ACTIVE.NONE: {
			result = ACTIVE.ACC_LOCK;
			break;
		}
		case ACTIVE.ACCEPT: {
			result = ACTIVE.ACC_UNLOCK;
			break;
		}
		default:
			result = ACTIVE.ACC_UNLOCK;
		}
		return result;

	}

	public String converActivePatienToString(int statusNumber) {

		String result = "";

		switch (statusNumber) {
		case ACTIVE.DENICE: {
			result = ACTIVE.NO;
			break;
		}
		case ACTIVE.NONE: {
			result = ACTIVE.NON;
			break;
		}
		case ACTIVE.ACCEPT: {
			result = ACTIVE.YES;
			break;
		}
		default:
			result = ACTIVE.NON;
		}

		return result;

	}

	public String convertLocalDateTimeToString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String result = time.format(formatter);
		return result;
	}

	public String convertToVND(int number) {
		String stringNumber = String.valueOf(number);
		stringNumber = stringNumber + "000";
		Integer converToInt = Integer.parseInt(stringNumber);
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		String formattedNumber = numberFormat.format(converToInt);
		return formattedNumber;
	}

	public String keyRandom() {
		String key = UUID.randomUUID().toString();
		return key;
	}

	public String getDomainApplication(HttpServletRequest request) {
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
		return baseUrl;
	}

	public String getCurrentUrl() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request.getRequestURL().toString();
	}

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	public String getCurrentDateTime(String tableName) {
		String url = "jdbc:mysql://localhost:3306/spring_medical_appointment_scheduling";
		String username = "assignment";
		String password = "assignment";
		LocalDateTime currentDateTime = LocalDateTime.now();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "INSERT INTO " + tableName + " (create_at) VALUES (?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setObject(1, currentDateTime);
			preparedStatement.executeUpdate();
			return currentDateTime.toString().replace("T", " ");
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	LocalDateTime getExpirationSession(LocalDateTime dateCreated) {
		LocalDateTime newDateTime = dateCreated.plusHours(24);
		return newDateTime;
	}

	public String convertDateToString(LocalDate date) {
		Date utilDate = java.sql.Date.valueOf(date);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = dateFormat.format(utilDate);
		return strDate;
	}

	public Date getCurrentDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = sdf.format(new Date());
			return sdf.parse(formattedDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isExpiration(String inputStartDate, String inputEndDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate dateStart = LocalDate.parse(inputStartDate, formatter);
		LocalDate dateEnd = LocalDate.parse(inputEndDate, formatter);
		return dateStart.isAfter(dateEnd);
	}

	public String generateDateTimeToStirng() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return currentDateTime.toString().replace("T", " ");
	}

}
