package com.example.doctorcare.dto.request;

import java.time.LocalDate;

import com.example.doctorcare.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NotNull
@Builder
public class ScheduleRequest {

	@NotNull(message = MESSENGER_FIELDS_ERROR.DATE_SCHEDULE)
	LocalDate date;

    @NotNull(message = MESSENGER_FIELDS_ERROR.TIME_SCHEDULE)
	String times;
	
    @NotNull(message = MESSENGER_FIELDS_ERROR.PRICE_ERROR)
    Integer price;
    
    @NotNull(message = MESSENGER_FIELDS_ERROR.DOC_SPECIALIZATION)
    Integer specializationId;
}
