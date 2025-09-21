package com.example.doctorcare.application.service.baseService.old.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.old.ActiveException;
import com.example.doctorcare.application.service.baseService.old.AccountService;
import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.infrastructure.common.utils.ERole;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_ERROR;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/*
 * Cả 3 thằng admin, user, docter đều có thể edit acount.
 * Đây AccountServiceImpl sẽ lời nơi xử lý logic cho các role Admin với doctoer từng chức vụ. 
 * 
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {

	UserService userService;
	UserMapper userMapper;

	static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Override
	public UserDtoResponse lockOrUnlock(String email, Integer id, String reason) {

		String result = "";

		User currentUser = userService.findByEmail(email);
		User userLocker = userService.findById(id);

		if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)
				&& userLocker.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_ADMIN);
		}

		if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_DOCTOR)
				&& (userLocker.getRoles().stream()
						.anyMatch(role -> role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_DOCTOR))) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK_ACCOUNT);
		}

		userLocker.setDescription(reason);
		userLocker.setUpdatedAt(LocalDateTime.now());
		userService.activeAccount(userLocker);
		userService.update(userLocker);

		logger.info(result);

		return userMapper.toDto(userLocker, result);
	}

}
