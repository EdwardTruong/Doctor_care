package com.example.doctorcare.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.common.utils.Const.ACTIVE;
import com.example.doctorcare.common.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.exception.ActiveException;
import com.example.doctorcare.exception.notfound.PatientNotFoundException;
import com.example.doctorcare.model.dto.request.SeeDoctorRequest;
import com.example.doctorcare.model.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.model.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.Schedule;
import com.example.doctorcare.model.entity.Statuses;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.model.mapper.PatientMapper;
import com.example.doctorcare.repository.PatientRepository;
import com.example.doctorcare.service.PatientService;
import com.example.doctorcare.service.AccountService;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientServiceImpl implements PatientService {

	PatientRepository patientRepository;

	PatientMapper patientMapper;

	AccountService accountService;

	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

	@Override
	public Patients findById(Integer id) {
		Optional<Patients> result = patientRepository.findById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional
	public void create(Patients patient) {
		patientRepository.save(patient);
	}

	@Override
	@Transactional
	public Patients update(Patients patient) {
		return patientRepository.saveAndFlush(patient);
	}

	@Override
	@Transactional
	public void delete(Patients patient) {
		patientRepository.delete(patient);
	}

	@Override
	public PatientDtoUserAppointmentResponse createPatient(SeeDoctorRequest request, UserEntity user,
			DoctorEntity doctors, Statuses statuses, Schedule schedule) {
		Patients newPatient = Patients.builder()
				.date(schedule.getDate())
				.time(request.getTime())
				.active(ACTIVE.NONE)
				.status(statuses)
				.doctor(doctors)
				.date(schedule.getDate())
				.price(schedule.getPrice())
				.build();
		this.create(newPatient);

		return patientMapper.toDtoForUser(doctors, schedule, user, statuses, newPatient);
	}

	@Override
	public Integer getDoctorIdByEmail(String email) {
		UserEntity user = userService.findByEmail(email);
		return user.getDoctorEntity().getId();
	}

	@Override
	public List<PatientDtoDoctorResponse> listPatientsWithDate(String email, LocalDate date) {
		Integer idDoc = this.getDoctorIdByEmail(email);
		logger.info("Load list patients : docterId : " + String.valueOf(idDoc) + " === LocalDate : " + date.toString());
		List<Patients> patients = patientRepository.listPatientsOfDoctor(idDoc, date);
		return patientMapper.toListDto(patients);
	}

	// Using for doctor
	@Override
	public PatientDtoDoctorResponse changeStatus(Integer idPatient, String email, int active, String note) {

		if (!this.correctActive(active)) {
			throw new ActiveException(MESSENGER_ERROR.ACTIVE_ERROR);
		}

		Integer idDoc = this.getDoctorIdByEmail(email);
		Patients patient = this.findById(idPatient);

		if (!this.isPatientExistInDoctorList(patient, idDoc)) {
			throw new PatientNotFoundException(MESSENGER_ERROR.PATIENTS_NOT_BELOW_DOC);
		}

		patient.setNote(note);
		patient.setActive(active);
		this.update(patient);
		return patientMapper.toDtoForDoctor(patient);
	}

	// Using for Admin
	@Override
	public Patients changeStatus(Integer idPatient, Integer active, String note) {
		if (!this.correctActive(active)) {
			throw new ActiveException(MESSENGER_ERROR.ACTIVE_ERROR);
		}

		Patients patient = this.findById(idPatient);
		patient.setActive(active);
		patient.setNote(note);
		this.update(patient);
		return patient;
	}

	@Override
	public boolean isPatientExistInDoctorList(Patients patients, Integer idDoc) {
		if (patients.getDoctor().getId() != idDoc) {
			throw new PatientNotFoundException(MESSENGER_ERROR.PATIENTS_NOT_BELOW_DOC);
		}
		return true;
	}

	public boolean correctActive(int active) {
		return (active >= ACTIVE.DENICE || active <= ACTIVE.ACCEPT);
	}

	@Override
	public List<PatientDtoAdminResponse> getPatientDtoForAdmin(UserEntity user) {
		List<Patients> listPatients = patientRepository.listPatientsByUserId(user.getId());
		return patientMapper.toListDtoForAdmin(listPatients);
	}

	@Override
	public PatientDtoAdminResponse getSimplePatientByUserIdForAdmin(UserEntity user, LocalDate date, String time) {
		Patients patient = patientRepository.getPatientsByUserIdAndDate(user.getId(), date, time);
		return patientMapper.toDtoForAdmin(patient);
	}

	@Override
	public UserEntity createStatus(String email, Statuses status) {
		UserEntity user = userService.findByEmail(email);
		user.addStatus(status);
		userService.update(user);
		return user;
	}

	@Override
	public List<Patients> findAll() {
		return patientRepository.findAll();
	}

}
