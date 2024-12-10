package com.example.doctorcare.model.dto.request;

import java.time.LocalDate;

import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScheduleInfoRequest {


	@NotBlank(message = MESSENGER_FIELDS_ERROR.PLACE_ERROR)
	String searchString;
	
	LocalDate date;
	Integer maxPrice;
	Integer minPrice;
	

}
