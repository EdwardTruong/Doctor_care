package com.example.doctorcare.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.exception.UserNotFoundException;
import com.example.doctorcare.auth.security.custom.UserDetailsCustom;
import com.example.doctorcare.common.utils.Const.*;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.repository.UserRepository;

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
		UserEntity userEntity = userDao.findUserByEmail(username)
				.orElseThrow(() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL + username));
		logger.info("User login and save user's infomations into UserDetailsCustom. !");
		return UserDetailsCustom.build(userEntity);
	}

}
