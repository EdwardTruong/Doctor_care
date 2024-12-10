package com.example.doctorcare.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorWithSchedulesResponse {

	Integer idDoctor; //show for testing (Lưu ý: Đi kèm với tham số là trường id của bác sĩ.)
	
	String nameDoctor;

	String email;
	
	String nameClinic;
	
	String active;
	
	String description;
	
	List<ScheduleDtoResponse> listSchdulesOfDoctor;
}
