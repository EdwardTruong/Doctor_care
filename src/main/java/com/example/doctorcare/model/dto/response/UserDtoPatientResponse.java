package com.example.doctorcare.model.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.model.entity.Statuses;

import lombok.Builder;

/*
 * The UserDtoPatientResponse used to return personal information and list of diseases that the user
 * has registered for examination
 */

@Builder
public record UserDtoPatientResponse(String name,

		String email,

		String address,

		String gender,

		String phone,

		String avatar,

		LocalDate dateOfBirth,

		String isActive,

		List<Statuses> statuses

) {
}
