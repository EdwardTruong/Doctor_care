package com.example.doctorcare.application.service.schedule.query;

import java.time.LocalDate;

import com.example.doctorcare.infrastructure.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;

public record GetScheduleInfoQuery(
		@NotBlank(message = MESSENGER_FIELDS_ERROR.PLACE_ERROR) String searchString,

		LocalDate datedate, Integer maxPrice, Integer minPrice) {


}
