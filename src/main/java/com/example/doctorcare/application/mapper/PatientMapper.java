package com.example.doctorcare.application.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.doctorcare.application.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.application.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.domain.business.status_schedule.Statuses;
import com.example.doctorcare.domain.system.user.User;

import com.example.doctorcare.infrastructure.utils.ApplicationUtils;
import com.example.doctorcare.infrastructure.utils.Const.MESSENGER;
import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.Schedule;

@Component
public class PatientMapper {
	
	@Autowired
	ApplicationUtils appUtils;
	
	public PatientDtoDoctorResponse toDtoForDoctor(Patients patient) {
		// return PatientDtoDoctorResponse.builder()
		// 		.name(patient.getStatus().getUser().getName())
		// 		.genderl(patient.getStatus().getUser().getGenderl())
		// 		.address(patient.getStatus().getUser().getAddress())
		// 		.note(patient.getNote())
		// 		.statusName(patient.getStatus().getName())
		// 		.time(patient.getTime())
		// 		.active(appUtils.converActivePatienToString(patient.getActive()))
		// 		.build();
		return null;
	}
	
	public List<PatientDtoDoctorResponse> toListDto(List<Patients> listPatients){
		return listPatients.stream().map(entity ->toDtoForDoctor(entity)).toList();
	}
	
	public PatientDtoAdminResponse toDtoForAdmin(Patients patient) {
		// return PatientDtoAdminResponse.builder()
		// 		.message(MESSENGER.PATIENT_INFO)
		// 		.userId(patient.getStatus().getUser().getId())
		// 		.userName(patient.getStatus().getUser().getName())
		// 		.email(patient.getStatus().getUser().getEmail())
		// 		.reason(patient.getStatus().getName())
		// 		.nameDoctor(patient.getDoctor().getUser().getName())
		// 		.clinicAddress(patient.getDoctor().getClinic().getAddress())
		// 		.appointmentDate(patient.getDate())
		// 		.time(patient.getTime())
		// 		.price(appUtils.convertToVND(patient.getPrice()))
		// 		.activePatient(appUtils.converActivePatienToString(patient.getActive()))
		// 		.activeUser(appUtils.converActiveUserToString(patient.getStatus().getUser().getActive()))
		// 		.build();
		return null;
	}
	
	public List<PatientDtoAdminResponse> toListDtoForAdmin(List<Patients> listPatients){
		return listPatients.stream().map(entity ->toDtoForAdmin(entity)).toList();
	}
	
	
	
	public PatientDtoUserAppointmentResponse toDtoForUser(DoctorEntity doctor, Schedule schedule, User user,
		Statuses status, Patients newPatien) {
	// return	PatientDtoUserAppointmentResponse.builder()
	// 		.idUser(user.getId())
	// 		.nameUser(user.getName())
	// 		.reason(status.getName())
	// 		.idDoc(doctor.getId())
	// 		.nameDoc(doctor.getUser().getName())
	// 		.idSchedule(schedule.getId())
	// 		.dateAppointment(newPatien.getDate())
	// 		.timeAppointment(newPatien.getTime())
	// 		.price(appUtils.convertToVND(schedule.getPrice()))
	// 		.active(appUtils.converActivePatienToString(newPatien.getActive()))
	// 		.build(); 
	 
	// }	
	return null;
}
}
