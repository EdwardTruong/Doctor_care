package com.example.doctorcare.service.impl;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.dao.DoctorRepository;
import com.example.doctorcare.dto.DataMailDto;
import com.example.doctorcare.dto.DoctorDto;
import com.example.doctorcare.dto.request.DoctorUpdateRequest;
import com.example.doctorcare.dto.request.SignupDoctorRequest;
import com.example.doctorcare.dto.request.UserUpdateRequest;
import com.example.doctorcare.dto.response.DoctorDtoResponse;
import com.example.doctorcare.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.entity.Clinics;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Specializations;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.exception.ActiveException;
import com.example.doctorcare.exception.DoctorNoFoundException;
import com.example.doctorcare.mapper.DoctorMapper;
import com.example.doctorcare.mapper.RequestMapper;
import com.example.doctorcare.mapper.ScheduleMapper;
import com.example.doctorcare.service.DoctorService;
import com.example.doctorcare.service.MailService;
import com.example.doctorcare.service.ScheduleService;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.Const.ACTIVE;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.utils.Const.VIEW;

import jakarta.mail.MessagingException;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorRepository dDao;

	@Autowired
	SpecializationService specializationService;
	
	@Autowired
	DoctorMapper docMapper;
	
	@Autowired
	DoctorMapper doctorMapper;
	
	@Autowired
	ScheduleMapper schduleMapper;	
	
	@Autowired
	UserService userService;
	
	@Autowired
	RequestMapper requestMapper;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	ScheduleService scheduleService;
	
	private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

	
	@Override
	public DoctorEntity findById(Integer idDoctor) {
		Optional<DoctorEntity> result = dDao.findById(idDoctor);
		return result.orElseThrow(()-> new DoctorNoFoundException(MESSENGER_NOT_FOUND.DOCTOR_NOT_FOUND + idDoctor));
	}

	@Override
	public void save(DoctorEntity docter) {
		 dDao.save(docter);
	}

	@Override
	public void update(DoctorEntity docter) {
		dDao.saveAndFlush(docter);
		
	}

	@Override
	public DoctorDtoResponse createNewDoctor(SignupDoctorRequest request,UserEntity user, Set<Specializations> specializations, Clinics clinic) {
		DoctorEntity newDoctor = new DoctorEntity();
		newDoctor.setCreateAt(user.getCreatedAt());
		newDoctor.setUser(user);
		newDoctor.setAchievement(request.getAchievement());
		newDoctor.setTrainingProcess(request.getTrainingProcess());
		newDoctor.setDescription(request.getDescription());
		newDoctor.setClinic(clinic);
		newDoctor.setSpecializations(specializations);
		this.save(newDoctor);	
		logger.info(MESSENGER.CREATE_DOCTOR);
		return doctorMapper.toDto(newDoctor,MESSENGER.CREATE_DOCTOR);
	}

	@Override
	public List<DoctorDto> getlistDocSaveSpecializationWithSchedule(String nameSpecialization, LocalDate date) {
		Specializations specialization = specializationService.findByName(nameSpecialization);
		specialization.setView(specialization.getView() +1);
		specializationService.update(specialization);
		return dDao.listDoctorSpecializationWithSchedule(specialization.getName(),date);
	}
	
	


	@Override
	public DoctorDtoResponse getDoctorInfo(UserEntity entity) {
		return doctorMapper.toDto(entity.getDoctorEntity(),MESSENGER.DOCTOR_INFO);
	}

	@Override
	public DoctorWithSchedulesResponse getDoctorDtoWithScheduleDtoForAdmin(DoctorEntity doctor) {
		List<ScheduleDtoResponse> listSchdulesOfDoctor = scheduleService.getAllSchedulesByDocId(doctor);
		return docMapper.toDoctorDtoWithSchedulesDtoForAdmin(doctor, listSchdulesOfDoctor);	 
	}

	@Override
	public DoctorDtoResponse updateDoctor(String email, DoctorUpdateRequest request) {
		UserUpdateRequest userRequest = requestMapper.toUserEditRequest(request);
		UserEntity user = userService.findByEmail(email);
		DoctorEntity doc = user.getDoctorEntity();
		doc.setDescription(request.getDescription());
		doc.setAchievement(request.getAchievement());
		doc.setTrainingProcess(request.getTrainingProcess());
		doc.setUpdateAt(LocalDateTime.now());
		userService.update(user,userRequest);
	
		DoctorDtoResponse result = doctorMapper.toDto(doc,MESSENGER.UPDATE_INFO);
		
		return result;
	}
	
	@Override
	public String doctorSendEmail(String docEmail , UserEntity toUser, MultipartFile file) throws MessagingException, IOException  {
					
		UserEntity docUser = userService.findByEmail(docEmail);
		
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
		DoctorEntity doc = this.findById(id);
		String result="";
		UserEntity user = doc.getUser();
	
		if(user.getActive() == ACTIVE.NONE) {
			throw new ActiveException(MESSENGER_ERROR.CANT_LOCK);
		}else {
			doc.getUser().setActive(ACTIVE.NONE);
			result = MESSENGER.LOCKED_SUCCESS;
		}
		user.setDescription(reason);
		user.setUpdateAt(LocalDateTime.now());
		userService.update(user);
		
		
		return doctorMapper.toDto(doc, result);
	}

	@Override
	public DoctorDtoResponse unlockDoc(Integer id, String reason) {
		DoctorEntity doc = this.findById(id);
		String result="";
		UserEntity user = doc.getUser();
		if (user.getActive() == ACTIVE.ACCEPT) {
			throw new ActiveException(MESSENGER_ERROR.CANT_UNLOCK);
		}else {
			user.setActive(ACTIVE.ACCEPT);
			result = MESSENGER.UNLOCK_SUCCESS;
		}
		user.setDescription(reason);
		user.setUpdateAt(LocalDateTime.now());
		userService.update(user);

		
		logger.info(result);
		return doctorMapper.toDto(doc, result);
	}

	
	/*
	 * For testing
	 */

	@Override
	public List<DoctorEntity> getListDoctor(List<Integer> idsDoctor) {
		List<DoctorEntity> listDoctors = new ArrayList<>();
		for(Integer idDoctor : idsDoctor) {
			DoctorEntity doctorEntity = this.findById(idDoctor);
			if(doctorEntity.getUser().getActive() != 0) {
				listDoctors.add(doctorEntity);
			}
		}
		return listDoctors;
	}
}
