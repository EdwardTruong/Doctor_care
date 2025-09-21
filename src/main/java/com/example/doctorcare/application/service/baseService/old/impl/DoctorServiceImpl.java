package com.example.doctorcare.application.service.baseService.old.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.application.exception.notfound.DoctorNotFoundException;
import com.example.doctorcare.application.exception.old.ActiveException;
import com.example.doctorcare.application.service.baseService.old.AccountService;
import com.example.doctorcare.application.service.baseService.old.DoctorService;
import com.example.doctorcare.application.service.baseService.old.MailService;
import com.example.doctorcare.application.service.baseService.old.ScheduleService;
import com.example.doctorcare.application.service.baseService.old.SpecializationService;
import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.infrastructure.common.utils.Const.ACTIVE;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.infrastructure.common.utils.Const.VIEW;
import com.example.doctorcare.model.dto.DataMailDto;
import com.example.doctorcare.model.dto.DoctorDto;
import com.example.doctorcare.model.dto.request.DoctorUpdateRequest;
import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.dto.request.UserUpdateRequest;
import com.example.doctorcare.model.dto.response.DoctorDtoResponse;
import com.example.doctorcare.model.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.model.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.model.entity.User;
import com.example.doctorcare.model.mapper.DoctorMapper;
import com.example.doctorcare.model.mapper.RequestMapper;
import com.example.doctorcare.model.mapper.ScheduleMapper;
import com.example.doctorcare.repository.DoctorRepository;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorServiceImpl implements DoctorService {

	DoctorRepository doctorRepository;

	SpecializationService specializationService;

	DoctorMapper doctorMapper;

	UserService userService;

	RequestMapper requestMapper;

	MailService mailService;

	ScheduleService scheduleService;

	private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

	@Override
	public Doctor findById(Integer idDoctor) {
		Optional<Doctor> result = doctorRepository.findById(idDoctor);
		return result.orElseThrow(() -> new DoctorNotFoundException(MESSENGER_NOT_FOUND.DOCTOR_NOT_FOUND + idDoctor));
	}

	@Override
	public void save(Doctor docter) {
		doctorRepository.save(docter);
	}

	@Override
	public void update(Doctor docter) {
		doctorRepository.saveAndFlush(docter);

	}

	@Override
	public DoctorDtoResponse createNewDoctor(SignupDoctorRequest request, User user,
			Set<Specializations> specializations, Clinics clinic) {
		Doctor newDoctor = new Doctor();
		// newDoctor.setCreateAt(user.getCreatedAt());
		newDoctor.setUser(user);
		newDoctor.setAchievement(request.getAchievement());
		newDoctor.setTrainingProcess(request.getTrainingProcess());
		newDoctor.setDescription(request.getDescription());
		newDoctor.setClinic(clinic);
		newDoctor.setSpecializations(specializations);
		this.save(newDoctor);
		logger.info(MESSENGER.CREATE_DOCTOR);
		return doctorMapper.toDto(newDoctor, MESSENGER.CREATE_DOCTOR);
	}

	@Override
	public List<DoctorDto> getlistDocSaveSpecializationWithSchedule(String nameSpecialization, LocalDate date) {
		Specializations specialization = specializationService.findByName(nameSpecialization);
		specialization.setView(specialization.getView() + 1);
		specializationService.update(specialization);
		return doctorRepository.listDoctorSpecializationWithSchedule(specialization.getName(), date);
	}

	@Override
	public DoctorDtoResponse getDoctorInfo(User entity) {
		return doctorMapper.toDto(entity.getDoctorEntity(), MESSENGER.DOCTOR_INFO);
	}

	@Override
	public DoctorWithSchedulesResponse getDoctorDtoWithScheduleDtoForAdmin(Doctor doctor) {
		List<ScheduleDtoResponse> listSchdulesOfDoctor = scheduleService.getAllSchedulesByDocId(doctor);
		return doctorMapper.toDoctorDtoWithSchedulesDtoForAdmin(doctor, listSchdulesOfDoctor);
	}

	@Override
	public DoctorDtoResponse updateDoctor(String email, DoctorUpdateRequest request) {
		UserUpdateRequest userRequest = requestMapper.toUserEditRequest(request);
		User user = userService.findByEmail(email);
		Doctor doc = user.getDoctorEntity();
		doc.setDescription(request.getDescription());
		doc.setAchievement(request.getAchievement());
		doc.setTrainingProcess(request.getTrainingProcess());
		doc.setUpdatedAt(LocalDateTime.now());
		userService.update(user, userRequest);

		DoctorDtoResponse result = doctorMapper.toDto(doc, MESSENGER.UPDATE_INFO);

		return result;
	}

	@Override
	public String doctorSendEmail(String docEmail, User toUser, MultipartFile file)
			throws MessagingException, IOException {

		User docUser = userService.findByEmail(docEmail);

		Map<String, Object> props = new HashMap<>();
		props.put("head", MESSENGER.HEADER_MAIL_DOC);
		props.put("doctorName", docUser.getName());
		props.put("patientName", toUser.getName());

		DataMailDto dataMail = DataMailDto.builder()
				.from(docUser.getEmail())
				.to(toUser.getEmail())
				.subject(MESSENGER.HEADER_MAIL_DOC)
				.props(props)
				.build();

		mailService.sendHtmlMail(dataMail, VIEW.RESULT_PDF, file);

		return MESSENGER.MAIL_SUCCESS;
	}

	@Override
	public DoctorDtoResponse lockDoc(Integer id, String reason) {
		Doctor doc = this.findById(id);
		String result = "";
		User user = doc.getUser();

		if (user.getActive() == ACTIVE.NONE) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK);
		} else {
			doc.getUser().setActive(ACTIVE.NONE);
			result = MESSENGER.LOCKED_SUCCESS;
		}
		user.setDescription(reason);
		user.setUpdatedAt(LocalDateTime.now());
		userService.update(user);

		return doctorMapper.toDto(doc, result);
	}

	@Override
	public DoctorDtoResponse unlockDoc(Integer id, String reason) {
		Doctor doc = this.findById(id);
		String result = "";
		User user = doc.getUser();
		if (user.getActive() == ACTIVE.ACCEPT) {
			throw new ActiveException(MESSENGER_ERROR.CANT_UNLOCK);
		} else {
			user.setActive(ACTIVE.ACCEPT);
			result = MESSENGER.UNLOCK_SUCCESS;
		}
		user.setDescription(reason);
		user.setUpdatedAt(LocalDateTime.now());
		userService.update(user);

		logger.info(result);
		return doctorMapper.toDto(doc, result);
	}

	/*
	 * For testing
	 */

	@Override
	public List<Doctor> getListDoctor(List<Integer> idsDoctor) {
		List<Doctor> listDoctors = new ArrayList<>();
		for (Integer idDoctor : idsDoctor) {
			Doctor doctorEntity = this.findById(idDoctor);
			if (doctorEntity.getUser().getActive() != 0) {
				listDoctors.add(doctorEntity);
			}
		}
		return listDoctors;
	}
}
