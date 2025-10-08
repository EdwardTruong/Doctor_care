package com.example.doctorcare.model.mapper;

 import java.util.List;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
import com.example.doctorcare.model.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.model.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.Schedule;
import com.example.doctorcare.model.entity.Statuses;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.utils.ApplicationUtils;
 import com.example.doctorcare.utils.Const.MESSENGER;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

 @Component
 @RequiredArgsConstructor
 @FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
 public class PatientMapper {
	
 	ApplicationUtils appUtils;
	
 	public PatientDtoDoctorResponse toDtoForDoctor(Patients patient) {
 		return PatientDtoDoctorResponse.builder()
 				.name(patient.getStatus().getUser().getName())
 				.gender(patient.getStatus().getUser().getGender())
 				.address(patient.getStatus().getUser().getAddress())
 				.note(patient.getNote())
 				.statusName(patient.getStatus().getName())
 				.time(patient.getTime())
 				.active(appUtils.convertActivePatineToString(patient.getActive()))
 				.build();
		
 	}
	
 	public List<PatientDtoDoctorResponse> toListDto(List<Patients> listPatients){
 		return listPatients.stream().map(entity ->toDtoForDoctor(entity)).toList();
 	}
	
 	public PatientDtoAdminResponse toDtoForAdmin(Patients patient) {
 		return PatientDtoAdminResponse.builder()
 				.message(MESSENGER.PATIENT_INFO)
 				.userId(patient.getStatus().getUser().getId())
 				.userName(patient.getStatus().getUser().getName())
 				.email(patient.getStatus().getUser().getEmail())
 				.reason(patient.getStatus().getName())
 				.nameDoctor(patient.getDoctor().getUser().getName())
 				.clinicAddress(patient.getDoctor().getClinic().getAddress())
 				.appointmentDate(patient.getDate())
 				.time(patient.getTime())
 				.price(appUtils.convertToVND(patient.getPrice()))
 				.activePatient(appUtils.convertActivePatineToString(patient.getActive()))
 				.activeUser(appUtils.converActiveUserToString(patient.getStatus().getUser().getActive()))
 				.build();
 	}
	
 	public List<PatientDtoAdminResponse> toListDtoForAdmin(List<Patients> listPatients){
 		return listPatients.stream().map(this::toDtoForAdmin).toList();
 	}
	
	
	
 	public PatientDtoUserAppointmentResponse toDtoForUser(DoctorEntity doctor, Schedule schedule, UserEntity user,
 		Statuses status, Patients newPatien) {
 	return	PatientDtoUserAppointmentResponse.builder()
 			.idUser(user.getId())
 			.nameUser(user.getName())
 			.reason(status.getName())
 			.idDoc(doctor.getId())
 			.nameDoc(doctor.getUser().getName())
 			.idSchedule(schedule.getId())
 			.dateAppointment(newPatien.getDate())
 			.timeAppointment(newPatien.getTime())
 			.price(appUtils.convertToVND(schedule.getPrice()))
 			.active(appUtils.convertActivePatineToString(newPatien.getActive()))
 			.build(); 
	 
 	}	
	
 }
