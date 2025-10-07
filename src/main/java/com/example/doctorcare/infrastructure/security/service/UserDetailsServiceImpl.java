package com.example.doctorcare.infrastructure.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.core.security.CustomUserDetails;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.utils.Const.*;
import lombok.RequiredArgsConstructor;

/*
 * 	UserDetailsService have only one method loadUserByUsername. 
 * 	When user login, user's information saved in UserDetailsCustom;
 */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserPermissionService userPermissionService;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	/**
	 * Load user by username (email) with full Role-Based Access Control.
	 * This method loads the user along with all roles and permissions
	 * for comprehensive authorization support.
	 * 
	 * @param username The username (email address)
	 * @return CustomUserDetails with full RBAC permissions
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailAndDeleted(username, false)
				.orElseThrow(() -> new EntityNotFoundException(User.class, MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL + username));
				
		logger.info("Loading user with enhanced RBAC permissions: {}", username);
		
		// Build CustomUserDetails with comprehensive permissions
		return CustomUserDetails.build(user, userPermissionService);
	}

}
