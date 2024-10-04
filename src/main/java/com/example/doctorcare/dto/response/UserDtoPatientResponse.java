package com.example.doctorcare.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.entity.Statuses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * The UserDtoPatientResponse used to return personal information 
 * 	and list of diseases that the user has registered for examination
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDtoPatientResponse {

	String name;

	String email;

	String address;

	String gender;

	String phone;

	String avatar;

	LocalDate dateOfbirth;

	String isActive;

	List<Statuses> statuses;

}
