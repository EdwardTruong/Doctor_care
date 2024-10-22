package com.example.doctorcare.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import com.example.doctorcare.dto.request.ChangePasswordRequest;
import com.example.doctorcare.dto.request.LoginRequest;
import com.example.doctorcare.dto.request.SignupDoctorRequest;
import com.example.doctorcare.dto.request.SignupRequest;
import com.example.doctorcare.dto.request.UserUpdateRequest;
import com.example.doctorcare.dto.response.JwtResponse;
import com.example.doctorcare.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.dto.response.UserDtoResponse;
import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.entity.Session;
import com.example.doctorcare.entity.Statuses;
import com.example.doctorcare.entity.UserEntity;

import jakarta.mail.MessagingException;

public interface UserService {
		
	List<UserEntity> loadAll();
		
	UserEntity findById(Integer id);
	
	void save(UserEntity entity);
	
	void update(UserEntity entity);
	
	UserEntity update(UserEntity entity,UserUpdateRequest request);
	
	void delete(UserEntity entity);
	
	JwtResponse userLogin(LoginRequest loginRequest) throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
	
	UserDtoResponse updateUser(String email,UserUpdateRequest request);
	
	boolean emailExist(String email);
	
	UserEntity findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data) throws MessagingException, IOException;

	UserEntity createrUser(SignupRequest signUpRequest, RoleEntity role);

	UserEntity changingPassword(ChangePasswordRequest request,Session session) throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
	
	UserDtoPatientResponse getUserInfo(String email); // ROLE_USER using this method.
	
	UserDtoResponse getInfo(String email); // ROLE_ADMIN & ROLE_DOCTOR using this method.
	
	UserDtoResponse lockPatient(Integer id, String reason);

	UserDtoResponse unlockPatient(Integer id, String reason);

	UserEntity createStatus(String email, Statuses status);
	
	UserDtoResponse createUserEntity(SignupRequest signUpRequest, RoleEntity role);

	UserEntity createrUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role);

	int passworRegisterdErrors(String password);

	
}	
