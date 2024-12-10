package com.example.doctorcare.service;

import com.example.doctorcare.model.dto.response.UserDtoResponse;

public interface AccountService {

	/**
	 * Lock patient it mean lock user account.
	 * 
	 * @param id is user id
	 * 
	 * @Param reason is the reason why Admin or Doctor want to lock account.
	 */
	UserDtoResponse lockOrUnlock(String email, Integer id, String reason);

}
