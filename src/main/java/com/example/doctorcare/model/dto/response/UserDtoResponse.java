package com.example.doctorcare.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.doctorcare.common.utils.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * UserDtoResponse is used to return user information and it is used for system responses.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {

	 Integer id; // Not allow showing but for now show it to test funtion success or not.
	 String email;
	 String name;
	 String address;
	 String phone;
	 String gender;
	 String accountDescription;
	 LocalDateTime createdAt;
	 LocalDateTime updateAt;
	 LocalDate dateOfbirth;
	 String isActive;
	 Set<ERole> role;
	 String message;
	 
	 
	
}
