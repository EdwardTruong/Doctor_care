// package com.example.doctorcare.application.service;

// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.security.KeyStoreException;
// import java.security.NoSuchAlgorithmException;
// import java.security.UnrecoverableKeyException;
// import java.security.cert.CertificateException;
// import java.util.List;
// import java.util.Map;

// import com.example.doctorcare.domain.system.user.User;

// import jakarta.mail.MessagingException;

// public interface UserService {
		
// 	List<User> loadAll();
		
// 	User findById(Integer id);
	
// 	void save(User entity);
	
// 	void update(User entity);
	
// 	User update(User entity,UserUpdateRequest request);
	
// 	void delete(User entity);
	
// 	JwtResponse userLogin(LoginRequest loginRequest) throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
	
// 	UserDtoResponse updateUser(String email,UserUpdateRequest request);
	
// 	boolean emailExist(String email);
	
// 	User findByEmail(String email);
	
// 	boolean existsByEmail(String email);
	
// 	Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data) throws MessagingException, IOException;

// 	User createrUser(SignupRequest signUpRequest, RoleEntity role);

// 	User changingPassword(ChangePasswordRequest request,Session session) throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
	
// 	UserDtoPatientResponse getUserInfo(String email); // ROLE_USER using this method.
	
// 	UserDtoResponse getInfo(String email); // ROLE_ADMIN & ROLE_DOCTOR using this method.
	
// 	UserDtoResponse lockPatient(Integer id, String reason);

// 	UserDtoResponse unlockPatient(Integer id, String reason);

// 	User createStatus(String email, Statuses status);
	
// 	UserDtoResponse createUser(SignupRequest signUpRequest, RoleEntity role);

// 	User createrUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role);

// 	int passworRegisterdErrors(String password);

	
// }	
