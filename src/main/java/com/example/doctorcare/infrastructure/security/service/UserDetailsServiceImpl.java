package com.example.doctorcare.infrastructure.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.security.old.auth.security.custom.UserDetailsCustom;
import com.example.doctorcare.infrastructure.utils.Const.*;

/*
 * 	UserDetailsService have only one method loadUserByUsername. 
 * 	When user login, user's information saved in UserDetailsCustom;
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User User = userRepository.findByEmailAndDeletedFalse(username)
				.orElseThrow(() -> new EntityNotFoundException(User.class, MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL + username));
		logger.info("User login and save user's infomations into UserDetailsCustom. !");
		return UserDetailsCustom.build(User);
	}

}
