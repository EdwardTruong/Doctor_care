package com.example.doctorcare.application.service;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.application.service.baseService.CrudService;
import com.example.doctorcare.domain.business.status_schedule.Statuses;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.model.dto.request.SeeDoctorRequest;
import com.example.doctorcare.model.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.model.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.Schedule;

public interface PatientService extends CrudService<Patients> {
	// Patients findById(Integer id);
	// void save(Patients patient);
	// void update(Patients patient);
	// void delete(Patients patient);

	PatientDtoUserAppointmentResponse createPatient(SeeDoctorRequest request, User user, DoctorEntity doctors,
			Statuses statuses, Schedule schedule);

	List<PatientDtoDoctorResponse> listPatientsWithDate(String email, LocalDate date);

	Integer getDoctorIdByEmail(String email);

	PatientDtoDoctorResponse changeStatus(Integer idPatient, String email, int active, String note);

	boolean isPatientExistInDoctorList(Patients patients, Integer idDoc);

	Patients changeStatus(Integer idPatient, Integer status, String note);

	List<PatientDtoAdminResponse> getPatientDtoForAdmin(User user);

	PatientDtoAdminResponse getSimplePatientByUserIdForAdmin(User user, LocalDate date, String time);

	User createStatus(String email, Statuses status);

}
