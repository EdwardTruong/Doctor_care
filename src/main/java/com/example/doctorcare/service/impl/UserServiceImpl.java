package com.example.doctorcare.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.UserRepository;
import com.example.doctorcare.model.dto.DataMailDto;
import com.example.doctorcare.model.dto.request.ChangePasswordRequest;
import com.example.doctorcare.model.dto.request.LoginRequest;
import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.dto.request.SignupRequest;
import com.example.doctorcare.model.dto.request.UserUpdateRequest;
import com.example.doctorcare.model.dto.response.JwtResponse;
import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.model.entity.Session;
import com.example.doctorcare.model.entity.Statuses;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.model.mapper.RequestMapper;
import com.example.doctorcare.model.mapper.UserMapper;
import com.example.doctorcare.exception.ActiveException;
import com.example.doctorcare.exception.EmailExistException;
import com.example.doctorcare.exception.PasswordRegisterErrors;
import com.example.doctorcare.exception.UserNotFoundException;
import com.example.doctorcare.security.custom.UserDetailsCustom;
import com.example.doctorcare.security.jwt.JwtUtils;
import com.example.doctorcare.service.MailService;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.Const.ACTIVE;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.utils.Const.PASSWORD;
import com.example.doctorcare.utils.Const.VIEW;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

	UserRepository userDao;

	AuthenticationManager authenticationManager;

	JwtUtils jwtUtils;

	MailService mailService;

	PasswordEncoder encoder;

	SessionService sService;

	UserMapper userMapper;

	RequestMapper signupMapper;


	@Override
	public List<UserDtoResponse> loadAll() {
		return userDao.findAllAndDeleted(false).stream()
				.map(user -> userMapper.toDto(user, MESSENGER.SUCCESS)).toList();
	}

	@Override
	public UserDtoResponse findById(Integer id) {
		Optional<UserEntity> result = userDao.findByIdAndDeleted(id, false);
		if (result.isPresent()) {
			return userMapper.toDto(result.get(), MESSENGER.SUCCESS + ": (" + id + ").");
		} else {
			throw new UserNotFoundException(
					MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + id + ").");
		}
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
		entity.setGender(request.getGender());
		entity.setDateOfBirth(request.getDateOfBirth());
		entity.setUpdatedAt(LocalDateTime.now());
		return userDao.saveAndFlush(entity);
	}

	@Override
	public void delete(UserEntity entity) {
		Optional<UserEntity> userFound = userDao.findById(entity.getId());

		if (userFound.isEmpty()) {
			throw new UserNotFoundException(
					MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + entity.getId() + ").");
		}

		UserEntity user = userFound.get();

		if (Boolean.TRUE.equals(user.getDeleted())) {
			throw new UserNotFoundException(
					MESSENGER_NOT_FOUND.ENTITY_DELETED + ": (" + entity.getId() + ").");
		}

		user.setDeleted(true);
		userDao.save(user);

	}

	@Override
	public UserDtoResponse updateUser(String email, UserUpdateRequest request) {
		UserEntity user = this.findByEmail(email);
		UserEntity userUpdated = this.update(user, request);
		return userMapper.toDto(userUpdated, MESSENGER.UPDATE_INFO);
	}

	@Override
	public boolean emailExist(String email) {
		Optional<UserEntity> result = userDao.findUserByEmail(email);
		return result.isPresent();
	}

	@Override
	public UserEntity findByEmail(String email) {
		Optional<UserEntity> result = userDao.findUserByEmail(email);
		return result.orElseThrow(
				() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL));
	}

	@Override
	public boolean existsByEmail(String email) {
		Optional<UserEntity> result = userDao.findUserByEmail(email);
		return result.isPresent();

	}

	@Override
	public Map<String, String> sendEmailRestPassword(String email, String tokenUrl, String data)
			throws MessagingException, IOException {
		Map<String, Object> props = new HashMap<>();
		props.put("tokenUrl", tokenUrl);

		DataMailDto dataMail = DataMailDto.builder().to(email)
				.subject(MESSENGER.HEADER_MAIL_PASSWORD).content(tokenUrl).props(props).build();

		mailService.sendHtmlMail(dataMail, VIEW.EMAIL_PASSWORD, null);

		Map<String, String> result = new HashMap<>();
		result.put("Message", MESSENGER.MAIL_SUCCESS);
		result.put("token", data);
		result.put("type", "Bearer");
		result.put("URL", tokenUrl);

		return result;
	}

	@Override
	public UserEntity createUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role) {
		SignupRequest userRegister = signupMapper.toSignupUser(docRequest);
		return this.createUser(userRegister, role);
	}

	@Override
	@Transactional
	public UserEntity createUser(SignupRequest signUpRequest, RoleEntity role) {

		if (this.emailExist(signUpRequest.getEmail())) {
			throw new EmailExistException(MESSENGER_ERROR.USER_EXIST);
		}

		if (passwordRegisteredErrors(signUpRequest.getPassword()) != 0) {
			throw new PasswordRegisterErrors(MESSENGER_ERROR.PASSWORD_REGISTER_ERROR);
		}

		Set<RoleEntity> roles = new HashSet<>();
		roles.add(role);

		UserEntity user = UserEntity.builder().email(signUpRequest.getEmail())
				.name(signUpRequest.getFullName()).gender(signUpRequest.getGender())
				.password(encoder.encode(signUpRequest.getPassword()))
				.phone(signUpRequest.getPhone()).address(signUpRequest.getAddress())
				.dateOfBirth(signUpRequest.getDateOfBirth()).createdAt(LocalDateTime.now())
				.active(0).roles(roles).build();
		this.save(user);
		return user;
	}

	@Override
	public UserDtoResponse createUserEntity(SignupRequest signUpRequest, RoleEntity role) {
		UserEntity user = this.createUser(signUpRequest, role);
		return userMapper.toDto(user, MESSENGER.CREATE_USER);
	}

	@Override
	@Transactional
	public UserEntity changingPassword(ChangePasswordRequest request, Session session)
			throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		String email = jwtUtils.getUserNameFromJwt(session.getData());

		UserEntity entity = this.findByEmail(email);
		entity.setPassword(encoder.encode(request.getPassword()));
		entity.setUpdatedAt(LocalDateTime.now());
		this.update(entity);

		sService.delete(session);

		return entity;
	}

	@Override
	public UserDtoResponse lockPatient(Integer id, String reason) {

		String result = "";
		UserEntity userFound =
				userDao.findByIdAndDeleted(id, false).orElseThrow(() -> new UserNotFoundException(
						MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + id + ")."));


		if (id == 1) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_ADMIN);
		}

		if (userFound.getDoctorEntity() != null) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_DOC);
		}

		if (userFound.getActive() == ACTIVE.NONE) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK);
		} else {
			userFound.setActive(ACTIVE.NONE);
			result = MESSENGER.LOCKED_SUCCESS;
		}
		userFound.setDescription(reason);
		userFound.setUpdatedAt(LocalDateTime.now());
		this.update(userFound);

		log.info(result);

		return userMapper.toDto(userFound, result);
	}

	@Override
	public UserDtoResponse unlockPatient(Integer id, String reason) {
		String result = "";
		UserEntity userFound =
				userDao.findByIdAndDeleted(id, false).orElseThrow(() -> new UserNotFoundException(
						MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + id + ")."));
		if (userFound.getActive() == ACTIVE.ACCEPT) {
			throw new ActiveException(MESSENGER_ERROR.CANT_UNLOCK);
		} else {
			userFound.setActive(ACTIVE.ACCEPT);
			result = MESSENGER.UNLOCK_SUCCESS;
		}
		userFound.setDescription(reason);
		userFound.setUpdatedAt(LocalDateTime.now());
		this.update(userFound);

		log.info(result);

		return userMapper.toDto(userFound, result);

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
	public JwtResponse userLogin(LoginRequest loginRequest)
			throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		UsernamePasswordAuthenticationToken userTryLogin = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(userTryLogin);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwt(authentication);

		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		JwtResponse newJwt = JwtResponse.builder().token(jwt).email(userDetails.getUsername())
				.type("Bearer").id(userDetails.getId()).roles(roles).build();

		return newJwt;
	}

	@Override
	public int passwordRegisteredErrors(String password) {
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
