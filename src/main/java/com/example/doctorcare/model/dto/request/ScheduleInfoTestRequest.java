package com.example.doctorcare.model.dto.request;

import java.util.List;
/*
 * for testing
 */

import lombok.Data;

@Data
public class ScheduleInfoTestRequest {

	Integer placeId;
	Integer specializationId;
	Integer clinicId;
	List<String> pricesList;
}
