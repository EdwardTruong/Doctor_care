package com.example.doctorcare.service;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.model.dto.request.SeeDoctorRequest;
import com.example.doctorcare.model.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.model.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.Schedule;
import com.example.doctorcare.model.entity.Statuses;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.service.baseService.CrudService;

public interface PatientService extends CrudService<Patients> {
	// Patients findById(Integer id);
	// void save(Patients patient);
	// void update(Patients patient);
	// void delete(Patients patient);

	PatientDtoUserAppointmentResponse createPatient(SeeDoctorRequest request, UserEntity user, DoctorEntity doctors,
			Statuses statuses, Schedule schedule);

	List<PatientDtoDoctorResponse> listPatientsWithDate(String email, LocalDate date);

	Integer getDoctorIdByEmail(String email);

	PatientDtoDoctorResponse changeStatus(Integer idPatient, String email, int active, String note);

	boolean isPatientExistInDoctorList(Patients patients, Integer idDoc);

	Patients changeStatus(Integer idPatient, Integer status, String note);

	List<PatientDtoAdminResponse> getPatientDtoForAdmin(UserEntity user);

	PatientDtoAdminResponse getSimplePatientByUserIdForAdmin(UserEntity user, LocalDate date, String time);

	UserEntity createStatus(String email, Statuses status);

}
