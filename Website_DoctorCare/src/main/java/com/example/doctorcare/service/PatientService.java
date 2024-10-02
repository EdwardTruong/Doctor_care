package com.example.doctorcare.service;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.dto.request.SeeDoctorRequest;
import com.example.doctorcare.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Patients;
import com.example.doctorcare.entity.Schedule;
import com.example.doctorcare.entity.Statuses;
import com.example.doctorcare.entity.UserEntity;

public interface PatientService {
	Patients findById(Integer id);
	void save(Patients patient);
	void update(Patients patient);
	void delete(Patients patient);
	
	PatientDtoUserAppointmentResponse createPatient(SeeDoctorRequest request, UserEntity user, DoctorEntity doctors,Statuses statuses,Schedule schedule);
	List<PatientDtoDoctorResponse> listPatientsWithDate(String email, LocalDate date);
	Integer getDoctorIdByEmail(String email);
	PatientDtoDoctorResponse changeStatus(Integer idPatient, String email,int active,String note);
	boolean isPatientExistInDoctorList(Patients patients,Integer idDoc);
	Patients changeStatus(Integer idPatient, Integer status, String note);
	
	List<PatientDtoAdminResponse> getPatientDtoForAdmin(UserEntity user);
	PatientDtoAdminResponse getSimplePatientByUserIdForAdmin(UserEntity user, LocalDate date, String time);
	
}
