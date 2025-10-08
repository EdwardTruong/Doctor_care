package com.example.doctorcare.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.doctorcare.model.entity.DoctorEntity;
import lombok.Builder;

@Builder
public record DoctorDtoResponse (
	String message,
	Integer userId, // Show it for testing
	Integer docId,	// Show it for testing
	String docName,
	String docEmail,
	LocalDate docDob,
	LocalDateTime createAt,
	String gender,
	String phone,
	String address,
	String achievement,
	String docDescription,
	String trainingProcess,
	String active,
	String accountDescription,
	String clinicName,
	Set<String> specializationsName
)  {
	DoctorDtoResponse toDto(DoctorEntity entity) {
		return DoctorDtoResponse.builder()
				.userId(entity.getUser().getId())
				.docId(entity.getId())
				.docName(entity.getUser().getName())
				.docEmail(entity.getUser().getEmail())
				.docDob(entity.getUser().getDateOfBirth())
				.createAt(entity.getCreatedAt())
				.gender(entity.getUser().getGender())
				.phone(entity.getUser().getPhone())
				.address(entity.getUser().getAddress())
				.achievement(entity.getAchievement())
				.docDescription(entity.getDescription())
				.trainingProcess(entity.getTrainingProcess())
				.clinicName(entity.getClinic().getName())
				.specializationsName(entity.getSpecializations().stream()
						.map(specialization -> specialization.getName())
						.collect(Collectors.toSet()))
				.build();
	}

}
