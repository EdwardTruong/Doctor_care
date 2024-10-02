package com.example.doctorcare.dto.request;

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
