package com.example.doctorcare.service.impl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bouncycastle.openssl.PasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.UserRepository;
import com.example.doctorcare.dto.DataMailDto;
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
import com.example.doctorcare.exception.ActiveException;
import com.example.doctorcare.exception.EmailExistException;
import com.example.doctorcare.exception.PasswordRegisterErrors;
import com.example.doctorcare.exception.UserNotFoundException;
import com.example.doctorcare.mapper.RequestMapper;
import com.example.doctorcare.mapper.UserMapper;
import com.example.doctorcare.security.custom.UserDetailsCustom;
import com.example.doctorcare.security.jwt.JwtUtils;
import com.example.doctorcare.service.MailService;
import com.example.doctorcare.service.RoleService;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.ApplicationUtils;
import com.example.doctorcare.utils.Const.ACTIVE;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.utils.Const.PASSWORD;
import com.example.doctorcare.utils.Const.VIEW;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;



@Service
public class UserServiceImple implements UserService {

	@Autowired
	UserRepository userDao;

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	MailService mailService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleService roleService;

	@Autowired
	SessionService sService;

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	RequestMapper signupMapper;
	
	@Autowired
	ApplicationUtils appUtils;
	
	

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImple.class);

	@Override
	public List<UserEntity> loadAll() {
		return userDao.findAll();
	}

	@Override
	public UserEntity findById(Integer id) {
		Optional<UserEntity> resutl = userDao.findById(id);
		return resutl.orElseThrow(
				() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + id + ")."));
	}

	@Override
	public void save(UserEntity entity) {
		userDao.save(entity);
	}
	
	@Override
	public void update(UserEntity entity) {
		userDao.saveAndFlush(entity);
	}

	@Override
	public UserEntity update(UserEntity entity, UserUpdateRequest request) {
		entity.setName(request.getName());
		entity.setAddress(request.getAddress());
		entity.setPhone(request.getPhone());
		entity.setGenderl(request.getGender());
		entity.setDateOfbirth(request.getDateOfbirth());
		entity.setUpdateAt(LocalDateTime.now());
		return userDao.saveAndFlush(entity);
	}
	
	@Override
	public void delete(UserEntity entity) {
		userDao.delete(entity);
	}
	
	
	@Override
	public UserDtoResponse updateUser(String email,UserUpdateRequest request) {
		UserEntity user =  this.findByEmail(email);
		UserEntity userUpdated = this.update(user,request);
		return userMapper.toDto(userUpdated,MESSENGER.UPDATE_INFO);
	}
	
	@Override
	public boolean emailExist(String email) {
		Optional<UserEntity> resutl = userDao.findUserByEmail(email);
		return resutl.isPresent();
	}

	@Override
	public UserEntity findByEmail(String email) {
		Optional<UserEntity> result = userDao.findUserByEmail(email);
		return result.orElseThrow(() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL));
	}

	@Override
	public boolean existsByEmail(String email) {
		Optional<UserEntity> result = userDao.findUserByEmail(email);
		return result.isPresent();

	}

	@Override
	public Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data) throws MessagingException, IOException {
		Map<String, Object> props = new HashMap<>();
		props.put("tokenUrl", tokenUrl);

		DataMailDto dataMail = DataMailDto.builder()
				.to(email)
				.subject(MESSENGER.HEADER_MAIL_PASSWORD)
				.content(tokenUrl)
				.props(props)
				.build();
		

		mailService.sendHtmlMail(dataMail, VIEW.EMAIL_PASSWORD, null);
		
		Map<String, String> result = new HashMap<>();
		result.put("Message", MESSENGER.MAIL_SUCCESS);
		result.put("token", data);
		result.put("type", "Bearer");
		result.put("URL", tokenUrl);
		
		return result;
	}
	

	@Override
	public UserEntity createrUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role) {
		SignupRequest userRegister = signupMapper.toSignupUser(docRequest);
		return this.createrUser(userRegister,role);
	}
	
	
	@Override
	@Transactional
	public UserEntity createrUser(SignupRequest signUpRequest, RoleEntity role) {

		if (this.emailExist(signUpRequest.getEmail())) {
			throw new EmailExistException(MESSENGER_ERROR.USER_EXIST);
		}

		if(passworRegisterdErrors(signUpRequest.getPassword()) != 0) {
			throw new PasswordRegisterErrors(MESSENGER_ERROR.PASSOWRD_REGISTER_ERROR);
		}
		
		Set<RoleEntity> roles = new HashSet<>();
		roles.add(role);
		
		UserEntity user = UserEntity.builder()
				.email(signUpRequest.getEmail())
				.name(signUpRequest.getFullName())
				.genderl(signUpRequest.getGender())
				.password(encoder.encode(signUpRequest.getPassword()))
				.phone(signUpRequest.getPhone())
				.address(signUpRequest.getAddress())
				.dateOfbirth(signUpRequest.getDateOfbirth())
				.createdAt(LocalDateTime.now())
				.active(0)
				.roles(roles)
				.build();
		this.save(user);
		return user;
	}
	
	@Override
	public UserDtoResponse createUserEntity(SignupRequest signUpRequest, RoleEntity role) {
		UserEntity user = this.createrUser(signUpRequest,role);
		return userMapper.toDto(user,MESSENGER.CREATE_USER);
	}

	@Override
	@Transactional
	public UserEntity changingPassword(ChangePasswordRequest request, Session session) {

		String email = jwtUtils.getUserNameFromJwt(session.getData());

		UserEntity entity = this.findByEmail(email);
		entity.setPassword(encoder.encode(request.getPassword()));
		entity.setUpdateAt(LocalDateTime.now());
		this.update(entity);

		sService.delete(session);

		return entity;
	}

	@Override
	public UserDtoResponse lockPatient(Integer id, String reason) {

		String result = "";
		UserEntity userEntity = this.findById(id);
		
		if(id == 1) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_ADMIN);
		}
		
		if(userEntity.getDoctorEntity() != null) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_DOC);
		}
		
		if (userEntity.getActive() == ACTIVE.NONE) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK);
		}else {
			userEntity.setActive(ACTIVE.NONE);
			result = MESSENGER.LOCKED_SUCCESS;
		}
		userEntity.setDescription(reason);
		userEntity.setUpdateAt(LocalDateTime.now());
		this.update(userEntity);

		logger.info(result);

		return userMapper.toDto(userEntity,result);
	}

	@Override
	public UserDtoResponse unlockPatient(Integer id, String reason) {
		String result = "";
		UserEntity userEntity = this.findById(id);
		if (userEntity.getActive() == ACTIVE.ACCEPT) {
			throw new ActiveException(MESSENGER_ERROR.CANT_UNLOCK);
		}else {
			userEntity.setActive(ACTIVE.ACCEPT);
			result = MESSENGER.UNLOCK_SUCCESS;
		}
		userEntity.setDescription(reason);
		userEntity.setUpdateAt(LocalDateTime.now());
		this.update(userEntity);

		logger.info(result);

		return userMapper.toDto(userEntity,result);
		
	}
	
	

	@Override
	public UserDtoPatientResponse getUserInfo(String email) {
		UserEntity entity = this.findByEmail(email);
		return userMapper.toDtoInfo(entity);
		
		
	}
	
	
	@Override
	public UserDtoResponse getInfo(String email) {
		UserEntity entity = this.findByEmail(email);
		return userMapper.toDto(entity, MESSENGER.USER_INFO);
	}
	

	@Override
	public UserEntity createStatus(String email, Statuses status) {
		UserEntity user = this.findByEmail(email);
		user.addStatus(status);
		this.update(user);
		return user;
	}

	@Override
	public JwtResponse userLogin(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken userTryLogin = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(userTryLogin);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwt(authentication);

		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		JwtResponse newJwt = JwtResponse.builder().token(jwt).email(userDetails.getUsername()).type("Bearer").id(userDetails.getId()).roles(roles).build();
		
		return newJwt;
	}


	@Override
	public int passworRegisterdErrors(String password) {
		boolean isNumberInside = false;
		boolean isLowerCaseInside = false;
		boolean isUpperCaseInside = false;
		boolean isSpecialCharacters = false;
		int count = 0;

		for (Character ch : password.toCharArray()) {
			if (Character.isLowerCase(ch))
				isLowerCaseInside = true;
			else if (Character.isUpperCase(ch))
				isUpperCaseInside = true;
			else if (Character.isDigit(ch))
				isNumberInside = true;
			else if (PASSWORD.SPECIAL_CHACTERLIST.contains(ch))
				isSpecialCharacters = true;
		}

		if (!isNumberInside)
			count++;
		if (!isLowerCaseInside)
			count++;
		if (!isUpperCaseInside)
			count++;
		if (!isSpecialCharacters)
			count++;

		return Math.max(count, 8 - password.length());
	}



}
