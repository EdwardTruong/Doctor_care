package com.example.doctorcare.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.dao.ScheduleRepository;
import com.example.doctorcare.dto.DoctorDto;
import com.example.doctorcare.dto.request.ChangePasswordRequest;
import com.example.doctorcare.dto.request.LoginRequest;
import com.example.doctorcare.dto.request.ScheduleInfoRequest;
import com.example.doctorcare.dto.request.SignupRequest;
import com.example.doctorcare.dto.response.JwtResponse;
import com.example.doctorcare.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.dto.response.UserDtoResponse;
import com.example.doctorcare.entity.Clinics;
import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.entity.Session;
import com.example.doctorcare.entity.Specializations;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.exception.PasswordNotMatchingException;
import com.example.doctorcare.service.ClinicsService;
import com.example.doctorcare.service.DoctorService;
import com.example.doctorcare.service.RoleService;
import com.example.doctorcare.service.ScheduleService;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.ApplicationUtils;
import com.example.doctorcare.utils.Const.JWT_HEARD;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_FIELDS_ERROR;
import com.example.doctorcare.utils.ERole;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


/*
 * The registerUser method 
 * 	The userEntity's field LocalDateTime createdAt can't save exactly the time 
 * 		because mysql setting TIME_ZONE is '+00:00'.
 * 	When user get current date(type LocalDateTime) in java system is correct but database Mysql change the hour (-7) and save it.
 * 	For now, i fix it by (+7) hours (in ApplicationUtils.convertLocalDateTimeBeforeSaveToDb) before save user entity.
 */

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class MainRestController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DoctorService doctorService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	SpecializationService specializationService;
	
	@Autowired
	ClinicsService clinicsService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	ApplicationUtils appUtils;
	

	@Autowired
	ScheduleRepository sDao;
	
	private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);
	
	/*
	 * DONE !
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		JwtResponse newJwt = userService.userLogin(loginRequest);
		sessionService.createSessionLogin(loginRequest.getUsername(), newJwt.getToken());
		HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_HEARD.HEADER_NAME , JWT_HEARD.HEADER_VALUE  +newJwt.getToken());
        headers.get(JWT_HEARD.HEADER_NAME);
        logger.info(MESSENGER.LOGIN_SUCCESS+ JWT_HEARD.HEADER_NAME +"," + JWT_HEARD.HEADER_VALUE + "," +newJwt.getToken());        
		return new ResponseEntity<>(newJwt,headers,HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<UserDtoResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		RoleEntity role = roleService.findByName(ERole.ROLE_USER);
		UserDtoResponse newUser = userService.createUserEntity(signUpRequest,role);	
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}


	/*
	 * The sendEmailResetPassword method have follow logic :
	 * 	-Find an email to check if the user has already been registered or not.
	 * 	-Create jwt by email. (for Spring Security).
	 * 	-Finding a Session by id. 
	 * 	 Having 2 ways :
	 * 		1. If the user has already logged in or is currently logged in, indicating that a session exists in the database,
	 * 			 I retrieve it using the name (sId) and update the data by generating a new JWT.
	 * 		2. If the user has not logged in yet, I create a new session entity
	 * 		3. The Session entity is used for changing passwords, and the generated JWT has the same expiration time of 15 minutes.
	 * 		4. Create a URL containing the information "sid" as a suffix.
	 * 		5. Finally, send an email to the user who needs to change their password along with the link to proceed to the next step of changing the password. 	
	 * 
	 * 		(Next step : The changePassword method )
	 * BONUT : The keyword "key" in "/info?key=" is a parameter that needs to be added to the method like @RequestParam.
	 * 			When users click on the generated link, they will proceed to the next step to change their password.
	 *  
	 */

    @Operation(summary = "Request reset password jwt time out : 5 minutes ")
	@PostMapping("/send-email") 
	public ResponseEntity<Map<String, String>> sendEmailResetPassword (@RequestParam("email") String email, HttpServletRequest request) throws MessagingException , IOException{
		
    	UserEntity userEntity = userService.findByEmail(email);
    	Session getNewSession = sessionService.sendEmailCreateKeyURl(userEntity);    	
		String resetPasswordURL = request.getRequestURL().toString() +"/info/value?key="+getNewSession.getKey();
		Map<String, String> result = userService.sendEmailRestPassword(email,resetPasswordURL,getNewSession.getData());


		return new ResponseEntity<>(result,HttpStatus.OK);
	}


	/*
	 * The changePassword method using 2 things to paramater :
	 * 		a. Object ChangePasswordRequest with 2 fileds : password and rePassword.
	 * 		b. String token (key) is sId of Session entity
	 * Next: Security check JWT when user access URL from email.
	 * If it passes through the security layer AuthTokenFilter, users only need to enter the new password twice 
	 * 	to match in order to complete the password change step.  
	 * 	
	 */
	@PutMapping("/send-email/info/value")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request,
													@RequestParam("key") String key) {

		if (!request.getPassword().equals(request.getRePassword())) {
			throw new PasswordNotMatchingException(MESSENGER_FIELDS_ERROR.PASSWORD_NOT_MATHCING);
		}
		Session session = sessionService.getSessionTakeEmailChangePassword(key);
		UserEntity userEntity = userService.changingPassword(request, session);
		logger.info(appUtils.convertLocalDateTimeToString(userEntity.getUpdateAt()));
		return ResponseEntity.ok(MESSENGER.PASSWORD_SUCCESS);

	}

	/*
	 * 5.1.4. Hiển thị thông tin của các chuyên khoa nổi bật DONE !
	 */
	@GetMapping("/top-specialization")
    public List<Specializations> topSpecializations(){
    	return specializationService.topSpecializations();
    }
	
	/*
	 * 5.1.5. Hiển thị thông tin của các cơ sở y tế nổi bật DONE !
	 */
	@GetMapping("/top-clinics")
    public List<Clinics> topClinics(){
    	return clinicsService.topClinics();
    }
	
	/*
     * 5.1.7. Tìm kiếm chung DONE !
     */
    @GetMapping("/getSchedule")
    public ResponseEntity<List<ScheduleDtoResponse>> scheduleFounds(@Valid @RequestBody ScheduleInfoRequest request){
    	List<ScheduleDtoResponse> result = scheduleService.findScheduleByInfo(request);
    			return ResponseEntity.ok(result);
    }
    
    
	/*
	 * 5.1.6. Hiển thị thông tin cá nhân DONE !
	 */
    @GetMapping("/info")
    public ResponseEntity<UserDtoResponse> showInformation() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDtoResponse result = userService.getInfo(email);
        return ResponseEntity.ok(result);
    }
	
	/*
	 * 5.1.8. Tìm kiếm theo chuyên khoa của bác sĩ DONE !
	 */
	@GetMapping("/doc-specialization")
	public List<DoctorDto> listDoctorSaveSpecialization(@RequestParam("name") String specialization,@RequestParam("date") LocalDate date){
		return doctorService.getlistDocSaveSpecializationWithSchedule(specialization,date);
	}
	
	
	/*
	 * Testing
	 */
    @GetMapping("/getTestSchedule")
    public ResponseEntity<List<ScheduleDtoResponse>>  scheduleFound(@RequestBody ScheduleInfoRequest request){
    	List<ScheduleDtoResponse> result = scheduleService.findScheduleByInfo(request);
    			return ResponseEntity.ok(result);
    }
}
