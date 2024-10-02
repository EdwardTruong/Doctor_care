package com.example.doctorcare.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Const {

	public final static class DIRECTORY_IMAGE {
		public final static String PATH_PERMANENT = "./src/main/resources/static/assets/upload/";
	}

	public final static class ALL_IMAGES_PATH {
		public final static String PATH = "/assets/upload/";
	}

	public final static class JWT_HEARD {
		public final static String HEADER_NAME = "Authorization";
		public final static String HEADER_VALUE = "Bearer ";
	}

	public final static class TIME {
		public final static long EXPIRE_DURATION_JWT = 6 * 60 * 60 * 1000; // 6h(milliseconds )
		public final static long EXPIRE_DURATION_JWT_MINUTES = 6 * 60;
		public final static long EXPIRE_DURATION_JWT_CHANGE_PASSWORD = 15 * 60 * 1000; // 15 munites(milliseconds )
		public final static long EXPIRE_DURATION_SESSION_CHANGE_PASSWORD = 15; // 15 (minutes)
		
	}

	public final static class VIEW {
		public final static String EMAIL_PASSWORD = "email-template-reset-password";
		public final static String RESULT_PDF = "email-template-result-pdf";

	}

	public final static class MESSENGER_FIELDS_ERROR {
		public final static String EMAIL_ERROR = "Email required !";
		public final static String PASSWORD_ERROR = "Password required !";
		public final static String REPASSWORD_ERROR = "Re-Password required !";
		public final static String NAME_ERROR = "Name required !";
		public final static String GENDERL_ERROR = "Genderl required !";
		public final static String PHONE_ERROR = "Requires 3 to 15 digit phone number !";
		public final static String ADDRESS_ERROR = "Address required !";
		public final static String PASSWORD_NOT_MATHCING = "Password not matching, try again !";
		public final static String HTML_INTRO = "Introduction HTML required!";
		public static final String MARDOWN_INTRO = "Introduction Markdown required!";
		public static final String TIME_SCHEDULE = "Schedule time required!";
		public static final String DATE_SCHEDULE = "Schedule date required!";
		public static final String MAX_BOOKIN = "Max booking required!";
		public static final String DOCTOR_ID = "Missing doctor's id!";
		public static final String REASON_MISSING = "Give reason what patient wanna see doctor !";
		public static final String PATIENT_MISSING = "Give reason what patient wanna see doctor !";
		public static final String DOB_MISSING = "Date Of birth missing !";
		public static final String DOB_ERROR = "Date Of birth error !";
		public static final String PLACE_ID_MISSION = "Place required!";
		public static final String DATE_SEE_DOCTOR = "Request (date) a doctor's appointment!";
		public static final String TIME_SEE_DOCTOR = "Request (time) a doctor's appointment!";
		public static final String DESCRIPTION_ERROR = "Description required!"; // DOCTOR NEED USER DON'T
		public static final String PRICE_ERROR = "Price required"; // DOCTOR NEED USER DON'T
		public static final String SCHEDULE_ERROR = "Schedule's id required"; // DOCTOR NEED USER DON'T
		public final static String PLACE_ERROR = "Need information at least area.";

		
		
		public static final String DOC_ACHIEVEMENT = "Doctor's academic program required!"; // DOCTOR NEED USER DON'T
		public final static String DOC_TRAINING_PROCESS = "Training process required !";
		public final static String DOC_CLINIC_ERROR = "Clinic's id required !";
		public final static String DOC_SPECIALIZATION = "Specialization's id(s) required !";
		public final static String DOC_SPECIALIZATION_ID_ERROR = "Specialization id not found !";
	
	}

	public final static class MESSENGER_ERROR {		
		public final static String SECURITY_ERROR = "URL need to JWT ! ";
		public final static String FOLDER_EMPTY = "File upload location can not be Empty.";
		public final static String FILE_EMPTY = "Failed to store empty files.";
		public final static String FILE_NAME_ERROR = "Sorry! Filename contains invalid path sequence: ";
		public final static String FAILED_STORE = "Failed to store file.";
		public final static String INITALIZE_STORE = "Could not initialize storage";
		public final static String FILE_DELETE = "Could not detele file: ";
		public final static String NOT_IMAGE = "This is not image.";
		public final static String ORVER_SIZE = "File upload over size.";
		public final static String SESSION_TIME_OUT = "Link has expired. ";
		public final static String PATIENTS_NOT_BELOW_DOC = "The patient is not on the examination list !";
		public final static String TIME_BOOK_ERROR = "Please select the doctor's exact working hours !";
		public final static String TIME_INPUT_ERROR = "Not in the correct format for date input!";
		public final static String USER_EXIST = "Error: Email is already in use !";
		public final static String CANT_LOCK_ADMIN = "Can not lock main Admin !";
		public final static String ACTIVE_ERROR = "Active have 3 values : (-1,0,1) - (No, Not confirmed yet, Yes).";
		public final static String PRICES_ERROR = "min price > max price.";
		public final static String CANT_LOCK = "This account lock already !";
		public final static String CANT_UNLOCK = "The account was previously locked !";
		public final static String CANT_LOCK_DOC = "This is doctor account.";
		public final static String PASSOWRD_REGISTER_ERROR = "Password need have ";
		public final static String FORBIDDEN_MESSENGER = "The password needs to be at least 6 characters long and contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character.";


		
	}

	public final static class MESSENGER {
		public final static String SUCCESS_FUNTION 		= "Success function before return !";
		public final static String SUCCESS 				= "Method success !!!";
		public final static String SEE_DOCTOR_SUCCESS 	= "Creating a date see doctor success !";
		public final static String PASSWORD_SUCCESS 	= "Change password success!";
		public final static String LOGIN_SUCCESS 		= "Login success and jwt created : ";
		public final static String CREATE_DOCTOR 		= "Create new doctor success! ";
		public final static String CREATE_USER 			= "Create user success! ";
		public final static String CHANGE_ACTIVE 		= "Changing active user success!";
		public final static String SEND_EMAIL_SUCCESS 	= "Email send success. Check mail please. ";
		public final static String HEADER_MAIL_PASSWORD = "Reseting email ";
		public final static String HEADER_MAIL_DOC		= "Medical examination results.";
		
		public final static String UPDATE_INFO 			= "Update infomation success !";
		public final static String USER_INFO 			= "Basic user's infomations.";
		public final static String DOCTOR_INFO 			= "Basic user's infomations.";
		public final static String PATIENT_INFO 		= "Patient infomations.";
		public final static String MAIL_SUCCESS 		= "Email sent successfully";

		
		public final static String LOCKED_SUCCESS = "This account lock success !";
		public final static String UNLOCK_SUCCESS = "This account active.";
	
	}

	public final static class MESSENGER_NOT_FOUND {
		public final static String ROLE_NOT_EXIST = "Error: Role cannot be found!";
		public final static String CLINIC_NOTFOUND = "Error: Clinic cannot be found!";
		public final static String SPECIALIZATION_NOTFOUND = "Error: Specialization cannot be found!";
		public final static String USER_NOT_FOUND_EMAIL = "The account cannot be found with email : ";
		public final static String USER_NOT_FOUND_ID = "The account cannot be found with id";
		public final static String KEY_NOT_FOUND = "Key not found !";
		public final static String DOCTOR_NOT_FOUND = "Doctor not found !";
		public final static String SESSION_NOT_FOUND = "Session doesn't exit !";
		public final static String SCHEDULE_NOT_FOUND = "Doctor's schedule not found!";
		public final static String STATUS_NOT_FOUND = "Status's name not ! (ok,no,non)";
		
	}

	public final static class ACTIVE {
		
		public final static int DENICE = -1;
		public final static int NONE = 0;
		public final static int ACCEPT = 1;
		public final static String NO = "Has been cancelled.";
		public final static String YES = "Confirmed.";
		public final static String NON = "Waiting for confirmation.";
		public final static String ACC_LOCK = "This account has been locked.";
		public final static String ACC_UNLOCK = "Normal account.";
	}
	public final static class KEY {
		public final static String VALUE = "Key-";

	}
	
	public final static class PASSWORD{
		public final static Set<Character> SPECIAL_CHACTERLIST = new HashSet<>(Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));

	}
}
