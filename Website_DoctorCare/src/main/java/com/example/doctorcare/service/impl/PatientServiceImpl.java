package com.example.doctorcare.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.PatientRepository;
import com.example.doctorcare.dto.request.SeeDoctorRequest;
import com.example.doctorcare.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.dto.response.PatientDtoDoctorResponse;
import com.example.doctorcare.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Patients;
import com.example.doctorcare.entity.Schedule;
import com.example.doctorcare.entity.Statuses;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.exception.ActiveException;
import com.example.doctorcare.exception.PatientNotFoundException;
import com.example.doctorcare.mapper.PatientMapper;
import com.example.doctorcare.service.PatientService;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.ApplicationUtils;
import com.example.doctorcare.utils.Const.ACTIVE;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;

import jakarta.transaction.Transactional;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientRepository pDao;
	
	@Autowired
	PatientMapper patientMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpecializationService specializationService;
	
	@Autowired
	ApplicationUtils appUtils;

	private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

	
	@Override
	public Patients findById(Integer id) {
		Optional<Patients> result = pDao.findById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional
	public void save(Patients patient) {
		pDao.save(patient);
	}

	@Override
	@Transactional
	public void update(Patients patient) {
		pDao.saveAndFlush(patient);
	}

	@Override
	@Transactional
	public void delete(Patients patient) {
		pDao.delete(patient);
	}

	@Override
	public PatientDtoUserAppointmentResponse createPatient(SeeDoctorRequest request,UserEntity user, DoctorEntity doctors, Statuses statuses,Schedule schedule) {
		Patients newPatient = Patients.builder()
				.date(schedule.getDate())
				.time(request.getTime())
				.active(ACTIVE.NONE)
				.status(statuses)
				.doctor(doctors)
				.date(schedule.getDate())
				.price(schedule.getPrice())
				.build();
		this.save(newPatient);
		
		return 	patientMapper.toDtoForUser(doctors,schedule,user,statuses, newPatient);
	}
	
	@Override
	public Integer getDoctorIdByEmail(String email) {
		UserEntity user = userService.findByEmail(email);
		return user.getDoctorEntity().getId();
	}
	

	@Override
	public List<PatientDtoDoctorResponse> listPatientsWithDate(String email, LocalDate date) {
		Integer idDoc = this.getDoctorIdByEmail(email);
		logger.info("Load list patients : docterId : "+ String.valueOf(idDoc) +" === LocalDate : " + date.toString());
		List<Patients> patients = pDao.listPatientsOfDoctor(idDoc,date);
		return patientMapper.toListDto(patients);
	}


	// Using for doctor
	@Override
	public PatientDtoDoctorResponse changeStatus(Integer idPatient,String email, int active, String note) {
		
		if(!this.correctActive(active)) {
			throw new ActiveException(MESSENGER_ERROR.ACTIVE_ERROR);
		}
		
		Integer idDoc = this.getDoctorIdByEmail(email);
		Patients patient = this.findById(idPatient);
		
		if(!this.isPatientExistInDoctorList(patient,idDoc)) {
			throw new PatientNotFoundException(MESSENGER_ERROR.PATIENTS_NOT_BELOW_DOC);
		}

		patient.setNote(note);
		patient.setActive(active);
		this.update(patient);
		return patientMapper.toDtoForDoctor(patient);
	}

	// Using for Admin
	@Override
	public Patients changeStatus(Integer idPatient,Integer active,String note) {
		if(!this.correctActive(active)) {
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
		if(patients.getDoctor().getId() != idDoc) {
			throw new PatientNotFoundException(MESSENGER_ERROR.PATIENTS_NOT_BELOW_DOC);
		}
		return true;
	}


	public boolean correctActive(int active) {
		return (active >= ACTIVE.DENICE || active <=ACTIVE.ACCEPT);
	}
	
	@Override
	public List<PatientDtoAdminResponse> getPatientDtoForAdmin(UserEntity user) {
		List<Patients> listPatients = pDao.listPatientsByUserId(user.getId());
		return patientMapper.toListDtoForAdmin(listPatients);
	}

	@Override
	public PatientDtoAdminResponse getSimplePatientByUserIdForAdmin(UserEntity user, LocalDate date, String time) {
		Patients patient = pDao.getPatientsByUserIdAndDate(user.getId(),date,time);
		return patientMapper.toDtoForAdmin(patient);
	}


}
