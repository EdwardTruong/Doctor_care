package com.example.doctorcare.auth.service.impl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.baseService.old.dao.UserRepository;
import com.example.doctorcare.auth.exception.UserNotFoundException;
import com.example.doctorcare.core.security.CustomUserDetails;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.infrastructure.common.utils.Const.*;

/*
 * 	UserDetailsService have only one method loadUserByUsername. 
 * 	When user login, user's information saved in UserDetailsCustom;
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userDao;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User User = userDao.findUserByEmail(username)
				.orElseThrow(() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL + username));
		logger.info("User login and save user's infomations into UserDetailsCustom. !");
		return new CustomUserDetails(User, Collections.emptyList());
	}


}
